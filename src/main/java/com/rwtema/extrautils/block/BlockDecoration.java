package com.rwtema.extrautils.block;

import cofh.api.block.IBlockAppearance;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDecoration extends Block implements IBlockAppearance {
  public static boolean gettingConnectedTextures = false;
  
  public String[][] texture = new String[16][6];
  
  public boolean[][] ctexture = new boolean[16][6];
  
  public int[] light = new int[16];
  
  public float[] hardness = new float[16];
  
  public float[] resistance = new float[16];
  
  public boolean[] opaque = new boolean[16];
  
  public int[] opacity = new int[16];
  
  public boolean[] flipTopBottom = new boolean[16];
  
  public float[] enchantBonus = new float[16];
  
  public boolean solid;
  
  public boolean[] isSuperEnder = new boolean[16];
  
  public boolean[] isEnder = new boolean[16];
  
  private IIcon[][] icons = new IIcon[16][6];
  
  public String[] name = new String[16];
  
  private int numBlocks = 0;
  
  public int[] fireEncouragement;
  
  public int[] fireFlammability;
  
  public boolean[] fireSource;
  
  public BlockDecoration(boolean solid) {
    super(solid ? Material.rock : Material.glass);
    this.fireEncouragement = new int[16];
    this.fireFlammability = new int[16];
    this.fireSource = new boolean[16];
    this.solid = solid;
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(0.45F).setResistance(10.0F).setStepSound(soundTypeStone);
  }
  
  public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
    return ((this.isSuperEnder[world.getBlockMetadata(x, y, z)] && plant instanceof BlockEnderLily) || super.canSustainPlant(world, x, y, z, direction, plant));
  }
  
  public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
    if (world instanceof World && !((World)world).blockExists(x, y, z))
      return 0; 
    return this.opacity[world.getBlockMetadata(x, y, z)];
  }
  
  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    Block block = world.getBlock(x, y, z);
    if (block != null && block != this)
      return block.getLightValue(world, x, y, z); 
    return this.light[world.getBlockMetadata(x, y, z)];
  }
  
  public float getBlockHardness(World par1World, int par2, int par3, int par4) {
    if (par1World == null)
      return this.blockHardness; 
    return this.hardness[getMetadataSafe((IBlockAccess)par1World, par2, par3, par4)];
  }
  
  public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
    if (world == null)
      return getExplosionResistance(par1Entity); 
    return this.resistance[getMetadataSafe((IBlockAccess)world, x, y, z)] / 5.0F;
  }
  
  public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
    return true;
  }
  
  public boolean isOpaqueCube() {
    return this.solid;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return this.solid ? super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) : ((!par1IBlockAccess.getBlock(par2, par3, par4).isOpaqueCube() && par1IBlockAccess.getBlock(par2, par3, par4) != this));
  }
  
  public boolean renderAsNormalBlock() {
    return this.solid;
  }
  
  public void addBlock(int id, String defaultname, String texture) {
    addBlock(id, defaultname, texture, false, true);
  }
  
  public void addBlock(int id, String defaultname, String texture, boolean connectedTexture, boolean opaque) {
    if (id >= 0 && id < 16) {
      assert this.name[id] != null;
      this.name[id] = texture;
      for (int side = 0; side < 6; side++) {
        this.texture[id][side] = texture;
        this.ctexture[id][side] = connectedTexture;
      } 
      this.hardness[id] = this.blockHardness;
      this.resistance[id] = this.blockHardness * 5.0F;
      this.opaque[id] = opaque;
      this.opacity[id] = this.solid ? 255 : 0;
      this.enchantBonus[id] = 0.0F;
      this.isEnder[id] = false;
      this.isSuperEnder[id] = false;
    } 
  }
  
  public float getEnchantPowerBonus(World world, int x, int y, int z) {
    return this.enchantBonus[getMetadataSafe((IBlockAccess)world, x, y, z)];
  }
  
  public int getMetadataSafe(IBlockAccess world, int x, int y, int z) {
    return world.getBlockMetadata(x, y, z);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par1 <= 1 && this.flipTopBottom[par2 & 0xF] && this.icons[par2 & 0xF][par1] instanceof IconConnectedTexture)
      return new IconConnectedTextureFlipped((IconConnectedTexture)this.icons[par2 & 0xF][par1]); 
    return this.icons[par2 & 0xF][par1];
  }
  
  public int damageDropped(int par1) {
    return par1 & 0xF;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    for (int i = 0; i < 16; i++) {
      if (this.name[i] != null)
        par3List.add(new ItemStack(par1, 1, i)); 
    } 
  }
  
  public int getRenderType() {
    return ExtraUtilsProxy.connectedTextureID;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    for (int i = 0; i < 16; i++) {
      for (int side = 0; side < 6; side++) {
        if (this.texture[i][side] != null && !this.texture[i][side].equals(""))
          if (this.ctexture[i][side]) {
            this.icons[i][side] = new IconConnectedTexture(par1IIconRegister, this.texture[i][side]);
          } else {
            this.icons[i][side] = par1IIconRegister.registerIcon(this.texture[i][side]);
          }  
      } 
    } 
  }
  
  public Block getVisualBlock(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return this;
  }
  
  public int getVisualMeta(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return world.getBlockMetadata(x, y, z);
  }
  
  public boolean supportsVisualConnections() {
    return true;
  }
  
  public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
    return this.fireSource[getMetadataSafe((IBlockAccess)world, x, y, z)];
  }
  
  public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
    return this.fireEncouragement[getMetadataSafe(world, x, y, z)];
  }
  
  public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
    return this.fireFlammability[getMetadataSafe(world, x, y, z)];
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockDecoration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */