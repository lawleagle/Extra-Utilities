package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockColorData extends Block implements ITileEntityProvider {
  public BlockColorData() {
    super(Material.air);
    setLightLevel(0.0F);
    setLightOpacity(0);
    setBlockName("extrautils:datablock");
    setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    setHardness(0.0F);
  }
  
  public static int dataBlockX(int x) {
    return x >> 4 << 4;
  }
  
  public static int dataBlockY(int y) {
    return 255;
  }
  
  public static int dataBlockZ(int z) {
    return z >> 4 << 4;
  }
  
  public static float[] getColorData(IBlockAccess world, int x, int y, int z) {
    return getColorData(world, x, y, z, world.getBlockMetadata(x, y, z));
  }
  
  public static float[] getColorData(IBlockAccess world, int x, int y, int z, int metadata) {
    x = dataBlockX(x);
    y = dataBlockY(y);
    z = dataBlockZ(z);
    TileEntityBlockColorData datablock = null;
    if (world.getTileEntity(x, y, z) instanceof TileEntityBlockColorData) {
      datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
    } else {
      return BlockColor.initColor[metadata];
    } 
    return datablock.palette[metadata];
  }
  
  public static boolean changeColorData(World world, int x, int y, int z, int metadata, float r, float g, float b) {
    x = dataBlockX(x);
    y = dataBlockY(y);
    z = dataBlockZ(z);
    TileEntityBlockColorData datablock = null;
    if (world.getTileEntity(x, y, z) instanceof TileEntityBlockColorData) {
      datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
    } else if (world.isAirBlock(x, y, z)) {
      world.setBlock(x, y, z, ExtraUtils.colorBlockData);
      datablock = (TileEntityBlockColorData)world.getTileEntity(x, y, z);
    } else {
      return false;
    } 
    if (datablock == null)
      return false; 
    datablock.setColor(metadata, r, g, b);
    world.markBlockForUpdate(x, y, z);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {}
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {}
  
  public int getRenderType() {
    return -1;
  }
  
  public int getMobilityFlag() {
    return 1;
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return null;
  }
  
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return null;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isCollidable() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return false;
  }
  
  public boolean isAir(IBlockAccess world, int x, int y, int z) {
    return true;
  }
  
  public TileEntity createNewTileEntity(World var1, int var2) {
    return (TileEntity)new TileEntityBlockColorData();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockColorData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */