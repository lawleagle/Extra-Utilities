package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemBedrockiumIngot extends Item {
  public ItemBedrockiumIngot() {
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setTextureName("extrautils:bedrockiumIngot");
    setUnlocalizedName("extrautils:bedrockiumIngot");
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


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBedrockiumIngot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */