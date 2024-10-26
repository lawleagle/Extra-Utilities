package com.rwtema.extrautils.tileentity.enderquarry;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.EventHandlerEntityItemStealer;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.crafting.RecipeEnchantCrafting;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.network.packets.PacketTempChatMultiline;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Facing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityEnderQuarry extends TileEntity implements IEnergyHandler {
  private static final Random rand = (Random)XURandom.getInstance();

  public static boolean disableSelfChunkLoading = false;

  public static int baseDrain = 1800;

  public static float hardnessDrain = 200.0F;

  public ArrayList<ItemStack> items = new ArrayList<ItemStack>();

  public int dx = 1;

  public int dy = 0;

  public int dz = 0;

  public EnergyStorage energy = new EnergyStorage(10000000);

  public int inventoryMask = -1;

  public int fluidMask = -1;

  public long progress = 0L;

  public int neededEnergy = -1;

  public boolean started = false;

  public boolean finished = false;

  public FluidStack fluid = null;

  int chunk_x = 0, chunk_z = 0;

  int chunk_y = 0;

  byte t = 0;

  boolean searching = false;

  int fence_x = this.xCoord, fence_y = this.yCoord, fence_z = this.zCoord, fence_dir = 2, fence_elev = -1;

  int min_x = this.xCoord, max_x = this.xCoord;

  int min_z = this.zCoord;

  int max_z = this.zCoord;

  private ForgeChunkManager.Ticket chunkTicket;

  private EntityPlayer owner;

  public boolean shouldRefresh(Block oldID, Block newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
    return (oldID != newID);
  }

  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.energy.readFromNBT(tags);
    int n = tags.getInteger("item_no");
    this.items.clear();
    for (int i = 0; i < n; i++) {
      NBTTagCompound t = tags.getCompoundTag("item_" + i);
      this.items.add(ItemStack.loadItemStackFromNBT(t));
    }
    if (tags.hasKey("fluid"))
      this.fluid = FluidStack.loadFluidStackFromNBT(tags.getCompoundTag("fluid"));
    this.finished = tags.getBoolean("finished");
    if (this.finished)
      return;
    this.started = tags.getBoolean("started");
    if (!this.started)
      return;
    this.min_x = tags.getInteger("min_x");
    this.min_z = tags.getInteger("min_z");
    this.max_x = tags.getInteger("max_x");
    this.max_z = tags.getInteger("max_z");
    this.chunk_x = tags.getInteger("chunk_x");
    this.chunk_y = tags.getInteger("chunk_y");
    this.chunk_z = tags.getInteger("chunk_z");
    this.dx = tags.getInteger("dx");
    this.dy = tags.getInteger("dy");
    this.dz = tags.getInteger("dz");
    this.progress = tags.getLong("progress");
  }

  public void writeToNBT(NBTTagCompound tags) {
    super.writeToNBT(tags);
    this.energy.writeToNBT(tags);
    for (int i = 0; i < this.items.size(); i++) {
      while (i < this.items.size() && this.items.get(i) == null)
        this.items.remove(i);
      if (i < this.items.size()) {
        NBTTagCompound t = new NBTTagCompound();
        ((ItemStack)this.items.get(i)).writeToNBT(t);
        tags.setTag("item_" + i, (NBTBase)t);
      }
    }
    tags.setInteger("item_no", this.items.size());
    if (this.fluid != null) {
      NBTTagCompound t = new NBTTagCompound();
      this.fluid.writeToNBT(t);
      tags.setTag("fluid", (NBTBase)t);
    }
    if (this.finished) {
      tags.setBoolean("finished", true);
    } else if (this.started) {
      tags.setBoolean("started", true);
      tags.setInteger("min_x", this.min_x);
      tags.setInteger("max_x", this.max_x);
      tags.setInteger("min_z", this.min_z);
      tags.setInteger("max_z", this.max_z);
      tags.setInteger("chunk_x", this.chunk_x);
      tags.setInteger("chunk_y", this.chunk_y);
      tags.setInteger("chunk_z", this.chunk_z);
      tags.setInteger("dx", this.dx);
      tags.setInteger("dy", this.dy);
      tags.setInteger("dz", this.dz);
      tags.setLong("progress", this.progress);
    }
  }

  public void startDig() {
    this.started = true;
    this.chunk_y += 5;
    this.chunk_x = this.min_x + 1 >> 4;
    this.chunk_z = this.min_z + 1 >> 4;
    this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
    this.dy = this.chunk_y;
    this.dz = Math.max(0, this.min_z + 1 - (this.chunk_z << 4));
    if (!stopHere())
      nextBlock();
    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 2);
  }

  public void nextBlock() {
    nextSubBlock();
    while (!stopHere())
      nextSubBlock();
  }

  public void nextSubBlock() {
    this.progress++;
    this.dy--;
    if (this.dy <= 0) {
      this.dx++;
      if (this.dx >= 16 || (this.chunk_x << 4) + this.dx >= this.max_x) {
        this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
        this.dz++;
        if (this.dz >= 16 || (this.chunk_z << 4) + this.dz >= this.max_z) {
          nextChunk();
          this.dx = Math.max(0, this.min_x + 1 - (this.chunk_x << 4));
          this.dz = Math.max(0, this.min_z + 1 - (this.chunk_z << 4));
        }
      }
      this.dy = this.chunk_y;
    }
  }

  public void nextChunk() {
    unloadChunk();
    this.chunk_x++;
    if (this.chunk_x << 4 >= this.max_x) {
      this.chunk_x = this.min_x + 1 >> 4;
      this.chunk_z++;
      if (this.chunk_z << 4 >= this.max_z) {
        this.finished = true;
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 2);
        ForgeChunkManager.releaseTicket(this.chunkTicket);
        return;
      }
    }
    this.dy = this.chunk_y;
    loadChunk();
  }

  public boolean stopHere() {
    return (this.finished || isValid((this.chunk_x << 4) + this.dx, (this.chunk_z << 4) + this.dz));
  }

  public boolean isValid(int x, int z) {
    return (this.min_x < x && x < this.max_x && this.min_z < z && z < this.max_z);
  }

  public void forceChunkLoading(ForgeChunkManager.Ticket ticket) {
    if (this.chunkTicket == null)
      this.chunkTicket = ticket;
    if (!disableSelfChunkLoading)
      ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
    loadChunk();
  }

  public void updateEntity() {
    if (this.worldObj.isRemote)
      return;
    if (this.inventoryMask < 0)
      detectInventories();
    this.t = (byte)(this.t + 1);
    if (this.t < getSpeedNo() && !this.overClock)
      return;
    this.t = 0;
    if (this.searching)
      advFencing();
    if (!this.started || this.finished)
      return;
    if (this.chunkTicket == null) {
      this.chunkTicket = ForgeChunkManager.requestTicket(ExtraUtilsMod.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
      if (this.chunkTicket == null) {
        if (this.owner != null)
          this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Problem registering chunk-preserving method"));
        this.finished = true;
        return;
      }
      this.chunkTicket.getModData().setString("id", "quarry");
      this.chunkTicket.getModData().setInteger("x", this.xCoord);
      this.chunkTicket.getModData().setInteger("y", this.yCoord);
      this.chunkTicket.getModData().setInteger("z", this.zCoord);
      if (!disableSelfChunkLoading)
        ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
      loadChunk();
    }
    if (this.neededEnergy > 0 && this.worldObj.getTotalWorldTime() % 100L == 0L)
      this.neededEnergy = -1;
    int n = this.overClock ? 200 : getSpeedStack();
    for (int k = 0; k < n; k++) {
      if (!this.items.isEmpty() || this.fluid != null) {
        if (!this.overClock)
          this.energy.extractEnergy(baseDrain, false);
      } else if (this.overClock || (this.energy.getEnergyStored() >= this.neededEnergy && this.energy.extractEnergy(baseDrain, true) == baseDrain)) {
        int x = (this.chunk_x << 4) + this.dx;
        int z = (this.chunk_z << 4) + this.dz;
        int y = this.dy;
        if (y >= 0) {
          NetworkHandler.sendParticleEvent(this.worldObj, 1, x, y, z);
          if (mineBlock(x, y, z, !this.upgrades[1])) {
            this.neededEnergy = -1;
            nextBlock();
          }
        } else {
          nextBlock();
        }
      }
      if (!this.items.isEmpty() &&
        this.inventoryMask > 0)
        for (int i = 0; i < 6; i++) {
          if ((this.inventoryMask & 1 << i) > 0) {
            TileEntity tile;
            if ((tile = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[i], this.zCoord + Facing.offsetsZForSide[i])) instanceof IInventory) {
              IInventory inv = (IInventory)tile;
              for (int j = 0; j < this.items.size(); j++) {
                if (XUHelper.invInsert(inv, this.items.get(j), Facing.oppositeSide[i]) == null) {
                  this.items.remove(j);
                  j--;
                }
              }
            } else {
              detectInventories();
            }
          }
        }
      if (this.fluid != null &&
        this.fluidMask > 0)
        for (int i = 0; this.fluid != null && i < 6; i++) {
          if ((this.fluidMask & 1 << i) > 0) {
            TileEntity tile;
            if ((tile = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[i], this.zCoord + Facing.offsetsZForSide[i])) instanceof IFluidHandler) {
              IFluidHandler tank = (IFluidHandler)tile;
              this.fluid.amount -= tank.fill(ForgeDirection.getOrientation(i).getOpposite(), this.fluid, true);
              if (this.fluid.amount == 0) {
                this.fluid = null;
                break;
              }
            } else {
              detectInventories();
            }
          }
        }
    }
  }

  private int getSpeedNo() {
    if (this.upgrades[6])
      return 1;
    if (this.upgrades[7])
      return 1;
    if (this.upgrades[8])
      return 1;
    return 3;
  }

  private int getSpeedStack() {
    if (this.upgrades[6])
      return 1;
    if (this.upgrades[7])
      return 3;
    if (this.upgrades[8])
      return 9;
    return 1;
  }

  public DigType getDigType() {
    if (this.upgrades[2])
      return DigType.SILK;
    if (this.upgrades[3])
      return DigType.FORTUNE;
    if (this.upgrades[4])
      return DigType.FORTUNE2;
    if (this.upgrades[5])
      return DigType.FORTUNE3;
    return DigType.NORMAL;
  }

  public void invalidate() {
    ForgeChunkManager.releaseTicket(this.chunkTicket);
    super.invalidate();
  }

  private void loadChunk() {
    if (this.xCoord >> 4 != this.chunk_x || this.zCoord >> 4 != this.chunk_z)
      ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x, this.chunk_z));
  }

  private void unloadChunk() {
    if (this.xCoord >> 4 != this.chunk_x || this.zCoord >> 4 != this.chunk_z)
      ForgeChunkManager.unforceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x, this.chunk_z));
  }

  public boolean mineBlock(int x, int y, int z, boolean replaceWithDirt) {
    Block block = this.worldObj.getBlock(x, y, z);
    if (block == Blocks.air || this.worldObj.isAirBlock(x, y, z)) {
      if (!this.overClock)
        this.energy.extractEnergy(baseDrain, false);
      return true;
    }
    if (BlockBreakingRegistry.blackList(block)) {
      if (this.upgrades[9] && XUHelper.isFluidBlock(block))
        this.fluid = XUHelper.drainBlock(this.worldObj, x, y, z, true);
      if (!this.overClock)
        this.energy.extractEnergy(baseDrain, false);
      return true;
    }
    if (replaceWithDirt && (block.isLeaves((IBlockAccess)this.worldObj, x, y, z) || block.isFoliage((IBlockAccess)this.worldObj, x, y, z) || block.isWood((IBlockAccess)this.worldObj, x, y, z) || block instanceof net.minecraftforge.common.IPlantable || block instanceof net.minecraft.block.IGrowable)) {
      if (!this.overClock)
        this.energy.extractEnergy(baseDrain, false);
      return true;
    }
    int meta = this.worldObj.getBlockMetadata(x, y, z);
    float hardness = block.getBlockHardness(this.worldObj, x, y, z);
    if (hardness < 0.0F) {
      if (!this.overClock)
        this.energy.extractEnergy(baseDrain, false);
      return true;
    }
    int amount = (int)Math.ceil(baseDrain + (hardness * hardnessDrain) * getPowerMultiplier());
    if (amount > this.energy.getMaxEnergyStored())
      amount = this.energy.getMaxEnergyStored();
    if (this.overClock)
      amount = 0;
    if (this.energy.extractEnergy(amount, true) < amount) {
      this.neededEnergy = amount;
      return false;
    }
    this.energy.extractEnergy(amount, false);
    if (replaceWithDirt && (block == Blocks.grass || block == Blocks.dirt)) {
      if (this.worldObj.canBlockSeeTheSky(x, y + 1, z))
        this.worldObj.setBlock(x, y, z, (Block)Blocks.grass, 0, 3);
      if (rand.nextInt(16) == 0 && this.worldObj.isAirBlock(x, y + 1, z))
        if (rand.nextInt(5) == 0) {
          this.worldObj.getBiomeGenForCoords(x, z).plantFlower(this.worldObj, rand, x, y + 1, z);
        } else if (rand.nextInt(2) == 0) {
          this.worldObj.setBlock(x, y + 1, z, (Block)Blocks.yellow_flower, rand.nextInt(BlockFlower.field_149858_b.length), 3);
        } else {
          this.worldObj.setBlock(x, y + 1, z, (Block)Blocks.red_flower, rand.nextInt(BlockFlower.field_149859_a.length), 3);
        }
      return true;
    }
    return harvestBlock(block, x, y, z, meta, replaceWithDirt, getDigType());
  }

  public enum DigType {
    NORMAL(null, 0),
    SILK(Enchantment.silkTouch, 1),
    FORTUNE(Enchantment.fortune, 1),
    FORTUNE2(Enchantment.fortune, 2),
    FORTUNE3(Enchantment.fortune, 3),
    SPEED(Enchantment.efficiency, 1),
    SPEED2(Enchantment.efficiency, 3),
    SPEED3(Enchantment.efficiency, 5);

    public Enchantment ench;

    public int level;

    DigType(Enchantment ench, int level) {
      this.ench = ench;
      this.level = level;
    }

    public int getFortuneModifier() {
      if (this.ench == Enchantment.fortune)
        return this.level;
      return 0;
    }

    public ItemStack newStack(Item pick) {
      ItemStack stack = new ItemStack(pick);
      if (this.ench != null)
        stack.addEnchantment(this.ench, this.level);
      return stack;
    }

    public boolean isSilkTouch() {
      return (this.ench == Enchantment.silkTouch);
    }
  }

  public boolean harvestBlock(Block block, int x, int y, int z, int meta, boolean replaceWithDirt, DigType digType) {
    boolean isOpaque = block.isOpaqueCube();
    boolean seesSky = (replaceWithDirt && isOpaque && this.worldObj.canBlockSeeTheSky(x, y + 1, z));
    FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)this.worldObj);
    fakePlayer.setCurrentItemOrArmor(0, digType.newStack(Items.diamond_pickaxe));
    try {
      if (BlockBreakingRegistry.isSpecial(block)) {
        EventHandlerEntityItemStealer.startCapture(true);
        block.onBlockHarvested(this.worldObj, x, y, z, meta, (EntityPlayer)fakePlayer);
        if (!block.removedByPlayer(this.worldObj, (EntityPlayer)fakePlayer, x, y, z, true)) {
          this.items.addAll(EventHandlerEntityItemStealer.getCapturedItemStacks());
          return false;
        }
        block.harvestBlock(this.worldObj, (EntityPlayer)fakePlayer, x, y, z, meta);
        block.onBlockDestroyedByPlayer(this.worldObj, x, y, z, meta);
        if (replaceWithDirt && isOpaque)
          this.worldObj.setBlock(x, y, z, seesSky ? (Block)Blocks.grass : Blocks.dirt, 0, 3);
        this.items.addAll(EventHandlerEntityItemStealer.getCapturedItemStacks());
      } else {
        EventHandlerEntityItemStealer.startCapture(true);
        boolean flag = this.worldObj.setBlock(x, y, z, (replaceWithDirt && isOpaque) ? (seesSky ? (Block)Blocks.grass : Blocks.dirt) : Blocks.air, 0, 3);
        this.items.addAll(EventHandlerEntityItemStealer.getCapturedItemStacks());
        if (!flag)
          return false;
        ArrayList<ItemStack> i = new ArrayList<ItemStack>();
        if (digType.isSilkTouch() && block.canSilkHarvest(this.worldObj, (EntityPlayer)fakePlayer, x, y, z, meta)) {
          int j = 0;
          Item item = Item.getItemFromBlock(block);
          if (item != null) {
            if (item.getHasSubtypes())
              j = meta;
            ItemStack itemstack = new ItemStack(item, 1, j);
            i.add(itemstack);
          }
        } else {
          i.addAll(block.getDrops(this.worldObj, x, y, z, meta, digType.getFortuneModifier()));
        }
        float p = ForgeEventFactory.fireBlockHarvesting(i, this.worldObj, block, x, y, z, meta, digType.getFortuneModifier(), 1.0F, digType.isSilkTouch(), (EntityPlayer)fakePlayer);
        if (p > 0.0F && !i.isEmpty() && (p == 1.0F || rand.nextFloat() < p))
          this.items.addAll(i);
      }
      NetworkHandler.sendParticleEvent(this.worldObj, 0, x, y, z);
      if (seesSky && rand.nextInt(16) == 0 && this.worldObj.isAirBlock(x, y + 1, z))
        if (rand.nextInt(5) == 0) {
          this.worldObj.getBiomeGenForCoords(x, z).plantFlower(this.worldObj, rand, x, y + 1, z);
        } else if (rand.nextInt(2) == 0) {
          this.worldObj.setBlock(x, y + 1, z, (Block)Blocks.yellow_flower, rand.nextInt(BlockFlower.field_149858_b.length), 3);
        } else {
          this.worldObj.setBlock(x, y + 1, z, (Block)Blocks.red_flower, rand.nextInt(BlockFlower.field_149859_a.length), 3);
        }
      return true;
    } finally {
      fakePlayer.setCurrentItemOrArmor(0, null);
    }
  }

  private boolean overClock = false;

  public void debug() {
    this.overClock = true;
  }

  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return this.energy.receiveEnergy(maxReceive, simulate);
  }

  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    return 0;
  }

  public boolean canConnectEnergy(ForgeDirection from) {
    return true;
  }

  public int getEnergyStored(ForgeDirection from) {
    return this.energy.getEnergyStored();
  }

  public int getMaxEnergyStored(ForgeDirection from) {
    return this.energy.getMaxEnergyStored();
  }

  public boolean[] upgrades = new boolean[16];

  public static final int UPGRADE_BLANK = 0;

  public static final int UPGRADE_VOID = 1;

  public static final int UPGRADE_SILK = 2;

  public static final int UPGRADE_FORTUNE1 = 3;

  public static final int UPGRADE_FORTUNE2 = 4;

  public static final int UPGRADE_FORTUNE3 = 5;

  public static final int UPGRADE_SPEED1 = 6;

  public static final int UPGRADE_SPEED2 = 7;

  public static final int UPGRADE_SPEED3 = 8;

  public static final int UPGRADE_FLUID = 9;

  public static void addUpgradeRecipes() {
    ItemStack base = new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 0);
    ItemStack burntQuartz = new ItemStack((Block)ExtraUtils.decorative1, 1, 2);
    ItemStack endersidian = new ItemStack((Block)ExtraUtils.decorative1, 1, 1);
    ExtraUtils.addRecipe(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 0), new Object[] { " E ", "EQE", " E ", Character.valueOf('E'), endersidian, Character.valueOf('Q'), burntQuartz });
    ExtraUtils.addRecipe(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 1), new Object[] { " T ", "RBR", Character.valueOf('B'), base, Character.valueOf('T'), ExtraUtils.trashCan, Character.valueOf('R'), Blocks.quartz_block });
    ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 2), new Object[] { " P ", "RBR", Character.valueOf('B'), base, Character.valueOf('P'), DigType.SILK.newStack(Items.golden_pickaxe), Character.valueOf('R'), Items.redstone }));
    ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 3), new Object[] { " P ", "RBR", Character.valueOf('B'), base, Character.valueOf('P'), DigType.FORTUNE.newStack(Items.iron_pickaxe), Character.valueOf('R'), Items.redstone }));
    ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 4), new Object[] { " P ", "RBR", Character.valueOf('B'), new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 3), Character.valueOf('P'), DigType.FORTUNE.newStack(Items.golden_pickaxe), Character.valueOf('R'), Items.redstone }));
    ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 5), new Object[] { "P P", "RBR", Character.valueOf('B'), new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 4), Character.valueOf('P'), DigType.FORTUNE.newStack(Items.diamond_pickaxe), Character.valueOf('R'), Items.redstone }));
    if (ExtraUtils.nodeUpgrade != null) {
      ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 6), new Object[] { " R ", "TBT", Character.valueOf('B'), base, Character.valueOf('T'), new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 0), Character.valueOf('R'), DigType.SPEED.newStack(Items.diamond_pickaxe) }));
      ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 7), new Object[] { " R ", "TBT", Character.valueOf('B'), new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 6), Character.valueOf('T'), new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 0), Character.valueOf('R'), DigType.SPEED2.newStack(Items.diamond_pickaxe) }));
      ExtraUtils.addRecipe((IRecipe)new RecipeEnchantCrafting(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 8), new Object[] { "R R", "TBT", Character.valueOf('B'), new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 7), Character.valueOf('T'), new ItemStack((Item)ExtraUtils.nodeUpgrade, 1, 3), Character.valueOf('R'), DigType.SPEED3.newStack(Items.diamond_pickaxe) }));
    }
    ExtraUtils.addRecipe(new ItemStack((Block)ExtraUtils.enderQuarryUpgrade, 1, 9), new Object[] { " T ", "RBR", Character.valueOf('B'), base, Character.valueOf('T'), Items.bucket, Character.valueOf('R'), Items.redstone });
  }

  public static double[] powerMultipliers = new double[] {
      1.0D, 1.0D, 1.5D, 5.0D, 20.0D, 80.0D, 1.0D, 1.5D, 2.0D, 1.0D,
      0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };

  public double getPowerMultiplier() {
    double multiplier = 1.0D;
    for (int i = 0; i < 16; i++) {
      if (this.upgrades[i])
        multiplier *= powerMultipliers[i];
    }
    return multiplier;
  }

  public void detectInventories() {
    this.inventoryMask = 0;
    this.fluidMask = 0;
    this.upgrades = new boolean[16];
    for (int i = 0; i < 6; i++) {
      int x = this.xCoord + Facing.offsetsXForSide[i];
      int y = this.yCoord + Facing.offsetsYForSide[i];
      int z = this.zCoord + Facing.offsetsZForSide[i];
      TileEntity tile = this.worldObj.getTileEntity(x, y, z);
      if (tile instanceof IInventory)
        this.inventoryMask |= 1 << i;
      if (tile instanceof IFluidHandler)
        this.fluidMask |= 1 << i;
      if (this.worldObj.getBlock(x, y, z) == ExtraUtils.enderQuarryUpgrade)
        this.upgrades[this.worldObj.getBlockMetadata(x, y, z)] = true;
    }
  }

  public void startFencing(EntityPlayer player) {
    if (this.finished) {
      PacketTempChat.sendChat(player, (IChatComponent)new ChatComponentText("Quarry has finished"));
      return;
    }
    if (this.started) {
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Mining at: (" + ((this.chunk_x << 4) + this.dx) + "," + this.dy + "," + ((this.chunk_z << 4) + this.dz) + ")"));
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("" + this.progress + " blocks scanned."));
      PacketTempChatMultiline.sendCached(player);
      return;
    }
    if (this.searching) {
      PacketTempChat.sendChat(player, (IChatComponent)new ChatComponentText("Searching fence boundary at: (" + this.fence_x + "," + this.fence_y + "," + this.fence_z + ")"));
      return;
    }
    this.owner = player;
    player.addChatComponentMessage((IChatComponent)new ChatComponentText("Analyzing Fence boundary"));
    if (checkForMarkers(player))
      return;
    this.fence_x = this.xCoord;
    this.fence_y = this.yCoord;
    this.fence_z = this.zCoord;
    this.fence_elev = -1;
    this.fence_dir = -1;
    int j = 0;
    for (int i = 2; i < 6; i++) {
      if (isFence(this.fence_x, this.fence_y, this.fence_z, i)) {
        if (this.fence_dir < 0)
          this.fence_dir = i;
        j++;
        if (j > 2) {
          stopFencing("Quarry is connected to more than fences on more than 2 sides", false);
          return;
        }
      }
    }
    if (j < 2) {
      if (j == 0)
        stopFencing("Unable to detect fence boundary", false);
      if (j == 1)
        stopFencing("Quarry is only connected to fence boundary on one side", false);
      return;
    }
    this.chunk_y = this.yCoord;
    this.fence_x = this.xCoord + Facing.offsetsXForSide[this.fence_dir];
    this.fence_y = this.yCoord + Facing.offsetsYForSide[this.fence_dir];
    this.fence_z = this.zCoord + Facing.offsetsZForSide[this.fence_dir];
    this.min_x = this.xCoord;
    this.max_x = this.xCoord;
    this.min_z = this.zCoord;
    this.max_z = this.zCoord;
    this.searching = true;
  }

  public boolean checkForMarkers(EntityPlayer player) {
    ForgeDirection[] arr$;
    int len$;
    int i$;
    for (arr$ = new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH }, len$ = arr$.length, i$ = 0; i$ < len$; ) {
      ForgeDirection d = arr$[i$];
      int[] test = { (getWorldObj()).provider.dimensionId, this.xCoord + d.offsetX, this.yCoord, this.zCoord + d.offsetZ };
      int[] test_forward = null;
      int[] test_side = null;
      boolean flag = true;
      for (int[] a : TileEntityEnderMarker.markers) {
        if (isIntEqual(a, test)) {
          flag = false;
          break;
        }
      }
      if (flag) {
        i$++;
        continue;
      }
      player.addChatComponentMessage((IChatComponent)new ChatComponentText("Found attached ender-marker"));
      for (int[] a : TileEntityEnderMarker.markers) {
        if (a[0] == test[0] && a[2] == test[2] && (
          a[1] != test[1] || a[3] != test[3])) {
          if (sign(a[1] - test[1]) == d.offsetX && sign(a[3] - test[3]) == d.offsetZ)
            if (test_forward == null) {
              test_forward = a;
            } else if (!isIntEqual(a, test_forward)) {
              player.addChatComponentMessage((IChatComponent)new ChatComponentText("Quarry marker square is ambiguous - multiple markers found at (" + a[1] + "," + a[3] + ") and (" + test_forward[1] + "," + test_forward[3] + ")"));
            }
          if ((d.offsetX == 0 && a[3] == test[3]) || (d.offsetZ == 0 && a[1] == test[1])) {
            if (test_side == null) {
              test_side = a;
              continue;
            }
            if (!isIntEqual(a, test_side))
              player.addChatComponentMessage((IChatComponent)new ChatComponentText("Quarry marker square is ambiguous - multiple markers found at (" + a[1] + "," + a[3] + ") and (" + test_side[1] + "," + test_side[3] + ")"));
          }
        }
      }
      if (test_forward == null) {
        player.addChatComponentMessage((IChatComponent)new ChatComponentText("Quarry marker square is incomplete"));
        return false;
      }
      if (test_side == null) {
        player.addChatComponentMessage((IChatComponent)new ChatComponentText("Quarry marker square is incomplete"));
        return false;
      }
      int amin_x = Math.min(Math.min(test[1], test_forward[1]), test_side[1]);
      int amax_x = Math.max(Math.max(test[1], test_forward[1]), test_side[1]);
      int amin_z = Math.min(Math.min(test[3], test_forward[3]), test_side[3]);
      int amax_z = Math.max(Math.max(test[3], test_forward[3]), test_side[3]);
      if (amax_x - amin_x <= 2 || amax_z - amin_z <= 2) {
        stopFencing("Region created by ender markers is too small", false);
        return false;
      }
      this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Sucessfully established boundary"));
      if (disableSelfChunkLoading)
        this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Note: Quarry is configured not to self-chunkload."));
      this.chunk_y = this.yCoord;
      this.min_x = amin_x;
      this.max_x = amax_x;
      this.min_z = amin_z;
      this.max_z = amax_z;
      this.searching = false;
      startDig();
      return true;
    }
    return false;
  }

  public static int sign(int d) {
    if (d == 0)
      return 0;
    if (d > 0)
      return 1;
    return -1;
  }

  public static boolean isIntEqual(int[] a, int[] b) {
    if (a == b)
      return true;
    for (int i = 0; i < 4; i++) {
      if (a[i] != b[i])
        return false;
    }
    return true;
  }

  public void stopFencing(String reason, boolean sendLocation) {
    this.searching = false;
    if (sendLocation)
      reason = reason + ": (" + this.fence_x + "," + this.fence_y + "," + this.fence_z + ")";
    if (this.owner != null)
      this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText(reason));
  }

  private void advFencing() {
    Long t = Long.valueOf(System.nanoTime());
    while (this.searching && System.nanoTime() - t.longValue() < 100000L)
      advFence();
  }

  public void advFence() {
    int new_dir = -1;
    for (int i = 0; i < 6; i++) {
      if ((this.fence_elev < 0) ? (i != Facing.oppositeSide[this.fence_dir]) : (i != Facing.oppositeSide[this.fence_elev]))
        if (isFence(this.fence_x, this.fence_y, this.fence_z, i))
          if (new_dir == -1) {
            new_dir = i;
          } else {
            stopFencing("Fence boundary splits at", true);
            return;
          }
    }
    if (new_dir < 0) {
      stopFencing("Fence boundary stops at", true);
      return;
    }
    if (new_dir <= 1) {
      this.fence_elev = new_dir;
      this.fence_y += Facing.offsetsYForSide[new_dir];
      if (new_dir == 1)
        this.chunk_y = Math.max(this.chunk_y, this.fence_y);
    } else {
      if (this.fence_dir != new_dir) {
        if ((this.min_z < this.fence_z && this.fence_z < this.max_z) || (this.min_x < this.fence_x && this.fence_x < this.max_x)) {
          stopFencing("Fence boundary must be square", true);
          return;
        }
        boolean flag = false;
        if (this.fence_z < this.zCoord) {
          flag = (this.fence_z != this.min_z && this.min_z != this.zCoord);
          this.min_z = this.fence_z;
        }
        if (this.fence_x < this.xCoord && !flag) {
          flag = (this.fence_x != this.min_x && this.min_x != this.xCoord);
          this.min_x = this.fence_x;
        }
        if (this.fence_z > this.zCoord && !flag) {
          flag = (this.fence_z != this.max_z && this.max_z != this.zCoord);
          this.max_z = this.fence_z;
        }
        if (this.fence_x > this.xCoord && !flag) {
          flag = (this.fence_x != this.max_x && this.max_x != this.xCoord);
          this.max_x = this.fence_x;
        }
        if (flag) {
          stopFencing("Fence boundary must be square", true);
          return;
        }
      }
      this.fence_x += Facing.offsetsXForSide[new_dir];
      this.fence_z += Facing.offsetsZForSide[new_dir];
      this.fence_dir = new_dir;
      this.fence_elev = -1;
    }
    if (this.fence_x == this.xCoord && this.fence_y == this.yCoord && this.fence_z == this.zCoord) {
      if (this.max_x - this.min_x <= 2 || this.max_z - this.min_z <= 2) {
        stopFencing("Region created by fence is too small", false);
        return;
      }
      this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Sucessfully established boundary"));
      if (disableSelfChunkLoading)
        this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Note: Quarry is configured not to self-chunkload."));
      startDig();
      this.searching = false;
    }
  }

  public boolean isFence(int x, int y, int z, int dir) {
    return isFence(x + Facing.offsetsXForSide[dir], y + Facing.offsetsYForSide[dir], z + Facing.offsetsZForSide[dir]);
  }

  public boolean isFence(int x, int y, int z) {
    Block id = this.worldObj.getBlock(x, y, z);
    if (BlockBreakingRegistry.isFence(id))
      return true;
    return (x == this.xCoord && z == this.zCoord && y == this.yCoord);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\TileEntityEnderQuarry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
