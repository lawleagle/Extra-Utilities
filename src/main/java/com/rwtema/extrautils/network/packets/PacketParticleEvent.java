package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.particle.ParticleTransferNodes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;

public class PacketParticleEvent extends XUPacketBase {
  int x;
  
  int y;
  
  int z;
  
  byte i;
  
  public PacketParticleEvent() {}
  
  public PacketParticleEvent(int x, int y, int z, byte i) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.i = i;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeInt(this.x);
    data.writeInt(this.y);
    data.writeInt(this.z);
    data.writeByte(this.i);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.x = data.readInt();
    this.y = data.readInt();
    this.z = data.readInt();
    this.i = data.readByte();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    doParticleEvent(this.i, this.x, this.y, this.z);
  }
  
  private static Random rand = (Random)XURandom.getInstance();
  
  @SideOnly(Side.CLIENT)
  private void doParticleEvent(int type, int x, int y, int z) {
    int dy;
    int y1;
    switch (type) {
      case 0:
        dy = 1;
        for (y1 = y + dy; y1 < 256; y1 += dy) {
          ExtraUtilsMod.proxy.getClientWorld().spawnParticle("portal", x + 0.5D, y1 + 0.5D, z + 0.5D, rand.nextGaussian() * 0.1D, -dy + rand.nextGaussian() * 0.1D, rand.nextGaussian() * 0.1D);
          dy = rand.nextInt(8);
        } 
        break;
      case 1:
        ExtraUtilsMod.proxy.getClientWorld().spawnParticle("reddust", x + 0.5D, y + 0.5D, z + 0.5D, 1.0D, 0.3D, 0.9D);
        break;
      case 2:
        dy = 1;
        for (y1 = y + dy; y1 < 256; y1 += dy) {
          ExtraUtilsMod.proxy.getClientWorld().spawnParticle("portal", x + 0.5D, y1 + 0.5D, z + 0.5D, rand.nextGaussian() * 0.1D, dy + rand.nextGaussian() * 0.1D, rand.nextGaussian() * 0.1D);
          dy = rand.nextInt(8);
        } 
        break;
      case 3:
        spawnNodeParticles(x, y, z, 1.0F, 0.0F, 0.0F);
        break;
      case 4:
        spawnNodeParticles(x, y, z, 0.0F, 0.0F, 1.0F);
        break;
      case 5:
        spawnNodeParticles(x, y, z, 0.0F, 1.0F, 0.0F);
        break;
      case 6:
        spawnNodeParticles(x, y, z, 0.0F, 1.0F, 1.0F);
        break;
      case 7:
        spawnNodeParticles(x, y, z, 1.0F, 1.0F, 0.0F);
        break;
    } 
  }
  
  public static final int[] dx = new int[] { 0, 1, 0, 1, 0, 1, 0, 1 };
  
  public static final int[] dy = new int[] { 0, 0, 1, 1, 0, 0, 1, 1 };
  
  public static final int[] dz = new int[] { 0, 0, 0, 0, 1, 1, 1, 1 };
  
  public static final double w = 0.2D;
  
  public static final double w1 = 0.3D;
  
  public static final double w2 = 0.4D;
  
  @SideOnly(Side.CLIENT)
  private void spawnNodeParticles(int x, int y, int z, float pr, float pg, float pb) {
    if (ExtraUtils.disableNodeParticles)
      return; 
    (Minecraft.getMinecraft()).effectRenderer.addEffect((EntityFX)new ParticleTransferNodes(ExtraUtilsMod.proxy.getClientWorld(), x + 0.5D, y + 0.5D, z + 0.5D, pr, pg, pb));
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketParticleEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */