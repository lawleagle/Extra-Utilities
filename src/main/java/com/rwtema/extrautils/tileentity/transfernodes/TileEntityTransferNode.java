package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.tileentity.enderquarry.IChunkLoad;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityTransferNode extends TileEntity implements IPipe, INode, IPipeCosmetic, IChunkLoad {
  public static int baseMaxCoolDown = 20;
  
  public int pipe_x = 0, pipe_y = 0, pipe_z = 0, pipe_dir = 6;
  
  public int pipe_type = 0;
  
  public float pr = 1.0F;
  
  public float pg = 0.0F;
  
  public float pb = 0.0F;
  
  public TileEntityTransferNodeUpgradeInventory upgrades = new TileEntityTransferNodeUpgradeInventory(6, this);
  
  public boolean isReceiver = false;
  
  public String type;
  
  public INodeBuffer buffer;
  
  public boolean powered = false;
  
  public boolean init = false;
  
  public SearchType searchType = SearchType.RANDOM_WALK;
  
  public HashSet<SearchPosition> prevSearch = new HashSet<SearchPosition>();
  
  public ArrayList<SearchPosition> toSearch = new ArrayList<SearchPosition>();
  
  public Random rand = (Random)XURandom.getInstance();
  
  protected int coolDown;
  
  protected int maxCoolDown = 384;
  
  protected int stepCoolDown = 1;
  
  protected boolean oldVersion = false;
  
  boolean joinedWorld;
  
  public boolean catchingDirty;
  
  public boolean isDirty;
  
  int ptype;
  
  public int upgradeNo(int n) {
    if (ExtraUtils.nodeUpgrade == null)
      return 0; 
    int k = 0;
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      ItemStack item = this.upgrades.getStackInSlot(i);
      if (item != null && this.upgrades.hasUpgradeNo(item) && this.upgrades.getUpgradeNo(item) == n)
        k += (this.upgrades.getStackInSlot(i)).stackSize; 
    } 
    return k;
  }
  
  public void calcUpgradeModifiers() {
    if (this.worldObj == null || this.worldObj.isRemote)
      return; 
    if (this.isReceiver)
      TransferNodeEnderRegistry.clearTileRegistrations(this.buffer); 
    this.isReceiver = false;
    this.stepCoolDown = 2;
    SearchType prevSearchType = this.searchType;
    this.searchType = SearchType.RANDOM_WALK;
    int prev_pipe_type = this.pipe_type;
    if (this.upgrades.isValidPipeType(this.pipe_type))
      this.pipe_type = 0; 
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      if (this.upgrades.getStackInSlot(i) != null && ExtraUtils.nodeUpgrade != null && this.upgrades.getStackInSlot(i).getItem() == ExtraUtils.nodeUpgrade) {
        if (this.upgrades.getStackInSlot(i).getItemDamage() == 0) {
          for (int k = 0; k < (this.upgrades.getStackInSlot(i)).stackSize && 
            this.stepCoolDown < this.maxCoolDown; k++)
            this.stepCoolDown++; 
        } else if (this.upgrades.getStackInSlot(i).getItemDamage() == 6 && this.upgrades.getStackInSlot(i).hasDisplayName()) {
          TransferNodeEnderRegistry.registerTile(new Frequency(this.upgrades.getStackInSlot(i)), this.buffer);
          this.isReceiver = true;
        } else if (this.upgrades.getStackInSlot(i).getItemDamage() == 7) {
          this.searchType = SearchType.DEPTH_FIRST;
        } else if (this.upgrades.getStackInSlot(i).getItemDamage() == 8) {
          this.searchType = SearchType.BREADTH_FIRST;
        } 
      } else if (this.upgrades.pipeType(this.upgrades.getStackInSlot(i)) > 0) {
        this.pipe_type = this.upgrades.pipeType(this.upgrades.getStackInSlot(i));
      } 
    } 
    if (prevSearchType != this.searchType)
      resetSearch(); 
    if (prev_pipe_type != this.pipe_type)
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord); 
  }
  
  public TileEntityTransferNode(String s, INodeBuffer buffer) {
    this.joinedWorld = false;
    this.catchingDirty = false;
    this.isDirty = false;
    this.ptype = 0;
    this.type = s;
    this.buffer = buffer;
  }
  
  public void updateEntity() {
    if (this.worldObj == null || this.worldObj.isRemote)
      return; 
    this.catchingDirty = true;
    if (!this.joinedWorld) {
      this.joinedWorld = true;
      onWorldJoin();
    } 
    processBuffer();
    if (ExtraUtils.nodeUpgrade != null)
      sendEnder(); 
    this.catchingDirty = false;
    if (this.isDirty) {
      super.markDirty();
      this.isDirty = false;
    } 
  }
  
  public void markDirty() {
    if (this.catchingDirty) {
      this.isDirty = true;
    } else {
      super.markDirty();
    } 
  }
  
  public void sendEnder() {
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      ItemStack item = this.upgrades.getStackInSlot(i);
      if (item != null && item.getItem() == ExtraUtils.nodeUpgrade && item.getItemDamage() == 5)
        TransferNodeEnderRegistry.doTransfer(this.buffer, new Frequency(item), item.stackSize); 
    } 
  }
  
  public void updateRedstone() {
    if (this.worldObj == null)
      return; 
    TileEntity tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
    if (tile instanceof INode)
      this.powered = ((INode)tile).recalcRedstone(); 
  }
  
  public boolean recalcRedstone() {
    return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
  }
  
  public boolean isPowered() {
    if (!this.init && this.worldObj != null) {
      this.powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
      this.init = true;
    } 
    return (this.powered == ((upgradeNo(-2) == 0)));
  }
  
  public boolean checkRedstone() {
    if (isPowered()) {
      resetSearch();
      return true;
    } 
    return false;
  }
  
  public int getNodeX() {
    return this.xCoord;
  }
  
  public int getNodeY() {
    return this.yCoord;
  }
  
  public int getNodeZ() {
    return this.zCoord;
  }
  
  public ForgeDirection getNodeDir() {
    return ForgeDirection.getOrientation(getBlockMetadata() % 6);
  }
  
  public int getPipeX() {
    return this.pipe_x;
  }
  
  public int getPipeY() {
    return this.pipe_y;
  }
  
  public int getPipeZ() {
    return this.pipe_z;
  }
  
  public int getPipeDir() {
    return this.pipe_dir;
  }
  
  public List<ItemStack> getUpgrades() {
    List<ItemStack> u = new ArrayList<ItemStack>();
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      if (this.upgrades.getStackInSlot(i) != null)
        u.add(this.upgrades.getStackInSlot(i)); 
    } 
    return u;
  }
  
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    if (tag.hasKey("pipe_x")) {
      this.pipe_x = tag.getInteger("pipe_x");
    } else {
      this.pipe_x = 0;
    } 
    if (tag.hasKey("pipe_y")) {
      this.pipe_y = tag.getInteger("pipe_y");
    } else {
      this.pipe_y = 0;
    } 
    if (tag.hasKey("pipe_z")) {
      this.pipe_z = tag.getInteger("pipe_z");
    } else {
      this.pipe_z = 0;
    } 
    this.pipe_dir = tag.getInteger("pipe_dir");
    if (tag.hasKey("pipe_type")) {
      this.pipe_type = tag.getByte("pipe_type");
    } else {
      this.pipe_type = 0;
    } 
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      if (tag.hasKey("upgrade_" + i)) {
        this.upgrades.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("upgrade_" + i)));
      } else {
        this.upgrades.setInventorySlotContents(i, null);
      } 
    } 
    this.buffer.readFromNBT(tag);
    NBTTagCompound t = tag.getCompoundTag("prevSearch");
    int s = t.getInteger("size");
    int j;
    for (j = 0; j < s; j++)
      this.prevSearch.add(SearchPosition.loadFromTag(t.getCompoundTag(Integer.toString(j)))); 
    t = tag.getCompoundTag("toSearch");
    s = t.getInteger("size");
    for (j = 0; j < s; j++)
      this.toSearch.add(SearchPosition.loadFromTag(t.getCompoundTag(Integer.toString(j)))); 
    if (tag.getByte("version") == 0)
      this.oldVersion = true; 
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    if (this.pipe_x != 0)
      par1NBTTagCompound.setInteger("pipe_x", this.pipe_x); 
    if (this.pipe_y != 0)
      par1NBTTagCompound.setInteger("pipe_y", this.pipe_y); 
    if (this.pipe_z != 0)
      par1NBTTagCompound.setInteger("pipe_z", this.pipe_z); 
    if (this.pipe_dir != 0)
      par1NBTTagCompound.setInteger("pipe_dir", this.pipe_dir); 
    if (this.pipe_type != 0)
      par1NBTTagCompound.setByte("pipe_type", (byte)this.pipe_type); 
    for (int i = 0; i < this.upgrades.getSizeInventory(); i++) {
      if (this.upgrades.getStackInSlot(i) != null) {
        NBTTagCompound newItem = new NBTTagCompound();
        this.upgrades.getStackInSlot(i).writeToNBT(newItem);
        par1NBTTagCompound.setTag("upgrade_" + i, (NBTBase)newItem);
      } 
    } 
    this.buffer.writeToNBT(par1NBTTagCompound);
    NBTTagCompound t2 = new NBTTagCompound();
    t2.setInteger("size", this.prevSearch.size());
    int j = 0;
    for (SearchPosition p : this.prevSearch) {
      t2.setTag(Integer.toString(j), (NBTBase)p.getTag());
      j++;
    } 
    NBTTagCompound t3 = new NBTTagCompound();
    t3.setInteger("size", this.toSearch.size());
    for (j = 0; j < this.toSearch.size(); j++)
      t3.setTag(Integer.toString(j), (NBTBase)((SearchPosition)this.toSearch.get(j)).getTag()); 
    par1NBTTagCompound.setTag("prevSearch", (NBTBase)t2);
    par1NBTTagCompound.setTag("toSearch", (NBTBase)t3);
    if (!this.oldVersion)
      par1NBTTagCompound.setByte("version", (byte)1); 
  }
  
  public int getType() {
    if (this.ptype == 0) {
      this.ptype = -1;
      Class<?> clazz = getClass();
      if (clazz.equals(TileEntityTransferNodeInventory.class)) {
        this.ptype = 3;
      } else if (clazz.equals(TileEntityTransferNodeLiquid.class)) {
        this.ptype = 4;
      } else if (clazz.equals(TileEntityRetrievalNodeInventory.class)) {
        this.ptype = 5;
      } else if (clazz.equals(TileEntityRetrievalNodeLiquid.class)) {
        this.ptype = 6;
      } else if (clazz.equals(TileEntityTransferNodeEnergy.class) || clazz.equals(TileEntityTransferNodeHyperEnergy.class)) {
        this.ptype = 7;
      } 
    } 
    return this.ptype;
  }
  
  public void sendParticleUpdate() {
    if (ExtraUtils.disableNodeParticles || !this.joinedWorld)
      return; 
    if (upgradeNo(-1) == 0)
      return; 
    NetworkHandler.sendParticleEvent(this.worldObj, getType(), this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
  }
  
  public void sendEnderParticleUpdate() {
    if (ExtraUtils.disableNodeParticles || !this.joinedWorld)
      return; 
    if (upgradeNo(-1) == 0)
      return; 
    NetworkHandler.sendParticleEvent(this.worldObj, 8, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
  }
  
  public boolean handleInventories() {
    boolean advance = false;
    boolean rr = (upgradeNo(9) != 0);
    boolean ss = this.buffer.shouldSearch();
    if (ss) {
      IPipe pipe = TNHelper.getPipe((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
      if (pipe != null) {
        sendParticleUpdate();
        advance = pipe.transferItems((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z, ForgeDirection.getOrientation(this.pipe_dir), this.buffer);
      } else {
        resetSearch();
        return false;
      } 
    } 
    if (rr)
      return ss; 
    if (!this.buffer.shouldSearch()) {
      resetSearch();
      return false;
    } 
    return advance;
  }
  
  public boolean advPipeSearch() {
    if (this.pipe_dir == 6)
      this.pipe_dir = getNodeDir().getOpposite().ordinal(); 
    IPipe pipeBlock = TNHelper.getPipe((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
    if (pipeBlock != null) {
      this.prevSearch.add(new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, ForgeDirection.getOrientation(this.pipe_dir)));
      ArrayList<ForgeDirection> dirs = pipeBlock.getOutputDirections((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z, ForgeDirection.getOrientation(this.pipe_dir), this.buffer);
      switch (this.searchType) {
        case RANDOM_WALK:
          if (!dirs.isEmpty())
            this.toSearch.add((new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, dirs.get(0))).adv()); 
          break;
        case DEPTH_FIRST:
          for (ForgeDirection d : dirs) {
            SearchPosition s = (new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, d)).adv();
            if (!this.prevSearch.contains(s) && !this.toSearch.contains(s)) {
              if (this.toSearch.isEmpty()) {
                this.toSearch.add((new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, d)).adv());
                continue;
              } 
              this.toSearch.add(0, (new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, d)).adv());
            } 
          } 
          break;
        case BREADTH_FIRST:
          for (ForgeDirection d : dirs) {
            SearchPosition s = (new SearchPosition(this.pipe_x, this.pipe_y, this.pipe_z, d)).adv();
            if (!this.prevSearch.contains(s) && !this.toSearch.contains(s))
              this.toSearch.add(s); 
          } 
          break;
      } 
    } 
    if (!loadNextPos()) {
      this.pipe_dir = getNodeDir().getOpposite().ordinal();
      resetSearch();
      return false;
    } 
    return true;
  }
  
  public void resetSearch() {
    if (this.pipe_x != 0 || this.pipe_y != 0 || this.pipe_z != 0 || !this.prevSearch.isEmpty()) {
      this.pipe_x = 0;
      this.pipe_y = 0;
      this.pipe_z = 0;
      this.pipe_dir = -1;
      this.toSearch.clear();
      this.prevSearch.clear();
    } 
  }
  
  public boolean loadNextPos() {
    if (this.toSearch.isEmpty())
      return false; 
    SearchPosition pos = this.toSearch.remove(0);
    if (this.searchType != SearchType.RANDOM_WALK) {
      while (this.prevSearch.contains(pos) && !this.toSearch.isEmpty())
        pos = this.toSearch.remove(0); 
      if (this.prevSearch.contains(pos))
        return false; 
      this.prevSearch.add(pos.copy());
    } 
    this.pipe_x = pos.x;
    this.pipe_y = pos.y;
    this.pipe_z = pos.z;
    this.pipe_dir = pos.side.ordinal();
    return true;
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound t = new NBTTagCompound();
    t.setByte("d", (byte)this.pipe_type);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
  }
  
  @SideOnly(Side.CLIENT)
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    if (!this.worldObj.isRemote)
      return; 
    if (pkt.func_148857_g().hasKey("d")) {
      if (pkt.func_148857_g().getByte("d") != this.pipe_type)
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord); 
      this.pipe_type = pkt.func_148857_g().getByte("d");
    } 
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return true;
  }
  
  public void invalidate() {
    super.invalidate();
    onWorldLeave();
  }
  
  public void onChunkLoad() {}
  
  public void onWorldJoin() {
    this.buffer.setNode(this);
    calcUpgradeModifiers();
  }
  
  public void onWorldLeave() {
    TransferNodeEnderRegistry.clearTileRegistrations(this.buffer);
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.pipe_type).getOutputDirections(world, x, y, z, dir, buffer);
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.pipe_type).transferItems(world, x, y, z, dir, buffer);
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (dir != getNodeDir() && StdPipes.getPipeType(this.pipe_type).canInput(world, x, y, z, dir));
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (dir != getNodeDir() && StdPipes.getPipeType(this.pipe_type).canOutput(world, x, y, z, dir));
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.pipe_type).limitTransfer(dest, side, buffer);
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return null;
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (dir != getNodeDir() && StdPipes.getPipeType(this.pipe_type).shouldConnectToTile(world, x, y, z, dir));
  }
  
  public IIcon baseTexture() {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.pipe_type)).baseTexture();
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.pipe_type)).pipeTexture(dir, blocked);
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.pipe_type)).invPipeTexture(dir);
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.pipe_type)).socketTexture(dir);
  }
  
  public String getPipeType() {
    return StdPipes.getPipeType(this.pipe_type).getPipeType();
  }
  
  public float baseSize() {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.pipe_type)).baseSize();
  }
  
  public TileEntityTransferNode getNode() {
    return this;
  }
  
  public String getNodeType() {
    return this.type;
  }
  
  public void update() {}
  
  public boolean initDirection() {
    return false;
  }
  
  public enum SearchType {
    RANDOM_WALK, DEPTH_FIRST, BREADTH_FIRST;
  }
  
  public void bufferChanged() {
    markDirty();
  }
  
  public void onChunkUnload() {
    super.onChunkUnload();
    onWorldLeave();
  }
  
  public abstract void processBuffer();
  
  public static class SearchPosition {
    public ForgeDirection side = ForgeDirection.UNKNOWN;
    
    public int x;
    
    public int y;
    
    public int z;
    
    public SearchPosition(int par1, int par2, int par3, ForgeDirection par4) {
      this.x = par1;
      this.y = par2;
      this.z = par3;
      this.side = par4;
    }
    
    public static byte getOrd(ForgeDirection e) {
      return (byte)e.ordinal();
    }
    
    public static SearchPosition loadFromTag(NBTTagCompound tag) {
      return new SearchPosition(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"), ForgeDirection.getOrientation(tag.getByte("side")));
    }
    
    public SearchPosition copy() {
      return new SearchPosition(this.x, this.y, this.z, this.side);
    }
    
    public String toString() {
      return "SearchPosition{side=" + this.side + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
    
    public SearchPosition adv() {
      this.x += this.side.offsetX;
      this.y += this.side.offsetY;
      this.z += this.side.offsetZ;
      return this;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true; 
      if (o == null || getClass() != o.getClass())
        return false; 
      SearchPosition that = (SearchPosition)o;
      if (effectiveX() != that.effectiveX())
        return false; 
      return (effectiveY() == that.effectiveY() && effectiveZ() == that.effectiveZ());
    }
    
    public int effectiveX() {
      return this.x * 2 - this.side.offsetX;
    }
    
    public int effectiveY() {
      return this.y * 2 - this.side.offsetY;
    }
    
    public int effectiveZ() {
      return this.z * 2 - this.side.offsetZ;
    }
    
    public int hashCode() {
      int result = effectiveX();
      result = 31 * result + effectiveY();
      result = 31 * result + effectiveZ();
      return result;
    }
    
    public NBTTagCompound getTag() {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setInteger("x", this.x);
      tag.setInteger("y", this.y);
      tag.setInteger("z", this.z);
      tag.setByte("side", (byte)this.side.ordinal());
      return tag;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */