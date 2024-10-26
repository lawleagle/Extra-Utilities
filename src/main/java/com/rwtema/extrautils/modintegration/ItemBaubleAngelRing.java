package com.rwtema.extrautils.modintegration;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.rwtema.extrautils.item.ItemAngelRing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemBaubleAngelRing extends ItemAngelRing implements IBauble {
  public BaubleType getBaubleType(ItemStack itemstack) {
    return super.getBaubleType(null);
  }
  
  public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
    onUpdate(itemstack, player.worldObj, (Entity)player, 0, false);
  }
  
  public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}
  
  public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}
  
  public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
    return false;
  }
  
  public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\ItemBaubleAngelRing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */