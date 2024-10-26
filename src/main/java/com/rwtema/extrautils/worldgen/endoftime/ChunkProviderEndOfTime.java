package com.rwtema.extrautils.worldgen.endoftime;

import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEndOfTime implements IChunkProvider {
  World worldObj;
  
  public ChunkProviderEndOfTime(World worldObj, long seed) {
    this.worldObj = worldObj;
  }
  
  public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
    return true;
  }
  
  public Chunk provideChunk(int x, int z) {
    Chunk chunk = new Chunk(this.worldObj, x, z);
    Arrays.fill(chunk.getBiomeArray(), (byte)BiomeGenBase.plains.biomeID);
    chunk.generateSkylightMap();
    chunk.isTerrainPopulated = true;
    chunk.isLightPopulated = true;
    chunk.isModified = true;
    return chunk;
  }
  
  public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
    return provideChunk(p_73158_1_, p_73158_2_);
  }
  
  public void populate(IChunkProvider provider, int x, int z) {}
  
  public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
    return true;
  }
  
  public boolean unloadQueuedChunks() {
    return false;
  }
  
  public boolean canSave() {
    return true;
  }
  
  public String makeString() {
    return "EoTLevelSource";
  }
  
  public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
    BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
    return biomegenbase.getSpawnableList(p_73155_1_);
  }
  
  public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
    return null;
  }
  
  public int getLoadedChunkCount() {
    return 0;
  }
  
  public void recreateStructures(int p_82695_1_, int p_82695_2_) {}
  
  public void saveExtraData() {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\endoftime\ChunkProviderEndOfTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */