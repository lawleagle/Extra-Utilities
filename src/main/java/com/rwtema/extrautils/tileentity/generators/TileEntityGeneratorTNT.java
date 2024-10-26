package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityGeneratorTNT extends TileEntityGeneratorFurnace {
  private static Random rand = (Random)XURandom.getInstance();
  
  public int transferLimit() {
    return 400;
  }
  
  public double getFuelBurn(ItemStack item) {
    if (item == null)
      return 0.0D; 
    if (item.getItem() == Items.gunpowder)
      return 400.0D; 
    if (item.getItem() == Item.getItemFromBlock(Blocks.tnt))
      return 6000.0D; 
    return 0.0D;
  }
  
  public double genLevel() {
    return 80.0D;
  }
  
  public void doSpecial() {
    if (this.coolDown > 0.0D && rand.nextInt(80) == 0)
      this.worldObj.createExplosion(null, this.xCoord + rand.nextDouble() * 2.0D - 0.5D, this.yCoord + rand.nextDouble() * 2.0D - 0.5D, this.zCoord + rand.nextDouble() * 2.0D - 0.5D, 1.0F, false); 
  }
  
  public boolean processInput() {
    if (super.processInput()) {
      this.worldObj.createExplosion(null, this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 5.0F, false);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorTNT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */