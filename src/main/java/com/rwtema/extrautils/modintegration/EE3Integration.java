package com.rwtema.extrautils.modintegration;

import com.google.common.base.Throwables;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.BlockColor;
import cpw.mods.fml.common.Loader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class EE3Integration {
  static final boolean EE3Present;

  public static Method addRecipe;

  public static Method setAsNotLearnable;

  public static Method setAsNotRecoverable;

  public static Method addPreAssignedEnergyValue;

  static {
    boolean found = false;
    if (Loader.isModLoaded("EE3"))
      try {
        Class<?> aClass = Class.forName("com.pahimar.ee3.api.exchange.RecipeRegistryProxy");
        addRecipe = aClass.getDeclaredMethod("addRecipe", new Class[] { Object.class, List.class });
        aClass = Class.forName("com.pahimar.ee3.api.knowledge.AbilityRegistryProxy");
        setAsNotLearnable = aClass.getDeclaredMethod("setAsNotLearnable", new Class[] { Object.class });
        setAsNotRecoverable = aClass.getDeclaredMethod("setAsNotRecoverable", new Class[] { Object.class });
        aClass = Class.forName("com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy");
        addPreAssignedEnergyValue = aClass.getDeclaredMethod("addPreAssignedEnergyValue", new Class[] { Object.class, float.class });
        found = true;
      } catch (Exception e) {
        found = false;
      }
    EE3Present = found;
  }

  public static void addPreAssignedEnergyValue(Object object, float val) {
    if (!EE3Present)
      return;
    try {
      addPreAssignedEnergyValue.invoke(null, new Object[] { object, Float.valueOf(val) });
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static void setAsNotLearnable(Object o) {
    if (!EE3Present)
      return;
    try {
      setAsNotLearnable.invoke(null, new Object[] { o });
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static void setAsNotRecoverable(Object o) {
    if (!EE3Present)
      return;
    try {
      setAsNotRecoverable.invoke(null, new Object[] { o });
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static void addEMCRecipes() {
    if (!EE3Present)
      return;
    if (ExtraUtils.cursedEarthEnabled)
      addRecipe(new ItemStack(ExtraUtils.cursedEarth), new Object[] { Blocks.dirt, Items.rotten_flesh });
    if (ExtraUtils.enderLilyEnabled)
      addRecipe(new ItemStack((Block)ExtraUtils.enderLily), new Object[] { new ItemStack(Items.ender_pearl, 64) });
    if (ExtraUtils.divisionSigilEnabled)
      addRecipe(new ItemStack(ExtraUtils.divisionSigil, 2), new Object[] { new ItemStack(Items.ender_pearl, 4), Items.nether_star });
    if (ExtraUtils.wateringCanEnabled)
      addRecipe(new ItemStack((Item)ExtraUtils.wateringCan, 1, 0), new Object[] { new ItemStack((Item)ExtraUtils.wateringCan, 1, 1), new FluidStack(FluidRegistry.WATER, 1000) });
    if (ExtraUtils.decorative1 != null) {
      addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 8), new Object[] { Blocks.bookshelf, new ItemStack(Items.gold_ingot, 4), new ItemStack(Items.ender_pearl, 4) });
      addRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 2), new Object[] { Blocks.quartz_block });
    }
    for (BlockColor colorblock : ExtraUtils.colorblocks) {
      for (int i = 0; i < 16; i++) {
        addRecipe(new ItemStack((Block)colorblock, 1, i), new Object[] { colorblock.baseBlock });
      }
    }
    if (ExtraUtils.decorative2 != null)
      addRecipe(new ItemStack((Block)ExtraUtils.decorative2, 1, 0), new Object[] { Blocks.glass });
    if (ExtraUtils.unstableIngot != null) {
      setAsNotLearnable(new ItemStack(ExtraUtils.unstableIngot, 1, 32767));
      addRecipe(new ItemStack(ExtraUtils.unstableIngot, 1, 0), new Object[] { ExtraUtils.unstableIngot, Integer.valueOf(9), Integer.valueOf(2) });
    }
    if (ExtraUtils.soul != null) {
      addPreAssignedEnergyValue(new ItemStack((Item)ExtraUtils.soul), 1515413.0F);
      setAsNotLearnable(new ItemStack((Item)ExtraUtils.soul, 1, 32767));
    }
  }

  public static void addRecipe(ItemStack itemStack, Object... inputs) {
    if (!EE3Present)
      return;
    ArrayList<Object> items = new ArrayList(inputs.length);
    Object[] arr$ = inputs;
    int len$ = arr$.length, i$ = 0;
    while (true) {
      if (i$ < len$) {
        Object a = arr$[i$];
        if (a != null) {
          ItemStack input = null;
          if (a instanceof ItemStack) {
            input = (ItemStack)a;
          } else {
            items.add(a);
            i$++;
          }
          if (input.stackSize != 0)
            if (input.stackSize == 1) {
              items.add(input.copy());
            } else {
              int k = input.stackSize;
              input = input.copy();
              input.stackSize = 1;
              for (int i = 0; i < k; i++)
                items.add(input.copy());
            }
        }
      } else {
        break;
      }
      i$++;
    }
    if (items.isEmpty())
      return;
    recipes.add(new EE3Recipe(itemStack, items));
  }

  public static void finalRegister() {
    if (!EE3Present)
      return;
    try {
      for (EE3Recipe recipe : recipes) {
        addRecipe.invoke(null, new Object[] { recipe.output, recipe.inputs });
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  static List<EE3Recipe> recipes = new ArrayList<EE3Recipe>();

  public static class EE3Recipe {
    ItemStack output;

    List<?> inputs;

    public EE3Recipe(ItemStack itemStack, List<?> object) {
      this.output = itemStack;
      this.inputs = object;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\EE3Integration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
