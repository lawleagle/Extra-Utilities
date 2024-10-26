package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ChunkPos;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBuildersWand extends Item implements IItemMultiTransparency {
  public int maxBlocks = 9;

  private IIcon[] icons;

  public ItemBuildersWand(int par2) {
    this.maxStackSize = 1;
    setUnlocalizedName("extrautils:builderswand");
    this.maxBlocks = par2;
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return false;
  }

  public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

  public EnumAction getItemUseAction(ItemStack par1ItemStack) {
    return EnumAction.none;
  }

  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    if (world.isRemote)
      ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, stack, hitX, hitY, hitZ);
    return true;
  }

  public boolean onItemUse(ItemStack wand, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    if (world.isRemote)
      return true;
    if (!player.capabilities.allowEdit)
      return false;
    List<ChunkPos> blocks = getPotentialBlocks(player, world, x, y, z, side);
    if (blocks.size() == 0)
      return false;
    Block blockId = world.getBlock(x, y, z);
    if (world.isAirBlock(x, y, z))
      return false;
    int data = -1;
    Item item1 = Item.getItemFromBlock(blockId);
    if (item1.getHasSubtypes())
      data = blockId.getDamageValue(world, x, y, z);
    if (blocks.size() > 0) {
      int slot = 0;
      for (ChunkPos temp : blocks) {
        for (slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
          if (player.inventory.getStackInSlot(slot) != null &&
            player.inventory.getStackInSlot(slot).getItem() == item1)
            if ((((data == -1) ? 1 : 0) | ((data == player.inventory.getStackInSlot(slot).getItemDamage()) ? 1 : 0)) != 0)
              break;
        }
        if (slot < player.inventory.getSizeInventory()) {
          ItemStack item = player.inventory.getStackInSlot(slot);
          if (player.capabilities.isCreativeMode)
            item = item.copy();
          if (item.tryPlaceItemIntoWorld(player, world, temp.x, temp.y, temp.z, side, hitX, hitY, hitZ) &&
            !player.capabilities.isCreativeMode && item.stackSize == 0)
            player.inventory.setInventorySlotContents(slot, null);
        }
      }
      player.inventory.markDirty();
      if (player instanceof EntityPlayerMP)
        ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
    }
    return true;
  }

  public List<ChunkPos> getPotentialBlocks(EntityPlayer player, World world, int x, int y, int z, int face) {
    List<ChunkPos> blocks = new ArrayList<ChunkPos>();
    if (world == null)
      return blocks;
    Block blockId = world.getBlock(x, y, z);
    if (world.isAirBlock(x, y, z))
      return blocks;
    if (player == null || XUHelper.isPlayerFake(player))
      return blocks;
    int data = -1;
    if (Item.getItemFromBlock(blockId) == null)
      return blocks;
    if (Item.getItemFromBlock(blockId).getHasSubtypes())
      data = blockId.getDamageValue(world, x, y, z);
    int numBlocks = 0;
    ItemStack genericStack = null;
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      if (player.inventory.getStackInSlot(i) != null) {
        if (player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(blockId) && (
          data == -1 || data == player.inventory.getStackInSlot(i).getItemDamage())) {
          genericStack = player.inventory.getStackInSlot(i);
          if (player.capabilities.isCreativeMode) {
            numBlocks = this.maxBlocks;
            break;
          }
          numBlocks += (player.inventory.getStackInSlot(i)).stackSize;
        }
        if (numBlocks >= this.maxBlocks) {
          numBlocks = this.maxBlocks;
          break;
        }
      }
    }
    int dx = Facing.offsetsXForSide[face], dy = Facing.offsetsYForSide[face], dz = Facing.offsetsZForSide[face];
    int mx = (dx == 0) ? 1 : 0, my = (dy == 0) ? 1 : 0, mz = (dz == 0) ? 1 : 0;
    boolean hollow = false;
    if (ExtraUtilsMod.proxy.isAltSneaking()) {
      if (player.isSneaking()) {
        hollow = true;
      } else if (face > 1) {
        mx = 0;
        mz = 0;
        my = 1;
      } else {
        return blocks;
      }
    } else if (player.isSneaking()) {
      if (face > 1) {
        my = 0;
      } else {
        return blocks;
      }
    }
    AxisAlignedBB var11 = blockId.getCollisionBoundingBoxFromPool(world, x, y, z);
    if (numBlocks <= 0 || (blockId.canPlaceBlockOnSide(world, x + dx, y + dy, z + dz, face) & (((y + dy < 255) ? 1 : 0) != 0)) == false)
      return blocks;
    if (!checkAAB(world, var11, dx, dy, dz))
      return blocks;
    blocks.add(new ChunkPos(x + dx, y + dy, z + dz));
    int j = 0;
    while (true) {
      if ((((j < blocks.size()) ? 1 : 0) & ((blocks.size() < numBlocks) ? 1 : 0)) != 0) {
        for (int ax = -mx; ax <= mx; ax++) {
          for (int ay = -my; ay <= my; ay++) {
            for (int az = -mz; az <= mz; az++) {
              ChunkPos temp = new ChunkPos(((ChunkPos)blocks.get(j)).x + ax, ((ChunkPos)blocks.get(j)).y + ay, ((ChunkPos)blocks.get(j)).z + az);
              if (blocks.size() < numBlocks &&
                player.canPlayerEdit(temp.x, temp.y, temp.z, face, genericStack) &&
                !blocks.contains(temp) &&
                world.getBlock(temp.x - dx, temp.y - dy, temp.z - dz) == blockId && blockId.canPlaceBlockOnSide(world, temp.x, temp.y, temp.z, face) && (
                data == -1 || data == blockId.getDamageValue(world, temp.x - dx, temp.y - dy, temp.z - dz)) &&
                checkAAB(world, var11, temp.x - x, temp.y - y, temp.z - z) &&
                !blocks.contains(temp))
                blocks.add(temp);
            }
          }
        }
        j++;
        continue;
      }
      break;
    }
    return blocks;
  }

  public boolean checkAAB(World world, AxisAlignedBB bounds, int dx, int dy, int dz) {
    if (bounds == null)
      return true;
    if (world.checkNoEntityCollision(bounds.getOffsetBoundingBox(dx, dy, dz)))
      return true;
    return false;
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[2];
    this.itemIcon = this.icons[0] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5));
    this.icons[1] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5) + "1");
  }

  public int numIcons(ItemStack item) {
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
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemBuildersWand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
