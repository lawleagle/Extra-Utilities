package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class PacketTempChatMultiline extends XUPacketBase {
  IChatComponent[] chat;
  
  static final int START_ID = 983423323;
  
  static int lastNum = 0;
  
  public PacketTempChatMultiline() {}
  
  public PacketTempChatMultiline(List<String> chat) {
    this.chat = new IChatComponent[chat.size()];
    for (int i = 0; i < chat.size(); i++)
      this.chat[i] = (IChatComponent)new ChatComponentText(chat.get(i)); 
  }
  
  public PacketTempChatMultiline(IChatComponent[] chat) {
    this.chat = chat;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeShort(this.chat.length);
    for (IChatComponent iChatComponent : this.chat)
      writeChatComponent(data, iChatComponent); 
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.chat = new IChatComponent[data.readUnsignedShort()];
    for (int i = 0; i < this.chat.length; i++)
      this.chat[i] = readChatComponent(data); 
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    addChat(this.chat);
  }
  
  private static synchronized void addChat(IChatComponent[] chat) {
    GuiNewChat chatGUI = (Minecraft.getMinecraft()).ingameGUI.getChatGUI();
    int i;
    for (i = 0; i < chat.length; i++) {
      IChatComponent iChatComponent = chat[i];
      chatGUI.printChatMessageWithOptionalDeletion(iChatComponent, 983423323 + i);
    } 
    for (i = chat.length; i < lastNum; i++)
      chatGUI.deleteChatLine(983423323 + i); 
    lastNum = Math.max(lastNum, chat.length);
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
  
  static ThreadLocal<List<IChatComponent>> chatComponents = new ThreadLocal<List<IChatComponent>>() {
      protected List<IChatComponent> initialValue() {
        return new ArrayList<IChatComponent>();
      }
    };
  
  public static void addChatComponentMessage(IChatComponent chatComponentText) {
    ((List<IChatComponent>)chatComponents.get()).add(chatComponentText);
  }
  
  public static void sendCached(EntityPlayer player) {
    List<IChatComponent> componentList = chatComponents.get();
    if (componentList.isEmpty())
      return; 
    if (!XUHelper.isPlayerFake(player)) {
      IChatComponent[] iChatComponents = componentList.<IChatComponent>toArray(new IChatComponent[componentList.size()]);
      if (player.worldObj.isRemote) {
        addChat(iChatComponents);
      } else {
        NetworkHandler.sendPacketToPlayer(new PacketTempChatMultiline(iChatComponents), player);
      } 
    } 
    componentList.clear();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketTempChatMultiline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */