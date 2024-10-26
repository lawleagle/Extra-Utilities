package com.rwtema.extrautils.tileentity.enderquarry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockDummy extends Block {
  public BlockDummy(Material par2Material) {
    super(par2Material);
    throw new RuntimeException("This block is a dummy and must never be assigned");
  }
  
  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public void onBlockPreDestroy(World par1World, int par2, int par3, int par4, int par5) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
    throw new RuntimeException("This block's methods must never be called");
  }
  
  public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
    throw new RuntimeException("This block's methods must never be called");
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\BlockDummy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */