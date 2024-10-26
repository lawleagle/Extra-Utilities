package com.rwtema.extrautils.tileentity.transfernodes;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.rwtema.extrautils.ChunkPos;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.modintegration.IC2EnergyHandler;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.EnergyBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTransferNodeEnergy extends TileEntityTransferNode implements INodeEnergy, IEnergyHandler {
  public static final int RF_PER_MJ = 10;
  
  public static final int RF_PER_EU = 4;
  
  public static final double RFtoBC = 0.1D;
  
  public static final double BCtoRF = 10.0D;
  
  public EnergyStorage powerHandler = new EnergyStorage(10000);
  
  public IC2EnergyHandler powerHandlerIC2 = null;
  
  public int powerInserted = 0;
  
  public boolean lastStep = false;
  
  public List<ChunkPos> searchLocations = new ArrayList<ChunkPos>();
  
  public List<EnergyPosition> clientele = new ArrayList<EnergyPosition>();
  
  public List<EnergyPosition> clientele_temp = new ArrayList<EnergyPosition>();
  
  public Object battery = null;
  
  private int search_i;
  
  public TileEntityTransferNodeEnergy() {
    this("Energy", (INodeBuffer)new EnergyBuffer());
  }
  
  public TileEntityTransferNodeEnergy(String s, INodeBuffer buffer) {
    super(s, buffer);
    if (Loader.isModLoaded("IC2"))
      this.powerHandlerIC2 = new IC2EnergyHandler(this, 512, 1); 
  }
  
  public static double RFtoBC(int RF) {
    return RF * 0.1D;
  }
  
  public static int toRF2(double BC) {
    return (int)Math.floor(BC * 10.0D);
  }
  
  public static int toRF3(double BC) {
    return (int)Math.round(BC * 10.0D);
  }
  
  public static int BCtoRF(double BC) {
    return (int)Math.floor(BC * 10.0D);
  }
  
  public static double toIC2(int k) {
    return k / 4.0D;
  }
  
  public static int fromIC2(double t) {
    return (int)Math.floor(t * 4.0D);
  }
  
  public int numMachines() {
    return this.clientele.size();
  }
  
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    this.clientele.clear();
    int i = 0;
    while (tag.hasKey("cx" + i)) {
      this.clientele.add(new EnergyPosition(tag.getInteger("cx" + i), tag.getInteger("cy" + i), tag.getInteger("cz" + i), tag.getByte("cs" + i), tag.getBoolean("ce")));
      this.clientele_temp.add(new EnergyPosition(tag.getInteger("cx" + i), tag.getInteger("cy" + i), tag.getInteger("cz" + i), tag.getByte("cs" + i), tag.getBoolean("ce")));
      i++;
    } 
    this.powerHandler.readFromNBT(tag);
    if (this.powerHandlerIC2 != null)
      this.powerHandlerIC2.readFromNBT(tag); 
  }
  
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    for (int i = 0; i < this.clientele.size(); i++) {
      tag.setInteger("cx" + i, ((EnergyPosition)this.clientele.get(i)).x);
      tag.setInteger("cy" + i, ((EnergyPosition)this.clientele.get(i)).y);
      tag.setInteger("cz" + i, ((EnergyPosition)this.clientele.get(i)).z);
      tag.setByte("cs" + i, ((EnergyPosition)this.clientele.get(i)).side);
      tag.setBoolean("ce", ((EnergyPosition)this.clientele.get(i)).extract);
    } 
    this.powerHandler.writeToNBT(tag);
    if (this.powerHandlerIC2 != null) {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      this.powerHandlerIC2.writeToNBT(nbttagcompound1);
      tag.setTag("buffer", (NBTBase)nbttagcompound1);
    } 
  }
  
  public boolean checkRedstone() {
    return false;
  }
  
  public void processBuffer() {
    if (this.powerHandlerIC2 != null) {
      this.powerHandlerIC2.updateEntity();
      this.powerHandlerIC2.useEnergy(toIC2(this.powerHandler.receiveEnergy(fromIC2(this.powerHandlerIC2.getEnergyStored()), false)));
    } 
    if (this.powerHandler.getEnergyStored() > 0)
      for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
        ItemStack itemstack = this.upgrades.getStackInSlot(i);
        if (itemstack != null && itemstack.getItem() instanceof IEnergyContainerItem) {
          int a = ((IEnergyContainerItem)itemstack.getItem()).receiveEnergy(itemstack, this.powerHandler.extractEnergy(10000, true), false);
          this.powerHandler.extractEnergy(a, false);
          if (a > 0)
            markDirty(); 
        } 
      }  
    if (upgradeNo(4) > 0)
      this.powerHandler.setEnergyStored(6400000); 
    if (this.coolDown > 0)
      this.coolDown -= this.stepCoolDown; 
    while (this.coolDown <= 0) {
      this.coolDown += baseMaxCoolDown;
      findMachines();
    } 
    sendEnergy();
  }
  
  public void onChunkUnload() {
    if (this.powerHandlerIC2 != null)
      this.powerHandlerIC2.onChunkUnload(); 
    super.onChunkUnload();
  }
  
  public void invalidate() {
    if (this.powerHandlerIC2 != null)
      this.powerHandlerIC2.invalidate(); 
    super.invalidate();
  }
  
  private void sendEnergy() {
    if (this.clientele.size() > 0) {
      Collections.shuffle(this.clientele);
      List<EnergyPosition> repeat = new ArrayList<EnergyPosition>();
      int m1 = this.powerHandler.getEnergyStored() / this.clientele.size();
      for (EnergyPosition client : this.clientele) {
        int x = this.xCoord + client.x;
        int y = this.yCoord + client.y;
        int z = this.zCoord + client.z;
        TileEntity tile = this.worldObj.getTileEntity(x, y, z);
        ForgeDirection from = ForgeDirection.getOrientation(client.side);
        if (client.extract) {
          if (tile instanceof IEnergyProvider && ((IEnergyProvider)tile).canConnectEnergy(from)) {
            int a = this.powerHandler.receiveEnergy(((IEnergyProvider)tile).extractEnergy(from, this.powerHandler.getMaxEnergyStored(), true), true);
            if (a > 0)
              this.powerHandler.receiveEnergy(((IEnergyProvider)tile).extractEnergy(from, a, false), false); 
          } 
          continue;
        } 
        if (this.powerHandler.getEnergyStored() > 0 && 
          tile instanceof IEnergyReceiver && ((IEnergyReceiver)tile).canConnectEnergy(from)) {
          IEnergyReceiver machine = (IEnergyReceiver)tile;
          int a = machine.receiveEnergy(from, this.powerHandler.getEnergyStored(), true);
          if (a > m1) {
            if (m1 >= 1) {
              a = machine.receiveEnergy(from, this.powerHandler.extractEnergy(m1, true), false);
              this.powerHandler.extractEnergy(a, false);
            } 
            repeat.add(client);
            continue;
          } 
          if (a > 0) {
            a = machine.receiveEnergy(from, this.powerHandler.extractEnergy(a, true), false);
            this.powerHandler.extractEnergy(a, false);
          } 
        } 
      } 
      if (this.powerHandler.getEnergyStored() > 0)
        for (EnergyPosition aRepeat : repeat) {
          int x = this.xCoord + aRepeat.x;
          int y = this.yCoord + aRepeat.y;
          int z = this.zCoord + aRepeat.z;
          TileEntity tile = this.worldObj.getTileEntity(x, y, z);
          ForgeDirection from = ForgeDirection.getOrientation(aRepeat.side);
          if (tile instanceof IEnergyReceiver) {
            IEnergyReceiver machine = (IEnergyReceiver)tile;
            int e = machine.receiveEnergy(from, this.powerHandler.getEnergyStored(), true);
            if (e > 0) {
              int a = machine.receiveEnergy(from, this.powerHandler.extractEnergy(e, true), false);
              this.powerHandler.extractEnergy(a, false);
            } 
          } 
        }  
    } 
  }
  
  public int hashCode() {
    return this.xCoord * 8976890 + this.yCoord * 981131 + this.zCoord;
  }
  
  private void findMachines() {
    this.search_i++;
    if (this.searchLocations.size() == 0 || this.search_i >= this.searchLocations.size()) {
      this.clientele.clear();
      this.clientele.addAll(this.clientele_temp);
      this.clientele_temp.clear();
      this.search_i = 0;
      this.searchLocations.clear();
      this.searchLocations.add(new ChunkPos(0, 0, 0));
    } 
    this.pipe_x = ((ChunkPos)this.searchLocations.get(this.search_i)).x;
    this.pipe_y = ((ChunkPos)this.searchLocations.get(this.search_i)).y;
    this.pipe_z = ((ChunkPos)this.searchLocations.get(this.search_i)).z;
    int x = this.pipe_x + this.xCoord;
    int y = this.pipe_y + this.yCoord;
    int z = this.pipe_z + this.zCoord;
    sendParticleUpdate();
    Block id = this.worldObj.getBlock(x, y, z);
    IPipe pipeBlock = TNHelper.getPipe((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
    if (pipeBlock != null)
      for (int i = 0; i < 6; i++) {
        if (pipeBlock.shouldConnectToTile((IBlockAccess)this.worldObj, x, y, z, ForgeDirection.getOrientation(i)) || (pipeBlock instanceof INode && ((INode)pipeBlock).getNodeDir() == ForgeDirection.getOrientation(i))) {
          TileEntity tile = this.worldObj.getTileEntity(x + Facing.offsetsXForSide[i], y + Facing.offsetsYForSide[i], z + Facing.offsetsZForSide[i]);
          if (TNHelper.isEnergy(tile, ForgeDirection.getOrientation(i).getOpposite())) {
            EnergyPosition pos = new EnergyPosition(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i], (byte)Facing.oppositeSide[i], "Energy_Extract".equals(pipeBlock.getPipeType()));
            if (!this.clientele_temp.contains(pos))
              this.clientele_temp.add(pos); 
            if (!this.clientele.contains(pos))
              this.clientele.add(pos); 
          } 
        } else if (TNHelper.doesPipeConnect((IBlockAccess)this.worldObj, x, y, z, ForgeDirection.getOrientation(i)) && 
          !this.searchLocations.contains(new ChunkPos(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i]))) {
          this.searchLocations.add(new ChunkPos(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i]));
        } 
      }  
  }
  
  public ForgeDirection getNodeDir() {
    return ForgeDirection.UNKNOWN;
  }
  
  public void resetSearch() {
    this.powerInserted = 0;
    super.resetSearch();
  }
  
  public TileEntityTransferNodeEnergy getNode() {
    return this;
  }
  
  public BoxModel getModel(ForgeDirection dir) {
    BoxModel boxes = new BoxModel();
    boxes.add(new Box(0.1875F, 0.3125F, 0.3125F, 0.8125F, 0.6875F, 0.6875F));
    boxes.add(new Box(0.3125F, 0.1875F, 0.3125F, 0.6875F, 0.8125F, 0.6875F));
    boxes.add(new Box(0.3125F, 0.3125F, 0.1875F, 0.6875F, 0.6875F, 0.8125F));
    boxes.add(new Box(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F));
    return boxes;
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return this.powerHandler.receiveEnergy(maxReceive, simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    return 0;
  }
  
  public boolean canConnectEnergy(ForgeDirection from) {
    return true;
  }
  
  public int getEnergyStored(ForgeDirection from) {
    return this.powerHandler.getEnergyStored();
  }
  
  public int getMaxEnergyStored(ForgeDirection from) {
    return this.powerHandler.getMaxEnergyStored();
  }
  
  public static class EnergyPosition extends ChunkPos {
    public byte side = 7;
    
    public boolean extract = false;
    
    public EnergyPosition(int par1, int par2, int par3, byte par4, boolean extract) {
      super(par1, par2, par3);
      this.side = par4;
      this.extract = extract;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true; 
      if (o == null || getClass() != o.getClass())
        return false; 
      if (!super.equals(o))
        return false; 
      EnergyPosition that = (EnergyPosition)o;
      if (this.extract != that.extract)
        return false; 
      if (this.side != that.side)
        return false; 
      return true;
    }
    
    public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.side;
      result = 31 * result + (this.extract ? 1 : 0);
      return result;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNodeEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */