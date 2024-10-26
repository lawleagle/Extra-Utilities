package com.rwtema.extrautils.item.scanner;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public interface IScanner {
  Class<?> getTargetClass();
  
  void addData(Object paramObject, List<String> paramList, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer);
  
  int getPriority();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\scanner\IScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */