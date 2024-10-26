package com.rwtema.extrautils.helper;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.BlockColor;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftHelper {
  public static final int[] pi = new int[] { 
      3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 
      5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 
      6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 
      9, 5 };
  
  public static void registerItems() {
    if (Loader.isModLoaded("Thaumcraft"))
      registerItems_do(); 
  }
  
  private static void registerItems_do() {
    addAspectsDivSigil();
    addAspectRecipe(ExtraUtils.unstableIngot, new Object[] { Aspect.METAL, Integer.valueOf(4), Aspect.ELDRITCH, Integer.valueOf(4), Aspect.ENERGY, Integer.valueOf(16) });
    addAspectRecipe(ExtraUtils.cursedEarth, new Object[] { Aspect.EARTH, Integer.valueOf(1), Aspect.DARKNESS, Integer.valueOf(1), Aspect.UNDEAD, Integer.valueOf(4), Aspect.ELDRITCH, Integer.valueOf(1), Aspect.EXCHANGE, Integer.valueOf(1) });
    addAspectRecipe((Block)ExtraUtils.enderLily, new Object[] { Aspect.DARKNESS, Integer.valueOf(1), Items.wheat_seeds, Items.ender_pearl, Aspect.ELDRITCH, Integer.valueOf(16) });
    addAspectRecipe((Block)ExtraUtils.transferPipe, new Object[] { { 0, 1, 2, 3, 4, 5, 6, 7 }, Aspect.TRAVEL, Integer.valueOf(1), Aspect.ORDER, Integer.valueOf(1), Aspect.EARTH, Integer.valueOf(1) });
    addAspectRecipe(ExtraUtils.buildersWand, new Object[] { Aspect.TOOL, Integer.valueOf(4), Blocks.obsidian, Aspect.ELDRITCH, Integer.valueOf(4) });
    addAspectRecipe(ExtraUtils.buildersWand, new Object[] { Integer.valueOf(32767), Aspect.TOOL, Integer.valueOf(4), Blocks.obsidian, Aspect.ELDRITCH, Integer.valueOf(4), Items.nether_star });
    addAspectRecipe(ExtraUtils.trashCan, new Object[] { Aspect.VOID, Integer.valueOf(8), Blocks.cobblestone, Aspect.ENTROPY, Integer.valueOf(4) });
    if (ExtraUtils.spike != null)
      addAspectRecipe(ExtraUtils.spike.itemSpike, new Object[] { Integer.valueOf(-1), null, Aspect.WEAPON, Integer.valueOf(18), Aspect.METAL, Integer.valueOf(14) }); 
    if (ExtraUtils.spikeDiamond != null)
      addAspectRecipe(ExtraUtils.spikeDiamond.itemSpike, new Object[] { Integer.valueOf(-1), null, Aspect.WEAPON, Integer.valueOf(18), Aspect.METAL, Integer.valueOf(14) }); 
    if (ExtraUtils.spikeGold != null)
      addAspectRecipe(ExtraUtils.spikeGold.itemSpike, new Object[] { Integer.valueOf(-1), null, Aspect.WEAPON, Integer.valueOf(18), Aspect.METAL, Integer.valueOf(14) }); 
    if (ExtraUtils.spikeWood != null)
      addAspectRecipe(ExtraUtils.spikeWood.itemSpike, new Object[] { Integer.valueOf(-1), null, Aspect.WEAPON, Integer.valueOf(18), Aspect.METAL, Integer.valueOf(14) }); 
    addAspectRecipe((Item)ExtraUtils.wateringCan, new Object[] { Integer.valueOf(-1), new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), Aspect.WATER, Integer.valueOf(1), Aspect.LIFE, Integer.valueOf(1), Aspect.EARTH, Integer.valueOf(2) });
    addAspectRecipe(ExtraUtils.conveyor, new Object[] { Integer.valueOf(-1), null, Blocks.rail, Aspect.TRAVEL, Integer.valueOf(4) });
    if (ExtraUtils.decorative1 != null) {
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 4), new Object[] { Blocks.stonebrick });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 7), new Object[] { Blocks.stonebrick });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 0), new Object[] { Blocks.stonebrick });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 1), new Object[] { Blocks.obsidian, Items.ender_pearl });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 2), new Object[] { Blocks.quartz_block, Aspect.FIRE, Integer.valueOf(4) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 3), new Object[] { Blocks.stone, Blocks.ice });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 6), new Object[] { Blocks.gravel });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 10), new Object[] { Blocks.gravel, Blocks.gravel, Aspect.TRAVEL, Integer.valueOf(1) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 9), new Object[] { Blocks.sand, Blocks.glass });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 13), new Object[] { Blocks.sand, Blocks.end_stone });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 14), new Object[] { Blocks.stonebrick, Aspect.SENSES, Integer.valueOf(2), Aspect.ELDRITCH, Integer.valueOf(2) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 8), new Object[] { 
            null, Aspect.MAGIC, Integer.valueOf(16), Aspect.METAL, Integer.valueOf(4), Aspect.GREED, Integer.valueOf(4), Aspect.MIND, Integer.valueOf(8), Aspect.TREE, 
            Integer.valueOf(8) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 11), new Object[] { null, Aspect.ELDRITCH, Integer.valueOf(16) });
    } 
    if (ExtraUtils.decorative2 != null) {
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 10), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2), Aspect.DARKNESS, Integer.valueOf(4) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 0), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 3), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2), Aspect.ENTROPY, Integer.valueOf(1) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 3), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2), Aspect.ENTROPY, Integer.valueOf(1) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 4), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2), Aspect.GREED, Integer.valueOf(1) });
      addAspectRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 8), new Object[] { Aspect.CRYSTAL, Integer.valueOf(2), Aspect.LIFE, Integer.valueOf(1), Aspect.HEAL, Integer.valueOf(1) });
    } 
    for (BlockColor colorblock : ExtraUtils.colorblocks) {
      AspectList aspectList = new AspectList(new ItemStack(colorblock.baseBlock, 1));
      if (aspectList.visSize() > 0)
        for (int i = 0; i < 16; i++) {
          addAspectRecipe(new ItemStack((Block)colorblock, 1, i), new Object[] { aspectList, Aspect.SENSES, Integer.valueOf(1) });
        }  
    } 
    addAspectRecipe(ExtraUtils.lawSword, new Object[] { Aspect.WEAPON, Integer.valueOf(64), Aspect.LIFE, Integer.valueOf(32), Aspect.MAGIC, Integer.valueOf(16) });
  }
  
  public static void handleQEDRecipes(ArrayList<ItemStack> items) {
    handleQEDRecipes_do(items);
  }
  
  private static void handleQEDRecipes_do(ArrayList<ItemStack> items) {
    for (ItemStack item : items)
      ThaumcraftApiHelper.getObjectAspects(item); 
  }
  
  private static void addAspectsDivSigil() {
    if (ExtraUtils.divisionSigil == null)
      return; 
    ArrayList<Aspect> a = new ArrayList<Aspect>();
    a.add(Aspect.AURA);
    a.add(Aspect.EXCHANGE);
    a.add(Aspect.TOOL);
    a.add(Aspect.CRAFT);
    a.add(Aspect.ELDRITCH);
    a.add(Aspect.SOUL);
    Collections.sort(a, new Comparator<Aspect>() {
          public int compare(Aspect o1, Aspect o2) {
            return o1.getTag().compareTo(o2.getTag());
          }
        });
    AspectList b = new AspectList();
    for (int i = 0; i < a.size(); i++)
      b.add(a.get(i), pi[i]); 
    ThaumcraftApi.registerObjectTag(new ItemStack(ExtraUtils.divisionSigil, 1, 32767), b);
  }
  
  private static void addAspectRecipe(Block block, Object... ingredients) {
    if (block != null)
      addAspectRecipe(new ItemStack(block), ingredients); 
  }
  
  private static void addAspectRecipe(Item item, Object... ingredients) {
    if (item != null)
      addAspectRecipe(new ItemStack(item), ingredients); 
  }
  
  private static void addAspectRecipe(ItemStack result, Object... ingredients) {
    if (result == null || result.getItem() == null)
      return; 
    int[] meta = null;
    AspectList al = new AspectList(result);
    for (int i = 0; i < ingredients.length; i++) {
      Object o = ingredients[i];
      if (o == null) {
        al.add(new AspectList(result));
      } else if (i == 0 && o instanceof Integer) {
        int newmeta = ((Integer)o).intValue();
        if (newmeta == -1)
          newmeta = 32767; 
        result.setItemDamage(newmeta);
      } else if (o instanceof int[]) {
        meta = (int[])o;
      } else if (o instanceof AspectList) {
        al.add((AspectList)o);
      } else if (o instanceof Aspect) {
        Aspect a = (Aspect)o;
        int a_num = 1;
        if (i + 1 < ingredients.length && ingredients[i + 1] instanceof Integer) {
          a_num = ((Integer)ingredients[i + 1]).intValue();
          i++;
        } 
        al.add(a, a_num);
      } else if (o instanceof ItemStack) {
        AspectList aspectList = new AspectList((ItemStack)o);
        al.add(aspectList);
      } else if (o instanceof Item) {
        AspectList aspectList = new AspectList(new ItemStack((Item)o));
        al.add(aspectList);
      } else if (o instanceof Block) {
        AspectList aspectList = new AspectList(new ItemStack((Block)o));
        al.add(aspectList);
      } else if (o instanceof String) {
        AspectList aspectList = new AspectList();
        for (ItemStack itemStack : OreDictionary.getOres((String)o))
          aspectList.merge(new AspectList(itemStack)); 
        al.add(aspectList);
      } 
    } 
    if (meta != null) {
      ThaumcraftApi.registerObjectTag(result, meta, al);
    } else {
      ThaumcraftApi.registerObjectTag(result, al);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\ThaumcraftHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */