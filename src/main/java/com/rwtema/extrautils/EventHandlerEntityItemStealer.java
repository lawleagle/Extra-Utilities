package com.rwtema.extrautils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHandlerEntityItemStealer {
  private static LinkedList<ItemStack> items = new LinkedList<ItemStack>();

  private static LinkedList<EntityItem> entityItems = new LinkedList<EntityItem>();

  private static boolean capturing = false;

  private static boolean killExperience = false;

  public static void startCapture(boolean killExp) {
    killExperience = killExp;
    startCapture();
  }

  public static void startCapture() {
    if (capturing)
      throw new IllegalStateException("Capturing item drops twice");
    capturing = true;
  }

  public static void stopCapture() {
    capturing = false;
    killExperience = false;
  }

  public static List<EntityItem> getCapturedEntities() {
    capturing = false;
    List<EntityItem> i = new ArrayList<EntityItem>();
    i.addAll(entityItems);
    entityItems.clear();
    items.clear();
    return i;
  }

  public static List<ItemStack> getCapturedItemStacks() {
    capturing = false;
    List<ItemStack> i = new ArrayList<ItemStack>();
    i.addAll(items);
    entityItems.clear();
    items.clear();
    return i;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    if (!capturing || event.entity.worldObj.isRemote)
      return;
    Entity entity = event.entity;
    if (entity.getClass() == EntitySilverfish.class) {
      ((EntitySilverfish)entity).onDeath(DamageSource.cactus);
      entity.setDead();
      event.setCanceled(true);
      return;
    }
    if (killExperience && entity.getClass() == EntityXPOrb.class) {
      entity.setDead();
      event.setCanceled(true);
      return;
    }
    if (isEntityItem(entity.getClass())) {
      ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
      if (stack == null)
        return;
      items.add(stack);
      entityItems.add((EntityItem)entity);
      entity.setDead();
      event.setCanceled(true);
    }
  }

  public static final HashMap<Class<?>, Boolean> clazztypes = new HashMap<>();

  public static boolean isEntityItem(Class<?> clazz) {
    Boolean aBoolean = clazztypes.get(clazz);
    if (aBoolean == null) {
      aBoolean = Boolean.valueOf(EntityItem.class.isAssignableFrom(clazz));
      clazztypes.put(clazz, aBoolean);
    }
    return aBoolean.booleanValue();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\EventHandlerEntityItemStealer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
