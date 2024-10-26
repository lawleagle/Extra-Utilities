package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class PipePart extends MCMetaTilePart implements ISidedHollowConnect, IPipe, IPipeCosmetic, INeighborTileChange, TSlottedPart {
  public static DummyPipePart[] dummyPipes = new DummyPipePart[6];
  
  static {
    for (int i = 0; i < 6; i++)
      dummyPipes[i] = new DummyPipePart(i, 0.375F); 
  }
  
  public int blockMasks = -1;
  
  public byte[] flagmasks = new byte[] { 1, 2, 4, 8, 16, 32 };
  
  public PipePart(int meta) {
    super(meta);
  }
  
  public Iterable<ItemStack> getDrops() {
    return Arrays.asList(new ItemStack[] { new ItemStack(getBlock(), 1, getBlock().damageDropped(getMetadata())) });
  }
  
  public ItemStack pickItem(MovingObjectPosition hit) {
    return new ItemStack(getBlock(), 1, getBlock().damageDropped(getMetadata()));
  }
  
  public int getMetadata() {
    return this.meta % 16;
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    return Arrays.asList(new Cuboid6[] { new Cuboid6((0.5F - baseSize()), (0.5F - baseSize()), (0.5F - baseSize()), (0.5F + baseSize()), (0.5F + baseSize()), (0.5F + baseSize())) });
  }
  
  public boolean activate(EntityPlayer player, MovingObjectPosition part, ItemStack item) {
    if ((getWorld()).isRemote)
      return true; 
    if (XUHelper.isWrench(item)) {
      int newmetadata = StdPipes.getNextPipeType((IBlockAccess)getWorld(), part.blockX, part.blockY, part.blockZ, this.meta);
      this.meta = (byte)newmetadata;
      sendDescUpdate();
      return true;
    } 
    return false;
  }
  
  public final Cuboid6 getBounds() {
    Box bounds = ((BlockTransferPipe)getBlock()).getWorldModel((IBlockAccess)getWorld(), x(), y(), z()).boundingBox();
    return new Cuboid6(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
  }
  
  public final HashSet<IndexedCuboid6> getSubParts() {
    HashSet<IndexedCuboid6> boxes = new HashSet<IndexedCuboid6>();
    for (Box bounds : ((BlockTransferPipe)getBlock()).getWorldModel((IBlockAccess)getWorld(), x(), y(), z()))
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
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    ArrayList<Cuboid6> t2 = new ArrayList<Cuboid6>();
    BoxModel model = ((BlockTransferPipe)getBlock()).getWorldModel((IBlockAccess)world(), x(), y(), z());
    for (Box box : model)
      t2.add(new Cuboid6(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)); 
    return t2;
  }
  
  public Block getBlock() {
    return (this.meta < 16) ? (Block)ExtraUtils.transferPipe : (Block)ExtraUtils.transferPipe2;
  }
  
  public String getType() {
    return "extrautils:transfer_pipe";
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.meta).getOutputDirections(world, x, y, z, dir, buffer);
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.meta).transferItems(world, x, y, z, dir, buffer);
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && StdPipes.getPipeType(this.meta).canInput(world, x, y, z, dir));
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && StdPipes.getPipeType(this.meta).canOutput((IBlockAccess)getWorld(), x, y, z, dir));
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return StdPipes.getPipeType(this.meta).limitTransfer(dest, side, buffer);
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return null;
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (!isBlocked(dir) && StdPipes.getPipeType(this.meta).shouldConnectToTile(world, x, y, z, dir));
  }
  
  public void reloadBlockMasks() {
    this.blockMasks = 0;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      (dummyPipes[dir.ordinal()]).h = 0.5F - baseSize();
      if (!tile().canAddPart((TMultiPart)dummyPipes[dir.ordinal()]))
        this.blockMasks |= this.flagmasks[dir.ordinal()]; 
    } 
  }
  
  public void onPartChanged(TMultiPart part) {
    reloadBlockMasks();
  }
  
  public void onNeighborChanged() {
    reloadBlockMasks();
  }
  
  public boolean isBlocked(ForgeDirection dir) {
    if (this.blockMasks < 0)
      reloadBlockMasks(); 
    return ((this.blockMasks & this.flagmasks[dir.ordinal()]) == this.flagmasks[dir.ordinal()]);
  }
  
  public IIcon baseTexture() {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).baseTexture();
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).socketTexture(dir);
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).pipeTexture(dir, blocked);
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).invPipeTexture(dir);
  }
  
  public String getPipeType() {
    return StdPipes.getPipeType(this.meta).getPipeType();
  }
  
  public float baseSize() {
    return ((IPipeCosmetic)StdPipes.getPipeType(this.meta)).baseSize();
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return (DummyPipePart.class.equals(npart.getClass()) || super.occlusionTest(npart));
  }
  
  public void onNeighborTileChanged(int arg0, boolean arg1) {
    reloadBlockMasks();
  }
  
  public boolean weakTileChanges() {
    return true;
  }
  
  public int getSlotMask() {
    return 64;
  }
  
  public int getHollowSize(int side) {
    return 6;
  }
  
  public TileEntity getBlockTile() {
    return (TileEntity)tile();
  }
  
  public PipePart() {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\PipePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */