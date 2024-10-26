package com.rwtema.extrautils.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.nio.charset.Charset;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;

public abstract class XUPacketBase {
  public abstract void writeData(ByteBuf paramByteBuf) throws Exception;
  
  public void writeVec(ByteBuf data, Vec3 vec3) {
    data.writeFloat((float)vec3.xCoord);
    data.writeFloat((float)vec3.yCoord);
    data.writeFloat((float)vec3.zCoord);
  }
  
  public void writeChatComponent(ByteBuf data, IChatComponent chatComponent) {
    writeString(data, IChatComponent.Serializer.func_150696_a(chatComponent));
  }
  
  public IChatComponent readChatComponent(ByteBuf data) {
    return IChatComponent.Serializer.func_150699_a(readString(data));
  }
  
  public abstract void readData(EntityPlayer paramEntityPlayer, ByteBuf paramByteBuf);
  
  public Vec3 readVec(ByteBuf data) {
    return Vec3.createVectorHelper(data.readFloat(), data.readFloat(), data.readFloat());
  }
  
  public abstract void doStuffServer(ChannelHandlerContext paramChannelHandlerContext);
  
  @SideOnly(Side.CLIENT)
  public abstract void doStuffClient();
  
  public abstract boolean isValidSenderSide(Side paramSide);
  
  public void writeString(ByteBuf data, String string) {
    byte[] stringData = string.getBytes(Charset.forName("UTF-8"));
    data.writeShort(stringData.length);
    data.writeBytes(stringData);
  }
  
  public String readString(ByteBuf data) {
    short length = data.readShort();
    byte[] bytes = new byte[length];
    data.readBytes(bytes);
    return new String(bytes, Charset.forName("UTF-8"));
  }
  
  public void writeNBT(ByteBuf data, NBTTagCompound tag) {
    if (tag == null) {
      data.writeShort(-1);
      return;
    } 
    try {
      byte[] compressed = CompressedStreamTools.compress(tag);
      data.writeShort(compressed.length);
      data.writeBytes(compressed);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public NBTTagCompound readNBT(ByteBuf data) {
    short length = data.readShort();
    if (length <= 0)
      return null; 
    byte[] bytes = new byte[length];
    data.readBytes(bytes);
    try {
      return CompressedStreamTools.func_152457_a(bytes, NBTSizeTracker.field_152451_a);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void writeItemStack(ByteBuf data, ItemStack item) {
    if (item == null) {
      data.writeShort(-1);
    } else {
      data.writeShort(Item.getIdFromItem(item.getItem()));
      data.writeByte(item.stackSize);
      data.writeShort(item.getItemDamage());
      NBTTagCompound nbttagcompound = null;
      if (item.getItem().isDamageable() || item.getItem().getShareTag())
        nbttagcompound = item.stackTagCompound; 
      writeNBT(data, nbttagcompound);
    } 
  }
  
  public ItemStack readItemStack(ByteBuf data) {
    ItemStack itemstack = null;
    short id = data.readShort();
    if (id >= 0) {
      byte stackSize = data.readByte();
      short metadata = data.readShort();
      itemstack = new ItemStack(Item.getItemById(id), stackSize, metadata);
      itemstack.stackTagCompound = readNBT(data);
    } 
    return itemstack;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\XUPacketBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */