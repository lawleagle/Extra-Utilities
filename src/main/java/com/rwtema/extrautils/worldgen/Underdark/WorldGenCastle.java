package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.ChunkPos;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.helper.XURandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;

public class WorldGenCastle extends WorldGenerator {
  public static final int rad = 17;
  
  private int[][] block = new int[17][17];
  
  public static final int[] dx = new int[] { -1, 0, 1, 0 };
  
  public static final int[] dy = new int[] { 0, -1, 0, 1 };
  
  public static final int d = 9;
  
  public static String[] dungeons = new String[] { 
      "dungeonChest", "strongholdCorridor", "strongholdLibrary", "pyramidDesertyChest", "pyramidJungleChest", "mineshaftCorridor", "villageBlacksmith", "strongholdCrossing", "dungeonChest", "dungeonChest", 
      "dungeonChest", "dungeonChest" };
  
  public static Random staticRand = (Random)XURandom.getInstance();
  
  public long timer = 0L;
  
  private int[] block_allocations = new int[16];
  
  private boolean colorbricks;
  
  private boolean colorWoods;
  
  private boolean lightGen;
  
  private ArrayList<ChunkPos> torchPos = new ArrayList<ChunkPos>();
  
  public static void setMobSpawner(World world, int x, int y, int z, Random rand) {
    world.setBlock(x, y, z, Blocks.mob_spawner, 0, 2);
    TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(x, y, z);
    if (tileentitymobspawner != null) {
      tileentitymobspawner.func_145881_a().setEntityName(DungeonHooks.getRandomDungeonMob(rand));
    } else {
      LogHelper.error("Failed to fetch mob spawner entity at (" + x + ", " + y + ", " + z + ")", new Object[0]);
    } 
  }
  
  public int castleX(long seed, int x, int z) {
    staticRand.setSeed(seed + (x >> 9) + (65535 * (z >> 9)));
    return (x >> 9 << 9) + staticRand.nextInt(390) + 61;
  }
  
  public int castleZ(long seed, int x, int z) {
    staticRand.setSeed(seed + (x >> 9) + (65535 * (z >> 9)));
    staticRand.nextInt(390);
    return (z >> 9 << 9) + staticRand.nextInt(390) + 61;
  }
  
  public boolean generate(World world, Random rand, int x, int y, int z) {
    int cx = castleX(world.getSeed(), x, z);
    int cz = castleZ(world.getSeed(), x, z);
    if (cx >> 4 != x >> 4 || cz >> 4 != z >> 4)
      return false; 
    for (int ax = 0; ax < 17; ax++) {
      for (int ay = 0; ay < 17; ay++)
        s(ax, ay, -1); 
    } 
    ArrayList<Vec2> initBlocks = new ArrayList<Vec2>();
    initBlocks.add(new Vec2(8, 8));
    for (int i = 0; i < initBlocks.size() && initBlocks.size() < 68.0D; i++) {
      int k = ((Vec2)initBlocks.get(i)).x;
      int ay = ((Vec2)initBlocks.get(i)).y;
      int dj = rand.nextInt(4);
      int m;
      for (m = (dj + 1) % 4; m != dj; m = (m + 1) % 4) {
        if (isValid(k + WorldGenCastle.dx[m], ay + dy[m])) {
          Vec2 t = new Vec2(k + WorldGenCastle.dx[m], ay + dy[m]);
          if (!initBlocks.contains(t)) {
            if (initBlocks.size() - 2 - i > 0) {
              initBlocks.add(i + 1 + rand.nextInt(initBlocks.size() - 2 - i), t);
            } else {
              initBlocks.add(t);
            } 
            s(k + WorldGenCastle.dx[m], ay + dy[m], 0);
            s(16 - k + WorldGenCastle.dx[m], 16 - ay + dy[m], 0);
            s(k + WorldGenCastle.dx[m], 16 - ay + dy[m], 0);
            s(16 - k + WorldGenCastle.dx[m], ay + dy[m], 0);
          } 
        } 
      } 
    } 
    x = cx - 24;
    z = cz - 24;
    float r = 1.0F, g = 1.0F, b = 1.0F;
    this.colorbricks = (ExtraUtils.colorBlockBrick != null);
    this.colorWoods = (ExtraUtils.coloredWood != null);
    if (this.colorbricks) {
      for (int k = 0; k < 16; k++) {
        int n = rand.nextInt(1 + k);
        if (k != n)
          this.block_allocations[k] = this.block_allocations[n]; 
        this.block_allocations[n] = k;
      } 
      float[][] cols = new float[16][3];
      r = g = b = 0.4F + 0.6F * rand.nextFloat();
      r *= (2.0F + rand.nextFloat()) / 3.0F;
      g *= (2.0F + rand.nextFloat()) / 3.0F;
      b *= (2.0F + rand.nextFloat()) / 3.0F;
      for (int m = 0; m < 16; m++) {
        float br = (1.0F + rand.nextFloat()) / 2.0F;
        cols[m][0] = r * br * (5.0F + rand.nextFloat()) / 6.0F;
        cols[m][1] = g * br * (5.0F + rand.nextFloat()) / 6.0F;
        cols[m][2] = b * br * (5.0F + rand.nextFloat()) / 6.0F;
      } 
      for (int dx = x - 16; dx <= x + 51 + 16; dx += 16) {
        for (int dz = z - 16; dz <= z + 51 + 16; dz += 16) {
          for (int n = 0; n < 16; n++)
            BlockColorData.changeColorData(world, dx, y, dz, this.block_allocations[n], cols[n][0], cols[n][1], cols[n][2]); 
        } 
      } 
    } 
    this.torchPos.clear();
    for (int j = 0; j < 5; j++)
      genLevel(world, rand, x, y + j * 5, z, j); 
    for (ChunkPos torchPo1 : this.torchPos)
      world.getChunkFromBlockCoords(torchPo1.x, torchPo1.z).func_150807_a(torchPo1.x & 0xF, torchPo1.y, torchPo1.z & 0xF, Blocks.torch, 5); 
    for (ChunkPos torchPo : this.torchPos)
      world.func_147451_t(torchPo.x, torchPo.y, torchPo.z); 
    return true;
  }
  
  public void genLevel(World world, Random rand, int x, int y, int z, int level) {
    for (int ax = 0; ax < 17; ax++) {
      for (int n = 0; n < 17; n++) {
        if (this.block[ax][n] == -1 || ax <= level || 16 - ax <= level || n <= level || 16 - n <= level) {
          this.block[ax][n] = -1;
        } else {
          this.block[ax][n] = 0;
        } 
      } 
    } 
    ArrayList<Vec2> list = new ArrayList<Vec2>();
    ArrayList<Vec2> corridors = new ArrayList<Vec2>();
    this.block[8][8] = 1;
    corridors.add(new Vec2(8, 8));
    if (level == 0) {
      for (int n = 1; n < 17; n++) {
        this.block[n][8] = 1;
        corridors.add(new Vec2(n, 8));
      } 
      this.block[0][8] = -1;
    } 
    int a = 0;
    while (a < 289) {
      a++;
      Vec2 t = corridors.get(rand.nextInt(corridors.size()));
      int n = 1 + rand.nextInt(8);
      int d = rand.nextInt(4);
      boolean canAdd = true;
      for (int i1 = 1; i1 <= n && canAdd; i1++) {
        if (!isValid(t.x + dx[d] * i1, t.y + dy[d] * i1) || g(t.x + dx[d] * i1, t.y + dy[d] * i1) != 0) {
          canAdd = false;
        } else {
          int c_n = 0;
          for (int i2 = 0; i2 < 4; i2++) {
            if (g(t.x + dx[d] * i1 + dx[i2], t.y + dy[d] * i1 + dy[i2]) != 0)
              c_n++; 
          } 
          if (c_n >= 2) {
            canAdd = false;
          } else {
            c_n = 0;
            for (int ddx = -2; ddx <= 2; ddx++) {
              for (int ddy = -2; ddy <= 2; ddy++) {
                if (g(t.x + dx[d] * i1 + ddx, t.y + dy[d] * i1 + ddy) != 0)
                  c_n++; 
              } 
            } 
            if (c_n >= 8) {
              canAdd = false;
            } else {
              s(t.x + dx[d] * i1, t.y + dy[d] * i1, 1);
              corridors.add(new Vec2(t.x + dx[d] * i1, t.y + dy[d] * i1));
              a += 12;
            } 
          } 
        } 
      } 
    } 
    Collections.shuffle(corridors);
    for (int i = 0; i < corridors.size() && i < 10; i++)
      list.add(corridors.get(i)); 
    int numDoors = 0;
    ArrayList<Integer> doorDirections = new ArrayList<Integer>();
    doorDirections.add(Integer.valueOf(0));
    for (int j = 0; j < list.size(); j++) {
      int n = ((Vec2)list.get(j)).x;
      int i1 = ((Vec2)list.get(j)).y;
      boolean added = false;
      if (g(n, i1) == 0) {
        for (int i3 = 0; i3 < 4; i3++) {
          if (g(n + dx[i3], i1 + dy[i3]) >> 1 > 0 && g(n + dx[i3], i1 + dy[i3]) >> 1 == g(n + dx[(i3 + 1) % 4], i1 + dy[(i3 + 1) % 4]) >> 1) {
            added = true;
            s(n, i1, (g(n + dx[i3], i1 + dy[i3]) >> 1) * 2 + 1);
          } 
        } 
        if (!added) {
          int i4 = rand.nextInt(4);
          for (int i5 = 0; i5 < 4; i5++) {
            if (g(n + dx[(i5 + i4) % 4], i1 + dy[(i5 + i4) % 4]) >> 1 > 0) {
              added = true;
              s(n, i1, (g(n + dx[(i5 + i4) % 4], i1 + dy[(i5 + i4) % 4]) >> 1) * 2 + 1);
            } 
          } 
        } 
        if (!added) {
          int i4 = rand.nextInt(4);
          for (int i5 = 0; i5 < 4; i5++) {
            if (g(n + dx[(i5 + i4) % 4], i1 + dy[(i5 + i4) % 4]) == 1) {
              added = true;
              numDoors++;
              doorDirections.add(Integer.valueOf(i5));
              s(n, i1, numDoors * 2);
            } 
          } 
        } 
      } 
      for (int i2 = 0; i2 < 4; i2++) {
        if (isValid(n + dx[i2], i1 + dy[i2]) && !list.contains(new Vec2(n + dx[i2], i1 + dy[i2])))
          if (list.size() - j >= 1) {
            list.add(j + rand.nextInt(list.size() - j), new Vec2(n + dx[i2], i1 + dy[i2]));
          } else {
            list.add(new Vec2(n + dx[i2], i1 + dy[i2]));
          }  
      } 
    } 
    int doorDir = 0;
    int k;
    for (k = 0; k < 17; k++) {
      for (int n = 0; n < 17; n++) {
        int d = g(k, n);
        if (d >= 0) {
          for (int dax = -1; dax <= 1; dax++) {
            for (int day = -1; day <= 1; day++) {
              setBrick(world, x + 1 + k * 3 + dax, y, z + 1 + n * 3 + day, 0);
              Block id = Blocks.air;
              if (dax == 0 && day == 0) {
                id = Blocks.air;
                if (d > 1)
                  setWood(world, x + 1 + k * 3 + dax, y, z + 1 + n * 3 + day, (d >> 1) % 16); 
              } else if (dax == 0 || day == 0) {
                doorDir = (dax == 0) ? ((day == 1) ? 3 : 1) : ((dax == 1) ? 2 : 0);
                if (g(k + dax, n + day) >> 1 == d >> 1 || (d == 1 && g(k + dax, n + day) <= 0) || (d == 1 && g(k + dax, n + day) % 2 == 0) || (d % 2 == 0 && g(k + dax, n + day) == 1)) {
                  if (d % 2 == 0 && g(k + dax, n + day) == 1) {
                    id = Blocks.planks;
                  } else {
                    id = Blocks.air;
                    if (d > 1)
                      setWood(world, x + 1 + k * 3 + dax, y, z + 1 + n * 3 + day, woodPattern(d, k * 3 + dax, n * 3 + day)); 
                  } 
                } else {
                  id = Blocks.stonebrick;
                } 
              } else if (d > 1 && g(k + dax, n) >> 1 == d >> 1 && g(k, n + day) >> 1 == d >> 1) {
                id = Blocks.air;
                setWood(world, x + 1 + k * 3 + dax, y, z + 1 + n * 3 + day, woodPattern(d, k * 3 + dax, n * 3 + day));
              } else {
                id = Blocks.stonebrick;
              } 
              if (id == Blocks.planks) {
                ItemDoor.placeDoorBlock(world, x + 1 + k * 3 + dax, y + 1, z + 1 + n * 3 + day, doorDir, Blocks.wooden_door);
                setBrick(world, x + 1 + k * 3 + dax, y + 3, z + 1 + n * 3 + day, 2);
                setBrick(world, x + 1 + k * 3 + dax, y + 4, z + 1 + n * 3 + day, 1);
              } else {
                for (int dh = 0; dh <= 3; dh++) {
                  if (id == Blocks.stonebrick) {
                    setBrick(world, x + 1 + k * 3 + dax, y + 1 + dh, z + 1 + n * 3 + day, (dh == 2) ? 2 : 1);
                  } else {
                    world.setBlock(x + 1 + k * 3 + dax, y + 1 + dh, z + 1 + n * 3 + day, id, 0, 2);
                  } 
                } 
              } 
              if (d == 1)
                setBrick(world, x + 1 + k * 3 + dax, y + 3, z + 1 + n * 3 + day, 1); 
              setBrick(world, x + 1 + k * 3 + dax, y + 4, z + 1 + n * 3 + day, 3);
            } 
          } 
        } else {
          for (int dax = -1; dax <= 1; dax++) {
            for (int day = -1; day <= 1; day++) {
              if (g(k + dax, n + day) >= 0 || g(k + dax, n) >= 0 || g(k, n + day) >= 0) {
                setBrick(world, x + 1 + k * 3 + dax, y, z + 1 + n * 3 + day, 0);
                setBrick(world, x + 1 + k * 3 + dax, y + 4, z + 1 + n * 3 + day, 4);
                if ((k + dax + n + day) % 2 == 0) {
                  setBrick(world, x + 1 + k * 3 + dax, y + 5, z + 1 + n * 3 + day, 5);
                  this.torchPos.add(new ChunkPos(x + 1 + k * 3 + dax, y + 6, z + 1 + n * 3 + day));
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    k = 8;
    int ay = 8;
    for (int h = 0; h <= 4; h++) {
      for (int dax = -2; dax <= 2; dax++) {
        for (int day = -2; day <= 2; day++) {
          if (Math.abs(dax) < 2 || Math.abs(day) < 2)
            if (h > 0 && h < 4) {
              world.setBlock(x + 1 + k * 3 + dax, y + h, z + 1 + ay * 3 + day, Blocks.air, 0, 2);
            } else {
              setBrick(world, x + 1 + k * 3 + dax, y + h, z + 1 + ay * 3 + day, 3);
            }  
        } 
      } 
      setBrick(world, x + 1 + k * 3 + 1, y + h, z + 1 + ay * 3, 0);
      world.setBlock(x + 1 + k * 3, y + h, z + 1 + ay * 3, Blocks.ladder, 4, 2);
    } 
    if (level == 0)
      for (int dax = -1; dax <= 1; dax++) {
        for (int day = -1; day <= 1; day++)
          setBrick(world, x + 1 + k * 3 + dax, y, z + 1 + ay * 3 + day, 1); 
      }  
    int numChests = (17 - 2 * level) * (17 - 2 * level) / 49;
    ArrayList<Vec2> chestPos = new ArrayList<Vec2>();
    for (int m = list.size() - 1; chestPos.size() < numChests && m >= 0; m--) {
      Vec2 v = list.get(m);
      boolean add = (g(v.x, v.y) > 1);
      if (add)
        for (int n = 0; add && n < chestPos.size(); n++) {
          if (((Vec2)chestPos.get(n)).distFrom(v) < 8.0D)
            add = false; 
        }  
      if (add)
        chestPos.add(v); 
    } 
    for (Vec2 chestPo : chestPos) {
      world.setBlock(x + 1 + 3 * chestPo.x, y + 1, z + 1 + 3 * chestPo.y, (Block)Blocks.chest);
      TileEntityChest tile = (TileEntityChest)world.getTileEntity(x + 1 + 3 * chestPo.x, y + 1, z + 1 + 3 * chestPo.y);
      if (tile != null) {
        ChestGenHooks info = ChestGenHooks.getInfo(dungeons[rand.nextInt(dungeons.length)]);
        WeightedRandomChestContent.generateChestContents(rand, info.getItems(rand), (IInventory)tile, info.getCount(rand));
      } 
      setMobSpawner(world, x + 1 + 3 * chestPo.x, y + 2, z + 1 + 3 * chestPo.y, rand);
    } 
  }
  
  public void setBrick(World world, int x, int y, int z, int type) {
    if (this.colorbricks) {
      world.setBlock(x, y, z, (Block)ExtraUtils.colorBlockBrick, this.block_allocations[type], 2);
    } else {
      world.setBlock(x, y, z, Blocks.stonebrick);
    } 
  }
  
  public void setWood(World world, int x, int y, int z, int type) {
    if (this.colorWoods) {
      world.setBlock(x, y, z, (Block)ExtraUtils.coloredWood, this.block_allocations[type], 2);
    } else {
      world.setBlock(x, y, z, Blocks.planks, type % 4, 2);
    } 
  }
  
  public int g(int x, int y) {
    return isValid(x, y) ? this.block[x][y] : -1;
  }
  
  public void s(int x, int y, int i) {
    if (isValid(x, y))
      this.block[x][y] = i; 
  }
  
  public boolean isValid(int x, int y) {
    if (x >= 0 && y >= 0)
      if ((((x < 17) ? 1 : 0) & ((y < 17) ? 1 : 0)) != 0); 
    return false;
  }
  
  public int woodPattern(int d, int x, int y) {
    d = (d >> 1) % 16;
    switch (d) {
      case 0:
        if (x % 2 == y % 2)
          d++; 
        break;
      case 3:
        if (x % 2 == 0)
          d++; 
        break;
      case 4:
        if (y % 2 == 0)
          d++; 
        break;
      case 6:
        if (x % 2 * y % 2 == 0)
          d++; 
        break;
      case 8:
        if (x % 3 * y % 3 == 0)
          d++; 
        break;
      case 9:
        if (x % 4 + y % 4 == 0)
          d++; 
        break;
      case 11:
        if (x % 4 + y % 2 == 0)
          d++; 
        break;
      case 12:
        if (x % 3 == 0)
          d++; 
        break;
      case 13:
        if (x % 6 == 0) {
          d++;
          break;
        } 
        if (x % 2 == 0)
          d += 2; 
        break;
    } 
    return d % 16;
  }
  
  public static class Vec2 {
    int x;
    
    int y;
    
    public Vec2(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    public boolean equals(Object o) {
      return (o instanceof Vec2 && ((Vec2)o).x == this.x && ((Vec2)o).y == this.y);
    }
    
    public double distFrom(Vec2 other) {
      return Math.sqrt(((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y)));
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\WorldGenCastle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */