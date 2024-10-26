package com.rwtema.extrautils.multipart.microblock;

import codechicken.microblock.ItemMicroPart;
import codechicken.microblock.MicroRecipe;
import com.rwtema.extrautils.multipart.FMPBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeMicroBlocks implements IRecipe {
  public static Item microID = null;
  
  public final int recipeWidth;
  
  public final int recipeHeight;
  
  public final Object[] recipeItems;
  
  public final Item recipeOutputItemID;
  
  private ItemStack recipeOutput;
  
  private boolean field_92101_f;
  
  public RecipeMicroBlocks(int par1, int par2, Object[] par3ArrayOfItemStack, ItemStack par4ItemStack) {
    this.recipeOutputItemID = par4ItemStack.getItem();
    this.recipeWidth = par1;
    this.recipeHeight = par2;
    this.recipeItems = par3ArrayOfItemStack;
    this.recipeOutput = par4ItemStack;
  }
  
  public ItemStack getRecipeOutput() {
    return this.recipeOutput;
  }
  
  public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
    for (int i = 0; i <= 3 - this.recipeWidth; i++) {
      for (int j = 0; j <= 3 - this.recipeHeight; j++) {
        if (checkMatch(par1InventoryCrafting, i, j, true))
          return true; 
        if (checkMatch(par1InventoryCrafting, i, j, false))
          return true; 
      } 
    } 
    return false;
  }
  
  public ItemStack getRecipeItem(int a) {
    if (this.recipeItems[a] instanceof ItemStack)
      return (ItemStack)this.recipeItems[a]; 
    if (this.recipeItems[a] instanceof Integer) {
      int damage = ((Integer)this.recipeItems[a]).intValue();
      if (getMicroID() != null)
        return new ItemStack(getMicroID(), 1, damage); 
    } 
    return null;
  }
  
  private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int par2, int par3, boolean par4) {
    if (getMicroID() == null)
      return false; 
    int curMat = 0;
    for (int k = 0; k < 3; k++) {
      for (int l = 0; l < 3; l++) {
        int i1 = k - par2;
        int j1 = l - par3;
        ItemStack itemstack = null;
        int i = -1;
        if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight)
          if (par4) {
            i = this.recipeWidth - i1 - 1 + j1 * this.recipeWidth;
          } else {
            i = i1 + j1 * this.recipeWidth;
          }  
        ItemStack itemstack1 = par1InventoryCrafting.getStackInRowAndColumn(k, l);
        if (this.recipeItems[i] == null || this.recipeItems[i] instanceof ItemStack) {
          itemstack = (this.recipeItems[i] != null) ? (ItemStack)this.recipeItems[i] : null;
          if (itemstack1 != null || itemstack != null) {
            if ((itemstack1 == null && itemstack != null) || (itemstack1 != null && itemstack == null))
              return false; 
            if (itemstack.getItem() != itemstack1.getItem())
              return false; 
            if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
              return false; 
          } 
        } else if (this.recipeItems[i] instanceof Integer) {
          int damage = ((Integer)this.recipeItems[i]).intValue();
          if (itemstack1 == null)
            return false; 
          if (damage == 0) {
            if (itemstack1.getItem() == null || itemstack1.getItem() == getMicroID())
              return false; 
            int mat = MicroRecipe.findMaterial(itemstack1);
            if (mat <= 0)
              return false; 
            if (curMat == 0) {
              curMat = mat;
            } else if (curMat != mat) {
              return false;
            } 
          } else {
            if (itemstack1.getItem() != getMicroID())
              return false; 
            if (!itemstack1.hasTagCompound())
              return false; 
            int s = ItemMicroPart.getMaterialID(itemstack1);
            if (s == 0)
              return false; 
            if (curMat == 0) {
              curMat = s;
            } else if (curMat != s) {
              return false;
            } 
          } 
        } 
      } 
    } 
    return true;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
    ItemStack itemstack = getRecipeOutput().copy();
    for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); i++) {
      ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);
      if (itemstack1 != null && itemstack1.getItem() == getMicroID() && itemstack1.hasTagCompound()) {
        NBTTagCompound tag = itemstack.getTagCompound();
        if (tag == null)
          tag = new NBTTagCompound(); 
        tag.setString("mat", itemstack1.getTagCompound().getString("mat"));
        itemstack.setTagCompound((NBTTagCompound)itemstack1.stackTagCompound.copy());
        return itemstack;
      } 
    } 
    return itemstack;
  }
  
  private Item getMicroID() {
    if (microID == null)
      microID = FMPBase.getMicroBlockItemId(); 
    return microID;
  }
  
  public int getRecipeSize() {
    return this.recipeWidth * this.recipeHeight;
  }
  
  public ItemStack[] getRecipeItems() {
    ItemStack[] t = new ItemStack[this.recipeItems.length];
    for (int i = 0; i < this.recipeItems.length; i++)
      t[i] = getRecipeItem(i); 
    return t;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\RecipeMicroBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */