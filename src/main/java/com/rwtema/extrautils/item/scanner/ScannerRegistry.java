package com.rwtema.extrautils.item.scanner;

import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.helper.XUHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class ScannerRegistry {
  public static List<IScanner> scanners = new ArrayList<IScanner>();
  
  private static boolean isSorted = false;
  
  static {
    addScanner(new scanTE());
    addScanner(new scanEntity());
    addScanner(new scanInv());
    addScanner(new scanSidedInv());
    addScanner(new scanTank());
    addScanner(new scanTE3Power());
  }
  
  public static void addScanner(IScanner scan) {
    scanners.add(scan);
    isSorted = false;
  }
  
  public static void sort() {
    Collections.sort(scanners, new SortScanners());
    isSorted = true;
  }
  
  public static List<String> getData(Object obj, ForgeDirection side, EntityPlayer player) {
    List<String> data = new ArrayList<String>();
    if (!isSorted)
      sort(); 
    for (IScanner scan : scanners) {
      if (scan.getTargetClass().isAssignableFrom(obj.getClass()))
        scan.addData(obj, data, side, player); 
    } 
    return data;
  }
  
  public static class SortScanners implements Comparator<IScanner> {
    public int compare(IScanner arg0, IScanner arg1) {
      int a = arg0.getPriority();
      int b = arg1.getPriority();
      if (a == b)
        return 0; 
      if (a < b)
        return -1; 
      return 1;
    }
  }
  
  public static class scanTE implements IScanner {
    public Class getTargetClass() {
      return TileEntity.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      NBTTagCompound tags = new NBTTagCompound();
      ((TileEntity)tile).writeToNBT(tags);
      data.add("~~ " + tags.getString("id") + " ~~");
      ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
      DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
      try {
        try {
          CompressedStreamTools.write(tags, dataoutputstream);
          data.add("Tile Data: " + bytearrayoutputstream.size());
        } finally {
          dataoutputstream.close();
        } 
      } catch (IOException ignored) {}
      if (player.capabilities.isCreativeMode)
        data.addAll(getString(tags)); 
    }
    
    public List<String> getString(NBTTagCompound tag) {
      List<String> v = new ArrayList<String>();
      appendStrings(v, (NBTBase)tag, "", "Tags");
      return v;
    }
    
    public void appendStrings(List<String> strings, NBTBase nbt, String prefix, String name) {
      if (nbt.getId() == XUHelper.NBTIds.NBT.id) {
        NBTTagCompound tag = (NBTTagCompound)nbt;
        if (tag.func_150296_c().isEmpty()) {
          strings.add(prefix + name + " = NBT{}");
        } else {
          strings.add(prefix + name + " = NBT{");
          ArrayList<String> l = new ArrayList<String>();
          l.addAll(tag.func_150296_c());
          Collections.sort(l);
          for (String key : l)
            appendStrings(strings, tag.getTag(key), prefix + "   ", key); 
          strings.add(prefix + "}");
        } 
      } else if (nbt.getId() == XUHelper.NBTIds.List.id) {
        NBTTagList tag = (NBTTagList)nbt;
        if (tag.tagCount() == 0) {
          strings.add(prefix + name + " = List{}");
        } else {
          strings.add(prefix + name + " = List{");
          for (int i = 0; i < tag.tagCount(); i++)
            appendStrings(strings, (NBTBase)tag.getCompoundTagAt(i), prefix + "   ", i + ""); 
          strings.add(prefix + "}");
        } 
      } else {
        strings.add(prefix + name + " = " + nbt.toString());
      } 
    }
    
    public int getPriority() {
      return -2147483647;
    }
  }
  
  public static class scanEntity implements IScanner {
    public Class getTargetClass() {
      return Entity.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      NBTTagCompound tags = new NBTTagCompound();
      if (((Entity)tile).writeToNBTOptional(tags)) {
        data.add("~~ " + tags.getString("id") + " ~~");
        data.add("Entity Data: " + tags.toString().length());
      } 
    }
    
    public int getPriority() {
      return Integer.MIN_VALUE;
    }
  }
  
  public static class scanEntityLiv implements IScanner {
    public Class getTargetClass() {
      return EntityLivingBase.class;
    }
    
    public void addData(Object target, List<String> data, ForgeDirection side, EntityPlayer player) {
      EntityLivingBase e = (EntityLivingBase)target;
      data.add(e.getHealth() + " / " + e.getMaxHealth());
    }
    
    public int getPriority() {
      return -110;
    }
  }
  
  public static class scanInv implements IScanner {
    public Class getTargetClass() {
      return IInventory.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      int n = ((IInventory)tile).getSizeInventory();
      if (n > 0) {
        int k = 0;
        for (int i = 0; i < n; i++) {
          if (((IInventory)tile).getStackInSlot(i) != null)
            k++; 
        } 
        data.add("Inventory Slots: " + k + " / " + n);
      } 
    }
    
    public int getPriority() {
      return -100;
    }
  }
  
  public static class scanSidedInv implements IScanner {
    public Class getTargetClass() {
      return ISidedInventory.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      int[] slots = ((ISidedInventory)tile).getAccessibleSlotsFromSide(side.ordinal());
      int k = 0;
      if (slots.length > 0) {
        for (int i = 0; i < slots.length; i++) {
          if (((ISidedInventory)tile).getStackInSlot(i) != null)
            k++; 
        } 
        data.add("Inventory Side Slots: " + k + " / " + slots.length);
      } 
    }
    
    public int getPriority() {
      return -99;
    }
  }
  
  public static class scanTank implements IScanner {
    public Class getTargetClass() {
      return IFluidHandler.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      FluidTankInfo[] tanks = ((IFluidHandler)tile).getTankInfo(side);
      if (tanks != null)
        if (tanks.length == 1) {
          if ((tanks[0]).fluid != null && (tanks[0]).fluid.amount > 0) {
            data.add("Fluid Tank: " + (tanks[0]).fluid.getFluid().getLocalizedName((tanks[0]).fluid) + " - " + (tanks[0]).fluid.amount + " / " + (tanks[0]).capacity);
          } else {
            data.add("Fluid Tank: Empty - 0 / " + (tanks[0]).capacity);
          } 
        } else {
          for (int i = 0; i < tanks.length; i++) {
            if ((tanks[i]).fluid != null && (tanks[i]).fluid.amount > 0) {
              data.add("Fluid Tank " + i + ": " + (tanks[i]).fluid.getFluid().getLocalizedName((tanks[i]).fluid) + " - " + (tanks[i]).fluid.amount + " / " + (tanks[i]).capacity);
            } else {
              data.add("Fluid Tank " + i + ": Empty - 0 / " + (tanks[i]).capacity);
            } 
          } 
        }  
    }
    
    public int getPriority() {
      return -98;
    }
  }
  
  public static class scanTE3Power implements IScanner {
    public Class getTargetClass() {
      return IEnergyHandler.class;
    }
    
    public void addData(Object tile, List<String> data, ForgeDirection side, EntityPlayer player) {
      IEnergyHandler a = (IEnergyHandler)tile;
      data.add(" TE3 Side Energy: " + a.getEnergyStored(side) + " / " + a.getMaxEnergyStored(side));
    }
    
    public int getPriority() {
      return 0;
    }
  }
  
  public static class scanShears implements IScanner {
    public Class getTargetClass() {
      return IShearable.class;
    }
    
    public void addData(Object target, List<String> data, ForgeDirection side, EntityPlayer player) {
      IShearable a = (IShearable)target;
      data.add("- Shearable");
    }
    
    public int getPriority() {
      return 0;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\scanner\ScannerRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */