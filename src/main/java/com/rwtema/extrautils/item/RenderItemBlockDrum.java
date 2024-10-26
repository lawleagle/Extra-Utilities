package com.rwtema.extrautils.item;

import com.rwtema.extrautils.block.render.RenderBlockDrum;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItemBlockDrum implements IItemRenderer {
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
    if (!(item.getItem() instanceof ItemBlock))
      return;
    Block block = ((ItemBlock)item.getItem()).field_150939_a;
    if (block == null)
      return;
    RenderBlocks renderer = (RenderBlocks)data[0];
    Entity holder = null;
    if (data.length > 1 &&
      data[1] instanceof Entity)
      holder = (Entity)data[1];
    EntityClientPlayerMP entityClientPlayerMP;
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
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glEnable(3008);
    RenderBlockDrum.drawInvBlock(block, item);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glTranslatef(0.5F, 0.0F, 0.5F);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemBlockDrum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
