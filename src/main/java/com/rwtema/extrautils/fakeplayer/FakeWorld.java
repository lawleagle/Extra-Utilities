package com.rwtema.extrautils.fakeplayer;

import java.io.File;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class FakeWorld extends World {
  public FakeWorld() {
    super(FakeSave.instance, "", null, FakeWorldProvider.instance, null);
  }
  
  protected IChunkProvider createChunkProvider() {
    return null;
  }
  
  protected int func_152379_p() {
    return 0;
  }
  
  public Entity getEntityByID(int var1) {
    return null;
  }
  
  public static class FakeSave implements ISaveHandler {
    public static FakeSave instance = new FakeSave();
    
    public WorldInfo loadWorldInfo() {
      return FakeWorld.FakeWorldInfo.instance;
    }
    
    public void checkSessionLock() throws MinecraftException {}
    
    public IChunkLoader getChunkLoader(WorldProvider var1) {
      return null;
    }
    
    public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {}
    
    public void saveWorldInfo(WorldInfo var1) {}
    
    public IPlayerFileData getSaveHandler() {
      return null;
    }
    
    public void flush() {}
    
    public File getWorldDirectory() {
      return null;
    }
    
    public File getMapFileFromName(String var1) {
      return null;
    }
    
    public String getWorldDirectoryName() {
      return null;
    }
  }
  
  public static class FakeWorldInfo extends WorldInfo {
    public static FakeWorldInfo instance = new FakeWorldInfo();
    
    public boolean isInitialized() {
      return true;
    }
    
    public int getVanillaDimension() {
      return 0;
    }
  }
  
  public static class FakeWorldProvider extends WorldProvider {
    public static FakeWorldProvider instance = new FakeWorldProvider();
    
    public void calculateInitialWeather() {}
    
    public String getDimensionName() {
      return "FAKE";
    }
    
    protected void registerWorldChunkManager() {}
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\fakeplayer\FakeWorld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */