package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class PacketVillagerReturn extends XUPacketBase {
  public int merchantId;
  
  public int world;
  
  public int x;
  
  public int y;
  
  public int z;
  
  EntityPlayerMP p;
  
  World theworld;
  
  public PacketVillagerReturn() {}
  
  public PacketVillagerReturn(int merchantId, int world, int x, int y, int z) {
    this.merchantId = merchantId;
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeInt(this.merchantId);
    data.writeInt(this.world);
    data.writeInt(this.x);
    data.writeInt(this.y);
    data.writeInt(this.z);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.merchantId = data.readInt();
    this.world = data.readInt();
    this.x = data.readInt();
    this.y = data.readInt();
    this.z = data.readInt();
    this.p = (EntityPlayerMP)player;
    this.theworld = this.p.getEntityWorld();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {
    try {
      if (this.world != this.theworld.provider.dimensionId)
        return; 
      if (this.p.getDistance(this.x, this.y, this.z) > 6.0D)
        return; 
      if (this.theworld.getEntityByID(this.merchantId) instanceof IMerchant) {
        Entity villager = this.theworld.getEntityByID(this.merchantId);
        IMerchant trader = (IMerchant)this.theworld.getEntityByID(this.merchantId);
        if ((((Math.abs(villager.posX - this.x) < TileEntityTradingPost.maxRange) ? 1 : 0) & ((Math.abs(villager.posZ - this.z) < TileEntityTradingPost.maxRange) ? 1 : 0)) != 0) {
          if (!villager.isEntityAlive()) {
            PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager has died"));
          } else if (trader.getCustomer() != null) {
            PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager is busy"));
          } else {
            this.p.interactWith(villager);
          } 
        } else {
          PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager is no longer in range"));
        } 
      } else {
        PacketTempChat.sendChat((EntityPlayer)this.p, (IChatComponent)new ChatComponentText("Villager can no longer be found"));
      } 
    } catch (Exception exception4) {
      exception4.printStackTrace();
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {}
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.CLIENT);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketVillagerReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */