package baubles.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IBauble {
  BaubleType getBaubleType(ItemStack paramItemStack);
  
  void onWornTick(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  void onEquipped(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  void onUnequipped(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  boolean canEquip(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  boolean canUnequip(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\baubles\api\IBauble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */