package com.rwtema.extrautils.tileentity.enderquarry;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.BlockMultiBlock;
import com.rwtema.extrautils.block.BoxModel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockEnderMarkers extends BlockMultiBlock {
  public static int[] dx = new int[] { 0, 0, 1, -1 };
  
  public static int[] dz = new int[] { 1, -1, 0, 0 };
  
  public BlockEnderMarkers() {
    super(Material.circuits);
    setBlockName("extrautils:endMarker");
    setBlockTextureName("extrautils:endMarker");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setStepSound(soundTypeStone);
  }
  
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return null;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
    int meta = world.getBlockMetadata(x, y, z);
    for (int i = 0; i < 4; i++) {
      if ((meta & 1 << i) != 0)
        for (int l = 0; l < 3; l++)
          world.spawnParticle("reddust", x + 0.5D + dx[i] * rand.nextDouble() * rand.nextDouble(), y + 0.5D, z + 0.5D + dz[i] * rand.nextDouble() * rand.nextDouble(), 0.501D, 0.0D, 1.0D);  
    } 
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return new TileEntityEnderMarker();
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    BoxModel model = new BoxModel();
    model.addBoxI(7, 0, 7, 9, 13, 9).fillIcons((Block)this, 0);
    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
      if (world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite(), false)) {
        model.rotateToSideTex(d);
        return model;
      } 
    } 
    return model;
  }
  
  public BoxModel getInventoryModel(int metadata) {
    BoxModel model = new BoxModel();
    model.addBoxI(7, 0, 7, 9, 13, 9);
    return model;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\BlockEnderMarkers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */