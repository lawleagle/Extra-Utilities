package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockConnectedTextures implements ISimpleBlockRenderingHandler {
  public FakeRenderBlocks getFakeRender() {
    return fakeRender;
  }
  
  public static FakeRenderBlocks fakeRender = new FakeRenderBlocks();
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator var4 = Tessellator.instance;
    block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.0F, -0.5F);
    float f = 1.0F;
    float f1 = 1.0F;
    float f2 = 1.0F;
    if (EntityRenderer.anaglyphEnable) {
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    } 
    GL11.glColor4f(f, f1, f2, 1.0F);
    renderer.colorRedTopLeft *= f;
    renderer.colorRedTopRight *= f;
    renderer.colorRedBottomLeft *= f;
    renderer.colorRedBottomRight *= f;
    renderer.colorGreenTopLeft *= f1;
    renderer.colorGreenTopRight *= f1;
    renderer.colorGreenBottomLeft *= f1;
    renderer.colorGreenBottomRight *= f1;
    renderer.colorBlueTopLeft *= f2;
    renderer.colorBlueTopRight *= f2;
    renderer.colorBlueBottomLeft *= f2;
    renderer.colorBlueBottomRight *= f2;
    if (block.getIcon(0, metadata) == null)
      return; 
    var4.startDrawingQuads();
    var4.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(block, 0.0D, -0.5D, 0.0D, block.getIcon(0, metadata));
    var4.draw();
    var4.startDrawingQuads();
    var4.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(block, 0.0D, -0.5D, 0.0D, block.getIcon(1, metadata));
    var4.draw();
    var4.startDrawingQuads();
    var4.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceXPos(block, 0.0D, -0.5D, 0.0D, block.getIcon(2, metadata));
    var4.draw();
    var4.startDrawingQuads();
    var4.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceXNeg(block, 0.0D, -0.5D, 0.0D, block.getIcon(3, metadata));
    var4.draw();
    var4.startDrawingQuads();
    var4.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceZNeg(block, 0.0D, -0.5D, 0.0D, block.getIcon(4, metadata));
    var4.draw();
    var4.startDrawingQuads();
    var4.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceZPos(block, 0.0D, -0.5D, 0.0D, block.getIcon(5, metadata));
    var4.draw();
    GL11.glTranslatef(0.5F, 0.0F, 0.5F);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    if (renderer.hasOverrideBlockTexture())
      return renderer.renderStandardBlock(block, x, y, z); 
    getFakeRender().setWorld(renderer.blockAccess);
    (getFakeRender()).curBlock = world.getBlock(x, y, z);
    (getFakeRender()).curMeta = world.getBlockMetadata(x, y, z);
    block.setBlockBoundsBasedOnState(fakeRender.blockAccess, x, y, z);
    getFakeRender().setRenderBoundsFromBlock(block);
    return getFakeRender().renderStandardBlock(block, x, y, z);
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
  
  public int getRenderId() {
    return ExtraUtilsProxy.connectedTextureID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\RenderBlockConnectedTextures.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */