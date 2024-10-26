package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEtherealStone extends Block {
  private static final int numTypes = 6;

  private IIcon[] icon = new IIcon[16];

  private final boolean[] dark;

  private final boolean[] polarity = new boolean[16];

  public BlockEtherealStone() {
    super(Material.glass);
    for (int i = 3; i < 6; i++)
      this.polarity[i] = true;
    this.dark = new boolean[16];
    this.dark[2] = true;
    this.dark[5] = true;
    setLightOpacity(0);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:etherealglass");
    setBlockTextureName("extrautils:etherealglass");
    setHardness(0.5F);
  }

  @SideOnly(Side.CLIENT) @Override
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
    for (int i = 0; i < 6; i++)
      p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.blockIcon = this.icon[0] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealglass");
    this.icon[1] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealglass1");
    this.icon[2] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/etherealdarkglass");
    this.icon[3] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealglass1");
    this.icon[4] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealglass");
    this.icon[5] = new IconConnectedTexture(par1IIconRegister, "extrautils:ConnectedTextures/untherealdarkglass");
  }

  public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
    int blockMetadata = world.getBlockMetadata(x, y, z);
    return (blockMetadata < 6 && this.polarity[blockMetadata]);
  }

  public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
    if (this.dark[world.getBlockMetadata(x, y, z)])
      return 255;
    return super.getLightOpacity(world, x, y, z);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    return this.icon[meta % 6];
  }

  public int getRenderType() {
    return ExtraUtilsProxy.connectedTextureEtheralID;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public boolean isNormalCube() {
    return false;
  }

  public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
    return false;
  }

  public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
    return super.canCollideCheck(p_149678_1_, p_149678_2_);
  }

  public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
    super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);
  }

  public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bbs, List list, Entity entity) {
    if (this.polarity[world.getBlockMetadata(x, y, z)]) {
      if (entity instanceof net.minecraft.entity.player.EntityPlayer)
        super.addCollisionBoxesToList(world, x, y, z, bbs, list, entity);
    } else {
      if (entity instanceof net.minecraft.entity.player.EntityPlayer &&
        !entity.isSneaking())
        return;
      super.addCollisionBoxesToList(world, x, y, z, bbs, list, entity);
    }
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    int meta = world.getBlockMetadata(x, y, z);
    if (meta < 6 && this.polarity[meta])
      return AxisAlignedBB.getBoundingBox(x, y + 0.001D, z, (x + 1), (y + 1), (z + 1));
    return AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1));
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return (par1IBlockAccess.getBlock(par2, par3, par4) != this);
  }

  public int damageDropped(int p_149692_1_) {
    return p_149692_1_;
  }

  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
    return AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1));
  }

  public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return !this.polarity[par1IBlockAccess.getBlockMetadata(par2, par3, par4) % 6];
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockEtherealStone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
