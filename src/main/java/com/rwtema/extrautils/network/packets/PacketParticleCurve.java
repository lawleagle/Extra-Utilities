package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.SplineHelper;
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
import net.minecraft.util.Vec3;

public class PacketParticleCurve extends XUPacketBase {
  Vec3 startPos;
  
  Vec3 endPos;
  
  Vec3 startVel;
  
  Vec3 endVel;
  
  public PacketParticleCurve() {}
  
  public PacketParticleCurve(Vec3 startPos, Vec3 endPos, Vec3 startVel, Vec3 endVel) {
    this.startPos = startPos;
    this.endPos = endPos;
    this.startVel = startVel;
    this.endVel = endVel;
  }
  
  public static Vec3 matchSpeed(EntityItem item, Vec3 v) {
    double s = 5.0D;
    return Vec3.createVectorHelper(v.xCoord * s, v.yCoord * s, v.zCoord * s);
  }
  
  public PacketParticleCurve(EntityItem item, Vec3 dest, Vec3 vec) {
    this(Vec3.createVectorHelper(item.posX, item.posY, item.posZ), dest, Vec3.createVectorHelper(item.motionX * 5.0D, item.motionY * 5.0D, item.motionZ * 5.0D), matchSpeed(item, vec));
  }
  
  public void writeData(ByteBuf data) throws Exception {
    writeVec(data, this.startPos);
    writeVec(data, this.endPos);
    writeVec(data, this.startVel);
    writeVec(data, this.endVel);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.startPos = readVec(data);
    this.endPos = readVec(data);
    this.startVel = readVec(data);
    this.endVel = readVec(data);
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    double v = 0.15D / this.endPos.subtract(this.startPos).lengthVector();
    double[] xParam = SplineHelper.splineParams(this.startPos.xCoord, this.endPos.xCoord, this.startVel.xCoord, this.endVel.xCoord);
    double[] yParam = SplineHelper.splineParams(this.startPos.yCoord, this.endPos.yCoord, this.startVel.yCoord, this.endVel.yCoord);
    double[] zParam = SplineHelper.splineParams(this.startPos.zCoord, this.endPos.zCoord, this.startVel.zCoord, this.endVel.zCoord);
    float f = XUHelper.rand.nextFloat() * 0.6F + 0.4F;
    double h;
    for (h = v; h <= 1.0D; h += v) {
      double x = SplineHelper.evalSpline(h, xParam);
      double y = SplineHelper.evalSpline(h, yParam);
      double z = SplineHelper.evalSpline(h, zParam);
      ParticleHelperClient.addParticle((EntityFX)new ParticleEndSmoke(ExtraUtilsMod.proxy.getClientWorld(), x, y, z, f, f * 0.3F, f * 0.9F));
    } 
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return properSenderSide.isServer();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketParticleCurve.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */