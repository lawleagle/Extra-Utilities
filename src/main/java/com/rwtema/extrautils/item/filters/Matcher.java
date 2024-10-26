package com.rwtema.extrautils.item.filters;

import gnu.trove.map.hash.TIntByteHashMap;
import java.util.HashSet;
import java.util.Locale;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Matcher {
  public Type type;

  public final String name;

  public final String unlocalizedName;

  int index;

  private boolean addToNEI;

  public static String getUnlocalizedName(String name) {
    name = name.replaceAll("[^{A-Za-z0-9}]", "").toLowerCase(Locale.ENGLISH);
    return "item.extrautils:nodeUpgrade.10.program." + name;
  }

  private static String titleCase(String prefix) {
    return prefix.substring(0, 1).toUpperCase(Locale.ENGLISH) + prefix.substring(1, prefix.length());
  }

  public String getLocalizedName() {
    if (!StatCollector.canTranslate(this.unlocalizedName))
      return this.name + ".exe";
    return StatCollector.translateToLocal(this.unlocalizedName);
  }

  public boolean isSelectable() {
    return true;
  }

  public Matcher(String name) {
    this(name, true);
  }

  public Matcher(String name, boolean addToNEI) {
    this.name = name;
    this.addToNEI = addToNEI;
    this.unlocalizedName = getUnlocalizedName(name);
    boolean hasFluid = methodExists("matchFluid", getClass(), new Class[] { FluidStack.class });
    boolean hasItem = methodExists("matchItem", getClass(), new Class[] { ItemStack.class });
    int t = (hasFluid ? 1 : 0) + (hasItem ? 2 : 0);
    Type type = null;
    switch (t) {
      default:
        throw new RuntimeException("No overrided methods");
      case 1:
        type = Type.FLUID;
        break;
      case 2:
        type = Type.ITEM;
        break;
      case 3:
        type = Type.BOTH;
        break;
    }
    this.type = type;
  }

  public boolean methodExists(String method, Class<?> clazz, Class... classes) {
    try {
      clazz.getDeclaredMethod(method, classes);
      return true;
    } catch (NoSuchMethodException e) {
      clazz = clazz.getSuperclass();
      return (clazz != Matcher.class && methodExists(method, clazz, classes));
    }
  }

  public boolean matchFluid(FluidStack fluid) {
    return false;
  }

  public boolean matchItem(ItemStack item) {
    return false;
  }

  public boolean shouldAddToNEI() {
    return this.addToNEI;
  }

  public enum Type {
    FLUID, ITEM, BOTH;
  }

  public static class MatcherTool extends Matcher {
    private final String tool;

    public MatcherTool(String tool) {
      super("Tool" + Matcher.titleCase(tool));
      this.tool = tool;
    }

    public boolean matchItem(ItemStack item) {
      for (String s : item.getItem().getToolClasses(item)) {
        if (this.tool.equals(s))
          return true;
      }
      return false;
    }
  }

  public static class MatcherOreDic extends Matcher {
    private final String prefix;

    private final TIntByteHashMap map = new TIntByteHashMap();

    public MatcherOreDic(String prefix) {
      super("OreDic" + Matcher.titleCase(prefix));
      this.prefix = prefix;
    }

    public boolean matchItem(ItemStack item) {
      for (int i : OreDictionary.getOreIDs(item)) {
        if (this.map.containsKey(i)) {
          if (this.map.get(i) != 0)
            return true;
        } else {
          this.map.put(i, (byte)(OreDictionary.getOreName(i).startsWith(this.prefix) ? 1 : 0));
        }
      }
      return false;
    }

    public boolean isSelectable() {
      for (String s : OreDictionary.getOreNames()) {
        if (s.startsWith(this.prefix))
          return true;
      }
      return false;
    }
  }

  public static abstract class MatcherItem extends Matcher {
    HashSet<Item> entries;

    public MatcherItem(String name) {
      super(name);
    }

    public void buildMap() {
      this.entries = new HashSet<Item>();
      for (Object anItemRegistry : Item.itemRegistry) {
        Item item = (Item)anItemRegistry;
        if (matchItem(item))
          this.entries.add(item);
      }
    }

    public boolean matchItem(ItemStack item) {
      if (this.entries == null)
        buildMap();
      return this.entries.contains(item.getItem());
    }

    public boolean isSelectable() {
      if (this.entries == null)
        buildMap();
      return !this.entries.isEmpty();
    }

    protected abstract boolean matchItem(Item param1Item);
  }

  public static class MatcherOreDicPair extends Matcher {
    private final String prefix;

    private final String prefix2;

    private final TIntByteHashMap map = new TIntByteHashMap();

    public MatcherOreDicPair(String prefix, String prefix2) {
      super("OrePair" + Matcher.titleCase(prefix) + Matcher.titleCase(prefix2));
      this.prefix = prefix;
      this.prefix2 = prefix2;
    }

    public boolean matchItem(ItemStack item) {
      for (int i : OreDictionary.getOreIDs(item)) {
        String oreName = OreDictionary.getOreName(i);
        boolean isOre = oreName.startsWith(this.prefix);
        isOre = (isOre && oreExists(oreName.replaceFirst(this.prefix, this.prefix2)));
        this.map.put(i, isOre ? (byte)1 : (byte)0);
        if (isOre)
          return true;
      }
      return false;
    }

    public boolean oreExists(String k) {
      for (String s : OreDictionary.getOreNames()) {
        if (k.equals(s) &&
          !OreDictionary.getOres(k, false).isEmpty())
          return true;
      }
      return false;
    }

    public boolean isSelectable() {
      for (String s : OreDictionary.getOreNames()) {
        if (s.startsWith(this.prefix) && oreExists(s.replace(this.prefix, this.prefix2)))
          return true;
      }
      return false;
    }
  }

  public static class InverseMatch extends Matcher {
    private final Matcher matcher;

    public InverseMatch(String item, Matcher matcher) {
      super(item);
      this.matcher = matcher;
      this.type = matcher.type;
    }

    public boolean matchItem(ItemStack item) {
      return !this.matcher.matchItem(item);
    }

    public boolean matchFluid(FluidStack fluid) {
      return !this.matcher.matchFluid(fluid);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\filters\Matcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
