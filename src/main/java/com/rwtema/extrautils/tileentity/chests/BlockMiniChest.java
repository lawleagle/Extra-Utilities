package com.rwtema.extrautils.tileentity.chests;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.BlockMultiBlock;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMiniChest extends BlockMultiBlock {
  private Random random = (Random)XURandom.getInstance();
  
  IIcon icon_front;
  
  IIcon icon_side;
  
  IIcon icon_top;
  
  public BlockMiniChest() {
    super(Material.wood);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("extrautils:chestMini");
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack itemstack) {
    byte meta = 0;
    int l = MathHelper.floor_double((p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
    if (l == 0)
      meta = 0; 
    if (l == 1)
      meta = 1; 
    if (l == 2)
      meta = 2; 
    if (l == 3)
      meta = 3; 
    world.setBlockMetadataWithNotify(x, y, z, meta, 3);
    if (itemstack.hasDisplayName())
      ((TileMiniChest)world.getTileEntity(x, y, z)).func_145976_a(itemstack.getDisplayName()); 
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    BoxModel boxes = new BoxModel();
    boxes.addBoxI(5, 0, 5, 11, 6, 11).fillIcons((Block)this, 0);
    boxes.rotateY(world.getBlockMetadata(x, y, z) & 0x3);
    return boxes;
  }
  
  public BoxModel getInventoryModel(int metadata) {
    BoxModel boxes = new BoxModel();
    boxes.addBoxI(5, 0, 5, 11, 6, 11).fillIcons((Block)this, 0).rotateY(1);
    return boxes;
  }
  
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    TileMiniChest tileentitychest = (TileMiniChest)world.getTileEntity(x, y, z);
    if (tileentitychest != null)
      for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); i1++) {
        ItemStack itemstack = tileentitychest.getStackInSlot(i1);
        if (itemstack != null) {
          float f = this.random.nextFloat() * 0.8F + 0.1F;
          float f1 = this.random.nextFloat() * 0.8F + 0.1F;
          for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
            int j1 = this.random.nextInt(21) + 10;
            if (j1 > itemstack.stackSize)
              j1 = itemstack.stackSize; 
            itemstack.stackSize -= j1;
            EntityItem entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
            float f3 = 0.05F;
            entityitem.motionX = ((float)this.random.nextGaussian() * f3);
            entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = ((float)this.random.nextGaussian() * f3);
            if (itemstack.hasTagCompound())
              entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy()); 
          } 
        } 
      }  
    super.breakBlock(world, x, y, z, block, meta);
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
    if (world.isRemote)
      return true; 
    player.openGui(ExtraUtilsMod.instance, 0, world, x, y, z);
    return true;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return new TileMiniChest();
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
    return Container.calcRedstoneFromInventory((TileMiniChest)world.getTileEntity(x, y, z));
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister p_149651_1_) {
    this.blockIcon = p_149651_1_.registerIcon("planks_oak");
    this.icon_front = p_149651_1_.registerIcon("extrautils:minichest_front");
    this.icon_side = p_149651_1_.registerIcon("extrautils:minichest_side");
    this.icon_top = p_149651_1_.registerIcon("extrautils:minichest_top");
  }
  
  public IIcon getIcon(int side, int p_149691_2_) {
    if (side <= 1)
      return this.icon_top; 
    if (side == 2)
      return this.icon_front; 
    return this.icon_side;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\chests\BlockMiniChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */