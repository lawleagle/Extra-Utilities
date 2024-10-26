package com.rwtema.extrautils.item.scanner;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.network.packets.PacketTempChatMultiline;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemScanner extends Item {
  public static boolean scannerOut = false;
  
  public ItemScanner() {
    setTextureName("extrautils:scanner");
    setUnlocalizedName("extrautils:scanner");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setMaxStackSize(1);
  }
  
  public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    scannerOut = true;
    super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    if (world.isRemote)
      return true; 
    Block b = world.getBlock(x, y, z);
    PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("~~~~~Scan~~~~~"));
    PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Block name: " + b.getUnlocalizedName()));
    PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Block metadata: " + world.getBlockMetadata(x, y, z)));
    Object tile = world.getTileEntity(x, y, z);
    if (tile == null) {
      PacketTempChatMultiline.sendCached(player);
      return false;
    } 
    ForgeDirection dir = ForgeDirection.getOrientation(side);
    List<String> data = ScannerRegistry.getData(tile, dir, player);
    for (String aData : data)
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(aData)); 
    PacketTempChatMultiline.sendCached(player);
    return true;
  }
  
  public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer player, EntityLivingBase entity) {
    if (player.worldObj.isRemote)
      return true; 
    if (entity == null)
      return false; 
    List<String> data = ScannerRegistry.getData(entity, ForgeDirection.UP, player);
    for (String aData : data)
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(aData)); 
    PacketTempChatMultiline.sendCached(player);
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\scanner\ItemScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */