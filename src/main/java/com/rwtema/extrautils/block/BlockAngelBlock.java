package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAngelBlock extends Block {
  public BlockAngelBlock() {
    super(Material.rock);
    setBlockName("extrautils:angelBlock");
    setBlockTextureName("extrautils:angelBlock");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setStepSound(Block.soundTypeStone);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockAngelBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */