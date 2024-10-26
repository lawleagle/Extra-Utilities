package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.BlockMultiBlock;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.block.IBlockTooltip;
import com.rwtema.extrautils.block.IMultiBoxBlock;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.texture.TextureMultiIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockGenerator extends BlockMultiBlock implements IMultiBoxBlock, IBlockTooltip {
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof TileEntityGenerator)
      ((TileEntityGenerator)tile).doRandomDisplayTickR(rand);
    super.randomDisplayTick(world, x, y, z, rand);
  }

  private static final int[] rotInd = new int[] { 0, 1, 4, 5, 3, 2 };

  public static int num_gens = 0;

  public static Class<? extends TileEntityGenerator>[] tiles = (Class<? extends TileEntityGenerator>[])new Class[16];

  public static String[] textures = new String[16];

  public static String[] names = new String[16];

  static {
    addName(0, "stone", "Survivalist", (Class)TileEntityGeneratorFurnaceSurvival.class);
    addName(1, "base", "Furnace", (Class)TileEntityGeneratorFurnace.class);
    addName(2, "lava", "Lava", (Class)TileEntityGeneratorMagma.class);
    addName(3, "ender", "Ender", (Class)TileEntityGeneratorEnder.class);
    addName(4, "redflux", "Heated Redstone", (Class)TileEntityGeneratorRedFlux.class);
    addName(5, "food", "Culinary", (Class)TileEntityGeneratorFood.class);
    addName(6, "potion", "Potions", (Class)TileEntityGeneratorPotion.class);
    addName(7, "solar", "Solar", (Class)TileEntityGeneratorSolar.class);
    addName(8, "tnt", "TNT", (Class)TileEntityGeneratorTNT.class);
    addName(9, "pink", "Pink", (Class)TileEntityGeneratorPink.class);
    addName(10, "overclocked", "High-temperature Furnace", (Class)TileEntityGeneratorFurnaceOverClocked.class);
    addName(11, "nether", "Nether Star", (Class)TileEntityGeneratorNether.class);
  }

  public static Random random = (Random)XURandom.getInstance();

  public final int numGenerators;

  private IIcon[][] icons = new IIcon[16][8];

  public BlockGenerator() {
    this(1);
  }

  public BlockGenerator(int numGenerators) {
    super(Material.rock);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:generator" + ((numGenerators > 1) ? ("." + numGenerators) : ""));
    setHardness(5.0F);
    setStepSound(Block.soundTypeMetal);
    this.numGenerators = numGenerators;
  }

  public static void addUpgradeRecipes(Block a, Block b) {
    for (int i = 0; i < num_gens; i++) {
      GameRegistry.addRecipe(new ItemStack(b, 1, i), new Object[] { "SSS", "SES", "SSS", Character.valueOf('S'), new ItemStack(a, 1, i), Character.valueOf('E'), ExtraUtils.transferNodeEnabled ? new ItemStack(ExtraUtils.transferNode, 1, 12) : Blocks.redstone_block });
    }
  }

  public static void addSuperUpgradeRecipes(Block a, Block b) {
    for (int i = 0; i < num_gens; i++) {
      GameRegistry.addRecipe(new ItemStack(b, 1, i), new Object[] { "SSS", "SES", "SSS", Character.valueOf('S'), new ItemStack(a, 1, i), Character.valueOf('E'), ExtraUtils.transferNodeEnabled ? new ItemStack(ExtraUtils.transferNode, 1, 13) : Blocks.redstone_block });
    }
  }

  public static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 0), new Object[] {
          "iii", "ItI", "rfr", Character.valueOf('i'), Blocks.cobblestone, Character.valueOf('I'), Items.iron_ingot, Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone, Character.valueOf('t'), Blocks.piston });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 5), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('I'), new ItemStack(ExtraUtils.generator, 1, 0), Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 1), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('I'), Blocks.iron_block, Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 2), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Items.gold_ingot, Character.valueOf('I'), Blocks.iron_block, Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 3), new Object[] {
          "eee", "iIi", "rfr", Character.valueOf('e'), Items.ender_pearl, Character.valueOf('i'), Items.ender_eye, Character.valueOf('I'), Blocks.iron_block, Character.valueOf('f'),
          Blocks.furnace, Character.valueOf('r'), Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 4), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Blocks.redstone_block, Character.valueOf('I'), new ItemStack(ExtraUtils.generator, 1, 2), Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 6), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Blocks.obsidian, Character.valueOf('I'), Blocks.enchanting_table, Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 7), new Object[] {
          "lql", "qIq", "rfr", Character.valueOf('q'), Items.quartz, Character.valueOf('l'), new ItemStack(Items.dye, 1, 4), Character.valueOf('I'), Blocks.diamond_block, Character.valueOf('f'),
          Blocks.furnace, Character.valueOf('r'), Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 8), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Blocks.tnt, Character.valueOf('I'), Blocks.iron_block, Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 9), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), new ItemStack(Blocks.wool, 1, 6), Character.valueOf('I'), new ItemStack(ExtraUtils.generator, 1, 0), Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 10), new Object[] {
          "iii", "iIi", "rfr", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('I'), new ItemStack(ExtraUtils.generator, 1, 1), Character.valueOf('f'), Blocks.furnace, Character.valueOf('r'),
          Items.redstone });
    GameRegistry.addRecipe(new ItemStack(ExtraUtils.generator, 1, 11), new Object[] {
          "wiw", "wIw", "rfr", Character.valueOf('w'), new ItemStack(Items.skull, 1, 1), Character.valueOf('i'), Items.nether_star, Character.valueOf('I'), ExtraUtils.decorative1Enabled ? new ItemStack((Block)ExtraUtils.decorative1, 1, 5) : Blocks.iron_block, Character.valueOf('f'),
          Blocks.furnace, Character.valueOf('r'), Items.redstone });
  }

  public static void mapTiles() {
    for (int i = 0; i < num_gens; i++)
      ExtraUtils.registerTile(tiles[i], "extrautils:generator" + textures[i]);
  }

  public static void addName(int i, String texture, String name, Class<? extends TileEntityGenerator> clazz) {
    textures[i] = texture;
    names[i] = name;
    tiles[i] = clazz;
    num_gens = Math.max(num_gens, i + 1);
  }

  public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz) {
    if (worldObj.isRemote)
      return true;
    TileEntity tile = worldObj.getTileEntity(x, y, z);
    if (player.getCurrentEquippedItem() != null && tile instanceof IFluidHandler) {
      ItemStack item = player.getCurrentEquippedItem();
      FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
      if (fluid != null && (
        (IFluidHandler)tile).fill(ForgeDirection.getOrientation(side), fluid, false) == fluid.amount) {
        ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side), fluid, true);
        if (!player.capabilities.isCreativeMode)
          player.setCurrentItemOrArmor(0, item.getItem().getContainerItem(item));
        return true;
      }
    }
    player.openGui(ExtraUtilsMod.instance, 0, worldObj, x, y, z);
    return true;
  }

  public int damageDropped(int par1) {
    return par1;
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int x) {
    return this.icons[x][par1];
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    int rot = ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation;
    for (int i = 0; i < rot; i++)
      side = rotInd[side];
    return this.icons[world.getBlockMetadata(x, y, z)][side];
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int i = 0; i < num_gens; i++)
      par3List.add(new ItemStack(par1, 1, i));
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    int w = (this.numGenerators == 64) ? 4 : 3;
    String s = (this.numGenerators > 1) ? ((this.numGenerators > 8) ? "extrautils:supergenerators/sgenerator_" : "extrautils:supergenerators/generator_") : "extrautils:generator_";
    for (int i = 0; i < num_gens; i++) {
      String texture = s + textures[i];
      for (int a = 0; a < w; a++) {
        for (int b = 0; b < 2; b++)
          this.icons[i][a * 2 + b] = TextureMultiIcon.registerGridIcon(par1IIconRegister, texture, a, b, w, 2);
      }
    }
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public boolean hasTileEntity(int metadata) {
    return (tiles[metadata] != null);
  }

  public TileEntity createTileEntity(World world, int metadata) {
    try {
      return tiles[metadata].newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public void prepareForRender(String label) {}

  public BoxModel genericModel(int meta) {
    if (this.numGenerators > 8)
      return superAdvModel(meta);
    if (this.numGenerators > 1)
      return advModel(meta);
    return standardModel(meta);
  }

  public BoxModel superAdvModel(int meta) {
    BoxModel model = new BoxModel();
    model.addBoxI(0, 0, 0, 16, 5, 16);
    model.addBoxI(0, 5, 0, 3, 7, 3);
    model.addBoxI(0, 5, 13, 3, 7, 16);
    model.addBoxI(13, 5, 0, 16, 7, 3);
    model.addBoxI(13, 5, 13, 16, 7, 16);
    model.addBoxI(4, 7, 4, 12, 15, 12).setTextureSides(new Object[] { Integer.valueOf(0), this.icons[meta][6], Integer.valueOf(1), this.icons[meta][7] });
    return model;
  }

  public BoxModel advModel(int meta) {
    BoxModel model = new BoxModel();
    model.addBoxI(0, 0, 0, 16, 4, 16);
    model.addBoxI(0, 12, 0, 16, 16, 16);
    model.addBoxI(3, 3, 3, 13, 13, 13);
    return model;
  }

  public BoxModel standardModel(int meta) {
    BoxModel model = new BoxModel();
    model.addBoxI(2, 14, 3, 14, 15, 13);
    model.addBoxI(1, 9, 2, 15, 14, 14);
    model.addBoxI(1, 8, 2, 15, 9, 6).setTextureSides(new Object[] { Integer.valueOf(3), this.icons[meta][2] });
    model.addBoxI(1, 8, 10, 15, 9, 14);
    model.addBoxI(1, 7, 2, 15, 8, 5);
    model.addBoxI(1, 4, 2, 15, 7, 4);
    model.addBoxI(2, 8, 6, 14, 9, 10);
    model.addBoxI(2, 7, 5, 14, 8, 11).setTextureSides(new Object[] { Integer.valueOf(1), this.icons[meta][0] });
    model.addBoxI(2, 3, 4, 14, 7, 12).setTextureSides(new Object[] { Integer.valueOf(1), this.icons[meta][0] });
    model.addBoxI(2, 2, 5, 14, 3, 11);
    model.addBoxI(2, 1, 6, 14, 2, 10);
    model.addBoxI(0, 1, 1, 1, 15, 2);
    model.addBoxI(0, 1, 14, 1, 15, 15);
    model.addBoxI(15, 1, 1, 16, 15, 2);
    model.addBoxI(15, 1, 14, 16, 15, 15);
    model.addBoxI(1, 0, 1, 15, 1, 2);
    model.addBoxI(1, 0, 14, 15, 1, 15);
    model.addBoxI(1, 1, 1, 2, 2, 2);
    model.addBoxI(14, 1, 1, 15, 2, 2);
    model.addBoxI(1, 1, 14, 2, 2, 15);
    model.addBoxI(14, 1, 14, 15, 2, 15);
    model.addBoxI(0, 15, 2, 1, 16, 14);
    model.addBoxI(15, 15, 2, 16, 16, 14);
    model.addBoxI(0, 14, 2, 1, 15, 3);
    model.addBoxI(0, 14, 13, 1, 15, 14);
    model.addBoxI(15, 14, 2, 16, 15, 3);
    model.addBoxI(15, 14, 13, 16, 15, 14);
    return model;
  }

  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    return genericModel(world.getBlockMetadata(x, y, z)).rotateY((world.getTileEntity(x, y, z) instanceof TileEntityGenerator) ? ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation : 0);
  }

  public BoxModel getInventoryModel(int metadata) {
    return genericModel(metadata).rotateY(1);
  }

  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack item) {
    int l = MathHelper.floor_double((par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) % 4;
    l = (l + 6) % 4;
    ((TileEntityGenerator)world.getTileEntity(x, y, z)).rotation = (byte)l;
    if (item.hasTagCompound())
      ((TileEntityGenerator)world.getTileEntity(x, y, z)).readInvFromTags(item.getTagCompound());
  }

  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
    ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
    if (world.setBlockToAir(x, y, z)) {
      if (!world.isRemote)
        for (ItemStack item : items) {
          if (player == null || !player.capabilities.isCreativeMode || item.hasTagCompound())
            dropBlockAsItem(world, x, y, z, item);
        }
      return true;
    }
    return false;
  }

  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    ItemStack item = new ItemStack((Block)this, 1, damageDropped(metadata));
    if (world.getTileEntity(x, y, z) instanceof TileEntityGenerator) {
      NBTTagCompound tags = new NBTTagCompound();
      ((TileEntityGenerator)world.getTileEntity(x, y, z)).writeInvToTags(tags);
      if (!tags.hasNoTags())
        item.setTagCompound(tags);
    }
    ret.add(item);
    return ret;
  }

  public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock((Block)this)], 1);
    par2EntityPlayer.addExhaustion(0.025F);
  }

  public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
    if (world.getTileEntity(x, y, z) instanceof TileEntityGenerator && world.getTileEntity(x, y, z) instanceof IInventory) {
      IInventory tile = (IInventory)world.getTileEntity(x, y, z);
      for (int i = 0; i < tile.getSizeInventory(); i++) {
        ItemStack itemstack = tile.getStackInSlot(i);
        if (itemstack != null) {
          float f = random.nextFloat() * 0.8F + 0.1F;
          float f1 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;
          for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
            int k1 = random.nextInt(21) + 10;
            if (k1 > itemstack.stackSize)
              k1 = itemstack.stackSize;
            itemstack.stackSize -= k1;
            entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
            float f3 = 0.05F;
            entityitem.motionX = ((float)random.nextGaussian() * f3);
            entityitem.motionY = ((float)random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = ((float)random.nextGaussian() * f3);
            if (itemstack.hasTagCompound())
              entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
          }
        }
      }
    }
    super.breakBlock(world, x, y, z, par5, par6);
  }

  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    par3List.add("Power Multiplier: x" + this.numGenerators);
    if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("Energy"))
      par3List.add(par1ItemStack.getTagCompound().getInteger("Energy") + " / " + (100000 * this.numGenerators) + " RF");
  }

  public boolean hasComparatorInputOverride() {
    return true;
  }

  public int getComparatorInputOverride(World world, int x, int y, int z, int par5) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof TileEntityGenerator)
      return ((TileEntityGenerator)tile).getCompLevel();
    return 0;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\BlockGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
