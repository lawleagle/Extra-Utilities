package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockMetadata extends ItemBlockTooltip {
  protected boolean hasBlockMetadata = true;
  
  private int blockId;
  
  IBlockLocalization blockLocalizationMetadata;
  
  public ItemBlockMetadata(Block par1) {
    super(par1);
    setMaxDamage(0);
    setHasSubtypes(true);
    if (par1 instanceof IBlockLocalization)
      this.blockLocalizationMetadata = (IBlockLocalization)par1; 
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    if (!this.hasBlockMetadata)
      return super.getIconFromDamage(par1); 
    return this.field_150939_a.getIcon(2, par1);
  }
  
  public int getMetadata(int par1) {
    if (!this.hasBlockMetadata)
      return super.getMetadata(par1); 
    return par1;
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (this.blockLocalizationMetadata != null)
      return this.blockLocalizationMetadata.getUnlocalizedName(par1ItemStack); 
    if (!this.hasBlockMetadata)
      return super.getUnlocalizedName(par1ItemStack); 
    return getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */