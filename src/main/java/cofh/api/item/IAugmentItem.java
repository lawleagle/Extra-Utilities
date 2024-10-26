package cofh.api.item;

import java.util.Set;
import net.minecraft.item.ItemStack;

public interface IAugmentItem {
  int getAugmentLevel(ItemStack paramItemStack, String paramString);
  
  Set<String> getAugmentTypes(ItemStack paramItemStack);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\item\IAugmentItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */