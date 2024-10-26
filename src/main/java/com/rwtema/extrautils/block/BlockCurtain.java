package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCurtain extends BlockMultiBlock {
  public BlockCurtain() {
    super(Material.cloth);
    setCreativeTab(CreativeTabs.tabBlock);
    setLightOpacity(8);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:curtains");
    setBlockTextureName("extrautils:curtains");
  }
  
  @SideOnly(Side.CLIENT)
  public String getItemIconName() {
    return "extrautils:curtains";
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getClothModel(IBlockAccess world, int x, int y, int z, float width) {
    BoxModel model = new BoxModel();
    for (int i = 2; i < 6; i++) {
      ForgeDirection dir = ForgeDirection.getOrientation(i);
      Block id = world.getBlock(x + dir.offsetX, y, z + dir.offsetZ);
      if (id == this || id.isOpaqueCube())
        model.add((new Box(0.0F, 0.0F, 0.5F - width, 1.0F, 0.5F + width, 0.5F + width)).rotateToSide(dir)); 
    } 
    if (model.isEmpty()) {
      model.add(new Box(0.0F, 0.0F, 0.5F - width, 1.0F, 1.0F, 0.5F + width));
      model.add(new Box(0.5F - width, 0.0F, 0.0F, 0.5F + width, 1.0F, 1.0F));
    } 
    return model;
  }
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    float width = 0.025F;
    return getClothModel(world, x, y, z, width);
  }
  
  public BoxModel getInventoryModel(int metadata) {
    return new BoxModel(0.0F, 0.0F, 0.0F, 0.05F, 1.0F, 1.0F);
  }
  
  public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {}
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    Box b = BoxModel.boundingBox(getClothModel((IBlockAccess)par1World, par2, par3, par4, 0.1875F));
    return AxisAlignedBB.getBoundingBox(par2 + b.minX, par3 + b.minY, par4 + b.minZ, par2 + b.maxX, par3 + b.maxY, par4 + b.maxZ);
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockCurtain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */