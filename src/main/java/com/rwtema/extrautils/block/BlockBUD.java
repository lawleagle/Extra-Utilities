package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.TileEntityBUD;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockBUD extends Block {
    private IIcon[] icons;

    public BlockBUD() {
        super(Material.anvil);
        setBlockName("extrautils:budoff");
        setBlockTextureName("extrautils:budoff");
        setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        setHardness(1.0F);
        setResistance(10.0F).setStepSound(soundTypeStone);
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote &&
            XUHelper.isWrench(player.getCurrentEquippedItem())) {
            int metadata = par1World.getBlockMetadata(par2, par3, par4);
            if (metadata >= 3) {
                metadata = 3 + (metadata - 2) % 7;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata, 3);
            }
        }
        return true;
    }

    public int damageDropped(int par1) {
        return (par1 >= 3) ? 3 : 0;
    }

    public boolean hasTileEntity(int metadata) {
        return (metadata >= 3);
    }

    public TileEntity createTileEntity(World world, int metadata) {
        if (metadata >= 3)
            return (TileEntity)new TileEntityBUD();
        return null;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        this.icons = new IIcon[6];
        this.icons[0] = par1IIconRegister.registerIcon("extrautils:budoff");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:budon");
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:advbudoff");
        this.icons[3] = par1IIconRegister.registerIcon("extrautils:advbudon");
        this.icons[4] = par1IIconRegister.registerIcon("extrautils:advbuddisabledoff");
        this.icons[5] = par1IIconRegister.registerIcon("extrautils:advbuddisabledon");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        if (par2 >= 3)
            return this.icons[2];
        return this.icons[0];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int i = (metadata >= 3) ? 2 : 0;
        if (metadata > 3 && metadata - 4 != Facing.oppositeSide[par5])
            i = 4;
        if (par1IBlockAccess.getTileEntity(par2, par3, par4) instanceof TileEntityBUD)
            return ((TileEntityBUD)par1IBlockAccess.getTileEntity(par2, par3, par4)).getPowered() ? this.icons[i + 1] : this.icons[i];
        return this.icons[i + metadata % 2];
    }

    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        if (metadata == 1)
            return 15;
        if (metadata >= 3) {
            if (metadata > 3 && metadata - 4 != par5)
                return 0;
            if (par1IBlockAccess.getTileEntity(par2, par3, par4) instanceof TileEntityBUD)
                return ((TileEntityBUD)par1IBlockAccess.getTileEntity(par2, par3, par4)).getPowered() ? 15 : 0;
            return 0;
        }
        return 0;
    }

    public boolean canProvidePower() {
        return true;
    }

    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return isProvidingStrongPower(par1IBlockAccess, par2, par3, par4, par5);
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir) {
        return true;
    }

    public int tickRate() {
        return 2;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
        if ((((par5 != this) ? 1 : 0) & ((par5 != Blocks.unpowered_repeater) ? 1 : 0)) != 0) {
            int data = par1World.getBlockMetadata(par2, par3, par4);
            if (data == 0)
                par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate());
        }
    }

    public void updateRedstone(World par1World, int par2, int par3, int par4) {
        par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this);
        par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this);
        par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this);
        par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this);
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 3);
            par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate());
        } else if (par1World.getBlockMetadata(par2, par3, par4) == 1) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 3);
            par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate() * 3);
        } else if (par1World.getBlockMetadata(par2, par3, par4) == 2) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
        }
        updateRedstone(par1World, par2, par3, par4);
    }

    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }

    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        super.isSideSolid(world, x, y, z, side);
        return true;
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockBUD.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
