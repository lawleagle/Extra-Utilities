package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.item.IBlockLocalization;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanEnergy;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanFluids;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrashCan extends BlockMultiBlock implements ITileEntityProvider, IBlockLocalization {
  Random random = (Random)XURandom.getInstance();
  
  private IIcon[][] icons;
  
  public BlockTrashCan() {
    super(Material.rock);
    setBlockName("extrautils:trashcan");
    setBlockTextureName("extrautils:trashcan");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(3.5F).setStepSound(soundTypeStone);
  }
  
  public int damageDropped(int p_149692_1_) {
    return p_149692_1_;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    int i = Math.min(side, 2);
    return this.icons[meta % 3][i];
  }
  
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_) {
    for (int i = 0; i < 3; i++)
      p_149666_3_.add(new ItemStack(p_149666_1_, 1, i)); 
  }
  
  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (par1World.isRemote)
      return true; 
    if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityTrashCan) {
      par5EntityPlayer.openGui(ExtraUtilsMod.instance, 0, par1World, par2, par3, par4);
      return true;
    } 
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[3][];
    this.icons[0] = new IIcon[3];
    this.icons[0][0] = par1IIconRegister.registerIcon("extrautils:trashcan_bottom");
    this.icons[0][1] = par1IIconRegister.registerIcon("extrautils:trashcan_top");
    this.icons[0][2] = par1IIconRegister.registerIcon("extrautils:trashcan");
    this.icons[1] = new IIcon[3];
    this.icons[1][0] = par1IIconRegister.registerIcon("extrautils:trashcan1_bottom");
    this.icons[1][1] = par1IIconRegister.registerIcon("extrautils:trashcan1_top");
    this.icons[1][2] = par1IIconRegister.registerIcon("extrautils:trashcan1");
    this.icons[2] = new IIcon[3];
    this.icons[2][0] = par1IIconRegister.registerIcon("extrautils:trashcan2_bottom");
    this.icons[2][1] = par1IIconRegister.registerIcon("extrautils:trashcan2_top");
    this.icons[2][2] = par1IIconRegister.registerIcon("extrautils:trashcan2");
  }
  
  public void prepareForRender(String label) {}
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    return getInventoryModel(0);
  }
  
  public BoxModel getInventoryModel(int metadata) {
    BoxModel model = new BoxModel(0.125F, 0.0F, 0.125F, 0.875F, 0.625F, 0.875F);
    model.add(new Box(0.0625F, 0.625F, 0.0625F, 0.9375F, 0.875F, 0.9375F));
    model.add(new Box(0.3125F, 0.875F, 0.4375F, 0.6875F, 0.9375F, 0.5625F));
    return model;
  }
  
  public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
    TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
    if (tileEntity instanceof TileEntityTrashCan) {
      TileEntityTrashCan tile = (TileEntityTrashCan)tileEntity;
      tile.processInv();
      ItemStack itemstack = tile.getStackInSlot(0);
      if (itemstack != null) {
        float f = this.random.nextFloat() * 0.8F + 0.1F;
        float f1 = this.random.nextFloat() * 0.8F + 0.1F;
        for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld((Entity)entityitem)) {
          int k1 = this.random.nextInt(21) + 10;
          if (k1 > itemstack.stackSize)
            k1 = itemstack.stackSize; 
          itemstack.stackSize -= k1;
          EntityItem entityitem = new EntityItem(par1World, (par2 + f), (par3 + f1), (par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
          float f3 = 0.05F;
          entityitem.motionX = ((float)this.random.nextGaussian() * f3);
          entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
          entityitem.motionZ = ((float)this.random.nextGaussian() * f3);
          if (itemstack.hasTagCompound())
            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy()); 
        } 
      } 
    } 
    super.breakBlock(par1World, par2, par3, par4, par5, par6);
  }
  
  public TileEntity createNewTileEntity(World var1, int meta) {
    if (meta == 1)
      return (TileEntity)new TileEntityTrashCanFluids(); 
    if (meta == 2)
      return (TileEntity)new TileEntityTrashCanEnergy(); 
    return (TileEntity)new TileEntityTrashCan();
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (par1ItemStack.getItemDamage() == 0)
      return getUnlocalizedName(); 
    return getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockTrashCan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */