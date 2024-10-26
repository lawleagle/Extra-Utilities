package com.rwtema.extrautils.tileentity.enderquarry;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEnderQuarry extends Block {
  int[] tiletype = new int[] { 
      0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 
      8, 9, 10, 11, 12, 13 };
  
  IIcon[] top = new IIcon[3], bottom = new IIcon[3], side = new IIcon[3];
  
  public BlockEnderQuarry() {
    super(Material.rock);
    setBlockName("extrautils:enderQuarry");
    setBlockTextureName("extrautils:enderQuarry");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setStepSound(soundTypeStone);
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return new TileEntityEnderQuarry();
  }
  
  public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
    TileEntity tile;
    if (tile = par1World.getTileEntity(par2, par3, par4) instanceof TileEntityEnderQuarry)
      ((TileEntityEnderQuarry)tile).detectInventories(); 
  }
  
  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (par1World.isRemote)
      return true; 
    TileEntity tile;
    if (tile = par1World.getTileEntity(par2, par3, par4) instanceof TileEntityEnderQuarry) {
      ((TileEntityEnderQuarry)tile).startFencing(par5EntityPlayer);
      if (par5EntityPlayer.getHeldItem() == null && par5EntityPlayer.capabilities.isCreativeMode && par5EntityPlayer.isSneaking() && ((TileEntityEnderQuarry)tile).started) {
        par5EntityPlayer.addChatComponentMessage((IChatComponent)new ChatComponentText("Overclock Mode Activated"));
        ((TileEntityEnderQuarry)tile).debug();
      } 
    } 
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.top[2] = par1IIconRegister.registerIcon("extrautils:enderQuarry_top");
    this.top[0] = par1IIconRegister.registerIcon("extrautils:enderQuarry_top");
    this.top[1] = par1IIconRegister.registerIcon("extrautils:enderQuarry_top_active");
    this.bottom[2] = par1IIconRegister.registerIcon("extrautils:enderQuarry_bottom");
    this.bottom[1] = par1IIconRegister.registerIcon("extrautils:enderQuarry_bottom");
    this.bottom[0] = par1IIconRegister.registerIcon("extrautils:enderQuarry_bottom");
    this.side[0] = this.blockIcon = par1IIconRegister.registerIcon("extrautils:enderQuarry");
    this.side[1] = par1IIconRegister.registerIcon("extrautils:enderQuarry_active");
    this.side[2] = par1IIconRegister.registerIcon("extrautils:enderQuarry_finished");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par2 > 2 || par2 < 0)
      par2 = 0; 
    if (par1 == 0)
      return this.bottom[par2]; 
    if (par1 == 1)
      return this.top[par2]; 
    return this.side[par2];
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\BlockEnderQuarry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */