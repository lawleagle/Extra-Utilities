package com.rwtema.extrautils.network.splitter;

import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;

public class PacketSplitter {
  static int curSendingIndex = XUHelper.rand.nextInt();
  
  public static final int maxSize = 2007136;
  
  public static boolean shouldSplit(FMLProxyPacket packet) {
    return (packet.payload().readableBytes() >= 2007136);
  }
  
  public static List<XUPacketSplit> splitPacket(FMLProxyPacket packet) {
    List<XUPacketSplit> out = new ArrayList<XUPacketSplit>();
    ByteBuf buf = packet.payload().copy();
    int n = buf.readableBytes() / 2007136;
    if (n * 2007136 < buf.readableBytes())
      n++; 
    curSendingIndex++;
    LogHelper.debug("Splitting packet to " + n + " packets", new Object[0]);
    for (int i = 0; i < n; i++) {
      int s = (buf.readableBytes() < 2007136) ? buf.readableBytes() : 2007136;
      ByteBuf o = Unpooled.buffer(s);
      buf.readBytes(o, s);
      out.add(new XUPacketSplit(buf, curSendingIndex, i, n));
    } 
    return out;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\splitter\PacketSplitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */