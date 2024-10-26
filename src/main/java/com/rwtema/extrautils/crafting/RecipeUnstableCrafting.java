package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.item.ItemUnstableIngot;
import com.rwtema.extrautils.modintegration.EE3Integration;
import java.util.ArrayList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUnstableCrafting extends ShapedOreRecipe implements IRecipe {
  public static final NBTTagCompound nbt = new NBTTagCompound();
  
  private ItemStack stableOutput;
  
  ShapedOreRecipe checker;
  
  static {
    nbt.setBoolean("isNEI", true);
  }
  
  public RecipeUnstableCrafting(ItemStack result, Object... recipe) {
    super(result, recipe);
    this.checker = new ShapedOreRecipe(result, recipe);
    Object[] input = getInput();
    ArrayList<Object> ee3input = new ArrayList();
    for (Object anInput : input) {
      boolean flag = true;
      if (anInput instanceof ArrayList) {
        ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>)anInput;
        for (ItemStack itemStack : itemStacks) {
          if (itemStack.getItem() == ExtraUtils.unstableIngot && itemStack.getItemDamage() == 2) {
            itemStack.setTagCompound(nbt);
            ee3input.add(Items.diamond);
            ee3input.add(Items.iron_ingot);
            flag = false;
            break;
          } 
        } 
      } 
      if (flag)
        ee3input.add(anInput); 
    } 
    EE3Integration.addRecipe(getRecipeOutput().copy(), ee3input.toArray());
  }
  
  public RecipeUnstableCrafting setStableItem(Item stack) {
    if (stack != null)
      this.stableOutput = new ItemStack(stack); 
    return this;
  }
  
  public RecipeUnstableCrafting setStable(ItemStack stack) {
    this.stableOutput = stack;
    return this;
  }
  
  public RecipeUnstableCrafting addStableEnchant(Enchantment enchantment, int level) {
    if (this.stableOutput == null)
      this.stableOutput = getRecipeOutput().copy(); 
    this.stableOutput.addEnchantment(enchantment, level);
    return this;
  }
  
  public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
    return (par2World != null && this.checker.matches(par1InventoryCrafting, par2World) && !hasExpired(par1InventoryCrafting, par2World));
  }
  
  public boolean hasExpired(InventoryCrafting par1InventoryCrafting, World par2World) {
    for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); i++) {
      ItemStack item = par1InventoryCrafting.getStackInSlot(i);
      if (item != null && 
        item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0)
        if (item.hasTagCompound() && 
          !item.getTagCompound().hasKey("creative"))
          if (!item.getTagCompound().hasKey("stable")) {
            if (!item.getTagCompound().hasKey("dimension") && !item.getTagCompound().hasKey("time") && 
              item.getTagCompound().hasKey("crafting"))
              return true; 
            long t = (200L - par2World.getTotalWorldTime() - item.getTagCompound().getLong("time")) / 20L;
            if (par2World.provider.dimensionId != item.getTagCompound().getInteger("dimension") || t < 0L)
              return true; 
          }   
    } 
    return false;
  }
  
  public boolean hasStable(InventoryCrafting par1InventoryCrafting) {
    for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); i++) {
      ItemStack item = par1InventoryCrafting.getStackInSlot(i);
      if (item != null && 
        item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && 
        item.hasTagCompound()) {
        if (!ItemUnstableIngot.isStable(item))
          return false; 
        if (!ItemUnstableIngot.isSuperStable(item))
          return false; 
        if (item.getTagCompound().hasKey("time"))
          return false; 
      } 
    } 
    return true;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
    if (this.stableOutput != null && hasStable(par1InventoryCrafting))
      return this.stableOutput.copy(); 
    return super.getCraftingResult(par1InventoryCrafting);
  }
  
  public static RecipeUnstableCrafting addRecipe(ItemStack itemStack, Object... objects) {
    return new RecipeUnstableCrafting(itemStack, objects);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeUnstableCrafting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */