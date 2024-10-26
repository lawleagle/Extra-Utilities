package com.rwtema.extrautils.multipart;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockGreenScreen;
import net.minecraft.block.Block;

public class RegisterMicroMaterials {
  public static void registerBlock(Block block) {
    if (block != null)
      BlockMicroMaterial.createAndRegister(block, 0); 
  }
  
  public static void registerFullBright(BlockGreenScreen block) {
    if (block != null)
      for (int m = 0; m < 16; m++)
        MicroMaterialRegistry.registerMaterial((MicroMaterialRegistry.IMicroMaterial)new FullBrightMicroMaterial(block, m), block.getUnlocalizedName() + ((m > 0) ? ("_" + m) : ""));  
  }
  
  public static void registerColorBlock(BlockColor block) {
    if (block != null)
      for (int m = 0; m < 16; m++)
        MicroMaterialRegistry.registerMaterial((MicroMaterialRegistry.IMicroMaterial)new ColoredBlockMicroMaterial(block, m), block.getUnlocalizedName() + ((m > 0) ? ("_" + m) : ""));  
  }
  
  public static void registerConnectedTexture(Block block, int m) {
    if (block != null)
      try {
        MicroMaterialRegistry.registerMaterial((MicroMaterialRegistry.IMicroMaterial)new ConnectedTextureMicroMaterial(block, m), block.getUnlocalizedName() + ((m > 0) ? ("_" + m) : ""));
      } catch (Throwable err) {
        Throwable e = err;
        while (e != null) {
          LogHelper.info("-------", new Object[0]);
          LogHelper.info(e.getMessage(), new Object[0]);
          for (Throwable f : e.getSuppressed()) {
            LogHelper.info("Found supressed error", new Object[0]);
            f.printStackTrace();
          } 
          for (StackTraceElement f : e.getStackTrace())
            LogHelper.info(f.getClassName() + " " + f.getMethodName() + " " + f.getFileName() + " " + f.getLineNumber() + " " + f.isNativeMethod(), new Object[0]); 
          e = e.getCause();
        } 
        throw new RuntimeException(e);
      }  
  }
  
  public static void registerBlock(Block block, int m) {
    if (block != null)
      MicroMaterialRegistry.registerMaterial((MicroMaterialRegistry.IMicroMaterial)new BlockMicroMaterial(block, m), block.getUnlocalizedName() + ((m > 0) ? ("_" + m) : "")); 
  }
  
  public static void registerBlock(Block block, int from, int to) {
    for (int i = from; i < to; i++)
      registerBlock(block, i); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\RegisterMicroMaterials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */