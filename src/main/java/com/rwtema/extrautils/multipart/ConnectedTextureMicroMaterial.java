package com.rwtema.extrautils.multipart;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.Vertex5;
import codechicken.lib.render.uv.MultiIconTransformation;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.rwtema.extrautils.block.BlockDecoration;
import com.rwtema.extrautils.block.IconConnectedTexture;
import com.rwtema.extrautils.block.render.FakeRenderBlocks;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTextures;
import com.rwtema.extrautils.helper.XUHelperClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.collection.Iterator;

public class ConnectedTextureMicroMaterial extends BlockMicroMaterial {
  public static final double[] u = new double[] { -1.0D, 1.0D, 1.0D, -1.0D };
  
  public static final double[] v = new double[] { 1.0D, 1.0D, -1.0D, -1.0D };
  
  public boolean isGlass = true;
  
  public boolean[] isConnected;
  
  public boolean hasConnected;
  
  private int pass;
  
  private World world;
  
  int[] cols;
  
  @SideOnly(Side.CLIENT)
  IconConnectedTexture resetIcons;
  
  public ConnectedTextureMicroMaterial(Block block, int meta) {
    super(block, meta);
    this.cols = new int[] { 269488383, -1, 269549567, 285151487, -15724289, -15663105, -61185 };
    if (block instanceof BlockDecoration) {
      this.isGlass = !((BlockDecoration)block).opaque[meta()];
      this.isConnected = ((BlockDecoration)block).ctexture[meta()];
      for (boolean b : this.isConnected) {
        if (b) {
          this.hasConnected = true;
          break;
        } 
      } 
    } else {
      this.isGlass = !block.isOpaqueCube();
      this.isConnected = new boolean[6];
      this.hasConnected = false;
    } 
  }
  
  public int getLightValue() {
    if (block() instanceof BlockDecoration)
      return ((BlockDecoration)block()).light[meta()]; 
    return 0;
  }
  
  @SideOnly(Side.CLIENT)
  public TMultiPart getPart(Vector3 pos, Cuboid6 bounds) {
    World world = XUHelperClient.clientWorld();
    TileMultipart t = TileMultipart.getOrConvertTile(world, new BlockCoord(pos));
    if (t == null)
      return null; 
    for (TMultiPart part : t.jPartList()) {
      if (part instanceof JNormalOcclusion)
        for (Cuboid6 bound : ((JNormalOcclusion)part).getOcclusionBoxes()) {
          if (bound.intersects(bounds))
            return part; 
        }  
    } 
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public void renderMicroFace(Vector3 pos, int pass, Cuboid6 b) {
    if (!CCRenderState.model.getClass().equals(BlockRenderer.BlockFace.class)) {
      super.renderMicroFace(pos, pass, b);
      return;
    } 
    Cuboid6 bounds = b.copy();
    Cuboid6 renderBounds = bounds.copy();
    TMultiPart part = getPart(pos, bounds);
    if (pass >= 0) {
      boolean isHollow = part instanceof codechicken.microblock.HollowMicroblockClient;
      int s = ((BlockRenderer.BlockFace)CCRenderState.model).side;
      if (isHollow)
        for (Cuboid6 b2 : ((JNormalOcclusion)part).getOcclusionBoxes())
          bounds.enclose(b2);  
      if (this.isGlass)
        glassChange(bounds); 
    } 
    if (!this.hasConnected || !renderConnected(pos, pass, bounds, renderBounds))
      super.renderMicroFace(pos, pass, bounds); 
    if (this.resetIcons != null)
      this.resetIcons.resetType(); 
  }
  
  @SideOnly(Side.CLIENT)
  public void glassChange(Cuboid6 c) {
    double u1, u2, v1, v2;
    BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
    int side = face.side;
    double x1 = c.min.x, x2 = c.max.x;
    double y1 = c.min.y, y2 = c.max.y;
    double z1 = c.min.z, z2 = c.max.z;
    switch (side) {
      case 0:
        u1 = x1;
        u2 = x2;
        v1 = z1;
        v2 = z2;
        break;
      case 1:
        u1 = x1;
        u2 = x2;
        v1 = z1;
        v2 = z2;
        break;
      case 2:
        u1 = 1.0D - x2;
        u2 = 1.0D - x1;
        v1 = 1.0D - y2;
        v2 = 1.0D - y1;
        break;
      case 3:
        u1 = x1;
        u2 = x2;
        v1 = 1.0D - y2;
        v2 = 1.0D - y1;
        break;
      case 4:
        u1 = z1;
        v1 = 1.0D - y2;
        u2 = z2;
        v2 = 1.0D - y1;
        break;
      case 5:
        u1 = 1.0D - z2;
        u2 = 1.0D - z1;
        v1 = 1.0D - y2;
        v2 = 1.0D - y1;
        break;
      default:
        return;
    } 
    if (v1 == v2 || u1 == u2)
      return; 
    for (Vertex5 v : face.verts) {
      v.uv.u = (v.uv.u - u1) / (u2 - u1);
      v.uv.v = (v.uv.v - v1) / (v2 - v1);
    } 
    face.lcComputed = false;
    face.computeLightCoords();
  }
  
  public boolean isInt(double t) {
    return (t == (int)t);
  }
  
  @SideOnly(Side.CLIENT)
  public boolean renderConnected(Vector3 pos, int pass, Cuboid6 bounds, Cuboid6 renderBounds) {
    this.pass = pass;
    if (pass < 0)
      return false; 
    if (!isInt(pos.x) || !isInt(pos.y) || !isInt(pos.z))
      return false; 
    if (!CCRenderState.model.getClass().equals(BlockRenderer.BlockFace.class))
      return false; 
    if (!isFlush(bounds))
      return false; 
    int s = getSideFromBounds(bounds);
    BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
    int side = face.side;
    if (!this.isConnected[side])
      return false; 
    if (s == -1)
      s = side; 
    IIcon icon = (icont()).icons[side];
    if (!(icon instanceof IconConnectedTexture))
      return false; 
    this.world = XUHelperClient.clientPlayer().getEntityWorld();
    if (s == side) {
      int c = getColour(pass);
      if (this.isGlass) {
        double h = 0.001D;
        switch (s) {
          case 0:
            renderHalfSide(block(), 0.5D, h, 0.5D, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
          case 1:
            renderHalfSide(block(), 0.5D, 1.0D - h, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
          case 2:
            renderHalfSide(block(), 0.5D, 0.5D, h, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
          case 3:
            renderHalfSide(block(), 0.5D, 0.5D, 1.0D - h, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
          case 4:
            renderHalfSide(block(), h, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
          case 5:
            renderHalfSide(block(), 1.0D - h, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
            break;
        } 
      } else {
        FakeRenderBlocks fr = RenderBlockConnectedTextures.fakeRender;
        fr.setWorld((IBlockAccess)this.world);
        fr.curBlock = block();
        fr.curMeta = meta();
        switch (s) {
          case 0:
            renderSide(block(), 0.5D, 0.0D, 0.5D, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
          case 1:
            renderSide(block(), 0.5D, 1.0D, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
          case 2:
            renderSide(block(), 0.5D, 0.5D, 0.0D, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
          case 3:
            renderSide(block(), 0.5D, 0.5D, 1.0D, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
          case 4:
            renderSide(block(), 0.0D, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
          case 5:
            renderSide(block(), 1.0D, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, icont(), renderBounds, bounds);
            break;
        } 
      } 
      return true;
    } 
    if (side == Facing.oppositeSide[s]) {
      double h = sideSize(bounds);
      switch (Facing.oppositeSide[s]) {
        case 0:
          renderHalfSide(block(), 0.5D, h, 0.5D, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
        case 1:
          renderHalfSide(block(), 0.5D, h, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
        case 2:
          renderHalfSide(block(), 0.5D, 0.5D, h, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
        case 3:
          renderHalfSide(block(), 0.5D, 0.5D, h, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
        case 4:
          renderHalfSide(block(), h, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
        case 5:
          renderHalfSide(block(), h, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
          break;
      } 
      return true;
    } 
    if (this.isGlass) {
      double d = renderBounds.getSide(side);
      return ((d == 0.0D || d == 1.0D) && hasMatchingPart(bounds, (int)pos.x + Facing.offsetsXForSide[side], (int)pos.y + Facing.offsetsYForSide[side], (int)pos.z + Facing.offsetsZForSide[side]));
    } 
    return false;
  }
  
  public double dist(Vector3 a, Vector3 b) {
    return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
  }
  
  public boolean isFlush(Cuboid6 bounds) {
    int i = 0;
    if (bounds.max.y != 1.0D)
      i++; 
    if (bounds.min.y != 0.0D)
      i++; 
    if (bounds.max.z != 1.0D)
      i++; 
    if (bounds.min.z != 0.0D)
      i++; 
    if (bounds.max.x != 1.0D)
      i++; 
    if (bounds.min.x != 0.0D)
      i++; 
    return (i <= 1);
  }
  
  public double sideSize(Cuboid6 bounds) {
    if (bounds.max.y != 1.0D)
      return bounds.max.y; 
    if (bounds.min.y != 0.0D)
      return bounds.min.y; 
    if (bounds.max.z != 1.0D)
      return bounds.max.z; 
    if (bounds.min.z != 0.0D)
      return bounds.min.z; 
    if (bounds.max.x != 1.0D)
      return bounds.max.x; 
    if (bounds.min.x != 0.0D)
      return bounds.min.x; 
    return 0.0D;
  }
  
  public int getSideFromBounds(Cuboid6 bounds) {
    if (bounds.max.y != 1.0D)
      return 0; 
    if (bounds.min.y != 0.0D)
      return 1; 
    if (bounds.max.z != 1.0D)
      return 2; 
    if (bounds.min.z != 0.0D)
      return 3; 
    if (bounds.max.x != 1.0D)
      return 4; 
    if (bounds.min.x != 0.0D)
      return 5; 
    return -1;
  }
  
  public boolean isTransparent() {
    return this.isGlass;
  }
  
  @SideOnly(Side.CLIENT)
  public void renderSide(Block block, double ox, double oy, double oz, int ax, int ay, int az, int bx, int by, int bz, IconConnectedTexture icon, int side, Vector3 pos, int colour, MultiIconTransformation icont, Cuboid6 renderBounds, Cuboid6 bounds) {
    int[] tex = new int[4];
    boolean isDifferent = false;
    for (int j = 0; j < 4; j++) {
      RenderBlockConnectedTextures.fakeRender.isOpaque = !this.isGlass;
      tex[j] = RenderBlockConnectedTextures.fakeRender.getType(block, side, (int)pos.x, (int)pos.y, (int)pos.z, ax * (int)u[j], ay * (int)u[j], az * (int)u[j], bx * (int)v[j], by * (int)v[j], bz * (int)v[j], (int)(ox * 2.0D - 1.0D), (int)(oy * 2.0D - 1.0D), (int)(oz * 2.0D - 1.0D));
      if (tex[j] != tex[0])
        isDifferent = true; 
    } 
    BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
    face.lcComputed = false;
    if (isDifferent) {
      for (int i = 0; i < 4; i++) {
        double cx = ox + ax * u[i] / 4.0D + bx * v[i] / 4.0D;
        double cy = oy + ay * u[i] / 4.0D + by * v[i] / 4.0D;
        double cz = oz + az * u[i] / 4.0D + bz * v[i] / 4.0D;
        Cuboid6 b = new Cuboid6(cx, cy, cz, cx, cy, cz);
        b.setSide(side, bounds.getSide(side));
        b.setSide(Facing.oppositeSide[side], bounds.getSide(Facing.oppositeSide[side]));
        for (int k = 0; k < 4; k++)
          expandToInclude(b, new Vector3(cx + u[k] * ax * 0.25D + v[k] * bx * 0.25D, cy + u[k] * ay * 0.25D + v[k] * by * 0.25D, cz + u[k] * az * 0.25D + v[k] * bz * 0.25D)); 
        b = shrinkToEnclose(b.copy(), renderBounds);
        if (!isEmpty(b)) {
          if (this.isGlass) {
            for (int m = 0; m < 4; m++)
              face.lightCoords[m].computeO((face.verts[m]).vec, side); 
            face.lcComputed = true;
          } else {
            face.lcComputed = false;
          } 
          for (IIcon ic : (icont()).icons) {
            if (ic instanceof IconConnectedTexture)
              ((IconConnectedTexture)ic).setType(tex[i]); 
          } 
          face.loadCuboidFace(b, side);
          super.renderMicroFace(pos, this.pass, b);
          for (IIcon ic : (icont()).icons) {
            if (ic instanceof IconConnectedTexture)
              ((IconConnectedTexture)ic).resetType(); 
          } 
        } 
      } 
    } else {
      bounds = shrinkToEnclose(bounds.copy(), renderBounds);
      if (isEmpty(bounds))
        return; 
      face.loadCuboidFace(bounds, side);
      for (IIcon ic : (icont()).icons) {
        if (ic instanceof IconConnectedTexture)
          ((IconConnectedTexture)ic).setType(tex[0]); 
      } 
      super.renderMicroFace(pos, this.pass, bounds);
      for (IIcon ic : (icont()).icons) {
        if (ic instanceof IconConnectedTexture)
          ((IconConnectedTexture)ic).resetType(); 
      } 
    } 
  }
  
  private double interp(double v) {
    return v / 16.0D;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasMatchingPart(Cuboid6 part, int x, int y, int z) {
    TileEntity tile_base = this.world.getTileEntity(x, y, z);
    if (tile_base != null && tile_base instanceof TileMultipart) {
      Iterator<TMultiPart> parts = ((TileMultipart)tile_base).partList().toIterator();
      while (parts.hasNext()) {
        TMultiPart p = (TMultiPart)parts.next();
        if (p instanceof CommonMicroblock) {
          CommonMicroblock mat = (CommonMicroblock)p;
          if (equalCubes(mat.getBounds(), part)) {
            int material = mat.getMaterial();
            if (MicroMaterialRegistry.getMaterial(material) == this)
              return true; 
          } 
        } 
      } 
    } 
    return false;
  }
  
  public boolean equalCubes(Cuboid6 a, Cuboid6 b) {
    return (getSideFromBounds(a) == getSideFromBounds(b) && sideSize(a) == sideSize(b));
  }
  
  @SideOnly(Side.CLIENT)
  public int getHalfType(Block block, int side, int x, int y, int z, int ax, int ay, int az, int bx, int by, int bz, Cuboid6 part) {
    boolean a = hasMatchingPart(part, x + ax, y + ay, z + az);
    boolean b = hasMatchingPart(part, x + bx, y + by, z + bz);
    if (a) {
      if (b) {
        if (hasMatchingPart(part, x + ax + bx, y + ay + by, z + az + bz))
          return 3; 
        return 4;
      } 
      return 2;
    } 
    if (b)
      return 1; 
    return 0;
  }
  
  public Cuboid6 getBounds(Cuboid6 b, int side) {
    return b;
  }
  
  public Cuboid6 expandToInclude(Cuboid6 a, Vector3 v) {
    if (a.min.x > v.x)
      a.min.x = v.x; 
    if (a.min.y > v.y)
      a.min.y = v.y; 
    if (a.min.z > v.z)
      a.min.z = v.z; 
    if (a.max.y < v.y)
      a.max.y = v.y; 
    if (a.max.x < v.x)
      a.max.x = v.x; 
    if (a.max.z < v.z)
      a.max.z = v.z; 
    return a;
  }
  
  public boolean isEmpty(Cuboid6 a) {
    return (a.min.x >= a.max.x || a.min.y >= a.max.y || a.min.z >= a.max.z);
  }
  
  public Cuboid6 shrinkToEnclose(Cuboid6 a, Cuboid6 c) {
    if (a.min.x < c.min.x)
      a.min.x = c.min.x; 
    if (a.min.y < c.min.y)
      a.min.y = c.min.y; 
    if (a.min.z < c.min.z)
      a.min.z = c.min.z; 
    if (a.max.x > c.max.x)
      a.max.x = c.max.x; 
    if (a.max.y > c.max.y)
      a.max.y = c.max.y; 
    if (a.max.z > c.max.z)
      a.max.z = c.max.z; 
    return a;
  }
  
  @SideOnly(Side.CLIENT)
  public void renderHalfSide(Block block, double ox, double oy, double oz, int ax, int ay, int az, int bx, int by, int bz, IconConnectedTexture icon, Cuboid6 bounds, int side, Vector3 pos, Cuboid6 renderBounds) {
    int[] tex = new int[4];
    boolean isDifferent = false;
    int s = Facing.oppositeSide[getSideFromBounds(bounds)];
    for (int j = 0; j < 4; j++) {
      RenderBlockConnectedTextures.fakeRender.isOpaque = !this.isGlass;
      tex[j] = getHalfType(block, side, (int)pos.x, (int)pos.y, (int)pos.z, ax * (int)u[j], ay * (int)u[j], az * (int)u[j], bx * (int)v[j], by * (int)v[j], bz * (int)v[j], bounds);
      if (tex[j] != tex[0])
        isDifferent = true; 
    } 
    BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
    face.lcComputed = false;
    if (isDifferent) {
      s = s;
      for (int i = 0; i < 4; i++) {
        double cx = ox + ax * u[i] / 4.0D + bx * v[i] / 4.0D;
        double cy = oy + ay * u[i] / 4.0D + by * v[i] / 4.0D;
        double cz = oz + az * u[i] / 4.0D + bz * v[i] / 4.0D;
        Cuboid6 b = new Cuboid6(cx, cy, cz, cx, cy, cz);
        b.setSide(face.side, bounds.getSide(face.side));
        b.setSide(Facing.oppositeSide[face.side], bounds.getSide(Facing.oppositeSide[face.side]));
        for (int k = 0; k < 4; k++)
          expandToInclude(b, new Vector3(cx + u[k] * ax * 0.25D + v[k] * bx * 0.25D, cy + u[k] * ay * 0.25D + v[k] * by * 0.25D, cz + u[k] * az * 0.25D + v[k] * bz * 0.25D)); 
        b = shrinkToEnclose(b.copy(), renderBounds);
        if (!isEmpty(b)) {
          if (this.isGlass) {
            for (int m = 0; m < 4; m++)
              face.lightCoords[m].computeO((face.verts[m]).vec, side); 
            face.lcComputed = true;
          } else {
            face.lcComputed = false;
          } 
          for (IIcon ic : (icont()).icons) {
            if (ic instanceof IconConnectedTexture)
              ((IconConnectedTexture)ic).setType(tex[i]); 
          } 
          face.loadCuboidFace(b, face.side);
          super.renderMicroFace(pos, this.pass, b);
          for (IIcon ic : (icont()).icons) {
            if (ic instanceof IconConnectedTexture)
              ((IconConnectedTexture)ic).resetType(); 
          } 
        } 
      } 
    } else {
      bounds = shrinkToEnclose(bounds.copy(), renderBounds);
      if (isEmpty(bounds))
        return; 
      face.loadCuboidFace(bounds, side);
      for (IIcon ic : (icont()).icons) {
        if (ic instanceof IconConnectedTexture)
          ((IconConnectedTexture)ic).setType(tex[0]); 
      } 
      super.renderMicroFace(pos, this.pass, bounds);
      for (IIcon ic : (icont()).icons) {
        if (ic instanceof IconConnectedTexture)
          ((IconConnectedTexture)ic).resetType(); 
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\ConnectedTextureMicroMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */