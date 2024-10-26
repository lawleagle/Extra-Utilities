package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.dynamicgui.WidgetProgressArrow;
import com.rwtema.extrautils.dynamicgui.WidgetSlot;
import com.rwtema.extrautils.dynamicgui.WidgetSlotGhost;
import com.rwtema.extrautils.dynamicgui.WidgetSlotRespectsInsertExtract;
import com.rwtema.extrautils.dynamicgui.WidgetTextData;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

@InventoryContainer
public class DynamicContainerEnderConstructor extends DynamicContainer {
  public TileEnderConstructor tile;
  
  public IInventory player;
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    return super.transferStackInSlot(par1EntityPlayer, par2);
  }
  
  public DynamicContainerEnderConstructor(IInventory player, TileEnderConstructor tile) {
    this.tile = tile;
    this.player = player;
    for (int j = 0; j < 3; j++) {
      for (int i = 0; i < 3; i++)
        this.widgets.add(new WidgetSlot((IInventory)tile.inv, i + j * 3, 30 + i * 18, 17 + j * 18)); 
    } 
    this.widgets.add(new WidgetSlotRespectsInsertExtract(tile, 9, 124, 35));
    this.widgets.add(new Arrow(tile, 90, 35));
    this.widgets.add(new WidgetSlotGhost((IInventory)tile.inv, 9, 92, 13));
    this.widgets.add(new WidgetEFText(tile, 9, 75, 124));
    cropAndAddPlayerSlots(player);
    validate();
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return true;
  }
  
  public static class Arrow extends WidgetProgressArrow {
    TileEnderConstructor tile;
    
    public Arrow(TileEnderConstructor tile, int x, int y) {
      super(x, y);
      this.tile = tile;
    }
    
    public int getWidth() {
      return this.tile.getDisplayProgress();
    }
  }
  
  public static class WidgetEFText extends WidgetTextData {
    IEnderFluxHandler tile;
    
    public WidgetEFText(IEnderFluxHandler tile, int x, int y, int w) {
      super(x, y, w);
      this.tile = tile;
    }
    
    public int getNumParams() {
      return 1;
    }
    
    public Object[] getData() {
      return new Object[] { Float.valueOf(this.tile.getAmountRequested()), Byte.valueOf((byte)(this.tile.isActive() ? 1 : 0)) };
    }
    
    public String getConstructedText() {
      if (this.curData == null || this.curData.length != 2 || !(this.curData[0] instanceof Float) || !(this.curData[1] instanceof Boolean))
        return ""; 
      if (((Byte)this.curData[1]).byteValue() == 1)
        return "Ender-Flux: " + (((Float)this.curData[0]).floatValue() / 1000.0F) + " EF"; 
      return "";
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\DynamicContainerEnderConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */