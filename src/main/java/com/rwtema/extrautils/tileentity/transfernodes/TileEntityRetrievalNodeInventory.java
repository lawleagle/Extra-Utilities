package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.ItemBufferRetrieval;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRetrievalNodeInventory extends TileEntityTransferNodeInventory {
  public TileEntityRetrievalNodeInventory() {
    super("Inv_remote", (INodeBuffer)new ItemBufferRetrieval());
  }
  
  public void processBuffer() {
    if (this.worldObj != null && !this.worldObj.isRemote) {
      if (this.coolDown > 0)
        this.coolDown -= this.stepCoolDown; 
      if (checkRedstone())
        return; 
      while (this.coolDown <= 0) {
        this.coolDown += baseMaxCoolDown;
        unloadbuffer();
        if (handleInventories())
          advPipeSearch(); 
      } 
    } 
  }
  
  private void unloadbuffer() {
    if (!this.buffer.isEmpty()) {
      ForgeDirection d = getNodeDir();
      TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
      if (tile != null && tile instanceof net.minecraft.inventory.IInventory)
        ((ItemBufferRetrieval)this.buffer).transferBack(tile, d.getOpposite(), this, this.xCoord, this.yCoord, this.zCoord); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityRetrievalNodeInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */