package com.rwtema.extrautils.tileentity.transfernodes;

import cofh.api.energy.IEnergyConnection;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TNHelper {
  public static Random rand = (Random)XURandom.getInstance();
  
  public static Set<Block> pipeBlocks = new HashSet<Block>();
  
  public static List<ForgeDirection> directions = new ArrayList<ForgeDirection>();
  
  static {
    for (int i = 0; i < 6; i++)
      directions.add(ForgeDirection.getOrientation(i)); 
  }
  
  public static IInventory getInventory(TileEntity tile) {
    if (tile instanceof IInventory) {
      if (tile instanceof net.minecraft.tileentity.TileEntityChest) {
        int x = tile.xCoord, y = tile.yCoord, z = tile.zCoord;
        Block blockID = tile.getWorldObj().getBlock(x, y, z);
        if (!tile.getWorldObj().isAirBlock(x, y, z) && blockID instanceof net.minecraft.block.BlockChest) {
          if (tile.getWorldObj().getBlock(x - 1, y, z) == blockID)
            return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile.getWorldObj().getTileEntity(x - 1, y, z), (IInventory)tile); 
          if (tile.getWorldObj().getBlock(x + 1, y, z) == blockID)
            return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile, (IInventory)tile.getWorldObj().getTileEntity(x + 1, y, z)); 
          if (tile.getWorldObj().getBlock(x, y, z - 1) == blockID)
            return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile.getWorldObj().getTileEntity(x, y, z - 1), (IInventory)tile); 
          if (tile.getWorldObj().getBlock(x, y, z + 1) == blockID)
            return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile, (IInventory)tile.getWorldObj().getTileEntity(x, y, z + 1)); 
        } 
      } 
      return (IInventory)tile;
    } 
    return null;
  }
  
  public static boolean isValidTileEntity(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == null && isValidTileEntity(world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ), dir.getOpposite().ordinal()));
  }
  
  public static boolean isValidTileEntity(TileEntity inv, int side) {
    if (inv == null)
      return false; 
    ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
    String classname = inv.getClass().toString();
    if (classname.contains("thermalexpansion") && classname.contains("conduit"))
      return false; 
    if (inv instanceof IFluidHandler) {
      FluidTankInfo[] t = ((IFluidHandler)inv).getTankInfo(forgeSide);
      if (t != null && t.length != 0)
        return true; 
    } 
    if (inv instanceof IInventory && (
      (IInventory)inv).getSizeInventory() > 0)
      if (inv instanceof ISidedInventory) {
        int[] t = ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
        if (t != null && t.length != 0)
          return true; 
      } else {
        return true;
      }  
    return isRFEnergy(inv, forgeSide);
  }
  
  public static boolean isRFEnergy(TileEntity inv, ForgeDirection forgeSide) {
    return (inv instanceof IEnergyConnection && ((IEnergyConnection)inv).canConnectEnergy(forgeSide));
  }
  
  public static boolean isEnergy(TileEntity inv, ForgeDirection forgeSide) {
    return isRFEnergy(inv, forgeSide);
  }
  
  public static void initBlocks() {
    for (Object aBlockRegistry : Block.blockRegistry) {
      Block i = (Block)aBlockRegistry;
      if (i instanceof IPipe || i instanceof IPipeBlock)
        pipeBlocks.add(i); 
    } 
  }
  
  public static IPipe getPipe(IBlockAccess world, int x, int y, int z) {
    if (world == null)
      return null; 
    if (y < 0 || y >= 256)
      return null; 
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile != null) {
      if (tile instanceof IPipe)
        return (IPipe)tile; 
      if (tile instanceof IPipeBlock)
        return ((IPipeBlock)tile).getPipe(world.getBlockMetadata(x, y, z)); 
    } 
    Block id = world.getBlock(x, y, z);
    if (!id.isAir(world, x, y, z) && pipeBlocks.contains(id)) {
      if (id instanceof IPipe)
        return (IPipe)id; 
      if (id instanceof IPipeBlock)
        return ((IPipeBlock)id).getPipe(world.getBlockMetadata(x, y, z)); 
    } 
    return null;
  }
  
  public static boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    IPipe pipe = getPipe(world, x, y, z);
    return (pipe != null && pipe.canInput(world, x, y, z, dir));
  }
  
  public static boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    IPipe pipe = getPipe(world, x, y, z);
    return (pipe != null && pipe.canOutput(world, x, y, z, dir));
  }
  
  public static boolean doesPipeConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return ((canOutput(world, x, y, z, dir) && canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite())) || (canInput(world, x, y, z, dir) && canOutput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite())));
  }
  
  public static ForgeDirection[] randomDirections() {
    Collections.shuffle(directions, rand);
    return directions.<ForgeDirection>toArray(new ForgeDirection[directions.size()]);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TNHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */