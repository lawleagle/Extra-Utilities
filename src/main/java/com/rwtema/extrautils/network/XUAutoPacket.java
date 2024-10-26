package com.rwtema.extrautils.network;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class XUAutoPacket extends XUPacketBase {
  boolean init = false;
  
  public void getReflectData() {
    getClass().getDeclaredFields();
  }
  
  public void writeField(Field f, ByteBuf data) throws IllegalAccessException {
    Class<?> type = f.getType();
    if (String.class.equals(type)) {
      writeString(data, (String)f.get(this));
    } else if (byte.class.equals(type)) {
      data.writeByte(((Byte)f.get(this)).byteValue());
    } else if (short.class.equals(type)) {
      data.writeShort(((Short)f.get(this)).shortValue());
    } else if (int.class.equals(type)) {
      data.writeInt(((Integer)f.get(this)).intValue());
    } else if (long.class.equals(type)) {
      data.writeDouble(((Long)f.get(this)).longValue());
    } else if (float.class.equals(type)) {
      data.writeFloat(((Float)f.get(this)).floatValue());
    } else if (double.class.equals(type)) {
      data.writeDouble(((Double)f.get(this)).doubleValue());
    } else if (NBTTagCompound.class.equals(type)) {
      writeNBT(data, (NBTTagCompound)f.get(this));
    } 
  }
  
  public void readField(Field f, ByteBuf data) throws IllegalAccessException {
    Class<?> type = f.getType();
    if (String.class.equals(type)) {
      f.set(this, readString(data));
    } else if (byte.class.equals(type)) {
      f.set(this, Byte.valueOf(data.readByte()));
    } else if (short.class.equals(type)) {
      f.set(this, Short.valueOf(data.readShort()));
    } else if (int.class.equals(type)) {
      f.set(this, Integer.valueOf(data.readInt()));
    } else if (long.class.equals(type)) {
      f.set(this, Long.valueOf(data.readLong()));
    } else if (float.class.equals(type)) {
      f.set(this, Float.valueOf(data.readFloat()));
    } else if (double.class.equals(type)) {
      f.set(this, Double.valueOf(data.readDouble()));
    } else if (NBTTagCompound.class.equals(type)) {
      f.set(this, readNBT(data));
    } 
  }
  
  public void writeData(ByteBuf data) throws Exception {}
  
  public void readData(EntityPlayer player, ByteBuf data) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\XUAutoPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */