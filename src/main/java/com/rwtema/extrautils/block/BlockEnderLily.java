package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnderLily extends BlockCrops {
  public static final long period_fast = 3700L;
  
  public static final long period = 24000L;
  
  public static final long period_grass = 96000L;
  
  public BlockEnderLily() {
    setBlockTextureName("extrautils:plant/ender_lilly");
    setBlockName("extrautils:plant/ender_lilly");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }
  
  public boolean canBlockStay(World world, int x, int y, int z) {
    return (super.canBlockStay(world, x, y, z) || canBePlantedHere(world, x, y, z));
  }
  
  protected void checkAndDropBlock(World p_149855_1_, int p_149855_2_, int p_149855_3_, int p_149855_4_) {
    if (!canBlockStay(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_) && !isEndStone(p_149855_1_, p_149855_2_, p_149855_3_ - 1, p_149855_4_)) {
      dropBlockAsItem(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_, p_149855_1_.getBlockMetadata(p_149855_2_, p_149855_3_, p_149855_4_), 0);
      p_149855_1_.setBlock(p_149855_2_, p_149855_3_, p_149855_4_, getBlockById(0), 0, 2);
    } 
  }
  
  public int getRenderType() {
    return 1;
  }
  
  public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
    return !(entity instanceof net.minecraft.entity.boss.EntityDragon);
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (par5EntityPlayer.capabilities.isCreativeMode && par5EntityPlayer.getCurrentEquippedItem() == null && par5EntityPlayer.isSneaking()) {
      if (world.isRemote)
        return true; 
      world.setBlockMetadataWithNotify(x, y, z, (world.getBlockMetadata(x, y, z) + 1) % 8, 2);
    } 
    return false;
  }
  
  public boolean canBePlantedHere(World world, int x, int y, int z) {
    return ((world.isAirBlock(x, y, z) && (canThisPlantGrowOnThisBlockID(world.getBlock(x, y - 1, z)) || isEndStone(world, x, y - 1, z) || isSuperEndStone(world, x, y - 1, z))) || isFluid(world, x, y - 1, z));
  }
  
  public boolean isWater(World world, int x, int y, int z) {
    Block block = world.getBlock(x, y, z);
    if (block != Blocks.water && block != Blocks.flowing_water)
      return false; 
    return (world.getBlockMetadata(x, y, z) == 0);
  }
  
  protected boolean canThisPlantGrowOnThisBlockID(Block par1) {
    return (par1 == Blocks.grass || par1 == Blocks.dirt || par1 == Blocks.end_stone || par1 == Blocks.farmland);
  }
  
  public boolean isSuperEndStone(World world, int x, int y, int z) {
    Block id = world.getBlock(x, y, z);
    return (id instanceof BlockDecoration && ((BlockDecoration)id).isSuperEnder[world.getBlockMetadata(x, y, z)]);
  }
  
  public boolean isEndStone(World world, int x, int y, int z) {
    Block id = world.getBlock(x, y, z);
    return (id == Blocks.end_stone || (id instanceof BlockDecoration && ((BlockDecoration)id).isEnder[world.getBlockMetadata(x, y, z)]));
  }
  
  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    checkAndDropBlock(par1World, par2, par3, par4);
    int l = par1World.getBlockMetadata(par2, par3, par4);
    if (l < 7)
      if (isSuperEndStone(par1World, par2, par3 - 1, par4)) {
        if (par5Random.nextInt(40) == 0) {
          l++;
          par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        } 
      } else if (isEndStone(par1World, par2, par3 - 1, par4)) {
        if (((l % 2 == 0) ? true : false) == ((par1World.getWorldTime() % 48000L < 24000L) ? true : false) && 
          par5Random.nextInt(10) == 0) {
          l++;
          par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
        } 
      } else if (((l % 2 == 0) ? true : false) == ((par1World.getWorldTime() % 192000L < 96000L) ? true : false) && 
        par5Random.nextInt(40) == 0) {
        l++;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
      }  
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    if (par5Random.nextInt(5) != 0 || par1World.getBlockMetadata(par2, par3, par4) < 7)
      return; 
    double d0 = (par2 + par5Random.nextFloat());
    double d1 = (par3 + par5Random.nextFloat());
    d0 = (par4 + par5Random.nextFloat());
    double d2 = 0.0D;
    double d3 = 0.0D;
    double d4 = 0.0D;
    int i1 = par5Random.nextInt(2) * 2 - 1;
    int j1 = par5Random.nextInt(2) * 2 - 1;
    d2 = (par5Random.nextFloat() - 0.5D) * 0.125D;
    d3 = (par5Random.nextFloat() - 0.5D) * 0.125D;
    d4 = (par5Random.nextFloat() - 0.5D) * 0.125D;
    double d5 = par4 + 0.5D + 0.25D * j1;
    d4 = (par5Random.nextFloat() * 1.0F * j1);
    double d6 = par2 + 0.5D + 0.25D * i1;
    d2 = (par5Random.nextFloat() * 1.0F * i1);
    par1World.spawnParticle("portal", d6, d1, d5, d2, d3, d4);
  }
  
  public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
    if (par1World.getBlockMetadata(par2, par3, par4) >= 3) {
      if (par5Entity instanceof EntityItem) {
        ItemStack item = ((EntityItem)par5Entity).getEntityItem();
        if (item != null && (item.getItem() == getSeedItem() || item.getItem() == getCropItem()))
          return; 
        if (par1World.isRemote)
          par1World.spawnParticle("crit", par5Entity.posX, par5Entity.posY, par5Entity.posZ, 0.0D, 0.0D, 0.0D); 
      } 
      if (par5Entity instanceof net.minecraft.entity.monster.EntityEnderman)
        return; 
      par5Entity.attackEntityFrom(DamageSource.cactus, 0.1F);
    } 
  }
  
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    ret.add(new ItemStack(getSeedItem(), 1, 0));
    if (metadata >= 7) {
      ret.add(new ItemStack(getCropItem(), 1, 0));
      if (isEndStone(world, x, y - 1, z))
        while (world.rand.nextInt(50) == 0)
          ret.add(new ItemStack(getSeedItem(), 1, 0));  
      if (isSuperEndStone(world, x, y - 1, z))
        while (world.rand.nextInt(20) == 0)
          ret.add(new ItemStack(getSeedItem(), 1, 0));  
    } 
    return ret;
  }
  
  public void func_149863_m(World par1World, int par2, int par3, int par4) {
    int l = par1World.getBlockMetadata(par2, par3, par4);
    if (l == 0) {
      par1World.func_147480_a(par2, par3, par4, true);
    } else {
      l -= MathHelper.getRandomIntegerInRange(par1World.rand, 1, 5);
      if (l < 0)
        l = 0; 
      par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
    } 
  }
  
  protected Item getSeedItem() {
    return Item.getItemFromBlock((Block)this);
  }
  
  protected Item getCropItem() {
    return Items.ender_pearl;
  }
  
  @SideOnly(Side.CLIENT)
  public String getItemIconName() {
    return "extrautils:ender_lilly_seed";
  }
  
  public boolean isFluid(World world, int x, int y, int z) {
    return isWater(world, x, y, z);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockEnderLily.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */