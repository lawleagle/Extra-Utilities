package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketGUIWidget extends XUPacketBase {
  int window_id;
  
  NBTTagCompound tag;
  
  public PacketGUIWidget() {}
  
  public PacketGUIWidget(int window_id, NBTTagCompound tag) {
    this.tag = tag;
    this.window_id = window_id;
  }
  
  public void writeData(ByteBuf data) {
    data.writeInt(this.window_id);
    writeNBT(data, this.tag);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    try {
      this.window_id = data.readInt();
      this.tag = readNBT(data);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    if (this.tag != null && 
      this.window_id != 0 && this.window_id == (ExtraUtilsMod.proxy.getClientPlayer()).openContainer.windowId)
      try {
        ((DynamicContainer)(ExtraUtilsMod.proxy.getClientPlayer()).openContainer).recieveDescriptionPacket(this.tag);
      } catch (Exception e) {
        e.printStackTrace();
      }  
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketGUIWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */