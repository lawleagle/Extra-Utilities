package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityGeneratorEnder extends TileEntityGeneratorFurnace {
  public FluidTank[] tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "ender" }) };
  
  public int transferLimit() {
    return 400;
  }
  
  public double genLevel() {
    return 40.0D;
  }
  
  public int getMaxCoolDown() {
    return 0;
  }
  
  public double getFuelBurn(ItemStack item) {
    if (item != null) {
      if (item.getItem() == Items.ender_pearl)
        return 750.0D; 
      if (item.getItem() == Items.ender_eye)
        return 3000.0D; 
      if (item.getItem() == Item.getItemFromBlock((Block)ExtraUtils.enderLily))
        return 12000.0D; 
      FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
      if (fluid != null && "ender".equals(fluid.getFluid().getName()))
        return (fluid.amount * 6); 
    } 
    return 0.0D;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorEnder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */