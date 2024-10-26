package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

public class PacketParticle extends XUPacketBase {
  public static List<String> particleNames = new ArrayList<String>();
  
  public String p;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public double vx;
  
  public double vy;
  
  public double vz;
  
  static {
    particleNames.add("bubble");
    particleNames.add("suspended");
    particleNames.add("depthsuspend");
    particleNames.add("townaura");
    particleNames.add("crit");
    particleNames.add("magicCrit");
    particleNames.add("smoke");
    particleNames.add("mobSpell");
    particleNames.add("mobSpellAmbient");
    particleNames.add("spell");
    particleNames.add("instantSpell");
    particleNames.add("witchMagic");
    particleNames.add("note");
    particleNames.add("portal");
    particleNames.add("enchantmenttable");
    particleNames.add("explode");
    particleNames.add("flame");
    particleNames.add("lava");
    particleNames.add("footstep");
    particleNames.add("splash");
    particleNames.add("largesmoke");
    particleNames.add("cloud");
    particleNames.add("reddust");
    particleNames.add("snowballpoof");
    particleNames.add("dripWater");
    particleNames.add("dripLava");
    particleNames.add("snowshovel");
    particleNames.add("slime");
    particleNames.add("heart");
    particleNames.add("angryVillager");
    particleNames.add("happyVillager");
  }
  
  public PacketParticle() {}
  
  public PacketParticle(String p, double x, double y, double z, double vx, double vy, double vz) {
    this.p = p;
    this.x = x;
    this.y = y;
    this.z = z;
    this.vx = vx;
    this.vy = vy;
    this.vz = vz;
  }
  
  public void writeData(ByteBuf dataoutputstream) throws Exception {
    if (!particleNames.contains(this.p)) {
      dataoutputstream.writeByte(0);
    } else {
      dataoutputstream.writeByte(particleNames.indexOf(this.p));
    } 
    dataoutputstream.writeFloat((float)this.x);
    dataoutputstream.writeFloat((float)this.y);
    dataoutputstream.writeFloat((float)this.z);
    dataoutputstream.writeFloat((float)this.vx);
    dataoutputstream.writeFloat((float)this.vy);
    dataoutputstream.writeFloat((float)this.vz);
  }
  
  public void readData(EntityPlayer player, ByteBuf datainputstream) {
    this.p = particleNames.get(datainputstream.readUnsignedByte());
    this.x = datainputstream.readFloat();
    this.y = datainputstream.readFloat();
    this.z = datainputstream.readFloat();
    this.vx = datainputstream.readFloat();
    this.vy = datainputstream.readFloat();
    this.vz = datainputstream.readFloat();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    ExtraUtilsMod.proxy.getClientWorld().spawnParticle(this.p, this.x, this.y, this.z, this.vx, this.vy, this.vz);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketParticle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */