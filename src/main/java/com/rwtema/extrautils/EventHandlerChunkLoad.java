package com.rwtema.extrautils;

import com.rwtema.extrautils.tileentity.enderquarry.IChunkLoad;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraftforge.event.world.ChunkEvent;

public class EventHandlerChunkLoad {
  @SubscribeEvent
  public void load(ChunkEvent.Load event) {
    Iterator iter = (event.getChunk()).chunkTileEntityMap.values().iterator();
    while (iter.hasNext()) {
      Object t = iter.next();
      if (t instanceof IChunkLoad)
        ((IChunkLoad)t).onChunkLoad(); 
    } 
  }
  
  @SubscribeEvent
  public void unload(ChunkEvent.Load event) {
    Iterator iter = (event.getChunk()).chunkTileEntityMap.values().iterator();
    while (iter.hasNext()) {
      Object t = iter.next();
      if (t instanceof IChunkLoad)
        ((IChunkLoad)t).onChunkUnload(); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\EventHandlerChunkLoad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */