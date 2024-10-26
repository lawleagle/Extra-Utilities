package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.ItemBlockDrum;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.tileentity.TileEntityDrum;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class BlockDrum extends Block {
  public static IIcon drum_side;
  
  public static IIcon drum_top;
  
  public static IIcon drum_side2;
  
  private static IIcon drum_top2;
  
  public BlockDrum() {
    super(Material.rock);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.5F);
    setBlockName("extrautils:drum");
    setBlockBounds(0.07499999F, 0.0F, 0.07499999F, 0.925F, 1.0F, 0.925F);
  }
  
  public boolean hasComparatorInputOverride() {
    return true;
  }
  
  public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
    if (world.getTileEntity(x, y, z) instanceof TileEntityDrum) {
      TileEntityDrum drum = (TileEntityDrum)world.getTileEntity(x, y, z);
      FluidTankInfo tank = drum.getTankInfo(ForgeDirection.UP)[0];
      if (tank == null || tank.fluid == null || tank.fluid.amount == 0)
        return 0; 
      double t = tank.fluid.amount * 14.0D / tank.capacity;
      if (t < 0.0D)
        t = 0.0D; 
      if (t > 15.0D)
        t = 14.0D; 
      return (int)Math.floor(t) + 1;
    } 
    return 0;
  }
  
  public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    if (!par1World.isRemote) {
      if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityDrum) {
        String s;
        TileEntityDrum drum = (TileEntityDrum)par1World.getTileEntity(par2, par3, par4);
        FluidTankInfo tank = drum.getTankInfo(ForgeDirection.UP)[0];
        ItemStack item = par5EntityPlayer.getCurrentEquippedItem();
        if (item != null) {
          if (item.getItem() == Items.stick || (par5EntityPlayer.isSneaking() && XUHelper.isWrench(item))) {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
            return true;
          } 
          if (FluidContainerRegistry.isEmptyContainer(item)) {
            ItemStack filled = FluidContainerRegistry.fillFluidContainer(tank.fluid, item);
            if (filled != null) {
              int a = (FluidContainerRegistry.getFluidForFilledItem(filled)).amount;
              if (par5EntityPlayer.capabilities.isCreativeMode) {
                drum.drain(ForgeDirection.DOWN, a, true);
              } else if (item.stackSize == 1) {
                par5EntityPlayer.setCurrentItemOrArmor(0, filled);
                drum.drain(ForgeDirection.DOWN, a, true);
              } else if (par5EntityPlayer.inventory.addItemStackToInventory(filled)) {
                item.stackSize--;
                drum.drain(ForgeDirection.DOWN, a, true);
                if (par5EntityPlayer instanceof EntityPlayerMP)
                  ((EntityPlayerMP)par5EntityPlayer).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)par5EntityPlayer); 
              } 
              if ((drum.getTankInfo(ForgeDirection.DOWN)[0]).fluid == null)
                par1World.markBlockForUpdate(par2, par3, par4); 
              return true;
            } 
          } else if (FluidContainerRegistry.isFilledContainer(item)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(item);
            if (drum.fill(ForgeDirection.UP, fluidStack, false) == fluidStack.amount) {
              if (par5EntityPlayer.capabilities.isCreativeMode) {
                drum.fill(ForgeDirection.UP, fluidStack, true);
              } else {
                ItemStack c = null;
                if (item.getItem().hasContainerItem(item))
                  c = item.getItem().getContainerItem(item); 
                if (c == null || item.stackSize == 1 || par5EntityPlayer.inventory.addItemStackToInventory(c)) {
                  drum.fill(ForgeDirection.UP, fluidStack, true);
                  if (item.stackSize == 1) {
                    par5EntityPlayer.setCurrentItemOrArmor(0, c);
                  } else if (item.stackSize > 1) {
                    item.stackSize--;
                  } 
                } 
              } 
              return true;
            } 
          } 
        } 
        FluidStack fluid = tank.fluid;
        if (fluid != null) {
          s = XUHelper.getFluidName(fluid) + ": " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(fluid.amount) }) + " / " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(tank.capacity) });
        } else {
          s = "Empty: 0 / " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(tank.capacity) });
        } 
        PacketTempChat.sendChat(par5EntityPlayer, s);
        return true;
      } 
      return false;
    } 
    return true;
  }
  
  public boolean renderAsNormalBlock() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return (par1 <= 1) ? ((par2 == 1) ? drum_top2 : drum_top) : ((par2 == 1) ? drum_side2 : drum_side);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    drum_side = par1IIconRegister.registerIcon("extrautils:drum_side");
    drum_side2 = par1IIconRegister.registerIcon("extrautils:drum_side2");
    drum_top = par1IIconRegister.registerIcon("extrautils:drum_top");
    drum_top2 = par1IIconRegister.registerIcon("extrautils:drum_top2");
  }
  
  public int getRenderType() {
    return ExtraUtilsProxy.drumRendererID;
  }
  
  @SideOnly(Side.CLIENT)
  public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
    if (world.getTileEntity(x, y, z) instanceof TileEntityDrum)
      return ((TileEntityDrum)world.getTileEntity(x, y, z)).getColor(); 
    return 16777215;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityDrum(metadata);
  }
  
  public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
    if (!player.capabilities.isCreativeMode && canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {
      ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      if (world.setBlockToAir(x, y, z)) {
        if (!world.isRemote)
          for (ItemStack item : items)
            dropBlockAsItem(world, x, y, z, item);  
        return true;
      } 
      return false;
    } 
    return super.removedByPlayer(world, player, x, y, z, willHarvest);
  }
  
  public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
    par2EntityPlayer.addExhaustion(0.025F);
  }
  
  public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
    if (par6ItemStack.hasTagCompound() && 
      par6ItemStack.getItem() instanceof ItemBlockDrum) {
      TileEntity drum = par1World.getTileEntity(par2, par3, par4);
      if (drum != null && drum instanceof TileEntityDrum) {
        FluidStack fluid = ((ItemBlockDrum)par6ItemStack.getItem()).drain(par6ItemStack, 2147483647, false);
        ((TileEntityDrum)drum).setCapacityFromMetadata(par6ItemStack.getItemDamage());
        if (fluid != null)
          ((TileEntityDrum)drum).fill(ForgeDirection.UP, fluid, true); 
      } 
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(new ItemStack(par1, 1, 1));
    if (par2CreativeTabs != null)
      return; 
    FluidRegistry.getRegisteredFluidIDs().keySet().iterator();
    for (Fluid fluid1 : FluidRegistry.getRegisteredFluids().values()) {
      ItemStack drum = new ItemStack(par1, 1, 0), drum2 = new ItemStack(par1, 1, 1);
      FluidStack fluid = FluidRegistry.getFluidStack(fluid1.getName(), TileEntityDrum.getCapacityFromMetadata(1));
      if (fluid != null) {
        ((ItemBlockDrum)par1).fill(drum, fluid, true);
        par3List.add(drum);
        ((ItemBlockDrum)par1).fill(drum2, fluid, true);
        par3List.add(drum2);
      } 
    } 
  }
  
  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    return getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
  }
  
  public int damageDropped(int meta) {
    return meta;
  }
  
  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
    ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    ItemStack item = new ItemStack(this, 1, damageDropped(metadata));
    if (world.getTileEntity(x, y, z) instanceof TileEntityDrum && item.getItem() instanceof ItemBlockDrum) {
      FluidStack fluid = (((TileEntityDrum)world.getTileEntity(x, y, z)).getTankInfo(ForgeDirection.UP)[0]).fluid;
      if (fluid != null)
        ((ItemBlockDrum)item.getItem()).fill(item, fluid, true); 
    } 
    ret.add(item);
    return ret;
  }
  
  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    if (!par1World.isRemote) {
      TileEntity drum = par1World.getTileEntity(par2, par3, par4);
      if (drum instanceof TileEntityDrum)
        ((TileEntityDrum)drum).ticked(); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockDrum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */