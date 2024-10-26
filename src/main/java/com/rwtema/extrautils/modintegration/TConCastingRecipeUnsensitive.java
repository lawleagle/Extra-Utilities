package com.rwtema.extrautils.modintegration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.client.FluidRenderProperties;
import tconstruct.library.crafting.CastingRecipe;

public class TConCastingRecipeUnsensitive extends CastingRecipe {
  public TConCastingRecipeUnsensitive(ItemStack replacement, FluidStack metal, ItemStack cast, boolean consume, int delay, FluidRenderProperties props) {
    super(replacement, metal, cast, consume, delay, props);
  }
  
  public TConCastingRecipeUnsensitive(CastingRecipe recipe) {
    super(recipe.output.copy(), recipe.castingMetal.copy(), recipe.cast.copy(), recipe.consumeCast, recipe.coolTime, recipe.fluidRenderProperties);
  }
  
  public boolean matches(FluidStack metal, ItemStack inputCast) {
    return (this.castingMetal != null && this.castingMetal.isFluidEqual(metal) && inputCast != null && this.cast != null && inputCast.getItem() == this.cast.getItem() && (this.cast.getItemDamage() == 32767 || this.cast.getItemDamage() == inputCast.getItemDamage()));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConCastingRecipeUnsensitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */