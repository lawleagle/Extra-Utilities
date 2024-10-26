package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface INode extends IPipe {
  TileEntityTransferNode getNode();
  
  int getNodeX();
  
  int getNodeY();
  
  int getNodeZ();
  
  ForgeDirection getNodeDir();
  
  int getPipeX();
  
  int getPipeY();
  
  int getPipeZ();
  
  int getPipeDir();
  
  List<ItemStack> getUpgrades();
  
  boolean checkRedstone();
  
  BoxModel getModel(ForgeDirection paramForgeDirection);
  
  String getNodeType();
  
  void bufferChanged();
  
  boolean isPowered();
  
  boolean recalcRedstone();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\INode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */