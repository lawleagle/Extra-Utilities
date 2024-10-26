package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemBlockBedrockium extends ItemBlock {
  public ItemBlockBedrockium(Block p_i45328_1_) {
    super(p_i45328_1_);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }
  
  public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean b) {
    super.onUpdate(itemStack, world, entity, i, b);
    if (entity instanceof EntityLivingBase)
      ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 6)); 
  }
  
  public int getEntityLifespan(ItemStack itemStack, World world) {
    return 2147473647;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockBedrockium.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */