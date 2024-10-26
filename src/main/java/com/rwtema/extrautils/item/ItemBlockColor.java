package com.rwtema.extrautils.item;

import com.rwtema.extrautils.block.BlockColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockColor extends ItemBlockMetadata {
  public ItemBlockColor(Block par1) {
    super(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack item, int p_82790_2_) {
    float[] col = BlockColor.initColor[item.getItemDamage() & 0xF];
    return (int)(col[0] * 255.0F) << 16 | (int)(col[1] * 255.0F) << 8 | (int)(col[2] * 255.0F);
  }
  
  public String getItemStackDisplayName(ItemStack p_77653_1_) {
    String name;
    Block bc = ((BlockColor)this.field_150939_a).baseBlock;
    Item i = Item.getItemFromBlock(bc);
    if (i == null) {
      name = bc.getUnlocalizedName();
      if (name != null) {
        name = StatCollector.translateToLocal(name);
        name = ("" + StatCollector.translateToLocal(name + ".name")).trim();
      } else {
        name = "";
      } 
    } else {
      name = (new ItemStack(i, 1, 0)).getDisplayName();
    } 
    return StatCollector.translateToLocal("tile.extrautils:colorBlock." + p_77653_1_.getItemDamage() + ".name").replaceAll("BLOCKNAME", name);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */