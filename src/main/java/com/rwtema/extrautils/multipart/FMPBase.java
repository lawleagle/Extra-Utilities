package com.rwtema.extrautils.multipart;

import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class FMPBase {
  public static Item getMicroBlockItemId() {
    if (!ExtraUtilsProxy.checked2) {
      ExtraUtilsProxy.checked2 = true;
      if (Loader.isModLoaded("ForgeMultipart")) {
        ExtraUtilsProxy.MicroBlockId = (Item)Item.itemRegistry.getObject("ForgeMicroblock:microblock");
        if (ExtraUtilsProxy.MicroBlockId == null)
          ExtraUtilsProxy.checked2 = false; 
      } 
    } 
    return ExtraUtilsProxy.MicroBlockId;
  }
  
  public static Block getFMPBlockId() {
    if (!ExtraUtilsProxy.checked) {
      ExtraUtilsProxy.checked = true;
      if (Loader.isModLoaded("ForgeMultipart")) {
        try {
          Block b = (Block)Block.blockRegistry.getObject("ForgeMultipart:block");
          ExtraUtilsProxy.FMPBlockId = b;
          if (b == null || b == Blocks.air)
            ExtraUtilsProxy.checked = false; 
        } catch (Exception e) {
          LogHelper.error("Unable to load FMP block id.", new Object[0]);
          throw new RuntimeException(e);
        } 
      } else {
        ExtraUtilsProxy.FMPBlockId = null;
      } 
    } 
    return ExtraUtilsProxy.FMPBlockId;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\FMPBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */