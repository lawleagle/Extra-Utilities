package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.Microblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.rwtema.extrautils.block.render.FakeRenderBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import scala.collection.Iterator;

@SideOnly(Side.CLIENT)
public class FakeRenderBlocksMultiPart extends FakeRenderBlocks {
  public int getSideFromBounds(Cuboid6 bounds) {
    if (bounds.max.y != 1.0D)
      return 0; 
    if (bounds.min.y != 0.0D)
      return 1; 
    if (bounds.max.z != 1.0D)
      return 2; 
    if (bounds.min.z != 0.0D)
      return 3; 
    if (bounds.max.x != 1.0D)
      return 4; 
    if (bounds.min.x != 0.0D)
      return 5; 
    return -1;
  }
  
  public boolean matchBlock(int side2, int x2, int y2, int z2) {
    if (this.isOpaque) {
      TileEntity tile_base = this.blockAccess.getTileEntity(x2, y2, z2);
      if (tile_base != null && tile_base instanceof TileMultipart) {
        TileMultipart tile = (TileMultipart)tile_base;
        Iterator<TMultiPart> parts = tile.partList().toIterator();
        while (parts.hasNext()) {
          TMultiPart part = (TMultiPart)parts.next();
          if ((part instanceof codechicken.microblock.FaceMicroblockClient || part instanceof codechicken.microblock.HollowMicroblockClient) && (
            side2 == -1 || getSideFromBounds(((Microblock)part).getBounds()) == side2)) {
            ItemStack t = MicroMaterialRegistry.getMaterial(((Microblock)part).getMaterial()).getItem();
            if (t.getItem() instanceof ItemBlock && 
              this.curBlock == ((ItemBlock)t.getItem()).field_150939_a && t.getItemDamage() == this.curMeta)
              return true; 
          } 
        } 
      } 
    } 
    return super.matchBlock(side2, x2, y2, z2);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\FakeRenderBlocksMultiPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */