package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockColor implements ISimpleBlockRenderingHandler {
  Random rand = (Random)XURandom.getInstance();
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator var4 = Tessellator.instance;
    block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.0F, -0.5F);
    if (metadata >= 16 || metadata < 0)
      metadata = this.rand.nextInt(16); 
    float f = BlockColor.initColor[metadata][0];
    float f1 = BlockColor.initColor[metadata][1];
    float f2 = BlockColor.initColor[metadata][2];
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
  
  public boolean renderWorldBlock(IBlockAccess world, int par2, int par3, int par4, Block par1Block, int modelId, RenderBlocks renderer) {
    Tessellator var8 = Tessellator.instance;
    int i = world.getBlockMetadata(par2, par3, par4);
    float f = BlockColor.initColor[i][0];
    float f1 = BlockColor.initColor[i][1];
    float f2 = BlockColor.initColor[i][2];
    TileEntity data = world.getTileEntity(BlockColorData.dataBlockX(par2), BlockColorData.dataBlockY(par3), BlockColorData.dataBlockZ(par4));
    if (data instanceof TileEntityBlockColorData) {
      f = ((TileEntityBlockColorData)data).palette[i][0];
      f1 = ((TileEntityBlockColorData)data).palette[i][1];
      f2 = ((TileEntityBlockColorData)data).palette[i][2];
    } 
    if (EntityRenderer.anaglyphEnable) {
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    } 
    return (Minecraft.isAmbientOcclusionEnabled() && par1Block.getLightValue() == 0) ? renderer.renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, f, f1, f2) : renderer.renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, f, f1, f2);
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
  
  public int getRenderId() {
    return ExtraUtilsProxy.colorBlockID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\RenderBlockColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */