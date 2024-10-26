package com.rwtema.extrautils.crafting;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public class RecipeDifficultySpecific extends ShapedRecipes {
  private static boolean isRemote = false;
  
  boolean[] validDifficulty = new boolean[4];
  
  private ItemStack fakeStack;
  
  public RecipeDifficultySpecific(int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack, boolean[] validDifficulties, ItemStack fakeStack) {
    super(par1, par2, par3ArrayOfItemStack, par4ItemStack);
    this.validDifficulty = validDifficulties;
    this.fakeStack = fakeStack;
  }
  
  public static RecipeDifficultySpecific addRecipe(boolean[] validDifficulties, ItemStack par1ItemStack, String[] LoreText, Object... par2ArrayOfObj) {
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
    NBTTagList nbttaglist = new NBTTagList();
    for (String aLoreText : LoreText)
      nbttaglist.appendTag((NBTBase)new NBTTagString(aLoreText)); 
    NBTTagCompound display = new NBTTagCompound();
    display.setTag("Lore", (NBTBase)nbttaglist);
    NBTTagCompound base = new NBTTagCompound();
    base.setTag("display", (NBTBase)display);
    ItemStack item = par1ItemStack.copy();
    item.setTagCompound(base);
    RecipeDifficultySpecific shapedrecipes = new RecipeDifficultySpecific(j, k, aitemstack, par1ItemStack, validDifficulties, item);
    return shapedrecipes;
  }
  
  public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
    if (par2World != null && par2World.difficultySetting != null) {
      isRemote = par2World.isRemote;
      if (par2World.difficultySetting.getDifficultyId() >= 0 && par2World.difficultySetting.getDifficultyId() <= 3 && (
        par2World.isRemote || this.validDifficulty[par2World.difficultySetting.getDifficultyId()]))
        return super.matches(par1InventoryCrafting, par2World); 
    } 
    return false;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
    ItemStack item = super.getCraftingResult(par1InventoryCrafting);
    if (isRemote && this.fakeStack != null)
      return this.fakeStack; 
    return item;
  }
  
  public ItemStack getRecipeOutput() {
    return this.fakeStack;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeDifficultySpecific.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */