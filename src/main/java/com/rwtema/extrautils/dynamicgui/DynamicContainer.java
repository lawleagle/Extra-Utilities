package com.rwtema.extrautils.dynamicgui;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.ISidedFunction;
import com.rwtema.extrautils.gui.InventoryTweaksHelper;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketGUIWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public abstract class DynamicContainer extends Container {
  public static final int playerInvWidth = 162;

  public static final int playerInvHeight = 95;

  public static final ISidedFunction<String, Integer> stringWidth = new ISidedFunction<String, Integer>() {
      @SideOnly(Side.SERVER)
      public Integer applyServer(String input) {
        return Integer.valueOf((input == null) ? 0 : input.length());
      }

      @SideOnly(Side.CLIENT)
      public Integer applyClient(String input) {
        return Integer.valueOf((input == null) ? 0 : (Minecraft.getMinecraft()).fontRenderer.getStringWidth(input));
      }
    };

  public List<IWidget> widgets = new ArrayList<IWidget>();

  public int playerSlotsStart = -1;

  public LinkedList<EntityPlayerMP> playerCrafters = new LinkedList<EntityPlayerMP>();

  public int width = 176;

  public int height = 166;

  public boolean changesOnly = false;

  public void addSlot(WidgetSlot slot) {
    addSlotToContainer(slot);
  }

  public void addCraftingToCrafters(ICrafting par1ICrafting) {
    if (par1ICrafting instanceof EntityPlayerMP)
      this.playerCrafters.add((EntityPlayerMP)par1ICrafting);
    this.changesOnly = false;
    super.addCraftingToCrafters(par1ICrafting);
  }

  public void recieveDescriptionPacket(NBTTagCompound tag) {
    int n = this.widgets.size();
    for (int j = 0; j < n; j++) {
      if (tag.hasKey(Integer.toString(j))) {
        NBTBase nbtobj = tag.getTag(Integer.toString(j));
        if (nbtobj instanceof NBTTagCompound) {
          NBTTagCompound desc = (NBTTagCompound)nbtobj;
          if (desc.hasKey("name")) {
            int i = desc.getInteger("name");
            if (i >= 0 && i < this.widgets.size())
              ((IWidget)this.widgets.get(i)).handleDescriptionPacket(desc);
          }
        }
      }
    }
  }

  public void detectAndSendChanges() {
    NBTTagCompound tag = null;
    for (int i = 0; i < this.widgets.size(); i++) {
      NBTTagCompound t = ((IWidget)this.widgets.get(i)).getDescriptionPacket(this.changesOnly);
      if (t != null) {
        if (tag == null)
          tag = new NBTTagCompound();
        t.setInteger("name", i);
        tag.setTag(Integer.toString(i), (NBTBase)t);
      }
    }
    this.changesOnly = true;
    if (tag != null)
      for (EntityPlayerMP player : this.playerCrafters)
        NetworkHandler.sendPacketToPlayer((XUPacketBase)new PacketGUIWidget(this.windowId, tag), (EntityPlayer)player);
    super.detectAndSendChanges();
  }

  @SideOnly(Side.CLIENT)
  public void removeCraftingFromCrafters(ICrafting par1ICrafting) {
    if (par1ICrafting instanceof EntityPlayerMP)
      this.playerCrafters.remove(par1ICrafting);
    super.removeCraftingFromCrafters(par1ICrafting);
  }

  protected void validate() {
    for (IWidget widget : this.widgets)
      widget.addToContainer(this);
  }

  public void addPlayerSlotsToBottom(IInventory inventory) {
    addPlayerSlots(inventory, (this.width - 162) / 2, this.height - 95);
  }

  public void crop(int border) {
    int maxx = 18;
    int maxy = 18;
    for (IWidget widget : this.widgets) {
      maxx = Math.max(maxx, widget.getX() + widget.getW());
      maxy = Math.max(maxy, widget.getY() + widget.getH());
    }
    this.width = maxx + border;
    this.height = maxy + border;
  }

  public void cropAndAddPlayerSlots(IInventory inventory) {
    crop(4);
    this.height += 95;
    if (this.width < 170)
      this.width = 170;
    addPlayerSlotsToBottom(inventory);
  }

  public void addPlayerSlots(IInventory inventory, int x, int y) {
    this.playerSlotsStart = 0;
    for (IWidget w : this.widgets) {
      if (w instanceof Slot)
        this.playerSlotsStart++;
    }
    this.widgets.add(new WidgetTextTranslate(x, y, inventory.getInventoryName(), 162));
    int j;
    for (j = 0; j < 3; j++) {
      for (int k = 0; k < 9; k++) {
        WidgetSlot w = new WidgetSlot(inventory, k + j * 9 + 9, x + k * 18, y + 14 + j * 18);
        addWidget(w);
      }
    }
    for (j = 0; j < 9; j++) {
      WidgetSlot w = new WidgetSlot(inventory, j, x + j * 18, y + 14 + 58);
      addWidget(w);
    }
  }

  public void addWidget(IWidget w) {
    this.widgets.add(w);
  }

  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(par2);
    if (this.playerSlotsStart > 0 && slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 < this.playerSlotsStart) {
        if (!mergeItemStack(itemstack1, this.playerSlotsStart, this.inventorySlots.size(), true))
          return null;
      } else if (!mergeItemStack(itemstack1, 0, this.playerSlotsStart, false)) {
        return null;
      }
      if (itemstack1.stackSize == 0) {
        slot.putStack(null);
      } else {
        slot.onSlotChanged();
      }
    }
    return itemstack;
  }

  protected boolean mergeItemStack(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
    boolean flag1 = false;
    int k = p_75135_2_;
    if (p_75135_4_)
      k = p_75135_3_ - 1;
    if (p_75135_1_.isStackable())
      while (p_75135_1_.stackSize > 0 && ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_))) {
        Slot slot = (Slot)this.inventorySlots.get(k);
        ItemStack itemstack1 = slot.getStack();
        if (slot.isItemValid(p_75135_1_) && itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1)) {
          int l = itemstack1.stackSize + p_75135_1_.stackSize;
          if (l <= p_75135_1_.getMaxStackSize()) {
            p_75135_1_.stackSize = 0;
            itemstack1.stackSize = l;
            slot.onSlotChanged();
            flag1 = true;
          } else if (itemstack1.stackSize < p_75135_1_.getMaxStackSize()) {
            p_75135_1_.stackSize -= p_75135_1_.getMaxStackSize() - itemstack1.stackSize;
            itemstack1.stackSize = p_75135_1_.getMaxStackSize();
            slot.onSlotChanged();
            flag1 = true;
          }
        }
        if (p_75135_4_) {
          k--;
          continue;
        }
        k++;
      }
    if (p_75135_1_.stackSize > 0) {
      if (p_75135_4_) {
        k = p_75135_3_ - 1;
      } else {
        k = p_75135_2_;
      }
      while ((!p_75135_4_ && k < p_75135_3_) || (p_75135_4_ && k >= p_75135_2_)) {
        Slot slot = (Slot)this.inventorySlots.get(k);
        ItemStack itemstack1 = slot.getStack();
        if (itemstack1 == null && slot.isItemValid(p_75135_1_)) {
          slot.putStack(p_75135_1_.copy());
          slot.onSlotChanged();
          p_75135_1_.stackSize = 0;
          flag1 = true;
          break;
        }
        if (p_75135_4_) {
          k--;
          continue;
        }
        k++;
      }
    }
    return flag1;
  }

  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlots() {
    return InventoryTweaksHelper.getSlots(this, false);
  }

  public int getStringWidth(String text) {
    return ((Integer)ExtraUtilsMod.proxy.apply(stringWidth, text)).intValue();
  }

  public void addTitle(String name, boolean translate) {
    WidgetText e;
    if (translate) {
      e = new WidgetTextTranslate(5, 5, name, ((Integer)ExtraUtilsMod.proxy.apply(stringWidth, StatCollector.translateToLocal(name))).intValue());
    } else {
      e = new WidgetText(5, 5, name, ((Integer)ExtraUtilsMod.proxy.apply(stringWidth, name)).intValue());
    }
    this.widgets.add(e);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\DynamicContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
