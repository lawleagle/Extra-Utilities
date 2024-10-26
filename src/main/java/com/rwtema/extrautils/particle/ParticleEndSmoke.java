package com.rwtema.extrautils.particle;

import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.world.World;

public class ParticleEndSmoke extends EntityReddustFX {
  public final int textureIndex = 7;
  
  public ParticleEndSmoke(World p_i1223_1_, double p_i1223_2_, double p_i1223_4_, double p_i1223_6_, float p_i1223_8_, float p_i1223_9_, float p_i1223_10_) {
    super(p_i1223_1_, p_i1223_2_, p_i1223_4_, p_i1223_6_, p_i1223_8_, p_i1223_9_, p_i1223_10_);
    setParticleTextureIndex(7);
  }
  
  public ParticleEndSmoke(World p_i1224_1_, double p_i1224_2_, double p_i1224_4_, double p_i1224_6_, float p_i1224_8_, float p_i1224_9_, float p_i1224_10_, float p_i1224_11_) {
    super(p_i1224_1_, p_i1224_2_, p_i1224_4_, p_i1224_6_, p_i1224_8_, p_i1224_9_, p_i1224_10_, p_i1224_11_);
    setParticleTextureIndex(7);
  }
  
  public void onUpdate() {
    super.onUpdate();
    setParticleTextureIndex(7);
  }
  
  public int getBrightnessForRender(float p_70070_1_) {
    int i = super.getBrightnessForRender(p_70070_1_);
    float f1 = this.particleAge / this.particleMaxAge;
    f1 *= f1;
    f1 *= f1;
    int j = i & 0xFF;
    int k = i >> 16 & 0xFF;
    k += (int)(f1 * 15.0F * 16.0F);
    if (k > 240)
      k = 240; 
    return j | k << 16;
  }
  
  public float getBrightness(float p_70013_1_) {
    float f1 = super.getBrightness(p_70013_1_);
    float f2 = this.particleAge / this.particleMaxAge;
    f2 = f2 * f2 * f2 * f2;
    return f1 * (1.0F - f2) + f2;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\ParticleEndSmoke.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */