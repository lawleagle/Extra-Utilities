package com.rwtema.extrautils.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

@SideOnly(Side.CLIENT)
public class ParticleHelperClient implements IResourceManagerReloadListener {
  private static EffectRenderer effectRenderer;
  
  @SideOnly(Side.CLIENT)
  public static void addParticle(EntityFX particle) {
    effectRenderer.addEffect(particle);
  }
  
  public void onResourceManagerReload(IResourceManager manager) {
    effectRenderer = (Minecraft.getMinecraft()).effectRenderer;
    registerTextures(manager);
  }
  
  private static void registerTextures(IResourceManager manager) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\ParticleHelperClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */