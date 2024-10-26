package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.tileentity.TileEntityEnchantedSpike;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSpike extends Block {
  public static final Field experienceField = ReflectionHelper.findField(EntityLiving.class, new String[] { "experienceValue", "field_70728_aV" });

  protected IIcon ironIcon;

  public final ItemStack swordStack;

  public Item itemSpike;

  public BlockSpike() {
    this(Material.iron, new ItemStack(Items.iron_sword));
  }

  public BlockSpike(ItemStack swordStack) {
    this(Material.iron, swordStack);
  }

  public BlockSpike(Material material, ItemStack swordStack) {
    super(material);
    setBlockName("extrautils:spike_base");
    setBlockTextureName("extrautils:spike_base");
    setHardness(5.0F);
    setResistance(500.0F);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setStepSound(Block.soundTypeMetal);
    this.swordStack = swordStack;
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    super.registerBlockIcons(par1IIconRegister);
    this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    int side = par2 % 6;
    if (par1 == Facing.oppositeSide[side])
      return this.blockIcon;
    return this.ironIcon;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public int getRenderType() {
    return ExtraUtilsProxy.spikeBlockID;
  }

  public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
    int meta = par5 % 6;
    int flag = 0;
    ForgeDirection side = ForgeDirection.getOrientation(meta);
    if (!par1World.isSideSolid(par2 - side.offsetX, par3 - side.offsetY, par4 - side.offsetZ, side.getOpposite()))
      for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
        if (side != dir) {
          if (par1World.isSideSolid(par2 - dir.offsetX, par3 - dir.offsetY, par4 - dir.offsetZ, dir.getOpposite()))
            return flag + dir.ordinal();
          if (par1World.getBlock(par2 - dir.offsetX, par3 - dir.offsetY, par4 - dir.offsetZ) == this)
            par5 = par1World.getBlockMetadata(par2 - dir.offsetX, par3 - dir.offsetY, par4 - dir.offsetZ) % 6;
        }
      }
    return flag + par5;
  }

  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
    if (!player.capabilities.isCreativeMode && canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {
      ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      if (world.setBlockToAir(x, y, z)) {
        if (!world.isRemote)
          for (ItemStack item : items)
            dropBlockAsItem(world, x, y, z, item);
        return true;
      }
      return false;
    }
    return super.removedByPlayer(world, player, x, y, z, willHarvest);
  }

  public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
    par2EntityPlayer.addExhaustion(0.025F);
  }

  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    return getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
  }

  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
    return getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
  }

  protected ItemStack createStackedBlock(int p_149644_1_) {
    return new ItemStack(this.itemSpike, 1, 0);
  }

  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = getItemDropped(metadata, world.rand, fortune);
    ItemStack stack = new ItemStack(item, 1, damageDropped(metadata));
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof TileEntityEnchantedSpike)
      stack.setTagCompound(((TileEntityEnchantedSpike)tile).getEnchantmentTagList());
    ret.add(stack);
    return ret;
  }

  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
    NBTTagList enchantments = stack.getEnchantmentTagList();
    if (enchantments != null) {
      int newmeta = 6 + world.getBlockMetadata(x, y, z) % 6;
      world.setBlock(x, y, z, this, newmeta, 2);
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileEntityEnchantedSpike)
        ((TileEntityEnchantedSpike)tile).setEnchantmentTagList(enchantments);
    }
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    double h = 0.0625D;
    int side = par1World.getBlockMetadata(par2, par3, par4) % 6;
    switch (side) {
      case 0:
        return AxisAlignedBB.getBoundingBox(par2 + h, par3 + h, par4 + h, par2 + 1.0D - h, par3 + 1.0D, par4 + 1.0D - h);
      case 1:
        return AxisAlignedBB.getBoundingBox(par2 + h, par3, par4 + h, par2 + 1.0D - h, par3 + 1.0D - h, par4 + 1.0D - h);
      case 2:
        return AxisAlignedBB.getBoundingBox(par2 + h, par3 + h, par4 + h, par2 + 1.0D - h, par3 + 1.0D - h, par4 + 1.0D);
      case 3:
        return AxisAlignedBB.getBoundingBox(par2 + h, par3 + h, par4, par2 + 1.0D - h, par3 + 1.0D - h, par4 + 1.0D - h);
      case 4:
        return AxisAlignedBB.getBoundingBox(par2 + h, par3 + h, par4 + h, par2 + 1.0D, par3 + 1.0D - h, par4 + 1.0D - h);
      case 5:
        return AxisAlignedBB.getBoundingBox(par2, par3 + h, par4 + h, par2 + 1.0D - h, par3 + 1.0D - h, par4 + 1.0D - h);
    }
    return AxisAlignedBB.getBoundingBox(par2 + h, par3 + h, par4 + h, par2 + 1.0D - h, par3 + 1.0D - h, par4 + 1.0D - h);
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity target) {
    boolean flag;
    if (world.isRemote || !(world instanceof WorldServer))
      return;
    FakePlayer fakeplayer = FakePlayerFactory.getMinecraft((WorldServer)world);
    if (!(target instanceof EntityLivingBase))
      return;
    TileEntity tile = world.getTileEntity(x, y, z);
    float damage = getDamageMultipliers(4.0F, tile, (EntityLivingBase)target);
    float h = ((EntityLivingBase)target).getHealth();
    if (h > damage || target instanceof EntityPlayer) {
      flag = target.attackEntityFrom(DamageSource.cactus, damage - 0.001F);
    } else if (world.rand.nextInt(5) == 0) {
      flag = doPlayerLastHit(world, target, tile);
    } else {
      flag = target.attackEntityFrom(DamageSource.cactus, 400.0F);
    }
    if (flag) {
      doPostAttack(world, target, tile, x, y, z);
      if (target instanceof EntityLiving)
        try {
          experienceField.setInt(target, 0);
        } catch (IllegalAccessException ignore) {}
    }
  }

  public void doPostAttack(World world, Entity target, TileEntity tile, int x, int y, int z) {
    if (!(tile instanceof TileEntityEnchantedSpike))
      return;
    ItemStack stack = this.swordStack.copy();
    stack.setTagCompound(((TileEntityEnchantedSpike)tile).getEnchantmentTagList());
    int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
    if (i > 0) {
      int m = world.getBlockMetadata(x, y, z) % 6;
      float dx = Facing.offsetsXForSide[m];
      float dz = Facing.offsetsZForSide[m];
      target.addVelocity((-dx * i * 0.5F), 0.1D, (-dz * i * 0.5F));
    }
    int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
    if (j > 0)
      target.setFire(j * 4);
  }

  public Item getSwordItem() {
    return Items.iron_sword;
  }

  public float getDamageMultipliers(float f, TileEntity tile, EntityLivingBase target) {
    if (!(tile instanceof TileEntityEnchantedSpike))
      return f;
    ItemStack swordStack = new ItemStack(getSwordItem(), 1, 0);
    swordStack.setTagCompound(((TileEntityEnchantedSpike)tile).getEnchantmentTagList());
    float f1 = EnchantmentHelper.func_152377_a(swordStack, target.getCreatureAttribute());
    if (f1 > 0.0F)
      f += f1;
    return f;
  }

  public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    if (par7Entity instanceof net.minecraft.entity.item.EntityItem) {
      AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(par2, par3, par4, (par2 + 1), (par3 + 1), (par4 + 1));
      if (axisalignedbb1 != null && par5AxisAlignedBB.intersectsWith(axisalignedbb1))
        par6List.add(axisalignedbb1);
    } else {
      super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }
  }

  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return (world.getBlockMetadata(x, y, z) % 6 == side.getOpposite().ordinal());
  }

  public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
    return (world.getBlockMetadata(x, y, z) % 6 == 1 || super.canPlaceTorchOnTop(world, x, y, z));
  }

  public boolean hasTileEntity(int metadata) {
    return (metadata >= 6);
  }

  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityEnchantedSpike();
  }

  public boolean doPlayerLastHit(World world, Entity target, TileEntity tile) {
    FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)world);
    try {
      ItemStack stack = this.swordStack.copy();
      if (tile instanceof TileEntityEnchantedSpike)
        stack.setTagCompound(((TileEntityEnchantedSpike)tile).getEnchantmentTagList());
      fakePlayer.setCurrentItemOrArmor(0, stack);
      boolean b = target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)fakePlayer), 400.0F);
      fakePlayer.setCurrentItemOrArmor(0, null);
      b = target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)fakePlayer), 400.0F) | b;
      b = target.attackEntityFrom(DamageSource.cactus, 400.0F) | b;
      return b;
    } finally {
      fakePlayer.setCurrentItemOrArmor(0, null);
    }
  }

  public static class DamageSourceSpike extends EntityDamageSource {
    public DamageSourceSpike(String p_i1567_1_, Entity p_i1567_2_) {
      super(p_i1567_1_, p_i1567_2_);
    }
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return this.itemSpike;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockSpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
