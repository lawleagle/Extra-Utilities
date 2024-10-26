package com.rwtema.extrautils.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockSpikeDiamond extends BlockSpike {
  public ItemStack lootah = newLootah();
  
  public static ItemStack newLootah() {
    ItemStack lootah = new ItemStack(Items.diamond_sword);
    lootah.setItemDamage(lootah.getMaxDamage());
    return lootah;
  }
  
  public BlockSpikeDiamond() {
    super(new ItemStack(Items.diamond_sword));
    setBlockName("extrautils:spike_base_diamond");
    setBlockTextureName("extrautils:spike_base_diamond");
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    super.registerBlockIcons(par1IIconRegister);
    this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_diamond");
  }
  
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity target) {
    if (world.isRemote || !(world instanceof net.minecraft.world.WorldServer))
      return; 
    boolean flag = false;
    if (target instanceof EntityLivingBase) {
      TileEntity tile = world.getTileEntity(x, y, z);
      float damage = getDamageMultipliers(10.0F, tile, (EntityLivingBase)target);
      float h = ((EntityLivingBase)target).getHealth();
      if (h > damage || target instanceof net.minecraft.entity.player.EntityPlayer) {
        flag = target.attackEntityFrom(DamageSource.cactus, damage - 0.01F);
      } else if (h > 0.5F) {
        flag = target.attackEntityFrom(DamageSource.generic, h - 0.001F);
      } else {
        flag = doPlayerLastHit(world, target, tile);
      } 
      if (flag)
        doPostAttack(world, target, tile, x, y, z); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockSpikeDiamond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */