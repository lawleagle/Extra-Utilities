package com.rwtema.extrautils.tileentity.enderquarry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityEnderMarker extends TileEntity implements IChunkLoad {
  public static List<int[]> markers = (List)new ArrayList<int>();
  
  public static int[] getCoord(TileEntity tile) {
    return new int[] { (tile.getWorldObj()).provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord };
  }
  
  public boolean init = false;
  
  public boolean canUpdate() {
    return true;
  }
  
  public boolean shouldRefresh(Block oldID, Block newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
    return (oldID != newID);
  }
  
  public void updateEntity() {
    if (!this.init)
      onChunkLoad(); 
    super.updateEntity();
  }
  
  public void invalidate() {
    super.invalidate();
    if (this.worldObj.isRemote)
      return; 
    int[] myCoord = getCoord();
    List<int[]> toUpdate = (List)new ArrayList<int>();
    for (int i = 0; i < markers.size(); i++) {
      int[] coord = markers.get(i);
      if (myCoord[0] == coord[0] && myCoord[2] == coord[2])
        if (myCoord[3] == coord[3] && myCoord[1] == coord[1]) {
          markers.remove(i);
          i--;
        } else if (myCoord[3] == coord[3] || myCoord[1] == coord[1]) {
          toUpdate.add(coord);
        }  
    } 
    for (int[] coord : toUpdate) {
      TileEntity tile = this.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
      if (tile instanceof TileEntityEnderMarker)
        ((TileEntityEnderMarker)tile).recheck(); 
    } 
  }
  
  public int[] getCoord() {
    return getCoord(this);
  }
  
  public void recheck() {
    int[] myCoord = getCoord();
    int flag = 0;
    for (int[] coord : markers) {
      if (myCoord[0] != coord[0] || myCoord[2] != coord[2] || (
        myCoord[1] == coord[1] && myCoord[3] == coord[3]))
        continue; 
      if (myCoord[1] == coord[1]) {
        flag |= (myCoord[3] < coord[3]) ? 1 : 2;
        continue;
      } 
      if (myCoord[3] == coord[3])
        flag |= (myCoord[1] < coord[1]) ? 4 : 8; 
    } 
    this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, flag, 2);
  }
  
  public void onChunkLoad() {
    if (this.init)
      return; 
    this.init = true;
    if (this.worldObj == null || this.worldObj.isRemote)
      return; 
    int[] myCoord = getCoord();
    List<int[]> toUpdate = (List)new ArrayList<int>();
    for (int[] coord : markers) {
      if (myCoord[0] == coord[0] && myCoord[2] == coord[2]) {
        if (myCoord[3] == coord[3] && myCoord[1] == coord[1])
          return; 
        if (myCoord[3] == coord[3] || myCoord[1] == coord[1])
          toUpdate.add(coord); 
      } 
    } 
    markers.add(myCoord);
    recheck();
    for (int[] coord : toUpdate) {
      TileEntity tile = this.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
      if (tile instanceof TileEntityEnderMarker)
        ((TileEntityEnderMarker)tile).recheck(); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\TileEntityEnderMarker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */