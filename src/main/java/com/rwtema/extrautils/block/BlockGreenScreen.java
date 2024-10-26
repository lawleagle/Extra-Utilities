package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGreenScreen extends Block {
  public BlockGreenScreen() {
    super(Material.cloth);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:greenscreen");
    setBlockTextureName("extrautils:greenscreen");
    setLightOpacity(0);
    setHardness(1.0F);
    setResistance(10.0F);
  }

  private static final float[][] cols = new float[][] {
      { 1.0F, 1.0F, 1.0F }, { 1.0F, 0.5F, 0.0F }, { 1.0F, 0.0F, 1.0F }, { 0.0F, 0.5F, 0.85F }, { 1.0F, 1.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 1.0F, 0.6F, 0.65F }, { 0.5F, 0.5F, 0.5F }, { 0.8F, 0.8F, 0.8F }, { 0.0F, 1.0F, 1.0F },
      { 0.7F, 0.2F, 1.0F }, { 0.0F, 0.0F, 1.0F }, { 0.5F, 0.2F, 0.0F }, { 0.0F, 0.6F, 0.0F }, { 1.0F, 0.0F, 0.0F }, { 0.0F, 0.0F, 0.0F } };

  public static int getLightLevel(int metadata) {
    return (int)((cols[metadata][0] + cols[metadata][1] + cols[metadata][2]) / 3.0F * 15.0F);
  }

  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    if (world instanceof World &&
      !((World)world).blockExists(x, y, z))
      return 0;
    return getLightLevel(world.getBlockMetadata(x, y, z));
  }

  @SideOnly(Side.CLIENT)
  public int getRenderColor(int p_149741_1_) {
    if (p_149741_1_ == 15)
      return 0;
    float[] col = cols[p_149741_1_ & 0xF];
    return (int)(col[0] * 255.0F) << 16 | (int)(col[1] * 255.0F) << 8 | (int)(col[2] * 255.0F);
  }

  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
    return getRenderColor(world.getBlockMetadata(x, y, z));
  }

  public int damageDropped(int p_149692_1_) {
    return p_149692_1_;
  }

  @SideOnly(Side.CLIENT) @Override
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
    for (int i = 0; i < 16; i++)
      p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

  public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
    return true;
  }

  public int getRenderType() {
    return ExtraUtilsProxy.fullBrightBlockID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockGreenScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
