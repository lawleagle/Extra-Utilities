package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.helper.XUHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;

public class TileEntityGeneratorPotion extends TileEntityGeneratorFurnace {
  public static final int MAX_META_TO_CHECK = 256;
  
  public static Map<Integer, Integer> powerMap = new HashMap<Integer, Integer>();
  
  int curLevel = 0;
  
  public static void genPotionLevels() {
    XUHelper.resetTimer();
    HashSet<Item> ingredientIDs = new HashSet<Item>();
    List<Integer> potionIDs = new ArrayList<Integer>();
    for (Object anItemRegistry : Item.itemRegistry) {
      Item item = (Item)anItemRegistry;
      int num = getMaxMeta(item);
      for (int meta = 0; meta < num; meta++) {
        if (item.isPotionIngredient(new ItemStack(item, 1, meta)))
          ingredientIDs.add(item); 
      } 
    } 
    powerMap.put(Integer.valueOf(0), Integer.valueOf(0));
    potionIDs.add(Integer.valueOf(0));
    for (int i = 0; i < potionIDs.size(); i++) {
      int potion = ((Integer)potionIDs.get(i)).intValue();
      label41: for (Item ingredient : ingredientIDs) {
        String k = "";
        String s = "";
        int num = getMaxMeta(ingredient);
        int meta = 0;
        while (true) {
          if (meta < num || !k.equals(s)) {
            if (ingredient.isPotionIngredient(new ItemStack(ingredient, 1, meta)))
              try {
                s = ingredient.getPotionEffect(new ItemStack(ingredient, 1, meta));
                int c = PotionHelper.applyIngredient(potion, s);
                if (!potionIDs.contains(Integer.valueOf(c))) {
                  potionIDs.add(Integer.valueOf(c));
                  powerMap.put(Integer.valueOf(c), Integer.valueOf(((Integer)powerMap.get(Integer.valueOf(potion))).intValue() + 1));
                } 
                k = (s == null) ? "" : s;
              } catch (Exception err) {
                throw new RuntimeException("Caught error while applying potion ingredient " + ingredient.toString() + " to " + potion, err);
              }  
            meta++;
            continue;
          } 
          continue label41;
        } 
      } 
    } 
    XUHelper.printTimer("Potion generation");
  }
  
  public static int getMaxMeta(Item i) {
    return isSpecial(i) ? 256 : 1;
  }
  
  public static boolean isSpecial(Item i) {
    return (i.getClass() != Item.class && i.getClass() != ItemBlock.class && i.getHasSubtypes());
  }
  
  public int transferLimit() {
    return Math.max(400, (int)genLevel());
  }
  
  public double genLevel() {
    return (20 * (1 << this.curLevel));
  }
  
  public int getPotionLevel(ItemStack item) {
    if (item != null && item.getItem() == Items.potionitem && powerMap.containsKey(Integer.valueOf(item.getItemDamage())))
      return ((Integer)powerMap.get(Integer.valueOf(item.getItemDamage()))).intValue(); 
    return 0;
  }
  
  public double getFuelBurn(ItemStack item) {
    if (getPotionLevel(item) == 0)
      return 0.0D; 
    return 800.0D;
  }
  
  public double getGenLevelForStack(ItemStack itemStack) {
    int c = getPotionLevel(itemStack);
    return (10 * (1 << c));
  }
  
  public boolean processInput() {
    if (this.coolDown > 0.0D)
      return false; 
    int c = getPotionLevel(this.inv.getStackInSlot(0));
    if (c > 0) {
      addCoolDown(getFuelBurn(this.inv.getStackInSlot(0)), false);
      this.curLevel = c;
      this.inv.getStackInSlot(0).setItemDamage(0);
      markDirty();
      return true;
    } 
    return false;
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.curLevel = nbt.getInteger("curLevel");
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setInteger("curLevel", this.curLevel);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorPotion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */