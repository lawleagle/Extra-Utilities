package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.multipart.FMPBase;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
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
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTransferNode extends BlockTransferPipe {
    public static IIcon nodeBase;

    public static IIcon nodeSideInsert;

    public static IIcon nodeSideExtract;

    public static IIcon nodeSideLiquid;

    public static IIcon nodeSideEnergy;

    public static IIcon nodeSideEnergyHyper;

    public static IIcon particle;

    private final Random random = (Random)XURandom.getInstance();

    private String curBlockLabel = "";

    public BlockTransferNode() {
        super(0);
        setBlockName("extrautils:extractor_base");
        setBlockTextureName("extrautils:extractor_base");
        setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        setHardness(0.5F);
        setStepSound(soundTypeStone);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        nodeBase = par1IIconRegister.registerIcon("extrautils:extractor_base");
        nodeSideEnergy = par1IIconRegister.registerIcon("extrautils:extractor_energy");
        nodeSideEnergyHyper = par1IIconRegister.registerIcon("extrautils:extractor_energy_hyper");
        nodeSideLiquid = par1IIconRegister.registerIcon("extrautils:extractor_liquid");
        nodeSideExtract = par1IIconRegister.registerIcon("extrautils:extractor_extract");
        particle = par1IIconRegister.registerIcon("extrautils:particle");
        super.registerBlockIcons(par1IIconRegister);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if (par2 < 6)
            return (par1 == par2 % 6) ? nodeBase : nodeSideExtract;
        if (par2 < 12)
            return (par1 == par2 % 6) ? nodeBase : nodeSideLiquid;
        if (par2 == 13)
            return nodeSideEnergyHyper;
        return nodeSideEnergy;
    }

    public void prepareForRender(String label) {
        this.curBlockLabel = label;
    }

    public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
        int pipe_type = 0, metadata = 0, node_dir = 0;
        if (!(world.getTileEntity(x, y, z) instanceof INode))
            return new BoxModel();
        INode node = (INode)world.getTileEntity(x, y, z);
        BoxModel boxes = node.getModel(node.getNodeDir());
        BoxModel boxModel1 = getPipeModel(world, x, y, z, (IPipe)null);
        if (boxModel1.size() > 1)
            boxes.addAll((Collection)boxModel1);
        return boxes;
    }

    public BoxModel getInventoryModel(int metadata) {
        if (metadata == 12 || metadata == 13)
            return getEnergyModel();
        return getModel(0);
    }

    public BoxModel getEnergyModel() {
        BoxModel boxes = new BoxModel();
        boxes.add(new Box(0.1875F, 0.3125F, 0.3125F, 0.8125F, 0.6875F, 0.6875F));
        boxes.add(new Box(0.3125F, 0.1875F, 0.3125F, 0.6875F, 0.8125F, 0.6875F));
        boxes.add(new Box(0.3125F, 0.3125F, 0.1875F, 0.6875F, 0.6875F, 0.8125F));
        boxes.add(new Box(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F));
        return boxes;
    }

    public BoxModel getModel(int metadata) {
        ForgeDirection dir = ForgeDirection.getOrientation(metadata);
        BoxModel boxes = new BoxModel();
        float w = 0.125F;
        boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F)).rotateToSide(dir).setTextureSides(new Object[] { Integer.valueOf(dir.ordinal()), nodeBase }));
        boxes.add((new Box(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.25F, 0.8125F)).rotateToSide(dir));
        boxes.add((new Box(0.3125F, 0.25F, 0.3125F, 0.6875F, 0.375F, 0.6875F)).rotateToSide(dir));
        boxes.add((new Box(0.375F, 0.25F, 0.375F, 0.625F, 0.375F, 0.625F)).rotateToSide(dir).setTexture(nodeBase).setAllSideInvisible().setSideInvisible(new Object[] { Integer.valueOf(dir.getOpposite().ordinal()), Boolean.valueOf(false) }));
        return boxes;
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote)
            return true;
        TileEntityTransferNode node = (TileEntityTransferNode)par1World.getTileEntity(par2, par3, par4);
        if (node == null)
            return false;
        if (par5EntityPlayer.getCurrentEquippedItem() != null)
            if (XUHelper.isWrench(par5EntityPlayer.getCurrentEquippedItem())) {
                node.pipe_type = StdPipes.getNextPipeType((IBlockAccess)par1World, par2, par3, par4, node.pipe_type);
                node.markDirty();
                par1World.markBlockForUpdate(par2, par3, par4);
                return true;
            }
        par5EntityPlayer.openGui(ExtraUtilsMod.instance, 0, node.getWorldObj(), node.xCoord, node.yCoord, node.zCoord);
        return true;
    }

    public int damageDropped(int par1) {
        if (par1 < 6)
            return 0;
        if (par1 < 12)
            return 6;
        return par1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
        if (par9 < 12)
            return ForgeDirection.OPPOSITES[par5] + par9 & 0xF;
        return par9 & 0xF;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityTransferNode)
            ((TileEntityTransferNode)par1World.getTileEntity(par2, par3, par4)).updateRedstone();
    }

    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        if (world.getBlock(x, y, z) != FMPBase.getFMPBlockId() &&
            world.getTileEntity(x, y, z) instanceof TileEntityTransferNode) {
            TileEntityTransferNode tile = (TileEntityTransferNode)world.getTileEntity(x, y, z);
            if (!tile.getUpgrades().isEmpty())
                for (int i = 0; i < tile.getUpgrades().size(); i++) {
                    ItemStack itemstack = tile.getUpgrades().get(i);
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;
                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
                        int k1 = this.random.nextInt(21) + 10;
                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;
                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = ((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = ((float)this.random.nextGaussian() * f3);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }
                }
            if (tile instanceof TileEntityTransferNodeInventory) {
                TileEntityTransferNodeInventory tileentity = (TileEntityTransferNodeInventory)tile;
                ItemStack itemstack = tileentity.getStackInSlot(0);
                if (itemstack != null) {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;
                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld((Entity)entityitem)) {
                        int k1 = this.random.nextInt(21) + 10;
                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;
                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = ((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = ((float)this.random.nextGaussian() * f3);
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }
                }
                world.func_147453_f(x, y, z, par5);
            }
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 6));
        par3List.add(new ItemStack(par1, 1, 12));
        par3List.add(new ItemStack(par1, 1, 13));
    }

    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public TileEntity createTileEntity(World world, int metadata) {
        if (metadata == 13)
            return new TileEntityTransferNodeHyperEnergy();
        if (metadata == 12)
            return new TileEntityTransferNodeEnergy();
        if (metadata >= 6 && metadata < 12)
            return new TileEntityTransferNodeLiquid();
        return new TileEntityTransferNodeInventory();
    }

    public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World world, int x, int y, int z, int par5) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityTransferNode) {
            TileEntityTransferNode tile = (TileEntityTransferNode)world.getTileEntity(x, y, z);
            if (tile.buffer.shouldSearch())
                return 15;
        }
        return 0;
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\BlockTransferNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
