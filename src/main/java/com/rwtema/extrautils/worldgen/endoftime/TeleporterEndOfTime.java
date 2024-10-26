package com.rwtema.extrautils.worldgen.endoftime;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.worldgen.TeleporterBase;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class TeleporterEndOfTime extends TeleporterBase {
  public TeleporterEndOfTime(WorldServer p_i1963_1_) {
    super(p_i1963_1_);
  }
  
  public void placeInPortal(Entity entity, double x, double y, double z, float r) {
    if (!placeInExistingPortal(entity, x, y, z, r))
      if (this.worldServerInstance.provider.dimensionId != ExtraUtils.endoftimeDimID) {
        y = this.worldServerInstance.getTopSolidOrLiquidBlock((int)x, (int)z);
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
      } else {
        ChunkCoordinates entrancePortalLocation = this.worldServerInstance.getEntrancePortalLocation();
        if (!placeInExistingPortal(entity, entrancePortalLocation.posX, y, entrancePortalLocation.posZ, r))
          makePortal(entity); 
      }  
  }
  
  public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float r) {
    TileEntity destPortal = null;
    for (int s = 0; s <= 5 && destPortal == null; s++) {
      for (int dx = -s; dx <= s; dx++) {
        for (int dz = -s; dz <= s; dz++) {
          if (destPortal == null)
            destPortal = findPortalInChunk(x + (dx * 16), z + (dz * 16)); 
        } 
      } 
    } 
    if (destPortal != null) {
      entity.setLocationAndAngles(destPortal.xCoord + 0.5D, (destPortal.yCoord + 1), destPortal.zCoord + 0.5D, entity.rotationYaw, entity.rotationPitch);
      entity.motionX = entity.motionY = entity.motionZ = 0.0D;
      return true;
    } 
    return false;
  }
  
  public boolean makePortal(Entity entity) {
    ChunkCoordinates dest = this.worldServerInstance.getEntrancePortalLocation();
    int x = dest.posX;
    int y = dest.posY;
    int z = dest.posZ;
    if (y < 64) {
      y = 64;
      this.worldServerInstance.getWorldInfo().setSpawnPosition(x, 64, z);
    } 
    int r = 8;
    boolean d = (ExtraUtils.decorative1 != null);
    Block bricks = d ? (Block)ExtraUtils.decorative1 : Blocks.stonebrick;
    int bricksMeta1 = d ? 4 : 0;
    int bricksMeta2 = d ? 0 : 0;
    int bricksMeta3 = d ? 10 : 0;
    Block darkBricks = d ? (Block)ExtraUtils.decorative1 : Blocks.obsidian;
    int darkBricksMeta = d ? 2 : 0;
    for (int dx = -r; dx <= r; dx++) {
      for (int dz = -r; dz <= r; dz++) {
        for (int dy = -2; dy < 7; dy++)
          this.worldServerInstance.setBlock(x + dx, y + dy, z + dz, Blocks.air, 0, 2); 
        int min_r = Math.min(Math.abs(dx), Math.abs(dz));
        int max_r = Math.max(Math.abs(dx), Math.abs(dz));
        if (max_r <= r - 1)
          this.worldServerInstance.setBlock(x + dx, y - 1, z + dz, Blocks.bedrock, 0, 2); 
        if (dx == 0 && dz == 0) {
          this.worldServerInstance.setBlock(x, y, z, ExtraUtils.portal, 3, 2);
        } else if (max_r == r && min_r > 1) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 0, 2);
          this.worldServerInstance.setBlock(x + dx, y + 1, z + dz, bricks, bricksMeta1, 2);
          this.worldServerInstance.setBlock(x + dx, y + 2, z + dz, Blocks.fence, 0, 2);
        } else if (max_r == r) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 3, 2);
        } else if (max_r == 1) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta2, 2);
        } else if (min_r == 1) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta2, 2);
        } else if (max_r == r - 1 && dx != 0 && dz != 0) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 0, 2);
          this.worldServerInstance.setBlock(x + dx, y + 1, z + dz, (Block)Blocks.stone_slab, 5, 2);
        } else if (max_r == 5) {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 3, 2);
        } else {
          this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta3, 2);
        } 
      } 
    } 
    int lx = x + 3, lz = z + 3;
    int lh = 6;
    this.worldServerInstance.setBlock(lx, y + 1, lz, darkBricks, darkBricksMeta, 2);
    int i;
    for (i = 2; i < lh; i++)
      this.worldServerInstance.setBlock(lx, y + i, lz, Blocks.nether_brick_fence, 0, 2); 
    if (ExtraUtils.colorBlockRedstone != null) {
      this.worldServerInstance.setBlock(lx, y + lh, lz, (Block)ExtraUtils.colorBlockRedstone, 15, 2);
    } else {
      this.worldServerInstance.setBlock(lx, y + lh, lz, Blocks.redstone_block, 0, 2);
    } 
    for (i = 2; i < 6; i++)
      this.worldServerInstance.setBlock(lx + Facing.offsetsXForSide[i], y + lh, lz + Facing.offsetsZForSide[i], Blocks.nether_brick_fence, 0, 2); 
    this.worldServerInstance.setBlock(lx, y + lh + 2, lz, (Block)Blocks.stone_slab, 6, 2);
    this.worldServerInstance.setBlock(lx, y + lh + 1, lz, Blocks.lit_redstone_lamp, 0, 3);
    this.worldServerInstance.setBlock(x + r - 2, y + 1, z - r + 2, (Block)Blocks.cauldron, 3, 2);
    EntityVillager villager = new EntityVillager((World)this.worldServerInstance);
    villager.setLocationAndAngles(lx - 0.5D, (y + 2), lz - 0.5D, 0.0F, 0.0F);
    villager.setProfession(0);
    MerchantRecipeList list = villager.getRecipes((EntityPlayer)FakePlayerFactory.getMinecraft(this.worldServerInstance));
    list.clear();
    if (!lastVillagerTrades.isEmpty())
      for (MerchantRecipe merchantRecipe : lastVillagerTrades)
        list.addToListWithCheck(merchantRecipe);  
    villager.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
    villager.setCustomNameTag("The Last Villager");
    villager.forceSpawn = true;
    villager.func_110163_bv();
    this.worldServerInstance.spawnEntityInWorld((Entity)villager);
    entity.setLocationAndAngles(x + 0.5D, (y + 1), z + 0.5D, entity.rotationYaw, entity.rotationPitch);
    entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    return true;
  }
  
  public static final LinkedList<MerchantRecipe> lastVillagerTrades = new LinkedList<MerchantRecipe>();
  
  public void removeStalePortalLocations(long p_85189_1_) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\endoftime\TeleporterEndOfTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */