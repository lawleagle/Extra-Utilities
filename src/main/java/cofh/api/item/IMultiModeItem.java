package cofh.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMultiModeItem {
  int getMode(ItemStack paramItemStack);
  
  boolean setMode(ItemStack paramItemStack, int paramInt);
  
  boolean incrMode(ItemStack paramItemStack);
  
  boolean decrMode(ItemStack paramItemStack);
  
  int getNumModes(ItemStack paramItemStack);
  
  void onModeChange(EntityPlayer paramEntityPlayer, ItemStack paramItemStack);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\item\IMultiModeItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */