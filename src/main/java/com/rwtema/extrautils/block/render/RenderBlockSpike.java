package com.rwtema.extrautils.block.render;

import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockSpike implements ISimpleBlockRenderingHandler {
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    Tessellator t = Tessellator.instance;
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    renderSpikeBlock((IBlockAccess)(Minecraft.getMinecraft()).theWorld, 0, 0, 0, 1, 0, block, renderer, -1);
    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }
  
  public boolean renderSpikeBlock(IBlockAccess world, int x, int y, int z, int side, int type, Block block, RenderBlocks renderer, int brightness) {
    float ax = 0.0F, ay = 0.0F, az = 0.0F;
    float bx = 1.0F, by = 0.0F, bz = 0.0F;
    float cx = 0.0F, cy = 0.0F, cz = 1.0F;
    float dx = 1.0F, dy = 0.0F, dz = 1.0F;
    float ex = 0.0F, ey = 1.0F, ez = 0.0F;
    float fx = 1.0F, fy = 1.0F, fz = 0.0F;
    float gx = 0.0F, gy = 1.0F, gz = 1.0F;
    float hx = 1.0F, hy = 1.0F, hz = 1.0F;
    switch (side) {
      case 0:
        ax = az = bx = bz = cx = cz = dx = dz = 0.5F;
        break;
      case 1:
        ex = ez = fx = fz = gx = gz = hx = hz = 0.5F;
        break;
      case 2:
        ay = by = ey = fy = ax = bx = ex = fx = 0.5F;
        break;
      case 3:
        cy = dy = gy = hy = cx = dx = gx = hx = 0.5F;
        break;
      case 4:
        ay = cy = ey = gy = az = cz = ez = gz = 0.5F;
        break;
      case 5:
        by = dy = fy = hy = bz = dz = fz = hz = 0.5F;
        break;
      default:
        return false;
    } 
    IIcon texture = block.getIcon(side, type);
    if (renderer.hasOverrideBlockTexture())
      texture = renderer.overrideBlockTexture; 
    Tessellator tessellator = Tessellator.instance;
    if (brightness >= 0)
      tessellator.setBrightness(brightness); 
    boolean inventory = (brightness < 0);
    if (brightness >= 0)
      tessellator.setColorOpaque_F(0.5F, 0.5F, 0.5F); 
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(0, side + type * 6); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
    } 
    if (side != 0) {
      float[] u = { ax, bx, dx, cx };
      float[] v = { az, bz, dz, cz };
      int rotation = calcRotation(0, side);
      tessellator.addVertexWithUV((x + ax), (y + ay), (z + az), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + bx), (y + by), (z + bz), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + dx), (y + dy), (z + dz), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + cx), (y + cy), (z + cz), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
    } 
    if (brightness >= 0)
      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F); 
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(1, side + type * 6); 
    if (side != 1) {
      float[] u = { ex, gx, hx, fx };
      float[] v = { ez, gz, hz, fz };
      int rotation = calcRotation(1, side);
      tessellator.addVertexWithUV((x + ex), (y + ey), (z + ez), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + gx), (y + gy), (z + gz), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + hx), (y + hy), (z + hz), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + fx), (y + fy), (z + fz), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    if (brightness >= 0)
      if (side == 0) {
        tessellator.setColorOpaque_F(0.65F, 0.65F, 0.65F);
      } else if (side == 1) {
        tessellator.setColorOpaque_F(0.9F, 0.9F, 0.9F);
      } else {
        tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
      }  
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(2, side + type * 6); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.445F, 0.894F);
    } 
    if (side != 2) {
      float[] u = { 1.0F - ax, 1.0F - ex, 1.0F - fx, 1.0F - bx };
      float[] v = { 1.0F - ay, 1.0F - ey, 1.0F - fy, 1.0F - by };
      int rotation = calcRotation(2, side);
      tessellator.addVertexWithUV((x + ax), (y + ay), (z + az), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + ex), (y + ey), (z + ez), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + fx), (y + fy), (z + fz), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + bx), (y + by), (z + bz), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    if (brightness >= 0)
      if (side == 0) {
        tessellator.setColorOpaque_F(0.65F, 0.65F, 0.65F);
      } else if (side == 1) {
        tessellator.setColorOpaque_F(0.9F, 0.9F, 0.9F);
      } else {
        tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
      }  
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(3, side + type * 6); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.445F, -0.894F);
    } 
    if (side != 3) {
      float[] u = { dx, hx, gx, cx };
      float[] v = { 1.0F - dy, 1.0F - hy, 1.0F - gy, 1.0F - cy };
      int rotation = calcRotation(3, side);
      tessellator.addVertexWithUV((x + dx), (y + dy), (z + dz), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + hx), (y + hy), (z + hz), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + gx), (y + gy), (z + gz), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + cx), (y + cy), (z + cz), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    if (brightness >= 0)
      if (side == 0) {
        tessellator.setColorOpaque_F(0.55F, 0.55F, 0.55F);
      } else if (side == 1) {
        tessellator.setColorOpaque_F(0.7F, 0.7F, 0.7F);
      } else {
        tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
      }  
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(4, side + type * 6); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.894F, 0.445F, 0.0F);
    } 
    if (side != 4) {
      float[] u = { cz, gz, ez, az };
      float[] v = { 1.0F - cy, 1.0F - gy, 1.0F - ey, 1.0F - ay };
      int rotation = calcRotation(4, side);
      tessellator.addVertexWithUV((x + cx), (y + cy), (z + cz), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + gx), (y + gy), (z + gz), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + ex), (y + ey), (z + ez), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + ax), (y + ay), (z + az), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    if (brightness >= 0)
      if (side == 0) {
        tessellator.setColorOpaque_F(0.55F, 0.55F, 0.55F);
      } else if (side == 1) {
        tessellator.setColorOpaque_F(0.7F, 0.7F, 0.7F);
      } else {
        tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
      }  
    if (!renderer.hasOverrideBlockTexture())
      texture = block.getIcon(5, side + type * 6); 
    if (inventory) {
      tessellator.startDrawingQuads();
      tessellator.setNormal(-0.894F, 0.445F, 0.0F);
    } 
    if (side != 5) {
      float[] u = { 1.0F - bz, 1.0F - fz, 1.0F - hz, 1.0F - dz };
      float[] v = { 1.0F - by, 1.0F - fy, 1.0F - hy, 1.0F - dy };
      int rotation = calcRotation(5, side);
      tessellator.addVertexWithUV((x + bx), (y + by), (z + bz), getU(0, texture, rotation, u, v), getV(0, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + fx), (y + fy), (z + fz), getU(1, texture, rotation, u, v), getV(1, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + hx), (y + hy), (z + hz), getU(2, texture, rotation, u, v), getV(2, texture, rotation, u, v));
      tessellator.addVertexWithUV((x + dx), (y + dy), (z + dz), getU(3, texture, rotation, u, v), getV(3, texture, rotation, u, v));
    } 
    if (inventory)
      tessellator.draw(); 
    return true;
  }
  
  public float getU(int i, IIcon texture, int rotation, float[] u, float[] v) {
    switch (rotation % 4) {
      case 0:
        return texture.getInterpolatedU((u[i % 4] * 16.0F));
      case 1:
        return texture.getInterpolatedU((v[i % 4] * 16.0F));
      case 2:
        return texture.getInterpolatedU((16.0F - u[i % 4] * 16.0F));
      case 3:
        return texture.getInterpolatedU((16.0F - v[i % 4] * 16.0F));
    } 
    return 0.0F;
  }
  
  public float getV(int i, IIcon texture, int rotation, float[] u, float[] v) {
    switch (rotation % 4) {
      case 0:
        return texture.getInterpolatedV((v[i % 4] * 16.0F));
      case 1:
        return texture.getInterpolatedV((16.0F - u[i % 4] * 16.0F));
      case 2:
        return texture.getInterpolatedV((16.0F - v[i % 4] * 16.0F));
      case 3:
        return texture.getInterpolatedV((u[i % 4] * 16.0F));
    } 
    return 0.0F;
  }
  
  public int calcRotation(int side, int direction) {
    if (side == direction)
      return 0; 
    if (side == Facing.oppositeSide[direction])
      return 0; 
    if (direction == 1)
      return 0; 
    if (direction == 0)
      return 2; 
    if (side == 0 || side == 1) {
      (new int[4])[0] = 0;
      (new int[4])[1] = 2;
      (new int[4])[2] = 3;
      (new int[4])[3] = 1;
      return (new int[4])[direction - 2];
    } 
    return 1 + (side + direction + direction / 2) % 2 * 2;
  }
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int side = world.getBlockMetadata(x, y, z) % 6;
    int type = (world.getBlockMetadata(x, y, z) - side) / 6;
    int brightness = block.getMixedBrightnessForBlock(world, x, y, z);
    return renderSpikeBlock(world, x, y, z, side, type, block, renderer, brightness);
  }
  
  public boolean shouldRender3DInInventory(int modelId) {
    return true;
  }
  
  public int getRenderId() {
    return ExtraUtilsProxy.spikeBlockID;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\render\RenderBlockSpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */