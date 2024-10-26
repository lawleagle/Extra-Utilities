package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.CommandTPSTimer;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public class PacketTime extends XUPacketBase {
  long time;
  
  int counter;
  
  public PacketTime() {
    this(0L, 0);
  }
  
  public PacketTime(long time, int counter) {
    this.time = time;
    this.counter = counter;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeLong(this.time);
    data.writeByte(this.counter);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.time = data.readLong();
    this.counter = data.readUnsignedByte();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {
    INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
    EntityPlayer player = ExtraUtilsMod.proxy.getPlayerFromNetHandler(netHandler);
    CommandTPSTimer.add(player.getCommandSenderName());
  }
  
  public void doStuffClient() {
    CommandTPSTimer.update(this.counter, this.time);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */