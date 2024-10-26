package com.rwtema.extrautils.network;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.scanner.ItemScanner;
import com.rwtema.extrautils.network.packets.PacketParticle;
import com.rwtema.extrautils.network.packets.PacketParticleEvent;
import com.rwtema.extrautils.network.packets.PacketPlaySound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.EnumMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class NetworkHandler {
  public static EnumMap<Side, FMLEmbeddedChannel> channels;
  
  public static void register() {
    channels = NetworkRegistry.INSTANCE.newChannel("XU|Pkt", new ChannelHandler[] { (ChannelHandler)new PacketCodec(), (ChannelHandler)ExtraUtilsMod.proxy.getNewPacketHandler() });
  }
  
  public static void checkPacket(XUPacketBase packet, Side properSenderSide) {
    if (!packet.isValidSenderSide(properSenderSide))
      throw new RuntimeException("Sending packet class" + packet.getClass().getSimpleName() + " from wrong side"); 
  }
  
  public static void sendToAllPlayers(XUPacketBase packet) {
    checkPacket(packet, Side.SERVER);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).writeOutbound(new Object[] { packet });
  }
  
  public static void sendPacketToPlayer(XUPacketBase packet, EntityPlayer player) {
    checkPacket(packet, Side.SERVER);
    if (XUHelper.isPlayerFake(player))
      return; 
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).writeOutbound(new Object[] { packet });
  }
  
  public static void sendToAllAround(XUPacketBase packet, int dimension, double x, double y, double z, double range) {
    sendToAllAround(packet, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
  }
  
  public static void sendToAllAround(XUPacketBase packet, NetworkRegistry.TargetPoint point) {
    checkPacket(packet, Side.SERVER);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
    ((FMLEmbeddedChannel)channels.get(Side.SERVER)).writeAndFlush(packet).addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
  }
  
  public static void sendPacketToServer(XUPacketBase packet) {
    checkPacket(packet, Side.CLIENT);
    ((FMLEmbeddedChannel)channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
    ((FMLEmbeddedChannel)channels.get(Side.CLIENT)).writeOutbound(new Object[] { packet });
  }
  
  public static void sendParticle(World world, String p, double x, double y, double z, double vx, double vy, double vz, boolean scannersOnly) {
    int maxDistance = 32;
    if (scannersOnly && ExtraUtils.scanner == null)
      return; 
    PacketParticle packet = new PacketParticle(p, x, y, z, vx, vy, vz);
    if (scannersOnly && !ItemScanner.scannerOut)
      return; 
    boolean noScanners = true;
    for (int j = 0; j < world.playerEntities.size(); j++) {
      EntityPlayerMP player = world.playerEntities.get(j);
      if (!scannersOnly || (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ExtraUtils.scanner))
        if (Math.abs(player.posX - x) <= maxDistance && Math.abs(player.posY - y) <= maxDistance && Math.abs(player.posZ - z) <= maxDistance)
          sendPacketToPlayer((XUPacketBase)packet, (EntityPlayer)player);  
    } 
  }
  
  public static void sendParticleEvent(World world, int type, int x, int y, int z) {
    int maxDistance = 24;
    if (type < 0)
      return; 
    PacketParticleEvent packet = new PacketParticleEvent(x, y, z, (byte)type);
    sendToAllAround((XUPacketBase)packet, world.provider.dimensionId, x, y, z, maxDistance);
  }
  
  public static void sendSoundEvent(World world, int type, float x, float y, float z) {
    int maxDistance = 32;
    if (type < 0)
      return; 
    PacketPlaySound packet = new PacketPlaySound((short)type, x, y, z);
    sendToAllAround((XUPacketBase)packet, world.provider.dimensionId, x, y, z, maxDistance);
  }
  
  public static void sendToAllAround(XUPacketBase packet, int chunkX, int chunkZ) {
    ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(chunkX, chunkZ);
    for (EntityPlayerMP player : (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager()).playerEntityList) {
      if (player.loadedChunks.contains(chunkCoordIntPair))
        sendPacketToPlayer(packet, (EntityPlayer)player); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\NetworkHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */