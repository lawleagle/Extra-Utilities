package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderUnderdark extends WorldProvider {
  private float[] colorsSunriseSunset = new float[4];
  
  public void registerWorldChunkManager() {
    this.worldChunkMgr = new WorldChunkManager(this.worldObj);
    this.dimensionId = ExtraUtils.underdarkDimID;
    this.hasNoSky = true;
  }
  
  public long getSeed() {
    return super.getSeed() + (ChunkProviderUnderdark.denyDecor ? 1L : 0L);
  }
  
  public IChunkProvider createChunkGenerator() {
    return new ChunkProviderUnderdark(this.worldObj, this.worldObj.getSeed(), false);
  }
  
  public int getAverageGroundLevel() {
    return 81;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean doesXZShowFog(int par1, int par2) {
    return true;
  }
  
  public String getDimensionName() {
    return "Underdark";
  }
  
  public boolean renderStars() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public float getStarBrightness(World world, float f) {
    return 0.0F;
  }
  
  public boolean renderClouds() {
    return false;
  }
  
  public boolean renderVoidFog() {
    return true;
  }
  
  public boolean renderEndSky() {
    return false;
  }
  
  public float setSunSize() {
    return 0.0F;
  }
  
  public float setMoonSize() {
    return 0.0F;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isSkyColored() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public double getVoidFogYFactor() {
    return 1.0D;
  }
  
  public boolean canRespawnHere() {
    return false;
  }
  
  public boolean isSurfaceWorld() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public float getCloudHeight() {
    return 256.0F;
  }
  
  public boolean canCoordinateBeSpawn(int par1, int par2) {
    return false;
  }
  
  public ChunkCoordinates getEntrancePortalLocation() {
    return new ChunkCoordinates(50, 5, 0);
  }
  
  protected void generateLightBrightnessTable() {
    float f = 0.0F;
    for (int i = 0; i <= 15; i++) {
      float p = i / 15.0F;
      float f1 = 1.0F - p;
      this.lightBrightnessTable[i] = p / (f1 * 3.0F + 1.0F);
      if (this.lightBrightnessTable[i] < 0.2F)
        this.lightBrightnessTable[i] = this.lightBrightnessTable[i] * this.lightBrightnessTable[i] / 0.2F; 
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public String getWelcomeMessage() {
    if (this instanceof WorldProviderUnderdark)
      return "Entering the 'Deep Dark'"; 
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public float[] calcSunriseSunsetColors(float par1, float par2) {
    return null;
  }
  
  public float calculateCelestialAngle(long par1, float par3) {
    int j = 18000;
    float f1 = (j + par3) / 24000.0F - 0.25F;
    if (f1 < 0.0F)
      f1++; 
    if (f1 > 1.0F)
      f1--; 
    float f2 = f1;
    f1 = 1.0F - (float)((Math.cos(f1 * Math.PI) + 1.0D) / 2.0D);
    f1 = f2 + (f1 - f2) / 3.0F;
    return f1;
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getFogColor(float par1, float par2) {
    return Vec3.createVectorHelper(9.999999974752427E-7D, 9.999999974752427E-7D, 9.999999974752427E-7D);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\WorldProviderUnderdark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */