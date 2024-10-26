package com.rwtema.extrautils.nei.ping;

import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticlePing extends EntityReddustFX {
  public static final Random RANDOM = (Random)XURandom.getInstance();
  
  public static final float r = 1.0F;
  
  public static final float g = 1.0F;
  
  public static final float b = 1.0F;
  
  public ParticlePing(World world, int x, int y, int z) {
    super(world, x + randOffset(), y + randOffset(), z + randOffset(), 1.0F, 1.0F, 1.0F);
    this.noClip = true;
    this.particleMaxAge *= 10;
    this.motionX *= 0.1D;
    this.motionY *= 0.1D;
    this.motionZ *= 0.1D;
    this.particleScale *= 2.0F;
  }
  
  public static double randOffset() {
    return 0.5D + (RANDOM.nextDouble() - 0.5D) * RANDOM.nextDouble();
  }
  
  public ParticlePing(WorldClient theWorld, ChunkPosition p) {
    this((World)theWorld, p.chunkPosX, p.chunkPosY, p.chunkPosZ);
  }
  
  public void renderParticle(Tessellator tessellator, float partialTickTime, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    super.renderParticle(tessellator, partialTickTime, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    tessellator.draw();
    GL11.glDisable(2929);
    tessellator.startDrawingQuads();
    super.renderParticle(tessellator, partialTickTime, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    tessellator.draw();
    GL11.glEnable(2929);
    tessellator.startDrawingQuads();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\ping\ParticlePing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */