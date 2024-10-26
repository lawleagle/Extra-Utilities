package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.FluidBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityTransferNodeLiquid extends TileEntityTransferNode implements INodeLiquid {
  public boolean nearSource = false;
  
  public long checkTimer = 0L;
  
  public TileEntityTransferNodeLiquid() {
    super("Liquid", (INodeBuffer)new FluidBuffer());
  }
  
  public TileEntityTransferNodeLiquid(String s, INodeBuffer buffer) {
    super(s, buffer);
  }
  
  public void processBuffer() {
    if (this.worldObj != null && !this.worldObj.isRemote) {
      if (this.coolDown > 0)
        this.coolDown -= this.stepCoolDown; 
      if (checkRedstone())
        return; 
      while (this.coolDown <= 0) {
        this.coolDown += baseMaxCoolDown;
        if (handleInventories())
          advPipeSearch(); 
        loadTank();
      } 
    } 
  }
  
  public void loadTank() {
    int dir = getBlockMetadata() % 6;
    if (this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) instanceof IFluidHandler) {
      IFluidHandler source = (IFluidHandler)this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]);
      FluidStack liquid = source.drain(ForgeDirection.getOrientation(dir).getOpposite(), (upgradeNo(3) == 0) ? 200 : ((FluidTank)this.buffer.getBuffer()).getCapacity(), false);
      int k = fill(getNodeDir(), liquid, false);
      if (k > 0)
        fill(getNodeDir(), source.drain(ForgeDirection.getOrientation(dir).getOpposite(), k, true), true); 
    } else if (!ExtraUtils.disableInfiniteWater && upgradeNo(2) > 0) {
      if (this.worldObj.getTotalWorldTime() - this.checkTimer > 20L) {
        this.checkTimer = this.worldObj.getTotalWorldTime();
        this.nearSource = false;
        if (isWaterSource(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir])) {
          int n = 0;
          for (int i = 2; i < 6; i++) {
            if (isWaterSource(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i]))
              n++; 
          } 
          if (n >= 2)
            this.nearSource = true; 
        } 
      } 
      if (this.nearSource) {
        long t = this.worldObj.getTotalWorldTime() / TileEntityTransferNode.baseMaxCoolDown * TileEntityTransferNode.baseMaxCoolDown;
        int a = 1000 * TileEntityTransferNode.baseMaxCoolDown / 20 * this.stepCoolDown;
        float b = 1000.0F * TileEntityTransferNode.baseMaxCoolDown / (20 * this.stepCoolDown);
        if (a != b && b - a > this.worldObj.rand.nextFloat())
          a++; 
        if (a > 0)
          fill(getNodeDir(), new FluidStack(FluidRegistry.WATER, a * (1 + upgradeNo(2))), true); 
      } 
    } 
  }
  
  public boolean isWaterSource(int x, int y, int z) {
    Block id = this.worldObj.getBlock(x, y, z);
    return ((id == Blocks.water || id == Blocks.flowing_water) && this.worldObj.getBlockMetadata(x, y, z) == 0);
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    if (from == getNodeDir()) {
      if (resource == null)
        return 0; 
      for (int j = 0; j < this.upgrades.getSizeInventory(); j++) {
        if (this.upgrades.getStackInSlot(j) != null && this.upgrades.getStackInSlot(j).getItemDamage() == 1 && this.upgrades.getStackInSlot(j).getTagCompound() != null && 
          !ItemNodeUpgrade.matchesFilterLiquid(resource, this.upgrades.getStackInSlot(j)))
          return 0; 
      } 
      return ((FluidTank)this.buffer.getBuffer()).fill(resource, doFill);
    } 
    return 0;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return null;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return null;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return (from.ordinal() == getBlockMetadata() % 6);
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return false;
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return new FluidTankInfo[] { ((FluidTank)this.buffer.getBuffer()).getInfo() };
  }
  
  public TileEntityTransferNodeLiquid getNode() {
    return this;
  }
  
  public BoxModel getModel(ForgeDirection dir) {
    BoxModel boxes = new BoxModel();
    float w = 0.125F;
    boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F)).rotateToSide(dir).setTextureSides(new Object[] { Integer.valueOf(dir.ordinal()), BlockTransferNode.nodeBase }));
    boxes.add((new Box(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.25F, 0.8125F)).rotateToSide(dir));
    boxes.add((new Box(0.3125F, 0.25F, 0.3125F, 0.6875F, 0.375F, 0.6875F)).rotateToSide(dir));
    boxes.add((new Box(0.375F, 0.25F, 0.375F, 0.625F, 0.375F, 0.625F)).rotateToSide(dir).setTexture(BlockTransferNode.nodeBase).setAllSideInvisible().setSideInvisible(new Object[] { Integer.valueOf(dir.getOpposite().ordinal()), Boolean.valueOf(false) }));
    return boxes;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNodeLiquid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */