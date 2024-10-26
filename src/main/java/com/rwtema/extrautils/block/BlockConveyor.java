package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConveyor extends Block implements IMultiBoxBlock {
  ItemStack potionEmptyStack = new ItemStack(Items.glass_bottle);
  
  private IIcon[] icons;
  
  public BlockConveyor() {
    super(Material.iron);
    setBlockName("extrautils:conveyor");
    setBlockTextureName("extrautils:conveyor");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(5.0F);
    setStepSound(soundTypeStone);
  }
  
  public int getRenderType() {
    return ExtraUtilsProxy.multiBlockID;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[3];
    this.icons[0] = par1IIconRegister.registerIcon("extrautils:conveyor_top");
    this.icons[1] = par1IIconRegister.registerIcon("extrautils:conveyor_side");
    this.icons[2] = (IIcon)new IconFlipped(this.icons[1], true, false);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    IIcon t = prevIcon(par1, par2);
    if (par2 % 2 == 1)
      if (t == this.icons[1]) {
        t = this.icons[2];
      } else if (t == this.icons[2]) {
        t = this.icons[1];
      }  
    if (shouldFlip(par1))
      if (t == this.icons[1]) {
        t = this.icons[2];
      } else if (t == this.icons[2]) {
        t = this.icons[1];
      }  
    return t;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon prevIcon(int par1, int par2) {
    if (par1 <= 1)
      return this.icons[0]; 
    if (par2 % 2 == 0) {
      if (par1 <= 3)
        return this.icons[0]; 
      if (par1 == 4 && par2 == 0)
        return this.icons[2]; 
      if (par1 == 5 && par2 == 2)
        return this.icons[2]; 
    } 
    if (par2 % 2 == 1) {
      if (par1 > 3)
        return this.icons[0]; 
      if (par1 == 2 && par2 == 1)
        return this.icons[2]; 
      if (par1 == 3 && par2 == 3)
        return this.icons[2]; 
    } 
    return this.icons[1];
  }
  
  public boolean shouldFlip(int side) {
    return (side == 3 || side == 2);
  }
  
  public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
    int var6 = ((MathHelper.floor_double((par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) + 2) % 4;
    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
  }
  
  public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity par5Entity) {
    double m_speed = 0.05D;
    int a = par1World.getBlockMetadata(x, y, z);
    int[] ax = { 0, 1, 0, -1 };
    int[] az = { -1, 0, 1, 0 };
    if (a > 3)
      return; 
    if (par5Entity != null && par5Entity.posY > y + 0.5D && !par5Entity.isSneaking()) {
      if (par5Entity instanceof EntityItem) {
        ItemStack my_item = null;
        my_item = ((EntityItem)par5Entity).getEntityItem();
        if (my_item != null)
          for (int j = 0; j < 4; j++) {
            if (a % 2 != j % 2 && 
              par1World.getTileEntity(x + ax[j % 4], y - 1, z + az[j % 4]) instanceof IInventory) {
              IInventory chest = (IInventory)par1World.getTileEntity(x + ax[j % 4], y - 1, z + az[j % 4]);
              boolean hasItem = false;
              boolean hasSpace = false;
              for (int i = 0; i < chest.getSizeInventory(); i++) {
                ItemStack ch_item = chest.getStackInSlot(i);
                if (ch_item != null) {
                  if (ch_item.getItem() == my_item.getItem() && (
                    ch_item.getItem().isDamageable() || ch_item.getItemDamage() == my_item.getItemDamage())) {
                    hasItem = true;
                    if (ch_item.stackSize < ch_item.getItem().getItemStackLimit(ch_item) && ch_item.stackSize < chest.getInventoryStackLimit())
                      hasSpace = true; 
                  } 
                } else {
                  hasSpace = true;
                } 
                if (hasItem && hasSpace) {
                  a = j % 4;
                  par5Entity.isAirBorne = true;
                  break;
                } 
              } 
            } 
          }  
      } 
      if (ax[a] == 0 && Math.abs(x + 0.5D - par5Entity.posX) < 0.5D && Math.abs(x + 0.5D - par5Entity.posX) > 0.1D)
        par5Entity.motionX += Math.signum(x + 0.5D - par5Entity.posX) * Math.min(m_speed, Math.abs(x + 0.5D - par5Entity.posX)) / 1.2D; 
      if (az[a] == 0 && Math.abs(z + 0.5D - par5Entity.posZ) < 0.5D && Math.abs(z + 0.5D - par5Entity.posZ) > 0.1D)
        par5Entity.motionZ += Math.signum(z + 0.5D - par5Entity.posZ) * Math.min(m_speed, Math.abs(z + 0.5D - par5Entity.posZ)) / 1.2D; 
      if (par5Entity instanceof EntityItem) {
        double jump_vel = 0.19D;
        double jump_point = 0.0D;
        boolean jump = (par1World.isAirBlock(x, y + 2, z) && (par1World.getBlock(x + ax[a], y + 1, z + az[a]) == this || par1World.getBlock(x + ax[a], y + 1, z + az[a]) == Blocks.hopper));
        if (!jump && 
          !par1World.isAirBlock(x + ax[a], y, z + az[a]) && par1World.getBlock(x + ax[a], y, z + az[a]) != this && !par1World.isAirBlock(x + ax[a], y + 1, z)) {
          jump = true;
          jump_vel = 0.07D;
          jump_point = 0.3D;
        } 
        if (jump) {
          double progress = (par5Entity.posX - x - 0.5D) * ax[a] + (par5Entity.posZ - z - 0.5D) * az[a];
          double prog_speed = par5Entity.motionX * ax[a] + par5Entity.motionZ * az[a];
          double prog_counterspeed = Math.abs(par5Entity.motionX * az[a] + par5Entity.motionZ * ax[a]);
          if (progress > jump_point || (progress > jump_point - 0.2D && prog_speed < 0.0D)) {
            a = (a + 2) % 4;
          } else if (progress + 1.5D * prog_speed > jump_point && prog_speed >= m_speed && prog_counterspeed < 0.2D) {
            par5Entity.onGround = false;
            par5Entity.isAirBorne = true;
            if (ax[a] == 0)
              par5Entity.motionX = 0.0D; 
            if (az[a] == 0)
              par5Entity.motionZ = 0.0D; 
            par5Entity.addVelocity(0.0D, jump_vel * 2.0D, 0.0D);
            return;
          } 
        } 
      } 
      par5Entity.motionX += ax[a] * m_speed;
      par5Entity.motionZ += az[a] * m_speed;
    } 
  }
  
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    float var5 = 0.0625F;
    return AxisAlignedBB.getBoundingBox(par2, par3, par4, (par2 + 1), ((par3 + 1) - var5), (par4 + 1));
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return AxisAlignedBB.getBoundingBox(par2, par3, par4, (par2 + 1), (par3 + 1), (par4 + 1));
  }
  
  public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
    return true;
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    return getModel(world.getBlockMetadata(x, y, z));
  }
  
  public BoxModel getInventoryModel(int metadata) {
    return getModel(1);
  }
  
  public BoxModel getModel(int metadata) {
    Box main = new Box(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    main.renderAsNormalBlock = true;
    main.uvRotateBottom = main.uvRotateTop = metadata % 2;
    return new BoxModel(main);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockConveyor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */