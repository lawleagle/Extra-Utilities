package com.rwtema.extrautils.sync;

import net.minecraft.nbt.NBTTagCompound;

public class AutoNBTTest extends AutoNBT {
  public void writeToNBT(NBTTagCompound tag, Object instance) {
    NBTTest t = (NBTTest)instance;
    NBTHandlers.NBTHandlerFloat.save(tag, "f", t.hat);
    NBTHandlers.NBTHandlerInt.save(tag, "i", t.hello);
  }
  
  public void readFromNBT(NBTTagCompound tag, Object instance) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sync\AutoNBTTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */