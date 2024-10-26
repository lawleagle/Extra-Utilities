package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeUnstableNuggetCrafting extends RecipeUnstableIngotCrafting {
  public RecipeUnstableNuggetCrafting(int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack) {
    super(par1, par2, par3ArrayOfItemStack, par4ItemStack);
  }
  
  public static IRecipe addRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
    String s = "";
    int i = 0;
    int j = 0;
    int k = 0;
    if (par2ArrayOfObj[i] instanceof String[]) {
      String[] astring = (String[])par2ArrayOfObj[i++];
      for (String s1 : astring) {
        k++;
        j = s1.length();
        s = s + s1;
      } 
    } else {
      while (par2ArrayOfObj[i] instanceof String) {
        String s2 = (String)par2ArrayOfObj[i++];
        k++;
        j = s2.length();
        s = s + s2;
      } 
    } 
    HashMap<Object, Object> hashmap;
    for (hashmap = new HashMap<Object, Object>(); i < par2ArrayOfObj.length; i += 2) {
      Character character = (Character)par2ArrayOfObj[i];
      ItemStack itemstack1 = null;
      if (par2ArrayOfObj[i + 1] instanceof Item) {
        itemstack1 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
      } else if (par2ArrayOfObj[i + 1] instanceof Block) {
        itemstack1 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
      } else if (par2ArrayOfObj[i + 1] instanceof ItemStack) {
        itemstack1 = (ItemStack)par2ArrayOfObj[i + 1];
      } 
      hashmap.put(character, itemstack1);
    } 
    ItemStack[] aitemstack = new ItemStack[j * k];
    for (int i1 = 0; i1 < j * k; i1++) {
      char c0 = s.charAt(i1);
      if (hashmap.containsKey(Character.valueOf(c0))) {
        aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
      } else {
        aitemstack[i1] = null;
      } 
    } 
    NBTTagCompound tags = new NBTTagCompound();
    tags.setBoolean("Bug", true);
    par1ItemStack.setTagCompound(tags);
    return (IRecipe)new RecipeUnstableNuggetCrafting(j, k, aitemstack, par1ItemStack);
  }
  
  public ItemStack makeResult(ItemStack n, ItemStack d, ItemStack s) {
    if (n.getItem() == Items.gold_nugget && d.getItem() == Items.diamond) {
      ItemStack item = new ItemStack(ExtraUtils.unstableIngot, 1, 1);
      return item;
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeUnstableNuggetCrafting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */