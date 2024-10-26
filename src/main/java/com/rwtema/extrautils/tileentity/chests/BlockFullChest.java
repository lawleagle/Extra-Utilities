package com.rwtema.extrautils.tileentity.chests;

import com.rwtema.extrautils.ExtraUtils;
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
import net.minecraft.world.World;

public class BlockFullChest extends Block {
  private Random random = (Random)XURandom.getInstance();
  
  IIcon icon_front;
  
  IIcon icon_side;
  
  IIcon icon_top;
  
  public BlockFullChest() {
    super(Material.wood);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(2.5F).setStepSound(soundTypeWood);
    setBlockName("extrautils:chestFull");
  }
  
  public boolean isOpaqueCube() {
    return true;
  }
  
  public boolean renderAsNormalBlock() {
    return true;
  }
  
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack itemstack) {
    byte meta = 0;
    int l = MathHelper.floor_double((p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
    if (l == 0)
      meta = 2; 
    if (l == 1)
      meta = 3; 
    if (l == 2)
      meta = 0; 
    if (l == 3)
      meta = 1; 
    world.setBlockMetadataWithNotify(x, y, z, meta, 3);
    if (itemstack.hasDisplayName())
      ((TileFullChest)world.getTileEntity(x, y, z)).func_145976_a(itemstack.getDisplayName()); 
  }
  
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    TileFullChest tileentitychest = (TileFullChest)world.getTileEntity(x, y, z);
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
    TileFullChest tileentitychest = (TileFullChest)world.getTileEntity(x, y, z);
    player.displayGUIChest(tileentitychest);
    return true;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return new TileFullChest();
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
    return Container.calcRedstoneFromInventory((TileFullChest)world.getTileEntity(x, y, z));
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister p_149651_1_) {
    this.blockIcon = p_149651_1_.registerIcon("planks_oak");
    this.icon_front = p_149651_1_.registerIcon("extrautils:fullblockchest_front");
    this.icon_side = p_149651_1_.registerIcon("extrautils:fullblockchest_side");
    this.icon_top = p_149651_1_.registerIcon("extrautils:fullblockchest_top");
  }
  
  public IIcon getIcon(int side, int meta) {
    if (side <= 1)
      return this.icon_top; 
    if (meta == 2 && side == 2)
      return this.icon_front; 
    if (meta == 3 && side == 5)
      return this.icon_front; 
    if (meta == 0 && side == 3)
      return this.icon_front; 
    if (meta == 1 && side == 4)
      return this.icon_front; 
    return this.icon_side;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\chests\BlockFullChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */