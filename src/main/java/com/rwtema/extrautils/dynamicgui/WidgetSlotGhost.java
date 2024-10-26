package com.rwtema.extrautils.dynamicgui;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class WidgetSlotGhost extends WidgetSlot {
  public WidgetSlotGhost(IInventory inv, int slot, int x, int y) {
    super(inv, slot, x, y);
  }
  
  public void renderBackground(TextureManager manager, DynamicGui gui, int x, int y) {}
  
  public boolean isItemValid(ItemStack par1ItemStack) {
    return false;
  }
  
  public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    return false;
  }
  
  public void putStack(ItemStack p_75215_1_) {}
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    boolean blendLevel = GL11.glIsEnabled(3042);
    if (!blendLevel)
      GL11.glEnable(3042); 
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), 0, 0, 18, 18);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    if (!blendLevel)
      GL11.glDisable(3042); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetSlotGhost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */