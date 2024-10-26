package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTimer extends Block {
  int powered_x = 0;
  
  int powered_y = 0;
  
  int powered_z = 0;
  
  private boolean powered = true;
  
  private boolean changing = false;
  
  public BlockTimer() {
    super(Material.rock);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:timer");
    setBlockTextureName("extrautils:timer");
    setHardness(1.0F);
  }
  
  public int getTickRate(int metadata) {
    return 20;
  }
  
  public int getMobilityFlag() {
    return 2;
  }
  
  public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return (this.powered && par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1) ? 15 : 0;
  }
  
  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isBlockNormalCube() {
    return false;
  }
  
  public boolean canProvidePower() {
    return true;
  }
  
  public void onBlockAdded(World world, int x, int y, int z) {
    int metadata = world.getBlockMetadata(x, y, z);
    world.scheduleBlockUpdate(x, y, z, this, getTickRate(metadata << 1) - 2);
  }
  
  public void updateTick(World world, int x, int y, int z, Random rand) {
    int metadata = world.getBlockMetadata(x, y, z);
    if (metadata == 0) {
      this.changing = true;
      world.setBlockMetadataWithNotify(x, y, z, 1, 1);
      this.changing = false;
      world.scheduleBlockUpdate(x, y, z, this, 2);
    } else if (metadata == 1) {
      world.setBlockMetadataWithNotify(x, y, z, 0, 1);
      this.powered = false;
      boolean p = (world.getBlockPowerInput(x, y, z) > 0);
      this.powered = true;
      if (p) {
        this.changing = true;
        world.setBlockMetadataWithNotify(x, y, z, 2, 0);
        this.changing = false;
      } else {
        world.scheduleBlockUpdate(x, y, z, this, getTickRate(metadata) - 2);
      } 
    } 
  }
  
  public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
    if (this.changing)
      return; 
    int metadata = world.getBlockMetadata(x, y, z);
    this.powered = false;
    boolean p = (world.getBlockPowerInput(x, y, z) > 0);
    this.powered = true;
    if (metadata == 0 && p) {
      world.setBlockMetadataWithNotify(x, y, z, 2, 0);
    } else if (metadata == 2 && !p) {
      world.setBlockMetadataWithNotify(x, y, z, 0, 0);
      world.scheduleBlockUpdate(x, y, z, this, getTickRate(metadata));
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */