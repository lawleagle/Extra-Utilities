package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemIngredient extends Item {
  public ArrayList<String> textures = new ArrayList<String>();

  public ArrayList<String> names = new ArrayList<String>();

  public ArrayList<IIcon> icons = new ArrayList<IIcon>();

  public HashSet<Integer> ids = new HashSet<Integer>();

  public int numItems = 1;

  public ItemIngredient() {
    addItem(0, "Error", "error");
    this.ids.remove(Integer.valueOf(0));
    setHasSubtypes(true);
  }

  public void addItem(int metadata, String name, String texture) {
    if (this.numItems < 1 + metadata) {
      this.numItems = 1 + metadata;
      this.textures.ensureCapacity(this.numItems);
      this.names.ensureCapacity(this.numItems);
      this.icons.ensureCapacity(this.numItems);
    }
    this.textures.set(metadata, texture);
    this.names.set(metadata, name);
    this.ids.add(Integer.valueOf(metadata));
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    for (Integer i : this.ids)
      this.icons.set(i.intValue(), par1IIconRegister.registerIcon("extrautils:" + (String)this.textures.get(i.intValue())));
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (Integer id : this.ids)
      par3List.add(new ItemStack(par1, 1, id.intValue()));
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    return this.icons.get(getMetaData(par1));
  }

  public int getMetaData(ItemStack item) {
    return getMetaData(item.getItemDamage());
  }

  public int getMetaData(int metadata) {
    if (!this.ids.contains(Integer.valueOf(metadata)))
      return 0;
    return metadata;
  }

  public String getUnlocalizedName(ItemStack par1ItemStack) {
    return getUnlocalizedName() + "." + getMetaData(par1ItemStack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemIngredient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
