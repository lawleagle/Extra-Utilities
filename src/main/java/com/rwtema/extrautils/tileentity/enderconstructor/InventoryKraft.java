package com.rwtema.extrautils.tileentity.enderconstructor;

import cofh.api.energy.EnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryKraft implements ISidedInventory {
  public CraftMatrix matrix = new CraftMatrix();
  
  public CraftResult result;
  
  EnergyStorage energy;
  
  TileEnderConstructor tile;
  
  ItemStack bufferItem;
  
  public InventoryKraft(TileEnderConstructor tile) {
    this.energy = tile.energy;
    this.tile = tile;
    this.result = new CraftResult(tile, this.matrix);
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    CraftResult.crafting = true;
    for (int i = 0; i < 9; i++) {
      if (tags.hasKey("items_" + i)) {
        this.matrix.inv.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tags.getCompoundTag("items_" + i)));
      } else {
        this.matrix.inv.setInventorySlotContents(i, null);
      } 
    } 
    CraftResult.crafting = false;
  }
  
  public void writeToNBT(NBTTagCompound tags) {
    for (int i = 0; i < 9; i++) {
      if (this.matrix.inv.getStackInSlot(i) != null) {
        NBTTagCompound t = new NBTTagCompound();
        this.matrix.inv.getStackInSlot(i).writeToNBT(t);
        tags.setTag("items_" + i, (NBTBase)t);
      } 
    } 
  }
  
  public int getSizeInventory() {
    return 10;
  }
  
  public ItemStack getStackInSlot(int i) {
    if (i == 9)
      return this.result.getStackInSlot(0); 
    return this.matrix.getStackInSlot(i);
  }
  
  public ItemStack decrStackSize(int i, int j) {
    if (i == 9) {
      if (this.tile.getBlockMetadata() == 4) {
        ItemStack item = this.result.decrStackSize(0, j);
        if (item != null) {
          if (!(this.tile.getWorldObj()).isRemote) {
            this.energy.setEnergyStored(0);
            this.tile.getWorldObj().setBlockMetadataWithNotify(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, 0, 2);
          } else {
            this.tile.getWorldObj().setBlockMetadataWithNotify(this.tile.xCoord, this.tile.yCoord, this.tile.zCoord, 0, 0);
          } 
          return item;
        } 
      } 
      return null;
    } 
    return this.matrix.decrStackSize(i, j);
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    if (i == 9)
      return this.result.getStackInSlotOnClosing(0); 
    return this.matrix.getStackInSlotOnClosing(i);
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    if (i != 9)
      this.matrix.setInventorySlotContents(i, itemstack); 
  }
  
  public String getInventoryName() {
    return "Ender Crafting";
  }
  
  public boolean hasCustomInventoryName() {
    return true;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public void markDirty() {
    this.matrix.markDirty();
    this.result.markDirty();
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return true;
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    return (i != 9);
  }
  
  public int[] getAccessibleSlotsFromSide(int var1) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j) {
    return (i < 9);
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    return (i == 9 && isEnabled());
  }
  
  public boolean isEnabled() {
    return (this.tile.energy.getEnergyStored() == this.tile.energy.getMaxEnergyStored());
  }
  
  public static class CraftMatrix extends InventoryCrafting {
    InventoryBasic inv = new InventoryBasic("Craft Matrix", false, 9);
    
    InventoryKraft.CraftResult result;
    
    public CraftMatrix() {
      super(null, 3, 3);
    }
    
    public ItemStack getStackInRowAndColumn(int par1, int par2) {
      if (par1 >= 0 && par1 < 3) {
        int k = par1 + par2 * 3;
        return this.inv.getStackInSlot(k);
      } 
      return null;
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
    
    public void setResult(InventoryKraft.CraftResult result) {
      this.result = result;
    }
    
    public void markDirty() {
      this.result.markDirty(this);
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
  
  public static class CraftResult extends InventoryBasic {
    public static boolean crafting = false;
    
    public InventoryKraft.CraftMatrix matrix;
    
    TileEnderConstructor tile;
    
    public CraftResult(TileEnderConstructor tile, InventoryKraft.CraftMatrix matrix) {
      super("Craft Result", false, 1);
      this.tile = tile;
      this.matrix = matrix;
      matrix.setResult(this);
    }
    
    public ItemStack decrStackSize(int par1, int par2) {
      return super.decrStackSize(par1, par2);
    }
    
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return false;
    }
    
    public void markDirty(InventoryKraft.CraftMatrix craftMatrix) {
      if (crafting)
        return; 
      ItemStack item = EnderConstructorRecipesHandler.findMatchingRecipe(craftMatrix, this.tile.getWorldObj());
      setInventorySlotContents(0, null);
      if (item != null)
        setInventorySlotContents(0, item); 
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\InventoryKraft.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */