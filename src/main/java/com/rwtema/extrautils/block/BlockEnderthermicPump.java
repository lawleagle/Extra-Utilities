package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.TileEntityEnderThermicLavaPump;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEnderthermicPump extends Block {
  IIcon pump;

  IIcon pumpTop;

  IIcon pumpBottom;

  public BlockEnderthermicPump() {
    super(Material.rock);
    setBlockName("extrautils:enderThermicPump");
    setBlockTextureName("extrautils:enderThermicPump");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setStepSound(soundTypeStone);
  }

  public boolean hasTileEntity(int metadata) {
    return true;
  }

  @SideOnly(Side.CLIENT) @Override
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.pump = par1IIconRegister.registerIcon("extrautils:enderThermicPump_side");
    this.pumpTop = par1IIconRegister.registerIcon("extrautils:enderThermicPump_top");
    this.pumpBottom = par1IIconRegister.registerIcon("extrautils:enderThermicPump");
    super.registerBlockIcons(par1IIconRegister);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par1 == 0)
      return this.pumpBottom;
    if (par1 == 1)
      return this.pumpTop;
    return this.pump;
  }

  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityEnderThermicLavaPump();
  }

  public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
    TileEntity tile = par1World.getTileEntity(par2, par3, par4);
    if (tile instanceof TileEntityEnderThermicLavaPump && par5EntityLiving instanceof EntityPlayer)
      ((TileEntityEnderThermicLavaPump)tile).owner = (EntityPlayer)par5EntityLiving;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockEnderthermicPump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
