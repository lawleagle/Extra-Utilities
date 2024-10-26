package com.rwtema.extrautils.crafting;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeEnchantedItems extends ShapedRecipes {
  public RecipeEnchantedItems(int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack) {
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
      if (itemstack1.isItemEnchantable()) {
        if (!itemstack1.hasTagCompound())
          itemstack1.setTagCompound(new NBTTagCompound()); 
        if (!itemstack1.getTagCompound().hasKey("ench"))
          itemstack1.getTagCompound().setTag("ench", (NBTBase)new NBTTagCompound()); 
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
    return (IRecipe)new RecipeEnchantedItems(j, k, aitemstack, par1ItemStack);
  }
  
  public boolean matches(InventoryCrafting inv, World par2World) {
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).isItemEnchantable())
        return false; 
    } 
    return super.matches(inv, par2World);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeEnchantedItems.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */