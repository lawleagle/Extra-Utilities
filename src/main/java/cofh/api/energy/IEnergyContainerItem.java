package cofh.api.energy;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {
  int receiveEnergy(ItemStack paramItemStack, int paramInt, boolean paramBoolean);
  
  int extractEnergy(ItemStack paramItemStack, int paramInt, boolean paramBoolean);
  
  int getEnergyStored(ItemStack paramItemStack);
  
  int getMaxEnergyStored(ItemStack paramItemStack);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\energy\IEnergyContainerItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */