package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.block.IconConnectedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class FakeRenderEtherealBlocks extends FakeRenderBlocks {
  private static final double h = 0.005D;
  
  private static final float darken = 0.75F;
  
  public void setLightAndColor(double u2, double v2, int side) {}
  
  public boolean renderStandardBlock(Block p_147784_1_, int p_147784_2_, int p_147784_3_, int p_147784_4_) {
    int l = p_147784_1_.colorMultiplier(this.blockAccess, p_147784_2_, p_147784_3_, p_147784_4_);
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    if (EntityRenderer.anaglyphEnable) {
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    } 
    f *= 0.75F;
    f1 *= 0.75F;
    f2 *= 0.75F;
    return renderStandardBlockWithColorMultiplier(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2);
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
      double cx = x + rx + ox;
      double cy = y + ry + oy;
      double cz = z + rz + oz;
      for (int k = 3; k >= 0; k--) {
        setLightAndColor(0.5D + u[k] * 0.5D, 0.5D + v[k] * 0.5D, side);
        t.addVertexWithUV(cx + u[k] * ax * 0.5D + v[k] * bx * 0.5D, cy + u[k] * ay * 0.5D + v[k] * by * 0.5D, cz + u[k] * az * 0.5D + v[k] * bz * 0.5D, icon.getInterpolatedU(16.0D - 8.0D + u[k] * 8.0D), icon.getInterpolatedV(16.0D - 8.0D + v[k] * 8.0D));
      } 
      icon.resetType();
      return;
    } 
    for (j = 0; j < 4; j++) {
      icon.setType(i[j]);
      double cx = x + rx + ox + ax * u[j] / 4.0D + bx * v[j] / 4.0D;
      double cy = y + ry + oy + ay * u[j] / 4.0D + by * v[j] / 4.0D;
      double cz = z + rz + oz + az * u[j] / 4.0D + bz * v[j] / 4.0D;
      for (int k = 3; k >= 0; k--) {
        setLightAndColor(0.5D + u[j] * 0.25D + u[k] * 0.25D, 0.5D + v[j] * 0.25D + v[k] * 0.25D, side);
        t.addVertexWithUV(cx + u[k] * ax * 0.25D + v[k] * bx * 0.25D, cy + u[k] * ay * 0.25D + v[k] * by * 0.25D, cz + u[k] * az * 0.25D + v[k] * bz * 0.25D, icon.getInterpolatedU(16.0D - 8.0D + u[j] * 4.0D + u[k] * 4.0D), icon.getInterpolatedV(16.0D - 8.0D + v[j] * 4.0D + v[k] * 4.0D));
      } 
      icon.resetType();
    } 
  }
  
  public void renderFaceYNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.0D, 0.5D, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)iIcon, 0, 0.0D, 0.005D, 0.0D);
    } else {
      super.renderFaceYPos(block, x, y - 1.0D + 0.005D, z, iIcon);
    } 
  }
  
  public void renderFaceYPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 1.0D, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)iIcon, 1, 0.0D, -0.005D, 0.0D);
    } else {
      super.renderFaceYNeg(block, x, y + 1.0D - 0.005D, z, iIcon);
    } 
  }
  
  public void renderFaceZNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.5D, 0.0D, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)iIcon, 2, 0.0D, 0.0D, 0.005D);
    } else {
      super.renderFaceZPos(block, x, y, z - 1.0D + 0.005D, iIcon);
    } 
  }
  
  public void renderFaceZPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.5D, 0.5D, 1.0D, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)iIcon, 3, 0.0D, 0.0D, -0.005D);
    } else {
      super.renderFaceZNeg(block, x, y, z + 1.0D - 0.005D, iIcon);
    } 
  }
  
  public void renderFaceXNeg(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 0.0D, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)iIcon, 4, 0.005D, 0.0D, 0.0D);
    } else {
      super.renderFaceXPos(block, x, y, z, iIcon);
    } 
  }
  
  public void renderFaceXPos(Block block, double x, double y, double z, IIcon iIcon) {
    if (hasOverrideBlockTexture())
      iIcon = this.overrideBlockTexture; 
    if (iIcon instanceof IconConnectedTexture) {
      renderSide(block, x, y, z, 1.0D, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)iIcon, 5, -0.005D, 0.0D, 0.0D);
    } else {
      super.renderFaceXNeg(block, x + 1.0D - 0.005D, y, z, iIcon);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\FakeRenderEtherealBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */