package com.rwtema.extrautils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CreativeTabExtraUtils extends CreativeTabs {
    private itemSorter alphabeticalSorter = new itemSorter();

    public CreativeTabExtraUtils(String label) {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (ExtraUtils.angelBlock != null)
            return new ItemStack(ExtraUtils.angelBlock, 1, 0);
        if (ExtraUtils.creativeTabIcon != null)
            return new ItemStack(ExtraUtils.creativeTabIcon, 1, 0);
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List par1List) {
        List<?> newList = new ArrayList();
        super.displayAllReleventItems(newList);
        Collections.sort(newList, this.alphabeticalSorter);
        par1List.addAll(newList);
        for (Object _item : newList) {
            ItemStack item = (ItemStack)_item;
            if (item.getDisplayName().endsWith(".name"))
                LogHelper.debug("Missing localization data for " + item.getDisplayName(), new Object[0]);
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return null;
    }

    private static class itemSorter implements Comparator {
        private itemSorter() {}

        public int compare(Object arg0, Object arg1) {
            ItemStack i0 = (ItemStack)arg0;
            ItemStack i1 = (ItemStack)arg1;
            if (i0.getItem() instanceof ItemBlock && !(i1.getItem() instanceof ItemBlock))
                return -1;
            if (i1.getItem() instanceof ItemBlock && !(i0.getItem() instanceof ItemBlock))
                return 1;
            String a = getString(i0);
            String b = getString(i1);
            return (int)Math.signum(a.compareToIgnoreCase(b));
        }

        public String getString(ItemStack item) {
            if (item.getItem() instanceof ICreativeTabSorting)
                return ((ICreativeTabSorting)item.getItem()).getSortingName(item);
            if (item.getItem() instanceof ItemBlock) {
                Block block_id = ((ItemBlock)item.getItem()).field_150939_a;
                if (block_id instanceof ICreativeTabSorting)
                    ((ICreativeTabSorting)block_id).getSortingName(item);
            }
            return item.getDisplayName();
        }
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\CreativeTabExtraUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
