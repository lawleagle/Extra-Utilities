package com.rwtema.extrautils.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemAngelBlock extends ItemBlock {
  public ItemAngelBlock(Block par1) {
    super(par1);
  }
  
  public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
    if (world.isRemote)
      return item; 
    int x = (int)Math.floor(player.posX);
    int y = (int)Math.floor(player.posY) + 1;
    int z = (int)Math.floor(player.posZ);
    Vec3 look = player.getLookVec();
    double max = Math.max(Math.max(Math.abs(look.xCoord), Math.abs(look.yCoord)), Math.abs(look.zCoord));
    if (look.yCoord == max) {
      y = (int)(Math.ceil(player.boundingBox.maxY) + 1.0D);
    } else if (-look.yCoord == max) {
      y = (int)(Math.floor(player.boundingBox.minY) - 1.0D);
    } else if (look.xCoord == max) {
      x = (int)(Math.floor(player.boundingBox.maxX) + 1.0D);
    } else if (-look.xCoord == max) {
      x = (int)(Math.floor(player.boundingBox.minX) - 1.0D);
    } else if (look.zCoord == max) {
      z = (int)(Math.floor(player.boundingBox.maxZ) + 1.0D);
    } else if (-look.zCoord == max) {
      z = (int)(Math.floor(player.boundingBox.minZ) - 1.0D);
    } 
    if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, 0, (Entity)player, item))
      item.tryPlaceItemIntoWorld(player, world, x, y, z, 0, 0.0F, 0.0F, 0.0F); 
    return item;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemAngelBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */