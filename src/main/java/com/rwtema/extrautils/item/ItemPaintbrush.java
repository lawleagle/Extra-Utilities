package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemPaintbrush extends Item {
  private IIcon[] icons;
  
  public ItemPaintbrush() {
    setMaxStackSize(1);
    setMaxDamage(0);
    setHasSubtypes(true);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setContainerItem(this);
    setUnlocalizedName("extrautils:paintbrush");
  }
  
  public static ItemStack setColor(ItemStack par1ItemStack, int color, int damage) {
    return setColor(par1ItemStack, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, damage);
  }
  
  public static ItemStack setColor(ItemStack par1ItemStack, int r, int g, int b, int damage) {
    if (par1ItemStack.getTagCompound() == null)
      par1ItemStack.setTagCompound(new NBTTagCompound()); 
    par1ItemStack.getTagCompound().setInteger("r", r & 0xFF);
    par1ItemStack.getTagCompound().setInteger("g", g & 0xFF);
    par1ItemStack.getTagCompound().setInteger("b", b & 0xFF);
    if (damage >= 0)
      par1ItemStack.getTagCompound().setInteger("damage", damage); 
    return par1ItemStack;
  }
  
  public static int getColor(ItemStack par1ItemStack) {
    int r = 255, g = 255, b = 255;
    if (par1ItemStack.getTagCompound() != null) {
      if (par1ItemStack.getTagCompound().hasKey("r"))
        r = par1ItemStack.getTagCompound().getInteger("r"); 
      if (par1ItemStack.getTagCompound().hasKey("g"))
        g = par1ItemStack.getTagCompound().getInteger("g"); 
      if (par1ItemStack.getTagCompound().hasKey("b"))
        b = par1ItemStack.getTagCompound().getInteger("b"); 
    } 
    return r << 16 | g << 8 | b;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[2];
    this.icons[0] = par1IIconRegister.registerIcon("extrautils:paintbrush_base");
    this.icons[1] = par1IIconRegister.registerIcon("extrautils:paintbrush_brush");
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
    if (par2 == 1)
      return getColor(par1ItemStack); 
    return 16777215;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  public IIcon getIcon(ItemStack stack, int pass) {
    return this.icons[pass];
  }
  
  public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
    return true;
  }
  
  public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
    return false;
  }
  
  public boolean onItemUse(ItemStack item, EntityPlayer par2EntityPlayer, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
    if (world.isRemote)
      return true; 
    if (item.getTagCompound() != null && item.getTagCompound().hasKey("damage")) {
      if (ExtraUtils.colorBlockBrick != null && world.getBlock(x, y, z) == Blocks.stonebrick && world.getBlockMetadata(x, y, z) == 0) {
        world.setBlock(x, y, z, (Block)ExtraUtils.colorBlockBrick, item.getTagCompound().getInteger("damage") & 0xF, 3);
        return true;
      } 
      if (ExtraUtils.coloredWood != null) {
        Block id = world.getBlock(x, y, z);
        if (!world.isAirBlock(x, y, z)) {
          ArrayList<ItemStack> items = id.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
          if (items != null && items.size() == 1 && ((ItemStack)items.get(0)).stackSize == 1 && ((ItemStack)items.get(0)).getItem() == Item.getItemFromBlock(world.getBlock(x, y, z)))
            for (ItemStack target : OreDictionary.getOres("plankWood")) {
              if (OreDictionary.itemMatches(target, items.get(0), false)) {
                world.setBlock(x, y, z, (Block)ExtraUtils.coloredWood, item.getTagCompound().getInteger("damage") & 0xF, 3);
                return true;
              } 
            }  
        } 
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemPaintbrush.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */