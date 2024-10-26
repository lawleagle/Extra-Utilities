package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.texture.LiquidColorRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityDrum extends TileEntity implements IFluidHandler {
  public static final int numBuckets = 256;

  public static final int defaultCapacity = 16000;

  private FluidTank tank = new FluidTank(16000);

  public static int numTicksTilDisplayEmpty = 100;

  public boolean recentlyDrained = false;

  public boolean recentlyFilled = false;

  public FluidStack prevFluid = null;

  boolean sided;

  public static int getCapacityFromMetadata(int meta) {
    if (meta == 1)
      return 256000;
    return 16000;
  }

  public void setCapacityFromMetadata(int meta) {
    if (meta == 1)
      this.tank.setCapacity(getCapacityFromMetadata(meta));
  }

  public void loadDrumFromNBT(NBTTagCompound par1NBTTagCompound) {
    this.tank.setFluid(null);
    this.tank.readFromNBT(par1NBTTagCompound.getCompoundTag("tank"));
  }

  public void writeDrumToNBT(NBTTagCompound par1NBTTagCompound) {
    NBTTagCompound tag = new NBTTagCompound();
    this.tank.writeToNBT(tag);
    par1NBTTagCompound.setTag("tank", (NBTBase)tag);
  }

  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    loadDrumFromNBT(tag);
    this.tank.setCapacity(tag.getCompoundTag("tank").getInteger("capacity"));
    if (this.tank.getFluid() != null)
      this.prevFluid = this.tank.getFluid().copy();
  }

  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    writeDrumToNBT(tag);
    NBTTagCompound tag2 = tag.getCompoundTag("tank");
    tag2.setInteger("capacity", this.tank.getCapacity());
    tag.setTag("tank", (NBTBase)tag2);
  }

  public boolean canUpdate() {
    return false;
  }

  public Packet getDescriptionPacket() {
    NBTTagCompound t = new NBTTagCompound();
    writeDrumToNBT(t);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
  }

  @SideOnly(Side.CLIENT)
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    if (!this.worldObj.isRemote)
      return;
    loadDrumFromNBT(pkt.func_148857_g());
    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
  }

  public void ticked() {
    if (this.recentlyDrained) {
      this.recentlyDrained = false;
      if (this.recentlyFilled) {
        this.recentlyFilled = false;
        this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, getBlockType(), numTicksTilDisplayEmpty);
      } else {
        this.prevFluid = null;
        markDirty();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
    }
  }

  public TileEntityDrum() {
    this.sided = false;
  }

  public TileEntityDrum(int metadata) {
    this.sided = false;
    this.blockMetadata = metadata;
    setCapacityFromMetadata(metadata);
  }

  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    if (this.sided && from.ordinal() > 1)
      return 0;
    boolean t2 = (this.tank.getFluid() == null);
    int t = this.tank.fill(resource, doFill);
    if (doFill) {
      if (t2 && this.tank.getFluid() != null &&
        !this.tank.getFluid().isFluidEqual(this.prevFluid)) {
        this.prevFluid = this.tank.getFluid().copy();
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
      if (t != 0) {
        this.recentlyFilled = true;
        markDirty();
      }
    }
    return t;
  }

  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    if ((this.sided && from.ordinal() > 1) || resource == null || !resource.isFluidEqual(this.tank.getFluid()))
      return null;
    if (doDrain)
      markDirty();
    return drain(from, resource.amount, doDrain);
  }

  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    if ((!this.sided || from.ordinal() <= 1) && this.tank.getFluidAmount() > 0) {
      FluidStack t = this.tank.drain(maxDrain, doDrain);
      if (doDrain && t != null) {
        if (this.tank.getFluidAmount() == 0) {
          this.recentlyFilled = false;
          this.recentlyDrained = true;
          this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, getBlockType(), numTicksTilDisplayEmpty);
        }
        markDirty();
      }
      return t;
    }
    return null;
  }

  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return (!this.sided || from.ordinal() <= 1);
  }

  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return true;
  }

  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return new FluidTankInfo[] { this.tank.getInfo() };
  }

  @SideOnly(Side.CLIENT)
  public int getColor() {
    return LiquidColorRegistry.getFluidColor(this.tank.getFluid());
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityDrum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
