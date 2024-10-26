package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ICreativeTabSorting;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.TileEntityDrum;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;

public class ItemBlockDrum extends ItemBlockMetadata implements IFluidContainerItem, ICreativeTabSorting {
  protected int capacity = 256000;
  
  public ItemFluidContainer slaveItem = new ItemFluidContainer(-1, this.capacity);
  
  public ItemBlockDrum(Block b) {
    super(b);
  }
  
  public void setCapacityFromMeta(int meta) {
    this.slaveItem.setCapacity(TileEntityDrum.getCapacityFromMetadata(meta));
  }
  
  public ItemFluidContainer setCapacity(int capacity) {
    return this.slaveItem.setCapacity(capacity);
  }
  
  public FluidStack getFluid(ItemStack container) {
    setCapacityFromMeta(container.getItemDamage());
    return this.slaveItem.getFluid(container);
  }
  
  public int getCapacity(ItemStack container) {
    setCapacityFromMeta(container.getItemDamage());
    return this.slaveItem.getCapacity(container);
  }
  
  public int fill(ItemStack container, FluidStack resource, boolean doFill) {
    setCapacityFromMeta(container.getItemDamage());
    return this.slaveItem.fill(container, resource, doFill);
  }
  
  public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
    setCapacityFromMeta(container.getItemDamage());
    FluidStack t = this.slaveItem.drain(container, maxDrain, doDrain);
    if (this.slaveItem.getFluid(container) != null && (this.slaveItem.getFluid(container)).amount < 0) {
      container.setTagCompound(null);
      throw new RuntimeException("Fluid container has been drained into negative numbers. This is a Forge bug.");
    } 
    return t;
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List<String> info, boolean par4) {
    setCapacityFromMeta(item.getItemDamage());
    FluidStack fluid = this.slaveItem.getFluid(item);
    if (fluid != null) {
      info.add(XUHelper.getFluidName(fluid) + ": " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(fluid.amount) }) + " / " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(this.slaveItem.getCapacity(null)) }));
    } else {
      info.add("Empty: 0 / " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(this.slaveItem.getCapacity(null)) }));
    } 
  }
  
  public String getItemStackDisplayName(ItemStack item) {
    String s = super.getItemStackDisplayName(item);
    FluidStack fluid = getFluid(item);
    if (fluid != null)
      s = XUHelper.getFluidName(fluid) + " " + s; 
    return s.trim();
  }
  
  public String getSortingName(ItemStack item) {
    return super.getItemStackDisplayName(item) + ":" + getItemStackDisplayName(item);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockDrum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */