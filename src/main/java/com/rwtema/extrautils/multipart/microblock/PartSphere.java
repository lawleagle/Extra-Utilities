package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.Vertex5;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PartSphere extends PartMicroBlock {
  final double r = 0.375D;
  
  public static Cuboid6 bounds;
  
  public PartSphere(int materialID) {
    super(materialID);
    this.face = new BlockFaceUniformLighting();
    this.h = 1.35D;
    this.h2 = Math.sqrt(1.0D - this.h * this.h / 4.0D);
  }
  
  public Cuboid6 getBounds() {
    return new Cuboid6(0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);
  }
  
  public String getType() {
    return "extrautils:sphere";
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    if (isEthereal())
      return new ArrayList<Cuboid6>(); 
    return Arrays.asList(new Cuboid6[] { getBounds() });
  }
  
  public boolean hideCreativeTab() {
    return false;
  }
  
  public void reloadShape() {}
  
  @SideOnly(Side.CLIENT)
  public void render(Vector3 pos, int pass) {
    renderSphere(pos.copy().add(0.5D), pass, this.mat);
  }
  
  public int getMetadata() {
    return 3;
  }
  
  public TMultiPart newPart(boolean client) {
    return (TMultiPart)new PartSphere();
  }
  
  public TMultiPart placePart(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5, int materialID) {
    return (TMultiPart)new PartSphere(materialID);
  }
  
  public void registerPassThroughs() {}
  
  @SideOnly(Side.CLIENT)
  public void renderItem(ItemStack item, MicroMaterialRegistry.IMicroMaterial material) {
    Vector3 pos = new Vector3(0.5D, 0.5D, 0.5D);
    if (faces_inv == null) {
      faces_inv = (ArrayList)new ArrayList<Vertex5>();
      calcSphere(0.5D, 0.5D, faces_inv);
      LogHelper.debug("Calculated faces " + faces_inv.size(), new Object[0]);
    } 
    CCRenderState.setModel((CCRenderState.IVertexSource)this.face);
    for (Vertex5[] f : faces_inv) {
      this.face.lcComputed = false;
      this.face.verts[0].set(f[0]);
      this.face.verts[1].set(f[1]);
      this.face.verts[2].set(f[2]);
      this.face.verts[3].set(f[3]);
      this.face.side = (f[0]).uv.tex;
      material.renderMicroFace(pos, -1, getBounds());
    } 
  }
  
  public static final Vector3 DOWN = new Vector3(0.0D, -1.0D, 0.0D);
  
  public static final Vector3 UP = new Vector3(0.0D, 1.0D, 0.0D);
  
  public static final Vector3 NORTH = new Vector3(0.0D, 0.0D, -1.0D);
  
  public static final Vector3 SOUTH = new Vector3(0.0D, 0.0D, 1.0D);
  
  public static final Vector3 EAST = new Vector3(-1.0D, 0.0D, 0.0D);
  
  public static final Vector3 WEST = new Vector3(1.0D, 0.0D, 0.0D);
  
  public BlockFaceUniformLighting face;
  
  public PartSphere() {
    this.face = new BlockFaceUniformLighting();
    this.h = 1.35D;
    this.h2 = Math.sqrt(1.0D - this.h * this.h / 4.0D);
  }
  
  public class BlockFaceUniformLighting extends BlockRenderer.BlockFace {
    public void prepareVertex() {
      CCRenderState.side = 1;
    }
    
    public BlockRenderer.BlockFace computeLightCoords() {
      if (!this.lcComputed) {
        for (int i = 0; i < 4; i++)
          this.lightCoords[i].set(0, 1.0F, 0.0F, 0.0F, 0.0F); 
        this.lcComputed = true;
      } 
      return this;
    }
  }
  
  public static ArrayList<Vertex5[]> faces = null;
  
  public static ArrayList<Vertex5[]> faces_inv = null;
  
  double h;
  
  double h2;
  
  @SideOnly(Side.CLIENT)
  public void renderSphere(Vector3 pos, int pass, MicroMaterialRegistry.IMicroMaterial m) {
    if (faces == null) {
      faces = (ArrayList)new ArrayList<Vertex5>();
      calcSphere(0.25D, 0.25D, faces);
      LogHelper.debug("Calculated " + faces.size(), new Object[0]);
    } 
    CCRenderState.setModel((CCRenderState.IVertexSource)this.face);
    for (Vertex5[] f : faces) {
      this.face.verts[0].set(f[0]);
      this.face.verts[1].set(f[1]);
      this.face.verts[2].set(f[2]);
      this.face.verts[3].set(f[3]);
      this.face.side = (f[0]).uv.tex;
      m.renderMicroFace(pos, pass, getBounds());
    } 
  }
  
  public void calcSphere(double d, double d2, ArrayList<Vertex5[]> faces) {
    renderCurvedSide2(DOWN, NORTH, WEST, 0, d, d2, faces);
    renderCurvedSide2(UP, NORTH, EAST, 1, d, d2, faces);
    renderCurvedSide(NORTH, EAST, UP, 2, d, d2, faces);
    renderCurvedSide(SOUTH, WEST, UP, 3, d, d2, faces);
    renderCurvedSide(WEST, NORTH, UP, 4, d, d2, faces);
    renderCurvedSide(EAST, SOUTH, UP, 5, d, d2, faces);
  }
  
  public int getLightValue() {
    return 15;
  }
  
  public void renderCurvedSide(Vector3 forward, Vector3 left, Vector3 up, int side, double d, double d2, ArrayList<Vertex5[]> faces) {
    double u;
    for (u = 0.0D; u < 1.0D; u += d2) {
      double v;
      for (v = 0.0D; v < 1.0D; v += d) {
        Vertex5[] verts = { new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5() };
        calcVec1(verts[0], forward, left, up, u + d2, v, side);
        calcVec1(verts[1], forward, left, up, u + d2, v + d, side);
        calcVec1(verts[2], forward, left, up, u, v + d, side);
        calcVec1(verts[3], forward, left, up, u, v, side);
        faces.add(verts);
      } 
    } 
  }
  
  public void calcVec1(Vertex5 vert, Vector3 forward, Vector3 left, Vector3 up, double u, double v, int side) {
    double a = u - 0.5D;
    double dy = (v - 0.5D) * this.h;
    double dx = Math.sin(a * Math.PI / 2.0D) * Math.sqrt(1.0D - dy * dy);
    double dz = Math.sqrt(1.0D - dx * dx - dy * dy);
    vert.vec.set(left.copy().multiply(dx).add(up.copy().multiply(dy)).add(forward.copy().multiply(dz)).multiply(0.375D));
    vert.uv.set(u, 1.0D - v, side);
  }
  
  public void renderCurvedSide2(Vector3 forward, Vector3 left, Vector3 up, int side, double d, double d2, ArrayList<Vertex5[]> faces) {
    double t;
    for (t = 0.0D; t < 1.0D; t += d2 / 4.0D) {
      double dr;
      for (dr = 0.0D; dr < 1.0D; dr += d * 2.0D) {
        Vertex5[] verts = { new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5() };
        calcVec2(verts[0], forward, left, up, t, dr, side);
        calcVec2(verts[1], forward, left, up, t, dr + d * 2.0D, side);
        calcVec2(verts[2], forward, left, up, t + d2 / 4.0D, dr + d * 2.0D, side);
        calcVec2(verts[3], forward, left, up, t + d2 / 4.0D, dr, side);
        faces.add(verts);
      } 
    } 
  }
  
  public void calcVec2(Vertex5 vert, Vector3 forward, Vector3 left, Vector3 up, double t, double dr, int side) {
    double du = Math.cos(t * Math.PI * 2.0D) * dr;
    double dv = Math.sin(t * Math.PI * 2.0D) * dr;
    double d = (du == 0.0D || dv == 0.0D) ? 0.0D : Math.min(Math.abs(du / dv), Math.abs(dv / du));
    d = Math.sqrt(1.0D + d * d);
    double dx = du * this.h2;
    double dy = dv * this.h2;
    double dz = Math.sqrt(1.0D - dx * dx - dy * dy);
    vert.vec.set(left.copy().multiply(dx).add(up.copy().multiply(dy)).add(forward.copy().multiply(dz)).multiply(0.375D));
    vert.uv.set((1.0D + d * du) / 2.0D, (1.0D + d * dv) / 2.0D, side);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartSphere.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */