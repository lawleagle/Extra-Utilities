package com.rwtema.extrautils.nei;

import codechicken.microblock.MicroRecipe;
import codechicken.microblock.handler.MicroblockProxy;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class FMPMicroBlockRecipeCreator extends InventoryCrafting {
  public static final int[] validClasses = new int[] { 0, 1, 2, 3 };
  
  public static final int[] validSizes = new int[] { 4, 2, 1 };
  
  public static final int[] validSizes2 = new int[] { 8, 4, 2, 1 };
  
  public static ItemStack stone = new ItemStack(Blocks.stone);
  
  public static FMPMicroBlockRecipeCreator craft = new FMPMicroBlockRecipeCreator();
  
  public static int mat;
  
  public static ItemStack saw;
  
  InventoryBasic inv;
  
  public FMPMicroBlockRecipeCreator() {
    super(null, 3, 3);
    this.inv = new InventoryBasic("Craft Matrix", false, 9);
  }
  
  public static ArrayList<ShapedRecipes> loadRecipes() {
    ArrayList<ShapedRecipes> set = new ArrayList<ShapedRecipes>();
    mat = MicroRecipe.findMaterial(stone);
    if (mat == -1)
      return set; 
    saw = new ItemStack(MicroblockProxy.sawDiamond());
    if (saw == null)
      return set; 
    loadThinningRecipes(set);
    loadSplittingRecipes(set);
    loadHollowRecipes(set);
    loadHollowFillingRecipes(set);
    loadGluingRecipes(set);
    return set;
  }
  
  public static void loadThinningRecipes(ArrayList<ShapedRecipes> recipes) {
    craft.clear();
    craft.setInventorySlotContents(0, 0, saw);
    for (int mclass : validClasses) {
      for (int msize : validSizes2) {
        if (msize != 8 || mclass == 0) {
          ItemStack a = MicroRecipe.create(1, mclass, msize, mat);
          craft.setInventorySlotContents(0, 1, a);
          ItemStack b = MicroRecipe.getCraftingResult(craft);
          if (b != null)
            recipes.add(new ShapedRecipes(1, 2, new ItemStack[] { saw, a }, b)); 
        } 
      } 
    } 
  }
  
  public static void loadSplittingRecipes(ArrayList<ShapedRecipes> recipes) {
    craft.clear();
    craft.setInventorySlotContents(0, 0, saw);
    for (int mclass : validClasses) {
      for (int msize : validSizes2) {
        if (msize != 8 || mclass == 0) {
          ItemStack a = MicroRecipe.create(1, mclass, msize, mat);
          craft.setInventorySlotContents(1, 0, a);
          ItemStack b = MicroRecipe.getCraftingResult(craft);
          if (b != null)
            recipes.add(new ShapedRecipes(2, 1, new ItemStack[] { saw, a }, b)); 
        } 
      } 
    } 
  }
  
  public static void loadHollowRecipes(ArrayList<ShapedRecipes> recipes) {
    craft.clear();
    for (int msize : validSizes) {
      ItemStack a = MicroRecipe.create(1, 0, msize, mat);
      for (int i = 0; i < 9; i++) {
        if (i != 4)
          craft.setInventorySlotContents(i, a); 
      } 
      ItemStack b = MicroRecipe.getCraftingResult(craft);
      if (b != null)
        recipes.add(new ShapedRecipes(3, 3, new ItemStack[] { a, a, a, a, null, a, a, a, a }, b)); 
    } 
  }
  
  public static void loadHollowFillingRecipes(ArrayList<ShapedRecipes> recipes) {
    craft.clear();
    for (int msize : validSizes) {
      ItemStack a = MicroRecipe.create(1, 1, msize, mat);
      craft.setInventorySlotContents(0, a);
      ItemStack b = MicroRecipe.getCraftingResult(craft);
      if (b != null)
        recipes.add(new ShapedRecipes(1, 1, new ItemStack[] { a }, b)); 
    } 
  }
  
  public static void loadGluingRecipes(ArrayList<ShapedRecipes> recipes) {
    craft.clear();
    for (int mClass : validClasses) {
      for (int msize : validSizes) {
        craft.clear();
        ItemStack a = MicroRecipe.create(1, mClass, msize, mat);
        craft.setInventorySlotContents(0, a);
        craft.setInventorySlotContents(1, a);
        ItemStack b = MicroRecipe.getCraftingResult(craft);
        if (b != null)
          recipes.add(new ShapedRecipes(2, 1, new ItemStack[] { a, a }, b)); 
        craft.setInventorySlotContents(2, a);
        craft.setInventorySlotContents(3, a);
        b = MicroRecipe.getCraftingResult(craft);
        if (b != null)
          recipes.add(new ShapedRecipes(2, 2, new ItemStack[] { a, a, a, a }, b)); 
        craft.setInventorySlotContents(4, a);
        craft.setInventorySlotContents(5, a);
        b = MicroRecipe.getCraftingResult(craft);
        if (b != null)
          recipes.add(new ShapedRecipes(2, 3, new ItemStack[] { a, a, a, a, a, a }, b)); 
        craft.setInventorySlotContents(6, a);
        craft.setInventorySlotContents(7, a);
        b = MicroRecipe.getCraftingResult(craft);
        if (b != null)
          recipes.add(new ShapedRecipes(3, 3, new ItemStack[] { a, a, a, a, a, a, a, a, null }, b)); 
      } 
    } 
  }
  
  public void clear() {
    for (int i = 0; i < 9; i++)
      this.inv.setInventorySlotContents(i, null); 
  }
  
  public ItemStack getStackInRowAndColumn(int par1, int par2) {
    if (par1 >= 0 && par1 < 3) {
      int k = par1 + par2 * 3;
      return this.inv.getStackInSlot(k);
    } 
    return null;
  }
  
  public void setInventorySlotContents(int par1, int par2, ItemStack itemstack) {
    this.inv.setInventorySlotContents(par1 + par2 * 3, itemstack);
  }
  
  public int getSizeInventory() {
    return this.inv.getSizeInventory();
  }
  
  public ItemStack getStackInSlot(int i) {
    return this.inv.getStackInSlot(i);
  }
  
  public ItemStack decrStackSize(int i, int j) {
    return this.inv.decrStackSize(i, j);
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    return this.inv.getStackInSlotOnClosing(i);
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    this.inv.setInventorySlotContents(i, itemstack);
  }
  
  public String getInventoryName() {
    return this.inv.getInventoryName();
  }
  
  public boolean hasCustomInventoryName() {
    return this.inv.hasCustomInventoryName();
  }
  
  public int getInventoryStackLimit() {
    return this.inv.getInventoryStackLimit();
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return false;
  }
  
  public void openInventory() {
    this.inv.openInventory();
  }
  
  public void closeInventory() {
    this.inv.closeInventory();
  }
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    return this.inv.isItemValidForSlot(i, itemstack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\FMPMicroBlockRecipeCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */