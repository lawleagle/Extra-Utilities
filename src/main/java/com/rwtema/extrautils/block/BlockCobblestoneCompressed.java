package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ICreativeTabSorting;
import com.rwtema.extrautils.texture.TextureComprBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCobblestoneCompressed extends Block implements IBlockTooltip, ICreativeTabSorting {
  private IIcon[] icons = new IIcon[16];
  
  public BlockCobblestoneCompressed(Material par2Material) {
    super(par2Material);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(2.0F);
    setStepSound(soundTypeStone);
    setResistance(10.0F);
    setBlockName("extrautils:cobblestone_compressed");
    setBlockTextureName("extrautils:cobblestone_compressed");
  }
  
  public static String getOreDictName(int metadata) {
    if (metadata < 8)
      return "Cobblestone"; 
    if (metadata < 12)
      return "Dirt"; 
    if (metadata < 14)
      return "Gravel"; 
    return "Sand";
  }
  
  public static Block getBlock(int metadata) {
    if (metadata < 8)
      return Blocks.cobblestone; 
    if (metadata < 12)
      return Blocks.dirt; 
    if (metadata < 14)
      return Blocks.gravel; 
    return (Block)Blocks.sand;
  }
  
  public static boolean isBaseBlock(int metadata) {
    return (metadata == 0 || metadata == 8 || metadata == 12 || metadata == 14);
  }
  
  public static int getCompr(int metadata) {
    if (metadata < 8)
      return metadata; 
    if (metadata < 12)
      return metadata - 8; 
    if (metadata < 14)
      return metadata - 12; 
    return metadata - 14;
  }
  
  public boolean canDropFromExplosion(Explosion par1Explosion) {
    return (par1Explosion == null || !(par1Explosion.exploder instanceof net.minecraft.entity.projectile.EntityWitherSkull));
  }
  
  public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    if (!world.isRemote && canDropFromExplosion(explosion))
      super.onBlockExploded(world, x, y, z, explosion); 
  }
  
  public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5) {
    return ForgeHooks.blockStrength(getBlock(par2World.getBlockMetadata(par3, par4, par5)), par1EntityPlayer, par2World, par3, par4, par5);
  }
  
  public boolean canHarvestBlock(EntityPlayer player, int meta) {
    return ForgeHooks.canHarvestBlock(getBlock(meta), player, meta);
  }
  
  public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
    return getBlock(world.getBlockMetadata(x, y, z)).isFireSource(world, x, y, z, side);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    if (!(par1IIconRegister instanceof TextureMap))
      return; 
    for (int i = 0; i < 16; i++) {
      if (getBlock(i).getIcon(0, 0) == null)
        getBlock(i).registerBlockIcons(par1IIconRegister); 
      String icon_name = getBlock(i).getIcon(0, 0).getIconName();
      int c = getCompr(i);
      String t = "extrautils:" + icon_name + "_compressed_" + (c + 1);
      this.icons[i] = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry(t);
      if (this.icons[i] == null) {
        TextureComprBlock textureComprBlock = new TextureComprBlock(t, icon_name, 1 + c);
        this.icons[i] = (IIcon)textureComprBlock;
        ((TextureMap)par1IIconRegister).setTextureEntry(t, (TextureAtlasSprite)textureComprBlock);
      } 
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    return this.icons[par2];
  }
  
  public int damageDropped(int par1) {
    return par1;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int var4 = 0; var4 < 16; var4++)
      par3List.add(new ItemStack(par1, 1, var4)); 
  }
  
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
    int i = par1ItemStack.getItemDamage();
    par3List.add(String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf((int)Math.pow(9.0D, (getCompr(i) + 1))) }) + " " + getBlock(i).getLocalizedName());
  }
  
  public String getSortingName(ItemStack item) {
    ItemStack i = item.copy();
    i.setItemDamage(i.getItemDamage() - getCompr(i.getItemDamage()));
    return i.getDisplayName();
  }
  
  public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
    return (getCompr(world.getBlockMetadata(x, y, z)) < 6);
  }
  
  public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
    int metadata = world.getBlockMetadata(x, y, z);
    return getBlock(metadata).getExplosionResistance(par1Entity) * (int)Math.pow(1.5D, (1 + getCompr(metadata)));
  }
  
  public float getBlockHardness(World world, int x, int y, int z) {
    int metadata = world.getBlockMetadata(x, y, z);
    return (int)(getBlock(metadata).getBlockHardness(world, x, y, z) * Math.pow(2.5D, (1 + getCompr(metadata))));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockCobblestoneCompressed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */