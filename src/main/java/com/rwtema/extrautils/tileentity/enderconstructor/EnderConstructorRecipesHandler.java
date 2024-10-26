package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.ThaumcraftHelper;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class EnderConstructorRecipesHandler {
  public static ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();
  
  public static void addRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
    registerRecipe((IRecipe)newRecipe(par1ItemStack, par2ArrayOfObj));
  }
  
  public static void registerRecipe(IRecipe recipe) {
    recipes.add(recipe);
  }
  
  public static void postInit() {
    if (ExtraUtils.qed == null)
      return; 
    if (ExtraUtils.qedList.isEmpty())
      return; 
    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    for (Object o : CraftingManager.getInstance().getRecipeList()) {
      ItemStack item;
      IRecipe recipe = (IRecipe)o;
      if (recipe == null)
        continue; 
      try {
        item = recipe.getRecipeOutput();
      } catch (Exception e) {
        LogHelper.info("Caught error from Recipe", new Object[0]);
        e.printStackTrace();
        continue;
      } 
      if (item != null) {
        String s;
        if (item.getItem() == null) {
          (new RuntimeException("ItemStack with null item found in " + recipe.getClass().getSimpleName() + " getRecipeOutput()")).printStackTrace();
          continue;
        } 
        try {
          s = item.getUnlocalizedName();
        } catch (Exception e) {
          (new RuntimeException("Caught error from ItemStack in getRecipeOutput()", e)).printStackTrace();
          continue;
        } 
        if (ExtraUtils.qedList.contains(s)) {
          items.add(item);
          recipes.add(recipe);
        } 
      } 
    } 
    if (Loader.isModLoaded("Thaumcraft"))
      ThaumcraftHelper.handleQEDRecipes(items); 
    CraftingManager.getInstance().getRecipeList().removeAll(recipes);
    if (!ExtraUtils.disableQEDIngotSmeltRecipes)
      for (String oreName : OreDictionary.getOreNames()) {
        if (oreName.startsWith("ore"))
          for (ItemStack ore : OreDictionary.getOres(oreName)) {
            ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(ore);
            if (smeltingResult == null)
              continue; 
            for (int i : OreDictionary.getOreIDs(smeltingResult)) {
              if (OreDictionary.getOreName(i).startsWith("ingot")) {
                ItemStack result = smeltingResult.copy();
                result.stackSize *= 3;
                if (result.stackSize > result.getMaxStackSize())
                  result.stackSize = result.getMaxStackSize(); 
                recipes.add(new ShapedOreRecipe(result, new Object[] { "Oc", Character.valueOf('O'), ore, Character.valueOf('c'), Items.coal }));
                recipes.add(new ShapelessOreRecipe(result, new Object[] { ore, Items.coal }));
                break;
              } 
            } 
          }  
      }  
  }
  
  public static ItemStack findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World) {
    for (IRecipe irecipe : recipes) {
      if (irecipe.matches(par1InventoryCrafting, par2World))
        return irecipe.getCraftingResult(par1InventoryCrafting); 
    } 
    return null;
  }
  
  public static ShapedRecipes newRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
    String s = "";
    int i = 0;
    int j = 0;
    int k = 0;
    if (par2ArrayOfObj[i] instanceof String[]) {
      String[] astring = (String[])par2ArrayOfObj[i++];
      for (String s1 : astring) {
        k++;
        j = s1.length();
        s = s + s1;
      } 
    } else {
      while (par2ArrayOfObj[i] instanceof String) {
        String s2 = (String)par2ArrayOfObj[i++];
        k++;
        j = s2.length();
        s = s + s2;
      } 
    } 
    HashMap<Object, Object> hashmap;
    for (hashmap = new HashMap<Object, Object>(); i < par2ArrayOfObj.length; i += 2) {
      Character character = (Character)par2ArrayOfObj[i];
      ItemStack itemstack1 = null;
      if (par2ArrayOfObj[i + 1] instanceof Item) {
        itemstack1 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
      } else if (par2ArrayOfObj[i + 1] instanceof Block) {
        itemstack1 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
      } else if (par2ArrayOfObj[i + 1] instanceof ItemStack) {
        itemstack1 = (ItemStack)par2ArrayOfObj[i + 1];
      } 
      hashmap.put(character, itemstack1);
    } 
    ItemStack[] aitemstack = new ItemStack[j * k];
    for (int i1 = 0; i1 < j * k; i1++) {
      char c0 = s.charAt(i1);
      if (hashmap.containsKey(Character.valueOf(c0))) {
        aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
      } else {
        aitemstack[i1] = null;
      } 
    } 
    ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, par1ItemStack);
    return shapedrecipes;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\EnderConstructorRecipesHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */