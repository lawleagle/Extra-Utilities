package com.rwtema.extrautils.sync;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHandlers {
  public static class NBTHandlerByte {
    public static void save(NBTTagCompound tag, String fieldName, byte b) {
      tag.setByte(fieldName, b);
    }
    
    public static byte load(NBTTagCompound tag, String fieldName) {
      return tag.getByte(fieldName);
    }
  }
  
  public static class NBTHandlerShort {
    public static void save(NBTTagCompound tag, String fieldName, short s) {
      tag.setShort(fieldName, s);
    }
    
    public static short load(NBTTagCompound tag, String fieldName) {
      return tag.getShort(fieldName);
    }
  }
  
  public static class NBTHandlerInt {
    public static void save(NBTTagCompound tag, String fieldName, int s) {
      tag.setInteger(fieldName, s);
    }
    
    public static int load(NBTTagCompound tag, String fieldName) {
      return tag.getInteger(fieldName);
    }
  }
  
  public static class NBTHandlerLong {
    public static void save(NBTTagCompound tag, String fieldName, long s) {
      tag.setLong(fieldName, s);
    }
    
    public static long load(NBTTagCompound tag, String fieldName) {
      return tag.getLong(fieldName);
    }
  }
  
  public static class NBTHandlerFloat {
    public static void save(NBTTagCompound tag, String fieldName, float s) {
      tag.setFloat(fieldName, s);
    }
    
    public static float load(NBTTagCompound tag, String fieldName) {
      return tag.getFloat(fieldName);
    }
  }
  
  public static class NBTHandlerDouble {
    public static void save(NBTTagCompound tag, String fieldName, double s) {
      tag.setDouble(fieldName, s);
    }
    
    public static double load(NBTTagCompound tag, String fieldName) {
      return tag.getDouble(fieldName);
    }
  }
  
  public static class NBTHandlerByteArray {
    public static void save(NBTTagCompound tag, String fieldName, byte[] s) {
      tag.setByteArray(fieldName, s);
    }
    
    public static byte[] load(NBTTagCompound tag, String fieldName) {
      return tag.getByteArray(fieldName);
    }
  }
  
  public static class NBTHandlerString {
    public static void save(NBTTagCompound tag, String fieldName, String s) {
      tag.setString(fieldName, s);
    }
    
    public static String load(NBTTagCompound tag, String fieldName) {
      return tag.getString(fieldName);
    }
  }
  
  public static class NBTHandlerNBT {
    public static void save(NBTTagCompound tag, String fieldName, NBTTagCompound s) {
      tag.setTag(fieldName, (NBTBase)s);
    }
    
    public static NBTTagCompound load(NBTTagCompound tag, String fieldName) {
      return tag.getCompoundTag(fieldName);
    }
  }
  
  public static class NBTHandlerIntArray {
    public static void save(NBTTagCompound tag, String fieldName, int[] s) {
      tag.setIntArray(fieldName, s);
    }
    
    public static int[] load(NBTTagCompound tag, String fieldName) {
      return tag.getIntArray(fieldName);
    }
  }
  
  public static class NBTHandlerItemStack {
    public static void save(NBTTagCompound tag, String fieldName, ItemStack s) {
      if (s != null)
        tag.setTag(fieldName, (NBTBase)s.writeToNBT(new NBTTagCompound())); 
    }
    
    public static ItemStack load(NBTTagCompound tag, String fieldName) {
      return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(fieldName));
    }
  }
  
  public static class NBTHandlerBoolean {
    public static void save(NBTTagCompound tag, String fieldName, boolean s) {
      tag.setBoolean(fieldName, s);
    }
    
    public static boolean load(NBTTagCompound tag, String fieldName) {
      return tag.getBoolean(fieldName);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sync\NBTHandlers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */