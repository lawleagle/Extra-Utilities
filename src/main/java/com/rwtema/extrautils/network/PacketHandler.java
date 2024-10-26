package com.rwtema.extrautils.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<XUPacketBase> {
  protected void channelRead0(ChannelHandlerContext ctx, XUPacketBase msg) throws Exception {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
      msg.doStuffServer(ctx); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\PacketHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */