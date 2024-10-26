package com.rwtema.extrautils.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.UUID;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class ItemSoul extends Item {
  public ItemSoul() {
    setUnlocalizedName("extrautils:mini-soul");
    setTextureName("extrautils:mini-soul");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setMaxStackSize(1);
    setHasSubtypes(true);
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs p_150895_2_, List list) {
    list.add(new ItemStack(item, 1, 0));
    list.add(new ItemStack(item, 1, 3));
  }

  public ItemStack onItemRightClick(ItemStack item, World par3World, EntityPlayer player) {
    if (par3World.isRemote)
      return item;
    if (!EntityPlayerMP.class.equals(player.getClass()))
      return item;
    if (player.capabilities.isCreativeMode && item.getItemDamage() == 3) {
      AttributeModifier mod = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(uuid);
      if (mod != null)
        player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).removeModifier(mod);
      item.stackSize--;
      return item;
    }
    if (item.getItemDamage() == 0 || item.getItemDamage() == 3) {
      if (item.hasTagCompound()) {
        NBTTagCompound tag = item.getTagCompound();
        if (tag.hasKey("owner_id") && player.getGameProfile().getId() != null) {
          if (!player.getGameProfile().getId().toString().equals(tag.getString("owner_id")))
            return item;
        } else if (tag.hasKey("owner") && !player.getCommandSenderName().equals(tag.getString("owner"))) {
          return item;
        }
      }
      double l = 0.0D;
      IAttributeInstance a = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
      AttributeModifier attr = a.getModifier(uuid);
      if (attr != null)
        l = attr.getAmount();
      if (l > -0.1D)
        return item;
      l += 0.1D;
      a.removeModifier(attr);
      a.applyModifier(new AttributeModifier(uuid, "Missing Soul", l, 2));
      player.addChatComponentMessage((IChatComponent)new ChatComponentText("You feel strangely refreshed (+10% Max Health)"));
      item.stackSize--;
      return item;
    }
    return item;
  }

  public static final UUID uuid = UUID.fromString("12345678-9182-3532-aaaa-aaabacadabaa");

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return (par1ItemStack.getItemDamage() == 3);
  }

  public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
    super.onCreated(par1ItemStack, par2World, player);
    par1ItemStack.setItemDamage(1);
    if (!par2World.isRemote && XUHelper.isPlayerFake(player))
      return;
    NBTTagCompound tag = par1ItemStack.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound();
    tag.setString("owner", player.getCommandSenderName());
    if (player.getGameProfile().getId() != null)
      tag.setString("owner_id", player.getGameProfile().getId().toString());
    par1ItemStack.setTagCompound(tag);
    if (!par2World.isRemote)
      player.attackEntityFrom(DamageSource.magic, 0.0F);
    double l = 0.0D;
    IAttributeInstance a = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
    AttributeModifier attr = a.getModifier(uuid);
    if (attr != null)
      l = attr.getAmount();
    l -= 0.1D;
    double c = Math.min(Math.min(a.getBaseValue() * (1.0D + l), a.getAttributeValue()), 20.0D * (1.0D + l));
    if (c >= 6.0D) {
      par1ItemStack.setItemDamage(0);
      if (!par2World.isRemote) {
        HashMultimap hashMultimap = HashMultimap.create();
        hashMultimap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(uuid, "Missing Soul", l, 2));
        player.getAttributeMap().applyAttributeModifiers((Multimap)hashMultimap);
        player.addChatComponentMessage((IChatComponent)new ChatComponentText("You feel diminished (-10% Max Health)"));
      }
    }
    player.inventory.markDirty();
  }

  public static void updatePlayer(Entity player) {
    if (player instanceof EntityPlayerMP)
      ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
  }

  public void onUpdate(ItemStack item, World par2World, Entity par3Entity, int par4, boolean par5) {
    super.onUpdate(item, par2World, par3Entity, par4, par5);
    if (item.getItemDamage() == 2 && par3Entity instanceof EntityPlayerMP) {
      onCreated(item, par2World, (EntityPlayer)par3Entity);
      updatePlayer(par3Entity);
    }
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, List par3List, boolean par4) {
    super.addInformation(item, player, par3List, par4);
    NBTTagCompound tag = item.getTagCompound();
    if (item.getItemDamage() == 3)
      par3List.add("Soul of a forgotten deity");
    if (item.getItemDamage() == 1)
      par3List.add("Soul is too weak and has been spread too thin");
    if (tag == null)
      return;
    if (tag.hasKey("owner"))
      par3List.add("Owner: " + tag.getString("owner"));
  }

  public boolean hasCustomEntity(ItemStack stack) {
    return true;
  }

  public Entity createEntity(World world, Entity location, ItemStack itemstack) {
    if (itemstack.getItemDamage() == 2)
      location.setDead();
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemSoul.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
