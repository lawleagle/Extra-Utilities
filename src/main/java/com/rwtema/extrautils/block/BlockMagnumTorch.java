package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMagnumTorch extends Block {
  private IIcon iconTop;
  
  private IIcon iconBase;
  
  public BlockMagnumTorch() {
    super(Material.circuits);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:magnumTorch");
    setBlockTextureName("extrautils:magnumTorch");
    setHardness(1.2F);
    setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    setLightLevel(1.0F);
    setLightOpacity(0);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.blockIcon = par1IIconRegister.registerIcon("extrautils:magnumTorch");
    this.iconTop = par1IIconRegister.registerIcon("extrautils:magnumTorchTop");
    this.iconBase = par1IIconRegister.registerIcon("extrautils:magnumTorchBase");
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    switch (par1) {
      case 0:
        return this.iconBase;
      case 1:
        return this.iconTop;
    } 
    return this.blockIcon;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityAntiMobTorch();
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    float dx = (float)((Math.random() * 2.0D - 1.0D) * 0.125D);
    float dy = (float)((Math.random() * 2.0D - 1.0D) * 0.0625D);
    float dz = (float)((Math.random() * 2.0D - 1.0D) * 0.125D);
    par1World.spawnParticle("smoke", (par2 + 0.5F + dx), ((par3 + 1) + dy), (par4 + 0.5F + dz), 0.0D, 0.0D, 0.0D);
    par1World.spawnParticle("flame", (par2 + 0.5F + dx), ((par3 + 1) + dy), (par4 + 0.5F + dz), 0.0D, 0.0D, 0.0D);
  }
  
  public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
    return par1World.isSideSolid(par2, par3 - 1, par4, ForgeDirection.UP, true);
  }
  
  public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
    if (!canPlaceBlockAt(par1World, par2, par3, par4) && 
      par1World.getBlock(par2, par3, par4) == this) {
      dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
      par1World.setBlockToAir(par2, par3, par4);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockMagnumTorch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */