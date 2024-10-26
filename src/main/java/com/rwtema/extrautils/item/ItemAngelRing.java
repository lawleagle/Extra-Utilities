package com.rwtema.extrautils.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketAngelRingNotifier;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ItemAngelRing extends Item implements IBauble {
  public static boolean foundItem = false;

  public static Map<String, Integer> curFlyingPlayers = new HashMap<String, Integer>();

  public ItemAngelRing() {
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:angelRing");
    setTextureName("extrautils:angelRing");
    setHasSubtypes(true);
    setMaxStackSize(1);
  }

  static {
    EventHandlerRing handler = new EventHandlerRing();
    MinecraftForge.EVENT_BUS.register(handler);
    FMLCommonHandler.instance().bus().register(handler);
  }

  public static void addPlayer(EntityPlayer player, int i, boolean override) {
    String name = player.getGameProfile().getName();
    if (!curFlyingPlayers.containsKey(name) || curFlyingPlayers.get(name) == null || (override && ((Integer)curFlyingPlayers.get(name)).intValue() != i)) {
      curFlyingPlayers.put(name, Integer.valueOf(i));
      NetworkHandler.sendToAllPlayers((XUPacketBase)new PacketAngelRingNotifier(name, i));
    }
  }

  public static void removePlayer(EntityPlayer player) {
    String name = player.getGameProfile().getName();
    if (curFlyingPlayers.containsKey(name)) {
      curFlyingPlayers.remove(name);
      NetworkHandler.sendToAllPlayers((XUPacketBase)new PacketAngelRingNotifier(name, 0));
    }
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
    for (int i = 0; i < 5; i++)
      p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    par3List.add(("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(par1ItemStack) + "." + par1ItemStack.getItemDamage() + ".name")).trim());
    super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
  }

  public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean par5) {
    super.onUpdate(itemstack, world, entity, slot, par5);
    if (world.isRemote)
      return;
    if (!(entity instanceof EntityPlayerMP))
      return;
    NBTTagCompound nbt = XUHelper.getPersistantNBT(entity);
    nbt.setByte("XU|Flying", (byte)20);
    addPlayer((EntityPlayer)entity, itemstack.getItemDamage(), par5);
    if (!((EntityPlayerMP)entity).capabilities.allowFlying || !nbt.hasKey("XU|FlyingDim") || nbt.getInteger("XU|FlyingDim") != world.provider.dimensionId) {
      addPlayer((EntityPlayer)entity, itemstack.getItemDamage(), false);
      ((EntityPlayerMP)entity).capabilities.allowFlying = true;
      ((EntityPlayerMP)entity).sendPlayerAbilities();
    }
    nbt.setInteger("XU|FlyingDim", world.provider.dimensionId);
  }

  public BaubleType getBaubleType(ItemStack itemstack) {
    return BaubleType.RING;
  }

  public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
    onUpdate(itemstack, player.worldObj, (Entity)player, -1, true);
  }

  public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

  public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

  public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
    return true;
  }

  public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
    return true;
  }

  public static class EventHandlerRing {
    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      for (String name : MinecraftServer.getServer().getAllUsernames()) {
        if (ItemAngelRing.curFlyingPlayers.containsKey(name)) {
          NetworkHandler.sendPacketToPlayer((XUPacketBase)new PacketAngelRingNotifier(name, ((Integer)ItemAngelRing.curFlyingPlayers.get(name)).intValue()), event.player);
        } else {
          NetworkHandler.sendPacketToPlayer((XUPacketBase)new PacketAngelRingNotifier(name, 0), event.player);
        }
      }
    }

    @SubscribeEvent
    public void entTick(LivingEvent.LivingUpdateEvent event) {
      if (event.entity.worldObj.isRemote)
        return;
      ItemAngelRing.foundItem = true;
      if (!XUHelper.hasPersistantNBT(event.entity) || !XUHelper.getPersistantNBT(event.entity).hasKey("XU|Flying", 1))
        return;
      Byte t = Byte.valueOf(XUHelper.getPersistantNBT(event.entity).getByte("XU|Flying"));
      Byte byte_1 = t, byte_2 = t = Byte.valueOf((byte)(t.byteValue() - 1));
      if (t.byteValue() == 0) {
        XUHelper.getPersistantNBT(event.entity).removeTag("XU|Flying");
        if (event.entity instanceof EntityPlayerMP) {
          EntityPlayerMP entityPlayer = (EntityPlayerMP)event.entity;
          ItemAngelRing.removePlayer((EntityPlayer)entityPlayer);
          if (!entityPlayer.capabilities.isCreativeMode) {
            entityPlayer.capabilities.allowFlying = false;
            entityPlayer.capabilities.isFlying = false;
            entityPlayer.sendPlayerAbilities();
          }
        }
      } else {
        XUHelper.getPersistantNBT(event.entity).setByte("XU|Flying", t.byteValue());
      }
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemAngelRing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
