package com.rwtema.extrautils.crafting;

import com.mojang.authlib.GameProfile;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.item.ItemLawSword;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;

public class LawSwordCraftHandler {
  public static void init() {
    FMLCommonHandler.instance().bus().register(new LawSwordCraftHandler());
  }
  
  private final Random rand = (Random)XURandom.getInstance();
  
  private final String[] strings = new String[] { "I feel pretty", "Oh, so pretty", "I feel pretty", "and witty and bright!", "All you need is Wuv!" };
  
  private final String flowerType = "tulipPink";
  
  private final int pinkColor = 15892389;
  
  private final double pinkR = (this.pinkColor >> 16 & 0xFF) / 255.0D;
  
  private final double pinkG = (this.pinkColor >> 8 & 0xFF) / 255.0D;
  
  private final double pinkB = (this.pinkColor & 0xFF) / 255.0D;
  
  @SubscribeEvent
  public void event(TickEvent.PlayerTickEvent event) {
    if (event.phase == TickEvent.Phase.END)
      return; 
    if (event.side == Side.CLIENT) {
      for (int j = 1; j < 5; j++) {
        ItemStack a = event.player.getEquipmentInSlot(j);
        if (a == null || !a.hasTagCompound())
          return; 
        if (!(a.getItem() instanceof ItemArmor))
          return; 
        ItemArmor b = (ItemArmor)a.getItem();
        if (b.getColor(a) != this.pinkColor)
          return; 
        if (!this.strings[4 - j].equals(a.getDisplayName()))
          return; 
      } 
      AxisAlignedBB bb = event.player.boundingBox;
      for (int k = 0; k < 5; k++)
        event.player.worldObj.spawnParticle("reddust", bb.minX + XUHelper.rand.nextFloat() * (bb.maxX - bb.minX), bb.minY + XUHelper.rand.nextFloat() * (bb.maxY - bb.minY) * (1 + k) / 5.0D, bb.minZ + XUHelper.rand.nextFloat() * (bb.maxZ - bb.minZ), this.pinkR, this.pinkG, this.pinkB); 
      return;
    } 
    if (this.rand.nextInt(XUHelper.deObf ? 20 : 800) != 0)
      return; 
    if (isPlayerCreative(event))
      return; 
    if (!isPlayerSprinting(event))
      return; 
    if (isPlayerAlone())
      return; 
    for (int i = 0; i < 5; i++) {
      ItemStack a = event.player.getEquipmentInSlot(i);
      if (a == null)
        return; 
      if (i == 0) {
        if (!isPinkFlower(a))
          return; 
      } else {
        if (!(a.getItem() instanceof ItemArmor))
          return; 
        ItemArmor b = (ItemArmor)a.getItem();
        if (b.getColor(a) != this.pinkColor)
          return; 
      } 
      if (!this.strings[4 - i].equals(a.getDisplayName()))
        return; 
    } 
    handlePlayer(event.player);
  }
  
  public boolean isPinkFlower(ItemStack a) {
    if (a.getItem() != Item.getItemFromBlock((Block)BlockFlower.func_149857_e("tulipPink")))
      return false; 
    if (a.getItemDamage() != BlockFlower.func_149856_f("tulipPink"))
      return false; 
    return true;
  }
  
  public boolean isPlayerCreative(TickEvent.PlayerTickEvent event) {
    return event.player.capabilities.isCreativeMode;
  }
  
  public boolean isPlayerSprinting(TickEvent.PlayerTickEvent event) {
    return event.player.isSprinting();
  }
  
  public boolean isPlayerAlone() {
    return (!XUHelper.deObf && (MinecraftServer.getServer().getConfigurationManager()).playerEntityList.size() <= 1);
  }
  
  public boolean handlePlayer(EntityPlayer player) {
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      ItemStack item = player.inventory.getStackInSlot(i);
      if (item != null && item.getItem() == ExtraUtils.ethericSword && 
        player.inventory.decrStackSize(i, 1) != null) {
        boolean troll = shouldTroll(player);
        player.inventory.addItemStackToInventory(troll ? newRoll() : createNewStack());
        List<EntityPlayer> playerList = (MinecraftServer.getServer().getConfigurationManager()).playerEntityList;
        for (EntityPlayer p : playerList) {
          p.addChatComponentMessage((new ChatComponentText(player.getCommandSenderName())).appendText(" is the Prettiest Pink Princess"));
          if (troll)
            p.addChatComponentMessage((IChatComponent)new ChatComponentText("but sadly not a very lucky one")); 
        } 
        player.addChatComponentMessage((IChatComponent)new ChatComponentText("Thanks to your commitment to exercise and prettiness. You have been granted the greatest gift!"));
        return true;
      } 
    } 
    return false;
  }
  
  public static ItemStack createNewStack() {
    return ItemLawSword.newSword();
  }
  
  public static ItemStack newRoll() {
    return XUHelper.addLore(XUHelper.addEnchant((new ItemStack(Items.record_13, 1, 101)).setStackDisplayName("Rick Astley - Never gonna give you up!"), Enchantment.unbreaking, 1), new String[] { "Awesome music to exercise to.", "The greatest gift a pretty fairy could ask for.", "Were you expecting something else?" });
  }
  
  public static boolean shouldTroll(EntityPlayer player) {
    long seed = DimensionManager.getWorld(0).getSeed();
    int hash = (int)(seed ^ seed >>> 32L);
    hash = hash * 31 + ExtraUtils.versionHash;
    GameProfile gameProfile = player.getGameProfile();
    if (gameProfile == null || gameProfile.getId() == null || gameProfile.getName() == null)
      return true; 
    hash = hash * 31 + gameProfile.getId().hashCode();
    hash = hash * 31 + gameProfile.getName().hashCode();
    return (new Random(hash)).nextBoolean();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\LawSwordCraftHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */