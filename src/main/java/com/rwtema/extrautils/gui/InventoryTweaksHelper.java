package com.rwtema.extrautils.gui;

import invtweaks.api.container.ContainerSection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class InventoryTweaksHelper {
  public static Map<ContainerSection, List<Slot>> getSlots(Container inventory) {
    return getSlots(inventory, false);
  }
  
  public static Map<ContainerSection, List<Slot>> getSlots(Container inventory, boolean playerInvOnly) {
    Map<ContainerSection, List<Slot>> map = new HashMap<ContainerSection, List<Slot>>();
    for (Slot s : inventory.inventorySlots) {
      ContainerSection c = null;
      if (s.inventory instanceof net.minecraft.entity.player.InventoryPlayer) {
        putSlot(map, s, ContainerSection.INVENTORY);
        if (s.slotNumber < 9) {
          putSlot(map, s, ContainerSection.INVENTORY_HOTBAR);
          continue;
        } 
        if (s.slotNumber < 36) {
          putSlot(map, s, ContainerSection.INVENTORY_NOT_HOTBAR);
          continue;
        } 
        putSlot(map, s, ContainerSection.ARMOR);
        continue;
      } 
      if (!playerInvOnly)
        putSlot(map, s, ContainerSection.CHEST); 
    } 
    return map;
  }
  
  private static void putSlot(Map<ContainerSection, List<Slot>> map, Slot s, ContainerSection c) {
    List<Slot> list = map.get(c);
    if (list == null)
      list = new ArrayList<Slot>(); 
    list.add(s);
    map.put(c, list);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\InventoryTweaksHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */