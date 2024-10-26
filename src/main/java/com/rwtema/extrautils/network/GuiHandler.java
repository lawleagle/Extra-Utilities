package com.rwtema.extrautils.network;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.dynamicgui.DynamicGui;
import com.rwtema.extrautils.dynamicgui.IDynamicContainerProvider;
import com.rwtema.extrautils.gui.ContainerFilingCabinet;
import com.rwtema.extrautils.gui.ContainerFilter;
import com.rwtema.extrautils.gui.ContainerFilterPipe;
import com.rwtema.extrautils.gui.ContainerHoldingBag;
import com.rwtema.extrautils.gui.ContainerTransferNode;
import com.rwtema.extrautils.gui.ContainerTrashCan;
import com.rwtema.extrautils.gui.GuiFilingCabinet;
import com.rwtema.extrautils.gui.GuiFilter;
import com.rwtema.extrautils.gui.GuiFilterPipe;
import com.rwtema.extrautils.gui.GuiHoldingBag;
import com.rwtema.extrautils.gui.GuiTradingPost;
import com.rwtema.extrautils.gui.GuiTransferNode;
import com.rwtema.extrautils.gui.GuiTrashCan;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import com.rwtema.extrautils.tileentity.enderconstructor.DynamicContainerEnderConstructor;
import com.rwtema.extrautils.tileentity.enderconstructor.DynamicGuiEnderConstructor;
import com.rwtema.extrautils.tileentity.enderconstructor.TileEnderConstructor;
import com.rwtema.extrautils.tileentity.generators.DynamicContainerGenerator;
import com.rwtema.extrautils.tileentity.generators.TileEntityGenerator;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
  public static final int TILE_ENTITY = 0;
  
  public static final int PLAYER_INVENTORY = 1;
  
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == 0) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof INode)
        return new ContainerTransferNode((IInventory)player.inventory, ((INode)tile).getNode()); 
      if (tile instanceof TileEntityTrashCan)
        return new ContainerTrashCan((IInventory)player.inventory, (TileEntityTrashCan)tile); 
      if (tile instanceof IFilterPipe)
        return new ContainerFilterPipe((IInventory)player.inventory, ((IFilterPipe)tile).getFilterInventory((IBlockAccess)world, x, y, z)); 
      if (tile instanceof TileEntityFilingCabinet)
        return new ContainerFilingCabinet((IInventory)player.inventory, (TileEntityFilingCabinet)tile, false); 
      if (tile instanceof TileEntityGenerator)
        return new DynamicContainerGenerator((IInventory)player.inventory, (TileEntityGenerator)tile); 
      if (tile instanceof TileEnderConstructor)
        return new DynamicContainerEnderConstructor((IInventory)player.inventory, (TileEnderConstructor)tile); 
      if (tile instanceof IDynamicContainerProvider)
        return ((IDynamicContainerProvider)tile).getContainer(player); 
    } else if (ID == 1) {
      if (x >= player.inventory.mainInventory.length)
        return null; 
      ItemStack item = player.inventory.getStackInSlot(x);
      if (item != null) {
        if (ExtraUtils.nodeUpgrade != null && item.getItem() == ExtraUtils.nodeUpgrade && item.getItemDamage() == 1)
          return new ContainerFilter(player, x); 
        if (ExtraUtils.goldenBag != null && item.getItem() == ExtraUtils.goldenBag)
          return new ContainerHoldingBag(player, x); 
      } 
    } 
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (ID == 0) {
      if (tile instanceof TileEntityTradingPost) {
        TileEntityTradingPost trade = (TileEntityTradingPost)tile;
        return new GuiTradingPost(player, trade.ids, trade.data, (TileEntity)trade);
      } 
      if (tile instanceof INode)
        return new GuiTransferNode((IInventory)player.inventory, ((INode)tile).getNode()); 
      if (tile instanceof TileEntityTrashCan)
        return new GuiTrashCan((IInventory)player.inventory, (TileEntityTrashCan)tile); 
      if (tile instanceof IFilterPipe)
        return new GuiFilterPipe((IInventory)player.inventory, ((IFilterPipe)tile).getFilterInventory((IBlockAccess)world, x, y, z)); 
      if (tile instanceof TileEntityFilingCabinet)
        return new GuiFilingCabinet((IInventory)player.inventory, (TileEntityFilingCabinet)tile); 
      if (tile instanceof TileEntityGenerator)
        return new DynamicGui((DynamicContainer)new DynamicContainerGenerator((IInventory)player.inventory, (TileEntityGenerator)tile)); 
      if (tile instanceof TileEnderConstructor)
        return new DynamicGuiEnderConstructor((DynamicContainer)new DynamicContainerEnderConstructor((IInventory)player.inventory, (TileEnderConstructor)tile)); 
      if (tile instanceof IDynamicContainerProvider)
        return new DynamicGui(((IDynamicContainerProvider)tile).getContainer(player)); 
    } else if (ID == 1) {
      if (x >= player.inventory.mainInventory.length)
        return null; 
      ItemStack item = player.inventory.getStackInSlot(x);
      if (item != null) {
        if (ExtraUtils.nodeUpgrade != null && item.getItem() == ExtraUtils.nodeUpgrade && item.getItemDamage() == 1)
          return new GuiFilter(player, x); 
        if (ExtraUtils.goldenBag != null && item.getItem() == ExtraUtils.goldenBag)
          return new GuiHoldingBag(player, x); 
      } 
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\GuiHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */