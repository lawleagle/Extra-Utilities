package com.rwtema.extrautils.multipart;

import codechicken.lib.render.uv.UVTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MaterialRenderHelper;
import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.helper.XUHelperClient;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class ColoredBlockMicroMaterial extends BlockMicroMaterial {
  public ColoredBlockMicroMaterial(BlockColor block, int meta) {
    super((Block)block, meta);
  }
  
  @SideOnly(Side.CLIENT)
  public void renderMicroFace(Vector3 pos, int pass, Cuboid6 bounds) {
    float[] col = BlockColor.initColor[meta()];
    if (XUHelperClient.clientPlayer().getEntityWorld() != null && pass != -1) {
      col = BlockColorData.getColorData((IBlockAccess)XUHelperClient.clientPlayer().getEntityWorld(), (int)pos.x, (int)pos.y, (int)pos.z, meta());
    } else {
      EntityPlayer entityPlayer = XUHelperClient.clientPlayer();
      if (entityPlayer != null) {
        TileEntity tiledata = ((Entity)entityPlayer).worldObj.getTileEntity(BlockColorData.dataBlockX((int)Math.floor(((Entity)entityPlayer).posX)), BlockColorData.dataBlockY((int)((Entity)entityPlayer).posY), BlockColorData.dataBlockZ((int)Math.floor(((Entity)entityPlayer).posZ)));
        if (tiledata instanceof TileEntityBlockColorData)
          col = ((TileEntityBlockColorData)tiledata).palette[meta()]; 
      } 
    } 
    int c = (int)(col[0] * 255.0F) << 24 | (int)(col[1] * 255.0F) << 16 | (int)(col[2] * 255.0F) << 8 | 0xFF;
    MaterialRenderHelper.start(pos, pass, (UVTransformation)icont()).blockColour(c).lighting().render();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\ColoredBlockMicroMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */