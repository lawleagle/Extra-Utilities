package com.rwtema.extrautils.worldgen;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;

public class WorldGenEnderLillies implements IWorldGenerator {
  public static String retrogen = "XU:EnderLilyRetrogen";
  
  public WorldGenEnderLillies() {
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  public static boolean isAirBlock(Block id) {
    return (id == Blocks.air || id == null);
  }
  
  public static void gen(Random random, Chunk chunk) {
    for (int i = 0; i < 32; i++) {
      int x = random.nextInt(16);
      int y = 10 + random.nextInt(65);
      int z = random.nextInt(16);
      if (chunk.getBlock(x, y, z) == Blocks.end_stone && isAirBlock(chunk.getBlock(x, y + 1, z))) {
        chunk.func_150807_a(x, y + 1, z, (Block)ExtraUtils.enderLily, 7);
        if (random.nextDouble() < 0.2D)
          return; 
      } 
    } 
  }
  
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
    if (world.provider.dimensionId == 1)
      gen(random, world.getChunkFromChunkCoords(chunkX, chunkZ)); 
  }
  
  public void saveData(ChunkDataEvent.Save event) {
    if (event.world.provider.dimensionId == 1)
      event.getData().setInteger(retrogen, ExtraUtils.enderLilyRetrogenId); 
  }
  
  public void loadData(ChunkDataEvent.Load event) {
    if (ExtraUtils.enderLilyRetrogenId > 0 && event.world.provider.dimensionId == 1 && event.world instanceof net.minecraft.world.WorldServer && 
      event.getData().getInteger(retrogen) != ExtraUtils.enderLilyRetrogenId) {
      long worldSeed = event.world.getSeed();
      Random random = new Random(worldSeed);
      long xSeed = random.nextLong() >> 3L;
      long zSeed = random.nextLong() >> 3L;
      long chunkSeed = xSeed * (event.getChunk()).xPosition + zSeed * (event.getChunk()).zPosition ^ worldSeed;
      random.setSeed(chunkSeed);
      gen(random, event.getChunk());
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\WorldGenEnderLillies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */