package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeUnEnchanting implements IRecipe {
  public boolean matches(InventoryCrafting inventorycrafting, World world) {
    return (getCraftingResult(inventorycrafting) != null);
  }
  
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
    if (inventorycrafting.getSizeInventory() != 9)
      return null; 
    int curRow = -1;
    int x;
    for (x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        if (inventorycrafting.getStackInRowAndColumn(x, y) != null) {
          if (curRow == -1) {
            if (y == 0) {
              curRow = x;
            } else {
              return null;
            } 
          } else if (x != curRow) {
            return null;
          } 
        } else if (curRow == x) {
          return null;
        } 
      } 
    } 
    if (curRow == -1)
      return null; 
    x = curRow;
    ItemStack n = inventorycrafting.getStackInRowAndColumn(x, 0);
    ItemStack s = inventorycrafting.getStackInRowAndColumn(x, 1);
    ItemStack d = inventorycrafting.getStackInRowAndColumn(x, 2);
    if (s.getItem() != ExtraUtils.divisionSigil)
      return null; 
    if (!s.hasTagCompound() || !s.getTagCompound().hasKey("damage"))
      return null; 
    if (n.getItem() == Items.iron_ingot && d.getItem() == Items.diamond)
      return null; 
    Map ne = EnchantmentHelper.getEnchantments(n);
    Map de = EnchantmentHelper.getEnchantments(d);
    if (de == null || de.isEmpty()) {
      if (d.getItem() == Items.book && n.getItem() != Items.book && ne != null && !ne.isEmpty()) {
        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
        for (Object o : ne.keySet()) {
          int id = ((Integer)o).intValue();
          int level = ((Integer)ne.get(Integer.valueOf(id))).intValue();
          if (level > 1)
            linkedHashMap.put(Integer.valueOf(id), Integer.valueOf(level - 1)); 
        } 
        ItemStack itemStack = n.copy();
        if (itemStack.hasTagCompound())
          itemStack.getTagCompound().removeTag("ench"); 
        EnchantmentHelper.setEnchantments(linkedHashMap, itemStack);
        return itemStack;
      } 
      if (ne == null || ne.isEmpty())
        return null; 
    } 
    LinkedHashMap<Object, Object> re = new LinkedHashMap<Object, Object>();
    boolean overlap = false;
    for (Object o : ne.keySet()) {
      int id = ((Integer)o).intValue();
      int level = ((Integer)ne.get(Integer.valueOf(id))).intValue();
      if (de != null && de.containsKey(Integer.valueOf(id))) {
        overlap = true;
        level -= ((Integer)de.get(Integer.valueOf(id))).intValue();
        if (level > 0)
          re.put(Integer.valueOf(id), Integer.valueOf(level)); 
        continue;
      } 
      re.put(Integer.valueOf(id), Integer.valueOf(level));
    } 
    if (!overlap)
      return null; 
    ItemStack r = n.copy();
    if (r.hasTagCompound())
      r.getTagCompound().removeTag("ench"); 
    EnchantmentHelper.setEnchantments(re, r);
    return r;
  }
  
  public int getRecipeSize() {
    return 3;
  }
  
  public ItemStack getRecipeOutput() {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeUnEnchanting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */