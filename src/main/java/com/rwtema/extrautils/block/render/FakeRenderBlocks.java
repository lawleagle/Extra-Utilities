package com.rwtema.extrautils.block.render;

import cofh.api.block.IBlockAppearance;
import com.rwtema.extrautils.block.IconConnectedTexture;
import com.rwtema.extrautils.block.IconConnectedTextureFlipped;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class FakeRenderBlocks extends RenderBlocks {
  public static final double[] u = new double[] { -1.0D, 1.0D, 1.0D, -1.0D };
  
  public static final double[] v = new double[] { 1.0D, 1.0D, -1.0D, -1.0D };
  
  public Block curBlock = null;
  
  public int curMeta = 0;
  
  public boolean isOpaque = false;
  
  public void setWorld(IBlockAccess blockAccess) {
    this.blockAccess = blockAccess;
  }
  
  public void setLightAndColor(double u2, double v2, int side) {
    if (this.enableAO) {
      Tessellator t = Tessellator.instance;
      double u = 0.0D, v = 0.0D;
      if (side == 0 || side == 1) {
        u = 1.0D - u2;
        v = 1.0D - v2;
      } else if (side == 2) {
        u = v2;
        v = 1.0D - u2;
      } else if (side == 3) {
        u = u2;
        v = v2;
      } else if (side == 4) {
        u = v2;
        v = 1.0D - u2;
      } else if (side == 5) {
        u = 1.0D - v2;
        v = u2;
      } 
      t.setBrightness(mixAoBrightness(this.brightnessTopLeft, this.brightnessTopRight, this.brightnessBottomLeft, this.brightnessBottomRight, u * v, v * (1.0D - u), (1.0D - v) * u, (1.0D - u) * (1.0D - v)));
      t.setColorOpaque_F(mix(this.colorRedTopLeft, this.colorRedTopRight, this.colorRedBottomLeft, this.colorRedBottomRight, u, v), mix(this.colorGreenTopLeft, this.colorGreenTopRight, this.colorGreenBottomLeft, this.colorGreenBottomRight, u, v), mix(this.colorBlueTopLeft, this.colorBlueTopRight, this.colorBlueBottomLeft, this.colorBlueBottomRight, u, v));
    } 
  }
  
  public float mix(double tl, double tr, double bl, double br, double u, double v) {
    return (float)(tl * u * v + tr * (1.0D - u) * v + bl * u * (1.0D - v) + br * (1.0D - u) * (1.0D - v));
  }
  
  public void renderSide(Block block, double x, double y, double z, double ox, double oy, double oz, int ax, int ay, int az, int bx, int by, int bz, IconConnectedTexture icon, int side, double rx, double ry, double rz) {
    Tessellator t = Tessellator.instance;
    byte[] i = new byte[4];
    this.isOpaque = block.isOpaqueCube();
    boolean areSame = true;
    int j;
    for (j = 0; j < 4; j++) {
      i[j] = getType(block, side, (int)x, (int)y, (int)z, ax * (int)u[j], ay * (int)u[j], az * (int)u[j], bx * (int)v[j], by * (int)v[j], bz * (int)v[j], (int)(ox * 2.0D - 1.0D), (int)(oy * 2.0D - 1.0D), (int)(oz * 2.0D - 1.0D));
      if (areSame && j > 0 && i[j] != i[0])
        areSame = false; 
    } 
    if (areSame) {
      icon.setType(i[0]);
      for (j = 0; j < 4; j++) {
        double cx = x + rx + ox + u[j] * ax * 0.5D + v[j] * bx * 0.5D;
        double cy = y + ry + oy + u[j] * ay * 0.5D + v[j] * by * 0.5D;
        double cz = z + rz + oz + u[j] * az * 0.5D + v[j] * bz * 0.5D;
        setLightAndColor(0.5D + u[j] * 0.5D, 0.5D + v[j] * 0.5D, side);
        t.addVertexWithUV(cx, cy, cz, icon.getInterpolatedU(16.0D - 8.0D + u[j] * 8.0D), icon.getInterpolatedV(16.0D - 8.0D + v[j] * 8.0D));
      } 
      icon.resetType();
      return;
    } 
    for (j = 0; j < 4; j++) {
      icon.setType(i[j]);
      double cx = x + rx + ox + ax * u[j] / 4.0D + bx * v[j] / 4.0D;
      double cy = y + ry + oy + ay * u[j] / 4.0D + by * v[j] / 4.0D;
      double cz = z + rz + oz + az * u[j] / 4.0D + bz * v[j] / 4.0D;
      for (int k = 0; k < 4; k++) {
        setLightAndColor(0.5D + u[j] * 0.25D + u[k] * 0.25D, 0.5D + v[j] * 0.25D + v[k] * 0.25D, side);
        t.addVertexWithUV(cx + u[k] * ax * 0.25D + v[k] * bx * 0.25D, cy + u[k] * ay * 0.25D + v[k] * by * 0.25D, cz + u[k] * az * 0.25D + v[k] * bz * 0.25D, icon.getInterpolatedU(16.0D - 8.0D + u[j] * 4.0D + u[k] * 4.0D), icon.getInterpolatedV(16.0D - 8.0D + v[j] * 4.0D + v[k] * 4.0D));
      } 
      icon.resetType();
    } 
  }
  
  public int getSideFromDir(int dx, int dy, int dz) {
    if (dy < 0)
      return 0; 
    if (dy > 0)
      return 1; 
    if (dz < 0)
      return 2; 
    if (dz > 0)
      return 3; 
    if (dx < 0)
      return 4; 
    if (dx > 0)
      return 5; 
    return 0;
  }
  
  public boolean matchBlock(int side2, int x2, int y2, int z2) {
    Block block = this.blockAccess.getBlock(x2, y2, z2);
    if (block == this.curBlock)
      return (this.curMeta == this.blockAccess.getBlockMetadata(x2, y2, z2)); 
    if (block instanceof IBlockAppearance) {
      IBlockAppearance block1 = (IBlockAppearance)block;
      return (block1.supportsVisualConnections() && this.curBlock == block1.getVisualBlock(this.blockAccess, x2, y2, z2, ForgeDirection.getOrientation(side2)) && this.curMeta == block1.getVisualMeta(this.blockAccess, x2, y2, z2, ForgeDirection.getOrientation(side2)));
    } 
    return false;
  }
  
  public byte getType(Block block, int side, int x, int y, int z, int ax, int ay, int az, int bx, int by, int bz, int cx, int cy, int cz) {
    int sidea = getSideFromDir(ax, ay, az);
    int sideb = getSideFromDir(bx, by, bz);
    boolean a = (matchBlock(side, x + ax, y + ay, z + az) && !matchBlock(sidea, x + cx, y + cy, z + cz) && !matchBlock(Facing.oppositeSide[sidea], x + ax + cx, y + ay + cy, z + az + cz));
    boolean b = (matchBlock(side, x + bx, y + by, z + bz) && !matchBlock(sideb, x + cx, y + cy, z + cz) && !matchBlock(Facing.oppositeSide[sideb], x + bx + cx, y + by + cy, z + bz + cz));
    if (a) {
      if (b) {
        if (matchBlock(side, x + ax + bx, y + ay + by, z + az + bz)) {
          if (matchBlock(Facing.oppositeSide[sidea], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz) || matchBlock(Facing.oppositeSide[sideb], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz) || matchBlock(sidea, x + bx + cx, y + by + cy, z + bz + cz) || matchBlock(sideb, x + ax + cx, y + ay + cy, z + az + cz))
            return 4; 
          return 3;
        } 
        return 4;
      } 
      return 2;
    } 
    if (b)
      return 1; 
    return 0;
  }
  
  public void renderFaceYNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.0D, 0.5D, -1, 0, 0, 0, 0, 1, (IconConnectedTexture)new IconConnectedTextureFlipped((IconConnectedTexture)iIcon), 0, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceYNeg(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceYPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 1.0D, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)iIcon, 1, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceYPos(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceZNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.5D, 0.0D, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)iIcon, 2, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceZNeg(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceZPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.5D, 1.0D, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)iIcon, 3, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceZPos(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceXNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.0D, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)iIcon, 4, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceXNeg(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceXPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 1.0D, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)iIcon, 5, 0.0D, 0.0D, 0.0D);
    } else {
      super.renderFaceXPos(block, x, y, z, iIcon);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\FakeRenderBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */