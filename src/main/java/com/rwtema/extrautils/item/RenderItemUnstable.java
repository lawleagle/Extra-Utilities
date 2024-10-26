package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemUnstable implements IItemRenderer {
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return true;
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return (helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING);
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    if (!(item.getItem() instanceof IItemMultiTransparency))
      return; 
    IItemMultiTransparency itemTrans = (IItemMultiTransparency)item.getItem();
    if (type == IItemRenderer.ItemRenderType.ENTITY) {
      GL11.glTranslatef(-0.5F, -0.25F, 0.0F);
      GL11.glDisable(2884);
      GL11.glTranslatef(1.0F, 0.0F, 0.0F);
      GL11.glScalef(-1.0F, 1.0F, 1.0F);
    } 
    if (type == IItemRenderer.ItemRenderType.INVENTORY)
      GL11.glScalef(16.0F, 16.0F, 1.0F); 
    Tessellator tessellator = Tessellator.instance;
    float t = 0.0F;
    if (item.hasTagCompound()) {
      NBTTagCompound tg = item.getTagCompound();
      if (tg.hasKey("time") && tg.hasKey("dimension")) {
        t = (float)((Minecraft.getMinecraft()).thePlayer.worldObj.getTotalWorldTime() - tg.getLong("time")) / 200.0F;
        if (t > 1.0F)
          t = 1.0F; 
      } 
    } 
    float r = 1.0F, g = 1.0F, b = 1.0F;
    if (t > 0.85D && Minecraft.getSystemTime() % 200L < 100L) {
      r = 1.0F;
      g = 1.0F - t * 0.7F / 3.0F;
      b = 0.0F;
    } else {
      r = 1.0F;
      g = 1.0F - t * 0.7F;
      b = 1.0F - t;
    } 
    GL11.glColor3f(r, g, b);
    for (int i = 0; i < itemTrans.numIcons(item); i++) {
      IIcon icon = itemTrans.getIconForTransparentRender(item, i);
      float trans = itemTrans.getIconTransparency(item, i);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glEnable(3008);
      if (trans < 1.0F) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glColor4f(r, g, b, trans);
      } else {
        GL11.glColor4f(r, g, b, 1.0F);
      } 
      if (type != IItemRenderer.ItemRenderType.INVENTORY) {
        GL11.glEnable(32826);
        ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        GL11.glDisable(32826);
      } else {
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, icon.getMaxU(), icon.getMinV());
        tessellator.draw();
      } 
      if (trans < 1.0F) {
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
      } 
    } 
    if (type == IItemRenderer.ItemRenderType.INVENTORY)
      GL11.glScalef(0.0625F, 0.0625F, 1.0F); 
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    if (type == IItemRenderer.ItemRenderType.ENTITY) {
      GL11.glScalef(-1.0F, 1.0F, 1.0F);
      GL11.glTranslatef(1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.5F, 0.25F, 0.0F);
      GL11.glEnable(2884);
    } 
  }
  
  private int f(float t, long a, int k) {
    int b = (int)((2.0D + Math.cos(((float)(Minecraft.getSystemTime() % a) / (float)a * 2.0F) * Math.PI) / 3.0D) * (t * t) * k);
    if (b < 0)
      return 0; 
    if (b > 8)
      return 8; 
    return 0;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemUnstable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */