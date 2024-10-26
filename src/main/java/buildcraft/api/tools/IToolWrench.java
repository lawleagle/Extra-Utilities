package buildcraft.api.tools;

import net.minecraft.entity.player.EntityPlayer;

public interface IToolWrench {
  boolean canWrench(EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3);
  
  void wrenchUsed(EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\buildcraft\api\tools\IToolWrench.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */