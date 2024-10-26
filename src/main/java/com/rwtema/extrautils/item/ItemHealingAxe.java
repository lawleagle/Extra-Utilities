package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemHealingAxe extends ItemAxe implements IItemMultiTransparency {
  private IIcon[] icons;
  
  public ItemHealingAxe() {
    super(Item.ToolMaterial.EMERALD);
    this.maxStackSize = 1;
    setMaxDamage(1561);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:defoliageAxe");
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return false;
  }
  
  public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
    return true;
  }
  
  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    float k = 2.0F;
    player.addExhaustion(10.0F);
    if (entity instanceof EntityLiving) {
      if (entity.worldObj.isRemote)
        for (int i = 0; i < 5; i++) {
          int j = Potion.heal.getLiquidColor();
          float r = (j >> 16 & 0xFF) / 255.0F;
          float g = (j >> 8 & 0xFF) / 255.0F;
          float b = (j & 0xFF) / 255.0F;
          entity.worldObj.spawnParticle("mobSpell", entity.posX + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width, entity.posY + entity.worldObj.rand.nextDouble() * entity.height - entity.yOffset, entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width, r, g, b);
        }  
      if (k > 0.0F) {
        EntityLiving entLivin = (EntityLiving)entity;
        k *= 2.0F;
        if (((EntityLiving)entity).isEntityUndead() && entity.isEntityAlive()) {
          if (entity instanceof EntityZombie && (
            (EntityZombie)entity).isVillager()) {
            if (!entity.worldObj.isRemote) {
              entity.getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
              entity.worldObj.setEntityState(entity, (byte)16);
            } 
            return true;
          } 
          entLivin.attackEntityFrom(DamageSource.causePlayerDamage(player), k * 4.0F);
          return true;
        } 
        if (entity.isEntityAlive() && ((EntityLiving)entity).getHealth() > 0.0F && 
          entLivin.getHealth() < entLivin.getHealth()) {
          if (!entLivin.worldObj.isRemote)
            ((EntityLiving)entity).heal(k); 
          return true;
        } 
      } 
    } 
    return true;
  }
  
  public int getDamageVsEntity(Entity par1Entity) {
    return 0;
  }
  
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }
  
  public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    if (par5 && 
      par3Entity instanceof EntityPlayer && 
      par2World.getTotalWorldTime() % 40L == 0L)
      ((EntityPlayer)par3Entity).getFoodStats().addStats(1, 0.2F); 
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


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemHealingAxe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */