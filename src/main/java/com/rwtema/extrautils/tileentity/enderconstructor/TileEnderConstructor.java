package com.rwtema.extrautils.tileentity.enderconstructor;

import cofh.api.energy.EnergyStorage;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.sounds.ISoundTile;
import com.rwtema.extrautils.sounds.Sounds;
import com.rwtema.extrautils.tileentity.enderquarry.IChunkLoad;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEnderConstructor extends TileEntity implements IEnderFluxHandler, ISidedInventory, IChunkLoad, ISoundTile {
  static Random rand = (Random)XURandom.getInstance();
  
  public EnergyStorage energy = new CustomEnergy(20000);
  
  public InventoryKraft inv = new InventoryKraft(this);
  
  public ItemStack outputslot = null;
  
  int coolDown = 0;
  
  public void setWorldObj(World p_145834_1_) {
    super.setWorldObj(p_145834_1_);
    if (p_145834_1_ != null && p_145834_1_.isRemote)
      Sounds.registerSoundTile(this); 
  }
  
  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    this.energy.readFromNBT(par1NBTTagCompound);
    this.inv.readFromNBT(par1NBTTagCompound);
    this.outputslot = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("output"));
  }
  
  public void onChunkLoad() {
    this.inv.markDirty();
  }
  
  public void markDirty() {
    if (this.worldObj != null) {
      super.markDirty();
      this.inv.markDirty();
    } 
  }
  
  public int getSizeInventory() {
    return 10;
  }
  
  public ItemStack getStackInSlot(int i) {
    if (i >= 9)
      return this.outputslot; 
    return this.inv.matrix.getStackInSlot(i);
  }
  
  public ItemStack decrStackSize(int i, int j) {
    ItemStack itemstack;
    if (i != 9)
      return null; 
    if (this.outputslot == null)
      return null; 
    if (this.outputslot.stackSize <= j) {
      itemstack = this.outputslot;
      this.outputslot = null;
      markDirty();
    } else {
      itemstack = this.outputslot.splitStack(j);
      if (this.outputslot.stackSize == 0)
        this.outputslot = null; 
      markDirty();
    } 
    return itemstack;
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    return getStackInSlot(i);
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    if (i == 9) {
      this.outputslot = itemstack;
    } else {
      this.inv.setInventorySlotContents(i, itemstack);
    } 
  }
  
  public String getInventoryName() {
    return this.inv.getInventoryName();
  }
  
  public boolean hasCustomInventoryName() {
    return false;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return true;
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    if (i == 9)
      return false; 
    if (this.inv.getStackInSlot(i) == null || itemstack == null)
      return false; 
    return XUHelper.canItemsStack(itemstack, this.inv.getStackInSlot(i));
  }
  
  public int[] getAccessibleSlotsFromSide(int var1) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j) {
    return false;
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    return (i == 9);
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    this.energy.writeToNBT(par1NBTTagCompound);
    this.inv.writeToNBT(par1NBTTagCompound);
    if (this.outputslot != null) {
      NBTTagCompound output = new NBTTagCompound();
      this.outputslot.writeToNBT(output);
      par1NBTTagCompound.setTag("output", (NBTBase)output);
    } 
  }
  
  public int getDisplayProgress() {
    return this.energy.getEnergyStored() * 22 / this.energy.getMaxEnergyStored();
  }
  
  public boolean isActive() {
    return ((getBlockMetadata() == 1 && (getWorldObj()).isRemote) || (!(getWorldObj()).isRemote && canAddMorez()));
  }
  
  public int recieveEnergy(int amount, Transfer simulate) {
    return this.energy.receiveEnergy(amount, (simulate == Transfer.SIMULATE));
  }
  
  public float getAmountRequested() {
    return (this.energy.getMaxEnergyStored() - this.energy.getEnergyStored());
  }
  
  public boolean canAddMorez() {
    ItemStack item = this.inv.getStackInSlot(9);
    if (item == null)
      return false; 
    if (this.outputslot == null)
      return true; 
    if (!XUHelper.canItemsStack(item, this.outputslot))
      return false; 
    return (this.outputslot.stackSize + item.stackSize <= this.outputslot.getMaxStackSize() && this.outputslot.stackSize + item.stackSize <= getInventoryStackLimit());
  }
  
  public void updateEntity() {
    if (this.worldObj.isRemote && getBlockMetadata() == 1) {
      double dx1 = this.xCoord + rand.nextDouble();
      double dy1 = this.yCoord + rand.nextDouble();
      double dz1 = this.zCoord + rand.nextDouble();
      double dx2 = this.xCoord + rand.nextDouble();
      double dy2 = this.yCoord + rand.nextDouble();
      double dz2 = this.zCoord + rand.nextDouble();
      this.worldObj.spawnParticle("portal", dx1, dy1, dz1, dx2 - dx1, dy2 - dy1, dz2 - dz1);
    } 
    if (!this.worldObj.isRemote) {
      int newMeta = -1;
      if (this.energy.getEnergyStored() == this.energy.getMaxEnergyStored() && canAddMorez()) {
        ItemStack result = this.inv.result.getStackInSlot(0).copy();
        for (int i = 0; i < 9; i++)
          this.inv.matrix.decrStackSize(i, 1); 
        this.inv.result.markDirty(this.inv.matrix);
        if (this.outputslot == null) {
          this.outputslot = result;
        } else {
          this.outputslot.stackSize += result.stackSize;
        } 
        this.energy.setEnergyStored(0);
        if (!canAddMorez())
          newMeta = 4; 
      } 
      if (this.energy.getEnergyStored() > 0)
        if (canAddMorez()) {
          newMeta = 1;
          this.coolDown = 20;
        } else {
          this.energy.extractEnergy(1, false);
        }  
      if (this.coolDown > 0) {
        this.coolDown--;
        if (this.coolDown == 0)
          newMeta = 0; 
      } 
      if (newMeta != -1 && newMeta != getBlockMetadata())
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, newMeta, 2); 
    } 
  }
  
  public boolean shouldSoundPlay() {
    return (getBlockMetadata() == 1);
  }
  
  ResourceLocation location = new ResourceLocation("extrautils", "ambient.qed");
  
  public ResourceLocation getSound() {
    return this.location;
  }
  
  public TileEntity getTile() {
    return this;
  }
  
  public static class CustomEnergy extends EnergyStorage {
    public CustomEnergy(int capacity) {
      super(capacity);
    }
    
    public EnergyStorage readFromNBT(NBTTagCompound nbt) {
      return super.readFromNBT(nbt);
    }
    
    public void setEnergyStored(int energy) {
      super.setEnergyStored(energy);
    }
    
    public int receiveEnergy(int maxReceive, boolean simulate) {
      return super.receiveEnergy(maxReceive, simulate);
    }
    
    public int extractEnergy(int maxExtract, boolean simulate) {
      return super.extractEnergy(maxExtract, simulate);
    }
    
    public void setCapacity(int capacity) {
      super.setCapacity(capacity);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\TileEnderConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */