package com.rwtema.extrautils.tileentity.transfernodes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRetrievalNode extends BlockTransferNode {
  public static IIcon nodeSideLiquidRemote;

  public static IIcon nodeSideExtractRemote;

  public BlockRetrievalNode() {
    setBlockName("extrautils:extractor_base_remote");
    setBlockTextureName("extrautils:extractor_base");
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    nodeSideLiquidRemote = par1IIconRegister.registerIcon("extrautils:extractor_liquid_remote");
    nodeSideExtractRemote = par1IIconRegister.registerIcon("extrautils:extractor_extract_remote");
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par2 < 6)
      return (par1 == par2 % 6) ? nodeBase : nodeSideExtractRemote;
    if (par2 < 12)
      return (par1 == par2 % 6) ? nodeBase : nodeSideLiquidRemote;
    return nodeSideEnergy;
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(new ItemStack(par1, 1, 6));
  }

  public boolean hasTileEntity(int metadata) {
    return true;
  }

  public TileEntity createTileEntity(World world, int metadata) {
    if (metadata == 12)
      return new TileEntityTransferNodeEnergy();
    if (metadata >= 6 && metadata < 12)
      return new TileEntityRetrievalNodeLiquid();
    return new TileEntityRetrievalNodeInventory();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\BlockRetrievalNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
