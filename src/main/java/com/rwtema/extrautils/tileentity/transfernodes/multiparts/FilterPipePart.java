package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;

public class FilterPipePart extends PipePart implements IFilterPipe {
  public InventoryBasic items = new InventoryBasic("Sorting Pipe", true, 6);
  
  public FilterPipePart(int meta) {
    super(9);
  }
  
  public FilterPipePart(InventoryBasic items) {
    super(9);
    this.items = items;
  }
  
  public FilterPipePart() {
    super(9);
  }
  
  public void onRemoved() {
    if (!(getWorld()).isRemote) {
      List<ItemStack> drops = new ArrayList<ItemStack>();
      for (int i = 0; i < this.items.getSizeInventory(); i++) {
        if (this.items.getStackInSlot(i) != null)
          drops.add(this.items.getStackInSlot(i)); 
      } 
      tile().dropItems(drops);
    } 
  }
  
  public String getType() {
    return "extrautils:transfer_pipe_filter";
  }
  
  public boolean activate(EntityPlayer player, MovingObjectPosition part, ItemStack item) {
    player.openGui(ExtraUtilsMod.instance, 0, getWorld(), x(), y(), z());
    return true;
  }
  
  public void load(NBTTagCompound tags) {
    super.load(tags);
    if (tags.hasKey("items")) {
      NBTTagCompound item_tags = tags.getCompoundTag("items");
      for (int i = 0; i < this.items.getSizeInventory(); i++) {
        if (item_tags.hasKey("item_" + i))
          this.items.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(item_tags.getCompoundTag("item_" + i))); 
      } 
    } 
  }
  
  public void save(NBTTagCompound par1NBTTagCompound) {
    super.save(par1NBTTagCompound);
    NBTTagCompound item_tags = new NBTTagCompound();
    for (int i = 0; i < this.items.getSizeInventory(); i++) {
      if (this.items.getStackInSlot(i) != null) {
        NBTTagCompound item = new NBTTagCompound();
        this.items.getStackInSlot(i).writeToNBT(item);
        item_tags.setTag("item_" + i, (NBTBase)item);
      } 
    } 
    par1NBTTagCompound.setTag("items", (NBTBase)item_tags);
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return (IInventory)this.items;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\FilterPipePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */