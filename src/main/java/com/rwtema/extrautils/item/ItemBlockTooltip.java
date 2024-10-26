package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ICreativeTabSorting;
import com.rwtema.extrautils.block.IBlockTooltip;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTooltip extends ItemBlock implements ICreativeTabSorting {
  public ItemBlockTooltip(Block par1) {
    super(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    if (this.field_150939_a instanceof IBlockTooltip)
      ((IBlockTooltip)this.field_150939_a).addInformation(par1ItemStack, par2EntityPlayer, par3List, par4); 
  }
  
  public String getSortingName(ItemStack par1ItemStack) {
    if (this.field_150939_a instanceof ICreativeTabSorting)
      return ((ICreativeTabSorting)this.field_150939_a).getSortingName(par1ItemStack); 
    return par1ItemStack.getDisplayName();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockTooltip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */