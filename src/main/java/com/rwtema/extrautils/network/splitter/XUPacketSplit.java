package com.rwtema.extrautils.network.splitter;

import io.netty.buffer.ByteBuf;

public class XUPacketSplit {
  ByteBuf data;
  
  int seq;
  
  int packetIndex;
  
  int total;
  
  public XUPacketSplit() {}
  
  public XUPacketSplit(ByteBuf data, int packetIndex, int seq, int total) {
    this.data = data;
    this.packetIndex = packetIndex;
    this.seq = seq;
    this.total = total;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\splitter\XUPacketSplit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */