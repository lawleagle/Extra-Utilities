package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.multipart.ItemBlockMultiPart;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class ItemBlockTransferNodeMultiPart extends ItemBlockMultiPart {
  public ItemBlockTransferNodeMultiPart(Block par1) {
    super(par1);
  }
  
  public TMultiPart createMultiPart(World world, BlockCoord pos, ItemStack item, int side) {
    int metadata = item.getItemDamage() & 0xF;
    if (metadata > 12)
      return null; 
    if (this.field_150939_a == ExtraUtils.transferNode) {
      TransferNodePart pipePart;
      if (metadata < 6) {
        pipePart = new TransferNodePartInventory();
      } else if (metadata < 12) {
        pipePart = new TransferNodePartLiquid();
      } else if (metadata == 13) {
        pipePart = new TransferNodePartHyperEnergy();
      } else {
        pipePart = new TransferNodePartEnergy();
      } 
      if (metadata < 12)
        metadata += Facing.oppositeSide[side]; 
      pipePart.meta = (byte)metadata;
      return (TMultiPart)pipePart;
    } 
    if (this.field_150939_a == ExtraUtils.transferNodeRemote) {
      TransferNodePart pipePart;
      if (metadata < 6) {
        pipePart = new TransferNodePartInventoryRemote();
      } else if (metadata < 12) {
        pipePart = new TransferNodePartLiquidRemote();
      } else {
        pipePart = new TransferNodePartEnergy();
      } 
      if (metadata < 12)
        metadata += Facing.oppositeSide[side]; 
      pipePart.meta = (byte)metadata;
      return (TMultiPart)pipePart;
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\ItemBlockTransferNodeMultiPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */