package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.rwtema.extrautils.item.ItemBlockMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBlockMultiPart extends ItemBlockMetadata {
  public ItemBlockMultiPart(Block par1) {
    super(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public boolean func_150936_a(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
    return (tryPlaceMultiPart(par1World, (new BlockCoord(par2, par3, par4)).offset(par5), par7ItemStack, par5, false) || super.func_150936_a(par1World, par2, par3, par4, par5, par6EntityPlayer, par7ItemStack));
  }
  
  public TMultiPart createMultiPart(World world, BlockCoord pos, ItemStack item, int side) {
    return null;
  }
  
  public boolean tryPlaceMultiPart(World world, BlockCoord pos, ItemStack item, int side, boolean doPlace) {
    TileMultipart tile = TileMultipart.getOrConvertTile(world, pos);
    if (tile == null)
      return false; 
    TMultiPart part = createMultiPart(world, pos, item, side);
    if (part == null)
      return false; 
    if (tile.canAddPart(part)) {
      if (doPlace)
        TileMultipart.addPart(world, pos, part); 
      return true;
    } 
    return false;
  }
  
  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    Block block = par3World.getBlock(par4, par5, par6);
    if (par8 != 0.0F && par9 != 0.0F && par10 != 0.0F && par8 != 1.0F && par9 != 1.0F && par10 != 1.0F) {
      BlockCoord pos = new BlockCoord(par4, par5, par6);
      if (tryPlaceMultiPart(par3World, pos, par1ItemStack, par7, !par3World.isRemote)) {
        par3World.playSoundEffect((pos.x + 0.5F), (pos.y + 0.5F), (pos.z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        par1ItemStack.stackSize--;
        return true;
      } 
    } 
    if ((block != Blocks.snow || (par3World.getBlockMetadata(par4, par5, par6) & 0x7) >= 1) && 
      block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && block.isReplaceable((IBlockAccess)par3World, par4, par5, par6)) {
      BlockCoord pos = (new BlockCoord(par4, par5, par6)).offset(par7);
      if (tryPlaceMultiPart(par3World, pos, par1ItemStack, par7, !par3World.isRemote)) {
        par3World.playSoundEffect((pos.x + 0.5F), (pos.y + 0.5F), (pos.z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        par1ItemStack.stackSize--;
        return true;
      } 
    } 
    return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\ItemBlockMultiPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */