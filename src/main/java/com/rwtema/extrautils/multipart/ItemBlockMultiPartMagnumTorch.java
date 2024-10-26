package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockMultiPartMagnumTorch extends ItemBlockMultiPart {
  public ItemBlockMultiPartMagnumTorch(Block par1) {
    super(par1);
    this.hasBlockMetadata = false;
  }
  
  public TMultiPart createMultiPart(World world, BlockCoord pos, ItemStack item, int side) {
    return (TMultiPart)new MagnumTorchPart();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\ItemBlockMultiPartMagnumTorch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */