package com.rwtema.extrautils.block;

import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockPureLove extends Block {
  Random rand;
  
  public BlockPureLove() {
    super(Material.iron);
    this.rand = (Random)XURandom.getInstance();
    setBlockName("extrautils:pureLove");
    setBlockTextureName("extrautils:heart");
    setHardness(1.0F);
  }
  
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
    float f = 0.125F;
    return AxisAlignedBB.getBoundingBox(p_149668_2_, p_149668_3_, p_149668_4_, (p_149668_2_ + 1), ((p_149668_3_ + 1) - f), (p_149668_4_ + 1));
  }
  
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (this.rand.nextInt(5) > 0)
      return; 
    if (entity instanceof EntityAnimal) {
      EntityAnimal animal = (EntityAnimal)entity;
      if (animal.getGrowingAge() < 0) {
        animal.addGrowth(this.rand.nextInt(40));
      } else if (animal.getGrowingAge() > 0) {
        int j = animal.getGrowingAge();
        j -= this.rand.nextInt(40);
        if (j < 0)
          j = 0; 
        animal.setGrowingAge(j);
      } else if (!animal.isInLove()) {
        if (world.getEntitiesWithinAABB(entity.getClass(), entity.boundingBox.expand(8.0D, 8.0D, 8.0D)).size() > 32)
          return; 
        animal.func_146082_f(null);
      } 
    } 
  }
  
  public MapColor getMapColor(int p_149728_1_) {
    return MapColor.pinkColor;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockPureLove.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */