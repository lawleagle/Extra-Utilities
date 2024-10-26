package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import com.rwtema.extrautils.tileentity.TileEntitySoundMuffler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSoundMuffler extends Block {
  private IIcon rainIcon;
  
  private IIcon rainOnIcon;
  
  public BlockSoundMuffler() {
    super(Material.cloth);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(0.8F);
    setStepSound(soundTypeCloth);
    setBlockName("extrautils:sound_muffler");
    setBlockTextureName("extrautils:sound_muffler");
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.blockIcon = par1IIconRegister.registerIcon("extrautils:sound_muffler");
    this.rainIcon = par1IIconRegister.registerIcon("extrautils:rain_muffler");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par2 == 1)
      return this.rainIcon; 
    return this.blockIcon;
  }
  
  public int damageDropped(int par1) {
    return par1;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(new ItemStack(par1, 1, 1));
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    if (metadata == 1)
      return (TileEntity)new TileEntityRainMuffler(); 
    return (TileEntity)new TileEntitySoundMuffler();
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
    if (world.getBlockMetadata(x, y, z) == 1 && 
      !XUHelper.isPlayerFake(player)) {
      NBTTagCompound tags = new NBTTagCompound();
      if (player.getEntityData().hasKey("PlayerPersisted")) {
        tags = player.getEntityData().getCompoundTag("PlayerPersisted");
      } else {
        player.getEntityData().setTag("PlayerPersisted", (NBTBase)tags);
      } 
      if (tags.hasKey("ExtraUtilities|Rain")) {
        if (tags.getBoolean("ExtraUtilities|Rain")) {
          tags.setBoolean("ExtraUtilities|Rain", false);
          if (world.isRemote)
            PacketTempChat.sendChat(player, "You remove the magic wool from your ears"); 
        } else {
          tags.setBoolean("ExtraUtilities|Rain", true);
          if (world.isRemote)
            PacketTempChat.sendChat(player, "You place some magic wool in your ears"); 
        } 
      } else {
        tags.setBoolean("ExtraUtilities|Rain", true);
        if (world.isRemote)
          PacketTempChat.sendChat(player, "You place some magic wool in your ears"); 
      } 
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockSoundMuffler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */