package com.rwtema.extrautils.particle;

import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class ParticleTransferNodes extends EntityFX {
  float baseScale;
  
  public static Random rand = (Random)XURandom.getInstance();
  
  float[][] col;
  
  public static double offset() {
    return rand.nextGaussian() * 0.0125D;
  }
  
  public int getFXLayer() {
    return 1;
  }
  
  public ParticleTransferNodes(World par1World, double par2, double par4, double par6, double r, double g, double b) {
    super(par1World, par2 + offset(), par4 + offset(), par6 + offset(), 0.0D, 0.0D, 0.0D);
    this.col = new float[8][3];
    this.particleIcon = BlockTransferNode.particle;
    this.motionX *= 0.2D;
    this.motionY *= 0.2D;
    this.motionZ *= 0.2D;
    for (int i = 0; i < 8; i++) {
      float f4 = (float)Math.random() * 0.4F + 0.6F;
      this.particleRed = this.col[i][0] = (float)((Math.random() * 0.20000000298023224D + 0.800000011920929D) * r * f4);
      this.particleGreen = this.col[i][1] = (float)((Math.random() * 0.20000000298023224D + 0.800000011920929D) * g * f4);
      this.particleBlue = this.col[i][2] = (float)((Math.random() * 0.20000000298023224D + 0.800000011920929D) * b * f4);
    } 
    this.particleScale = 0.5F;
    this.baseScale = this.particleScale;
    this.particleMaxAge = (int)(10.0D / (Math.random() * 0.2D + 0.6D));
    this.noClip = true;
  }
  
  private static final int[][] dirs = new int[][] { { -1, -1, -1 }, { -1, -1, 1 }, { 1, -1, -1 }, { 1, -1, 1 }, { -1, 1, -1 }, { -1, 1, 1 }, { 1, 1, -1 }, { 1, 1, 1 } };
  
  public void renderParticle(Tessellator par1Tessellator, float x, float y, float z, float r, float g, float b) {
    float f6 = 1.0F - (this.particleAge + x) / this.particleMaxAge;
    if (f6 < 0.0F)
      f6 = 0.0F; 
    if (f6 > 1.0F)
      f6 = 1.0F; 
    this.particleScale = this.baseScale * (1.0F - f6 * 0.5F) * Math.min(1.0F, 2.0F * f6);
    float h = 0.125F + (1.0F - f6) * 0.075F;
    for (int i = 0; i < 8; i++)
      renderParticles(par1Tessellator, x, y, z, r, g, b, h * dirs[i][0], h * dirs[i][1], h * dirs[i][2], this.col[i]); 
  }
  
  public void renderParticles(Tessellator par1Tessellator, float x, float y, float z, float r, float g, float b, float dx, float dy, float dz, float[] cols) {
    float f6 = this.particleTextureIndexX / 16.0F;
    float f7 = f6 + 0.0624375F;
    float f8 = this.particleTextureIndexY / 16.0F;
    float f9 = f8 + 0.0624375F;
    float f10 = 0.1F * this.particleScale;
    if (this.particleIcon != null) {
      f6 = this.particleIcon.getMinU();
      f7 = this.particleIcon.getMaxU();
      f8 = this.particleIcon.getMinV();
      f9 = this.particleIcon.getMaxV();
    } 
    double f11 = ((float)(this.prevPosX + (this.posX - this.prevPosX) * x - interpPosX) + dx);
    double f12 = ((float)(this.prevPosY + (this.posY - this.prevPosY) * x - interpPosY) + dy);
    double f13 = ((float)(this.prevPosZ + (this.posZ - this.prevPosZ) * x - interpPosZ) + dz);
    par1Tessellator.setColorRGBA_F(cols[0], cols[1], cols[2], this.particleAlpha);
    par1Tessellator.addVertexWithUV(f11 - (y * f10) - (g * f10), f12 - (z * f10), f13 - (r * f10) - (b * f10), f7, f9);
    par1Tessellator.addVertexWithUV(f11 - (y * f10) + (g * f10), f12 + (z * f10), f13 - (r * f10) + (b * f10), f7, f8);
    par1Tessellator.addVertexWithUV(f11 + (y * f10) + (g * f10), f12 + (z * f10), f13 + (r * f10) + (b * f10), f6, f8);
    par1Tessellator.addVertexWithUV(f11 + (y * f10) - (g * f10), f12 - (z * f10), f13 + (r * f10) - (b * f10), f6, f9);
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.particleAge++ >= this.particleMaxAge)
      setDead(); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\ParticleTransferNodes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */