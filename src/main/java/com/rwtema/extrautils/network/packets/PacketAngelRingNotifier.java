package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.EventHandlerClient;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketAngelRingNotifier extends XUPacketBase {
  String username;
  
  int wingType;
  
  public PacketAngelRingNotifier() {}
  
  public PacketAngelRingNotifier(String player, int wing) {
    this.username = player;
    this.wingType = wing;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    writeString(data, this.username);
    data.writeByte(this.wingType);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.username = readString(data);
    this.wingType = data.readByte();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    if (this.wingType > 0) {
      EventHandlerClient.flyingPlayers.put(this.username, Integer.valueOf(this.wingType));
    } else {
      EventHandlerClient.flyingPlayers.remove(this.username);
    } 
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketAngelRingNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */