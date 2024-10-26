package com.rwtema.extrautils.worldgen.endoftime;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

public class RenderHandlersEndOfTime {
  @SideOnly(Side.CLIENT)
  public static NullRenderer nullRenderer;
  
  @SideOnly(Side.CLIENT)
  public static SkyRenderer skyRenderer;
  
  static {
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          @SideOnly(Side.CLIENT)
          public void exectuteClientCode() {
            RenderHandlersEndOfTime.skyRenderer = new RenderHandlersEndOfTime.SkyRenderer();
            RenderHandlersEndOfTime.nullRenderer = new RenderHandlersEndOfTime.NullRenderer();
          }
        });
  }
  
  @SideOnly(Side.CLIENT)
  public static class NullRenderer extends IRenderHandler {
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks, WorldClient world, Minecraft mc) {}
  }
  
  @SideOnly(Side.CLIENT)
  public static class SkyRenderer extends IRenderHandler {
    int glSkyList;
    
    int glSkyList2;
    
    RenderGlobal renderGlobal = null;
    
    Field field_glSkyList = ReflectionHelper.findField(RenderGlobal.class, new String[] { "glSkyList", "field_72771_w" });
    
    Field field_glSkyList2 = ReflectionHelper.findField(RenderGlobal.class, new String[] { "glSkyList2", "field_72781_x" });
    
    @SideOnly(Side.CLIENT)
    public void render(float p_72714_1_, WorldClient theWorld, Minecraft mc) {
      if (mc.renderGlobal != this.renderGlobal) {
        this.renderGlobal = mc.renderGlobal;
        try {
          this.glSkyList = this.field_glSkyList.getInt(this.renderGlobal);
          this.glSkyList2 = this.field_glSkyList2.getInt(this.renderGlobal);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        } 
      } else {
        GL11.glDisable(2912);
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        GL11.glDepthMask(false);
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        Vec3 vec31 = theWorld.getSkyColor((Entity)mc.renderViewEntity, p_72714_1_);
        float r = (float)vec31.xCoord;
        float g = (float)vec31.yCoord;
        float b = (float)vec31.zCoord;
        for (int i = 0; i < 6; i++) {
          GL11.glPushMatrix();
          if (i == 1)
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F); 
          if (i == 2)
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F); 
          if (i == 3)
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F); 
          if (i == 4)
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F); 
          if (i == 5)
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F); 
          tessellator.startDrawingQuads();
          tessellator.setColorOpaque_F(r, g, b);
          tessellator.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
          tessellator.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
          tessellator.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
          tessellator.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
          tessellator.draw();
          GL11.glPopMatrix();
        } 
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        return;
      } 
      Vec3 vec3 = theWorld.getSkyColor((Entity)mc.renderViewEntity, p_72714_1_);
      float f1 = (float)vec3.xCoord;
      float f2 = (float)vec3.yCoord;
      float f3 = (float)vec3.zCoord;
      if (mc.gameSettings.anaglyph) {
        float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
        float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
        float f11 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
        f1 = f4;
        f2 = f5;
        f3 = f11;
      } 
      GL11.glColor3f(f1, f2, f3);
      Tessellator tessellator1 = Tessellator.instance;
      GL11.glDepthMask(false);
      GL11.glEnable(2912);
      GL11.glColor3f(f1, f2, f3);
      GL11.glCallList(this.glSkyList);
      GL11.glDisable(2912);
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      RenderHelper.disableStandardItemLighting();
      float[] afloat = theWorld.provider.calcSunriseSunsetColors(theWorld.getCelestialAngle(p_72714_1_), p_72714_1_);
      if (afloat != null) {
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((MathHelper.sin(theWorld.getCelestialAngleRadians(p_72714_1_)) < 0.0F) ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        float f4 = afloat[0];
        float f5 = afloat[1];
        float f11 = afloat[2];
        if (mc.gameSettings.anaglyph) {
          float f12 = (f4 * 30.0F + f5 * 59.0F + f11 * 11.0F) / 100.0F;
          float f13 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
          float f14 = (f4 * 30.0F + f11 * 70.0F) / 100.0F;
          f4 = f12;
          f5 = f13;
          f11 = f14;
        } 
        tessellator1.startDrawing(6);
        tessellator1.setColorRGBA_F(f4, f5, f11, afloat[3]);
        tessellator1.addVertex(0.0D, 100.0D, 0.0D);
        byte b0 = 16;
        tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);
        for (int j = 0; j <= b0; j++) {
          float f14 = j * 3.1415927F * 2.0F / b0;
          float f12 = MathHelper.sin(f14);
          float f13 = MathHelper.cos(f14);
          tessellator1.addVertex((f12 * 120.0F), (f13 * 120.0F), (-f13 * 40.0F * afloat[3]));
        } 
        tessellator1.draw();
        GL11.glPopMatrix();
        GL11.glShadeModel(7424);
      } 
      GL11.glEnable(3553);
      OpenGlHelper.glBlendFunc(770, 1, 1, 0);
      GL11.glPushMatrix();
      float f6 = 1.0F - theWorld.getRainStrength(p_72714_1_);
      float f7 = 0.0F;
      float f8 = 0.0F;
      float f9 = 0.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
      GL11.glTranslatef(f7, f8, f9);
      GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(theWorld.getCelestialAngle(p_72714_1_) * 360.0F, 1.0F, 0.0F, 0.0F);
      float f10 = 30.0F;
      GL11.glDisable(3553);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glEnable(2912);
      GL11.glPopMatrix();
      GL11.glDisable(3553);
      GL11.glColor3f(0.0F, 0.0F, 0.0F);
      double d0 = (mc.thePlayer.getPosition(p_72714_1_)).yCoord - theWorld.getHorizon();
      if (d0 < 0.0D) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 12.0F, 0.0F);
        GL11.glCallList(this.glSkyList2);
        GL11.glPopMatrix();
        f8 = 1.0F;
        f9 = -((float)(d0 + 65.0D));
        f10 = -f8;
        tessellator1.startDrawingQuads();
        tessellator1.setColorRGBA_I(0, 255);
        tessellator1.addVertex(-f8, f9, f8);
        tessellator1.addVertex(f8, f9, f8);
        tessellator1.addVertex(f8, f10, f8);
        tessellator1.addVertex(-f8, f10, f8);
        tessellator1.addVertex(-f8, f10, -f8);
        tessellator1.addVertex(f8, f10, -f8);
        tessellator1.addVertex(f8, f9, -f8);
        tessellator1.addVertex(-f8, f9, -f8);
        tessellator1.addVertex(f8, f10, -f8);
        tessellator1.addVertex(f8, f10, f8);
        tessellator1.addVertex(f8, f9, f8);
        tessellator1.addVertex(f8, f9, -f8);
        tessellator1.addVertex(-f8, f9, -f8);
        tessellator1.addVertex(-f8, f9, f8);
        tessellator1.addVertex(-f8, f10, f8);
        tessellator1.addVertex(-f8, f10, -f8);
        tessellator1.addVertex(-f8, f10, -f8);
        tessellator1.addVertex(-f8, f10, f8);
        tessellator1.addVertex(f8, f10, f8);
        tessellator1.addVertex(f8, f10, -f8);
        tessellator1.draw();
      } 
      if (theWorld.provider.isSkyColored()) {
        GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
      } else {
        GL11.glColor3f(f1, f2, f3);
      } 
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, -((float)(d0 - 16.0D)), 0.0F);
      GL11.glCallList(this.glSkyList2);
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDepthMask(true);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\endoftime\RenderHandlersEndOfTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */