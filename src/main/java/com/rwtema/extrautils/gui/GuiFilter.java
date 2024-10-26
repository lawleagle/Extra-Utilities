package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.helper.XUHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GuiFilter extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/guiFilter.png");
  
  private EntityPlayer player;
  
  private int currentFilter = -1;
  
  public GuiFilter(EntityPlayer player, int currentFilter) {
    super(new ContainerFilter(player, currentFilter));
    this.currentFilter = currentFilter;
    this.player = player;
    this.xSize = 176;
    this.ySize = 131;
  }
  
  public List<String> handleItemTooltip(ItemStack stack, int mousex, int mousey, List<String> tooltip) {
    List<String> overide = getOveride(stack, mousex, mousey);
    if (overide != null)
      return overide; 
    return tooltip;
  }
  
  public List<String> getOveride(ItemStack par1ItemStack, int par2, int par3) {
    for (int j1 = 0; j1 < this.inventorySlots.inventorySlots.size(); j1++) {
      Slot slot = this.inventorySlots.inventorySlots.get(j1);
      if (slot instanceof SlotGhostItemContainer && slot.getHasStack()) {
        if (func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, par2, par3) && slot.func_111238_b()) {
          ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
          if (filter != null && filter.hasTagCompound() && filter.getTagCompound().hasKey("isLiquid_" + slot.slotNumber)) {
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(slot.getStack());
            List<String> t = new ArrayList();
            t.add(XUHelper.getFluidName(liquid));
            return t;
          } 
          return null;
        } 
        return null;
      } 
    } 
    return null;
  }
  
  protected void drawSlotInventory(Slot slot) {
    int i = slot.slotNumber;
    if (slot instanceof SlotGhostItemContainer && slot.getHasStack()) {
      ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
      if (filter != null && filter.hasTagCompound() && filter.getTagCompound().hasKey("isLiquid_" + i)) {
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(slot.getStack());
        if (liquid != null && liquid.getFluid().getIcon() != null) {
          GL11.glDisable(2896);
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
          drawTexturedModelRectFromIcon(slot.xDisplayPosition, slot.yDisplayPosition, liquid.getFluid().getIcon(liquid), 16, 16);
          GL11.glEnable(2896);
        } 
      } 
    } 
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


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */