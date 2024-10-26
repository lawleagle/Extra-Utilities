package com.rwtema.extrautils.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockSpikeWood extends BlockSpike {
  public BlockSpikeWood() {
    super(Material.wood, new ItemStack(Items.wooden_sword));
    setBlockName("extrautils:spike_base_wood");
    setBlockTextureName("extrautils:spike_base_wood");
    setHardness(2.0F);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    super.registerBlockIcons(par1IIconRegister);
    this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_wood");
  }
  
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity target) {
    if (world.isRemote || !(world instanceof net.minecraft.world.WorldServer))
      return; 
    if (target instanceof EntityLivingBase) {
      float h = ((EntityLivingBase)target).getHealth();
      float damage = getDamageMultipliers(1.0F, world.getTileEntity(x, y, z), (EntityLivingBase)target);
      if (h > damage && 
        target.attackEntityFrom(DamageSource.cactus, damage))
        doPostAttack(world, target, world.getTileEntity(x, y, z), x, y, z); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockSpikeWood.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */