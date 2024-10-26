package com.rwtema.extrautils.dynamicgui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WidgetSlot extends Slot implements IWidget {
  public boolean playerSlot;
  
  boolean isISided;
  
  int side;
  
  public WidgetSlot(IInventory inv, int slot, int x, int y) {
    super(inv, slot, x + 1, y + 1);
    this.isISided = inv instanceof ISidedInventory;
    this.side = 0;
    if (this.isISided)
      for (this.side = 0; this.side < 6; this.side++) {
        int[] slots = ((ISidedInventory)inv).getAccessibleSlotsFromSide(this.side);
        for (int s : slots) {
          if (s == slot)
            return; 
        } 
      }  
  }
  
  public boolean isItemValid(ItemStack par1ItemStack) {
    return this.inventory.isItemValidForSlot(this.slotNumber, par1ItemStack);
  }
  
  public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    return true;
  }
  
  public int getX() {
    return this.xDisplayPosition - 1;
  }
  
  public int getY() {
    return this.yDisplayPosition - 1;
  }
  
  public int getW() {
    return 18;
  }
  
  public int getH() {
    return 18;
  }
  
  public void addToContainer(DynamicContainer container) {
    container.addSlot(this);
  }
  
  @SideOnly(Side.CLIENT)
  public void renderBackground(TextureManager manager, DynamicGui gui, int x, int y) {
    gui.drawTexturedModalRect(x + getX(), y + getY(), 0, 0, 18, 18);
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {}
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {}
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    return null;
  }
  
  public List<String> getToolTip() {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetSlot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */