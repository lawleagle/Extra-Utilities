package com.rwtema.extrautils.item;

import cofh.api.item.ISpecialFilterFluid;
import cofh.api.item.ISpecialFilterItem;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.ICreativeTabSorting;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.filters.AdvancedNodeUpgrades;
import com.rwtema.extrautils.item.filters.Matcher;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ItemNodeUpgrade extends Item implements ICreativeTabSorting, ISpecialFilterItem, ISpecialFilterFluid {
  private static final int numUpgrades = 11;
  
  private IIcon[] icons = new IIcon[11];
  
  public ItemNodeUpgrade() {
    setHasSubtypes(true);
    setUnlocalizedName("extrautils:nodeUpgrade");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }
  
  public static boolean hasKey(ItemStack filter, String key) {
    if (filter != null) {
      NBTTagCompound tags = filter.getTagCompound();
      if (tags != null && 
        tags.hasKey(key))
        return true; 
    } 
    return false;
  }
  
  public static boolean getPolarity(ItemStack filter) {
    return hasKey(filter, "Inverted");
  }
  
  public static boolean getFuzzyMetadata(ItemStack filter) {
    return hasKey(filter, "FuzzyMeta");
  }
  
  public static boolean getFuzzyNBT(ItemStack filter) {
    return hasKey(filter, "FuzzyNBT");
  }
  
  public static boolean matchesFilterBuffer(INodeBuffer item, ItemStack filter) {
    if (item == null)
      return false; 
    Object buffer = item.getBuffer();
    if (buffer == null)
      return false; 
    if (buffer instanceof ItemStack)
      return matchesFilterItem((ItemStack)buffer, filter); 
    return (!(buffer instanceof FluidTank) || matchesFilterLiquid(((FluidTank)buffer).getFluid(), filter));
  }
  
  public static boolean isFilter(ItemStack filter) {
    if (filter == null)
      return false; 
    if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade)
      return (filter.getItemDamage() == 1 || filter.getItemDamage() == 10); 
    return filter.getItem() instanceof ISpecialFilterItem;
  }
  
  public static boolean matchesFilterItem(ItemStack item, ItemStack filter) {
    if (item == null || item.getItem() == null || filter == null)
      return false; 
    if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade) {
      if (filter.getItemDamage() == 1) {
        boolean polarity = !getPolarity(filter);
        boolean fuzzyMeta = getFuzzyMetadata(filter);
        boolean fuzzyNBT = getFuzzyNBT(filter);
        NBTTagCompound tags = filter.getTagCompound();
        if (tags != null)
          for (int i = 0; i < 9; i++) {
            if (tags.hasKey("items_" + i)) {
              ItemStack f = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("items_" + i));
              if (f != null) {
                if (XUHelper.canItemsStack(f, item, fuzzyMeta, true, fuzzyNBT))
                  return polarity; 
                if (isFilter(f) && 
                  matchesFilterItem(item, f))
                  return polarity; 
              } 
            } 
          }  
        return !polarity;
      } 
      if (filter.getItemDamage() == 10) {
        Matcher matcher = AdvancedNodeUpgrades.getMatcher(filter);
        return (matcher.matchItem(item) != getPolarity(filter));
      } 
    } 
    if (filter.getItem() instanceof ISpecialFilterItem)
      return ((ISpecialFilterItem)filter.getItem()).matchesItem(filter, item); 
    return XUHelper.canItemsStack(item, filter, false, true);
  }
  
  public static boolean matchesFilterLiquid(FluidStack fluid, ItemStack filter) {
    if (fluid == null || filter == null)
      return false; 
    if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade) {
      if (filter.getItemDamage() == 1) {
        boolean polarity = !getPolarity(filter);
        NBTTagCompound tags = filter.getTagCompound();
        if (tags != null)
          for (int i = 0; i < 9; i++) {
            if (tags.hasKey("items_" + i)) {
              ItemStack f = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("items_" + i));
              if (f != null) {
                if (fluid.isFluidEqual(f))
                  return polarity; 
                if (isFilter(f) && 
                  matchesFilterLiquid(fluid, f))
                  return polarity; 
              } 
            } 
          }  
        return !polarity;
      } 
      if (filter.getItemDamage() == 10) {
        Matcher matcher = AdvancedNodeUpgrades.getMatcher(filter);
        return (matcher.matchFluid(fluid) != getPolarity(filter));
      } 
    } 
    if (filter.getItem() instanceof ISpecialFilterFluid)
      ((ISpecialFilterFluid)filter.getItem()).matchesFluid(filter, fluid); 
    return fluid.isFluidEqual(filter);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.icons[0] = par1IIconRegister.registerIcon("extrautils:nodeUpgrade");
    this.icons[1] = par1IIconRegister.registerIcon("extrautils:filter");
    this.icons[2] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeMining");
    this.icons[3] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeStack");
    this.icons[4] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeCreative");
    this.icons[5] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeEnder");
    this.icons[6] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeEnderReceiver");
    this.icons[7] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeDepth");
    this.icons[8] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeBreadth");
    this.icons[9] = par1IIconRegister.registerIcon("extrautils:nodeUpgradePatience");
    this.icons[10] = par1IIconRegister.registerIcon("extrautils:filter2");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    return this.icons[par1 % 11];
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int i = 0; i < 11; i++)
      par3List.add(new ItemStack(par1, 1, i)); 
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
    if (item.getItemDamage() == 1)
      if (item.getTagCompound() != null) {
        if (getPolarity(item))
          par3List.add("Inverted"); 
        if (getFuzzyMetadata(item))
          par3List.add("Fuzzy - Ignores Metadata"); 
        if (getFuzzyNBT(item))
          par3List.add("Fuzzy - Ignores NBT"); 
        for (int i = 0; i < 9; i++) {
          if (item.getTagCompound().hasKey("items_" + i)) {
            ItemStack temp = ItemStack.loadItemStackFromNBT(item.getTagCompound().getCompoundTag("items_" + i));
            List<String> tempList = temp.getTooltip(par2EntityPlayer, false);
            for (int j = 0; j < tempList.size(); j++) {
              if (j == 0) {
                par3List.add("  " + tempList.get(j));
              } else {
                par3List.add("     " + tempList.get(j));
              } 
            } 
            tempList.clear();
          } 
        } 
      } else {
        par3List.add(EnumChatFormatting.ITALIC + "Right click to select items to filter" + EnumChatFormatting.RESET);
        par3List.add(EnumChatFormatting.ITALIC + "Filters can be placed within other filters to create advanced behaviours" + EnumChatFormatting.RESET);
        par3List.add(EnumChatFormatting.ITALIC + "Craft with" + EnumChatFormatting.RESET);
      }  
    if (item.getItemDamage() == 10) {
      Matcher matcher = AdvancedNodeUpgrades.getMatcher(item);
      par3List.add("Filter Program: " + matcher.getLocalizedName());
      if (getPolarity(item)) {
        par3List.add("Inverted");
      } else if (matcher == AdvancedNodeUpgrades.nullMatcher) {
        par3List.add(EnumChatFormatting.ITALIC + "Right-click to change Filter Program" + EnumChatFormatting.RESET);
        par3List.add(EnumChatFormatting.ITALIC + "Craft with a redstone torch to Invert" + EnumChatFormatting.RESET);
        par3List.add(EnumChatFormatting.ITALIC + "Can be placed in normal filters to create advanced behaviours" + EnumChatFormatting.RESET);
      } 
    } 
    if (item.getItemDamage() == 5 || item.getItemDamage() == 6) {
      par3List.set(0, ((String)par3List.get(0)).replaceFirst(EnumChatFormatting.ITALIC + item.getDisplayName() + EnumChatFormatting.RESET, getItemStackDisplayName(item)));
      if (!item.hasDisplayName()) {
        par3List.add("Unspecified Frequency: You must name this upgrade in an anvil to choose a frequency");
        par3List.add("You cannot use this upgrade until it has a frequency");
      } else {
        par3List.add("Frequency: " + item.getDisplayName());
        String s = XUHelper.getPlayerOwner(item);
        if (s.equals("")) {
          par3List.add("Public Spectrum");
        } else {
          par3List.add("Private Spectrum - " + s);
        } 
      } 
    } 
  }
  
  public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
    if (!par2World.isRemote) {
      Matcher matcher;
      switch (par1ItemStack.getItemDamage()) {
        case 1:
          par3EntityPlayer.openGui(ExtraUtilsMod.instance, 1, par2World, par3EntityPlayer.inventory.currentItem, 0, 0);
          break;
        case 5:
        case 6:
          if (XUHelper.getPlayerOwner(par1ItemStack).equals("")) {
            PacketTempChat.sendChat(par3EntityPlayer, (IChatComponent)new ChatComponentText("Spectrum set to private"));
            XUHelper.setPlayerOwner(par1ItemStack, par3EntityPlayer.getGameProfile().getName());
            break;
          } 
          PacketTempChat.sendChat(par3EntityPlayer, (IChatComponent)new ChatComponentText("Spectrum set to public"));
          XUHelper.setPlayerOwner(par1ItemStack, "");
          break;
        case 10:
          matcher = AdvancedNodeUpgrades.nextEntry(par1ItemStack, !par3EntityPlayer.isSneaking());
          PacketTempChat.sendChat(par3EntityPlayer, (new ChatComponentText("Filter Program: ")).appendSibling((IChatComponent)new ChatComponentTranslation(matcher.unlocalizedName, new Object[0])));
          break;
      } 
    } 
    return par1ItemStack;
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    return super.getUnlocalizedName(par1ItemStack) + "." + par1ItemStack.getItemDamage();
  }
  
  public String getSortingName(ItemStack item) {
    if (item.getItemDamage() == 1)
      return item.getDisplayName(); 
    ItemStack i = item.copy();
    i.setItemDamage(-1);
    return i.getDisplayName() + item.getDisplayName();
  }
  
  public boolean matchesItem(ItemStack filter, ItemStack item) {
    return matchesFilterItem(item, filter);
  }
  
  public boolean matchesFluid(ItemStack filter, FluidStack fluidstack) {
    return matchesFilterLiquid(fluidstack, filter);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemNodeUpgrade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */