package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRainMuffler extends TileEntity {
  public static final int range = 8;
  
  public static final int rain_range = 4096;
  
  public static boolean playerNeedsMuffler = true;
  
  public static boolean playerNeedsMufflerInstantCheck = false;
  
  public static int curDimension = -100;
  
  public static int curX = -1, curY = -1, curZ = -1;
  
  public TileEntityRainMuffler() {
    if (this.worldObj == null || !this.worldObj.isRemote)
      return; 
    if ((curDimension != this.worldObj.provider.dimensionId || playerNeedsMuffler) && 
      this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 1 && 
      getDistanceFrom((ExtraUtilsMod.proxy.getClientPlayer()).posX, (ExtraUtilsMod.proxy.getClientPlayer()).posY, (ExtraUtilsMod.proxy.getClientPlayer()).posZ) < 4096.0D) {
      curX = this.xCoord;
      curY = this.yCoord;
      curZ = this.zCoord;
      curDimension = this.worldObj.provider.dimensionId;
      playerNeedsMuffler = false;
    } 
  }
  
  public void updateEntity() {
    if (this.worldObj == null || !this.worldObj.isRemote)
      return; 
    if (!playerNeedsMufflerInstantCheck && this.worldObj.getWorldTime() % 100L != 0L)
      return; 
    if (playerNeedsMuffler && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 1)
      if (getDistanceFrom((ExtraUtilsMod.proxy.getClientPlayer()).posX, (ExtraUtilsMod.proxy.getClientPlayer()).posY, (ExtraUtilsMod.proxy.getClientPlayer()).posZ) < 4096.0D) {
        curX = this.xCoord;
        curY = this.yCoord;
        curZ = this.zCoord;
        curDimension = this.worldObj.provider.dimensionId;
        playerNeedsMuffler = false;
      }  
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityRainMuffler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */