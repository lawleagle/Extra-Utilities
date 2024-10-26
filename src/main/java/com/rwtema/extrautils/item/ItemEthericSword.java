package com.rwtema.extrautils.item;

import com.google.common.collect.Multimap;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemEthericSword extends ItemSword implements IItemMultiTransparency {
  private double weaponDamage;
  
  private IIcon[] icons;
  
  public ItemEthericSword() {
    super(Item.ToolMaterial.IRON);
    this.maxStackSize = 1;
    setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses());
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:ethericsword");
    this.weaponDamage = 8.0D;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return false;
  }
  
  public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
    if (par2Block == Blocks.web)
      return 15.0F; 
    Material var3 = par2Block.getMaterial();
    return (var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.plants) ? 1.0F : 1.5F;
  }
  
  public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
    return true;
  }
  
  public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving) {
    if (par3.getBlockHardness(par2World, par4, par5, par6) != 0.0D)
      par1ItemStack.damageItem(1, par7EntityLiving); 
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }
  
  public EnumAction getItemUseAction(ItemStack par1ItemStack) {
    return EnumAction.block;
  }
  
  public int getMaxItemUseDuration(ItemStack par1ItemStack) {
    return 72000;
  }
  
  public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
    if (ExtraUtils.lawSwordEnabled && XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player))
      return ItemLawSword.newSword(); 
    player.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
    return par1ItemStack;
  }
  
  public boolean canHarvestBlock(Block par1Block, ItemStack item) {
    return (par1Block == Blocks.web);
  }
  
  public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    return false;
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
  
  public Multimap getItemAttributeModifiers() {
    Multimap multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", this.weaponDamage, 0));
    return multimap;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemEthericSword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */