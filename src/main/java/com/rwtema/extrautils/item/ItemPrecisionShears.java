package com.rwtema.extrautils.item;

import cofh.api.block.IDismantleable;
import com.rwtema.extrautils.EventHandlerEntityItemStealer;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.network.NetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemPrecisionShears extends ItemShears implements IItemMultiTransparency {
  public static final Item[] toolsToMimic = new Item[] { Items.stone_pickaxe, Items.stone_axe, Items.stone_shovel, Items.stone_sword, Items.stone_hoe, (Item)Items.shears };
  
  public static final ItemStack[] toolStacks = new ItemStack[toolsToMimic.length];
  
  static {
    for (int i = 0; i < toolsToMimic.length; i++)
      toolStacks[i] = new ItemStack(toolsToMimic[i]); 
  }
  
  public static int getCooldown(ItemStack stack) {
    int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
    if (i < 0)
      i = 0; 
    if (i >= COOLDOWN.length)
      i = COOLDOWN.length - 1; 
    return COOLDOWN[i];
  }
  
  public static final int[] COOLDOWN = new int[] { 20, 16, 12, 8, 4, 0 };
  
  public Random rand = (Random)XURandom.getInstance();
  
  private IIcon[] icons;
  
  public ItemPrecisionShears() {
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:shears");
    setMaxStackSize(1);
    setMaxDamage(1024);
  }
  
  public int cofh_canEnchantApply(ItemStack stack, Enchantment ench) {
    return (ench.type == EnumEnchantmentType.digger) ? 1 : -1;
  }
  
  public int getItemEnchantability() {
    return Items.iron_pickaxe.getItemEnchantability();
  }
  
  public boolean isItemTool(ItemStack p_77616_1_) {
    return (p_77616_1_.stackSize == 1);
  }
  
  public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
    World worldObj = player.worldObj;
    if (worldObj.isRemote)
      return false; 
    Block block = worldObj.getBlock(x, y, z);
    int meta = worldObj.getBlockMetadata(x, y, z);
    worldObj.playAuxSFXAtEntity(player, 2001, x, y, z, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(x, y, z) << 12));
    boolean flag1 = block.canHarvestBlock(player, meta);
    if (itemstack != null) {
      itemstack.func_150999_a(worldObj, block, x, y, z, player);
      if (itemstack.stackSize == 0)
        player.destroyCurrentEquippedItem(); 
    } 
    List<EntityItem> extraDrops = null;
    List<EntityItem> baseCapturedDrops = null;
    EventHandlerEntityItemStealer.startCapture();
    if (block instanceof IDismantleable && ((IDismantleable)block).canDismantle(player, worldObj, x, y, z)) {
      ((IDismantleable)block).dismantleBlock(player, worldObj, x, y, z, false);
    } else {
      block.onBlockHarvested(worldObj, x, y, z, meta, player);
      if (block.removedByPlayer(worldObj, player, x, y, z, true)) {
        block.onBlockDestroyedByPlayer(worldObj, x, y, z, meta);
        if (flag1 || player.capabilities.isCreativeMode) {
          extraDrops = EventHandlerEntityItemStealer.getCapturedEntities();
          EventHandlerEntityItemStealer.startCapture();
          block.harvestBlock(worldObj, player, x, y, z, meta);
          baseCapturedDrops = EventHandlerEntityItemStealer.getCapturedEntities();
        } 
      } 
    } 
    EventHandlerEntityItemStealer.stopCapture();
    boolean added = false;
    if (baseCapturedDrops == null)
      baseCapturedDrops = EventHandlerEntityItemStealer.getCapturedEntities(); 
    if (extraDrops != null)
      baseCapturedDrops.addAll(extraDrops); 
    for (EntityItem j : baseCapturedDrops) {
      if (player.inventory.addItemStackToInventory(j.getEntityItem())) {
        added = true;
        NetworkHandler.sendParticle(worldObj, "reddust", j.posX, j.posY, j.posZ, 0.5D + this.rand.nextDouble() * 0.15D, 0.35D, 0.65D + this.rand.nextDouble() * 0.3D, false);
      } 
      if (j.getEntityItem() != null && (j.getEntityItem()).stackSize > 0)
        worldObj.spawnEntityInWorld((Entity)new EntityItem(j.worldObj, j.posX, j.posY, j.posZ, j.getEntityItem())); 
    } 
    if (added) {
      for (int i = 0; i < 10; i++)
        NetworkHandler.sendParticle(worldObj, "reddust", x + this.rand.nextDouble(), y + this.rand.nextDouble(), z + this.rand.nextDouble(), 0.5D + this.rand.nextDouble() * 0.15D, 0.35D, 0.65D + this.rand.nextDouble() * 0.3D, false); 
      ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
    } 
    return true;
  }
  
  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
    if (!player.isSneaking())
      return false; 
    if (!check(par1ItemStack, world))
      return true; 
    if (world.isAirBlock(x, y, z))
      return false; 
    Block block = world.getBlock(x, y, z);
    int meta = world.getBlockMetadata(x, y, z);
    if (block.getBlockHardness(world, x, y, z) < 0.0F && (
      !(block instanceof IDismantleable) || !((IDismantleable)block).canDismantle(player, world, x, y, z)))
      return false; 
    if (block.canHarvestBlock(player, meta)) {
      player.swingItem();
      if (world.isRemote || !(player instanceof EntityPlayerMP))
        return true; 
      if (!check(par1ItemStack, world))
        return true; 
      if (!world.isAirBlock(x, y, z) && 
        block.getBlockHardness(world, x, y, z) >= 0.0F)
        ((EntityPlayerMP)player).theItemInWorldManager.tryHarvestBlock(x, y, z); 
      return true;
    } 
    return false;
  }
  
  private void collectItems(World world, EntityPlayer player, double x, double y, double z, List before, List after) {
    Iterator<EntityItem> iter = after.iterator();
    boolean added = false;
    while (iter.hasNext()) {
      EntityItem j = iter.next();
      if (j.getClass() == EntityItem.class && !before.contains(j) && 
        player.inventory.addItemStackToInventory(j.getEntityItem())) {
        NetworkHandler.sendParticle(world, "reddust", j.posX, j.posY, j.posZ, 0.5D + this.rand.nextDouble() * 0.15D, 0.35D, 0.65D + this.rand.nextDouble() * 0.3D, false);
        added = true;
        if (j.getEntityItem() == null || (j.getEntityItem()).stackSize == 0)
          j.setDead(); 
      } 
    } 
    if (added) {
      for (int i = 0; i < 10; i++)
        NetworkHandler.sendParticle(world, "reddust", x + this.rand.nextDouble(), y + this.rand.nextDouble(), z + this.rand.nextDouble(), 0.5D + this.rand.nextDouble() * 0.15D, 0.35D, 0.65D + this.rand.nextDouble() * 0.3D, false); 
      ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
    } 
  }
  
  public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity) {
    AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ).offset(0.5D, 0.5D, 0.5D).expand(3.0D, 3.0D, 3.0D);
    List items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
    boolean sheared = super.itemInteractionForEntity(itemstack, player, entity);
    if (sheared)
      collectItems(player.worldObj, player, entity.posX - 0.5D, entity.posY - 0.5D, entity.posZ - 0.5D, items, player.worldObj.getEntitiesWithinAABB(EntityItem.class, aabb)); 
    return sheared;
  }
  
  public boolean canHarvestBlock(Block par1Block, ItemStack item) {
    for (Item tool : toolsToMimic) {
      if (tool.canHarvestBlock(par1Block, new ItemStack(tool)))
        return true; 
    } 
    return false;
  }
  
  public float func_150893_a(ItemStack stack, Block block) {
    for (ItemStack tool : toolStacks) {
      if (ForgeHooks.isToolEffective(tool, block, 0))
        return 4.0F; 
    } 
    return super.func_150893_a(stack, block);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[2];
    this.itemIcon = this.icons[0] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5));
    this.icons[1] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5) + "1");
  }
  
  @SideOnly(Side.CLIENT)
  public int numIcons(ItemStack item) {
    if ((Minecraft.getMinecraft()).thePlayer != null && (Minecraft.getMinecraft()).thePlayer.worldObj != null && 
      !check(item, (Minecraft.getMinecraft()).thePlayer.worldObj))
      return 1; 
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
  
  public boolean onBlockDestroyed(ItemStack itemstack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
    itemstack.damageItem(1, par7EntityLivingBase);
    NBTTagCompound tag = itemstack.getTagCompound();
    if (tag == null)
      tag = new NBTTagCompound(); 
    tag.setInteger("dim", par2World.provider.dimensionId);
    tag.setLong("time", par2World.getTotalWorldTime());
    itemstack.setTagCompound(tag);
    return true;
  }
  
  public boolean check(ItemStack itemstack, World world) {
    if (!itemstack.hasTagCompound())
      return true; 
    if (!itemstack.getTagCompound().hasKey("dim") && !itemstack.getTagCompound().hasKey("time"))
      return true; 
    long totalWorldTime = world.getTotalWorldTime();
    long time = itemstack.getTagCompound().getLong("time") + getCooldown(itemstack);
    if (itemstack.getTagCompound().getInteger("dim") != world.provider.dimensionId || time < totalWorldTime) {
      if (!world.isRemote) {
        itemstack.getTagCompound().removeTag("dim");
        itemstack.getTagCompound().removeTag("time");
        if (itemstack.getTagCompound().hasNoTags())
          itemstack.setTagCompound(null); 
      } 
      return true;
    } 
    return false;
  }
  
  public void onUpdate(ItemStack itemstack, World par2World, Entity par3Entity, int par4, boolean par5) {
    check(itemstack, par2World);
  }
  
  public Entity createEntity(World world, Entity location, ItemStack itemstack) {
    if (itemstack.hasTagCompound()) {
      itemstack.getTagCompound().removeTag("dim");
      itemstack.getTagCompound().removeTag("time");
      if (itemstack.getTagCompound().hasNoTags())
        itemstack.setTagCompound(null); 
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemPrecisionShears.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */