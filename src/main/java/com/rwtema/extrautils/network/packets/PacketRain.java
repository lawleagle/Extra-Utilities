package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class PacketRain extends XUPacketBase {
  private boolean shouldRain;
  
  public PacketRain() {}
  
  public PacketRain(boolean shouldRain) {
    this.shouldRain = shouldRain;
  }
  
  public void writeData(ByteBuf data) {
    data.writeBoolean(this.shouldRain);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.shouldRain = data.readBoolean();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    NBTTagCompound t = new NBTTagCompound();
    EntityPlayer player = ExtraUtilsMod.proxy.getClientPlayer();
    if (player.getEntityData().hasKey("PlayerPersisted")) {
      t = player.getEntityData().getCompoundTag("PlayerPersisted");
    } else {
      player.getEntityData().setTag("PlayerPersisted", (NBTBase)t);
    } 
    t.setBoolean("ExtraUtilities|Rain", this.shouldRain);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketRain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */