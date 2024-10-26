package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemErosionShovel extends ItemSpade implements IItemMultiTransparency {
  private IIcon[] icons;
  
  public ItemErosionShovel() {
    super(Item.ToolMaterial.EMERALD);
    setUnlocalizedName("extrautils:erosionShovel");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 4);
  }
  
  public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
    float t = super.func_150893_a(par1ItemStack, par2Block);
    t = Math.max(t, Items.diamond_shovel.func_150893_a(par1ItemStack, par2Block));
    return t * 2.2F;
  }
  
  public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[2];
    this.itemIcon = this.icons[0] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5));
    this.icons[1] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5) + "1");
  }
  
  public int numIcons(ItemStack item) {
    return 2;
  }
  
  public IIcon getIconForTransparentRender(ItemStack item, int pass) {
    return this.icons[pass];
  }
  
  public float getIconTransparency(ItemStack item, int pass) {
    if (pass == 1)
      return 0.5F; 
    return 1.0F;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemErosionShovel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */