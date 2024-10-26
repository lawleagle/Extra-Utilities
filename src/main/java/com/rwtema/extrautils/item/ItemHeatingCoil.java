package com.rwtema.extrautils.item;

import cofh.api.energy.ItemEnergyContainer;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemHeatingCoil extends ItemEnergyContainer implements IFuelHandler {
  public static final int rate = 25;

  public static final int energyAmmountForOneHeat = 15000;

  public static final int uses = 100;

  public ItemHeatingCoil() {
    super(1500000, 1500000, 75000);
    GameRegistry.registerFuelHandler(this);
    setMaxStackSize(1);
    setTextureName("extrautils:heatingElement");
    setUnlocalizedName("extrautils:heatingElement");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }

  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
    int i = 30;
    float f13 = (float)(Minecraft.getSystemTime() % 6000L) / 3000.0F * 3.141592F * 2.0F;
    float t = 0.9F + 0.1F * MathHelper.cos(f13);
    double v = 1.0D - getDurabilityForDisplay(par1ItemStack);
    int r = i + (int)(v * (255 - i) * t);
    if (r > 255)
      r = 255;
    int g = i + (int)(v * (64 - i) * t);
    return r << 16 | g << 8 | i;
  }

  public boolean showDurabilityBar(ItemStack stack) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    par3List.add(XUHelper.addThousandsCommas(getEnergyStored(par1ItemStack)) + " / " + XUHelper.addThousandsCommas(getMaxEnergyStored(par1ItemStack)));
  }

  public double getDurabilityForDisplay(ItemStack stack) {
    return 1.0D - getEnergyStored(stack) / this.capacity;
  }

  public ItemStack getContainerItem(ItemStack itemStack) {
    ItemStack newItem = itemStack.copy();
    newItem.stackSize = 1;
    extractEnergy(newItem, 15000, false);
    return newItem;
  }

  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }

  public int getBurnTime(ItemStack fuel) {
    if (fuel == null || fuel.getItem() != this)
      return 0;
    return extractEnergy(fuel, 15000, true) / 50;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemHeatingCoil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
