package cofh.api.block;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockInfo {
  void getBlockInfo(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer, List<IChatComponent> paramList, boolean paramBoolean);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\block\IBlockInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */