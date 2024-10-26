package com.rwtema.extrautils.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHoldingBag extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
  
  private EntityPlayer player;
  
  private int currentFilter = -1;
  
  public GuiHoldingBag(EntityPlayer player, int currentFilter) {
    super(new ContainerHoldingBag(player, currentFilter));
    this.currentFilter = currentFilter;
    this.player = player;
    this.xSize = 176;
    this.ySize = 222;
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    if (this.player.inventory.getStackInSlot(this.currentFilter) != null)
      this.fontRendererObj.drawString(this.player.inventory.getStackInSlot(this.currentFilter).getDisplayName(), 8, 6, 4210752); 
    this.fontRendererObj.drawString(this.player.inventory.hasCustomInventoryName() ? this.player.inventory.getInventoryName() : StatCollector.translateToLocal(this.player.inventory.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiHoldingBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */