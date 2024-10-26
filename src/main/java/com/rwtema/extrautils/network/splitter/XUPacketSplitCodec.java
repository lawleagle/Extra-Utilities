package com.rwtema.extrautils.network.splitter;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class XUPacketSplitCodec extends FMLIndexedMessageToMessageCodec<XUPacketSplit> {
  public XUPacketSplitCodec() {
    addDiscriminator(0, XUPacketSplit.class);
  }
  
  public void encodeInto(ChannelHandlerContext ctx, XUPacketSplit msg, ByteBuf target) throws Exception {
    target.writeInt(msg.packetIndex);
    target.writeInt(msg.seq);
    target.writeInt(msg.total);
    target.writeBytes(msg.data);
  }
  
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, XUPacketSplit msg) {
    msg.packetIndex = source.readInt();
    msg.seq = source.readInt();
    msg.total = source.readInt();
    ByteBuf buffer = Unpooled.buffer(source.readableBytes());
    source.readBytes(buffer);
    msg.data = buffer;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\splitter\XUPacketSplitCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */