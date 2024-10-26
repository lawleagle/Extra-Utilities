package com.rwtema.extrautils.tileentity.endercollector;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.BlockMultiBlock;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockEnderCollector extends BlockMultiBlock {
  IIcon side;
  
  IIcon bottom;
  
  IIcon top1;
  
  IIcon top2;
  
  IIcon side_disabled;
  
  IIcon top2_disabled;
  
  public BlockEnderCollector() {
    super(Material.rock);
    setBlockName("extrautils:enderCollector");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.5F).setStepSound(soundTypeStone);
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    return getInventoryModel(world.getBlockMetadata(x, y, z));
  }
  
  public BoxModel getInventoryModel(int metadata) {
    boolean disabled = (metadata >= 6);
    BoxModel boxes = new BoxModel();
    IIcon sideIcon = disabled ? this.side_disabled : this.side;
    boxes.addBoxI(1, 0, 4, 15, 2, 12).setTexture(sideIcon).setTextureSides(new Object[] { this.bottom, this.top1 });
    boxes.addBoxI(4, 0, 1, 12, 2, 15).setTexture(sideIcon).setTextureSides(new Object[] { this.bottom, this.top1 });
    boxes.addBoxI(4, 2, 4, 12, 4, 12).setTexture(sideIcon).setTextureSides(new Object[] { this.bottom, this.top1 });
    boxes.addBoxI(5, 4, 5, 11, 6, 11).setTexture(sideIcon).setTextureSides(new Object[] { this.bottom, this.top1 });
    boxes.addBoxI(6, 6, 6, 10, 16, 10).setTexture(sideIcon).setTextureSides(new Object[] { this.bottom, this.top1 });
    IIcon top2Icon = disabled ? this.top2_disabled : this.top2;
    boxes.addBoxI(6, 10, 1, 10, 14, 15).setTexture(sideIcon).setTextureSides(new Object[] { top2Icon, top2Icon });
    boxes.addBoxI(1, 10, 6, 15, 14, 10).setTexture(sideIcon).setTextureSides(new Object[] { top2Icon, top2Icon });
    boxes.rotateToSideTex(ForgeDirection.getOrientation(metadata % 6));
    return boxes;
  }
  
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileEnderCollector)
      ((TileEnderCollector)tileEntity).onNeighbourChange(); 
  }
  
  public void registerBlockIcons(IIconRegister register) {
    this.side = register.registerIcon("extrautils:enderCollectorSide");
    this.side_disabled = register.registerIcon("extrautils:enderCollectorSide_disabled");
    this.blockIcon = this.bottom = register.registerIcon("extrautils:enderCollectorBottom");
    this.top1 = register.registerIcon("extrautils:enderCollectorTop1");
    this.top2 = register.registerIcon("extrautils:enderCollectorTop2");
    this.top2_disabled = register.registerIcon("extrautils:enderCollectorTop2_disabled");
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return new TileEnderCollector();
  }
  
  public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int meta) {
    return side ^ 0x1;
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileEnderCollector)
      return ((TileEnderCollector)tileEntity).onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ); 
    return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
  }
  
  public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileEnderCollector)
      ((TileEnderCollector)tileEntity).dropItems(); 
    super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\endercollector\BlockEnderCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */