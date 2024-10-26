package com.rwtema.extrautils.modintegration;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TE4IMC {
  public static void addIntegration() {
    if (ExtraUtils.decorative1 != null)
      addTransposerFill(0, new ItemStack(Blocks.obsidian), new ItemStack((Block)ExtraUtils.decorative1, 1, 1), createFluidTag("ender", 50), false); 
    if (ExtraUtils.decorative2 != null)
      addSmelter(800, new ItemStack((Block)Blocks.sand), new ItemStack(Blocks.glass), new ItemStack((Block)ExtraUtils.decorative2, 2, 0)); 
    if (ExtraUtils.enderLily != null)
      addCrucible(80000, new ItemStack((Block)ExtraUtils.enderLily), createFluidTag("ender", 4000)); 
    if (ExtraUtils.decorative1 != null && 
      FluidRegistry.getFluid("xpjuice") != null)
      addTransposerFill(0, new ItemStack(Blocks.bookshelf, 1), new ItemStack((Block)ExtraUtils.decorative1, 1, 8), createFluidTag("xpjuice", 8000), false); 
  }
  
  public static void addSmelter(int i, ItemStack itemStack, ItemStack itemStack1, ItemStack itemStack2) {
    addSmelter(i, itemStack, itemStack1, itemStack2, null, 0);
  }
  
  public static NBTTagCompound getItemStackNBT(ItemStack item, int newStackSize) {
    NBTTagCompound tag = getItemStackNBT(item);
    tag.setByte("Count", (byte)newStackSize);
    return tag;
  }
  
  public static NBTTagCompound getItemStackNBT(ItemStack item) {
    NBTTagCompound tag = new NBTTagCompound();
    item.writeToNBT(tag);
    return tag;
  }
  
  public static ItemStack cloneStack(ItemStack item, int newStackSize) {
    ItemStack newitem = item.copy();
    newitem.stackSize = newStackSize;
    return newitem;
  }
  
  public static NBTTagCompound createFluidTag(String fluid, int amount) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("FluidName", fluid);
    nbt.setInteger("Amount", amount);
    return nbt;
  }
  
  public static void addSmelter(int energy, ItemStack a, ItemStack b, ItemStack output, ItemStack altOutput, int prob) {
    if (a == null || b == null || output == null)
      return; 
    if (energy <= 0)
      energy = 4000; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("energy", energy);
    tag.setTag("primaryInput", (NBTBase)getItemStackNBT(a));
    tag.setTag("secondaryInput", (NBTBase)getItemStackNBT(b));
    tag.setTag("primaryOutput", (NBTBase)getItemStackNBT(output));
    if (altOutput != null) {
      tag.setTag("secondaryOutput", (NBTBase)getItemStackNBT(altOutput));
      tag.setInteger("secondaryChance", prob);
    } 
    FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", tag);
  }
  
  public static void addPulverizer(int energy, ItemStack input, ItemStack output, ItemStack altOutput, int prob) {
    if (energy <= 0)
      energy = 3200; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("energy", energy);
    tag.setTag("input", (NBTBase)getItemStackNBT(input));
    tag.setTag("primaryOutput", (NBTBase)getItemStackNBT(output));
    if (altOutput != null) {
      tag.setTag("secondaryOutput", (NBTBase)getItemStackNBT(altOutput));
      tag.setInteger("secondaryChance", prob);
    } 
    FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", tag);
  }
  
  public static void addSawmill(int energy, ItemStack input, ItemStack output, ItemStack altOutput, int prob) {
    if (energy <= 0)
      energy = 2400; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("energy", energy);
    tag.setTag("input", (NBTBase)getItemStackNBT(input));
    tag.setTag("primaryOutput", (NBTBase)getItemStackNBT(output));
    if (altOutput != null) {
      tag.setTag("secondaryOutput", (NBTBase)getItemStackNBT(altOutput));
      tag.setInteger("secondaryChance", prob);
    } 
    FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", tag);
  }
  
  public static void addTransposerFill(int energy, ItemStack input, ItemStack output, FluidStack fluid, boolean reversible) {
    NBTTagCompound fluidTag = new NBTTagCompound();
    fluid.writeToNBT(fluidTag);
    addTransposerFill(energy, input, output, fluidTag, reversible);
  }
  
  public static void addTransposerFill(int energy, ItemStack input, ItemStack output, NBTTagCompound fluid, boolean reversible) {
    if (energy <= 0)
      energy = 8000; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("energy", energy);
    tag.setTag("input", (NBTBase)getItemStackNBT(input));
    tag.setTag("output", (NBTBase)getItemStackNBT(output));
    tag.setTag("fluid", (NBTBase)fluid);
    tag.setBoolean("reversible", reversible);
    FMLInterModComms.sendMessage("ThermalExpansion", "TransposerFillRecipe", tag);
  }
  
  public static void addCrucible(int energy, ItemStack input, FluidStack fluid) {
    NBTTagCompound fluidTag = new NBTTagCompound();
    fluid.writeToNBT(fluidTag);
    addCrucible(energy, input, fluidTag);
  }
  
  public static void addCrucible(int energy, ItemStack input, NBTTagCompound fluid) {
    if (energy <= 0)
      energy = 8000; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("energy", energy);
    tag.setTag("input", (NBTBase)getItemStackNBT(input));
    tag.setTag("output", (NBTBase)fluid);
    FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", tag);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TE4IMC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */