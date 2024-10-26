package com.rwtema.extrautils.item.filters;

import cofh.api.energy.IEnergyContainerItem;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.asm.RemoteCallFactory;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.fluids.FluidStack;

public class AdvancedNodeUpgrades {
  public static HashMap<String, Matcher> matcherMap = new HashMap<String, Matcher>();
  
  public static ArrayList<Matcher> entryList = new ArrayList<Matcher>();
  
  public static Matcher nullMatcher;
  
  public static boolean problemCodePresent = (Loader.isModLoaded("gregtech") || Loader.isModLoaded("gregapi") || ModAPIManager.INSTANCE.hasAPI("gregapi"));
  
  static {
    nullMatcher = new Matcher("Default", false) {
        public boolean matchFluid(FluidStack fluid) {
          return true;
        }
        
        public boolean matchItem(ItemStack fluid) {
          return true;
        }
      };
    addEntry(nullMatcher);
    Matcher m;
    addEntry(m = new Matcher.MatcherItem("Block") {
          protected boolean matchItem(Item item) {
            return item instanceof net.minecraft.item.ItemBlock;
          }
        });
    addEntry(new Matcher.InverseMatch("Item", m));
    addEntry(new Matcher.MatcherItem("HasSubTypes") {
          protected boolean matchItem(Item item) {
            return item.getHasSubtypes();
          }
        });
    addEntry(new Matcher("StackSize1") {
          public boolean matchItem(ItemStack item) {
            return (item.getMaxStackSize() == 1);
          }
        });
    addEntry(new Matcher("StackSize64") {
          public boolean matchItem(ItemStack item) {
            return (item.getMaxStackSize() == 64);
          }
        });
    addEntry(new Matcher.MatcherOreDic("ore"));
    addEntry(new Matcher.MatcherOreDic("ingot"));
    addEntry(new Matcher.MatcherOreDic("nugget"));
    addEntry(new Matcher.MatcherOreDic("block"));
    addEntry(new Matcher.MatcherOreDic("gem"));
    addEntry(new Matcher.MatcherOreDic("dust"));
    addEntry(new Matcher.MatcherItem("EnergyItem") {
          public boolean matchItem(ItemStack item) {
            return (super.matchItem(item) && ((IEnergyContainerItem)item.getItem()).getMaxEnergyStored(item) != 0);
          }
          
          protected boolean matchItem(Item item) {
            return item instanceof IEnergyContainerItem;
          }
        });
    addEntry(new Matcher.MatcherItem("EnergyItemEmpty") {
          public boolean matchItem(ItemStack item) {
            return (super.matchItem(item) && ((IEnergyContainerItem)item.getItem()).getMaxEnergyStored(item) > 0 && ((IEnergyContainerItem)item.getItem()).getEnergyStored(item) == 0);
          }
          
          protected boolean matchItem(Item item) {
            return item instanceof IEnergyContainerItem;
          }
        });
    addEntry(new Matcher.MatcherItem("EnergyItem<50") {
          public boolean matchItem(ItemStack item) {
            if (!super.matchItem(item))
              return false; 
            IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)item.getItem();
            int maxEnergyStored = energyContainerItem.getMaxEnergyStored(item);
            return (maxEnergyStored > 0 && energyContainerItem.getEnergyStored(item) <= maxEnergyStored >> 1);
          }
          
          protected boolean matchItem(Item item) {
            return item instanceof IEnergyContainerItem;
          }
        });
    addEntry(new Matcher.MatcherItem("EnergyItemFull") {
          public boolean matchItem(ItemStack item) {
            if (!super.matchItem(item))
              return false; 
            IEnergyContainerItem energyContainerItem = (IEnergyContainerItem)item.getItem();
            int maxEnergyStored = energyContainerItem.getMaxEnergyStored(item);
            return (maxEnergyStored != 0 && energyContainerItem.getEnergyStored(item) == maxEnergyStored);
          }
          
          protected boolean matchItem(Item item) {
            return item instanceof IEnergyContainerItem;
          }
        });
    addEntry(new Matcher.MatcherItem("Food") {
          protected boolean matchItem(Item item) {
            return item instanceof net.minecraft.item.ItemFood;
          }
        });
    addEntry(new Matcher("Smeltable") {
          public boolean matchItem(ItemStack item) {
            return (FurnaceRecipes.smelting().getSmeltingResult(item) != null);
          }
        });
    if (RemoteCallFactory.pulverizer != null)
      addEntry(new Matcher("Pulverizer") {
            public boolean matchItem(ItemStack item) {
              return RemoteCallFactory.pulverizer.evaluate(item);
            }
          }); 
    addEntry(new Matcher("Enchanted") {
          public boolean matchItem(ItemStack item) {
            return item.isItemEnchanted();
          }
        });
    addEntry(new Matcher("Enchantable") {
          public boolean matchItem(ItemStack item) {
            return item.isItemEnchantable();
          }
        });
    if (!problemCodePresent)
      addEntry(new Matcher("HasContainerItem") {
            public boolean matchItem(ItemStack item) {
              return item.getItem().hasContainerItem(item);
            }
          }); 
    addEntry(new Matcher("DurabilityBarShown") {
          public boolean matchItem(ItemStack item) {
            return item.getItem().showDurabilityBar(item);
          }
        });
    addEntry(new Matcher("DurabilityBarFull") {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return (item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) <= 0.0D);
          }
        });
    addEntry(new Matcher("DurabilityBar<90", false) {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return (item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.5D);
          }
        });
    addEntry(new Matcher("DurabilityBar<50", false) {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return (item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.5D);
          }
        });
    addEntry(new Matcher("DurabilityBar<10", false) {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return (item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 0.9D);
          }
        });
    addEntry(new Matcher("DurabilityBarEmpty") {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return (item.showDurabilityBar(stack) && item.getDurabilityForDisplay(stack) >= 1.0D);
          }
        });
    addEntry(new Matcher("HasDisplayName", false) {
          public boolean matchItem(ItemStack stack) {
            return stack.hasDisplayName();
          }
        });
    addEntry(new Matcher("Repairable") {
          public boolean matchItem(ItemStack stack) {
            Item item = stack.getItem();
            return item.isRepairable();
          }
        });
    addEntry(new Matcher.MatcherItem("HasDispenserBehaviour") {
          public boolean matchItem(Item item) {
            return ((RegistrySimple)BlockDispenser.dispenseBehaviorRegistry).containsKey(item);
          }
        });
    addEntry(new Matcher.MatcherItem("Plantable") {
          protected boolean matchItem(Item item) {
            return item instanceof net.minecraftforge.common.IPlantable;
          }
        });
    if (LogHelper.isDeObf) {
      StringBuilder builder = new StringBuilder();
      for (Matcher matcher : entryList) {
        builder.append('\n');
        builder.append(matcher.unlocalizedName);
        builder.append("=");
        builder.append(matcher.name);
        builder.append(".exe");
      } 
      LogHelper.info(builder.toString(), new Object[0]);
    } 
  }
  
  public static Matcher getMatcher(ItemStack itemStack) {
    if (itemStack.hasTagCompound()) {
      Matcher matcher = matcherMap.get(itemStack.getTagCompound().getString("Matcher"));
      if (matcher != null)
        return matcher; 
    } 
    return nullMatcher;
  }
  
  public static Matcher nextEntry(ItemStack itemStack, boolean next) {
    int i = 0;
    if (!itemStack.hasTagCompound())
      itemStack.setTagCompound(new NBTTagCompound()); 
    NBTTagCompound tags = itemStack.getTagCompound();
    Matcher matcher = matcherMap.get(tags.getString("Matcher"));
    if (matcher != null)
      i = matcher.index; 
    do {
      if (next) {
        i++;
        if (i >= entryList.size())
          i = 0; 
      } else {
        i--;
        if (i < 0)
          i = entryList.size() - 1; 
      } 
      matcher = entryList.get(i);
    } while (!matcher.isSelectable());
    tags.setString("Matcher", matcher.name);
    return matcher;
  }
  
  public static void addEntry(Matcher matcher) {
    String entry = matcher.name;
    matcher.index = entryList.size();
    entryList.add(matcher);
    matcherMap.put(entry, matcher);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\filters\AdvancedNodeUpgrades.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */