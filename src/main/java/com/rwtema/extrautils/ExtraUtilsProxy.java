package com.rwtema.extrautils;

import com.rwtema.extrautils.item.ItemAngelRing;
import com.rwtema.extrautils.network.PacketHandler;
import com.rwtema.extrautils.network.packets.PacketUseItemAlt;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ExtraUtilsProxy {
  public static ExtraUtilsProxy INSTANCE;
  
  public ExtraUtilsProxy() {
    INSTANCE = this;
  }
  
  public static int colorBlockID = 0;
  
  public static int fullBrightBlockID = 0;
  
  public static int multiBlockID = 0;
  
  public static int spikeBlockID = 0;
  
  public static int drumRendererID = 0;
  
  public static int connectedTextureID = 0;
  
  public static int connectedTextureEtheralID = 0;
  
  public static Block FMPBlockId = null;
  
  public static boolean checked = false;
  
  public static Item MicroBlockId = null;
  
  public static boolean checked2 = false;
  
  public void postInit() {}
  
  public void registerEventHandler() {
    MinecraftForge.EVENT_BUS.register(new EventHandlerServer());
    MinecraftForge.EVENT_BUS.register(new EventHandlerSiege());
    MinecraftForge.EVENT_BUS.register(new EventHandlerEntityItemStealer());
    MinecraftForge.EVENT_BUS.register(new EventHandlerChunkLoad());
  }
  
  public void registerRenderInformation() {}
  
  public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
    if (handler instanceof NetHandlerPlayServer)
      return (EntityPlayer)((NetHandlerPlayServer)handler).playerEntity; 
    return null;
  }
  
  public void throwLoadingError(String cause, String... message) {
    String concat = cause + ": ";
    for (String m : message)
      concat = concat + " - " + m; 
    throw new RuntimeException(concat);
  }
  
  public EntityPlayer getClientPlayer() {
    throw new RuntimeException("getClientPlayer called on server");
  }
  
  public World getClientWorld() {
    throw new RuntimeException("getClientWorld called on server");
  }
  
  public boolean isClientSideAvailable() {
    return false;
  }
  
  public void newServerStart() {
    if (ExtraUtils.angelRingEnabled)
      ItemAngelRing.curFlyingPlayers.clear(); 
  }
  
  public void registerClientCommands() {}
  
  public PacketHandler getNewPacketHandler() {
    return new PacketHandler();
  }
  
  public void exectuteClientCode(IClientCode clientCode) {}
  
  public void sendUsePacket(PlayerInteractEvent event) {}
  
  public void sendUsePacket(int x, int y, int z, int face, ItemStack item, float f, float f1, float f2) {}
  
  public void sendAltUsePacket(int x, int y, int z, int face, ItemStack item, float f, float f1, float f2) {}
  
  public void sendAltUsePacket(ItemStack item) {}
  
  public boolean isAltSneaking() {
    return (PacketUseItemAlt.altPlace.get() == Boolean.TRUE);
  }
  
  public <F, T> T apply(ISidedFunction<F, T> func, F input) {
    return func.applyServer(input);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ExtraUtilsProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */