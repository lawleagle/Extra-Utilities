package com.rwtema.extrautils.tileentity.generators;

import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.dynamicgui.WidgetEnergy;
import com.rwtema.extrautils.dynamicgui.WidgetSlot;
import com.rwtema.extrautils.dynamicgui.WidgetTank;
import com.rwtema.extrautils.dynamicgui.WidgetText;
import com.rwtema.extrautils.dynamicgui.WidgetTextData;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;

@InventoryContainer
public class DynamicContainerGenerator extends DynamicContainer {
  TileEntityGenerator gen;
  
  public TileEntityGeneratorFurnace genFurnace = null;
  
  public DynamicContainerGenerator(IInventory player, TileEntityGenerator gen) {
    this.gen = gen;
    if (this.gen instanceof TileEntityGeneratorFurnace)
      this.genFurnace = (TileEntityGeneratorFurnace)this.gen; 
    this.widgets.add(new WidgetText(5, 5, BlockGenerator.names[gen.getBlockMetadata()] + " Generator", 162));
    int x = 5, y = 19;
    if (gen instanceof IInventory) {
      IInventory inv = (IInventory)gen;
      for (int i = 0; i < inv.getSizeInventory(); i++) {
        WidgetSlot widgetSlot = new WidgetSlot(inv, i, x, y);
        this.widgets.add(widgetSlot);
        x += widgetSlot.getW() + 5;
      } 
    } 
    if (gen instanceof net.minecraftforge.fluids.IFluidHandler) {
      FluidTankInfo[] tanks = gen.getTankInfo((ForgeDirection)null);
      for (FluidTankInfo tank : tanks) {
        WidgetTank widgetTank = new WidgetTank(tank, x, y, 2);
        this.widgets.add(widgetTank);
        x += widgetTank.getW() + 5;
      } 
    } 
    WidgetTextCooldown widgetTextCooldown = new WidgetTextCooldown(gen, x, y, 120);
    this.widgets.add(widgetTextCooldown);
    x += widgetTextCooldown.getW() + 5;
    this.widgets.add(new WidgetEnergy(gen, ForgeDirection.UP, x, y));
    cropAndAddPlayerSlots(player);
    validate();
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return true;
  }
  
  public class WidgetTextCooldown extends WidgetTextData {
    TileEntityGenerator gen;
    
    public WidgetTextCooldown(TileEntityGenerator gen, int x, int y, int w) {
      super(x, y, w);
      this.gen = gen;
    }
    
    public int getNumParams() {
      return 2;
    }
    
    public Object[] getData() {
      return new Object[] { Long.valueOf((long)(10.0D * this.gen.coolDown)), Long.valueOf((long)Math.ceil(10.0D * this.gen.genLevel() * this.gen.getMultiplier())) };
    }
    
    public String getConstructedText() {
      double t;
      double t2;
      if (this.curData == null || this.curData[0] == null)
        return ""; 
      try {
        t = ((Long)this.curData[0]).longValue() / 200.0D;
        t2 = ((Long)this.curData[1]).longValue() / 10.0D;
      } catch (Exception e) {
        return "";
      } 
      return this.gen.getBlurb(t, t2);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\DynamicContainerGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */