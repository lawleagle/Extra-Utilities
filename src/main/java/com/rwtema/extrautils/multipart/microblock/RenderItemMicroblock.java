package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.render.CCRenderPipeline;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureUtils;
import codechicken.microblock.MicroMaterialRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemMicroblock implements IItemRenderer {
  MicroMaterialRegistry.IMicroMaterial wool = null;
  
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return true;
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return true;
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack stack, Object... data) {
    if (!stack.hasTagCompound())
      return; 
    String mat = stack.getTagCompound().getString("mat");
    MicroMaterialRegistry.IMicroMaterial material = MicroMaterialRegistry.getMaterial(mat);
    if (material == null)
      return; 
    GL11.glPushMatrix();
    if (type == IItemRenderer.ItemRenderType.ENTITY)
      GL11.glScaled(0.5D, 0.5D, 0.5D); 
    if (type == IItemRenderer.ItemRenderType.INVENTORY || type == IItemRenderer.ItemRenderType.ENTITY)
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F); 
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glEnable(3008);
    CCRenderPipeline.PipelineBuilder builder = CCRenderState.pipeline.builder();
    CCRenderState.reset();
    TextureUtils.bindAtlas(0);
    CCRenderState.useNormals = true;
    CCRenderState.pullLightmap();
    CCRenderState.startDrawing();
    IMicroBlock part = RegisterMicroBlocks.mParts.get(Integer.valueOf(stack.getItemDamage()));
    if (part != null)
      part.renderItem(stack, material); 
    CCRenderState.draw();
    GL11.glPopMatrix();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\RenderItemMicroblock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */