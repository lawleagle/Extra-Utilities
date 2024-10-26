package invtweaks.api;

import invtweaks.api.container.ContainerSection;
import net.minecraft.item.ItemStack;

public interface InvTweaksAPI {
  void addOnLoadListener(IItemTreeListener paramIItemTreeListener);
  
  boolean removeOnLoadListener(IItemTreeListener paramIItemTreeListener);
  
  void setSortKeyEnabled(boolean paramBoolean);
  
  void setTextboxMode(boolean paramBoolean);
  
  int compareItems(ItemStack paramItemStack1, ItemStack paramItemStack2);
  
  void sort(ContainerSection paramContainerSection, SortingMethod paramSortingMethod);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\invtweaks\api\InvTweaksAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */