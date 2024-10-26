package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFilingCabinet extends Block implements IBlockTooltip {
    private IIcon[] icon = new IIcon[6];

    public BlockFilingCabinet() {
        super(Material.rock);
        setBlockName("extrautils:filing");
        setHardness(1.5F);
        setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        this.icon[0] = par1IIconRegister.registerIcon("extrautils:filingcabinet");
        this.icon[1] = par1IIconRegister.registerIcon("extrautils:filingcabinet_side");
        this.icon[2] = par1IIconRegister.registerIcon("extrautils:filingcabinet_back");
        this.icon[3] = par1IIconRegister.registerIcon("extrautils:filingcabinet_diamond");
        this.icon[4] = par1IIconRegister.registerIcon("extrautils:filingcabinet_side_diamond");
        this.icon[5] = par1IIconRegister.registerIcon("extrautils:filingcabinet_back_diamond");
    }

    public int damageDropped(int par1) {
        return par1 / 6 % 2;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        int side = par2 % 6;
        int type = par2 / 6;
        if (type > 1)
            return null;
        if (par2 < 2) {
            type = par2;
            if (par1 == 4)
                return this.icon[type * 3];
            if (par1 == 5)
                return this.icon[2 + type * 3];
            return this.icon[1 + type * 3];
        }
        if (par1 == side)
            return this.icon[type * 3];
        if (par1 == Facing.oppositeSide[side])
            return this.icon[2 + type * 3];
        return this.icon[1 + type * 3];
    }

    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public TileEntity createTileEntity(World world, int metadata) {
        return (TileEntity)new TileEntityFilingCabinet();
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote)
            return true;
        TileEntity tile = par1World.getTileEntity(par2, par3, par4);
        par5EntityPlayer.openGui(ExtraUtilsMod.instance, 0, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
        return true;
    }

    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        ArrayList<ItemStack> items = getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        if (world.setBlockToAir(x, y, z)) {
            if (!world.isRemote)
                for (ItemStack item : items) {
                    if (player == null || !player.capabilities.isCreativeMode || item.hasTagCompound())
                        dropBlockAsItem(world, x, y, z, item);
                }
            return true;
        }
        return false;
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ItemStack item = new ItemStack(this, 1, damageDropped(metadata));
        if (world.getTileEntity(x, y, z) instanceof TileEntityFilingCabinet) {
            NBTTagCompound tags = new NBTTagCompound();
            ((TileEntityFilingCabinet)world.getTileEntity(x, y, z)).writeInvToTags(tags);
            if (!tags.hasNoTags())
                item.setTagCompound(tags);
        }
        ret.add(item);
        return ret;
    }

    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        par2EntityPlayer.addExhaustion(0.025F);
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        int l = MathHelper.floor_double((par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
        int metadata = 0;
        if (l == 0)
            metadata = 2;
        if (l == 1)
            metadata = 5;
        if (l == 2)
            metadata = 3;
        if (l == 3)
            metadata = 4;
        metadata += par6ItemStack.getItemDamage() % 2 * 6;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata, 2);
        if (par6ItemStack.hasTagCompound()) {
            TileEntity cabinet = par1World.getTileEntity(par2, par3, par4);
            if (cabinet != null && cabinet instanceof TileEntityFilingCabinet)
                ((TileEntityFilingCabinet)cabinet).readInvFromTags(par6ItemStack.getTagCompound());
        }
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (item.hasTagCompound()) {
            NBTTagCompound tags = item.getTagCompound();
            if (tags.hasKey("item_no")) {
                int n = item.getTagCompound().getInteger("item_no");
                int k = 0;
                for (int i = 0; i < n; i++)
                    k += tags.getCompoundTag("item_" + i).getInteger("Size");
                par3List.add("contains " + k + " item" + XUHelper.s(k) + " of " + n + " type" + XUHelper.s(k));
            }
        }
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
