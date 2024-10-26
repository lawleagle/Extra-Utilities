package com.rwtema.extrautils.particle;

import com.rwtema.extrautils.block.BlockPortal;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class ParticlePortal extends EntityFX {
  public final double startX;
  
  public final double startY;
  
  public final double startZ;
  
  public ParticlePortal(World world, double x, double y, double z, float r, float g, float b) {
    super(world, x, y, z);
    this.startX = x;
    this.startY = y;
    this.startZ = z;
    this.noClip = true;
    this.particleRed = r;
    this.particleGreen = g;
    this.particleBlue = b;
    this.particleScale = (float)(0.20000000298023224D + 0.20000000298023224D * Math.random());
    this.motionY = 0.20000000298023224D * (1.0D + Math.random()) / 4.0D;
    this.particleIcon = BlockPortal.particle;
    this.particleMaxAge = (int)(80.0D / (Math.random() * 0.6D + 0.4D));
  }
  
  public void renderParticle(Tessellator tessellator, float partialTickTime, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
    this.particleAlpha = 1.0F - (this.particleAge + partialTickTime) / this.particleMaxAge;
    float u1 = this.particleTextureIndexX / 16.0F;
    float u2 = u1 + 0.0624375F;
    float v1 = this.particleTextureIndexY / 16.0F;
    float v2 = v1 + 0.0624375F;
    float size = 0.1F * this.particleScale;
    if (this.particleIcon != null) {
      u1 = this.particleIcon.getMinU();
      u2 = this.particleIcon.getMaxU();
      v1 = this.particleIcon.getMinV();
      v2 = this.particleIcon.getMaxV();
    } 
    float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTickTime - interpPosX);
    float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTickTime - interpPosY);
    float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTickTime - interpPosZ);
    float sx = (float)(this.startX - interpPosX);
    float sy = (float)(this.startY - interpPosY);
    float sz = (float)(this.startZ - interpPosZ);
    tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
    tessellator.addVertexWithUV((x - rotationX * size + rotationYZ * size), (y + rotationXZ * size), (z - rotationZ * size + rotationXY * size), u2, v1);
    tessellator.addVertexWithUV((x + rotationX * size + rotationYZ * size), (y + rotationXZ * size), (z + rotationZ * size + rotationXY * size), u1, v1);
    tessellator.addVertexWithUV((sx + rotationX * size - rotationYZ * size), (sy - rotationXZ * size), (sz + rotationZ * size - rotationXY * size), u1, v2);
    tessellator.addVertexWithUV((sx - rotationX * size - rotationYZ * size), (sy - rotationXZ * size), (sz - rotationZ * size - rotationXY * size), u2, v2);
  }
  
  public int getFXLayer() {
    return 1;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\ParticlePortal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */