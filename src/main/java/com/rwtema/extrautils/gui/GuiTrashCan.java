package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTrashCan extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/guiTrashCan.png");
  
  private IInventory player;
  
  private TileEntityTrashCan trashCan;
  
  public GuiTrashCan(IInventory player, TileEntityTrashCan trashCan) {
    super(new ContainerTrashCan(player, trashCan));
    this.trashCan = trashCan;
    this.player = player;
    this.xSize = 176;
    this.ySize = 222;
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiTrashCan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */