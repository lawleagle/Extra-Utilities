package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.block.BlockEnderLily;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemBlockEnderLily extends ItemBlock implements IPlantable {
  BlockEnderLily enderLily;
  
  public ItemBlockEnderLily(Block par1) {
    super(par1);
    this.enderLily = (BlockEnderLily)par1;
  }
  
  public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
    return EnumPlantType.Cave;
  }
  
  public Block getPlant(IBlockAccess world, int x, int y, int z) {
    return this.field_150939_a;
  }
  
  public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
    return 0;
  }
  
  private static final ChunkCoordinates zero = new ChunkCoordinates(0, 0, 0);
  
  public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    Block block = world.getBlock(x, y, z);
    if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 0x7) < 1) {
      side = 1;
    } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable((IBlockAccess)world, x, y, z)) {
      if (side == 0)
        y--; 
      if (side == 1)
        y++; 
      if (side == 2)
        z--; 
      if (side == 3)
        z++; 
      if (side == 4)
        x--; 
      if (side == 5)
        x++; 
    } 
    int my = y + 8;
    while (y < my && this.enderLily.isFluid(world, x, y, z))
      y++; 
    boolean b = world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, (Entity)player, itemstack);
    if (itemstack.stackSize == 0)
      return false; 
    if (!player.canPlayerEdit(x, y, z, side, itemstack))
      return false; 
    if (y == 255 && this.field_150939_a.getMaterial().isSolid())
      return false; 
    if (b) {
      int i1 = getMetadata(itemstack.getItemDamage());
      int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, i1);
      if (placeBlockAt(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, j1)) {
        world.playSoundEffect((x + 0.5F), (y + 0.5F), (z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
        if (!player.capabilities.isCreativeMode)
          itemstack.stackSize--; 
      } 
      return true;
    } 
    return false;
  }
  
  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    if (XUHelper.isPlayerFake(player) && zero.equals(player.getPlayerCoordinates()))
      return false; 
    MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player, true);
    if (movingobjectposition == null)
      return false; 
    if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      if (movingobjectposition.sideHit == 0)
        return false; 
      side = 0;
      x = movingobjectposition.blockX;
      y = movingobjectposition.blockY;
      z = movingobjectposition.blockZ;
      if (!this.enderLily.isFluid(world, x, y, z))
        return false; 
      hitX = (float)movingobjectposition.hitVec.xCoord - x;
      hitY = (float)movingobjectposition.hitVec.yCoord - y;
      hitZ = (float)movingobjectposition.hitVec.zCoord - z;
      y++;
      if (player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, stack)) {
        if (onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ)) {
          if (world.isRemote)
            ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, stack, hitX, hitY, hitZ); 
          return true;
        } 
        return false;
      } 
    } 
    return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBlockEnderLily.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */