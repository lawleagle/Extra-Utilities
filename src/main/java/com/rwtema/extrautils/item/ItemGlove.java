package com.rwtema.extrautils.item;

import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemGlove extends ItemSword {
  public static final int INVALID_METADATA = 32767;
  
  static Item.ToolMaterial materialWool = EnumHelper.addToolMaterial("wool", 0, 0, 0.0F, 0.0F, 0);
  
  IIcon glove1;
  
  IIcon glove2;
  
  static {
    materialWool.setRepairItem(new ItemStack(Blocks.wool));
  }
  
  public ItemGlove() {
    super(materialWool);
    this.freezeUUID = UUID.fromString("EC21E5A7-1E80-4913-b55C-6ABD8EC8EA90");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:glove");
    setTextureName("extrautils:glove");
    setHasSubtypes(false);
    setMaxStackSize(1);
  }
  
  public boolean hasCustomEntity(ItemStack stack) {
    return (stack != null && stack.getItemDamage() == 32767);
  }
  
  public Entity createEntity(World world, Entity location, ItemStack itemstack) {
    if (itemstack.getItemDamage() == 32767)
      location.setDead(); 
    return null;
  }
  
  public void registerIcons(IIconRegister register) {
    this.glove1 = register.registerIcon("extrautils:glove_1");
    this.glove2 = register.registerIcon("extrautils:glove_2");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
    return (p_77618_2_ == 0) ? this.glove1 : this.glove2;
  }
  
  @SideOnly(Side.CLIENT)
  public int getColorFromItemStack(ItemStack p_82790_1_, int pass) {
    int dmg = p_82790_1_.getItemDamage();
    return XUHelper.dyeCols[getColIndex(pass, dmg)];
  }
  
  static int genericDmg = 0;
  
  public static int getColIndex(int pass, int dmg) {
    if (isInvalidDamage(dmg))
      dmg = genericDmg; 
    if (pass == 0)
      return dmg & 0xF; 
    return dmg >> 4 & 0xF;
  }
  
  public static boolean isInvalidDamage(int dmg) {
    return (dmg < 0 || dmg > 255);
  }
  
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses() {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public int getRenderPasses(int metadata) {
    return 2;
  }
  
  public String getUnlocalizedName(ItemStack p_77667_1_) {
    if (isInvalidDamage(p_77667_1_.getItemDamage()))
      return super.getUnlocalizedName(p_77667_1_) + ".english"; 
    return super.getUnlocalizedName(p_77667_1_);
  }
  
  @SubscribeEvent
  public void repair(AnvilUpdateEvent event) {}
  
  @SubscribeEvent
  public void attack(PlayerInteractEvent event) {
    if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
      return; 
    EntityPlayer player = event.entityPlayer;
    if (event.world == null || event.world.isRemote)
      return; 
    if (player == null || !(player instanceof EntityPlayerMP))
      return; 
    ItemStack heldItem = player.getHeldItem();
    if (heldItem == null || heldItem.getItem() != this)
      return; 
    if (heldItem.getItemDamage() == 32767) {
      heldItem.stackSize = 0;
      return;
    } 
    int i = player.inventory.currentItem;
    if (i >= 9 || i < 0)
      return; 
    event.setCanceled(true);
    ItemStack item = heldItem.copy();
    heldItem.setItemDamage(32767);
    player.inventory.setInventorySlotContents(i, null);
    int x = event.x, y = event.y, z = event.z, side = event.face;
    try {
      PlayerInteractEvent e = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, x, y, z, side, player.worldObj);
      boolean result = (!e.isCanceled() && event.useBlock != Event.Result.DENY);
      if (result) {
        Block block = event.world.getBlock(x, y, z);
        block.onBlockClicked(event.world, x, y, z, player);
      } 
    } catch (Exception err) {
      for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
        ItemStack stackInSlot = player.inventory.getStackInSlot(i);
        if (stackInSlot != null && stackInSlot.getItem() == this)
          if (stackInSlot.getItemDamage() == 32767)
            player.inventory.setInventorySlotContents(j, null);  
      } 
      if (player.inventory.getStackInSlot(i) == null) {
        player.inventory.setInventorySlotContents(i, item);
      } else if (!player.inventory.addItemStackToInventory(item)) {
        player.dropPlayerItemWithRandomChoice(item, false);
      } 
      throw Throwables.propagate(err);
    } 
    ((EntityPlayerMP)player).playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange(x, y, z, event.world));
    ItemStack newItem = player.inventory.getStackInSlot(i);
    player.inventory.setInventorySlotContents(i, item);
    if (newItem != null && !player.inventory.addItemStackToInventory(newItem.copy()))
      player.dropPlayerItemWithRandomChoice(newItem.copy(), false); 
    ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
  }
  
  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    if (player == null)
      return false; 
    ItemStack heldItem = player.getHeldItem();
    if (heldItem == null || heldItem.getItem() != this)
      return false; 
    if (ExtraUtilsMod.proxy.isAltSneaking()) {
      if (world.isRemote) {
        ExtraUtilsMod.proxy.sendAltUsePacket(x, y, z, side, stack, hitX, hitY, hitZ);
        return true;
      } 
      return false;
    } 
    if (heldItem.getItemDamage() == 32767) {
      heldItem.stackSize = 0;
      return true;
    } 
    int i = player.inventory.currentItem;
    if (i >= 9 || i < 0)
      return false; 
    ItemStack item = heldItem.copy();
    heldItem.setItemDamage(32767);
    player.inventory.setInventorySlotContents(i, null);
    try {
      PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, player.worldObj);
      boolean result = (!event.isCanceled() && event.useBlock != Event.Result.DENY);
      if (result) {
        Block block = world.getBlock(x, y, z);
        block.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
      } 
    } catch (Exception err) {
      for (int j = 0; j < player.inventory.getSizeInventory(); j++) {
        ItemStack stackInSlot = player.inventory.getStackInSlot(i);
        if (stackInSlot != null && stackInSlot.getItem() == this)
          if (stackInSlot.getItemDamage() == 32767)
            player.inventory.setInventorySlotContents(j, null);  
      } 
      if (player.inventory.getStackInSlot(i) == null) {
        player.inventory.setInventorySlotContents(i, item);
      } else if (!player.inventory.addItemStackToInventory(item)) {
        player.dropPlayerItemWithRandomChoice(item, false);
      } 
      throw Throwables.propagate(err);
    } 
    ItemStack newItem = player.inventory.getStackInSlot(i);
    player.inventory.setInventorySlotContents(i, item);
    if (newItem != null && !player.inventory.addItemStackToInventory(newItem.copy()))
      player.dropPlayerItemWithRandomChoice(newItem.copy(), false); 
    if (player.worldObj.isRemote) {
      ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, item, hitX, hitY, hitZ);
    } else {
      if (player instanceof EntityPlayerMP)
        ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player); 
      player.openContainer.detectAndSendChanges();
    } 
    return true;
  }
  
  public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
    return true;
  }
  
  public static BaseAttribute woolProtection = (BaseAttribute)new RangedAttribute("extrautils.freezeProtection", 0.0D, -1.7976931348623157E308D, Double.MAX_VALUE);
  
  UUID freezeUUID;
  
  public Multimap getAttributeModifiers(ItemStack stack) {
    HashMultimap hashMultimap = HashMultimap.create();
    hashMultimap.put(woolProtection.getAttributeUnlocalizedName(), new AttributeModifier(this.freezeUUID, "Weapon modifier", 0.001D, 0));
    return (Multimap)hashMultimap;
  }
  
  public EnumAction getItemUseAction(ItemStack p_77661_1_) {
    return EnumAction.none;
  }
  
  public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
    return p_77659_1_;
  }
  
  public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
    return true;
  }
  
  public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
    return true;
  }
  
  public int getMaxItemUseDuration(ItemStack p_77626_1_) {
    return 0;
  }
  
  public int getItemEnchantability() {
    return 2;
  }
  
  public boolean isItemTool(ItemStack p_77616_1_) {
    return true;
  }
  
  public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemGlove.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */