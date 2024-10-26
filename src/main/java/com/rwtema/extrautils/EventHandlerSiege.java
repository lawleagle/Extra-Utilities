package com.rwtema.extrautils;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.item.ItemDivisionSigil;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;

public class EventHandlerSiege {
  public static final int numKills = 100;

  public static final int[] ddx = new int[] { -1, 0, 1, 0 };

  public static final int[] ddz = new int[] { 0, -1, 0, 1 };

  public static List<String> SiegeParticipants = new ArrayList<String>();

  public static List<BiomeGenBase.SpawnListEntry> mobSpawns = new ArrayList<BiomeGenBase.SpawnListEntry>();

  public static ItemStack[] earthItems;

  public static ItemStack[] fireItems;

  static {
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 200, 3, 3));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityCaveSpider.class, 40, 4, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 80, 4, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 200, 4, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 200, 4, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 80, 2, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 40, 4, 4));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 40, 1, 3));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntitySilverfish.class, 40, 3, 3));
    mobSpawns.add(new BiomeGenBase.SpawnListEntry(EntityGiantZombie.class, 5, 1, 1));
  }

  private static Random rand = (Random)XURandom.getInstance();

  public static void endSiege(World world, boolean announce) {
    if (world.isRemote)
      return;
    for (int i = 0; i < world.loadedEntityList.size(); i++) {
      if (world.loadedEntityList.get(i) instanceof EntityMob && (
        (EntityMob)world.loadedEntityList.get(i)).getEntityData().hasKey("Siege"))
        world.removeEntity((Entity)world.loadedEntityList.get(i));
    }
    if (announce)
      MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentText("The Siege has ended in 'The End'"));
  }

  public static void upgradeSigil(EntityPlayer player) {
    for (int j = 0; j < player.inventory.mainInventory.length; j++) {
      if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == ExtraUtils.divisionSigil) {
        if (player.inventory.mainInventory[j].hasTagCompound() && player.inventory.mainInventory[j].getTagCompound().hasKey("damage"))
          player.inventory.mainInventory[j] = ItemDivisionSigil.newStableSigil();
        return;
      }
    }
  }

  public static void beginSiege(World world) {
    if (world.isRemote)
      return;
    if (world.provider.dimensionId != 1)
      return;
    for (int i = 0; i < world.loadedEntityList.size(); i++) {
      if (world.loadedEntityList.get(i) instanceof EntityMob) {
        world.removeEntity((Entity)world.loadedEntityList.get(i));
      } else if (world.loadedEntityList.get(i) instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) world.loadedEntityList.get(i);
        SiegeParticipants.add(player.getGameProfile().getName());
        player.getEntityData().setInteger("SiegeKills", 0);
      }
    }
    if (SiegeParticipants.size() != 0) {
      MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentText("The Siege has begun in 'The End'"));
    } else {
      endSiege(world, false);
    }
  }

  public static boolean hasSigil(EntityPlayer player) {
    for (int j = 0; j < player.inventory.mainInventory.length; j++) {
      if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == ExtraUtils.divisionSigil &&
        player.inventory.mainInventory[j].hasTagCompound() && player.inventory.mainInventory[j].getTagCompound().hasKey("damage"))
        return true;
    }
    return false;
  }

  public static void checkPlayers() {
    WorldServer worldServer = DimensionManager.getWorld(1);
    if (worldServer == null || ((World)worldServer).isRemote) {
      SiegeParticipants.clear();
      return;
    }
    if (SiegeParticipants.size() > 0) {
      for (int i = 0; i < SiegeParticipants.size(); i++) {
        if (worldServer.getPlayerEntityByName(SiegeParticipants.get(i)) == null) {
          SiegeParticipants.remove(SiegeParticipants.get(i));
          i--;
        }
      }
      if (SiegeParticipants.size() == 0)
        endSiege((World)worldServer, true);
    }
  }

  public static int[] getStrength(World world, int x, int y, int z) {
    List<ChunkPos> rs = new ArrayList<ChunkPos>();
    List<ChunkPos> st = new ArrayList<ChunkPos>();
    rs.add(new ChunkPos(x, y, z));
    st.add(new ChunkPos(x, y, z));
    int maxDist = 0;
    BlockRedstoneWire blockRedstoneWire = Blocks.redstone_wire;
    Block stId = Blocks.tripwire;
    int k2 = 0;
    for (int i = 0; i < rs.size(); i++) {
      for (int m = 0; m < 4; m++) {
        ChunkPos nPos = new ChunkPos(((ChunkPos)rs.get(i)).x + ddx[m], y, ((ChunkPos)rs.get(i)).z + ddz[m]);
        int n = mDist(nPos.x - x, nPos.z - z);
        if (n < 16 && world.getBlock(nPos.x, y, nPos.z) == blockRedstoneWire && !rs.contains(nPos)) {
          rs.add(nPos);
          if (world.getBlockMetadata(nPos.x, y, nPos.z) != 0)
            k2++;
          if (n > maxDist)
            maxDist = n;
        }
      }
    }
    rs.remove(new ChunkPos(x, y, z));
    int k = 0;
    for (int j = 0; j < st.size(); j++) {
      for (int m = 0; m < 4; m++) {
        ChunkPos nPos = new ChunkPos(((ChunkPos)st.get(j)).x + ddx[m], y, ((ChunkPos)st.get(j)).z + ddz[m]);
        int n = mDist(nPos.x - x, nPos.z - z);
        if (n < 16)
          if (world.getBlock(nPos.x, y, nPos.z) == stId && !st.contains(nPos)) {
            st.add(nPos);
            if (n > maxDist)
              maxDist = n;
          } else if (j != 0 && world.getBlock(nPos.x, y, nPos.z) == blockRedstoneWire && rs.contains(nPos)) {
            k++;
          }
      }
    }
    return new int[] { k, maxDist * maxDist * 4 };
  }

  public static int mDist(int x, int z) {
    if (x < 0)
      x *= -1;
    if (z < 0)
      z *= -1;
    return (x > z) ? x : z;
  }

  public static int checkChestList(IInventory chest, ItemStack[] items, boolean destroy) {
    boolean[] check = new boolean[items.length];
    int s = 0;
    for (int i = 0; i < chest.getSizeInventory() && s < items.length; i++) {
      if (chest.getStackInSlot(i) != null)
        for (int j = 0; j < items.length && (!destroy || chest.getStackInSlot(i) != null); j++) {
          if (!check[j] && XUHelper.canItemsStack(items[j], chest.getStackInSlot(i), false, true)) {
            if (destroy)
              chest.setInventorySlotContents(i, null);
            check[j] = true;
            s++;
            break;
          }
        }
    }
    return s;
  }

  public static int checkChestEarth(IInventory chest, boolean destroy) {
    if (chest == null)
      return 0;
    if (earthItems == null)
      earthItems = new ItemStack[] {
          new ItemStack(Blocks.coal_ore), new ItemStack(Blocks.gold_ore), new ItemStack(Blocks.iron_ore), new ItemStack(Blocks.lapis_ore), new ItemStack(Blocks.diamond_ore), new ItemStack(Blocks.emerald_ore), new ItemStack(Blocks.redstone_ore), new ItemStack((Block)Blocks.grass), new ItemStack(Blocks.dirt), new ItemStack(Blocks.clay),
          new ItemStack((Block)Blocks.sand), new ItemStack(Blocks.gravel), new ItemStack(Blocks.obsidian) };
    return checkChestList(chest, earthItems, destroy);
  }

  public static int checkChestFire(IInventory chest, boolean destroy) {
    if (chest == null)
      return 0;
    if (fireItems == null)
      fireItems = new ItemStack[] {
          new ItemStack(Items.coal, 1, 1), new ItemStack(Blocks.stone), new ItemStack(Items.brick), new ItemStack(Items.cooked_fished), new ItemStack(Blocks.glass), new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.baked_potato), new ItemStack(Items.netherbrick), new ItemStack(Items.dye, 1, 2),
          new ItemStack(Blocks.hardened_clay), new ItemStack(Items.cooked_porkchop), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_chicken) };
    return checkChestList(chest, fireItems, destroy);
  }

  public static int checkChestWater(IInventory chest, boolean destroy) {
    if (chest == null)
      return 0;
    List<PotionEffect> numEffects = new ArrayList<PotionEffect>();
    for (int i = 0; i < chest.getSizeInventory() && numEffects.size() < 12; i++) {
      if (chest.getStackInSlot(i) != null && chest.getStackInSlot(i).getItem() == Items.potionitem) {
        List temp = Items.potionitem.getEffects(chest.getStackInSlot(i));
        if (temp != null)
          for (Object aTemp : temp) {
            if (!numEffects.contains(aTemp)) {
              numEffects.add((PotionEffect)aTemp);
              if (destroy)
                chest.setInventorySlotContents(i, null);
            }
          }
      }
    }
    return numEffects.size();
  }

  public static int checkChestAir(IInventory chest, boolean destroy) {
    if (chest == null)
      return 0;
    int s = 0;
    List<ItemStack> pt = OreDictionary.getOres("record");
    HashSet<Item> items = new HashSet<Item>();
    for (int i = 0; i < chest.getSizeInventory() && s < 12; i++) {
      if (chest.getStackInSlot(i) != null) {
        ItemStack item = chest.getStackInSlot(i);
        if (!items.contains(item.getItem())) {
          boolean flag = false;
          for (ItemStack ore : pt) {
            if (OreDictionary.itemMatches(item, ore, false)) {
              flag = true;
              break;
            }
          }
          if (flag) {
            if (destroy)
              chest.setInventorySlotContents(i, null);
            items.add(item.getItem());
            s++;
          }
        }
      }
    }
    return s;
  }

  @SubscribeEvent
  public void Siege(EntityJoinWorldEvent event) {
    if (event.world.isRemote)
      return;
    checkPlayers();
    if (event.entity instanceof EntityPlayer)
      if (event.world.provider.dimensionId != 1) {
        if (event.entity.getEntityData().hasKey("SiegeKills")) {
          event.entity.getEntityData().removeTag("SiegeKills");
          SiegeParticipants.remove(((EntityPlayer)event.entity).getGameProfile().getName());
        }
      } else if (event.entity.getEntityData().hasKey("SiegeKills") &&
        !SiegeParticipants.contains(((EntityPlayer)event.entity).getGameProfile().getName())) {
        SiegeParticipants.add(((EntityPlayer)event.entity).getGameProfile().getName());
      }
  }

  public double sq(double x, double y, double z) {
    return x * x + z * z + y * y;
  }

  @SubscribeEvent
  public void golemDeath(LivingDeathEvent event) {
    if (!event.entity.worldObj.isRemote && event.entity.worldObj.provider.dimensionId == 1 &&
      event.entity instanceof net.minecraft.entity.monster.EntityIronGolem &&
      event.source.getSourceOfDamage() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
      if (!hasSigil(player))
        return;
      List t = event.entity.worldObj.loadedTileEntityList;
      for (Object aT : t) {
        if (aT.getClass().equals(TileEntityBeacon.class)) {
          TileEntityBeacon beacon = (TileEntityBeacon)aT;
          int x = beacon.xCoord, y = beacon.yCoord, z = beacon.zCoord;
          if (sq(event.entity.posX - x - 0.5D, event.entity.posY - y - 0.5D, event.entity.posZ - z - 0.5D) < 300.0D) {
            int[] s = getStrength(event.entity.worldObj, x, y, z);
            World world = beacon.getWorldObj();
            if (s[0] == 64) {
              int debug = 1;
              boolean flag = true;
              if (checkChestFire(TileEntityHopper.func_145893_b(world, x, y, (z - 5)), false) < debug)
                flag = false;
              if (flag && checkChestEarth(TileEntityHopper.func_145893_b(world, x, y, (z + 5)), false) < debug)
                flag = false;
              if (flag && checkChestAir(TileEntityHopper.func_145893_b(world, (x - 5), y, z), false) < debug)
                flag = false;
              if (flag && checkChestWater(TileEntityHopper.func_145893_b(world, (x + 5), y, z), false) < debug)
                flag = false;
              if (flag) {
                world.func_147480_a(x, y, z, false);
                int j;
                for (j = 0; j < 4; j++)
                  world.func_147480_a(x + ddx[j] * 5, y, z + ddz[j] * 5, false);
                world.func_147480_a(x, y, z, false);
                world.createExplosion(null, x, y, z, 6.0F, true);
                for (j = 0; j < 4; j++)
                  world.createExplosion(null, (x + ddx[j] * 5), y, (z + ddz[j] * 5), 3.0F, true);
                beginSiege(world);
                return;
              }
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void SiegeDeath(LivingDeathEvent event) {
    if (SiegeParticipants.isEmpty())
      return;
    if (event.entityLiving.worldObj.isRemote || event.entityLiving.worldObj.provider.dimensionId != 1)
      return;
    if (event.entityLiving instanceof EntityPlayer)
      checkPlayers();
    if (event.entityLiving instanceof EntityMob && event.source.getSourceOfDamage() instanceof EntityPlayer && event.entityLiving.getEntityData().hasKey("Siege")) {
      EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
      if (player != null && SiegeParticipants.contains(player.getGameProfile().getName()) && hasSigil(player)) {
        if (player.getEntityData().hasKey("SiegeKills")) {
          player.getEntityData().setInteger("SiegeKills", player.getEntityData().getInteger("SiegeKills") + 1);
        } else {
          player.getEntityData().setInteger("SiegeKills", 1);
        }
        int kills = player.getEntityData().getInteger("SiegeKills");
        if (kills > 100) {
          upgradeSigil(player);
          player.getEntityData().removeTag("SiegeKills");
          SiegeParticipants.remove(player.getGameProfile().getName());
          PacketTempChat.sendChat(player, "Your Sigil has stabilized");
        } else {
          PacketTempChat.sendChat(player, "Kills: " + player.getEntityData().getInteger("SiegeKills"));
        }
      }
    } else if (!(event.entityLiving instanceof EntityPlayer) || SiegeParticipants.contains(((EntityPlayer)event.entityLiving).getGameProfile().getName())) {

    }
  }

  @SubscribeEvent
  public void SiegePotentialSpawns(WorldEvent.PotentialSpawns event) {
    if (!event.world.isRemote && event.world.provider.dimensionId == 1 && event.type == EnumCreatureType.monster) {
      checkPlayers();
      if (SiegeParticipants.isEmpty()) {
        event.list.removeAll(mobSpawns);
      } else if (event.list.size() < mobSpawns.size()) {
        event.list.addAll(mobSpawns);
      }
    }
  }

  @SubscribeEvent
  public void Siege(LivingEvent.LivingUpdateEvent event) {
    if (event.entity.worldObj.isRemote)
      return;
    if (SiegeParticipants.isEmpty()) {
      if (event.entityLiving.getEntityData().hasKey("Siege")) {
        event.entity.setDead();
        endSiege(event.entity.worldObj, true);
      }
      return;
    }
    if (event.entityLiving.worldObj.rand.nextInt(1000) == 0)
      checkPlayers();
    if (event.entityLiving.worldObj.provider.dimensionId != 1)
      return;
    if (event.entityLiving instanceof EntityMob && ((EntityMob)event.entityLiving).getAttackTarget() == null && event.entityLiving.getEntityData().hasKey("Siege")) {
      int i = rand.nextInt(SiegeParticipants.size());
      EntityPlayer player = event.entityLiving.worldObj.getPlayerEntityByName(SiegeParticipants.get(i));
      if (player != null) {
        ((EntityMob)event.entityLiving).setAttackTarget((EntityLivingBase)player);
        ((EntityMob)event.entityLiving).setTarget((Entity)player);
      } else {
        SiegeParticipants.remove(i);
      }
    }
    if (event.entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer)event.entityLiving;
      if (player.motionY == 0.0D && player.fallDistance == 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && player.ridingEntity == null)
        player.attackEntityFrom(DamageSource.generic, 0.5F);
    }
  }

  @SubscribeEvent
  public void SiegeCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
    if (SiegeParticipants.isEmpty())
      return;
    if (event.entity.worldObj.isRemote)
      return;
    if (event.world.provider.dimensionId != 1)
      return;
    if (event.entityLiving instanceof EntityMob &&
      event.entityLiving.worldObj.checkNoEntityCollision(event.entityLiving.boundingBox) && event.entityLiving.worldObj.getCollidingBoundingBoxes((Entity)event.entityLiving, event.entityLiving.boundingBox).isEmpty() && !event.entityLiving.worldObj.isAnyLiquid(event.entityLiving.boundingBox)) {
      event.entityLiving.getEntityData().setBoolean("Siege", true);
      event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 7200, 2));
      event.setResult(Event.Result.ALLOW);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\EventHandlerSiege.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
