package com.rwtema.extrautils.item;

import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockQED extends ItemBlockMetadata {
  public static Random rand = (Random)XURandom.getInstance();
  
  public static long prevTime = -2147483648L;
  
  public static int curRand = -1;
  
  public ItemBlockQED(Block p_i45328_1_) {
    super(p_i45328_1_);
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (par1ItemStack.getItemDamage() == 0 && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
      StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
      if ("net.minecraft.item.Item".equals(stackTrace[1].getClassName())) {
        long curTime = System.currentTimeMillis();
        if (curTime - prevTime > 1000L || curRand == -1)
          curRand = rand.nextInt(17); 
        prevTime = curTime;
        return "tile.extrautils:qed.rand." + curRand;
      } 
    } 
    return super.getUnlocalizedName(par1ItemStack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockQED.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */