package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class TileEntityTransferNodeUpgradeInventory extends InventoryBasic {
  private final boolean isEnergy;
  
  public TileEntityTransferNode node;
  
  public TileEntityTransferNodeUpgradeInventory(int par3, TileEntityTransferNode node) {
    super("Upgrade Inventory", true, par3);
    this.node = node;
    this.isEnergy = node.getNode() instanceof cofh.api.energy.IEnergyHandler;
  }
  
  public boolean isItemValidForSlot(int par1, ItemStack item) {
    return (item != null && (pipeType(item) > 0 || isEnergy(item) || hasUpgradeNo(item)));
  }
  
  public boolean hasUpgradeNo(ItemStack item) {
    if (item == null || item.getItem() == null)
      return false; 
    return (item.getItem() == Items.glowstone_dust || item.getItem() == ExtraUtils.nodeUpgrade || item.getItem() == Item.getItemFromBlock(Blocks.redstone_torch));
  }
  
  public int getUpgradeNo(ItemStack item) {
    if (item.getItem() == Items.glowstone_dust)
      return -1; 
    if (item.getItem() == Item.getItemFromBlock(Blocks.redstone_torch))
      return -2; 
    return item.getItemDamage();
  }
  
  public boolean isEnergy(ItemStack item) {
    return (this.isEnergy && item.getItem() instanceof cofh.api.energy.IEnergyContainerItem);
  }
  
  public int pipeType(ItemStack item) {
    if (item == null || !(item.getItem() instanceof ItemBlock) || !(((ItemBlock)item.getItem()).field_150939_a instanceof BlockTransferPipe))
      return -1; 
    int i = item.getItemDamage() + ((BlockTransferPipe)((ItemBlock)item.getItem()).field_150939_a).pipePage * 16;
    return isValidPipeType(i) ? i : -1;
  }
  
  public boolean isValidPipeType(int i) {
    return ((i <= 0 || i > 7) && i != 9 && i != 15);
  }
  
  public void markDirty() {
    this.node.calcUpgradeModifiers();
    this.node.markDirty();
    super.markDirty();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNodeUpgradeInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */