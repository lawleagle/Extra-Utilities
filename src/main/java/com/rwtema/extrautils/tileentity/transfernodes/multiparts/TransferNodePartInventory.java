package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class TransferNodePartInventory extends TransferNodePart implements INodeInventory {
  public TransferNodePartInventory() {
    super((TileEntityTransferNode)new TileEntityTransferNodeInventory());
  }
  
  public TransferNodePartInventory(int meta) {
    super(meta, (TileEntityTransferNode)new TileEntityTransferNodeInventory());
  }
  
  public TransferNodePartInventory(TileEntityTransferNode node) {
    super(node);
  }
  
  public TransferNodePartInventory(int meta, TileEntityTransferNodeInventory node) {
    super(meta, (TileEntityTransferNode)node);
  }
  
  public void onRemoved() {
    if (!(getWorld()).isRemote && 
      !this.node.buffer.isEmpty()) {
      List<ItemStack> drops = new ArrayList<ItemStack>();
      ItemStack item = (ItemStack)(getNode()).buffer.getBuffer();
      drops.add(item);
      tile().dropItems(drops);
    } 
    super.onRemoved();
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    Box t = new Box(0.125F, 0.0F, 0.125F, 0.875F, 0.375F, 0.875F);
    t.rotateToSide(getNodeDir());
    List<Cuboid6> s = new ArrayList<Cuboid6>();
    s.add(new Cuboid6(t.minX, t.minY, t.minZ, t.maxX, t.maxY, t.maxZ));
    return s;
  }
  
  public TileEntityTransferNodeInventory getNode() {
    return (TileEntityTransferNodeInventory)this.node;
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return (!(npart instanceof net.minecraft.inventory.IInventory) && super.occlusionTest(npart));
  }
  
  public String getType() {
    return "extrautils:transfer_node_inv";
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */