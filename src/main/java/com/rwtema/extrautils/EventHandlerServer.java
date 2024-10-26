package com.rwtema.extrautils;

import com.rwtema.extrautils.item.ItemDivisionSigil;
import com.rwtema.extrautils.item.ItemGoldenBag;
import com.rwtema.extrautils.item.ItemSoul;
import com.rwtema.extrautils.item.ItemUnstableIngot;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.tileentity.IAntiMobTorch;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.oredict.OreDictionary;

public class EventHandlerServer {
  public static String debug = "";
  
  public static List<int[]> magnumTorchRegistry = (List)new ArrayList<int>();
  
  private String silverName = "nuggetSilver";
  
  public static boolean isInRangeOfTorch(Entity entity) {
    for (int[] coord : magnumTorchRegistry) {
      if (coord[0] == entity.worldObj.provider.dimensionId && 
        entity.worldObj.blockExists(coord[1], coord[2], coord[3]) && entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]) instanceof IAntiMobTorch) {
        TileEntity tile = entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
        double dx = (tile.xCoord + 0.5F) - entity.posX;
        double dy = (tile.yCoord + 0.5F) - entity.posY;
        double dz = (tile.zCoord + 0.5F) - entity.posZ;
        if ((dx * dx + dz * dz) / ((IAntiMobTorch)tile).getHorizontalTorchRangeSquared() + dy * dy / ((IAntiMobTorch)tile).getVerticalTorchRangeSquared() <= 1.0D)
          return true; 
      } 
    } 
    return false;
  }
  
  @SubscribeEvent
  public void soulDropping(LivingDropsEvent event) {
    int prob = 43046721;
    if (ExtraUtils.lawSwordEnabled && event.source.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer)event.source.getEntity();
      if (player.getHeldItem() != null && player.getHeldItem().getItem() == ExtraUtils.lawSword)
        prob /= 10; 
    } 
    if (ExtraUtils.soul != null && event.entityLiving instanceof net.minecraft.entity.monster.EntityMob && !event.entity.worldObj.isRemote && event.entity.worldObj.rand.nextInt(prob) == 0)
      event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entity.posY, event.entityLiving.posZ, new ItemStack((Item)ExtraUtils.soul, 1, 3))); 
  }
  
  @SubscribeEvent
  public void silverFishDrop(LivingDropsEvent event) {
    if (event.entityLiving instanceof net.minecraft.entity.monster.EntitySilverfish && !event.entity.worldObj.isRemote && event.entity.worldObj.rand.nextInt(5) == 0 && event.recentlyHit && 
      OreDictionary.getOres(this.silverName).size() > 0) {
      ItemStack item = ((ItemStack)OreDictionary.getOres(this.silverName).get(0)).copy();
      if (event.drops.size() > 0)
        for (int i = 0; i < event.drops.size(); i++) {
          ItemStack t = ((EntityItem)event.drops.get(i)).getEntityItem();
          if (t != null && (t.getItem() == item.getItem() || t.getItemDamage() == item.getItemDamage()))
            return; 
        }  
      event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entity.posY, event.entityLiving.posZ, item));
    } 
  }
  
  @SubscribeEvent
  public void netherDrop(LivingDropsEvent event) {
    if (ExtraUtils.divisionSigil != null && event.entityLiving instanceof net.minecraft.entity.boss.EntityWither && event.source.getSourceOfDamage() instanceof EntityPlayer && event.entity.worldObj != null && event.entity.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
      EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
      NBTTagCompound t = new NBTTagCompound();
      if (player.getEntityData().hasKey("PlayerPersisted")) {
        t = player.getEntityData().getCompoundTag("PlayerPersisted");
      } else {
        player.getEntityData().setTag("PlayerPersisted", (NBTBase)t);
      } 
      int kills = 0;
      if (t.hasKey("witherKills"))
        kills = t.getInteger("witherKills"); 
      kills++;
      t.setInteger("witherKills", kills);
      if (kills == 1 || !t.hasKey("hasSigil") || event.entity.worldObj.rand.nextInt(10) == 0) {
        ItemStack item = new ItemStack(ExtraUtils.divisionSigil);
        EntityItem entityitem = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, item);
        entityitem.delayBeforeCanPickup = 10;
        event.drops.add(entityitem);
      } 
    } 
  }
  
  @SubscribeEvent
  public void decoratePiEasterEgg(PopulateChunkEvent.Post event) {
    if (event.chunkX == 196349 && event.chunkZ == 22436 && event.world.provider.dimensionId == 0) {
      event.world.setBlock(3141592, 65, 358979, (Block)Blocks.chest);
      TileEntity chest = event.world.getTileEntity(3141592, 65, 358979);
      if (chest instanceof IInventory)
        ((IInventory)chest).setInventorySlotContents(0, new ItemStack(Items.pumpkin_pie)); 
    } 
  }
  
  @SubscribeEvent
  public void magnumTorchDenyTeleport(EnderTeleportEvent event) {
    if (event.entityLiving instanceof EntityEnderman && !((EntityEnderman)event.entityLiving).isScreaming())
      for (int[] coord : magnumTorchRegistry) {
        if (coord[0] == event.entity.worldObj.provider.dimensionId && 
          event.entity.worldObj.blockExists(coord[1], coord[2], coord[3]) && event.entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]) instanceof IAntiMobTorch) {
          TileEntity tile = event.entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
          double dx = (tile.xCoord + 0.5F) - event.targetX;
          double dy = (tile.yCoord + 0.5F) - event.targetY;
          double dz = (tile.zCoord + 0.5F) - event.targetZ;
          if ((dx * dx + dz * dz) / ((IAntiMobTorch)tile).getHorizontalTorchRangeSquared() + dy * dy / ((IAntiMobTorch)tile).getVerticalTorchRangeSquared() <= 1.0D) {
            double dx2 = (tile.xCoord + 0.5F) - event.entity.posX;
            double dy2 = (tile.yCoord + 0.5F) - event.entity.posY;
            double dz2 = (tile.zCoord + 0.5F) - event.entity.posZ;
            if (dx * dx + dy * dy + dz * dz < dx2 * dx2 + dy2 * dy2 + dz2 * dz2)
              event.setCanceled(true); 
          } 
        } 
      }  
  }
  
  @SubscribeEvent
  public void magnumTorchDenySpawn(LivingSpawnEvent.CheckSpawn event) {
    if (event.getResult() == Event.Result.ALLOW)
      return; 
    if (event.entityLiving.isCreatureType(EnumCreatureType.monster, false) && 
      isInRangeOfTorch(event.entity))
      event.setResult(Event.Result.DENY); 
  }
  
  @SubscribeEvent
  public void rainInformer(EntityJoinWorldEvent event) {
    if (event.world.isRemote || event.entity instanceof EntityPlayerMP);
  }
  
  @SubscribeEvent
  public void debugValueLoad(ServerChatEvent event) {
    debug = event.message;
  }
  
  @SubscribeEvent
  public void angelBlockDestroy(PlayerInteractEvent event) {
    if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && ExtraUtils.angelBlock != null && 
      event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == ExtraUtils.angelBlock && 
      event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(ExtraUtils.angelBlock)) && 
      !event.entityPlayer.worldObj.isRemote) {
      event.entityPlayer.worldObj.func_147480_a(event.x, event.y, event.z, false);
      event.setCanceled(true);
    } 
  }
  
  @SubscribeEvent
  public void unstableIngotDestroyer(EntityItemPickupEvent event) {
    if (ExtraUtils.unstableIngot != null && 
      event.item.getEntityItem().getItem() == ExtraUtils.unstableIngot && 
      event.item.getEntityItem().hasTagCompound() && (
      event.item.getEntityItem().getTagCompound().hasKey("crafting") || event.item.getEntityItem().getTagCompound().hasKey("time"))) {
      event.item.setDead();
      event.setCanceled(true);
    } 
  }
  
  @SubscribeEvent
  public void unstableIngotExploder(ItemTossEvent event) {
    if (ExtraUtils.unstableIngot != null && 
      event.entityItem.getEntityItem().getItem() == ExtraUtils.unstableIngot && 
      event.entityItem.getEntityItem().hasTagCompound() && 
      event.entityItem.getEntityItem().getTagCompound().hasKey("time"))
      ItemUnstableIngot.explode(event.player); 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void updateSoulDrain(LivingSpawnEvent event) {
    if (event.world.isRemote)
      return; 
    if (!EntityPlayerMP.class.equals(event.getClass()))
      return; 
    IAttributeInstance a = event.entityLiving.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
    AttributeModifier attr = a.getModifier(ItemSoul.uuid);
    if (attr == null || attr.getOperation() == 2)
      return; 
    double l = attr.getAmount() / 20.0D;
    a.removeModifier(attr);
    a.applyModifier(new AttributeModifier(ItemSoul.uuid, "Missing Soul", l, 0));
  }
  
  @SubscribeEvent
  public void persistSoulDrain(PlayerEvent.Clone event) {
    if (event.entityPlayer.worldObj.isRemote || ExtraUtils.permaSoulDrainOff)
      return; 
    EntityPlayer original = event.original;
    EntityPlayer clone = event.entityPlayer;
    AttributeModifier attr = original.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(ItemSoul.uuid);
    if (attr == null) {
      attr = clone.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(ItemSoul.uuid);
      if (attr != null)
        clone = event.original; 
    } 
    if (attr != null)
      try {
        clone.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).applyModifier(attr);
      } catch (IllegalArgumentException ignore) {} 
  }
  
  @SubscribeEvent
  public void updateEntity(LivingEvent.LivingUpdateEvent event) {
    if (event.entity.worldObj.isRemote)
      return; 
    if (event.entity.getEntityData().hasKey("CursedEarth")) {
      int c = event.entity.getEntityData().getInteger("CursedEarth");
      if (c == 0) {
        event.entity.setDead();
        NetworkHandler.sendParticle(event.entity.worldObj, "smoke", event.entity.posX, event.entity.posY + (event.entity.height / 4.0F), event.entity.posZ, 0.0D, 0.0D, 0.0D, false);
      } else {
        c--;
        event.entity.getEntityData().setInteger("CursedEarth", c);
      } 
    } 
  }
  
  @SubscribeEvent
  public void ActivationRitual(LivingDeathEvent event) {
    if (ExtraUtils.divisionSigil == null && ExtraUtils.cursedEarth == null)
      return; 
    if (!(event.source.getSourceOfDamage() instanceof EntityPlayer))
      return; 
    EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
    if (ExtraUtils.divisionSigil != null) {
      boolean flag = false;
      for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
        ItemStack item = player.inventory.getStackInSlot(j);
        if (item != null && item.getItem() == ExtraUtils.divisionSigil) {
          flag = true;
          break;
        } 
      } 
      if (!flag)
        return; 
    } 
    World world = event.entityLiving.worldObj;
    if (world.isRemote)
      return; 
    int x = (int)event.entityLiving.posX;
    int y = (int)event.entityLiving.boundingBox.minY;
    int z = (int)event.entityLiving.posZ;
    boolean found = false;
    long time = world.getWorldTime() % 24000L;
    if (time < 12000L || time > 24000L)
      return; 
    int dx = -2;
    while (true) {
      if (((!found ? 1 : 0) & ((dx <= 2) ? 1 : 0)) != 0) {
        int dy = -2;
        for (;; dx++) {
          if (((!found ? 1 : 0) & ((dy <= 0) ? 1 : 0)) != 0) {
            int dz = -2;
            for (;; dy++) {
              if (((!found ? 1 : 0) & ((dz <= 2) ? 1 : 0)) != 0) {
                if (world.getBlock(x + dx, y + dy, z + dz) == Blocks.enchanting_table) {
                  found = true;
                  x += dx;
                  y += dy;
                  z += dz;
                } 
                dz++;
                continue;
              } 
            } 
            break;
          } 
        } 
      } 
      break;
    } 
    if (!found)
      return; 
    if (world.getBlock(x, y, z) != Blocks.enchanting_table)
      return; 
    if (!ActivationRitual.redstoneCirclePresent(world, x, y, z))
      return; 
    if (!ActivationRitual.altarCanSeeMoon(world, x, y, z)) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar cannot see the moon");
      return;
    } 
    if (!ActivationRitual.altarOnEarth(world, x, y, z)) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar and circle must be on natural earth");
      return;
    } 
    if (!ActivationRitual.altarInDarkness(world, x, y, z)) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar is too brightly lit");
      return;
    } 
    if (!ActivationRitual.naturalEarth(world, x, y, z)) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar requires more natural earth");
      return;
    } 
    int i = ActivationRitual.checkTime(world.getWorldTime());
    if (i == -1) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Too early");
      return;
    } 
    if (i == 1) {
      PacketTempChat.sendChat(player, "Activation Ritual Failure: Too late");
      return;
    } 
    ActivationRitual.startRitual(world, x, y, z, player);
    NetworkHandler.sendSoundEvent(world, 0, x + 0.5F, y + 0.5F, z + 0.5F);
    if (ExtraUtils.divisionSigil != null)
      for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
        ItemStack item = player.inventory.getStackInSlot(j);
        if (item != null && item.getItem() == ExtraUtils.divisionSigil) {
          NBTTagCompound tags;
          if (item.hasTagCompound()) {
            tags = item.getTagCompound();
          } else {
            tags = new NBTTagCompound();
          } 
          tags.setInteger("damage", ItemDivisionSigil.maxdamage);
          item.setTagCompound(tags);
        } 
      }  
  }
  
  @SubscribeEvent
  public void goldenLasso(EntityInteractEvent event) {
    ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();
    if (itemstack != null && 
      ExtraUtils.goldenLasso != null && 
      itemstack.getItem() == ExtraUtils.goldenLasso && 
      event.target instanceof EntityLivingBase && 
      itemstack.interactWithEntity(event.entityPlayer, (EntityLivingBase)event.target)) {
      if (itemstack.stackSize <= 0)
        event.entityPlayer.destroyCurrentEquippedItem(); 
      event.setCanceled(true);
    } 
  }
  
  @SubscribeEvent
  public void captureGoldenBagDrop(PlayerDropsEvent event) {
    NBTTagCompound e;
    if (ExtraUtils.goldenBag == null)
      return; 
    if (event.entity.getEntityData().getCompoundTag("PlayerPersisted").hasKey("XU|GoldenBags"))
      return; 
    int j = 0;
    NBTTagCompound t = new NBTTagCompound();
    for (int i = 0; i < event.drops.size(); i++) {
      ItemStack item = ((EntityItem)event.drops.get(i)).getEntityItem();
      if (item != null && item.getItem() == ExtraUtils.goldenBag && ItemGoldenBag.isMagic(item)) {
        NBTTagCompound tags = new NBTTagCompound();
        item.writeToNBT(tags);
        t.setTag("item_" + j, (NBTBase)tags);
        j++;
        event.drops.remove(i);
        i--;
      } 
    } 
    t.setInteger("no_items", j);
    if (event.entityPlayer.getEntityData().hasKey("PlayerPersisted")) {
      e = event.entityPlayer.getEntityData().getCompoundTag("PlayerPersisted");
    } else {
      e = new NBTTagCompound();
      event.entityPlayer.getEntityData().setTag("PlayerPersisted", (NBTBase)e);
    } 
    e.setTag("XU|GoldenBags", (NBTBase)t);
  }
  
  @SubscribeEvent
  public void recreateGoldenBags(EntityJoinWorldEvent event) {
    if (event.world.isRemote || ExtraUtils.goldenBag == null)
      return; 
    if (event.entity instanceof EntityPlayer && 
      event.entity.getEntityData().hasKey("PlayerPersisted") && 
      event.entity.getEntityData().getCompoundTag("PlayerPersisted").hasKey("XU|GoldenBags")) {
      NBTTagCompound tags = event.entity.getEntityData().getCompoundTag("PlayerPersisted").getCompoundTag("XU|GoldenBags");
      int n = tags.getInteger("no_items");
      for (int i = 0; i < n; i++) {
        ItemStack item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("item_" + i));
        if (item != null)
          ((EntityPlayer)event.entity).inventory.addItemStackToInventory(ItemGoldenBag.clearMagic(item)); 
      } 
      event.entity.getEntityData().getCompoundTag("PlayerPersisted").removeTag("XU|GoldenBags");
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\EventHandlerServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */