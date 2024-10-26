package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockChandelier extends Block {
  public static int range = 1;
  
  public static int[] dx = new int[] { -1, 1, 0, 0, 0, 0 };
  
  public static int[] dy = new int[] { 0, 0, 0, 0, -1, 1 };
  
  public static int[] dz = new int[] { 0, 0, -1, 1, 0, 0 };
  
  public BlockChandelier() {
    super(Material.circuits);
    setLightLevel(1.0F);
    setLightOpacity(0);
    setBlockName("extrautils:chandelier");
    setBlockTextureName("extrautils:chandelier");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(0.1F);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public int getRenderType() {
    return 1;
  }
  
  public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
    if (par1World.isSideSolid(par2, par3 + 1, par4, ForgeDirection.DOWN, true))
      return true; 
    Block id = par1World.getBlock(par2, par3 + 1, par4);
    return (id == Blocks.fence || id == Blocks.nether_brick_fence || id == Blocks.glass || id == Blocks.cobblestone_wall);
  }
  
  private boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4) {
    if (!canPlaceBlockAt(par1World, par2, par3, par4)) {
      if (par1World.getBlock(par2, par3, par4) == this) {
        dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
        par1World.setBlockToAir(par2, par3, par4);
      } 
      return false;
    } 
    return true;
  }
  
  public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
    if (!canPlaceBlockAt(world, x, y, z)) {
      dropBlockAsItem(world, x, y, z, 0, 0);
      world.setBlockToAir(x, y, z);
    } 
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityAntiMobTorch();
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
    double i = rand.nextInt(5);
    double t = (2.0D + i * 3.0D) / 16.0D;
    if (rand.nextBoolean()) {
      world.spawnParticle("smoke", x + t, y + Math.abs(i - 2.0D) * 2.0D / 16.0D, z + t, 0.0D, 0.0D, 0.0D);
      world.spawnParticle("flame", x + t, y + Math.abs(i - 2.0D) * 2.0D / 16.0D, z + t, 0.0D, 0.0D, 0.0D);
    } else {
      world.spawnParticle("smoke", x + t, y + Math.abs(i - 2.0D) * 2.0D / 16.0D, (z + 1) - t, 0.0D, 0.0D, 0.0D);
      world.spawnParticle("flame", x + t, y + Math.abs(i - 2.0D) * 2.0D / 16.0D, (z + 1) - t, 0.0D, 0.0D, 0.0D);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockChandelier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */