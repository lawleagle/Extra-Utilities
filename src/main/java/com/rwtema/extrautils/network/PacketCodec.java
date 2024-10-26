package com.rwtema.extrautils.network;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

@Sharable
public class PacketCodec extends FMLIndexedMessageToMessageCodec<XUPacketBase> {
  public static HashMap<String, Class<? extends XUPacketBase>> classes = new HashMap<String, Class<? extends XUPacketBase>>();
  
  public PacketCodec() {
    ArrayList<String> t = new ArrayList<String>();
    t.addAll(classes.keySet());
    Collections.sort(t);
    for (int i = 0; i < t.size(); i++) {
      LogHelper.fine("Registering Packet class " + (String)t.get(i) + " with discriminator: " + i, new Object[0]);
      addDiscriminator(i, classes.get(t.get(i)));
    } 
  }
  
  public static void addClass(Class<? extends XUPacketBase> clazz) {
    classes.put(clazz.getSimpleName(), clazz);
  }
  
  public void encodeInto(ChannelHandlerContext ctx, XUPacketBase msg, ByteBuf target) throws Exception {
    INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
    EntityPlayer player = ExtraUtilsMod.proxy.getPlayerFromNetHandler(netHandler);
    msg.writeData(target);
  }
  
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, XUPacketBase msg) {
    INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
    EntityPlayer player = ExtraUtilsMod.proxy.getPlayerFromNetHandler(netHandler);
    msg.readData(player, source);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\PacketCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */