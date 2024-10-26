package com.rwtema.extrautils.tileentity.generators;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;

public class TileEntityGeneratorNether extends TileEntityGeneratorFurnace {
  public double genLevel() {
    return 40000.0D;
  }
  
  public int transferLimit() {
    return 100000;
  }
  
  @SideOnly(Side.CLIENT)
  public void doRandomDisplayTickR(Random random) {
    super.doRandomDisplayTickR(random);
    if (shouldSoundPlay()) {
      int col = Potion.wither.getLiquidColor();
      double d0 = (col >> 16 & 0xFF) / 255.0D;
      double d1 = (col >> 8 & 0xFF) / 255.0D;
      double d2 = (col >> 0 & 0xFF) / 255.0D;
      this.worldObj.spawnParticle("mobSpell", (x() + random.nextFloat()), y() + 0.9D, (z() + random.nextFloat()), d0, d1, d2);
      for (int i = 0; i < 50; i++) {
        double dx = x() + 0.5D - 10.0D + random.nextInt(22);
        double dy = y() + 0.5D - 10.0D + random.nextInt(22);
        double dz = z() + 0.5D - 10.0D + random.nextInt(22);
        if (getDistanceFrom(dx, dy, dz) < 100.0D)
          this.worldObj.spawnParticle("mobSpell", dx, dy, dz, d0, d1, d2); 
      } 
    } 
  }
  
  private final int range = 10;
  
  public void doSpecial() {
    if (this.coolDown > 0.0D)
      for (Object ent : this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x(), y(), z(), (x() + 1), (y() + 1), (z() + 1)).expand(10.0D, 10.0D, 10.0D))) {
        double d = 10.0D - ((EntityLivingBase)ent).getDistance(x() + 0.5D, y() + 0.5D, z() + 0.5D);
        if (d > 0.0D)
          if (!(ent instanceof EntityPlayer) || !((EntityPlayer)ent).capabilities.isCreativeMode) {
            ((EntityLivingBase)ent).addPotionEffect(new PotionEffect(Potion.wither.getId(), 200, 2));
            if (!((EntityLivingBase)ent).isEntityUndead())
              ((EntityLivingBase)ent).attackEntityFrom(DamageSource.setExplosionSource(null), (float)d); 
          }  
      }  
  }
  
  public double getFuelBurn(ItemStack item) {
    if (item != null && (item.getItem() == Items.nether_star || item.getItem() == Item.getItemFromBlock(Blocks.dragon_egg)))
      return 2400.0D; 
    return 0.0D;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorNether.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */