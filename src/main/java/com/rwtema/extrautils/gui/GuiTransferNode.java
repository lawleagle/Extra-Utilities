package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Locale;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTransferNode extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/guiTransferNode.png");
  
  TileEntityTransferNode node;
  
  IInventory player;
  
  FluidStack liquid;
  
  public GuiTransferNode(IInventory player, TileEntityTransferNode node) {
    super(new ContainerTransferNode(player, node));
    this.node = node;
    this.player = player;
    this.xSize = 176;
    this.ySize = 222;
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    if (this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory) {
      this.fontRendererObj.drawString(((IInventory)this.node).hasCustomInventoryName() ? ((IInventory)this.node).getInventoryName() : StatCollector.translateToLocal(((IInventory)this.node).getInventoryName()), 8, 6, 4210752);
      if (this.inventorySlots.getSlot(0).getHasStack() || this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory) {
        String msg = StatCollector.translateToLocal("gui.transferNode.searching");
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
        msg = "x: " + formatRelCoord(this.node.pipe_x) + " y: " + formatRelCoord(this.node.pipe_y) + " z: " + formatRelCoord(this.node.pipe_z);
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
      } 
    } else if (this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy) {
      String msg = "Holding: " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(((ContainerTransferNode)this.inventorySlots).lastenergy) }) + " RF";
      this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
      msg = "Powering: " + ((ContainerTransferNode)this.inventorySlots).lastenergycount + " Connection";
      if (((ContainerTransferNode)this.inventorySlots).lastenergycount != 1)
        msg = msg + "s"; 
      this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
      msg = StatCollector.translateToLocal("gui.transferNode.searching");
      this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 46, 4210752);
      msg = "x: " + formatRelCoord(this.node.pipe_x) + " y: " + formatRelCoord(this.node.pipe_y) + " z: " + formatRelCoord(this.node.pipe_z);
      this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 56, 4210752);
    } else if (this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid) {
      if (((ContainerTransferNode)this.inventorySlots).liquid_amount > 0) {
        FluidStack liquid = new FluidStack(((ContainerTransferNode)this.inventorySlots).liquid_type, ((ContainerTransferNode)this.inventorySlots).liquid_amount);
        String msg = XUHelper.getFluidName(liquid);
        msg = "Holding: " + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(liquid.amount) }) + "mB of " + msg;
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
        msg = "x: " + formatRelCoord(this.node.pipe_x) + " y: " + formatRelCoord(this.node.pipe_y) + " z: " + formatRelCoord(this.node.pipe_z);
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
        if (liquid.getFluid().getIcon() != null) {
          this.mc.renderEngine.bindTexture((liquid.getFluid().getSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
          int col = liquid.getFluid().getColor(liquid);
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
          drawTexturedModelRectFromIcon(80, 83, liquid.getFluid().getIcon(liquid), 16, 16);
        } 
      } else if (this.node instanceof com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid) {
        String msg = StatCollector.translateToLocal("gui.transferNode.searching");
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 26, 4210752);
        msg = "x: " + formatRelCoord(this.node.pipe_x) + " y: " + formatRelCoord(this.node.pipe_y) + " z: " + formatRelCoord(this.node.pipe_z);
        this.fontRendererObj.drawString(msg, this.xSize / 2 - this.fontRendererObj.getStringWidth(msg) / 2, 36, 4210752);
      } 
    } 
  }
  
  private String formatRelCoord(int no) {
    if (no > 0)
      return "+" + no; 
    if (no == 0)
      return " " + no; 
    return "" + no;
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiTransferNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */