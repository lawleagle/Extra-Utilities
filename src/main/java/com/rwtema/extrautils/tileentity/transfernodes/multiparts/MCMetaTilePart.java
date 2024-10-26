package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.Vector3;
import codechicken.multipart.minecraft.IPartMeta;
import codechicken.multipart.minecraft.McMetaPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public abstract class MCMetaTilePart extends McMetaPart {
  public MCMetaTilePart(int meta) {
    super(meta);
  }
  
  public MCMetaTilePart() {}
  
  public abstract TileEntity getBlockTile();
  
  @SideOnly(Side.CLIENT)
  public boolean renderStatic(Vector3 pos, int pass) {
    if (pass == 0) {
      (new RenderBlocks((IBlockAccess)new TilePartMetaAccess((IPartMeta)this, getBlockTile()))).renderBlockByRenderType(getBlock(), x(), y(), z());
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\MCMetaTilePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */