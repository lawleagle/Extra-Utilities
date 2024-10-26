package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTradingPost extends BlockContainer {
  private IIcon[] icons = new IIcon[3];
  
  public BlockTradingPost() {
    super(Material.wood);
    setBlockName("extrautils:trading_post");
    setBlockTextureName("extrautils:trading_post");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setResistance(10.0F).setStepSound(soundTypeWood);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.icons[0] = par1IIconRegister.registerIcon("planks");
    this.icons[1] = par1IIconRegister.registerIcon("extrautils:trading_post_top");
    this.icons[2] = par1IIconRegister.registerIcon("extrautils:trading_post_side");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par1 <= 1)
      return this.icons[par1]; 
    return this.icons[2];
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
    if (!world.isRemote) {
      if (world.getTileEntity(x, y, z) instanceof TileEntityTradingPost) {
        XUPacketBase packet = ((TileEntityTradingPost)world.getTileEntity(x, y, z)).getTradePacket(player);
        if (packet != null) {
          NetworkHandler.sendPacketToPlayer(packet, player);
        } else {
          PacketTempChat.sendChat(player, "No villagers found in range");
        } 
        return true;
      } 
    } else {
      return true;
    } 
    return false;
  }
  
  public TileEntity createNewTileEntity(World var1, int var2) {
    return (TileEntity)new TileEntityTradingPost();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockTradingPost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */