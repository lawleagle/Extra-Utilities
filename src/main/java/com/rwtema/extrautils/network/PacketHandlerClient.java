package com.rwtema.extrautils.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
@SideOnly(Side.CLIENT)
public class PacketHandlerClient extends PacketHandler {
  protected void channelRead0(ChannelHandlerContext ctx, XUPacketBase msg) throws Exception {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
      msg.doStuffServer(ctx);
    } else {
      msg.doStuffClient();
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\PacketHandlerClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */