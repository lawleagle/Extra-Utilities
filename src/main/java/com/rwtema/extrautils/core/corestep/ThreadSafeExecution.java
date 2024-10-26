package com.rwtema.extrautils.core.corestep;

import com.rwtema.extrautils.ExtraUtilsMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;

public class ThreadSafeExecution {
  static {
    FMLCommonHandler.instance().bus().register(new ThreadSafeExecution());
  }
  
  public static final ArrayList<IDelayCallable> clientCallable = new ArrayList<IDelayCallable>();
  
  public static final ArrayList<IDelayCallable> serverCallable = new ArrayList<IDelayCallable>();
  
  public static void assignCode(Side side, IDelayCallable delayCallable) {
    if (side.isClient()) {
      if (ExtraUtilsMod.proxy.isClientSideAvailable())
        synchronized (clientCallable) {
          clientCallable.add(delayCallable);
        }  
    } else {
      synchronized (serverCallable) {
        serverCallable.add(delayCallable);
      } 
    } 
  }
  
  @SubscribeEvent
  public void server(TickEvent.ServerTickEvent server) {
    synchronized (serverCallable) {
      for (IDelayCallable iDelayCallable : serverCallable) {
        try {
          iDelayCallable.call();
        } catch (Exception e) {
          (new RuntimeException("Network code failed on Server: " + e.toString(), e)).printStackTrace();
        } 
      } 
      serverCallable.clear();
    } 
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void client(TickEvent.ClientTickEvent server) {
    synchronized (clientCallable) {
      for (IDelayCallable iDelayCallable : clientCallable) {
        try {
          iDelayCallable.call();
        } catch (Exception e) {
          (new RuntimeException("Network code failed on Client: " + e.toString(), e)).printStackTrace();
        } 
      } 
      clientCallable.clear();
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\corestep\ThreadSafeExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */