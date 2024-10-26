package com.rwtema.extrautils.network.packets;

import com.google.common.base.Throwables;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class PacketUseItemAlt extends XUPacketBase {
  private int x;
  
  private int y;
  
  private int z;
  
  private int face;
  
  private ItemStack item;
  
  private float hitX;
  
  private float hitY;
  
  private float hitZ;
  
  private EntityPlayerMP player;
  
  public PacketUseItemAlt(int x, int y, int z, int face, ItemStack item, float hitX, float hitY, float hitZ) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.face = face;
    this.item = item;
    this.hitX = hitX;
    this.hitY = hitY;
    this.hitZ = hitZ;
  }
  
  public PacketUseItemAlt() {}
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeInt(this.x);
    data.writeInt(this.y);
    data.writeInt(this.z);
    data.writeByte(this.face);
    writeItemStack(data, this.item);
    data.writeByte((byte)(int)(this.hitX * 16.0F));
    data.writeByte((byte)(int)(this.hitY * 16.0F));
    data.writeByte((byte)(int)(this.hitZ * 16.0F));
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.x = data.readInt();
    this.y = data.readInt();
    this.z = data.readInt();
    this.face = data.readByte();
    this.item = readItemStack(data);
    this.hitX = data.readByte() * 0.0625F;
    this.hitY = data.readByte() * 0.0625F;
    this.hitZ = data.readByte() * 0.0625F;
    this.player = (EntityPlayerMP)player;
  }
  
  public static final ThreadLocal<Boolean> altPlace = new ThreadLocal<Boolean>();
  
  public synchronized void doStuffServer(ChannelHandlerContext ctx) {
    C08PacketPlayerBlockPlacement placement;
    try {
      PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
      packetbuffer.writeInt(this.x);
      packetbuffer.writeByte(this.y);
      packetbuffer.writeInt(this.z);
      packetbuffer.writeByte(this.face);
      packetbuffer.writeItemStackToBuffer(this.item);
      packetbuffer.writeByte((int)(this.hitX * 16.0F));
      packetbuffer.writeByte((int)(this.hitY * 16.0F));
      packetbuffer.writeByte((int)(this.hitZ * 16.0F));
      placement = new C08PacketPlayerBlockPlacement();
      placement.readPacketData(packetbuffer);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    } 
    altPlace.set(Boolean.valueOf(true));
    this.player.playerNetServerHandler.processPlayerBlockPlacement(placement);
    altPlace.set(Boolean.valueOf(false));
  }
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {}
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.CLIENT);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketUseItemAlt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */