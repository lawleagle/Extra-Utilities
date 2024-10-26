package com.rwtema.extrautils;

import com.rwtema.extrautils.block.BlockCursedEarth;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class ActivationRitual {
  public static int max_range = 8;
  
  public static int num_light = 0;
  
  public static int required_dirt = 20;
  
  public static int time_window = 500;
  
  public static boolean redstoneCirclePresent(World world, int x, int y, int z) {
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if ((((dx != 0) ? 1 : 0) | ((dz != 0) ? 1 : 0)) != 0 && 
          world.getBlock(x + dx, y, z + dz) != Blocks.redstone_wire)
          return false; 
      } 
    } 
    return true;
  }
  
  public static boolean altarInDarkness_Client(World world, int x, int y, int z) {
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if (world.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x + dx, y, z + dz) + world.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x + dx, y, z + dz) - world.calculateSkylightSubtracted(1.0F) > 9)
          return false; 
      } 
    } 
    return (world.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y + 1, z) + world.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x, y + 1, z) - world.calculateSkylightSubtracted(1.0F) <= 9);
  }
  
  public static boolean altarInDarkness(World world, int x, int y, int z) {
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if (world.getBlockLightValue(x + dx, y, z + dz) > 9)
          return false; 
      } 
    } 
    return (world.getBlockLightValue(x, y + 1, z) <= 9);
  }
  
  public static boolean altarCanSeeMoon(World world, int x, int y, int z) {
    return world.canBlockSeeTheSky(x, y, z);
  }
  
  public static boolean altarOnEarth(World world, int x, int y, int z) {
    boolean hasDirt = false;
    for (int dx = -1; dx <= 1 && !hasDirt; dx++) {
      for (int dz = -1; dz <= 1 && !hasDirt; dz++) {
        if (world.getBlock(x + dx, y - 1, z + dz) != Blocks.dirt && world.getBlock(x + dx, y - 1, z + dz) != Blocks.grass)
          return false; 
      } 
    } 
    return true;
  }
  
  public static int checkTime(long time) {
    time %= 24000L;
    if (time < (18000 - time_window))
      return -1; 
    if (time > (18000 + time_window))
      return 1; 
    return 0;
  }
  
  public static boolean naturalEarth(World world, int x, int y, int z) {
    int num_dirt = 0;
    for (int dx = -max_range; dx <= max_range; dx++) {
      for (int dz = -max_range; dz <= max_range; dz++) {
        if (dx * dx + dz * dz < max_range * max_range) {
          for (int dy = Math.min(3 + world.getTopSolidOrLiquidBlock(x + dx, z + dz) - y, max_range); dy >= -max_range; dy--) {
            if (dx * dx + dy * dy + dz * dz <= max_range * max_range) {
              Block id = world.getBlock(x + dx, y + dy, z + dz);
              if (id == Blocks.dirt || id == Blocks.grass) {
                num_dirt++;
                if (canShift(world.getBlock(x + dx, y + dy + 1, z + dz)) && num_dirt > required_dirt)
                  return true; 
                break;
              } 
              if (id.isOpaqueCube())
                break; 
            } else if (dy < 0) {
              break;
            } 
          } 
        } else if (dz > 0) {
          break;
        } 
      } 
    } 
    return false;
  }
  
  public static boolean canShift(Block id) {
    if (id == Blocks.air)
      return true; 
    return (id != null && id.getMaterial() != Material.water && id.getMobilityFlag() == 1);
  }
  
  public static void startRitual(World world, int x, int y, int z, EntityPlayer player) {
    world.addWeatherEffect((Entity)new EntityLightningBolt(world, x, y, z));
    if (ExtraUtils.cursedEarth != null) {
      BlockCursedEarth.powered = 16;
      for (int dx = -max_range; dx <= max_range; dx++) {
        for (int dz = -max_range; dz <= max_range; dz++) {
          if (dx * dx + dz * dz < max_range * max_range) {
            for (int dy = max_range; dy > -max_range; dy--) {
              if (dx * dx + dy * dy + dz * dz <= max_range * max_range) {
                Block id = world.getBlock(x + dx, y + dy, z + dz);
                if (id != Blocks.air) {
                  if (id == Blocks.dirt || id == Blocks.grass) {
                    world.setBlock(x + dx, y + dy, z + dz, ExtraUtils.cursedEarth, 0, 3);
                    break;
                  } 
                  if (id instanceof net.minecraft.block.BlockLeaves) {
                    id.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                    world.setBlock(x + dx, y + dy, z + dz, Blocks.air);
                  } else if (id instanceof net.minecraft.block.BlockSnow) {
                    world.setBlock(x + dx, y + dy, z + dz, Blocks.air);
                  } else if (id.getMobilityFlag() == 1 && id != Blocks.redstone_wire) {
                    world.func_147480_a(x + dx, y + dy, z + dz, true);
                  } else if (id.isOpaqueCube()) {
                    break;
                  } 
                } 
              } else if (dy < 0) {
                break;
              } 
            } 
          } else if (dz > 0) {
            break;
          } 
        } 
      } 
      BlockCursedEarth.powered = 0;
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ActivationRitual.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */