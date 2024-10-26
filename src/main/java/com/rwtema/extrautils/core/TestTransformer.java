package com.rwtema.extrautils.core;

import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class TestTransformer {
  static {
    performTest();
    FMLCommonHandler.instance().exitJava(0, true);
  }
  
  public static void performTest() {
    for (String s : FMLDeobfuscatingRemapper.INSTANCE.getObfedClasses())
      LogHelper.info(s + FMLDeobfuscatingRemapper.INSTANCE.map(s), new Object[0]); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\TestTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */