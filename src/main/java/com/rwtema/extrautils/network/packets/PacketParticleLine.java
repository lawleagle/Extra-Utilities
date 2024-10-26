package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.particle.ParticleEndSmoke;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

public class PacketParticleLine extends XUPacketBase {
  Vec3 start;
  
  Vec3 end;
  
  public PacketParticleLine() {}
  
  public PacketParticleLine(EntityItem item, TileEntity tile) {
    this.start = Vec3.createVectorHelper(item.posX, item.posY, item.posZ);
    this.end = Vec3.createVectorHelper(tile.xCoord + 0.5D, tile.yCoord + 0.8D, tile.zCoord + 0.5D);
  }
  
  public PacketParticleLine(Vec3 start, Vec3 end) {
    this.start = start;
    this.end = end;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    writeVec(data, this.start);
    writeVec(data, this.end);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.start = readVec(data);
    this.end = readVec(data);
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    double v = 0.25D / this.end.subtract(this.start).lengthVector();
    for (double h = 0.0D; h <= 1.0D; h += v) {
      float f = XUHelper.rand.nextFloat() * 0.6F + 0.4F;
      double x = this.start.xCoord + (this.end.xCoord - this.start.xCoord) * h;
      double y = this.start.yCoord + (this.end.yCoord - this.start.yCoord) * h;
      double z = this.start.zCoord + (this.end.zCoord - this.start.zCoord) * h;
      ParticleHelperClient.addParticle((EntityFX)new ParticleEndSmoke(ExtraUtilsMod.proxy.getClientWorld(), x, y, z, f, f * 0.3F, f * 0.9F));
    } 
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return properSenderSide.isServer();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketParticleLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */