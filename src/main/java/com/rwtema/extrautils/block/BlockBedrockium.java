package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBedrockium extends BlockCompressed {
  public BlockBedrockium() {
    super(Material.rock.getMaterialMapColor());
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:block_bedrockium");
    setBlockTextureName("extrautils:bedrockiumBlock");
    setHardness(1000.0F);
    setResistance(6000000.0F);
  }
  
  public boolean canDropFromExplosion(Explosion par1Explosion) {
    return false;
  }
  
  public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {}
  
  public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
    return false;
  }
  
  public static class ItemBedrockium extends ItemBlock {
    public ItemBedrockium(Block p_i45328_1_) {
      super(p_i45328_1_);
    }
    
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean b) {
      super.onUpdate(itemStack, world, entity, i, b);
      if (entity instanceof EntityLivingBase)
        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 3)); 
    }
    
    public int getEntityLifespan(ItemStack itemStack, World world) {
      return 2147473647;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockBedrockium.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */