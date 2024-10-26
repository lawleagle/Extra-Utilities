package com.rwtema.extrautils.multipart.microblock;

import codechicken.microblock.MicroMaterialRegistry;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabMicroBlocks extends CreativeTabs {
  public static CreativeTabMicroBlocks instance = null;
  
  public static Random rand = (Random)XURandom.getInstance();
  
  private static ItemStack item;
  
  public CreativeTabMicroBlocks() {
    super("extraUtil_FMP");
  }
  
  @SideOnly(Side.CLIENT)
  public Item getTabIconItem() {
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public ItemStack getIconItemStack() {
    if (item == null)
      item = ItemMicroBlock.getStack(new ItemStack(ExtraUtils.microBlocks, 1, rand.nextBoolean() ? 2 : 1), (String)MicroMaterialRegistry.getIdMap()[rand.nextInt((MicroMaterialRegistry.getIdMap()).length)]._1()); 
    return item;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\CreativeTabMicroBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */