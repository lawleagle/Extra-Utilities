package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.inventory.LiquidInventory;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.ItemBuffer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityTransferNodeInventory extends TileEntityTransferNode implements INodeInventory, ISidedInventory {
  private static final int[] contents = new int[] { 0 };
  
  private static final int[] nullcontents = new int[0];
  
  private static InvCrafting crafting = new InvCrafting(3, 3);
  
  private static ForgeDirection[] orthY = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.NORTH, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UP, ForgeDirection.UNKNOWN };
  
  private static ForgeDirection[] orthX = new ForgeDirection[] { ForgeDirection.WEST, ForgeDirection.WEST, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN };
  
  private boolean hasCStoneGen = false;
  
  private int genCStoneCounter = 0;
  
  private long checkTimer = 0L;
  
  private IRecipe cachedRecipe = null;
  
  private int prevStack = 0;
  
  private boolean delay;
  
  private boolean isDirty;
  
  public TileEntityTransferNodeInventory() {
    super("Inv", (INodeBuffer)new ItemBuffer());
    this.delay = false;
    this.isDirty = false;
  }
  
  public TileEntityTransferNodeInventory(String txt, INodeBuffer buffer) {
    super(txt, buffer);
    this.delay = false;
    this.isDirty = false;
  }
  
  public static IRecipe findMatchingRecipe(InventoryCrafting inv, World world) {
    for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++) {
      IRecipe recipe = CraftingManager.getInstance().getRecipeList().get(i);
      if (recipe.matches(inv, world))
        return recipe; 
    } 
    return null;
  }
  
  private static int getFirstExtractableItemStackSlot(IInventory inv, int side) {
    for (int i : XUHelper.getInventorySideSlots(inv, side)) {
      ItemStack item = inv.getStackInSlot(i);
      if (item != null && item.stackSize > 0 && (!(inv instanceof ISidedInventory) || ((ISidedInventory)inv).canExtractItem(i, item, side)))
        if (item.getItem().hasContainerItem(item)) {
          ItemStack t = item.getItem().getContainerItem(item);
          for (int j : XUHelper.getInventorySideSlots(inv, side)) {
            if (((j != i && inv.getStackInSlot(j) == null) || (j == i && item.stackSize == 1)) && inv.isItemValidForSlot(j, t) && (!(inv instanceof ISidedInventory) || ((ISidedInventory)inv).canInsertItem(i, t, side)))
              return i; 
          } 
        } else {
          return i;
        }  
    } 
    return -1;
  }
  
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
  }
  
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
  }
  
  public void processBuffer() {
    if (this.worldObj != null && !this.worldObj.isRemote) {
      if (this.coolDown > 0)
        this.coolDown -= this.stepCoolDown; 
      if (checkRedstone())
        return; 
      startDelayMarkDirty();
      while (this.coolDown <= 0) {
        this.coolDown += baseMaxCoolDown;
        if (handleInventories())
          advPipeSearch(); 
        loadbuffer();
      } 
      finishMarkDirty();
    } 
  }
  
  public void loadbuffer() {
    if (this.buffer.getBuffer() != null && ((ItemStack)this.buffer.getBuffer()).stackSize >= ((ItemStack)this.buffer.getBuffer()).getMaxStackSize())
      return; 
    int dir = getBlockMetadata() % 6;
    IInventory inv = TNHelper.getInventory(this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]));
    if (inv != null) {
      if (inv instanceof ISidedInventory) {
        dir = Facing.oppositeSide[dir];
        ISidedInventory invs = (ISidedInventory)inv;
        int[] aint = invs.getAccessibleSlotsFromSide(dir);
        int i = 0;
        for (; i < aint.length && (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()); i++) {
          ItemStack itemstack = invs.getStackInSlot(aint[i]);
          if (itemstack != null && itemstack.stackSize > 0 && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack, false, true)) && invs.canExtractItem(aint[i], itemstack, dir)) {
            ItemStack itemstack2, itemstack1 = itemstack.copy();
            if (upgradeNo(3) == 0) {
              itemstack2 = XUHelper.invInsert((IInventory)this, invs.decrStackSize(aint[i], 1), -1);
            } else {
              itemstack2 = XUHelper.invInsert((IInventory)this, invs.getStackInSlot(aint[i]), -1);
            } 
            if (upgradeNo(3) == 0) {
              if (itemstack2 != null) {
                inv.setInventorySlotContents(aint[i], itemstack1);
              } else {
                inv.markDirty();
                return;
              } 
            } else {
              inv.setInventorySlotContents(aint[i], itemstack2);
            } 
            inv.markDirty();
          } 
        } 
      } else {
        int j = inv.getSizeInventory();
        for (int k = 0; k < j && (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()); k++) {
          ItemStack itemstack = inv.getStackInSlot(k);
          if (itemstack != null && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack, false, true))) {
            ItemStack itemstack2, itemstack1 = itemstack.copy();
            if (upgradeNo(3) == 0) {
              itemstack2 = XUHelper.invInsert((IInventory)this, inv.decrStackSize(k, 1), -1);
            } else {
              itemstack2 = XUHelper.invInsert((IInventory)this, inv.getStackInSlot(k), -1);
            } 
            if (itemstack2 != null && itemstack2.stackSize == 0)
              itemstack2 = null; 
            if (upgradeNo(3) == 0) {
              if (itemstack2 != null) {
                inv.setInventorySlotContents(k, itemstack1);
              } else {
                inv.markDirty();
                return;
              } 
            } else {
              inv.setInventorySlotContents(k, itemstack2);
            } 
            inv.markDirty();
          } 
        } 
      } 
    } else if (upgradeNo(2) > 0) {
      if (genCobble())
        return; 
      if (doCraft())
        return; 
      suckItems();
    } 
  }
  
  public void startDelayMarkDirty() {
    if (this.delay)
      throw new RuntimeException("Tile Entity to be marked for delayMarkDirty is already marked as such"); 
    this.delay = true;
    this.isDirty = false;
  }
  
  public void finishMarkDirty() {
    if (this.isDirty)
      super.markDirty(); 
    this.delay = false;
    this.isDirty = false;
  }
  
  public void markDirty() {
    if (!this.delay) {
      this.isDirty = false;
      super.markDirty();
    } else {
      this.isDirty = true;
    } 
  }
  
  private void suckItems() {
    if (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()) {
      ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata() % 6);
      double r = Math.log(upgradeNo(2)) / Math.log(2.0D);
      if (r > 3.5D)
        r = 3.5D; 
      for (Object o : this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).offset(dir.offsetX * (1.0D + r), dir.offsetY * (1.0D + r), dir.offsetZ * (1.0D + r)).expand(r, r, r))) {
        EntityItem item = (EntityItem)o;
        ItemStack itemstack = item.getEntityItem();
        if (item.isEntityAlive() && itemstack != null && (this.buffer.getBuffer() == null || XUHelper.canItemsStack((ItemStack)this.buffer.getBuffer(), itemstack, false, true))) {
          ItemStack itemstack1 = itemstack.copy();
          if (upgradeNo(3) == 0)
            itemstack1.stackSize = 1; 
          int n = itemstack1.stackSize;
          itemstack1 = XUHelper.invInsert((IInventory)this, itemstack1, -1);
          if (itemstack1 != null)
            n -= itemstack1.stackSize; 
          if (n > 0) {
            itemstack.stackSize -= n;
            if (itemstack.stackSize > 0) {
              item.setEntityItemStack(itemstack);
            } else {
              item.setDead();
            } 
            if (upgradeNo(3) == 0)
              return; 
          } 
        } 
      } 
    } 
  }
  
  private boolean doCraft() {
    if (this.buffer.getBuffer() == null || ((ItemStack)this.buffer.getBuffer()).stackSize < ((ItemStack)this.buffer.getBuffer()).getMaxStackSize()) {
      ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata() % 6);
      boolean craft = (this.worldObj.getBlock(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ) == Blocks.crafting_table);
      if (!craft)
        return false; 
      ForgeDirection dirX = orthX[dir.ordinal()];
      ForgeDirection dirY = orthY[dir.ordinal()];
      int[] slots = new int[9];
      IInventory[] inventories = new IInventory[9];
      boolean isEmpty = true;
      for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
          TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX * 2 + dirX.offsetX * dx + dirY.offsetX * dy, this.yCoord + dir.offsetY * 2 + dirX.offsetY * dx + dirY.offsetY * dy, this.zCoord + dir.offsetZ * 2 + dirX.offsetZ * dx + dirY.offsetZ * dy);
          int j = dx + 1 + 3 * (-dy + 1);
          boolean a = tile instanceof IInventory;
          boolean b = (a || tile instanceof IFluidHandler);
          if (b) {
            if (a) {
              inventories[j] = (IInventory)tile;
            } else {
              inventories[j] = (IInventory)new LiquidInventory((IFluidHandler)tile, dir.getOpposite());
            } 
            int k = getFirstExtractableItemStackSlot(inventories[j], dir.getOpposite().ordinal());
            slots[j] = k;
            if (k >= 0) {
              ItemStack item = inventories[j].getStackInSlot(k);
              crafting.setInventorySlotContents(j, item.copy());
              isEmpty = false;
            } else {
              crafting.setInventorySlotContents(j, null);
            } 
          } else {
            inventories[j] = null;
            crafting.setInventorySlotContents(j, null);
          } 
        } 
      } 
      if (isEmpty)
        return true; 
      if (this.cachedRecipe == null || !this.cachedRecipe.matches(crafting, this.worldObj) || this.cachedRecipe.getCraftingResult(crafting) == null) {
        int p = crafting.hashCode();
        if (p == this.prevStack && this.prevStack != 0 && 
          this.rand.nextInt(10) > 0)
          return true; 
        this.prevStack = p;
        IRecipe r = findMatchingRecipe(crafting, this.worldObj);
        if (r == null || r.getCraftingResult(crafting) == null || !isItemValidForSlot(0, r.getCraftingResult(crafting)))
          return true; 
        this.cachedRecipe = r;
      } 
      ItemStack stack = this.cachedRecipe.getCraftingResult(crafting);
      this.prevStack = 0;
      if (this.buffer.getBuffer() != null) {
        if (!XUHelper.canItemsStack(stack, (ItemStack)this.buffer.getBuffer(), false, true, false))
          return true; 
        if (stack.stackSize + ((ItemStack)this.buffer.getBuffer()).stackSize > stack.getMaxStackSize())
          return true; 
      } 
      if (!isItemValidForSlot(0, stack))
        return true; 
      ItemStack[] items = new ItemStack[9];
      for (int i = 0; i < 9; i++) {
        if (inventories[i] != null && slots[i] >= 0) {
          ItemStack c = inventories[i].getStackInSlot(slots[i]);
          boolean flag = false;
          if (c == null || !XUHelper.canItemsStack(crafting.getStackInSlot(i), c))
            flag = true; 
          if (!flag) {
            items[i] = inventories[i].decrStackSize(slots[i], 1);
            if (items[i] != null && (items[i]).stackSize != 1)
              flag = true; 
          } 
          if (flag) {
            for (int j = 0; j <= i; j++) {
              if (items[j] != null && inventories[j] != null) {
                items[j] = XUHelper.invInsert(inventories[j], items[j], dir.getOpposite().ordinal());
                if (items[j] != null)
                  XUHelper.dropItem(getWorldObj(), getNodeX(), getNodeY(), getNodeZ(), items[j]); 
              } 
            } 
            return true;
          } 
          if (c.getItem().hasContainerItem(c)) {
            ItemStack t = c.getItem().getContainerItem(c);
            if (t != null && (!t.isItemStackDamageable() || t.getItemDamage() <= t.getMaxDamage()))
              XUHelper.invInsert(inventories[i], t, dir.getOpposite().ordinal()); 
          } 
        } 
      } 
      XUHelper.invInsert((IInventory)this, stack, -1);
    } 
    return true;
  }
  
  private boolean genCobble() {
    if (ExtraUtils.disableCobblegen)
      return false; 
    if (this.buffer.getBuffer() == null || (((ItemStack)this.buffer.getBuffer()).getItem() == Item.getItemFromBlock(Blocks.cobblestone) && ((ItemStack)this.buffer.getBuffer()).stackSize < 64)) {
      int dir = getBlockMetadata() % 6;
      this.genCStoneCounter = (this.genCStoneCounter + 1) % (1 + upgradeNo(0));
      if (this.genCStoneCounter != 0)
        return false; 
      if (this.worldObj.getTotalWorldTime() - this.checkTimer > 100L) {
        this.checkTimer = this.worldObj.getTotalWorldTime();
        this.hasCStoneGen = false;
        if (this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) == Blocks.cobblestone) {
          int j, k;
          boolean hasLava = false;
          boolean hasWater = false;
          for (int i = 2; (!hasWater || !hasLava) && i < 6; i++) {
            k = hasWater | ((this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i]).getMaterial() == Material.water) ? 1 : 0);
            j = hasLava | ((this.worldObj.getBlock(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i]).getMaterial() == Material.lava) ? 1 : 0);
          } 
          if (k != 0 && j != 0)
            this.hasCStoneGen = true; 
        } 
      } 
      if (this.hasCStoneGen) {
        if (this.buffer.getBuffer() == null) {
          this.buffer.setBuffer(new ItemStack(Blocks.cobblestone, upgradeNo(2)));
        } else {
          ((ItemStack)this.buffer.getBuffer()).stackSize += 1 + upgradeNo(2);
          if (((ItemStack)this.buffer.getBuffer()).stackSize > 64)
            ((ItemStack)this.buffer.getBuffer()).stackSize = 64; 
        } 
        return true;
      } 
    } 
    return false;
  }
  
  public int getSizeInventory() {
    return 1;
  }
  
  public ItemStack getStackInSlot(int i) {
    if (i == 0)
      return (ItemStack)this.buffer.getBuffer(); 
    return null;
  }
  
  public ItemStack decrStackSize(int i, int j) {
    if (i != 0)
      return null; 
    if (this.buffer.getBuffer() != null) {
      if (((ItemStack)this.buffer.getBuffer()).stackSize <= j) {
        ItemStack itemStack = (ItemStack)this.buffer.getBuffer();
        this.buffer.setBuffer(null);
        markDirty();
        return itemStack;
      } 
      ItemStack itemstack = ((ItemStack)this.buffer.getBuffer()).splitStack(j);
      if (((ItemStack)this.buffer.getBuffer()).stackSize == 0)
        this.buffer.setBuffer(null); 
      markDirty();
      return itemstack;
    } 
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    if (i != 0)
      return null; 
    if (this.buffer.getBuffer() != null) {
      ItemStack itemstack = (ItemStack)this.buffer.getBuffer();
      this.buffer.setBuffer(null);
      return itemstack;
    } 
    return null;
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    this.buffer.setBuffer(itemstack);
    if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
      itemstack.stackSize = getInventoryStackLimit(); 
    markDirty();
  }
  
  public String getInventoryName() {
    return "gui.transferNode";
  }
  
  public boolean hasCustomInventoryName() {
    return false;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
    return (par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    for (int j = 0; j < this.upgrades.getSizeInventory(); j++) {
      ItemStack upgrade = this.upgrades.getStackInSlot(j);
      if (upgrade != null && ItemNodeUpgrade.isFilter(upgrade) && 
        !ItemNodeUpgrade.matchesFilterItem(itemstack, upgrade))
        return false; 
    } 
    return true;
  }
  
  public int[] getAccessibleSlotsFromSide(int j) {
    if (j < 0 || j >= 6 || j == getBlockMetadata() % 6)
      return contents; 
    return nullcontents;
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j) {
    return ((j < 0 || j >= 6 || j == getBlockMetadata() % 6) && isItemValidForSlot(i, itemstack));
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    return false;
  }
  
  public TileEntityTransferNodeInventory getNode() {
    return this;
  }
  
  public BoxModel getModel(ForgeDirection dir) {
    BoxModel boxes = new BoxModel();
    boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F)).rotateToSide(dir).setTextureSides(new Object[] { Integer.valueOf(dir.ordinal()), BlockTransferNode.nodeBase }));
    boxes.add((new Box(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.25F, 0.8125F)).rotateToSide(dir));
    boxes.add((new Box(0.3125F, 0.25F, 0.3125F, 0.6875F, 0.375F, 0.6875F)).rotateToSide(dir));
    boxes.add((new Box(0.375F, 0.25F, 0.375F, 0.625F, 0.375F, 0.625F)).rotateToSide(dir).setTexture(BlockTransferNode.nodeBase).setAllSideInvisible().setSideInvisible(new Object[] { Integer.valueOf(dir.getOpposite().ordinal()), Boolean.valueOf(false) }));
    return boxes;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNodeInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */