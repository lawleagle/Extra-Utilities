package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeMagicalWood extends ShapedRecipes {
  public static final ItemStack gold = new ItemStack(Items.gold_ingot);
  
  public static final ItemStack bookshelf = new ItemStack(Blocks.bookshelf);
  
  public static final ItemStack book = new ItemStack((Item)Items.enchanted_book);
  
  public RecipeMagicalWood() {
    super(3, 3, new ItemStack[] { gold, book, gold, book, bookshelf, book, gold, book, gold }, new ItemStack((Block)ExtraUtils.decorative1, 1, 8));
  }
  
  public int getEnchantmentLevel(ItemStack book) {
    NBTTagList nbttaglist = (book.getItem() == Items.enchanted_book) ? Items.enchanted_book.func_92110_g(book) : book.getEnchantmentTagList();
    int m = 0;
    if (nbttaglist != null) {
      if (nbttaglist.tagCount() == 0)
        return 0; 
      int j = book.getItem().getItemEnchantability();
      j /= 2;
      j = 1 + 2 * (j >> 1);
      if (j < 1)
        j = 1; 
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
        short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");
        float k = Enchantment.enchantmentsList[short1].getMinEnchantability(short2);
        k -= 0.5F;
        k *= 0.869F;
        k -= j;
        if (k < 1.0F)
          k = 1.0F; 
        m = Math.max(m, (int)Math.floor(k));
      } 
    } 
    return m;
  }
  
  public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
    return (checkMatch(par1InventoryCrafting) > 0);
  }
  
  private int checkMatch(InventoryCrafting par1InventoryCrafting) {
    int n = 0;
    for (int k = 0; k < 3; k++) {
      for (int l = 0; l < 3; l++) {
        int i1 = k;
        int j1 = l;
        ItemStack itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
        ItemStack itemstack1 = par1InventoryCrafting.getStackInRowAndColumn(k, l);
        if (itemstack1 == null)
          return 0; 
        if (itemstack.getItem() == book.getItem()) {
          int m = getEnchantmentLevel(itemstack1);
          if (m == 0)
            return 0; 
          n += m;
        } else {
          if (itemstack.getItem() != itemstack1.getItem())
            return 0; 
          if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
            return 0; 
        } 
      } 
    } 
    n /= 8;
    if (n < 1)
      n = 1; 
    if (n > 64)
      return 64; 
    return n;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
    ItemStack itemstack = getRecipeOutput().copy();
    itemstack.stackSize = checkMatch(par1InventoryCrafting);
    return itemstack;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeMagicalWood.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */