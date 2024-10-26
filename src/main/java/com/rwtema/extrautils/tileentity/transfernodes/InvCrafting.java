package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvCrafting extends InventoryCrafting {
  public InvCrafting(int par2, int par3) {
    super(fakeContainer.instance, par2, par3);
  }
  
  public int hashCode() {
    int hash = 0;
    int n = 1;
    for (int i = 0; i < getSizeInventory(); i++) {
      n *= 31;
      hash += n * hashItemStack(getStackInSlot(i));
    } 
    return hash;
  }
  
  public int hashItemStack(ItemStack item) {
    if (item == null)
      return 0; 
    int k = Item.getIdFromItem(item.getItem());
    k = k * 31 + item.getItemDamage();
    k *= 31;
    if (item.hasTagCompound())
      k += item.getTagCompound().hashCode(); 
    return k;
  }
  
  public static class fakeContainer extends Container {
    public static fakeContainer instance = new fakeContainer();
    
    public boolean canInteractWith(EntityPlayer var1) {
      return false;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\InvCrafting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */