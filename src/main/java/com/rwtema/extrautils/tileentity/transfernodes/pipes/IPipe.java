package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPipe {
  ArrayList<ForgeDirection> getOutputDirections(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, INodeBuffer paramINodeBuffer);
  
  boolean transferItems(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, INodeBuffer paramINodeBuffer);
  
  boolean canInput(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection);
  
  boolean canOutput(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection);
  
  int limitTransfer(TileEntity paramTileEntity, ForgeDirection paramForgeDirection, INodeBuffer paramINodeBuffer);
  
  IInventory getFilterInventory(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3);
  
  boolean shouldConnectToTile(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection);
  
  String getPipeType();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\IPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */