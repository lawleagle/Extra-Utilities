package com.rwtema.extrautils.item;

import com.rwtema.extrautils.block.BlockSpike;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemSwordSpike extends ItemSword {
  public static final Field mat = ReflectionHelper.findField(ItemSword.class, new String[] { "field_150933_b" });
  
  public final BlockSpike spike;
  
  public ItemSwordSpike(Block spike) {
    this((BlockSpike)spike);
  }
  
  public static Item.ToolMaterial getMaterial(BlockSpike spike) {
    ItemSword item = (ItemSword)spike.swordStack.getItem();
    try {
      return (Item.ToolMaterial)mat.get(item);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public ItemSwordSpike(BlockSpike spike) {
    super(getMaterial(spike));
    this.spike = spike;
    spike.itemSpike = (Item)this;
    this.maxStackSize = 64;
    setMaxDamage(0);
  }
  
  public int getSpriteNumber() {
    return 0;
  }
  
  public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
    Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
    if (block == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 0x7) < 1) {
      p_77648_7_ = 1;
    } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable((IBlockAccess)p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
      if (p_77648_7_ == 0)
        p_77648_5_--; 
      if (p_77648_7_ == 1)
        p_77648_5_++; 
      if (p_77648_7_ == 2)
        p_77648_6_--; 
      if (p_77648_7_ == 3)
        p_77648_6_++; 
      if (p_77648_7_ == 4)
        p_77648_4_--; 
      if (p_77648_7_ == 5)
        p_77648_4_++; 
    } 
    if (p_77648_1_.stackSize == 0)
      return false; 
    if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
      return false; 
    if (p_77648_5_ == 255 && this.spike.getMaterial().isSolid())
      return false; 
    if (p_77648_3_.canPlaceEntityOnSide((Block)this.spike, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, (Entity)p_77648_2_, p_77648_1_)) {
      int i1 = getMetadata(p_77648_1_.getItemDamage());
      int j1 = this.spike.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);
      if (placeBlockAt(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1)) {
        p_77648_3_.playSoundEffect((p_77648_4_ + 0.5F), (p_77648_5_ + 0.5F), (p_77648_6_ + 0.5F), this.spike.stepSound.func_150496_b(), (this.spike.stepSound.getVolume() + 1.0F) / 2.0F, this.spike.stepSound.getPitch() * 0.8F);
        p_77648_1_.stackSize--;
      } 
      return true;
    } 
    return false;
  }
  
  public String getUnlocalizedName(ItemStack p_77667_1_) {
    return this.spike.getUnlocalizedName();
  }
  
  public String getUnlocalizedName() {
    return this.spike.getUnlocalizedName();
  }
  
  @SideOnly(Side.CLIENT)
  public CreativeTabs getCreativeTab() {
    return this.spike.getCreativeTabToDisplayOn();
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
    this.spike.getSubBlocks(p_150895_1_, p_150895_2_, p_150895_3_);
  }
  
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
    if (!world.setBlock(x, y, z, (Block)this.spike, metadata, 3))
      return false; 
    if (world.getBlock(x, y, z) == this.spike) {
      this.spike.onBlockPlacedBy(world, x, y, z, (EntityLivingBase)player, stack);
      this.spike.onPostBlockPlaced(world, x, y, z, metadata);
    } 
    return true;
  }
  
  public String getString() {
    String s = this.spike.getUnlocalizedName().substring("tile.".length());
    s = s.replace("extrautils:", "");
    return s;
  }
  
  public boolean isItemTool(ItemStack p_77616_1_) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemSwordSpike.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */