package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityAdvTransferPipe extends TileEntity implements IPipe, IPipeCosmetic {
  int basePipeType = 0;
  
  int outputsMask = 0;
  
  public static boolean isVPipe(World world, int x, int y, int z) {
    return false;
  }
  
  public static int getType(World world, int x, int y, int z) {
    return 0;
  }
  
  public static boolean setBlockType() {
    return false;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return null;
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return false;
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return false;
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return false;
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return 0;
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return null;
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return false;
  }
  
  public String getPipeType() {
    return null;
  }
  
  public IIcon baseTexture() {
    return null;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return null;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return null;
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return null;
  }
  
  public float baseSize() {
    return 0.0F;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityAdvTransferPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */