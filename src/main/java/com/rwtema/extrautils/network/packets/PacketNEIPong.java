package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.nei.ping.ParticlePing;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;

public class PacketNEIPong extends XUPacketBase {
  List<ChunkPosition> positionList;
  
  public static final int MAX_SIZE = 20;
  
  public PacketNEIPong() {}
  
  public PacketNEIPong(List<ChunkPosition> positionList) {
    this.positionList = positionList;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeByte(this.positionList.size());
    for (ChunkPosition pos : this.positionList) {
      data.writeInt(pos.chunkPosX);
      data.writeByte(pos.chunkPosY);
      data.writeInt(pos.chunkPosZ);
    } 
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    int n = data.readUnsignedByte();
    if (n > 20)
      n = 20; 
    this.positionList = new ArrayList<ChunkPosition>(n);
    for (int i = 0; i < n; i++) {
      int x = data.readInt();
      int y = data.readUnsignedByte();
      int z = data.readInt();
      this.positionList.add(new ChunkPosition(x, y, z));
    } 
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    (Minecraft.getMinecraft()).thePlayer.closeScreen();
    for (ChunkPosition chunkPosition : this.positionList) {
      for (int i = 0; i < 20; i++)
        (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)new ParticlePing((Minecraft.getMinecraft()).theWorld, chunkPosition)); 
    } 
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketNEIPong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */