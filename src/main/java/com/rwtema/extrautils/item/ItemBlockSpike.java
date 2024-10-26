package com.rwtema.extrautils.item;

import com.google.common.collect.Multimap;
import com.rwtema.extrautils.block.BlockSpike;
import java.util.HashSet;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSpike extends ItemBlock {
  public final BlockSpike spike;
  
  public static final HashSet<Item> itemHashSet = new HashSet<Item>();
  
  public ItemBlockSpike(Block p_i45328_1_) {
    super(p_i45328_1_);
    this.spike = (BlockSpike)p_i45328_1_;
    this.spike.itemSpike = (Item)this;
    itemHashSet.add(this);
  }
  
  public int getItemEnchantability() {
    return this.spike.swordStack.getItem().getItemEnchantability(this.spike.swordStack);
  }
  
  public int cofh_canEnchantApply(ItemStack stack, Enchantment ench) {
    if (ench.canApply(this.spike.swordStack.copy()))
      return 1; 
    return -1;
  }
  
  public boolean isItemTool(ItemStack p_77616_1_) {
    return (p_77616_1_.stackSize == 1);
  }
  
  public Multimap getAttributeModifiers(ItemStack stack) {
    return this.spike.swordStack.copy().getAttributeModifiers();
  }
  
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return (stack.stackSize == 1);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockSpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */