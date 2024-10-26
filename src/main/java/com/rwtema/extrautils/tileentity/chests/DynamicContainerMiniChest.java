package com.rwtema.extrautils.tileentity.chests;

import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.dynamicgui.WidgetDescPacket;
import com.rwtema.extrautils.dynamicgui.WidgetSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class DynamicContainerMiniChest extends DynamicContainer {
  private final TileMiniChest inv;
  
  public DynamicContainerMiniChest(IInventory player, TileMiniChest inv) {
    this.inv = inv;
    int midPoint = 76;
    this.widgets.add(new WidgetSlot(inv, 0, midPoint, 19));
    this.widgets.add(new WidgetDescPacket() {
          public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
            if (changesOnly || !DynamicContainerMiniChest.this.inv.hasCustomInventoryName())
              return null; 
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Name", DynamicContainerMiniChest.this.inv.getInventoryName());
            return tag;
          }
          
          public void handleDescriptionPacket(NBTTagCompound packet) {
            String name = packet.getString("Name");
            if (name.length() > 0)
              DynamicContainerMiniChest.this.inv.func_145976_a(name); 
          }
        });
    addTitle(inv.getInventoryName(), !inv.hasCustomInventoryName());
    cropAndAddPlayerSlots(player);
    validate();
  }
  
  public boolean canInteractWith(EntityPlayer player) {
    return this.inv.isUseableByPlayer(player);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\chests\DynamicContainerMiniChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */