package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.asm.FluidIDGetter;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import invtweaks.api.container.InventoryContainer;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@InventoryContainer
public class ContainerTransferNode extends Container {
  public int lastenergy = 0;
  
  public int lastenergycount = 0;
  
  public int liquid_type = -1;
  
  public int liquid_amount = -1;
  
  private TileEntityTransferNode node;
  
  private IInventory player;
  
  private int lastpipe_x = 0, lastpipe_y = 0, lastpipe_z = 0;
  
  public ContainerTransferNode(IInventory player, TileEntityTransferNode node) {
    this.node = node;
    if (node instanceof IInventory)
      addSlotToContainer(new Slot((IInventory)node, 0, 80, 83)); 
    for (int i = 0; i < node.upgrades.getSizeInventory(); i++)
      addSlotToContainer(new SlotChecksValidity((IInventory)node.upgrades, i, 35 + i * 18, 121)); 
    for (int iy = 0; iy < 3; iy++) {
      for (int j = 0; j < 9; j++)
        addSlotToContainer(new Slot(player, j + iy * 9 + 9, 8 + j * 18, 143 + iy * 18)); 
    } 
    for (int ix = 0; ix < 9; ix++)
      addSlotToContainer(new Slot(player, ix, 8 + ix * 18, 201)); 
  }
  
  public void addCraftingToCrafters(ICrafting icrafting) {
    super.addCraftingToCrafters(icrafting);
    icrafting.sendProgressBarUpdate(this, 0, this.lastpipe_x = this.node.pipe_x);
    icrafting.sendProgressBarUpdate(this, 1, this.lastpipe_y = this.node.pipe_y);
    icrafting.sendProgressBarUpdate(this, 2, this.lastpipe_z = this.node.pipe_z);
    if (this.node instanceof TileEntityTransferNodeEnergy) {
      icrafting.sendProgressBarUpdate(this, 3, this.lastenergycount = ((TileEntityTransferNodeEnergy)this.node).numMachines());
      this.lastenergy = ((TileEntityTransferNodeEnergy)this.node).getEnergyStored(null);
      for (int i = 0; i < 3; i++)
        icrafting.sendProgressBarUpdate(this, 6 + i, convToShort(this.lastenergy, i)); 
    } 
    int newliquid_type = -1;
    int newliquid_amount = -1;
    if (this.node instanceof TileEntityTransferNodeLiquid) {
      FluidStack t = (((TileEntityTransferNodeLiquid)this.node).getTankInfo(null)[0]).fluid;
      if (t != null && t.amount > 0) {
        newliquid_type = FluidIDGetter.fluidLegacy.getID(t);
        newliquid_amount = t.amount;
        icrafting.sendProgressBarUpdate(this, 4, newliquid_type);
        icrafting.sendProgressBarUpdate(this, 5, newliquid_amount);
      } 
    } 
  }
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = this.inventorySlots.get(par2);
    int start = 0, end = this.node.upgrades.getSizeInventory();
    if (this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory) {
      start++;
      end++;
    } 
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 < end) {
        if (!mergeItemStack(itemstack1, end, this.inventorySlots.size(), true))
          return null; 
      } else if ((!this.node.upgrades.isItemValidForSlot(0, itemstack1) || !mergeItemStack(itemstack1, start, end, false)) && (
        start == 0 || !mergeItemStack(itemstack1, 0, start, false))) {
        return null;
      } 
      if (itemstack1.stackSize == 0) {
        slot.putStack(null);
      } else {
        slot.onSlotChanged();
      } 
    } 
    return itemstack;
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return this.node.isUseableByPlayer(entityplayer);
  }
  
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    int newenergy = -1;
    int newenergycount = -1;
    if (this.node instanceof TileEntityTransferNodeEnergy) {
      newenergy = ((TileEntityTransferNodeEnergy)this.node).getEnergyStored(null);
      newenergycount = ((TileEntityTransferNodeEnergy)this.node).numMachines();
    } 
    int newliquid_type = -1;
    int newliquid_type_metadata = -1;
    int newliquid_amount = -1;
    if (this.node instanceof TileEntityTransferNodeLiquid) {
      FluidStack t = (((TileEntityTransferNodeLiquid)this.node).getTankInfo(null)[0]).fluid;
      if (t != null && t.amount > 0) {
        newliquid_type = FluidIDGetter.fluidLegacy.getID(t);
        newliquid_amount = t.amount;
      } 
    } 
    for (Object crafter : this.crafters) {
      ICrafting icrafting = (ICrafting)crafter;
      if (this.lastpipe_x != this.node.pipe_x || this.lastpipe_y != this.node.pipe_y || this.lastpipe_z != this.node.pipe_z) {
        icrafting.sendProgressBarUpdate(this, 0, this.node.pipe_x);
        icrafting.sendProgressBarUpdate(this, 1, this.node.pipe_y);
        icrafting.sendProgressBarUpdate(this, 2, this.node.pipe_z);
      } 
      if (newenergycount != this.lastenergycount)
        icrafting.sendProgressBarUpdate(this, 3, newenergycount); 
      if (this.liquid_type != newliquid_type || this.liquid_amount != newliquid_amount) {
        icrafting.sendProgressBarUpdate(this, 4, newliquid_type);
        icrafting.sendProgressBarUpdate(this, 5, newliquid_amount);
      } 
      if (convToShort(newenergy, 2) != convToShort(this.lastenergy, 0))
        icrafting.sendProgressBarUpdate(this, 6, convToShort(this.lastenergy, 0)); 
      if (convToShort(newenergy, 1) != convToShort(this.lastenergy, 1))
        icrafting.sendProgressBarUpdate(this, 7, convToShort(this.lastenergy, 1)); 
      if (convToShort(newenergy, 2) != convToShort(this.lastenergy, 2))
        icrafting.sendProgressBarUpdate(this, 8, convToShort(this.lastenergy, 2)); 
    } 
    this.lastpipe_x = this.node.pipe_x;
    this.lastpipe_y = this.node.pipe_y;
    this.lastpipe_z = this.node.pipe_z;
    this.lastenergy = newenergy;
    this.lastenergycount = newenergycount;
    this.liquid_type = newliquid_type;
    this.liquid_amount = newliquid_amount;
  }
  
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int par1, int par2) {
    switch (par1) {
      case 0:
        this.node.pipe_x = par2;
      case 1:
        this.node.pipe_y = par2;
      case 2:
        this.node.pipe_z = par2;
        break;
      case 3:
        this.lastenergycount = par2;
        break;
      case 4:
        this.liquid_type = par2;
        break;
      case 5:
        this.liquid_amount = par2;
        break;
      case 6:
      case 7:
      case 8:
        this.lastenergy = (int)changeShort(this.lastenergy, (short)par2, par1 - 6);
        break;
    } 
  }
  
  public static short convToShort(double t, int level) {
    switch (level) {
      case 0:
        return (short)(int)Math.floor((t - Math.floor(t)) * 32768.0D);
      case 1:
        return (short)(int)(Math.floor(t) % 32768.0D);
      case 2:
        return (short)(int)Math.floor(t / 32768.0D);
    } 
    return 0;
  }
  
  public static float changeShort(float t, short k, int level) {
    short[] v = new short[3];
    for (int i = 0; i < 3; i++) {
      if (i == level) {
        v[i] = k;
      } else {
        v[i] = convToShort(t, i);
      } 
    } 
    return (v[2] * 32768 + v[1]) + v[0] / 32768.0F;
  }
  
  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlots() {
    return InventoryTweaksHelper.getSlots(this, false);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerTransferNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */