package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemLawSword implements IItemRenderer {
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return true;
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return true;
  }
  
  public static final ResourceLocation temaBlade = new ResourceLocation("extrautils", "textures/rwtemaBlade.png");
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    double offset = -0.5D;
    if (!(item.getItem() instanceof ItemLawSword))
      return; 
    boolean firstPerson = (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
    GL11.glPushMatrix();
    if (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
      offset = 0.0D;
    } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
      GL11.glScalef(0.5F, 0.5F, 0.5F);
    } 
    GL11.glTranslated(offset, offset, offset);
    if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
      GL11.glTranslated(0.5D, 0.5D, 0.5D);
      GL11.glRotated(50.0D, -1.0D, 0.0D, 1.0D);
      GL11.glTranslated(-0.5D, -0.5D, -0.5D);
    } 
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    RenderBlocks renderer = (RenderBlocks)data[0];
    renderer.overrideBlockBounds(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(2884);
    (Minecraft.getMinecraft()).renderEngine.bindTexture(temaBlade);
    Tessellator t = Tessellator.instance;
    float h = 87.0F;
    float h2 = 20.0F;
    float w = 18.0F;
    float w2 = 5.0F;
    float w3 = 13.0F;
    double u = (w2 / w);
    float h3 = h2 / h;
    GL11.glScalef(1.7F / h, 1.7F / h, 1.7F / h);
    if (firstPerson) {
      if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
        GL11.glRotated(220.0D, 0.0D, 1.0D, 0.0D);
      } else {
        GL11.glRotated(-50.0D, 0.0D, 1.0D, 0.0D);
      } 
      GL11.glScalef(2.7F, 2.7F, 2.7F);
      GL11.glTranslatef(0.0F, h * 0.3F, 0.0F);
    } 
    GL11.glTranslatef(-w2 / 2.0F, -h / 2.0F, 0.0F);
    GL11.glPushMatrix();
    t.startDrawingQuads();
    t.setNormal(0.0F, 0.0F, -1.0F);
    t.addVertexWithUV(0.0D, h2, (w2 / 2.0F), 0.0D, h3);
    t.addVertexWithUV(0.0D, h, (w2 / 2.0F), 0.0D, 1.0D);
    t.addVertexWithUV(w2, h, (w2 / 2.0F), u, 1.0D);
    t.addVertexWithUV(w2, h2, (w2 / 2.0F), u, h3);
    t.setNormal(0.0F, 0.0F, -1.0F);
    t.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    t.addVertexWithUV(0.0D, h2, 0.0D, 0.0D, h3);
    t.addVertexWithUV(w2, h2, 0.0D, u, h3);
    t.addVertexWithUV(w2, 0.0D, 0.0D, u, 0.0D);
    t.setNormal(0.0F, 0.0F, 1.0F);
    t.addVertexWithUV(w2, 0.0D, w2, u, 0.0D);
    t.addVertexWithUV(w2, h2, w2, u, h3);
    t.addVertexWithUV(0.0D, h2, w2, 0.0D, h3);
    t.addVertexWithUV(0.0D, 0.0D, w2, 0.0D, 0.0D);
    t.setNormal(1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(w2, 0.0D, w2, u, 0.0D);
    t.addVertexWithUV(w2, h2, w2, u, h3);
    t.addVertexWithUV(w2, h2, 0.0D, 0.0D, h3);
    t.addVertexWithUV(w2, 0.0D, 0.0D, 0.0D, 0.0D);
    t.setNormal(-1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(0.0D, 0.0D, 0.0D, u, 0.0D);
    t.addVertexWithUV(0.0D, h2, 0.0D, u, h3);
    t.addVertexWithUV(0.0D, h2, w2, 0.0D, h3);
    t.addVertexWithUV(0.0D, 0.0D, w2, 0.0D, 0.0D);
    t.setNormal(0.0F, -1.0F, 0.0F);
    t.addVertexWithUV(0.0D, 0.0D, 0.0D, (9.0F / w), (4.0F / h));
    t.addVertexWithUV(w2, 0.0D, 0.0D, (13.0F / w), (8.0F / h));
    t.addVertexWithUV(w2, 0.0D, w2, (13.0F / w), (8.0F / h));
    t.addVertexWithUV(0.0D, 0.0D, w2, (9.0F / w), (4.0F / h));
    t.setNormal(0.0F, -1.0F, 0.0F);
    t.addVertexWithUV(-3.0D, 16.0D, -3.0D, (6.0F / w), (18.0F / h));
    t.addVertexWithUV(8.0D, 16.0D, -3.0D, (17.0F / w), (18.0F / h));
    t.addVertexWithUV(8.0D, 16.0D, 8.0D, (17.0F / w), (29.0F / h));
    t.addVertexWithUV(-3.0D, 16.0D, 8.0D, (6.0F / w), (29.0F / h));
    t.setNormal(0.0F, 1.0F, 0.0F);
    t.addVertexWithUV(-3.0D, 20.0D, -3.0D, (6.0F / w), (1.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, -3.0D, (17.0F / w), (1.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, 8.0D, (17.0F / w), (12.0F / h));
    t.addVertexWithUV(-3.0D, 20.0D, 8.0D, (6.0F / w), (12.0F / h));
    t.setNormal(0.0F, 0.0F, -1.0F);
    t.addVertexWithUV(-3.0D, 16.0D, -3.0D, u, (12.0F / h));
    t.addVertexWithUV(-3.0D, 20.0D, -3.0D, u, (17.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
    t.addVertexWithUV(8.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
    t.setNormal(0.0F, 0.0F, 1.0F);
    t.addVertexWithUV(-3.0D, 16.0D, 8.0D, u, (12.0F / h));
    t.addVertexWithUV(-3.0D, 20.0D, 8.0D, u, (17.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, 8.0D, 1.0D, (17.0F / h));
    t.addVertexWithUV(8.0D, 16.0D, 8.0D, 1.0D, (12.0F / h));
    t.setNormal(1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(8.0D, 16.0D, 8.0D, u, (12.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, 8.0D, u, (17.0F / h));
    t.addVertexWithUV(8.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
    t.addVertexWithUV(8.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
    t.setNormal(-1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(-3.0D, 16.0D, 8.0D, u, (12.0F / h));
    t.addVertexWithUV(-3.0D, 20.0D, 8.0D, u, (17.0F / h));
    t.addVertexWithUV(-3.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
    t.addVertexWithUV(-3.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
    t.draw();
    GL11.glPopMatrix();
    renderer.unlockBlockBounds();
    renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    GL11.glPopMatrix();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemLawSword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */