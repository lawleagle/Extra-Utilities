package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.RedstoneInteractions;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.scalatraits.TRedstoneTile;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import scala.util.Random;

public abstract class TransferNodePart extends MCMetaTilePart implements INode, IPipeCosmetic, ISidedHollowConnect, TSlottedPart, TFacePart {
  public static Random rand = new Random();
  
  private static DummyPipePart[] dummyPipes = new DummyPipePart[6];
  
  public TileEntityTransferNode node;
  
  static {
    for (int i = 0; i < 6; i++)
      dummyPipes[i] = new DummyPipePart(i, 0.625F); 
  }
  
  public boolean valid = true;
  
  public boolean init = false;
  
  public int blockMasks = -1;
  
  public byte[] flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
  
  int id = rand.nextInt();
  
  public TransferNodePart(int meta, TileEntityTransferNode node) {
    super(meta);
    this.node = node;
    node.blockMetadata = meta;
  }
  
  public TransferNodePart(TileEntityTransferNode node) {
    this.node = node;
    node.blockMetadata = this.meta;
  }
  
  public TileEntity getBlockTile() {
    return (TileEntity)tile();
  }
  
  public int getHollowSize(int side) {
    return 6;
  }
  
  public Iterable<ItemStack> getDrops() {
    return Arrays.asList(new ItemStack[] { new ItemStack(getBlock(), 1, getBlock().damageDropped(this.meta)) });
  }
  
  public ItemStack pickItem(MovingObjectPosition hit) {
    return new ItemStack(getBlock(), 1, getBlock().damageDropped(getMetadata()));
  }
  
  public void bufferChanged() {
    getNode().bufferChanged();
  }
  
  public boolean activate(EntityPlayer player, MovingObjectPosition part, ItemStack item) {
    if ((getWorld()).isRemote)
      return true; 
    if (XUHelper.isWrench(item)) {
      int newmetadata = StdPipes.getNextPipeType((IBlockAccess)getWorld(), part.blockX, part.blockY, part.blockZ, (getNode()).pipe_type);
      (getNode()).pipe_type = (byte)newmetadata;
      sendDescUpdate();
      return true;
    } 
    player.openGui(ExtraUtilsMod.instance, 0, getWorld(), x(), y(), z());
    return true;
  }
  
  public void onRemoved() {
    if (!(getWorld()).isRemote) {
      List<ItemStack> drops = new ArrayList<ItemStack>();
      for (int i = 0; i < this.node.upgrades.getSizeInventory(); i++) {
        if (this.node.upgrades.getStackInSlot(i) != null)
          drops.add(this.node.upgrades.getStackInSlot(i)); 
      } 
      tile().dropItems(drops);
    } 
  }
  
  public void onWorldJoin() {
    if (getWorld() != null)
      this.node.setWorldObj(getWorld()); 
    this.node.xCoord = x();
    this.node.yCoord = y();
    this.node.zCoord = z();
    this.node.onWorldJoin();
    reloadBlockMasks();
  }
  
  public void onWorldSeparate() {
    this.node.invalidate();
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    ArrayList<AxisAlignedBB> t = new ArrayList<AxisAlignedBB>();
    ArrayList<Cuboid6> t2 = new ArrayList<Cuboid6>();
    ExtraUtils.transferNode.addCollisionBoxesToList(getWorld(), x(), y(), z(), AxisAlignedBB.getBoundingBox(x(), y(), z(), (x() + 1), (y() + 1), (z() + 1)), t, null);
    for (AxisAlignedBB aT : t)
      t2.add((new Cuboid6(aT.minX, aT.minY, aT.minZ, aT.maxX, aT.maxY, aT.maxZ)).sub(new Vector3(x(), y(), z()))); 
    return t2;
  }
  
  public void save(NBTTagCompound tag) {
    super.save(tag);
    NBTTagCompound subtag = new NBTTagCompound();
    this.node.writeToNBT(subtag);
    tag.setTag("node", (NBTBase)subtag);
  }
  
  public void load(NBTTagCompound tag) {
    super.load(tag);
    this.node.readFromNBT(tag.getCompoundTag("node"));
  }
  
  public boolean doesTick() {
    return true;
  }
  
  public void update() {
    if (this.node != null && !(world()).isRemote) {
      this.node.blockMetadata = this.meta;
      if (getWorld().getTileEntity(x(), y(), z()) == tile()) {
        if (this.node.getWorldObj() == null)
          onWorldJoin(); 
        this.node.updateEntity();
      } 
    } 
  }
  
  public void writeDesc(MCDataOutput packet) {
    packet.writeByte(this.meta);
    packet.writeByte(this.node.pipe_type);
  }
  
  public void readDesc(MCDataInput packet) {
    this.meta = packet.readByte();
    this.node.pipe_type = packet.readByte();
  }
  
  public Block getBlock() {
    return ExtraUtils.transferNode;
  }
  
  public int getNodeX() {
    return this.node.getNodeX();
  }
  
  public int getNodeY() {
    return this.node.getNodeY();
  }
  
  public int getNodeZ() {
    return this.node.getNodeZ();
  }
  
  public ForgeDirection getNodeDir() {
    this.node.blockMetadata = this.meta;
    return this.node.getNodeDir();
  }
  
  public int getPipeX() {
    return this.node.getPipeX();
  }
  
  public int getPipeY() {
    return this.node.getPipeY();
  }
  
  public int getPipeZ() {
    return this.node.getPipeZ();
  }
  
  public int getPipeDir() {
    return this.node.getPipeDir();
  }
  
  public List<ItemStack> getUpgrades() {
    return this.node.getUpgrades();
  }
  
  public boolean checkRedstone() {
    return this.node.checkRedstone();
  }
  
  public BoxModel getModel(ForgeDirection dir) {
    return this.node.getModel(dir);
  }
  
  public String getNodeType() {
    return this.node.getNodeType();
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return getNode().transferItems(world, x, y, z, dir, buffer);
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && getNode().canInput(world, x, y, z, dir));
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return getNode().getOutputDirections(world, x, y, z, dir, buffer);
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && getNode().canOutput(world, x, y, z, dir));
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return getNode().limitTransfer(dest, side, buffer);
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return getNode().getFilterInventory(world, x, y, z);
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && getNode().shouldConnectToTile(world, x, y, z, dir));
  }
  
  public void reloadBlockMasks() {
    this.blockMasks = 0;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      (dummyPipes[dir.ordinal()]).h = 0.5F + baseSize();
      if (dir == getNodeDir()) {
        this.blockMasks |= this.flagmasks[dir.ordinal()];
      } else if (!tile().canAddPart((TMultiPart)dummyPipes[dir.ordinal()])) {
        this.blockMasks |= this.flagmasks[dir.ordinal()];
      } 
    } 
  }
  
  public void onPartChanged(TMultiPart part) {
    reloadBlockMasks();
  }
  
  public void onNeighborChanged() {
    this.node.updateRedstone();
    reloadBlockMasks();
  }
  
  public boolean isPowered() {
    return this.node.isPowered();
  }
  
  public boolean recalcRedstone() {
    if (tile() instanceof TRedstoneTile) {
      TRedstoneTile rsT = (TRedstoneTile)tile();
      for (int i = 0; i < 6; i++) {
        if (rsT.weakPowerLevel(i) > 0)
          return true; 
      } 
    } 
    for (int side = 0; side < 6; side++) {
      if (RedstoneInteractions.getPowerTo(world(), x(), y(), z(), side, 31) > 0)
        return true; 
    } 
    return false;
  }
  
  public boolean isBlocked(ForgeDirection dir) {
    if (this.node.getWorldObj() == null)
      onWorldJoin(); 
    if (this.blockMasks < 0)
      reloadBlockMasks(); 
    return ((this.blockMasks & this.flagmasks[dir.ordinal()]) == this.flagmasks[dir.ordinal()]);
  }
  
  public IIcon baseTexture() {
    return getNode().baseTexture();
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return getNode().pipeTexture(dir, blocked);
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return getNode().invPipeTexture(dir);
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return getNode().socketTexture(dir);
  }
  
  public String getPipeType() {
    return getNode().getPipeType();
  }
  
  public float baseSize() {
    return getNode().baseSize();
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return (npart instanceof DummyPipePart || super.occlusionTest(npart));
  }
  
  public final Cuboid6 getBounds() {
    Box bounds = ((BlockTransferNode)getBlock()).getWorldModel((IBlockAccess)getWorld(), x(), y(), z()).boundingBox();
    return new Cuboid6(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
  }
  
  public final HashSet<IndexedCuboid6> getSubParts() {
    HashSet<IndexedCuboid6> boxes = new HashSet<IndexedCuboid6>();
    for (Box bounds : ((BlockTransferNode)getBlock()).getWorldModel((IBlockAccess)getWorld(), x(), y(), z()))
      boxes.add(new IndexedCuboid6(Integer.valueOf(0), new Cuboid6(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ))); 
    return boxes;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame) {
    GL11.glEnable(3042);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
    GL11.glLineWidth(2.0F);
    GL11.glDisable(3553);
    GL11.glDepthMask(false);
    float f1 = 0.002F;
    double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * frame;
    double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * frame;
    double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * frame;
    RenderGlobal.drawOutlinedBoundingBox(getBounds().add(new Vector3(x(), y(), z())).toAABB().expand(f1, f1, f1).getOffsetBoundingBox(-d0, -d1, -d2), -1);
    GL11.glDepthMask(true);
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    return true;
  }
  
  public int getSlotMask() {
    if (getNode().getNodeDir() == ForgeDirection.UNKNOWN)
      return 64; 
    return 0x40 | 1 << getNode().getNodeDir().ordinal();
  }
  
  public int redstoneConductionMap() {
    return 0;
  }
  
  public boolean solid(int arg0) {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */