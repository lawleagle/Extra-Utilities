package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.BlockMultiBlock;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockEnderConstructor extends BlockMultiBlock {
  public IIcon[] icons = new IIcon[10];
  
  Random rand;
  
  public BlockEnderConstructor() {
    super(Material.rock);
    this.rand = (Random)XURandom.getInstance();
    setBlockName("extrautils:endConstructor");
    setBlockTextureName("extrautils:enderConstructor_pillar_bottom");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setResistance(10.0F).setStepSound(soundTypeStone);
  }
  
  public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
    TileEntity tile = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
    if (tile != null && tile instanceof TileEnderConstructor) {
      InventoryKraft inventoryKraft = ((TileEnderConstructor)tile).inv;
      for (int i1 = 0; i1 < 9; i1++) {
        ItemStack itemstack = inventoryKraft.getStackInSlot(i1);
        dropItem(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, itemstack);
      } 
      if (((TileEnderConstructor)tile).outputslot != null)
        dropItem(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, ((TileEnderConstructor)tile).outputslot); 
      p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
    } 
    super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
  }
  
  public void dropItem(World p_149749_1_, float p_149749_2_, float p_149749_3_, float p_149749_4_, ItemStack itemstack) {
    if (itemstack != null) {
      float f = this.rand.nextFloat() * 0.8F + 0.1F;
      float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
      for (float f2 = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; p_149749_1_.spawnEntityInWorld((Entity)entityitem)) {
        int j1 = this.rand.nextInt(21) + 10;
        if (j1 > itemstack.stackSize)
          j1 = itemstack.stackSize; 
        itemstack.stackSize -= j1;
        EntityItem entityitem = new EntityItem(p_149749_1_, (p_149749_2_ + f), (p_149749_3_ + f1), (p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
        float f3 = 0.05F;
        entityitem.motionX = ((float)this.rand.nextGaussian() * f3);
        entityitem.motionY = ((float)this.rand.nextGaussian() * f3 + 0.2F);
        entityitem.motionZ = ((float)this.rand.nextGaussian() * f3);
        if (itemstack.hasTagCompound())
          entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy()); 
      } 
    } 
  }
  
  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    Block block = world.getBlock(x, y, z);
    if (block != null && block != this)
      return block.getLightValue(world, x, y, z); 
    return (world.getBlockMetadata(x, y, z) % 2 == 1) ? 10 : 0;
  }
  
  public void prepareForRender(String label) {}
  
  public int damageDropped(int par1) {
    return par1 - par1 % 2 & 0xF;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.icons[1] = this.blockIcon = par1IIconRegister.registerIcon(getTextureName());
    this.icons[0] = this.blockIcon = par1IIconRegister.registerIcon(getTextureName());
    this.icons[2] = par1IIconRegister.registerIcon("extrautils:enderConstructor_top");
    this.icons[3] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_top");
    this.icons[4] = par1IIconRegister.registerIcon("extrautils:enderConstructor_side");
    this.icons[5] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar");
    this.icons[6] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_enabled");
    this.icons[7] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_top_enabled");
    this.icons[8] = par1IIconRegister.registerIcon("extrautils:enderConstructor_top_enabled");
    this.icons[9] = par1IIconRegister.registerIcon("extrautils:enderConstructor_side_enabled");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par2 == 2) {
      if (par1 == 0)
        return this.icons[1]; 
      if (par1 == 1)
        return this.icons[3]; 
      return this.icons[5];
    } 
    if (par2 == 3) {
      if (par1 == 0)
        return this.icons[1]; 
      if (par1 == 1)
        return this.icons[7]; 
      return this.icons[6];
    } 
    if (par2 == 1) {
      if (par1 == 0)
        return this.icons[0]; 
      if (par1 == 1)
        return this.icons[8]; 
      return this.icons[9];
    } 
    if (par1 == 0)
      return this.icons[0]; 
    if (par1 == 1)
      return this.icons[2]; 
    return this.icons[4];
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(new ItemStack(par1, 1, 2));
  }
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    int metadata = world.getBlockMetadata(x, y, z);
    BoxModel model = getInventoryModel(metadata);
    if (metadata == 2 || metadata == 3) {
      model.fillIcons((Block)this, metadata);
      for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
        if (world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite(), false)) {
          model.rotateToSideTex(d);
          return model;
        } 
      } 
    } 
    return model;
  }
  
  public BoxModel getInventoryModel(int metadata) {
    if (metadata == 2 || metadata == 3) {
      BoxModel model = new BoxModel();
      model.add(new Box(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.4375F, 0.9375F));
      model.add(new Box(0.25F, 0.4375F, 0.25F, 0.75F, 0.9375F, 0.75F));
      return model;
    } 
    BoxModel box = BoxModel.newStandardBlock();
    return box;
  }
  
  public void miscInit() {}
  
  public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List<AxisAlignedBB> par6List, Entity par7Entity) {
    BoxModel boxModel = getWorldModel((IBlockAccess)par1World, par2, par3, par4);
    if (boxModel == null || boxModel.size() == 0)
      return; 
    Box b = BoxModel.boundingBox((List)boxModel);
    AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(((par2 + b.offsetx) + b.minX), ((par3 + b.offsety) + b.minY), ((par4 + b.offsetz) + b.minZ), ((par2 + b.offsetx) + b.maxX), ((par3 + b.offsety) + b.maxY), ((par4 + b.offsetz) + b.maxZ));
    if (axisalignedbb1 != null && par5AxisAlignedBB.intersectsWith(axisalignedbb1))
      par6List.add(axisalignedbb1); 
  }
  
  public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int side, float dx, float dy, float dz) {
    int metadata = worldObj.getBlockMetadata(x, y, z);
    if (metadata <= 1 || metadata == 4) {
      if (worldObj.isRemote)
        return true; 
      player.openGui(ExtraUtilsMod.instance, 0, worldObj, x, y, z);
      return true;
    } 
    return false;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    if (metadata == 2 || metadata == 3)
      return new TileEnderPillar(); 
    return new TileEnderConstructor();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\BlockEnderConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */