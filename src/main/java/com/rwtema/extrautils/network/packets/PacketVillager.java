package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipeList;

public class PacketVillager extends XUPacketBase {
  public int x;
  
  public int y;
  
  public int z;
  
  public NBTTagCompound tag;
  
  public PacketVillager() {}
  
  public PacketVillager(int x, int y, int z, NBTTagCompound tag) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.tag = tag;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeInt(this.x);
    data.writeInt(this.y);
    data.writeInt(this.z);
    writeNBT(data, this.tag);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.x = data.readInt();
    this.y = data.readInt();
    this.z = data.readInt();
    this.tag = readNBT(data);
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    if (this.tag == null)
      return; 
    if (!this.tag.hasKey("player_id"))
      return; 
    if (!this.tag.hasKey("n"))
      return; 
    TileEntity tile = ExtraUtilsMod.proxy.getClientWorld().getTileEntity(this.x, this.y, this.z);
    if (!(tile instanceof TileEntityTradingPost))
      return; 
    TileEntityTradingPost t2 = (TileEntityTradingPost)tile;
    int n = this.tag.getInteger("n");
    t2.ids = new int[n];
    t2.data = new MerchantRecipeList[n];
    for (int i = 0; i < n; i++) {
      t2.ids[i] = this.tag.getInteger("i" + i);
      t2.data[i] = new MerchantRecipeList(this.tag.getCompoundTag("t" + i));
    } 
    ExtraUtilsMod.proxy.getClientPlayer().openGui(ExtraUtilsMod.instance, 0, ExtraUtilsMod.proxy.getClientWorld(), this.x, this.y, this.z);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */