package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.multipart.ItemBlockMultiPart;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockTransferPipeMultiPart extends ItemBlockMultiPart {
  public final int pipePage;
  
  public ItemBlockTransferPipeMultiPart(Block par1) {
    super(par1);
    this.pipePage = ((BlockTransferPipe)par1).pipePage;
  }
  
  public TMultiPart createMultiPart(World world, BlockCoord pos, ItemStack item, int side) {
    return (TMultiPart)new PipePart(this.pipePage * 16 + getMetadata(item.getItemDamage()));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\ItemBlockTransferPipeMultiPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */