package com.rwtema.extrautils.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Facing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

@InterfaceList({@Interface(iface = "buildcraft.api.mj.IBatteryProvider", modid = "BuildCraftAPI|power")})
public class TileEntityEnderThermicLavaPump extends TileFluidHandler implements IFluidHandler, IEnergyHandler {
  public EntityPlayer owner = null;
  
  public boolean finished = false;
  
  private ForgeChunkManager.Ticket chunkTicket;
  
  private FluidTank tank;
  
  private int pump_y = -1;
  
  private int chunk_x = 0;
  
  private int chunk_z = 0;
  
  private int b = 0;
  
  private boolean find_new_block = false;
  
  private boolean init = false;
  
  private int chunk_no = -1;
  
  private float p = 0.95F;
  
  private EnergyStorage cofhEnergy = new EnergyStorage(10000);
  
  public TileEntityEnderThermicLavaPump() {
    this.tank = new FluidTank(1000);
  }
  
  public void updateEntity() {
    if (this.worldObj.isRemote)
      return; 
    if (this.finished) {
      if (this.chunkTicket != null) {
        ForgeChunkManager.releaseTicket(this.chunkTicket);
        this.chunkTicket = null;
      } 
      return;
    } 
    if (this.chunkTicket == null) {
      boolean valid = false;
      if (ExtraUtils.validDimensionsForEnderPump != null) {
        if (ExtraUtils.allNonVanillaDimensionsValidForEnderPump)
          valid = true; 
        for (int i = 0; i < ExtraUtils.validDimensionsForEnderPump.length; i++) {
          if (ExtraUtils.validDimensionsForEnderPump[i] == this.worldObj.provider.dimensionId) {
            valid = !valid;
            break;
          } 
        } 
      } 
      if (!valid) {
        this.finished = true;
        if (this.owner != null) {
          this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Pump will not function in this dimension"));
          this.owner = null;
        } 
        markDirty();
        return;
      } 
      this.chunkTicket = ForgeChunkManager.requestTicket(ExtraUtilsMod.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
      if (this.chunkTicket == null) {
        this.finished = true;
        if (this.owner != null) {
          this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Unable to assign Chunkloader, this pump will not work"));
          this.owner = null;
        } 
        markDirty();
        return;
      } 
      this.owner = null;
      this.chunkTicket.getModData().setString("id", "pump");
      this.chunkTicket.getModData().setInteger("pumpX", this.xCoord);
      this.chunkTicket.getModData().setInteger("pumpY", this.yCoord);
      this.chunkTicket.getModData().setInteger("pumpZ", this.zCoord);
      ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
    } 
    boolean goAgain = true;
    for (int t = 0; t < 16 && goAgain; t++) {
      goAgain = false;
      int bx = this.b >> 4, bz = this.b & 0xF;
      int pump_x = (this.chunk_x << 4) + bx;
      int pump_z = (this.chunk_z << 4) + bz;
      Block id = this.worldObj.getBlock(pump_x, this.pump_y, pump_z);
      if (this.pump_y >= 0 && XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, false) != null) {
        if (((this.tank.getInfo()).fluid == null || (this.tank.getInfo()).fluid.amount <= 0) && 
          this.cofhEnergy.extractEnergy(100, true) == 100 && this.cofhEnergy.extractEnergy(100, false) > 0) {
          FluidStack fluidStack = XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, true);
          this.tank.fill(fluidStack, true);
          if (this.worldObj.isAirBlock(pump_x, this.pump_y, pump_z))
            if (this.worldObj.rand.nextDouble() < this.p) {
              this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.stone, 0, 2);
            } else {
              this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.cobblestone, 0, 2);
            }  
          this.pump_y--;
          markDirty();
        } 
      } else {
        goAgain = true;
        if (!this.init)
          this.b = 256; 
        this.b++;
        if (this.b >= 256) {
          this.b = 0;
          goAgain = false;
          if (this.init && this.chunk_no > 0)
            for (int i = -2; i <= 2; i++) {
              for (int dz = -2; dz <= 2; dz++)
                ForgeChunkManager.unforceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + i, this.chunk_z + dz)); 
            }  
          this.chunk_no++;
          setChunk(this.chunk_no);
          for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
              ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
              this.worldObj.getChunkFromChunkCoords(this.chunk_x + dx, this.chunk_z + dz);
            } 
          } 
          ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
        } 
        this.pump_y = this.yCoord - 1;
        this.init = true;
        markDirty();
      } 
    } 
    FluidStack liquid = (this.tank.getInfo()).fluid;
    if (liquid != null && liquid.amount > 0) {
      int[] seq = XUHelper.rndSeq(6, this.worldObj.rand);
      for (int i = 0; i < 6; i++) {
        TileEntity tile = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[seq[i]], this.yCoord + Facing.offsetsYForSide[seq[i]], this.zCoord + Facing.offsetsZForSide[seq[i]]);
        if (tile instanceof IFluidHandler) {
          int moved = ((IFluidHandler)tile).fill(ForgeDirection.values()[seq[i]].getOpposite(), liquid, true);
          markDirty();
          this.tank.drain(moved, true);
          liquid = (this.tank.getInfo()).fluid;
          if (liquid == null || liquid.amount <= 0)
            break; 
        } 
      } 
    } 
  }
  
  public void invalidate() {
    ForgeChunkManager.releaseTicket(this.chunkTicket);
    super.invalidate();
  }
  
  public void onChunkUnload() {}
  
  public void setChunk(int chunk_no) {
    int base_chunk_x = this.xCoord >> 4;
    int base_chunk_z = this.zCoord >> 4;
    int j = chunk_no;
    if (j == 0) {
      this.chunk_x = base_chunk_x;
      this.chunk_z = base_chunk_z;
      return;
    } 
    j--;
    for (int k = 1; k <= 5; k++) {
      if (j >= 4 * k) {
        j -= 4 * k;
      } else {
        if (j < k) {
          this.chunk_x = base_chunk_x + j;
          this.chunk_z = base_chunk_z + k - j;
        } else if (j < 2 * k) {
          j -= k;
          this.chunk_x = base_chunk_x + k - j;
          this.chunk_z = base_chunk_z - j;
        } else if (j < 3 * k) {
          j -= 2 * k;
          this.chunk_x = base_chunk_x - j;
          this.chunk_z = base_chunk_z - k - j;
        } else {
          j -= 3 * k;
          this.chunk_x = base_chunk_x - k - j;
          this.chunk_z = base_chunk_z + j;
        } 
        return;
      } 
    } 
    this.finished = true;
    markDirty();
    chunk_no = 255;
  }
  
  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    if (par1NBTTagCompound.hasKey("block_no") && par1NBTTagCompound.getTag("block_no") instanceof net.minecraft.nbt.NBTTagInt) {
      this.b = par1NBTTagCompound.getInteger("block_no");
    } else {
      LogHelper.info("Extra Utilities: Problem loading EnderPump TileEntity Tag (block_no)", new Object[0]);
    } 
    if (par1NBTTagCompound.hasKey("chunk_no") && par1NBTTagCompound.getTag("chunk_no") instanceof net.minecraft.nbt.NBTTagByte) {
      this.chunk_no = par1NBTTagCompound.getByte("chunk_no");
    } else {
      LogHelper.info("Extra Utilities: Problem loading EnderPump TileEntity Tag (chunk_no)", new Object[0]);
    } 
    if (this.chunk_no == -128) {
      this.finished = true;
    } else {
      setChunk(this.chunk_no);
    } 
    this.tank.readFromNBT(par1NBTTagCompound.getCompoundTag("tank"));
    this.init = true;
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    par1NBTTagCompound.setInteger("block_no", this.b);
    if (this.finished) {
      par1NBTTagCompound.setByte("chunk_no", -128);
    } else {
      par1NBTTagCompound.setByte("chunk_no", (byte)this.chunk_no);
    } 
    NBTTagCompound tank_tags = new NBTTagCompound();
    this.tank.writeToNBT(tank_tags);
    par1NBTTagCompound.setTag("tank", (NBTBase)tank_tags);
    NBTTagCompound power_tags = new NBTTagCompound();
    par1NBTTagCompound.setTag("power", (NBTBase)power_tags);
  }
  
  public void forceChunkLoading(ForgeChunkManager.Ticket ticket) {
    if (this.chunkTicket == null)
      this.chunkTicket = ticket; 
    ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
    for (int dx = -2; dx <= 2; dx++) {
      for (int dz = -2; dz <= 2; dz++) {
        ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
        this.worldObj.getChunkFromChunkCoords(this.chunk_x + dx, this.chunk_z + dz);
      } 
    } 
  }
  
  public Packet getDescriptionPacket() {
    if (this.finished) {
      NBTTagCompound t = new NBTTagCompound();
      t.setBoolean("finished", true);
    } 
    return null;
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return this.cofhEnergy.receiveEnergy(maxReceive, simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    return this.cofhEnergy.extractEnergy(maxExtract, simulate);
  }
  
  public boolean canConnectEnergy(ForgeDirection from) {
    return true;
  }
  
  public int getEnergyStored(ForgeDirection from) {
    return this.cofhEnergy.getEnergyStored();
  }
  
  public int getMaxEnergyStored(ForgeDirection from) {
    return this.cofhEnergy.getMaxEnergyStored();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityEnderThermicLavaPump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */