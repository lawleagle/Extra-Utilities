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
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class BlockSpikeGold extends BlockSpike {
  public BlockSpikeGold() {
    super(new ItemStack(Items.golden_sword));
    setBlockName("extrautils:spike_base_gold");
    setBlockTextureName("extrautils:spike_base_gold");
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    super.registerBlockIcons(par1IIconRegister);
    this.ironIcon = par1IIconRegister.registerIcon("extrautils:spike_side_gold");
  }
  
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity target) {
    if (world.isRemote || !(world instanceof WorldServer))
      return; 
    FakePlayer fakeplayer = FakePlayerFactory.getMinecraft((WorldServer)world);
    if (target instanceof EntityLivingBase) {
      TileEntity tile = world.getTileEntity(x, y, z);
      float damage = getDamageMultipliers(4.0F, tile, (EntityLivingBase)target);
      float h = ((EntityLivingBase)target).getHealth();
      boolean flag = false;
      if (h > damage || target instanceof net.minecraft.entity.player.EntityPlayer) {
        flag = target.attackEntityFrom(DamageSource.cactus, damage);
      } else if (h > 0.5F) {
        flag = target.attackEntityFrom(DamageSource.generic, h - 0.001F);
      } else if (world.rand.nextInt(3) == 0) {
        flag = doPlayerLastHit(world, target, tile);
      } else {
        flag = target.attackEntityFrom(DamageSource.cactus, 40.0F);
      } 
      if (flag)
        doPostAttack(world, target, tile, x, y, z); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockSpikeGold.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */