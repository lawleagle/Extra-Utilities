package com.rwtema.extrautils.item;

import com.google.common.collect.Multimap;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.crafting.LawSwordCraftHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collection;
import java.util.List;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemLawSword extends ItemSword {
  public static Item.ToolMaterial material = EnumHelper.addToolMaterial("OpeOpeNoMi", 3, 4096, 8.0F, 3.0F, 10);

  static {
    LawSwordCraftHandler.init();
  }

  public ItemLawSword() {
    super(material);
    this.maxStackSize = 1;
    setUnlocalizedName("extrautils:lawSword");
  }

  public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
    p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
    return p_77659_1_;
  }

  public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
    return true;
  }

  public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    if (entity.canAttackWithItem()) {
      Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers();
      Collection<AttributeModifier> gsd = multimap.get(godSlayingDamage.getAttributeUnlocalizedName());
      if (gsd != null)
        for (AttributeModifier t : gsd)
          attackEntity(player, entity, t.getAmount(), (DamageSource)new DamageSourceEvil(player, true));
      gsd = multimap.get(armorPiercingDamage.getAttributeUnlocalizedName());
      if (gsd != null)
        for (AttributeModifier gs : gsd)
          attackEntity(player, entity, gs.getAmount(), (DamageSource)new DamageSourceEvil(player, false));
    }
    return false;
  }

  public void attackEntity(EntityPlayer player, Entity entity, double f, DamageSource damageSource) {
    float f1 = 0.0F;
    if (entity instanceof EntityLivingBase)
      f1 = EnchantmentHelper.getEnchantmentModifierLiving((EntityLivingBase)player, (EntityLivingBase)entity);
    if (f > 0.0D || f1 > 0.0F) {
      boolean flag = (player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && entity instanceof EntityLivingBase);
      if (flag && f > 0.0D)
        f *= 1.5D;
      f += f1;
      boolean flag2 = entity.attackEntityFrom(damageSource, (float)f);
      if (flag2) {
        if (flag)
          player.onCriticalHit(entity);
        if (f1 > 0.0F)
          player.onEnchantmentCritical(entity);
        player.setLastAttacker(entity);
        if (entity instanceof EntityLivingBase)
          EnchantmentHelper.func_151384_a((EntityLivingBase)entity, (Entity)player);
        EnchantmentHelper.func_151385_b((EntityLivingBase)player, entity);
      }
    }
  }

  public Multimap getAttributeModifiers(ItemStack stack) {
    Multimap multimap = getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 4.0D, 0));
    multimap.put(godSlayingDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 2.0D, 0));
    multimap.put(armorPiercingDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 4.0D, 0));
    return multimap;
  }

  public static BaseAttribute godSlayingDamage = (BaseAttribute)new RangedAttribute("extrautils.godSlayingAttackDamage", 2.0D, 0.0D, Double.MAX_VALUE);

  public static BaseAttribute armorPiercingDamage = (BaseAttribute)new RangedAttribute("extrautils.armorPiercingAttackDamage", 2.0D, 0.0D, Double.MAX_VALUE);

  public static class DamageSourceEvil extends EntityDamageSource {
    public DamageSourceEvil(EntityPlayer player, boolean creative) {
      super("player", (Entity)player);
      setDamageBypassesArmor();
      setDamageIsAbsolute();
      if (creative)
        setDamageAllowedInCreativeMode();
    }
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
    super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    p_77624_3_.add(("" + StatCollector.translateToLocal(getUnlocalizedNameInefficiently(p_77624_1_) + ".tooltip")).trim());
  }

  public static ItemStack newSword() {
    return new ItemStack(ExtraUtils.lawSword);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemLawSword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
