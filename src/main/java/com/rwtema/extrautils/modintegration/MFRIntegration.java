package com.rwtema.extrautils.modintegration;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

public class MFRIntegration {
  private static MFRIntegration instance = null;
  
  public static void registerMFRIntegration() {
    if (instance != null)
      throw new IllegalStateException("Already registered"); 
    if (!ExtraUtils.enderLilyEnabled)
      return; 
    instance = new MFRIntegration();
    FactoryRegistry.sendMessage("registerHarvestable_Crop", new ItemStack((Block)ExtraUtils.enderLily, 1, 7));
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("seed", Item.itemRegistry.getNameForObject(Item.getItemFromBlock((Block)ExtraUtils.enderLily)));
    tag.setString("crop", Block.blockRegistry.getNameForObject(ExtraUtils.enderLily));
    FactoryRegistry.sendMessage("registerPlantable_Standard", tag);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\MFRIntegration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */