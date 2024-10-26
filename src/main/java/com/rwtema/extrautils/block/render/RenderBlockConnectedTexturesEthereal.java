package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class RenderBlockConnectedTexturesEthereal extends RenderBlockConnectedTextures {
  public static FakeRenderEtherealBlocks fakeRenderEtherealBlocks = new FakeRenderEtherealBlocks();
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    if (renderer.hasOverrideBlockTexture())
      return renderer.renderStandardBlock(block, x, y, z); 
    fakeRender.setWorld(renderer.blockAccess);
    fakeRender.curBlock = world.getBlock(x, y, z);
    fakeRender.curMeta = world.getBlockMetadata(x, y, z);
    block.setBlockBoundsBasedOnState(fakeRender.blockAccess, x, y, z);
    fakeRender.setRenderBoundsFromBlock(block);
    boolean render = fakeRender.renderStandardBlock(block, x, y, z);
    fakeRenderEtherealBlocks.setWorld(renderer.blockAccess);
    fakeRenderEtherealBlocks.curBlock = fakeRender.curBlock;
    fakeRenderEtherealBlocks.curMeta = fakeRender.curMeta;
    double h = 0.05D;
    fakeRenderEtherealBlocks.setRenderBounds(h, h, h, 1.0D - h, 1.0D - h, 1.0D - h);
    render &= fakeRenderEtherealBlocks.renderStandardBlock(block, x, y, z);
    return render;
  }
  
  public int getRenderId() {
    return ExtraUtilsProxy.connectedTextureEtheralID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\RenderBlockConnectedTexturesEthereal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */