package com.rwtema.extrautils.item;

import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemBlockColor implements IItemRenderer {
  private Random rand = (Random)XURandom.getInstance();

  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    switch (type) {
      case ENTITY:
        return true;
      case EQUIPPED:
        return true;
      case EQUIPPED_FIRST_PERSON:
        return true;
      case INVENTORY:
        return true;
    }
    return false;
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return true;
  }

  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    EntityClientPlayerMP entityClientPlayerMP = null;
    if (item == null || !(item.getItem() instanceof ItemBlock))
      return;
    Block block = ((ItemBlock)item.getItem()).field_150939_a;
    if (block == null || data == null || data.length == 0)
      return;
    int metadata = item.getItemDamage();
    if (metadata < 0 || metadata >= 16)
      metadata = this.rand.nextInt(16);
    RenderBlocks renderer = (RenderBlocks)data[0];
    Entity holder = null;
    if (data.length > 1 &&
      data[1] instanceof Entity)
      holder = (Entity)data[1];
    if (holder == null)
      entityClientPlayerMP = (Minecraft.getMinecraft()).thePlayer;
    Tessellator var4 = Tessellator.instance;
    block.setBlockBoundsForItemRender();
    renderer.setRenderBoundsFromBlock(block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    switch (type) {
      case EQUIPPED:
      case EQUIPPED_FIRST_PERSON:
        GL11.glTranslatef(-1.0F, 0.5F, 0.0F);
        break;
      default:
        GL11.glTranslatef(-0.5F, -0.0F, -0.5F);
        break;
    }
    float f = BlockColor.initColor[metadata][0];
    float f1 = BlockColor.initColor[metadata][1];
    float f2 = BlockColor.initColor[metadata][2];
    if (entityClientPlayerMP != null &&
      ((Entity)entityClientPlayerMP).worldObj != null) {
      TileEntity tiledata = ((Entity)entityClientPlayerMP).worldObj.getTileEntity(BlockColorData.dataBlockX((int)Math.floor(((Entity)entityClientPlayerMP).posX)), BlockColorData.dataBlockY((int)((Entity)entityClientPlayerMP).posY), BlockColorData.dataBlockZ((int)Math.floor(((Entity)entityClientPlayerMP).posZ)));
      if (tiledata instanceof TileEntityBlockColorData) {
        f = ((TileEntityBlockColorData)tiledata).palette[metadata][0];
        f1 = ((TileEntityBlockColorData)tiledata).palette[metadata][1];
        f2 = ((TileEntityBlockColorData)tiledata).palette[metadata][2];
      }
    }
    if (EntityRenderer.anaglyphEnable) {
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    }
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glEnable(3008);
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
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glTranslatef(0.5F, 0.0F, 0.5F);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemBlockColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
