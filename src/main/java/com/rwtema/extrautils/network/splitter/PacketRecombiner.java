package com.rwtema.extrautils.network.splitter;

import com.rwtema.extrautils.network.NetworkHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.HashMap;
import java.util.Map;

@Sharable
public class PacketRecombiner extends SimpleChannelInboundHandler<XUPacketSplit> {
  public static Map<Integer, ByteBuf[]> map = (Map)new HashMap<Integer, ByteBuf>();
  
  protected void channelRead0(ChannelHandlerContext ctx, XUPacketSplit msg) throws Exception {
    ByteBuf[] b = map.get(Integer.valueOf(msg.packetIndex));
    if (b == null || b.length != msg.total)
      b = new ByteBuf[msg.total]; 
    b[msg.seq] = msg.data;
    boolean flag = true;
    int s = 0;
    for (int i = 0; i < b.length && flag; i++) {
      if (b[i] != null) {
        flag = false;
        s += b[i].readableBytes();
      } 
    } 
    if (flag) {
      ByteBuf data = Unpooled.buffer(s);
      for (int j = 0; j < b.length; j++)
        data.writeBytes(b[j]); 
      FMLProxyPacket proxy = new FMLProxyPacket(data, (String)((FMLEmbeddedChannel)NetworkHandler.channels.get(Side.CLIENT)).attr(NetworkRegistry.FML_CHANNEL).get());
      ((FMLEmbeddedChannel)NetworkHandler.channels.get(FMLCommonHandler.instance().getEffectiveSide())).writeInbound(new Object[] { proxy });
      map.remove(Integer.valueOf(msg.packetIndex));
      if (map.size() > 1024)
        map.clear(); 
    } else {
      map.put(Integer.valueOf(msg.packetIndex), b);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\splitter\PacketRecombiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */