package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.helper.GLHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTileEntitySpike extends TileEntitySpecialRenderer {
  private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
  
  public static int hashCode = 0;
  
  public static void render(double[] v1, double[] v2, double[] v3, double[] v4, boolean isPoint) {
    Tessellator tes = Tessellator.instance;
    float f8 = 0.125F;
    GL11.glPushMatrix();
    GL11.glScalef(f8, f8, f8);
    float f9 = (float)((Minecraft.getSystemTime() + hashCode) % 3000L) / 3000.0F * 8.0F;
    GL11.glTranslatef(f9, 0.0F, 0.0F);
    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
    renderFace(v1, v2, v3, v4, tes, isPoint);
    GL11.glPopMatrix();
    GL11.glPushMatrix();
    GL11.glScalef(f8, f8, f8);
    f9 = (float)((Minecraft.getSystemTime() + hashCode) % 4873L) / 4873.0F * 8.0F;
    GL11.glTranslatef(-f9, 0.0F, 0.0F);
    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
    renderFace(v1, v2, v3, v4, tes, isPoint);
    GL11.glPopMatrix();
  }
  
  private static void renderFace(double[] v1, double[] v2, double[] v3, double[] v4, Tessellator tes, boolean isPoint) {
    tes.startDrawingQuads();
    tes.setBrightness(255);
    if (isPoint) {
      tes.addVertexWithUV(v1[0], v1[1], v1[2], 0.0D, 0.5D);
      tes.addVertexWithUV(v2[0], v2[1], v2[2], 0.0D, 0.5D);
    } else {
      tes.addVertexWithUV(v1[0], v1[1], v1[2], 0.0D, 0.0D);
      tes.addVertexWithUV(v2[0], v2[1], v2[2], 0.0D, 1.0D);
    } 
    tes.addVertexWithUV(v3[0], v3[1], v3[2], 1.0D, 1.0D);
    tes.addVertexWithUV(v4[0], v4[1], v4[2], 1.0D, 0.0D);
    tes.draw();
  }
  
  static double[][] pointCoords = new double[][] { { 0.5D, 1.0D, 0.5D }, { 0.5D, 0.0D, 0.5D }, { 0.5D, 0.5D, 1.0D }, { 0.5D, 0.5D, 0.0D }, { 1.0D, 0.5D, 0.5D }, { 0.0D, 0.5D, 0.5D } };
  
  static double[][] base1Coords = new double[][] { { 0.0D, 0.0D, 0.0D }, { 0.0D, 1.0D, 0.0D }, { 0.0D, 0.0D, 0.0D }, { 0.0D, 0.0D, 1.0D }, { 0.0D, 0.0D, 0.0D }, { 1.0D, 0.0D, 0.0D } };
  
  static double[][] base2Coords = new double[][] { { 0.0D, 0.0D, 1.0D }, { 0.0D, 1.0D, 1.0D }, { 0.0D, 1.0D, 0.0D }, { 0.0D, 1.0D, 1.0D }, { 0.0D, 0.0D, 1.0D }, { 1.0D, 0.0D, 1.0D } };
  
  static double[][] base3Coords = new double[][] { { 1.0D, 0.0D, 1.0D }, { 1.0D, 1.0D, 1.0D }, { 1.0D, 1.0D, 0.0D }, { 1.0D, 1.0D, 1.0D }, { 0.0D, 1.0D, 1.0D }, { 1.0D, 1.0D, 1.0D } };
  
  static double[][] base4Coords = new double[][] { { 1.0D, 0.0D, 0.0D }, { 1.0D, 1.0D, 0.0D }, { 1.0D, 0.0D, 0.0D }, { 1.0D, 0.0D, 1.0D }, { 0.0D, 1.0D, 0.0D }, { 1.0D, 1.0D, 0.0D } };
  
  public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
    hashCode = 0;
    int side = Facing.oppositeSide[tileentity.getBlockMetadata() % 6];
    GL11.glPushMatrix();
    GL11.glTranslated(x, y, z);
    GL11.glTranslated(0.5D, 0.5D, 0.5D);
    GL11.glScaled(1.01D, 1.01D, 1.01D);
    GL11.glTranslated(-0.5D, -0.5D, -0.5D);
    drawEnchantedSpike(side);
    GL11.glPopMatrix();
    hashCode = 0;
  }
  
  public static void drawEnchantedSpike(int side) {
    TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
    GLHelper.pushGLState();
    GL11.glDepthMask(true);
    GLHelper.disableGLState(2884);
    GL11.glDepthFunc(515);
    GLHelper.disableGLState(2896);
    texturemanager.bindTexture(RES_ITEM_GLINT);
    GLHelper.enableGLState(3042);
    GL11.glBlendFunc(774, 774);
    OpenGlHelper.glBlendFunc(768, 1, 1, 0);
    float f7 = 0.76F;
    GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
    GL11.glMatrixMode(5890);
    GL11.glPushMatrix();
    render(base1Coords[side], base2Coords[side], base3Coords[side], base4Coords[side], false);
    hashCode += 123;
    render(pointCoords[side], pointCoords[side], base2Coords[side], base1Coords[side], true);
    hashCode += 123;
    render(pointCoords[side], pointCoords[side], base1Coords[side], base4Coords[side], true);
    hashCode += 123;
    render(pointCoords[side], pointCoords[side], base2Coords[side], base3Coords[side], true);
    hashCode += 123;
    render(pointCoords[side], pointCoords[side], base3Coords[side], base4Coords[side], true);
    hashCode = 0;
    GL11.glPopMatrix();
    GLHelper.popGLState();
    GL11.glMatrixMode(5888);
    GL11.glDepthFunc(515);
    GL11.glDepthMask(true);
    GL11.glDepthFunc(515);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\RenderTileEntitySpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */