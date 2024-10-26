package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTrashCan extends TileEntity implements IInventory {
  public static final int NUM_SLOTS = 64;
  
  private ItemStack[] itemSlots = new ItemStack[64];
  
  public static boolean instantAdd = false;
  
  private boolean added = false;
  
  private boolean checkedValid;
  
  public void updateEntity() {
    if (!this.checkedValid && this.worldObj != null)
      if (ExtraUtils.trashCan == null || this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) != ExtraUtils.trashCan) {
        this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        invalidate();
      } else {
        this.checkedValid = true;
      }  
    if (this.added) {
      this.added = false;
      processInv();
    } 
  }
  
  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    if (par1NBTTagCompound.hasKey("filter"))
      this.itemSlots[0] = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("filter")); 
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    if (this.itemSlots[0] != null) {
      NBTTagCompound itemTag = new NBTTagCompound();
      this.itemSlots[0].writeToNBT(itemTag);
      par1NBTTagCompound.setTag("filter", (NBTBase)itemTag);
    } 
  }
  
  public void processInv() {
    if (this.itemSlots[0] != null && (
      ExtraUtils.nodeUpgrade == null || !ItemNodeUpgrade.isFilter(this.itemSlots[0])))
      this.itemSlots[0] = null; 
    for (int i = 1; i < 64; i++)
      this.itemSlots[i] = null; 
    markDirty();
  }
  
  public int getSizeInventory() {
    return 65;
  }
  
  public ItemStack getStackInSlot(int i) {
    return (i == 64) ? null : this.itemSlots[i];
  }
  
  public ItemStack decrStackSize(int par1, int par2) {
    if (par1 == 64)
      return null; 
    if (this.itemSlots[par1] != null) {
      if ((this.itemSlots[par1]).stackSize <= par2) {
        ItemStack itemStack = this.itemSlots[par1];
        this.itemSlots[par1] = null;
        markDirty();
        return itemStack;
      } 
      ItemStack itemstack = this.itemSlots[par1].splitStack(par2);
      if ((this.itemSlots[par1]).stackSize == 0)
        this.itemSlots[par1] = null; 
      markDirty();
      return itemstack;
    } 
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    return getStackInSlot(i);
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    if (i == 64)
      return; 
    this.itemSlots[i] = itemstack;
    markDirty();
    if (instantAdd) {
      processInv();
    } else {
      this.added = true;
    } 
  }
  
  public String getInventoryName() {
    return "TrashCan";
  }
  
  public boolean hasCustomInventoryName() {
    return false;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
    return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
  }
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    return (i <= 64 && (this.itemSlots[0] == null || ItemNodeUpgrade.matchesFilterItem(itemstack, this.itemSlots[0])));
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityTrashCan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */