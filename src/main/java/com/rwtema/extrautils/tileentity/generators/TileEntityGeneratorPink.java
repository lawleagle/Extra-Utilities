package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class TileEntityGeneratorPink extends TileEntityGeneratorFurnace {
  public static HashSet<ItemStack> pink;
  
  public static void genPink() {
    String[] dyes = { 
        "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyeLime", 
        "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };
    HashSet<Integer> dyeids = new HashSet<Integer>();
    for (String dye : dyes)
      dyeids.add(Integer.valueOf(OreDictionary.getOreID(dye))); 
    int pinkid = OreDictionary.getOreID("dyePink");
    pink = new HashSet<ItemStack>();
    pink.add(new ItemStack(Items.dye, 1, 9));
    pink.add(new ItemStack(Blocks.wool, 1, 6));
    for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
      if (((IRecipe)recipe).getRecipeOutput() == null)
        continue; 
      boolean flag = false;
      for (int oreid : OreDictionary.getOreIDs(((IRecipe)recipe).getRecipeOutput())) {
        if (!dyeids.contains(Integer.valueOf(oreid)))
          if (pinkid == oreid) {
            flag = true;
            break;
          }  
      } 
      if (!flag) {
        if (isPinkItem(((IRecipe)recipe).getRecipeOutput()))
          continue; 
        if ((recipe instanceof ShapelessOreRecipe) ? 
          !isPinkRecipe(((ShapelessOreRecipe)recipe).getInput()) : (
          
          (recipe instanceof ShapedOreRecipe) ? 
          !isPinkRecipe(((ShapedOreRecipe)recipe).getInput()) : (
          
          (recipe instanceof ShapelessRecipes) ? 
          !isPinkRecipe(((ShapelessRecipes)recipe).recipeItems) : (
          
          !(recipe instanceof ShapedRecipes) || 
          !isPinkRecipe((Object[])((ShapedRecipes)recipe).recipeItems)))))
          continue; 
      } 
      pink.add(((IRecipe)recipe).getRecipeOutput().copy());
    } 
  }
  
  public static boolean isPinkRecipe(List recipe) {
    for (Object item : recipe) {
      if (isPinkItem(item))
        return true; 
    } 
    return false;
  }
  
  public static boolean isPinkRecipe(Object[] recipe) {
    for (Object item : recipe) {
      if (isPinkItem(item))
        return true; 
    } 
    return false;
  }
  
  public static boolean isPinkItem(Object item) {
    if (item instanceof ItemStack) {
      ItemStack p = (ItemStack)item;
      if (p.getItem() == Items.dye && p.getItemDamage() == 9)
        return true; 
      if (p.getItem() == Item.getItemFromBlock(Blocks.wool) && p.getItemDamage() == 6)
        return true; 
    } else if (item instanceof ItemStack[]) {
      for (ItemStack i : (ItemStack[])item) {
        if (isPinkItem(i))
          return true; 
      } 
    } else if (item instanceof List) {
      for (Object i : item) {
        if (isPinkItem(i))
          return true; 
      } 
    } 
    return false;
  }
  
  public int transferLimit() {
    return 160;
  }
  
  public Random rand = (Random)XURandom.getInstance();
  
  @SideOnly(Side.CLIENT)
  public void doRandomDisplayTickR(Random random) {
    if (this.rand.nextInt(2) != 0)
      return; 
    if (!shouldSoundPlay())
      return; 
    double d0 = this.rand.nextGaussian() * 0.5D;
    double d1 = this.rand.nextGaussian() * 0.5D;
    double d2 = this.rand.nextGaussian() * 0.5D;
    this.worldObj.spawnParticle("heart", x() + 0.2D + 0.6D * this.rand.nextFloat(), y() + 0.95D, z() + 0.2D + 0.6D * this.rand.nextFloat(), d0, d1, d2);
    super.doRandomDisplayTickR(random);
  }
  
  public void doSpecial() {
    if (this.coolDown > 0.0D && this.rand.nextInt(40) == 0)
      for (Object entity : this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(x(), y(), z(), (x() + 1), (y() + 1), (z() + 1)).expand(10.0D, 10.0D, 10.0D))) {
        EntityAnimal animal = (EntityAnimal)entity;
        if (animal.getGrowingAge() < 0) {
          animal.addGrowth(this.rand.nextInt(40));
          continue;
        } 
        if (animal.getGrowingAge() > 0) {
          int j = animal.getGrowingAge();
          j -= this.rand.nextInt(40);
          if (j < 0)
            j = 0; 
          animal.setGrowingAge(j);
          continue;
        } 
        if (!animal.isInLove() && this.rand.nextInt(40) == 0) {
          if (animal.worldObj.getEntitiesWithinAABB(entity.getClass(), animal.boundingBox.expand(8.0D, 8.0D, 8.0D)).size() > 32)
            return; 
          animal.func_146082_f(null);
        } 
      }  
  }
  
  public boolean isPink(ItemStack item) {
    if (item == null)
      return false; 
    if (item.getUnlocalizedName() != null && item.getUnlocalizedName().contains("pink"))
      return true; 
    if (pink == null) {
      long t = System.nanoTime();
      genPink();
      LogHelper.info("Pink recipes gened in " + ((System.nanoTime() - t) / 1000000.0D), new Object[0]);
    } 
    for (ItemStack pinkItem : pink) {
      if (XUHelper.canItemsStack(item, pinkItem, false, true))
        return true; 
    } 
    return false;
  }
  
  public double genLevel() {
    return 1.0D;
  }
  
  public double getFuelBurn(ItemStack item) {
    if (isPink(item))
      return 600.0D; 
    return 0.0D;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorPink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */