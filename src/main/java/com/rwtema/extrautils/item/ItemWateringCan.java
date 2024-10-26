package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemWateringCan extends Item {
  public static ArrayList<ItemStack> flowers = null;

  private static Random rand = (Random)XURandom.getInstance();

  IIcon busted = null;

  IIcon reinforced = null;

  public ThreadLocal<Boolean> watering;

  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1) {
    if (par1 == 2)
      return this.busted;
    if (par1 == 3)
      return this.reinforced;
    return this.itemIcon;
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.itemIcon = par1IIconRegister.registerIcon(getIconString());
    this.busted = par1IIconRegister.registerIcon("extrautils:watering_can_bust");
    this.reinforced = par1IIconRegister.registerIcon("extrautils:watering_can_reinforced");
  }

  public int getMaxItemUseDuration(ItemStack par1ItemStack) {
    return 72000;
  }

  public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
    if (par1ItemStack.getItemDamage() != 3 || !XUHelper.isPlayerFake(par2EntityPlayer))
      return false;
    waterLocation(par3World, par4 + 0.5D, par5 + 0.5D, par6 + 0.5D, par7, par1ItemStack, par2EntityPlayer);
    return true;
  }

  public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player) {
    if (par1ItemStack.getItemDamage() != 1) {
      if (XUHelper.isPlayerFake(player)) {
        if (par1ItemStack.getItemDamage() == 0) {
          par1ItemStack.setItemDamage(2);
        } else {
          onUsingTick(par1ItemStack, player, 0);
        }
      } else if (par1ItemStack.getItemDamage() == 2 &&
        XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player)) {
        par1ItemStack.setItemDamage(4);
      }
      player.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
    } else {
      MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player, true);
      if (movingobjectposition == null)
        return par1ItemStack;
      if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
        int i = movingobjectposition.blockX;
        int j = movingobjectposition.blockY;
        int k = movingobjectposition.blockZ;
        if (world.getBlock(i, j, k).getMaterial() == Material.water) {
          if (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player)) {
            par1ItemStack.setItemDamage(3);
          } else {
            par1ItemStack.setItemDamage(0);
          }
          return par1ItemStack;
        }
      }
      return par1ItemStack;
    }
    return par1ItemStack;
  }

  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (par1ItemStack.getItemDamage() == 1)
      return getUnlocalizedName() + ".empty";
    if (par1ItemStack.getItemDamage() == 2)
      return getUnlocalizedName() + ".busted";
    if (par1ItemStack.getItemDamage() == 3)
      return getUnlocalizedName() + ".reinforced";
    return getUnlocalizedName();
  }

  public EnumAction getItemUseAction(ItemStack par1ItemStack) {
    return EnumAction.none;
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(new ItemStack(par1, 1, 1));
    par3List.add(new ItemStack(par1, 1, 2));
    par3List.add(new ItemStack(par1, 1, 3));
  }

  public void initFlowers() {
    if (flowers != null)
      return;
    flowers = new ArrayList<ItemStack>();
    if (!Loader.isModLoaded("Forestry"))
      return;
    try {
      Class<?> flowerManager = Class.forName("forestry.api.apiculture.FlowerManager");
      ArrayList<ItemStack> temp = (ArrayList<ItemStack>)flowerManager.getField("plainFlowers").get(null);
      flowers.addAll(temp);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
    MovingObjectPosition pos = getMovingObjectPositionFromPlayer(player.worldObj, player, true);
    if (pos != null)
      waterLocation(player.worldObj, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, pos.sideHit, stack, player);
  }

  public ItemWateringCan() {
    this.watering = new ThreadLocal<Boolean>();
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setUnlocalizedName("extrautils:watering_can");
    setTextureName("extrautils:watering_can");
    setHasSubtypes(false);
    setMaxStackSize(1);
  }

  public void waterLocation(World worldObj, double hitX, double hitY, double hitZ, int side, ItemStack stack, EntityPlayer play) {
    int range = 1;
    if (stack.getItemDamage() == 3)
      range = 3;
    if (stack.getItemDamage() == 4)
      range = 5;
    if (this.watering.get() == Boolean.TRUE)
      return;
    this.watering.set(Boolean.TRUE);
    waterLocation(worldObj, hitX, hitY, hitZ, side, stack, play, range);
    this.watering.set(Boolean.FALSE);
  }

  private void waterLocation(World worldObj, double hitX, double hitY, double hitZ, int side, ItemStack stack, EntityPlayer play, int range) {
    List enderman = worldObj.getEntitiesWithinAABB(EntityEnderman.class, AxisAlignedBB.getBoundingBox(hitX - range, hitY - range, hitZ - range, hitX + range, hitY + 6.0D, hitZ + range));
    if (enderman != null)
      for (Object anEnderman : enderman)
        ((EntityEnderman)anEnderman).attackEntityFrom(DamageSource.drown, 1.0F);
    boolean cheat = (stack.getItemDamage() == 4 && (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(play) || LogHelper.isDeObf || XUHelper.isPlayerFake(play)));
    if (worldObj.isRemote) {
      double dx = Facing.offsetsXForSide[side], dy = Facing.offsetsYForSide[side], dz = Facing.offsetsZForSide[side];
      double x2 = hitX + dx * 0.1D, y2 = hitY + dy * 0.1D, z2 = hitZ + dz * 0.1D;
      int i = 0;
      while (true) {
        if (i < ((stack.getItemDamage() == 2) ? 1 : ((stack.getItemDamage() == 3) ? 100 : 10))) {
          worldObj.spawnParticle("splash", x2 + worldObj.rand.nextGaussian() * 0.6D * range, y2, z2 + worldObj.rand.nextGaussian() * 0.6D * range, 0.0D, 0.0D, 0.0D);
          i++;
          continue;
        }
        break;
      }
    } else {
      List ents = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(hitX - range, hitY - range, hitZ - range, hitX + range, hitY + range + 6.0D, hitZ + range));
      if (ents != null)
        for (Object ent : ents) {
          if (((Entity)ent).isBurning()) {
            float p = 0.01F;
            if (ent instanceof EntityPlayer)
              p = 0.333F;
            ((Entity)ent).extinguish();
            if (worldObj.rand.nextDouble() < p) {
              if (stack.getItemDamage() < 3)
                stack.setItemDamage(1);
              if (play != null)
                play.stopUsingItem();
              return;
            }
          }
        }
      int blockX = (int)Math.floor(hitX);
      int blockY = (int)Math.floor(hitY);
      int blockZ = (int)Math.floor(hitZ);
      for (int x = blockX - range; x <= blockX + range; x++) {
        for (int y = blockY - range; y <= blockY + range; y++) {
          for (int z = blockZ - range; z <= blockZ + range; z++) {
            Block id = worldObj.getBlock(x, y, z);
            if (!worldObj.isAirBlock(x, y, z)) {
              if (id == Blocks.fire)
                worldObj.setBlockToAir(x, y, z);
              if (id == Blocks.flowing_lava &&
                worldObj.rand.nextInt(2) == 0)
                Blocks.flowing_lava.updateTick(worldObj, x, y, z, worldObj.rand);
              if (id == Blocks.farmland &&
                worldObj.getBlockMetadata(x, y, z) < 7)
                worldObj.setBlockMetadataWithNotify(x, y, z, 7, 2);
              int timer = -1;
              if (id == Blocks.grass) {
                timer = 20;
                if (!cheat && worldObj.rand.nextInt(4500) == 0 &&
                  worldObj.isAirBlock(x, y + 1, z)) {
                  initFlowers();
                  if (flowers.size() > 0 && worldObj.rand.nextInt(5) == 0) {
                    ItemStack flower = flowers.get(worldObj.rand.nextInt(flowers.size()));
                    if (flower.getItem() instanceof ItemBlock &&
                      play != null)
                      ((ItemBlock)flower.getItem()).placeBlockAt(flower, play, worldObj, x, y + 1, z, 1, 0.5F, 1.0F, 0.5F, flower.getItem().getMetadata(flower.getItemDamage()));
                  } else {
                    worldObj.getBiomeGenForCoords(x, z).plantFlower(worldObj, rand, x, y + 1, z);
                  }
                }
              } else if (id == Blocks.mycelium) {
                timer = 20;
              } else if (id == Blocks.wheat) {
                timer = 40;
              } else if (id instanceof net.minecraft.block.BlockSapling) {
                timer = 50;
              } else if (id instanceof net.minecraftforge.common.IPlantable || id instanceof net.minecraft.block.IGrowable) {
                timer = 40;
              } else if (id.getMaterial() == Material.grass) {
                timer = 20;
              }
              if (stack.getItemDamage() == 2)
                timer *= 20;
              timer /= range;
              if (timer > 0 &&
                id.getTickRandomly())
                worldObj.scheduleBlockUpdate(x, y, z, id, worldObj.rand.nextInt(timer));
            }
          }
        }
      }
      if (cheat)
        for (int i = 0; i < 100; i++) {
          for (int j = blockX - range; j <= blockX + range; j++) {
            for (int y = blockY - range; y <= blockY + range; y++) {
              for (int z = blockZ - range; z <= blockZ + range; z++) {
                Block block = worldObj.getBlock(j, y, z);
                block.updateTick(worldObj, j, y, z, worldObj.rand);
                TileEntity tile = worldObj.getTileEntity(j, y, z);
                if (tile != null && tile.canUpdate() && !tile.isInvalid())
                  tile.updateEntity();
              }
            }
          }
        }
    }
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    if (par1ItemStack.getItemDamage() == 2) {
      par3List.add("It appears that mechanical hands are not delicate enough");
      par3List.add("to use the watering can properly.");
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemWateringCan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
