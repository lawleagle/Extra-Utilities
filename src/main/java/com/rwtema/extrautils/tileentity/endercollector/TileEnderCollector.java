package com.rwtema.extrautils.tileentity.endercollector;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketParticleCurve;
import com.rwtema.extrautils.network.packets.PacketParticleLine;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Facing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnderCollector extends TileEntity {
  int range = 8;
  
  AxisAlignedBB bounds;
  
  boolean isStuffed = false;
  
  boolean init = false;
  
  LinkedList<ItemStack> items;
  
  public void setRange(int r) {
    this.range = r;
    this.bounds = AxisAlignedBB.getBoundingBox((this.xCoord - r / 2.0F), (this.yCoord - r / 2.0F), (this.zCoord - r / 2.0F), (this.xCoord + r / 2.0F + 1.0F), (this.yCoord + r / 2.0F + 1.0F), (this.zCoord + r / 2.0F + 1.0F));
  }
  
  public TileEnderCollector() {
    this.items = new LinkedList<ItemStack>();
    this.rand = (Random)XURandom.getInstance();
  }
  
  public void updateEntity() {
    if (this.worldObj.isRemote)
      return; 
    this.init = true;
    if (!this.isStuffed && this.worldObj.getTotalWorldTime() % 40L == 0L) {
      if (this.bounds == null)
        setRange(this.range); 
      List<EntityItem> entitiesWithinAABB = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.bounds);
      for (EntityItem entityItem : entitiesWithinAABB)
        grabEntity(entityItem); 
    } 
    if (!this.items.isEmpty()) {
      if (this.isStuffed && this.worldObj.getTotalWorldTime() % 10L != 0L)
        return; 
      int side = getBlockMetadata() % 6;
      ListIterator<ItemStack> iter = this.items.listIterator();
      TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side]);
      if (tileEntity instanceof IInventory) {
        IInventory inventory = (IInventory)tileEntity;
        while (iter.hasNext()) {
          ItemStack next = iter.next();
          next = XUHelper.invInsert(inventory, next.copy(), side ^ 0x1);
          if (next != null) {
            iter.set(next);
            continue;
          } 
          iter.remove();
        } 
      } 
    } 
    this.isStuffed = !this.items.isEmpty();
    updateMeta();
  }
  
  public void updateMeta() {
    int oldMeta = getBlockMetadata();
    int newMeta = oldMeta % 6 + (this.isStuffed ? 6 : 0);
    if (newMeta != oldMeta)
      changeMeta(newMeta); 
  }
  
  public void changeMeta(int newMeta) {
    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, newMeta, 2);
  }
  
  public boolean inRange(Entity entity) {
    if (this.isStuffed || !this.init || this.tileEntityInvalid)
      return false; 
    if (this.bounds == null)
      setRange(this.range); 
    return this.bounds.intersectsWith(entity.boundingBox);
  }
  
  public void invalidate() {
    super.invalidate();
    CollectorHandler.unregister(this);
  }
  
  public void onChunkUnload() {
    CollectorHandler.unregister(this);
  }
  
  public void setWorldObj(World p_145834_1_) {
    super.setWorldObj(p_145834_1_);
    if (!this.worldObj.isRemote)
      CollectorHandler.register(this); 
  }
  
  public void grabEntity(EntityItem entity) {
    if (entity.isDead)
      return; 
    int side = getBlockMetadata() % 6;
    if (this.worldObj.isAirBlock(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side]))
      return; 
    TileEntity tileEntity = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[side], this.yCoord + Facing.offsetsYForSide[side], this.zCoord + Facing.offsetsZForSide[side]);
    if (!(tileEntity instanceof IInventory))
      return; 
    IInventory inv = (IInventory)tileEntity;
    ItemStack stack = entity.getDataWatcher().getWatchableObjectItemStack(10);
    if (stack == null)
      return; 
    if (this.filter != null && !ItemNodeUpgrade.matchesFilterItem(stack, this.filter))
      return; 
    ItemStack itemStack = XUHelper.simInvInsert(inv, stack, side ^ 0x1);
    if (itemStack != null && itemStack.stackSize == stack.stackSize)
      return; 
    this.items.add(stack);
    signalChange(entity);
    entity.setEntityItemStack(null);
    entity.setDead();
  }
  
  private static final Vec3[] sides = new Vec3[] { Vec3.createVectorHelper(0.0D, -1.0D, 0.0D), Vec3.createVectorHelper(0.0D, 1.0D, 0.0D), Vec3.createVectorHelper(0.0D, 0.0D, -1.0D), Vec3.createVectorHelper(0.0D, 0.0D, 1.0D), Vec3.createVectorHelper(-1.0D, 0.0D, 0.0D), Vec3.createVectorHelper(1.0D, 0.0D, 0.0D) };
  
  ItemStack filter;
  
  private Random rand;
  
  public void signalChange(EntityItem item) {
    int side = getBlockMetadata() % 6;
    NetworkHandler.sendToAllAround((XUPacketBase)new PacketParticleCurve(item, Vec3.createVectorHelper(this.xCoord + 0.5D - (ForgeDirection.getOrientation(side)).offsetX * 0.4D, this.yCoord + 0.5D - (ForgeDirection.getOrientation(side)).offsetY * 0.4D, this.zCoord + 0.5D - (ForgeDirection.getOrientation(side)).offsetZ * 0.4D), sides[side]), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0D);
  }
  
  public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
    return (newBlock != oldBlock);
  }
  
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    this.range = tag.getByte("Range");
    NBTTagList tagList = tag.getTagList("Items", XUHelper.NBTIds.NBT.id);
    for (int i = 0; i < tagList.tagCount(); i++) {
      ItemStack itemStack = ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(i));
      if (itemStack != null)
        this.items.add(itemStack); 
    } 
    if (tag.hasKey("Filter"))
      this.filter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Filter")); 
  }
  
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    NBTTagList list = new NBTTagList();
    for (ItemStack item : this.items)
      list.appendTag((NBTBase)item.writeToNBT(new NBTTagCompound())); 
    tag.setTag("Items", (NBTBase)list);
    tag.setByte("Range", (byte)this.range);
    if (this.filter != null)
      tag.setTag("Filter", (NBTBase)this.filter.writeToNBT(new NBTTagCompound())); 
  }
  
  public void onNeighbourChange() {}
  
  public void drawLine(Vec3 a, Vec3 b) {
    NetworkHandler.sendToAllAround((XUPacketBase)new PacketParticleLine(a, b), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0D);
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    if (world.isRemote)
      return true; 
    if (this.isStuffed) {
      dropItems();
      return true;
    } 
    ItemStack heldItem = player.getHeldItem();
    if (heldItem != null) {
      if (this.filter == null && ItemNodeUpgrade.isFilter(heldItem)) {
        this.filter = heldItem.copy();
        heldItem.stackSize = 0;
        this.worldObj.markBlockForUpdate(x, y, z);
        return true;
      } 
      if (this.filter != null && XUHelper.isWrench(heldItem)) {
        try {
          CollectorHandler.dontCollect = true;
          dropItem(this.filter.copy());
        } finally {
          CollectorHandler.dontCollect = false;
        } 
        this.filter = null;
        this.worldObj.markBlockForUpdate(x, y, z);
        return true;
      } 
    } 
    if (!player.isSneaking()) {
      this.range++;
      if (this.range > 8)
        this.range = 1; 
    } else {
      this.range--;
      if (this.range < 1)
        this.range = 8; 
    } 
    setRange(this.range);
    drawCube(this.bounds);
    PacketTempChat.sendChat(player, (IChatComponent)new ChatComponentText(String.format("Range: %s  (%s, %s, %s -> %s, %s, %s)", new Object[] { Float.valueOf(this.range / 2.0F), Double.valueOf(this.bounds.minX), Double.valueOf(this.bounds.minY), Double.valueOf(this.bounds.minZ), Double.valueOf(this.bounds.maxX), Double.valueOf(this.bounds.maxY), Double.valueOf(this.bounds.maxZ) })));
    return true;
  }
  
  public void drawCube(AxisAlignedBB b) {
    double x0 = b.minX, x1 = b.maxX;
    double y0 = b.minY, y1 = b.maxY;
    double z0 = b.minZ, z1 = b.maxZ;
    Vec3 p000 = Vec3.createVectorHelper(x0, y0, z0);
    Vec3 p001 = Vec3.createVectorHelper(x0, y0, z1);
    Vec3 p010 = Vec3.createVectorHelper(x0, y1, z0);
    Vec3 p011 = Vec3.createVectorHelper(x0, y1, z1);
    Vec3 p100 = Vec3.createVectorHelper(x1, y0, z0);
    Vec3 p101 = Vec3.createVectorHelper(x1, y0, z1);
    Vec3 p110 = Vec3.createVectorHelper(x1, y1, z0);
    Vec3 p111 = Vec3.createVectorHelper(x1, y1, z1);
    Vec3 center = Vec3.createVectorHelper((x0 + x1) / 2.0D, (y0 + y1) / 2.0D, (z0 + z1) / 2.0D);
    drawLine(p000, p001);
    drawLine(p000, p010);
    drawLine(p000, p100);
    drawLine(p001, p011);
    drawLine(p001, p101);
    drawLine(p010, p011);
    drawLine(p010, p110);
    drawLine(p100, p101);
    drawLine(p100, p110);
    drawLine(p011, p111);
    drawLine(p101, p111);
    drawLine(p110, p111);
    drawLine(center, p000);
    drawLine(center, p001);
    drawLine(center, p010);
    drawLine(center, p011);
    drawLine(center, p100);
    drawLine(center, p101);
    drawLine(center, p110);
    drawLine(center, p111);
  }
  
  public void dropItems() {
    try {
      CollectorHandler.dontCollect = true;
      for (ItemStack itemstack : this.items)
        dropItem(itemstack); 
    } finally {
      CollectorHandler.dontCollect = false;
    } 
    this.items.clear();
    this.isStuffed = false;
  }
  
  public void dropItem(ItemStack itemstack) {
    float dx = this.rand.nextFloat() * 0.8F + 0.1F;
    float dy = this.rand.nextFloat() * 0.8F + 0.1F;
    float dz = this.rand.nextFloat() * 0.8F + 0.1F;
    while (itemstack.stackSize > 0) {
      int j1 = this.rand.nextInt(21) + 10;
      if (j1 > itemstack.stackSize)
        j1 = itemstack.stackSize; 
      itemstack.stackSize -= j1;
      EntityItem entityitem = new EntityItem(this.worldObj, (this.xCoord + dx), (this.yCoord + dy), (this.zCoord + dz), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
      if (itemstack.hasTagCompound())
        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy()); 
      entityitem.motionX = this.rand.nextGaussian() * 0.05000000074505806D;
      entityitem.motionY = this.rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
      entityitem.motionZ = this.rand.nextGaussian() * 0.05000000074505806D;
      this.worldObj.spawnEntityInWorld((Entity)entityitem);
    } 
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound t = new NBTTagCompound();
    if (this.filter != null)
      t.setTag("Filter", (NBTBase)this.filter.writeToNBT(new NBTTagCompound())); 
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
  }
  
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    super.onDataPacket(net, pkt);
    NBTTagCompound tag = pkt.func_148857_g();
    if (tag.hasKey("Filter")) {
      this.filter = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Filter"));
    } else {
      this.filter = null;
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\endercollector\TileEnderCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */