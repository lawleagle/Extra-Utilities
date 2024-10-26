package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemTemporalHoe extends ItemHoe implements IItemMultiTransparency {
  private IIcon[] icons;
  
  public ItemTemporalHoe() {
    super(Item.ToolMaterial.EMERALD);
    this.maxStackSize = 1;
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:temporalHoe");
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return false;
  }
  
  public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
    if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
      return false; 
    UseHoeEvent event = new UseHoeEvent(p_77648_2_, p_77648_1_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
    if (MinecraftForge.EVENT_BUS.post((Event)event))
      return false; 
    if (event.getResult() == Event.Result.ALLOW)
      return true; 
    Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
    if (p_77648_7_ != 0 && p_77648_3_.getBlock(p_77648_4_, p_77648_5_ + 1, p_77648_6_).isAir((IBlockAccess)p_77648_3_, p_77648_4_, p_77648_5_ + 1, p_77648_6_) && (block == Blocks.grass || block == Blocks.dirt)) {
      Block block1 = Blocks.farmland;
      p_77648_3_.playSoundEffect((p_77648_4_ + 0.5F), (p_77648_5_ + 0.5F), (p_77648_6_ + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
      if (p_77648_3_.isRemote)
        return true; 
      p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, block1);
      return true;
    } 
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
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


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemTemporalHoe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */