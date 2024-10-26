package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import invtweaks.api.container.InventoryContainer;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@InventoryContainer
public class ContainerFilingCabinet extends Container {
  public static boolean updated = false;

  private TileEntityFilingCabinet cabinet;

  private int mimicThreshold;

  private boolean client;

  public ContainerFilingCabinet(IInventory player, TileEntityFilingCabinet cabinet, boolean client) {
    this.cabinet = cabinet;
    this.client = client;
    int j;
    for (j = 0; j < cabinet.getMaxSlots(); j++)
      addSlotToContainer(new SlotFilingCabinet((IInventory)cabinet, j, 8 + j * 18, 18));
    for (j = 0; j < 3; j++) {
      for (int k = 0; k < 9; k++)
        addSlotToContainer(new Slot(player, k + j * 9 + 9, 8 + k * 18, 158 + j * 18));
    }
    for (j = 0; j < 9; j++)
      addSlotToContainer(new Slot(player, j, 8 + j * 18, 216));
  }

  protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {}

  public void putStackInSlot(int par1, ItemStack par2ItemStack) {
    updated = true;
    super.putStackInSlot(par1, par2ItemStack);
  }

  @SideOnly(Side.CLIENT)
  public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
    updated = true;
    for (int i = 0; i < par1ArrayOfItemStack.length; i++)
      getSlot(i).putStack(par1ArrayOfItemStack[i]);
  }

  public boolean canDragIntoSlot(Slot par1Slot) {
    return (par1Slot.slotNumber >= this.cabinet.getMaxSlots());
  }

  public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
    ItemStack item;
    updated = true;
    if (par1 >= 0 && par1 < this.cabinet.getMaxSlots() && par4EntityPlayer.inventory.getItemStack() != null) {
      item = super.slotClick(this.cabinet.getSizeInventory() - 1, par2, par3, par4EntityPlayer);
    } else {
      item = super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
    this.cabinet.handleInput();
    return item;
  }

  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(par2);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 < this.cabinet.getMaxSlots()) {
        int m = Math.min(itemstack1.stackSize, itemstack1.getMaxStackSize());
        itemstack1.stackSize = m;
        if (!mergeItemStack(itemstack1, this.cabinet.getMaxSlots(), this.inventorySlots.size(), true))
          return null;
        itemstack1.stackSize = itemstack.stackSize - m + itemstack1.stackSize;
      } else if (!this.cabinet.isItemValidForSlot(this.cabinet.getSizeInventory() - 1, itemstack1) || !mergeItemStack(itemstack1, 0, this.cabinet.getMaxSlots(), false)) {
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
    return this.cabinet.isUseableByPlayer(entityplayer);
  }

  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlots() {
    return InventoryTweaksHelper.getSlots(this, true);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
