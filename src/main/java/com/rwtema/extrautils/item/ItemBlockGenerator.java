package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlockGenerator extends ItemBlockMetadata {
  public ItemBlockGenerator(Block par1) {
    super(par1);
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (this.field_150939_a != ExtraUtils.generator)
      return Item.getItemFromBlock(ExtraUtils.generator).getUnlocalizedName(par1ItemStack); 
    return super.getUnlocalizedName(par1ItemStack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */