package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class PacketTempChat extends XUPacketBase {
  IChatComponent chatComponent;
  
  public static void sendChat(EntityPlayer player, final IChatComponent s) {
    if (XUHelper.isPlayerFake(player))
      return; 
    if (player.worldObj.isRemote) {
      ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            public void exectuteClientCode() {
              PacketTempChat.addChat(s);
            }
          });
    } else {
      NetworkHandler.sendPacketToPlayer(new PacketTempChat(s), player);
    } 
  }
  
  public static void sendChat(EntityPlayer player, String s) {
    sendChat(player, (IChatComponent)new ChatComponentText(s));
  }
  
  public PacketTempChat() {}
  
  public PacketTempChat(String s) {
    this((IChatComponent)new ChatComponentText(s));
  }
  
  public PacketTempChat(IChatComponent chatComponent) {
    this.chatComponent = chatComponent;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    writeChatComponent(data, this.chatComponent);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.chatComponent = readChatComponent(data);
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    addChat(this.chatComponent);
  }
  
  @SideOnly(Side.CLIENT)
  private static void addChat(IChatComponent chat) {
    (Minecraft.getMinecraft()).ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chat, 7706071);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketTempChat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */