package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtilsClient;
import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.helper.GLHelper;
import com.rwtema.extrautils.tileentity.RenderTileEntitySpike;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemSpikeSword implements IItemRenderer {
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return true;
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return true;
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    GL11.glPushMatrix();
    if (type == IItemRenderer.ItemRenderType.ENTITY)
      GL11.glScaled(0.5D, 0.5D, 0.5D); 
    if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == IItemRenderer.ItemRenderType.EQUIPPED)
      GL11.glTranslated(0.5D, 0.5D, 0.5D); 
    ExtraUtilsClient.renderBlockSpike.renderInventoryBlock((Block)((ItemBlockSpike)item.getItem()).spike, 0, ExtraUtilsProxy.spikeBlockID, (RenderBlocks)data[0]);
    if (item.hasEffect(0)) {
      GL11.glTranslated(0.0D, 0.0D, -1.0D);
      GL11.glScaled(1.01D, 1.01D, 1.01D);
      GL11.glTranslated(-0.5D, -0.5D, -0.5D);
      GLHelper.pushGLState();
      GLHelper.enableGLState(3008);
      RenderTileEntitySpike.hashCode = System.identityHashCode(item);
      RenderTileEntitySpike.drawEnchantedSpike(0);
      GLHelper.popGLState();
    } 
    GL11.glPopMatrix();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemSpikeSword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */