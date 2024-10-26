package com.rwtema.extrautils.helper;

import com.mojang.authlib.GameProfile;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.core.CastIterator;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.linked.TIntLinkedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

public class XUHelper {
  public static boolean deObf = false;
  
  public static final Random rand = XURandom.getInstance();
  
  public static final String[] dyes = new String[] { 
      "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", 
      "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };
  
  public static final int[] dyeCols = new int[16];
  
  static {
    for (int i = 0; i < 16; i++) {
      float[] cols = EntitySheep.fleeceColorTable[i];
      int r = (int)(cols[0] * 255.0F);
      int g = (int)(cols[1] * 255.0F);
      int b = (int)(cols[2] * 255.0F);
      dyeCols[i] = r << 16 | g << 8 | b;
    } 
  }
  
  private static long timer = 0L;
  
  static {
    String[] wrenchClassNames = { "buildcraft.api.tools.IToolWrench", "cofh.api.item.IToolHammer", "powercrystals.minefactoryreloaded.api.IMFRHammer", "appeng.api.implementations.items.IAEWrench", "crazypants.enderio.api.tool.ITool" };
  }
  
  static final ArrayList<Class<?>> wrenchClazzes = new ArrayList<Class<?>>();
  
  static {
    for (String wrenchClassName : wrenchClassNames) {
      try {
        wrenchClazzes.add(Class.forName(wrenchClassName));
        LogHelper.fine("Detected wrench class: " + wrenchClassName, new Object[0]);
      } catch (ClassNotFoundException ignore) {}
    } 
  }
  
  public static boolean isWrench(ItemStack itemstack) {
    if (itemstack == null)
      return false; 
    Item item = itemstack.getItem();
    if (item == null)
      return false; 
    if (item == Items.stick)
      return true; 
    for (Class<?> wrenchClazz : wrenchClazzes) {
      if (wrenchClazz.isAssignableFrom(item.getClass()))
        return true; 
    } 
    return false;
  }
  
  public static FluidStack drainBlock(World world, int x, int y, int z, boolean doDrain) {
    Block id = world.getBlock(x, y, z);
    if (id == Blocks.flowing_lava || id == Blocks.lava) {
      FluidStack liquid;
      if (world.getBlockMetadata(x, y, z) == 0) {
        liquid = new FluidStack(FluidRegistry.LAVA, 1000);
      } else {
        liquid = new FluidStack(FluidRegistry.LAVA, 0);
      } 
      if (doDrain)
        world.setBlockToAir(x, y, z); 
      return liquid;
    } 
    if (id == Blocks.flowing_water || id == Blocks.water) {
      FluidStack liquid;
      if (world.getBlockMetadata(x, y, z) == 0) {
        liquid = new FluidStack(FluidRegistry.WATER, 1000);
      } else {
        liquid = new FluidStack(FluidRegistry.WATER, 0);
      } 
      if (doDrain)
        world.setBlockToAir(x, y, z); 
      return liquid;
    } 
    if (id instanceof IFluidBlock) {
      IFluidBlock fluidBlock = (IFluidBlock)id;
      if (fluidBlock.getFluid() != null && fluidBlock.canDrain(world, x, y, z))
        return fluidBlock.drain(world, x, y, z, doDrain); 
    } 
    return null;
  }
  
  public static String getAnvilName(ItemStack item) {
    String s = "";
    if (item != null && item.getTagCompound() != null && item.getTagCompound().hasKey("display")) {
      NBTTagCompound nbttagcompound = item.getTagCompound().getCompoundTag("display");
      if (nbttagcompound.hasKey("Name"))
        s = nbttagcompound.getString("Name"); 
    } 
    return s;
  }
  
  public static boolean hasPersistantNBT(Entity entity) {
    return entity.getEntityData().hasKey("PlayerPersisted", 10);
  }
  
  public static NBTTagCompound getPersistantNBT(Entity entity) {
    NBTTagCompound t = entity.getEntityData();
    if (!t.hasKey("PlayerPersisted", 10)) {
      NBTTagCompound tag = new NBTTagCompound();
      t.setTag("PlayerPersisted", (NBTBase)tag);
      return tag;
    } 
    return t.getCompoundTag("PlayerPersisted");
  }
  
  public static String getPlayerOwner(ItemStack item) {
    String s = "";
    if (item != null && item.getTagCompound() != null && item.getTagCompound().hasKey("XU:owner"))
      s = item.getTagCompound().getString("XU:owner"); 
    return s;
  }
  
  public static void setPlayerOwner(ItemStack item, String s) {
    if (item != null) {
      NBTTagCompound tags = item.getTagCompound();
      if (tags == null)
        tags = new NBTTagCompound(); 
      if (s == null || s.equals("")) {
        if (tags.hasKey("XU:owner")) {
          tags.removeTag("XU:owner");
          if (tags.hasNoTags()) {
            item.setTagCompound(null);
          } else {
            item.setTagCompound(tags);
          } 
        } 
      } else {
        tags.setString("XU:owner", s);
        item.setTagCompound(tags);
      } 
    } 
  }
  
  public static void resetTimer() {
    timer = System.nanoTime();
  }
  
  public static void printTimer(String t) {
    LogHelper.debug("time:" + t + " - " + ((System.nanoTime() - timer) / 1000000.0D), new Object[0]);
    timer = System.nanoTime();
  }
  
  public static int getDyeFromItemStack(ItemStack dye) {
    if (dye == null)
      return -1; 
    if (dye.getItem() == Items.dye)
      return dye.getItemDamage(); 
    for (int i = 0; i < 16; i++) {
      for (ItemStack target : OreDictionary.getOres(dyes[i])) {
        if (OreDictionary.itemMatches(target, dye, false))
          return i; 
      } 
    } 
    return -1;
  }
  
  public static int rndInt(int n) {
    if (n <= 0)
      return 0; 
    return rand.nextInt(n);
  }
  
  public static String getFluidName(FluidStack fluid) {
    if (fluid == null || fluid.getFluid() == null)
      return "ERROR"; 
    String s = fluid.getFluid().getLocalizedName(fluid);
    if (s == null)
      return "ERROR"; 
    if (s.equals(""))
      s = "Unnamed liquid"; 
    if (s.equals(fluid.getFluid().getUnlocalizedName())) {
      s = fluid.getFluid().getName();
      if (s.equals(s.toLowerCase()))
        s = s.substring(0, 1).toUpperCase() + s.substring(1, s.length()); 
    } 
    return s;
  }
  
  public static Block safeBlockId(World world, int x, int y, int z) {
    return safeBlockId(world, x, y, z, Blocks.air);
  }
  
  public static int[] getSlots(int k) {
    int[] slots = new int[k];
    for (int i = 0; i < k; i++)
      slots[i] = i; 
    return slots;
  }
  
  public static TileEntity safegetTileEntity(World world, int x, int y, int z) {
    if (world.blockExists(x, y, z))
      return world.getTileEntity(x, y, z); 
    return null;
  }
  
  public static Block safeBlockId(World world, int x, int y, int z, Block falseReturnValue) {
    if (world.blockExists(x, y, z))
      return world.getBlock(x, y, z); 
    return falseReturnValue;
  }
  
  public static boolean canItemsStack(ItemStack a, ItemStack b) {
    return canItemsStack(a, b, false, true);
  }
  
  public static ItemStack invInsert(IInventory inv, ItemStack item, int side) {
    if (item != null && item.stackSize > 0) {
      boolean nonSided = !(inv instanceof ISidedInventory);
      int empty = -1;
      int filter = -1;
      int maxStack = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
      filter = maxStack;
      boolean flag = false;
      for (int i : InvHelper.getSlots(inv, side)) {
        ItemStack dest = inv.getStackInSlot(i);
        if (dest == null) {
          if (empty == -1 && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)))
            empty = i; 
        } else if (InvHelper.canStack(item, dest) && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)) && 
          maxStack - dest.stackSize > 0 && filter > 0) {
          int l = Math.min(Math.min(item.stackSize, maxStack - dest.stackSize), filter);
          if (l > 0) {
            dest.stackSize += l;
            item.stackSize -= l;
            filter -= l;
            flag = true;
            if (item.stackSize <= 0) {
              item = null;
              break;
            } 
            if (filter <= 0)
              break; 
          } 
        } 
      } 
      if (filter > 0 && item != null && empty != -1 && inv.isItemValidForSlot(empty, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, item, side))) {
        if (filter < item.stackSize) {
          inv.setInventorySlotContents(empty, item.splitStack(filter));
        } else {
          inv.setInventorySlotContents(empty, item);
          item = null;
        } 
        flag = true;
      } 
      if (flag)
        inv.markDirty(); 
    } 
    return item;
  }
  
  public static ItemStack[] simMassInvInsert(IInventory inv, ItemStack[] items, int side) {
    TIntLinkedList resultInd = new TIntLinkedList();
    ItemStack[] result = new ItemStack[items.length];
    for (int i = 0; i < result.length; i++) {
      if (items[i] != null && (items[i]).stackSize > 0) {
        result[i] = items[i].copy();
        resultInd.add(i);
      } 
    } 
    return simMassInvInsert_do(inv, side, resultInd, result);
  }
  
  public static ItemStack[] simMassInvInsert(IInventory inv, Collection<ItemStack> items, int side) {
    TIntLinkedList resultInd = new TIntLinkedList();
    ItemStack[] result = new ItemStack[items.size()];
    int i = 0;
    for (ItemStack item : items) {
      if (item != null && item.stackSize > 0) {
        result[i] = item.copy();
        resultInd.add(i);
      } 
      i++;
    } 
    return simMassInvInsert_do(inv, side, resultInd, result);
  }
  
  public static ItemStack[] simMassInvInsert_do(IInventory inv, int side, TIntLinkedList resultInd, ItemStack[] result) {
    if (resultInd.isEmpty())
      return null; 
    int[] slots = InvHelper.getSlots(inv, side);
    ISidedInventory invS = null;
    boolean sided = inv instanceof ISidedInventory;
    if (sided)
      invS = (ISidedInventory)inv; 
    int maxStack = inv.getInventoryStackLimit();
    TIntLinkedList emptySlots = new TIntLinkedList();
    for (int i : slots) {
      ItemStack curStack = inv.getStackInSlot(i);
      if (curStack == null) {
        emptySlots.add(i);
      } else {
        int m = Math.min(maxStack, curStack.getMaxStackSize()) - curStack.stackSize;
        if (m > 0) {
          TIntIterator resultIterator = resultInd.iterator();
          while (resultIterator.hasNext()) {
            int j = resultIterator.next();
            ItemStack itemStack = result[j];
            if (itemStack == null)
              continue; 
            if (!canItemsStack(itemStack, curStack) || !inv.isItemValidForSlot(i, itemStack) || (invS != null && !invS.canInsertItem(i, itemStack, side)))
              continue; 
            itemStack.stackSize -= m;
            if (itemStack.stackSize <= 0) {
              result[j] = null;
              resultIterator.remove();
              if (resultInd.isEmpty())
                return null; 
            } 
          } 
        } 
      } 
    } 
    if (emptySlots.isEmpty())
      return result; 
    TIntIterator slotIterator = emptySlots.iterator();
    while (slotIterator.hasNext()) {
      int i = slotIterator.next();
      TIntIterator resultIterator = resultInd.iterator();
      while (resultIterator.hasNext()) {
        int j = resultIterator.next();
        ItemStack itemStack = result[j];
        if (itemStack == null)
          continue; 
        if (!inv.isItemValidForSlot(i, itemStack) || (invS != null && !invS.canInsertItem(i, itemStack, side)))
          continue; 
        itemStack.stackSize -= maxStack;
        if (itemStack.stackSize <= 0) {
          result[j] = null;
          resultIterator.remove();
        } 
      } 
    } 
    if (resultInd.isEmpty())
      return null; 
    return result;
  }
  
  public static ItemStack simInvInsert(IInventory inv, ItemStack item, int side) {
    if (item == null || item.stackSize <= 0)
      return item; 
    item = item.copy();
    boolean nonSided = !(inv instanceof ISidedInventory);
    int empty = -1;
    int filter = -1;
    int maxStack = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
    filter = maxStack;
    for (int i : InvHelper.getSlots(inv, side)) {
      ItemStack dest = inv.getStackInSlot(i);
      if (dest == null) {
        if (empty == -1 && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)))
          empty = i; 
      } else if (InvHelper.canStack(item, dest) && inv.isItemValidForSlot(i, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, item, side)) && 
        maxStack - dest.stackSize > 0 && filter > 0) {
        int l = Math.min(Math.min(item.stackSize, maxStack - dest.stackSize), filter);
        if (l > 0) {
          item.stackSize -= l;
          filter -= l;
          if (item.stackSize <= 0) {
            item = null;
            break;
          } 
          if (filter <= 0)
            break; 
        } 
      } 
    } 
    if (filter > 0 && item != null && empty != -1 && inv.isItemValidForSlot(empty, item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, item, side)))
      if (filter < item.stackSize) {
        item.stackSize -= filter;
      } else {
        item = null;
      }  
    return item;
  }
  
  public static int[] getInventorySideSlots(IInventory inv, ForgeDirection side) {
    return getInventorySideSlots(inv, side.ordinal());
  }
  
  public static int[] getInventorySideSlots(IInventory inv, int side) {
    if (inv instanceof ISidedInventory)
      return ((ISidedInventory)inv).getAccessibleSlotsFromSide(side); 
    int[] arr = new int[inv.getSizeInventory()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = i; 
    return arr;
  }
  
  public static boolean canItemsStack(ItemStack a, ItemStack b, boolean ignoreDurability, boolean ignoreStackLimits) {
    return canItemsStack(a, b, ignoreDurability, ignoreStackLimits, false);
  }
  
  public static boolean canItemsStack(ItemStack a, ItemStack b, boolean ignoreDurability, boolean ignoreStackLimits, boolean ignoreNBT) {
    if (a == null || b == null)
      return false; 
    if (a.getItem() != b.getItem())
      return false; 
    if (!ignoreDurability && 
      a.getItemDamage() != b.getItemDamage())
      return false; 
    if (!ignoreStackLimits) {
      if (!a.isStackable() || a.stackSize >= a.getMaxStackSize())
        return false; 
      if (!b.isStackable() || b.stackSize >= b.getMaxStackSize())
        return false; 
    } 
    return (ignoreNBT || ItemStack.areItemStackTagsEqual(a, b));
  }
  
  public static boolean contains(ISidedInventory inv, int side, ItemStack s) {
    return false;
  }
  
  public static int[] rndSeq(int n, Random rand) {
    int[] rnd = new int[n];
    int t = -1;
    for (int i = 1; i < n; i++) {
      int j = rand.nextInt(i + 1);
      rnd[i] = rnd[j];
      rnd[j] = i;
    } 
    return rnd;
  }
  
  public static String s(int k) {
    return (k == 0) ? "" : "s";
  }
  
  public static boolean isPlayerReal(EntityPlayer player) {
    return !isPlayerFake(player);
  }
  
  public static boolean isPlayerReal(EntityPlayerMP player) {
    return !isPlayerFake(player);
  }
  
  public static boolean isPlayerFake(EntityPlayer player) {
    if (player.worldObj == null)
      return true; 
    if (player.worldObj.isRemote)
      return false; 
    if (player.getClass() == EntityPlayerMP.class)
      return false; 
    return !(MinecraftServer.getServer().getConfigurationManager()).playerEntityList.contains(player);
  }
  
  public static boolean isPlayerFake(EntityPlayerMP player) {
    if (player.getClass() != EntityPlayerMP.class)
      return true; 
    if (player.playerNetServerHandler == null)
      return true; 
    try {
      player.getPlayerIP().length();
      player.playerNetServerHandler.netManager.getSocketAddress().toString();
    } catch (Exception e) {
      return true;
    } 
    return !(MinecraftServer.getServer().getConfigurationManager()).playerEntityList.contains(player);
  }
  
  private static final UUID temaID = UUID.fromString("72ddaa05-7bbe-4ae2-9892-2c8d90ea0ad8");
  
  public static boolean isThisPlayerACheatyBastardOfCheatBastardness(EntityPlayer player) {
    return (!isPlayerFake(player) && isTema(player.getGameProfile()));
  }
  
  public static boolean isTema(GameProfile gameProfile) {
    return isTema(gameProfile.getName(), gameProfile.getId());
  }
  
  private static boolean isTema(String name, UUID id) {
    return ("RWTema".equals(name) && id.equals(temaID));
  }
  
  public static String addThousandsCommas(int a) {
    return String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(a) });
  }
  
  public static TileEntity getNearestTile(World world, int x, int y, int z, int r, Class<? extends TileEntity> clazz) {
    TileEntity closestTile = null;
    r *= r;
    double dist = 2.147483647E9D;
    for (int cx = x - r >> 4; cx <= x + r >> 4; cx++) {
      for (int cz = z - r >> 4; cz <= z + r >> 4; cz++) {
        Chunk c = world.getChunkFromChunkCoords(cx, cz);
        for (TileEntity tile : new CastIterator(c.chunkTileEntityMap.values())) {
          if (tile.isInvalid() || !tile.getClass().equals(clazz))
            continue; 
          double d = dist2((tile.xCoord - x), (tile.yCoord - y), (tile.zCoord - z));
          if (d > r)
            continue; 
          if (d < dist || closestTile == null) {
            dist = d;
            closestTile = tile;
          } 
        } 
      } 
    } 
    return closestTile;
  }
  
  public static double dist2(double dx, double dy, double dz) {
    return dx * dx + dy * dy + dz * dz;
  }
  
  public static Random random = XURandom.getInstance();
  
  public static void dropItem(World world, int x, int y, int z, ItemStack itemstack) {
    if (itemstack != null) {
      float dx = random.nextFloat() * 0.8F + 0.1F;
      float dy = random.nextFloat() * 0.8F + 0.1F;
      for (float dz = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
        int k1 = random.nextInt(21) + 10;
        if (k1 > itemstack.stackSize)
          k1 = itemstack.stackSize; 
        itemstack.stackSize -= k1;
        EntityItem entityitem = new EntityItem(world, (x + dx), (y + dy), (z + dz), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
        float f3 = 0.05F;
        entityitem.motionX = ((float)random.nextGaussian() * f3);
        entityitem.motionY = ((float)random.nextGaussian() * f3 + 0.2F);
        entityitem.motionZ = ((float)random.nextGaussian() * f3);
        if (itemstack.hasTagCompound())
          entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy()); 
      } 
    } 
  }
  
  public static boolean isFluidBlock(Block b) {
    return (b == Blocks.water || b == Blocks.lava || b instanceof IFluidBlock);
  }
  
  public static String niceFormat(double v) {
    String format;
    if (v == (int)v) {
      format = String.format(Locale.ENGLISH, "%d", new Object[] { Integer.valueOf((int)v) });
    } else {
      format = String.format(Locale.ENGLISH, "%s", new Object[] { Double.valueOf(v) });
    } 
    return format;
  }
  
  public static ItemStack newRoll() {
    return addLore(addEnchant((new ItemStack(Items.record_13, 1, 101)).setStackDisplayName("Rick Astley - Never gonna give you up!"), Enchantment.unbreaking, 1), new String[] { "Awesome music to exercise to.", "The greatest gift a pretty fairy could ask for.", "Were you expecting something else?" });
  }
  
  public static ItemStack addEnchant(ItemStack stack, Enchantment enchantment, int level) {
    stack.addEnchantment(enchantment, level);
    return stack;
  }
  
  public static ItemStack addLore(ItemStack a, String... lore) {
    NBTTagCompound tag = a.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound(); 
    if (!tag.hasKey("display", 10))
      tag.setTag("display", (NBTBase)new NBTTagCompound()); 
    NBTTagList l = new NBTTagList();
    for (String s : lore)
      l.appendTag((NBTBase)new NBTTagString(s)); 
    tag.getCompoundTag("display").setTag("Lore", (NBTBase)l);
    a.setTagCompound(tag);
    return a;
  }
  
  public enum NBTIds {
    End(0),
    Byte(1),
    Short(2),
    Int(3),
    Long(4),
    Float(5),
    Double(6),
    ByteArray(7),
    String(8),
    List(9),
    NBT(10),
    IntArray(12);
    
    public final int id;
    
    NBTIds(int i) {
      this.id = i;
    }
  }
  
  public static UUID createUUID(String a, String b, String c, String d) {
    long u = a.hashCode() | b.hashCode() >> 32L;
    long v = c.hashCode() | d.hashCode() >> 32L;
    return new UUID(u, v);
  }
  
  public static NBTTagCompound writeInventoryBasicToNBT(NBTTagCompound tag, InventoryBasic inventoryBasic) {
    if (inventoryBasic.hasCustomInventoryName())
      tag.setString("CustomName", inventoryBasic.getInventoryName()); 
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < inventoryBasic.getSizeInventory(); i++) {
      ItemStack stackInSlot = inventoryBasic.getStackInSlot(i);
      if (stackInSlot != null) {
        NBTTagCompound itemTag = new NBTTagCompound();
        itemTag.setByte("Slot", (byte)i);
        stackInSlot.writeToNBT(itemTag);
        nbttaglist.appendTag((NBTBase)itemTag);
      } 
    } 
    tag.setTag("Items", (NBTBase)nbttaglist);
    return tag;
  }
  
  public static NBTTagCompound readInventoryBasicFromNBT(NBTTagCompound tag, InventoryBasic inventoryBasic) {
    if (tag.hasKey("CustomName", 8))
      inventoryBasic.func_110133_a(tag.getString("CustomName")); 
    NBTTagList items = tag.getTagList("Items", 10);
    for (int i = 0; i < items.tagCount(); i++) {
      NBTTagCompound itemTag = items.getCompoundTagAt(i);
      int j = itemTag.getByte("Slot") & 0xFF;
      if (j >= 0 && j < inventoryBasic.getSizeInventory())
        inventoryBasic.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(itemTag)); 
    } 
    return tag;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\XUHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */