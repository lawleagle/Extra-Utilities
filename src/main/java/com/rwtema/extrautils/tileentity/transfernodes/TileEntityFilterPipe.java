package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFilterPipe extends TileEntity implements IFilterPipe, IPipe, IPipeCosmetic {
  public InventoryBasic items = new InventoryBasic("Sorting Pipe", true, 6);
  
  public boolean canUpdate() {
    return false;
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    if (tags.hasKey("items")) {
      NBTTagCompound item_tags = tags.getCompoundTag("items");
      for (int i = 0; i < this.items.getSizeInventory(); i++) {
        if (item_tags.hasKey("item_" + i))
          this.items.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(item_tags.getCompoundTag("item_" + i))); 
      } 
    } 
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    NBTTagCompound item_tags = new NBTTagCompound();
    for (int i = 0; i < this.items.getSizeInventory(); i++) {
      if (this.items.getStackInSlot(i) != null) {
        NBTTagCompound item = new NBTTagCompound();
        this.items.getStackInSlot(i).writeToNBT(item);
        item_tags.setTag("item_" + i, (NBTBase)item);
      } 
    } 
    par1NBTTagCompound.setTag("items", (NBTBase)item_tags);
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return (IInventory)this.items;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(9).getOutputDirections(world, x, y, z, dir, buffer);
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(9).transferItems(world, x, y, z, dir, buffer);
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return StdPipes.getPipeType(9).canInput(world, x, y, z, dir);
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return StdPipes.getPipeType(9).canOutput(world, x, y, z, dir);
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return StdPipes.getPipeType(9).limitTransfer(dest, side, buffer);
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return StdPipes.getPipeType(9).shouldConnectToTile(world, x, y, z, dir);
  }
  
  public IIcon baseTexture() {
    return ((IPipeCosmetic)StdPipes.getPipeType(9)).baseTexture();
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return ((IPipeCosmetic)StdPipes.getPipeType(9)).pipeTexture(dir, blocked);
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(9)).invPipeTexture(dir);
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(9)).socketTexture(dir);
  }
  
  public String getPipeType() {
    return StdPipes.getPipeType(9).getPipeType();
  }
  
  public float baseSize() {
    return ((IPipeCosmetic)StdPipes.getPipeType(9)).baseSize();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityFilterPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */