package com.rwtema.extrautils.tileentity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnchantedSpike extends TileEntity {
  NBTTagList enchants;
  
  public boolean canUpdate() {
    return false;
  }
  
  public void setEnchantmentTagList(NBTTagList enchants) {
    this.enchants = enchants;
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.enchants = tags.getTagList("ench", 10);
  }
  
  public void writeToNBT(NBTTagCompound tags) {
    super.writeToNBT(tags);
    if (this.enchants != null && this.enchants.tagCount() > 0)
      tags.setTag("ench", (NBTBase)this.enchants); 
  }
  
  public NBTTagCompound getEnchantmentTagList() {
    if (this.enchants == null || this.enchants.tagCount() == 0)
      return null; 
    NBTTagCompound tagCompound = new NBTTagCompound();
    tagCompound.setTag("ench", this.enchants.copy());
    return tagCompound;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityEnchantedSpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */