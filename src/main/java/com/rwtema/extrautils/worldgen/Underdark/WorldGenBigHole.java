package com.rwtema.extrautils.worldgen.Underdark;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBigHole extends WorldGenerator {
  public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
    if (par2Random.nextInt(96) == 0) {
      int r = 4 + par2Random.nextInt(6);
      int x = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
      int z = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
      for (int y = 0; y < 82 + r; y++) {
        for (int dx = -r; dx <= r; dx++) {
          for (int dz = -r; dz <= r; dz++) {
            int ey = y - 82;
            if (ey < 0)
              ey = 0; 
            if (par2Random.nextInt(1 + ey * ey + dx * dx + dz * dz) < 1 + (r - 1) * (r - 1) / 2)
              par1World.setBlock(x + dx, y, z + dz, Blocks.air, 0, 2); 
          } 
        } 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\WorldGenBigHole.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */