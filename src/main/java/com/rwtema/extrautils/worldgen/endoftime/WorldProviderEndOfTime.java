package com.rwtema.extrautils.worldgen.endoftime;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

public class WorldProviderEndOfTime extends WorldProvider implements IClientCode {
  public static final float celestialAngle = 0.5F;
  
  public static final int skyColor = 1052736;
  
  public WorldProviderEndOfTime() {
    ExtraUtilsMod.proxy.exectuteClientCode(this);
  }
  
  @SideOnly(Side.CLIENT)
  public void exectuteClientCode() {
    setCloudRenderer(RenderHandlersEndOfTime.nullRenderer);
    setSkyRenderer(RenderHandlersEndOfTime.skyRenderer);
  }
  
  public void registerWorldChunkManager() {
    this.worldChunkMgr = new WorldChunkManager(this.worldObj);
    this.dimensionId = ExtraUtils.endoftimeDimID;
  }
  
  public IChunkProvider createChunkGenerator() {
    return new ChunkProviderEndOfTime(this.worldObj, this.worldObj.getSeed());
  }
  
  public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
    return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
  }
  
  public ChunkCoordinates getEntrancePortalLocation() {
    WorldInfo worldInfo = this.worldObj.getWorldInfo();
    if (worldInfo.getSpawnY() != 64)
      worldInfo.setSpawnPosition(worldInfo.getSpawnX(), 64, worldInfo.getSpawnZ()); 
    return new ChunkCoordinates(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
  }
  
  public int getAverageGroundLevel() {
    return 64;
  }
  
  public String getDimensionName() {
    return "The Last Millenium";
  }
  
  @SideOnly(Side.CLIENT)
  public double getVoidFogYFactor() {
    return 1.0D;
  }
  
  public boolean canRespawnHere() {
    return true;
  }
  
  public boolean isSurfaceWorld() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public float getCloudHeight() {
    return 256.0F;
  }
  
  protected void generateLightBrightnessTable() {
    float f = 0.0F;
    for (int i = 0; i <= 15; i++) {
      float f1 = 1.0F - i / 15.0F;
      this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public String getWelcomeMessage() {
    return "Travelling to the End of Time";
  }
  
  public ChunkCoordinates getSpawnPoint() {
    return getEntrancePortalLocation();
  }
  
  public ChunkCoordinates getRandomizedSpawnPoint() {
    ChunkCoordinates chunkCoordinates = getEntrancePortalLocation();
    chunkCoordinates.posY = this.worldObj.getTopSolidOrLiquidBlock(chunkCoordinates.posX, chunkCoordinates.posZ);
    return chunkCoordinates;
  }
  
  public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
    return 0.5F;
  }
  
  public float getCurrentMoonPhaseFactor() {
    return 0.0F;
  }
  
  public double getHorizon() {
    return 3333.0D;
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getSkyColor(Entity p_72833_1_, float p_72833_2_) {
    int color = 65538;
    float r = (color >> 16 & 0xFF) / 255.0F;
    float g = (color >> 8 & 0xFF) / 255.0F;
    float b = (color & 0xFF) / 255.0F;
    float f7 = this.worldObj.getRainStrength(p_72833_2_);
    if (f7 > 0.0F) {
      float f1 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.6F;
      float f9 = 1.0F - f7 * 0.75F;
      r = r * f9 + f1 * (1.0F - f9);
      g = g * f9 + f1 * (1.0F - f9);
      b = b * f9 + f1 * (1.0F - f9);
    } 
    float f8 = this.worldObj.getWeightedThunderStrength(p_72833_2_);
    if (f8 > 0.0F) {
      float f9 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.2F;
      float f10 = 1.0F - f8 * 0.75F;
      r = r * f10 + f9 * (1.0F - f10);
      g = g * f10 + f9 * (1.0F - f10);
      b = b * f10 + f9 * (1.0F - f10);
    } 
    if (this.worldObj.lastLightningBolt > 0) {
      float f9 = this.worldObj.lastLightningBolt - p_72833_2_;
      if (f9 > 1.0F)
        f9 = 1.0F; 
      f9 *= 0.45F;
      r = r * (1.0F - f9) + 0.8F * f9;
      g = g * (1.0F - f9) + 0.8F * f9;
      b = b * (1.0F - f9) + 1.0F * f9;
    } 
    return Vec3.createVectorHelper(r, g, b);
  }
  
  public int getMoonPhase(long p_76559_1_) {
    return 4;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\endoftime\WorldProviderEndOfTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */