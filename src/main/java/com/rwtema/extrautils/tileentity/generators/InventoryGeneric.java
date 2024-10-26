package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryGeneric extends InventoryBasic {
  public InventoryGeneric(String par1Str, boolean par2, int par3) {
    super(par1Str, par2, par3);
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    NBTTagCompound tag = new NBTTagCompound();
    for (int i = 0; i < getSizeInventory(); i++) {
      ItemStack item = getStackInSlot(i);
      if (item != null) {
        NBTTagCompound itemtag = new NBTTagCompound();
        item.writeToNBT(itemtag);
        tag.setTag("item_" + i, (NBTBase)itemtag);
      } 
    } 
    nbt.setTag("items", (NBTBase)tag);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    if (!nbt.hasKey("items")) {
      for (int i = 0; i < getSizeInventory(); i++)
        setInventorySlotContents(i, null); 
    } else {
      NBTTagCompound tag = nbt.getCompoundTag("items");
      for (int i = 0; i < getSizeInventory(); i++) {
        if (tag.hasKey("item_" + i)) {
          setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item_" + i)));
        } else {
          setInventorySlotContents(i, null);
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\InventoryGeneric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */