package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilingCabinet extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/guiFilingCabinet.png");
  
  ItemSorter sorter = new ItemSorter();
  
  private int numItems = 0;
  
  private int currentScroll = 0;
  
  private boolean isScrolling = false;
  
  private int prevn = -1;
  
  private TileEntityFilingCabinet cabinet;
  
  public GuiFilingCabinet(IInventory player, TileEntityFilingCabinet cabinet) {
    super(new ContainerFilingCabinet(player, cabinet, true));
    this.xSize = 176;
    this.ySize = 240;
    this.prevn = cabinet.getMaxSlots();
    this.cabinet = cabinet;
  }
  
  public void sortItems() {
    List<Slot> items = new ArrayList<Slot>();
    items.clear();
    this.numItems = 0;
    for (int i = 0; i < this.cabinet.getMaxSlots(); i++) {
      if (((Slot)this.inventorySlots.inventorySlots.get(i)).getHasStack()) {
        this.numItems++;
      } else if (i > this.prevn) {
        break;
      } 
      items.add(this.inventorySlots.inventorySlots.get(i));
    } 
    this.prevn = this.numItems + 1;
    Collections.sort(items, this.sorter);
    int start = getStartSlot();
    if (start > this.numItems - 54)
      start = this.numItems - 54; 
    if (start < 0)
      start = 0; 
    for (int j = 0; j < items.size(); j++) {
      if (j < start || j >= start + 54) {
        ((Slot)items.get(j)).xDisplayPosition = Integer.MIN_VALUE;
        ((Slot)items.get(j)).yDisplayPosition = Integer.MIN_VALUE;
      } else {
        int x = (j - start) % 9;
        int y = (j - start - x) / 9;
        ((Slot)items.get(j)).xDisplayPosition = 8 + x * 18;
        ((Slot)items.get(j)).yDisplayPosition = 18 + y * 18;
      } 
    } 
    ContainerFilingCabinet.updated = false;
  }
  
  public int getStartSlot() {
    float t = this.currentScroll;
    t /= 144.0F;
    return (int)Math.floor(t * Math.ceil(((this.numItems - 54 + 1) / 1.0F)));
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    if (this.numItems <= 54) {
      drawTexturedModalRect(k + ScrollX(), l + 128, 176, 9, 17, 8);
    } else {
      drawTexturedModalRect(k + ScrollX(), l + 128, 176, 0, 17, 8);
    } 
  }
  
  public void setScroll(int mX) {
    int prevScroll = getStartSlot();
    if (this.numItems <= 54) {
      this.currentScroll = 0;
    } else {
      this.currentScroll = mX;
    } 
    if (prevScroll != getStartSlot())
      sortItems(); 
  }
  
  public int ScrollX() {
    if (this.numItems <= 54)
      return 8; 
    if (this.currentScroll < 0)
      return 8; 
    if (this.currentScroll > 143)
      return 151; 
    return 8 + this.currentScroll;
  }
  
  protected void mouseMovedOrUp(int par1, int par2, int par3) {
    if (par3 >= 0)
      this.isScrolling = false; 
    super.mouseMovedOrUp(par1, par2, par3);
  }
  
  protected void mouseClicked(int par1, int par2, int par3) {
    if (!this.isScrolling && par3 == 0 && 
      func_146978_c(8, 128, 162, 8, par1, par2)) {
      this.isScrolling = true;
      setScroll(par1 - this.guiLeft - 8 - 9);
    } 
    super.mouseClicked(par1, par2, par3);
  }
  
  protected void mouseClickMove(int par1, int par2, int par3, long par4) {
    if (this.isScrolling)
      setScroll(par1 - this.guiLeft - 8 - 9); 
    super.mouseClickMove(par1, par2, par3, par4);
  }
  
  public void handleMouseInput() {
    super.handleMouseInput();
    int i = Mouse.getEventDWheel();
    if (i != 0 && this.numItems > 54) {
      if (i > 0)
        i = 1; 
      if (i < 0)
        i = -1; 
      this.currentScroll -= i * 9;
      if (this.currentScroll < 0)
        this.currentScroll = 0; 
      if (this.currentScroll > 153)
        this.currentScroll = 153; 
      setScroll(this.currentScroll);
      sortItems();
    } 
  }
  
  public void drawScreen(int par1, int par2, float par3) {
    if (ContainerFilingCabinet.updated)
      sortItems(); 
    SlotFilingCabinet.drawing = true;
    super.drawScreen(par1, par2, par3);
    SlotFilingCabinet.drawing = false;
  }
  
  public static class ItemSorter implements Comparator<Slot> {
    public int compare(Slot arg0, Slot arg1) {
      if (!arg0.getHasStack()) {
        if (!arg1.getHasStack())
          return 0; 
        return 1;
      } 
      if (!arg1.getHasStack())
        return -1; 
      EntityClientPlayerMP player = (Minecraft.getMinecraft()).thePlayer;
      boolean sneak = player.movementInput.sneak;
      player.movementInput.sneak = false;
      String i1 = concat(arg0.getStack().getTooltip((EntityPlayer)(Minecraft.getMinecraft()).thePlayer, true));
      String i2 = concat(arg1.getStack().getTooltip((EntityPlayer)(Minecraft.getMinecraft()).thePlayer, true));
      player.movementInput.sneak = sneak;
      int a = i1.compareTo(i2);
      if (a == 0) {
        int b = arg0.getStack().getItem().getUnlocalizedName().compareTo(arg0.getStack().getItem().getUnlocalizedName());
        if (b != 0)
          return b; 
        int c = intCompare(arg0.getStack().getItemDamage(), arg0.getStack().getItemDamage());
        if (c != 0)
          return c; 
        return 0;
      } 
      return a;
    }
    
    public int intCompare(int a, int b) {
      if (a == b)
        return 0; 
      if (a > b)
        return 1; 
      return -1;
    }
    
    public String concat(List list) {
      String s = "";
      for (Object aList : list)
        s = s + aList + "\n"; 
      return s;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */