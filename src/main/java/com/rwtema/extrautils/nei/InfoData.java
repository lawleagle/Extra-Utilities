package com.rwtema.extrautils.nei;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InfoData {
  public static List<InfoData> data = new ArrayList<InfoData>();
  
  public ItemStack item;
  
  public String[] info;
  
  public String name;
  
  public String url;
  
  public boolean isBlock = false;
  
  public boolean precise = false;
  
  public InfoData(ItemStack item, String[] info, String name, String url, boolean precise) {
    this.item = item;
    this.info = info;
    this.name = name;
    if (url != null && !url.endsWith(".png"))
      throw new RuntimeException(name + " is missing .png from url : " + url); 
    this.url = url;
    this.precise = precise;
    this.isBlock = item.getItem() instanceof net.minecraft.item.ItemBlock;
  }
  
  @Deprecated
  public static InfoData add(Object item, String name, String url, String... info) {
    InfoData newData = null;
    if (item instanceof ItemStack) {
      newData = new InfoData((ItemStack)item, info, name, url, true);
    } else if (item instanceof Item) {
      newData = new InfoData(new ItemStack((Item)item), info, name, url, false);
    } else if (item instanceof Block) {
      newData = new InfoData(new ItemStack((Block)item), info, name, url, false);
    } 
    data.add(newData);
    return newData;
  }
  
  public boolean matches(ItemStack item) {
    if (item == null)
      return false; 
    if (this.precise)
      return ItemStack.areItemStacksEqual(item, this.item); 
    return (item == this.item);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\InfoData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */