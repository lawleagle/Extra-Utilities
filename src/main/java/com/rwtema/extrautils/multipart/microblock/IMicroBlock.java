package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.TMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMicroBlock {
  int getMetadata();
  
  String getType();
  
  TMultiPart newPart(boolean paramBoolean);
  
  TMultiPart placePart(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, World paramWorld, BlockCoord paramBlockCoord, int paramInt1, Vector3 paramVector3, int paramInt2);
  
  void registerPassThroughs();
  
  void renderItem(ItemStack paramItemStack, MicroMaterialRegistry.IMicroMaterial paramIMicroMaterial);
  
  boolean hideCreativeTab();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\IMicroBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */