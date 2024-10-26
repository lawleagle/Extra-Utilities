package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.item.ItemBlockDrum;
import com.rwtema.extrautils.texture.LiquidColorRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockDrum implements ISimpleBlockRenderingHandler {
  public static final double numTex = 3.0D;
  
  public static double w = 0.5D;
  
  static float base_w = 0.425F;
  
  public static double du(double i) {
    return i % 3.0D / 3.0D;
  }
  
  public static double du2(double i) {
    return (i % 3.0D + 1.0D) / 3.0D;
  }
  
  public static double dx(double i) {
    return 0.5D + ddx(i) * w;
  }
  
  public static double dz(double i) {
    return 0.5D + ddz(i) * w;
  }
  
  public static float ddx(double i) {
    return (float)Math.cos(-(0.5D + i) / 8.0D * 2.0D * Math.PI);
  }
  
  public static float ddz(double i) {
    return (float)Math.sin(-(0.5D + i) / 8.0D * 2.0D * Math.PI);
  }
  
  public static void drawInvBlock(Block block, ItemStack item) {
    float h = 0.97F;
    float d = 0.2F;
    float h2 = 0.3125F;
    int l = 16777215;
    int meta = item.getItemDamage();
    if (item.hasTagCompound() && item.stackTagCompound.hasKey("color"))
      l = item.stackTagCompound.getInteger("color"); 
    FluidStack fluid = ((ItemBlockDrum)item.getItem()).getFluid(item);
    l = LiquidColorRegistry.getFluidColor(fluid);
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    Tessellator t = Tessellator.instance;
    t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    GL11.glTranslatef(-0.0F, -0.5F, -0.0F);
    IIcon icon = block.getIcon(2, meta);
    float wu = icon.getMaxU() - icon.getMinU();
    float wv = icon.getMaxV() - icon.getMinV();
    float ddv = wv * 0.3125F;
    int i;
    for (i = 0; i < 8; i++) {
      w = (base_w * h);
      t.startDrawingQuads();
      t.setNormal(ddx(i + 0.5D), 0.0F, ddz(i + 0.5D));
      t.setColorOpaque_F(f, f1, f2);
      t.addVertexWithUV(dx((i + 1)), 0.0D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      t.addVertexWithUV(dx((i + 1)), h2, dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMaxV() - ddv));
      t.addVertexWithUV(dx(i), h2, dz(i), icon.getMinU() + du(i) * wu, (icon.getMaxV() - ddv));
      t.addVertexWithUV(dx(i), 0.0D, dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      t.addVertexWithUV(dx((i + 1)), (h2 * 2.0F), dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMinV() + ddv));
      t.addVertexWithUV(dx((i + 1)), 1.0D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
      t.addVertexWithUV(dx(i), 1.0D, dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV(dx(i), (h2 * 2.0F), dz(i), icon.getMinU() + du(i) * wu, (icon.getMinV() + ddv));
      t.draw();
      t.startDrawingQuads();
      t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      t.setNormal(ddx(i + 0.5D), 0.0F, ddz(i + 0.5D));
      t.addVertexWithUV(dx((i + 1)), h2, dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMaxV() - ddv));
      t.addVertexWithUV(dx((i + 1)), (h2 * 2.0F), dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMinV() + ddv));
      t.addVertexWithUV(dx(i), (h2 * 2.0F), dz(i), icon.getMinU() + du(i) * wu, (icon.getMinV() + ddv));
      t.addVertexWithUV(dx(i), h2, dz(i), icon.getMinU() + du(i) * wu, (icon.getMaxV() - ddv));
      t.draw();
      w = base_w;
      t.startDrawingQuads();
      t.setColorOpaque_F(0.6F, 0.6F, 0.6F);
      t.setNormal(ddx(i + 0.5D), 0.0F, ddz(i + 0.5D));
      t.addVertexWithUV(dx((i + 1)), 0.0D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      t.addVertexWithUV(dx((i + 1)), 0.05D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV(dx(i), 0.05D, dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV(dx(i), 0.0D, dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      t.addVertexWithUV(dx((i + 1)), 0.95D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV() + wu * 0.05D);
      t.addVertexWithUV(dx((i + 1)), 1.0D, dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
      t.addVertexWithUV(dx(i), 1.0D, dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV(dx(i), 0.95D, dz(i), icon.getMinU() + du(i) * wu, icon.getMinV() + wu * 0.05D);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(1.0D - dx(i), 0.95D, 1.0D - dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      t.addVertexWithUV(1.0D - dx(i), 1.0D, 1.0D - dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV(1.0D - dx((i + 1)), 1.0D, 1.0D - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV(1.0D - dx((i + 1)), 0.95D, 1.0D - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      t.addVertexWithUV(1.0D - dx(i), 0.0D, 1.0D - dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV(1.0D - dx(i), 0.05D, 1.0D - dz(i), icon.getMinU() + du(i) * wu, icon.getMinV() + wu * 0.05D);
      t.addVertexWithUV(1.0D - dx((i + 1)), 0.05D, 1.0D - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV() + wu * 0.05D);
      t.addVertexWithUV(1.0D - dx((i + 1)), 0.0D, 1.0D - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
      t.draw();
    } 
    w = base_w;
    icon = block.getIcon(1, meta);
    wu = icon.getMaxU() - icon.getMinU();
    wv = icon.getMaxV() - icon.getMinV();
    for (i = 0; i < 8; i++) {
      t.startDrawingQuads();
      t.setColorOpaque_F(0.8F, 0.8F, 0.8F);
      t.setNormal(0.0F, 1.0F, 0.0F);
      w = base_w;
      t.addVertexWithUV(dx(i), 1.0D, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(dx((i + 1)), 1.0D, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(dx((i + 1)), 1.0D, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(dx(i), 1.0D, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.draw();
      t.startDrawingQuads();
      t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      t.setNormal(0.0F, 1.0F, 0.0F);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(dx(i), h, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(dx((i + 1)), h, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = 0.0D;
      t.addVertexWithUV(dx((i + 1)), h, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(dx(i), h, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.draw();
    } 
    icon = block.getIcon(0, meta);
    wu = icon.getMaxU() - icon.getMinU();
    wv = icon.getMaxV() - icon.getMinV();
    for (i = 0; i < 8; i++) {
      t.startDrawingQuads();
      t.setColorOpaque_F(0.8F, 0.8F, 0.8F);
      t.setNormal(0.0F, -1.0F, 0.0F);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(dx(i), 0.0D, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(dx((i + 1)), 0.0D, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = base_w;
      t.addVertexWithUV(dx((i + 1)), 0.0D, dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(dx(i), 0.0D, dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.draw();
      t.startDrawingQuads();
      t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      t.setNormal(0.0F, 1.0F, 0.0F);
      w = 0.0D;
      t.addVertexWithUV(dx(i), (1.0F - h), dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(dx((i + 1)), (1.0F - h), dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(dx((i + 1)), (1.0F - h), dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(dx(i), (1.0F - h), dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.draw();
    } 
    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
  }
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
    int meta = world.getBlockMetadata(x, y, z);
    float h = 0.97F;
    float d = 0.2F;
    float h2 = 0.3125F;
    int l = block.colorMultiplier(world, x, y, z);
    float f = (l >> 16 & 0xFF) / 255.0F;
    float f1 = (l >> 8 & 0xFF) / 255.0F;
    float f2 = (l & 0xFF) / 255.0F;
    Tessellator t = Tessellator.instance;
    t.setBrightness(brightness);
    t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    t.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
    IIcon icon = block.getIcon(2, meta);
    if (renderer.overrideBlockTexture != null)
      icon = renderer.overrideBlockTexture; 
    float wu = icon.getMaxU() - icon.getMinU();
    float wv = icon.getMaxV() - icon.getMinV();
    float ddv = wv * 0.3125F;
    int i;
    for (i = 0; i < 8; i++) {
      w = (base_w * h);
      setB((i + 1) - d, 1.0F, f, f1, f2);
      t.addVertexWithUV(x + dx((i + 1)), y, z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      t.addVertexWithUV(x + dx((i + 1)), (y + h2), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMaxV() - ddv));
      setB(i + d, 0.9F, f, f1, f2);
      t.addVertexWithUV(x + dx(i), (y + h2), z + dz(i), icon.getMinU() + du(i) * wu, (icon.getMaxV() - ddv));
      t.addVertexWithUV(x + dx(i), y, z + dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      setB((i + 1) - d, 1.0F, f, f1, f2);
      t.addVertexWithUV(x + dx((i + 1)), (y + h2 * 2.0F), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMinV() + ddv));
      t.addVertexWithUV(x + dx((i + 1)), (y + 1), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
      setB(i + d, 0.9F, f, f1, f2);
      t.addVertexWithUV(x + dx(i), (y + 1), z + dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV(x + dx(i), (y + h2 * 2.0F), z + dz(i), icon.getMinU() + du(i) * wu, (icon.getMinV() + ddv));
      setB((i + 1) - d, 1.0F);
      t.addVertexWithUV(x + dx((i + 1)), (y + h2), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMaxV() - ddv));
      t.addVertexWithUV(x + dx((i + 1)), (y + h2 * 2.0F), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, (icon.getMinV() + ddv));
      setB(i + d, 1.0F);
      t.addVertexWithUV(x + dx(i), (y + h2 * 2.0F), z + dz(i), icon.getMinU() + du(i) * wu, (icon.getMinV() + ddv));
      t.addVertexWithUV(x + dx(i), (y + h2), z + dz(i), icon.getMinU() + du(i) * wu, (icon.getMaxV() - ddv));
      t.setColorOpaque_F(0.65F, 0.65F, 0.65F);
      w = base_w;
      setB((i + 1) - d, 0.6F);
      t.addVertexWithUV(x + dx((i + 1)), y, z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      t.addVertexWithUV(x + dx((i + 1)), y + 0.05D, z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV() - wu * 0.05D);
      setB(i + d, 0.6F);
      t.addVertexWithUV(x + dx(i), y + 0.05D, z + dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV(x + dx(i), y, z + dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      setB((i + 1) - d, 0.6F);
      t.addVertexWithUV(x + dx((i + 1)), y + 0.95D, z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV() + wu * 0.05D);
      t.addVertexWithUV(x + dx((i + 1)), (y + 1), z + dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
      setB(i + d, 0.6F);
      t.addVertexWithUV(x + dx(i), (y + 1), z + dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV(x + dx(i), y + 0.95D, z + dz(i), icon.getMinU() + du(i) * wu, icon.getMinV() + wu * 0.05D);
      w = (base_w * h) * 0.9D;
      setB(i + d, 0.6F);
      t.addVertexWithUV((x + 1) - dx(i), y + 0.95D, (z + 1) - dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV());
      t.addVertexWithUV((x + 1) - dx(i), (y + 1), (z + 1) - dz(i), icon.getMinU() + du(i) * wu, icon.getMaxV() - wu * 0.05D);
      setB((i + 1) - d, 0.6F);
      t.addVertexWithUV((x + 1) - dx((i + 1)), (y + 1), (z + 1) - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV() - wu * 0.05D);
      t.addVertexWithUV((x + 1) - dx((i + 1)), y + 0.95D, (z + 1) - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMaxV());
      setB(i + d, 0.6F);
      t.addVertexWithUV((x + 1) - dx(i), y, (z + 1) - dz(i), icon.getMinU() + du(i) * wu, icon.getMinV());
      t.addVertexWithUV((x + 1) - dx(i), y + 0.05D, (z + 1) - dz(i), icon.getMinU() + du(i) * wu, icon.getMinV() + wu * 0.05D);
      setB((i + 1) - d, 0.6F);
      t.addVertexWithUV((x + 1) - dx((i + 1)), y + 0.05D, (z + 1) - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV() + wu * 0.05D);
      t.addVertexWithUV((x + 1) - dx((i + 1)), y, (z + 1) - dz((i + 1)), icon.getMinU() + du2(i) * wu, icon.getMinV());
    } 
    w = base_w;
    icon = block.getIcon(1, meta);
    if (renderer.overrideBlockTexture != null)
      icon = renderer.overrideBlockTexture; 
    wu = icon.getMaxU() - icon.getMinU();
    wv = icon.getMaxV() - icon.getMinV();
    for (i = 0; i < 8; i++) {
      t.setColorOpaque_F(0.8F, 0.8F, 0.8F);
      w = base_w;
      t.addVertexWithUV(x + dx(i), (y + 1), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(x + dx((i + 1)), (y + 1), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(x + dx((i + 1)), (y + 1), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(x + dx(i), (y + 1), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(x + dx(i), (y + h), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(x + dx((i + 1)), (y + h), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = 0.0D;
      t.addVertexWithUV(x + dx((i + 1)), (y + h), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(x + dx(i), (y + h), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
    } 
    t.setColorOpaque_F(0.5F, 0.5F, 0.5F);
    icon = block.getIcon(0, meta);
    if (renderer.overrideBlockTexture != null)
      icon = renderer.overrideBlockTexture; 
    wu = icon.getMaxU() - icon.getMinU();
    wv = icon.getMaxV() - icon.getMinV();
    for (i = 0; i < 8; i++) {
      t.setColorOpaque_F(0.5F, 0.5F, 0.5F);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(x + dx(i), y, z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(x + dx((i + 1)), y, z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = base_w;
      t.addVertexWithUV(x + dx((i + 1)), y, z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(x + dx(i), y, z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.setColorOpaque_F(0.6F, 0.6F, 0.6F);
      w = 0.0D;
      t.addVertexWithUV(x + dx(i), ((y + 1) - h), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
      t.addVertexWithUV(x + dx((i + 1)), ((y + 1) - h), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      w = (base_w * h) * 0.9D;
      t.addVertexWithUV(x + dx((i + 1)), ((y + 1) - h), z + dz((i + 1)), icon.getMinU() + dx((i + 1)) * wu, icon.getMaxV() - dz((i + 1)) * wv);
      t.addVertexWithUV(x + dx(i), ((y + 1) - h), z + dz(i), icon.getMinU() + dx(i) * wu, icon.getMaxV() - dz(i) * wv);
    } 
    return false;
  }
  
  public void setB(float i, float p, float r, float g, float b) {
    float brightness = (float)(p * (0.7D + Math.cos(((i + 0.5D) / 4.0D * 2.0D + 1.0D) * Math.PI) * 0.1D));
    Tessellator.instance.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
  }
  
  public void setB(float i, float p) {
    setB(i, p, 1.0F, 1.0F, 1.0F);
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }
  
  public int getRenderId() {
    return ExtraUtilsProxy.drumRendererID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\RenderBlockDrum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */