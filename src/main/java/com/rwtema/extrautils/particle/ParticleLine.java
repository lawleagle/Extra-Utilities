package com.rwtema.extrautils.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ParticleLine extends EntityFX {
  public final Vec3 start;
  
  public final Vec3 end;
  
  public ParticleLine(World world, Vec3 start, Vec3 end, float r, float g, float b, IIcon particle) {
    super(world, start.xCoord, start.yCoord, start.zCoord);
    this.start = start;
    this.end = end;
    this.noClip = true;
    this.particleRed = r;
    this.particleGreen = g;
    this.particleBlue = b;
    this.particleScale = (float)(0.20000000298023224D + 0.20000000298023224D * Math.random());
    this.particleIcon = particle;
    this.particleMaxAge = (int)(10.0D / (Math.random() * 0.6D + 0.4D));
  }
  
  public void renderParticle(Tessellator tessellator, float partialTickTime, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
    this.particleAlpha = 1.0F - (this.particleAge + partialTickTime) / this.particleMaxAge;
    float size = 0.25F * this.particleScale;
    float u1 = this.particleIcon.getMinU();
    float u2 = this.particleIcon.getMaxU();
    float v1 = this.particleIcon.getMinV();
    float v2 = this.particleIcon.getMaxV();
    float ax = (float)(this.start.xCoord - interpPosX);
    float ay = (float)(this.start.yCoord - interpPosY);
    float az = (float)(this.start.zCoord - interpPosZ);
    float bx = (float)(this.end.xCoord - interpPosX);
    float by = (float)(this.end.yCoord - interpPosY);
    float bz = (float)(this.end.zCoord - interpPosZ);
    tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
    tessellator.addVertexWithUV((bx - rotationX * size - rotationYZ * size), (by - rotationXZ * size), (bz - rotationZ * size - rotationXY * size), u2, v2);
    tessellator.addVertexWithUV((ax - rotationX * size + rotationYZ * size), (ay + rotationXZ * size), (az - rotationZ * size + rotationXY * size), u2, v1);
    tessellator.addVertexWithUV((ax + rotationX * size + rotationYZ * size), (ay + rotationXZ * size), (az + rotationZ * size + rotationXY * size), u1, v1);
    tessellator.addVertexWithUV((bx + rotationX * size - rotationYZ * size), (by - rotationXZ * size), (bz + rotationZ * size - rotationXY * size), u1, v2);
  }
  
  public int getFXLayer() {
    return 1;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\ParticleLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */