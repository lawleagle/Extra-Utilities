package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.modintegration.EE3Integration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeEnchantCrafting extends ShapedOreRecipe implements IRecipe {
  private static final int MAX_CRAFT_GRID_WIDTH = 3;
  
  private static final int MAX_CRAFT_GRID_HEIGHT = 3;
  
  private ItemStack output = null;
  
  private Object[] input = null;
  
  private int width = 0;
  
  private int height = 0;
  
  private boolean mirrored = true;
  
  public RecipeEnchantCrafting(Block result, Object... recipe) {
    this(new ItemStack(result), recipe);
  }
  
  public RecipeEnchantCrafting(Item result, Object... recipe) {
    this(new ItemStack(result), recipe);
  }
  
  public RecipeEnchantCrafting(ItemStack result, Object... recipe) {
    super(result, recipe);
    this.output = result.copy();
    String shape = "";
    int idx = 0;
    if (recipe[idx] instanceof Boolean) {
      this.mirrored = ((Boolean)recipe[idx]).booleanValue();
      if (recipe[idx + 1] instanceof Object[]) {
        recipe = (Object[])recipe[idx + 1];
      } else {
        idx = 1;
      } 
    } 
    if (recipe[idx] instanceof String[]) {
      String[] parts = (String[])recipe[idx++];
      for (String s : parts) {
        this.width = s.length();
        shape = shape + s;
      } 
      this.height = parts.length;
    } else {
      while (recipe[idx] instanceof String) {
        String s = (String)recipe[idx++];
        shape = shape + s;
        this.width = s.length();
        this.height++;
      } 
    } 
    if (this.width * this.height != shape.length()) {
      String ret = "Invalid shaped ore recipe: ";
      for (Object tmp : recipe)
        ret = ret + tmp + ", "; 
      ret = ret + this.output;
      throw new RuntimeException(ret);
    } 
    HashMap<Character, Object> itemMap = new HashMap<Character, Object>();
    for (; idx < recipe.length; idx += 2) {
      Character chr = (Character)recipe[idx];
      Object in = recipe[idx + 1];
      if (in instanceof ItemStack) {
        itemMap.put(chr, ((ItemStack)in).copy());
      } else if (in instanceof Item) {
        itemMap.put(chr, new ItemStack((Item)in));
      } else if (in instanceof Block) {
        itemMap.put(chr, new ItemStack((Block)in, 1, 32767));
      } else if (in instanceof String) {
        itemMap.put(chr, OreDictionary.getOres((String)in));
      } else {
        String ret = "Invalid shaped ore recipe: ";
        for (Object tmp : recipe)
          ret = ret + tmp + ", "; 
        ret = ret + this.output;
        throw new RuntimeException(ret);
      } 
    } 
    this.input = new Object[this.width * this.height];
    int x = 0;
    for (char chr : shape.toCharArray())
      this.input[x++] = itemMap.get(Character.valueOf(chr)); 
    Object[] copyInput = new Object[this.input.length];
    for (int i = 0; i < this.input.length; i++) {
      copyInput[i] = this.input[i];
      if (this.input[i] instanceof ItemStack) {
        ItemStack itemStack = (ItemStack)this.input[i];
        if (itemStack.isItemEnchanted()) {
          itemStack = itemStack.copy();
          itemStack.setTagCompound(null);
          copyInput[i] = itemStack;
        } 
      } 
    } 
    EE3Integration.addRecipe(this.output, copyInput);
  }
  
  public ItemStack getCraftingResult(InventoryCrafting var1) {
    return this.output.copy();
  }
  
  public int getRecipeSize() {
    return this.input.length;
  }
  
  public ItemStack getRecipeOutput() {
    return this.output;
  }
  
  public boolean matches(InventoryCrafting inv, World world) {
    for (int x = 0; x <= 3 - this.width; x++) {
      for (int y = 0; y <= 3 - this.height; y++) {
        if (checkMatch(inv, x, y, false))
          return true; 
        if (this.mirrored && checkMatch(inv, x, y, true))
          return true; 
      } 
    } 
    return false;
  }
  
  private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        int subX = x - startX;
        int subY = y - startY;
        Object target = null;
        if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height)
          if (mirror) {
            target = this.input[this.width - subX - 1 + subY * this.width];
          } else {
            target = this.input[subX + subY * this.width];
          }  
        ItemStack slot = inv.getStackInRowAndColumn(x, y);
        if (target instanceof ItemStack) {
          ItemStack stack = (ItemStack)target;
          if (!OreDictionary.itemMatches(stack, slot, false))
            return false; 
          Map<Integer, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
          if (!enchants.isEmpty()) {
            Map<Integer, Integer> other = EnchantmentHelper.getEnchantments(slot);
            for (Map.Entry<Integer, Integer> entry : enchants.entrySet()) {
              Integer t = other.get(entry.getKey());
              if (t == null || t.intValue() < ((Integer)entry.getValue()).intValue())
                return false; 
            } 
          } 
        } else if (target instanceof ArrayList) {
          boolean matched = false;
          Iterator<ItemStack> itr = ((ArrayList<ItemStack>)target).iterator();
          while (itr.hasNext() && !matched)
            matched = OreDictionary.itemMatches(itr.next(), slot, false); 
          if (!matched)
            return false; 
        } else if (target == null && slot != null) {
          return false;
        } 
      } 
    } 
    return true;
  }
  
  public RecipeEnchantCrafting setMirrored(boolean mirror) {
    this.mirrored = mirror;
    return this;
  }
  
  public Object[] getInput() {
    return this.input;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeEnchantCrafting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */