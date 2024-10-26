package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtilsProxy;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockMultiBlock extends Block implements IMultiBoxBlock {
    public BlockMultiBlock(Material xMaterial) {
        super(xMaterial);
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return ExtraUtilsProxy.multiBlockID;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        List models = getWorldModel((IBlockAccess)par1World, par2, par3, par4);
        if (models == null || models.size() == 0)
            return;
        for (Object model : models) {
            Box b = (Box)model;
            AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(((par2 + b.offsetx) + b.minX), ((par3 + b.offsety) + b.minY), ((par4 + b.offsetz) + b.minZ), ((par2 + b.offsetx) + b.maxX), ((par3 + b.offsety) + b.maxY), ((par4 + b.offsetz) + b.maxZ));
            if (axisalignedbb1 != null && par5AxisAlignedBB.intersectsWith(axisalignedbb1))
                par6List.add(axisalignedbb1);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z) {
        Box bounds = BoxModel.boundingBox(getWorldModel(par1IBlockAccess, x, y, z));
        if (bounds != null)
            setBlockBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockMultiBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
