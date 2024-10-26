package com.rwtema.extrautils.tileentity.transfernodes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.Arrays;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class InvHelper {
  public static int[] getSlots(IInventory inv, int side) {
    if (inv instanceof ISidedInventory)
      return ((ISidedInventory)inv).getAccessibleSlotsFromSide(side); 
    if (inv != null) {
      int size = inv.getSizeInventory();
      int[] arr = new int[size];
      for (int i = 0; i < size; i++)
        arr[i] = i; 
      return arr;
    } 
    return new int[0];
  }
  
  public static boolean canStack(ItemStack a, ItemStack b) {
    if (a == null || b == null)
      return false; 
    return (a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && ItemStack.areItemStackTagsEqual(a, b));
  }
  
  public static boolean sameType(ItemStack a, ItemStack b) {
    if (a == null || b == null)
      return false; 
    if (canStack(a, b))
      return true; 
    int[] t = OreDictionary.getOreIDs(a);
    return (t.length > 0 && Arrays.equals(t, OreDictionary.getOreIDs(b)));
  }
  
  public static boolean sameMod(ItemStack a, ItemStack b) {
    return (a != null && b != null && (canStack(a, b) || a.getItem() == b.getItem() || getModID(a).equals(getModID(b))));
  }
  
  public static String getModID(ItemStack item) {
    ModContainer ID = getModForItemStack(item);
    if (ID == null || ID.getModId() == null)
      return "Unknown"; 
    return ID.getModId();
  }
  
  public static ModContainer getModForItemStack(ItemStack stack) {
    Item item = stack.getItem();
    Class<?> klazz = null;
    if (item == null)
      return null; 
    GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(item);
    klazz = item.getClass();
    if (identifier == null && 
      item instanceof ItemBlock) {
      Block block = ((ItemBlock)item).field_150939_a;
      identifier = GameRegistry.findUniqueIdentifierFor(block);
      klazz = block.getClass();
    } 
    Map<String, ModContainer> modList = Loader.instance().getIndexedModList();
    if (identifier != null) {
      ModContainer container = modList.get(identifier.modId);
      if (container != null)
        return container; 
    } 
    String[] itemClassParts = klazz.getName().split("\\.");
    ModContainer closestMatch = null;
    int mostMatchingPackages = 0;
    for (Map.Entry<String, ModContainer> entry : modList.entrySet()) {
      Object mod = ((ModContainer)entry.getValue()).getMod();
      if (mod == null)
        continue; 
      String[] modClassParts = mod.getClass().getName().split("\\.");
      int packageMatches = 0;
      for (int i = 0; i < modClassParts.length && 
        i < itemClassParts.length && itemClassParts[i] != null && itemClassParts[i].equals(modClassParts[i]); i++)
        packageMatches++; 
      if (packageMatches > mostMatchingPackages) {
        mostMatchingPackages = packageMatches;
        closestMatch = entry.getValue();
      } 
    } 
    return closestMatch;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\InvHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */