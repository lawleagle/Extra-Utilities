package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.helper.XUHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityFilingCabinet extends TileEntity implements IInventory {
  public List<ItemStack> itemSlots = new ArrayList<ItemStack>();
  
  public List<ItemStack> inputSlots = new ArrayList<ItemStack>();
  
  private boolean needsUpdate = false;
  
  public static boolean areCloseEnoughForBasic(ItemStack a, ItemStack b) {
    if (a == null || b == null)
      return false; 
    int[] da = OreDictionary.getOreIDs(a), db = OreDictionary.getOreIDs(b);
    return (da.length > 0 || db.length > 0) ? arrayContain(da, db) : ((a.getItem() == b.getItem()));
  }
  
  public static boolean arrayContain(int[] a, int[] b) {
    if (a.length == 0 || b.length == 0)
      return false; 
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < b.length; j++) {
        if (a[i] == a[j])
          return true; 
      } 
    } 
    return false;
  }
  
  public int getMaxSlots() {
    if (getBlockMetadata() < 6)
      return 1728; 
    return 1728;
  }
  
  public void updateEntity() {
    handleInput();
  }
  
  public void handleInput() {
    if (this.needsUpdate) {
      for (int i = 0; i < this.itemSlots.size(); i++) {
        if (this.itemSlots.get(i) == null) {
          this.itemSlots.remove(i);
          i--;
        } 
      } 
      while (this.inputSlots.size() > 0) {
        boolean added = false;
        for (ItemStack itemSlot : this.itemSlots) {
          if (XUHelper.canItemsStack(itemSlot, this.inputSlots.get(0), false, true)) {
            itemSlot.stackSize += ((ItemStack)this.inputSlots.get(0)).stackSize;
            added = true;
            break;
          } 
        } 
        if (!added)
          this.itemSlots.add(this.inputSlots.get(0)); 
        this.inputSlots.remove(0);
      } 
    } 
  }
  
  public void markDirty() {
    this.needsUpdate = true;
    super.markDirty();
  }
  
  public int getSizeInventory() {
    return this.itemSlots.size() + this.inputSlots.size() + 1;
  }
  
  public ItemStack getStackInSlot(int i) {
    if (i < this.itemSlots.size())
      return this.itemSlots.get(i); 
    if (i - this.itemSlots.size() < this.inputSlots.size())
      return this.inputSlots.get(i - this.itemSlots.size()); 
    return null;
  }
  
  public ItemStack decrStackSize(int par1, int par2) {
    if (par1 < this.itemSlots.size() && this.itemSlots.get(par1) != null) {
      if (par2 > ((ItemStack)this.itemSlots.get(par1)).getMaxStackSize())
        par2 = ((ItemStack)this.itemSlots.get(par1)).getMaxStackSize(); 
      if (((ItemStack)this.itemSlots.get(par1)).stackSize <= par2) {
        ItemStack itemStack = this.itemSlots.get(par1);
        this.itemSlots.set(par1, null);
        markDirty();
        return itemStack;
      } 
      ItemStack itemstack = ((ItemStack)this.itemSlots.get(par1)).splitStack(par2);
      if (((ItemStack)this.itemSlots.get(par1)).stackSize == 0)
        this.itemSlots.set(par1, null); 
      markDirty();
      return itemstack;
    } 
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    if (i < this.itemSlots.size())
      return this.itemSlots.get(i); 
    return null;
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    if (i < this.itemSlots.size()) {
      this.itemSlots.set(i, itemstack);
    } else if (i - this.itemSlots.size() < this.inputSlots.size()) {
      this.inputSlots.set(i - this.itemSlots.size(), itemstack);
    } else if (i == this.itemSlots.size() + this.inputSlots.size() && itemstack != null) {
      this.inputSlots.add(itemstack);
    } 
    this.needsUpdate = true;
  }
  
  public String getInventoryName() {
    return "extrautils:filing.cabinet";
  }
  
  public boolean hasCustomInventoryName() {
    return false;
  }
  
  public int getInventoryStackLimit() {
    if (getBlockMetadata() >= 6)
      return 1; 
    int n = 0;
    for (int j = 0; j < this.itemSlots.size() && n <= getMaxSlots(); j++) {
      if (this.itemSlots.get(j) != null)
        n += ((ItemStack)this.itemSlots.get(j)).stackSize; 
    } 
    for (ItemStack inputSlot : this.inputSlots) {
      if (inputSlot != null)
        n += inputSlot.stackSize; 
    } 
    return Math.max(1, getMaxSlots() - n);
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return !isInvalid();
  }
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    if (itemstack == null)
      return false; 
    if (i == this.itemSlots.size() + this.inputSlots.size()) {
      boolean basic = (getBlockMetadata() < 6);
      if (!basic && itemstack.getMaxStackSize() != 1)
        return false; 
      int n = 0;
      int j;
      for (j = 0; j < this.itemSlots.size() && n < getMaxSlots(); j++) {
        if (this.itemSlots.get(j) != null) {
          if (basic && !areCloseEnoughForBasic(this.itemSlots.get(j), itemstack))
            return false; 
          n += ((ItemStack)this.itemSlots.get(j)).stackSize;
        } 
      } 
      for (j = 0; j < this.inputSlots.size() && n < getMaxSlots(); j++) {
        if (this.inputSlots.get(j) != null)
          n += ((ItemStack)this.inputSlots.get(j)).stackSize; 
      } 
      return (n < getMaxSlots());
    } 
    return false;
  }
  
  public void readInvFromTags(NBTTagCompound tags) {
    int n = 0;
    if (tags.hasKey("item_no"))
      n = tags.getInteger("item_no"); 
    this.itemSlots.clear();
    this.inputSlots.clear();
    for (int i = 0; i < n; i++) {
      ItemStack item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("item_" + i));
      if (item != null) {
        item.stackSize = tags.getCompoundTag("item_" + i).getInteger("Size");
        if (item.stackSize > 0)
          this.itemSlots.add(item); 
      } 
    } 
  }
  
  public void writeInvToTags(NBTTagCompound tags) {
    handleInput();
    if (this.itemSlots.size() > 0) {
      tags.setInteger("item_no", this.itemSlots.size());
      for (int i = 0; i < this.itemSlots.size(); i++) {
        NBTTagCompound t = new NBTTagCompound();
        ((ItemStack)this.itemSlots.get(i)).writeToNBT(t);
        t.setInteger("Size", ((ItemStack)this.itemSlots.get(i)).stackSize);
        tags.setTag("item_" + i, (NBTBase)t);
      } 
    } 
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    readInvFromTags(tags);
  }
  
  public void writeToNBT(NBTTagCompound tags) {
    super.writeToNBT(tags);
    writeInvToTags(tags);
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */