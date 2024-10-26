package com.rwtema.extrautils.tileentity.generators;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import com.rwtema.extrautils.sounds.ISoundTile;
import com.rwtema.extrautils.sounds.Sounds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class TileEntityGenerator extends TileEntity implements IEnergyHandler, ISoundTile {
  public static final int capacity = 100000;
  
  public EnergyStorage storage = new EnergyStorage(100000);
  
  public byte rotation = 0;
  
  public double coolDown = 0.0D;
  
  int c = -1;
  
  public boolean initPower = true;
  
  public EnergyStorage getStorage() {
    if (this.initPower && this.worldObj != null && !this.worldObj.isRemote && this.worldObj.blockExists(x(), y(), z())) {
      this.initPower = false;
      this.storage.setCapacity(100000 * getMultiplier());
      this.storage.setMaxTransfer(100000 * getMultiplier());
    } 
    return this.storage;
  }
  
  public boolean playSound = false;
  
  private int multiplier = -1;
  
  private double divisor = -1.0D;
  
  public int getMultiplier() {
    if (this.multiplier == -1) {
      Block b = getBlockType();
      if (b instanceof BlockGenerator) {
        this.multiplier = ((BlockGenerator)b).numGenerators;
      } else {
        this.multiplier = 1;
      } 
    } 
    return this.multiplier;
  }
  
  public void invalidate() {
    super.invalidate();
  }
  
  public double getDivisor() {
    if (this.divisor == -1.0D)
      this.divisor = 1.0D / getMultiplier(); 
    return this.divisor;
  }
  
  public static int getFurnaceBurnTime(ItemStack item) {
    if (item == null)
      return 0; 
    if (item.getItem() == Items.lava_bucket)
      return 0; 
    return TileEntityFurnace.getItemBurnTime(item);
  }
  
  public TileEntity getTile() {
    return this;
  }
  
  public static ResourceLocation hum = new ResourceLocation("extrautils", "ambient.hum");
  
  public ResourceLocation getSound() {
    return hum;
  }
  
  public boolean shouldSoundPlay() {
    return this.playSound;
  }
  
  public static String getCoolDownString(double time) {
    String s = String.format("%.1f", new Object[] { Double.valueOf(time % 60.0D) }) + "s";
    int t = (int)time / 60;
    if (t == 0)
      return s; 
    s = (t % 60) + "m " + s;
    t /= 60;
    if (t == 0)
      return s; 
    s = (t % 24) + "h " + s;
    t /= 24;
    if (t == 0)
      return s; 
    s = t + "d " + s;
    return s;
  }
  
  public int x() {
    return this.xCoord;
  }
  
  public int y() {
    return this.yCoord;
  }
  
  public int z() {
    return this.zCoord;
  }
  
  public boolean isPowered() {
    return this.worldObj.isBlockIndirectlyGettingPowered(x(), y(), z());
  }
  
  public String getBlurb(double coolDown, double energy) {
    if (coolDown == 0.0D)
      return ""; 
    return "PowerLevel:\n" + String.format("%.1f", new Object[] { Double.valueOf(energy) }) + "\nTime Remaining:\n" + getCoolDownString(coolDown);
  }
  
  public double stepCoolDown() {
    return 1.0D;
  }
  
  public int getCompLevel() {
    if (this.c == -1)
      this.c = getStorage().getEnergyStored() * 15 / getStorage().getMaxEnergyStored(); 
    return this.c;
  }
  
  public void checkCompLevel() {
    if (getCompLevel() != getStorage().getEnergyStored() * 15 / getStorage().getMaxEnergyStored()) {
      this.c = getStorage().getEnergyStored() * 15 / getStorage().getMaxEnergyStored();
      this.worldObj.notifyBlocksOfNeighborChange(x(), y(), z(), getBlockType());
    } 
  }
  
  public boolean shouldProcess() {
    return false;
  }
  
  private boolean shouldInit = true;
  
  public void updateEntity() {
    if (this.worldObj.isRemote) {
      if (this.shouldInit) {
        this.shouldInit = false;
        Sounds.addGenerator(this);
      } 
      return;
    } 
    if (this.coolDown > 0.0D) {
      if (this.coolDown > 1.0D) {
        getStorage().receiveEnergy((int)Math.floor(genLevel() * getMultiplier()), false);
        this.coolDown -= stepCoolDown();
      } else {
        getStorage().receiveEnergy((int)Math.floor(this.coolDown * genLevel() * getMultiplier()), false);
        this.coolDown = 0.0D;
      } 
    } else {
      this.coolDown = 0.0D;
    } 
    doSpecial();
    if (shouldProcess() && (getStorage().getEnergyStored() == 0 || getStorage().getEnergyStored() < Math.min((getStorage().getMaxEnergyStored() - 1000), getStorage().getMaxEnergyStored() - getMultiplier() * genLevel())))
      processInput(); 
    if (((this.coolDown > 0.0D)) != this.playSound) {
      this.worldObj.markBlockForUpdate(x(), y(), z());
      this.playSound = (this.coolDown > 0.0D);
    } 
    if (shouldTransmit() && getStorage().getEnergyStored() > 0)
      transmitEnergy(); 
    checkCompLevel();
  }
  
  public void doSpecial() {}
  
  @SideOnly(Side.CLIENT)
  public void doRandomDisplayTickR(Random random) {}
  
  private void transmitEnergy() {
    for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
      TileEntity tile = this.worldObj.getTileEntity(x() + side.offsetX, y() + side.offsetY, z() + side.offsetZ);
      if (!(tile instanceof TileEntityGenerator))
        if (tile instanceof IEnergyReceiver)
          getStorage().extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), getStorage().extractEnergy(transferLimit() * getMultiplier(), true), false), false);  
    } 
  }
  
  public int transferLimit() {
    return getStorage().getMaxEnergyStored();
  }
  
  public boolean shouldTransmit() {
    return true;
  }
  
  public int getMaxCoolDown() {
    return 200;
  }
  
  public double getNerfVisor() {
    if (getMultiplier() == 1)
      return 1.0D; 
    if (getMultiplier() <= 8)
      return 1.0D; 
    return 1.0D;
  }
  
  public final boolean addCoolDown(double coolDown, boolean simulate) {
    if (!simulate)
      this.coolDown += coolDown * getDivisor() * getNerfVisor(); 
    return true;
  }
  
  public boolean processInput() {
    return false;
  }
  
  public double genLevel() {
    return 0.0D;
  }
  
  public FluidTank[] getTanks() {
    return new FluidTank[0];
  }
  
  public InventoryGeneric getInventory() {
    return null;
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    int energy = nbt.getInteger("Energy");
    if (energy > this.storage.getMaxEnergyStored()) {
      this.storage.setCapacity(energy);
      this.initPower = true;
    } 
    this.storage.setEnergyStored(energy);
    if (getInventory() != null)
      getInventory().readFromNBT(nbt); 
    if (getTanks() != null)
      for (int i = 0; i < (getTanks()).length; i++)
        getTanks()[i].readFromNBT(nbt.getCompoundTag("Tank_" + i));  
    this.rotation = (byte)nbt.getInteger("rotation");
    this.coolDown = nbt.getDouble("coolDown");
    this.playSound = (this.coolDown > 0.0D);
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    getStorage().writeToNBT(nbt);
    if (getInventory() != null)
      getInventory().writeToNBT(nbt); 
    if (getTanks() != null)
      for (int i = 0; i < (getTanks()).length; i++) {
        NBTTagCompound t = new NBTTagCompound();
        getTanks()[i].writeToNBT(t);
        nbt.setTag("Tank_" + i, (NBTBase)t);
      }  
    nbt.setInteger("rotation", this.rotation);
    nbt.setDouble("coolDown", this.coolDown);
    super.writeToNBT(nbt);
    NBTTagCompound backup = new NBTTagCompound();
    super.writeToNBT(backup);
    nbt.setTag("backup", (NBTBase)backup);
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound t = new NBTTagCompound();
    t.setByte("d", this.rotation);
    t.setBoolean("s", (this.coolDown > 0.0D));
    this.playSound = (this.coolDown > 0.0D);
    return (Packet)new S35PacketUpdateTileEntity(x(), y(), z(), 4, t);
  }
  
  @SideOnly(Side.CLIENT)
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    if (!this.worldObj.isRemote)
      return; 
    NBTTagCompound tags = pkt.func_148857_g();
    if (tags.hasKey("d")) {
      if (tags.getByte("d") != this.rotation)
        this.worldObj.markBlockForUpdate(x(), y(), z()); 
      this.rotation = tags.getByte("d");
    } 
    if (tags.hasKey("s")) {
      this.playSound = tags.getBoolean("s");
      Sounds.refresh();
    } 
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return 0;
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    return shouldTransmit() ? getStorage().extractEnergy(Math.min(transferLimit() * getMultiplier(), maxExtract), simulate) : 0;
  }
  
  public boolean canConnectEnergy(ForgeDirection from) {
    return true;
  }
  
  public int getEnergyStored(ForgeDirection from) {
    return getStorage().getEnergyStored();
  }
  
  public int getMaxEnergyStored(ForgeDirection from) {
    return getStorage().getMaxEnergyStored();
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    int c = 0;
    for (FluidTank tank : getTanks())
      c += tank.fill(resource, doFill); 
    return c;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return null;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return null;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return true;
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return false;
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    FluidTankInfo[] info = new FluidTankInfo[(getTanks()).length];
    for (int i = 0; i < (getTanks()).length; i++)
      info[i] = getTanks()[i].getInfo(); 
    return info;
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    return true;
  }
  
  public void readInvFromTags(NBTTagCompound tags) {
    if (tags.hasKey("Energy"))
      getStorage().readFromNBT(tags); 
  }
  
  public void writeInvToTags(NBTTagCompound tags) {
    if (getStorage().getEnergyStored() > 0)
      getStorage().writeToNBT(tags); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */