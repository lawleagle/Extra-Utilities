package com.rwtema.extrautils.sync;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AutoNBT<T> {
  public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound, T paramT);
  
  public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound, T paramT);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sync\AutoNBT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */