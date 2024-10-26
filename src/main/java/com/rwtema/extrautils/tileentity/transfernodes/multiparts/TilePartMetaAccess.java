package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.multipart.minecraft.IPartMeta;
import codechicken.multipart.minecraft.PartMetaAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;

public class TilePartMetaAccess extends PartMetaAccess {
  TileEntity tile;
  
  public TilePartMetaAccess(IPartMeta p, TileEntity tileEntity) {
    super(p);
    this.tile = tileEntity;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isAirBlock(int i, int j, int k) {
    return ((i != (this.part.getPos()).x || j != (this.part.getPos()).y || k != (this.part.getPos()).z) && this.part.getWorld().isAirBlock(i, j, k));
  }
  
  public TileEntity getTileEntity(int i, int j, int k) {
    if (i == (this.part.getPos()).x && j == (this.part.getPos()).y && k == (this.part.getPos()).z)
      return this.tile; 
    return super.getTileEntity(i, j, k);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TilePartMetaAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */