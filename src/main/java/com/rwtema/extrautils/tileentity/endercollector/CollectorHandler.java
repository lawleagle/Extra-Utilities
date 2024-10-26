package com.rwtema.extrautils.tileentity.endercollector;

import com.rwtema.extrautils.EventHandlerEntityItemStealer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.WeakHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class CollectorHandler {
  public static final WeakHashMap<World, WeakHashMap<TileEnderCollector, Object>> map = new WeakHashMap<World, WeakHashMap<TileEnderCollector, Object>>();
  
  public static CollectorHandler INSTANCE = new CollectorHandler();
  
  public static boolean dontCollect;
  
  static {
    MinecraftForge.EVENT_BUS.register(INSTANCE);
  }
  
  private static final ArrayList<TileEnderCollector> collectors = new ArrayList<TileEnderCollector>(1);
  
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    if (event.entity.worldObj.isRemote)
      return; 
    if (dontCollect)
      return; 
    if (CollectorHandler.map.isEmpty())
      return; 
    WeakHashMap<TileEnderCollector, Object> map = CollectorHandler.map.get(event.entity.worldObj);
    if (map == null || map.isEmpty())
      return; 
    Entity entity = event.entity;
    if (!EventHandlerEntityItemStealer.isEntityItem(entity.getClass()))
      return; 
    ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
    if (stack == null)
      return; 
    collectors.clear();
    for (TileEnderCollector tileEnderCollector : map.keySet()) {
      if (tileEnderCollector.inRange(entity))
        collectors.add(tileEnderCollector); 
    } 
    if (collectors.isEmpty())
      return; 
    if (collectors.size() == 1) {
      ((TileEnderCollector)collectors.get(0)).grabEntity((EntityItem)entity);
    } else {
      Collections.shuffle(collectors);
      EntityItem entityItem = (EntityItem)entity;
      for (TileEnderCollector collector : collectors) {
        collector.grabEntity(entityItem);
        if (entity.isDead)
          break; 
      } 
    } 
    collectors.clear();
    if (entity.isDead)
      event.setCanceled(true); 
  }
  
  public static void register(TileEnderCollector tile) {
    World worldObj = tile.getWorldObj();
    if (worldObj == null || worldObj.isRemote)
      return; 
    getWorldMap(tile).put(tile, null);
  }
  
  public static void unregister(TileEnderCollector tile) {
    World worldObj = tile.getWorldObj();
    if (worldObj == null || worldObj.isRemote)
      return; 
    getWorldMap(tile).remove(tile);
  }
  
  public static WeakHashMap<TileEnderCollector, Object> getWorldMap(TileEnderCollector tile) {
    WeakHashMap<TileEnderCollector, Object> worldMap = map.get(tile.getWorldObj());
    if (worldMap == null) {
      worldMap = new WeakHashMap<TileEnderCollector, Object>();
      map.put(tile.getWorldObj(), worldMap);
    } 
    return worldMap;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\endercollector\CollectorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */