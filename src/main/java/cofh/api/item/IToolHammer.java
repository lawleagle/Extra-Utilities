package cofh.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IToolHammer {
  boolean isUsable(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase, int paramInt1, int paramInt2, int paramInt3);
  
  void toolUsed(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase, int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\item\IToolHammer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */