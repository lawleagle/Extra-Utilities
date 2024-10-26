package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.texture.TextureColorBlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGoldenBag extends Item {
  public IIcon bwIcon;
  
  public ItemGoldenBag() {
    setUnlocalizedName("extrautils:golden_bag");
    setTextureName("extrautils:golden_bag");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setMaxStackSize(1);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    super.registerIcons(par1IIconRegister);
    String t = getIconString();
    this.bwIcon = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry("extrautils:bw_(" + t + ")");
    if (this.bwIcon == null) {
      TextureColorBlockBase t2 = new TextureColorBlockBase(t, "items");
      t2.scale = 20.0F;
      this.bwIcon = (IIcon)t2;
      ((TextureMap)par1IIconRegister).setTextureEntry("extrautils:bw_(" + t + ")", (TextureAtlasSprite)t2);
    } 
  }
  
  public static boolean isMagic(ItemStack item) {
    return (item.hasTagCompound() && item.getTagCompound().hasKey("enchanted"));
  }
  
  public static void setMagic(ItemStack item) {
    NBTTagCompound tag = item.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound(); 
    tag.setBoolean("enchanted", true);
    item.setTagCompound(tag);
  }
  
  public static ItemStack clearMagic(ItemStack item) {
    if (item == null)
      return null; 
    NBTTagCompound tag = item.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound(); 
    if (tag.hasKey("enchanted")) {
      tag.removeTag("enchanted");
      if (tag.hasNoTags())
        tag = null; 
      item.setTagCompound(tag);
    } 
    return item;
  }
  
  public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
    if (!par2World.isRemote && !XUHelper.isPlayerFake(par3EntityPlayer))
      par3EntityPlayer.openGui(ExtraUtilsMod.instance, 1, par2World, par3EntityPlayer.inventory.currentItem, 0, 0); 
    return par1ItemStack;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return isMagic(par1ItemStack);
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
    if (isMagic(par1ItemStack))
      par3List.add("Reincarnating I"); 
  }
  
  public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
    return getColor(p_82790_1_);
  }
  
  public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
    return getIconIndex(stack);
  }
  
  public IIcon getIconIndex(ItemStack item) {
    return hasColor(item) ? this.bwIcon : super.getIconIndex(item);
  }
  
  public boolean hasColor(ItemStack item) {
    return (item.hasTagCompound() && item.getTagCompound().hasKey("Color"));
  }
  
  public int getColor(ItemStack item) {
    return hasColor(item) ? item.getTagCompound().getInteger("Color") : 16777215;
  }
  
  public ItemStack setColor(ItemStack item, int color) {
    NBTTagCompound tag = item.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound(); 
    tag.setInteger("Color", color);
    item.setTagCompound(tag);
    return item;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemGoldenBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */