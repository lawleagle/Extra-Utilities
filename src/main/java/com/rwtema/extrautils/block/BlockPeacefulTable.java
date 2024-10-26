package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class BlockPeacefulTable extends BlockMultiBlock {
  private IIcon[] icons;

  public BlockPeacefulTable() {
    super(Material.wood);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    setBlockName("extrautils:peaceful_table_top");
    setBlockTextureName("extrautils:peaceful_table_top");
    setTickRandomly(true);
    setHardness(1.0F);
    setResistance(10.0F).setStepSound(soundTypeWood);
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.icons = new IIcon[3];
    this.icons[0] = par1IIconRegister.registerIcon("extrautils:peaceful_table_bottom");
    this.icons[1] = par1IIconRegister.registerIcon("extrautils:peaceful_table_top");
    this.icons[2] = par1IIconRegister.registerIcon("extrautils:peaceful_table_side");
  }

  public void updateTick(World world, int x, int y, int z, Random par5Random) {
    if (!world.isRemote && world instanceof WorldServer) {
      if (!ExtraUtils.peacefulTableInAllDifficulties && world.difficultySetting != EnumDifficulty.PEACEFUL)
        return;
      BiomeGenBase.SpawnListEntry var7 = ((WorldServer)world).spawnRandomCreature(EnumCreatureType.monster, x, y, z);
      if (var7 != null) {
        EntityLiving t;
        IInventory swordInv = null;
        int swordSlot = -1;
        ItemStack sword = null;
        for (int j = 0; j < 6; j++) {
          TileEntity tile = world.getTileEntity(x + Facing.offsetsXForSide[j], y + Facing.offsetsYForSide[j], z + Facing.offsetsZForSide[j]);
          if (tile instanceof IInventory) {
            IInventory inv = (IInventory)tile;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
              ItemStack item = inv.getStackInSlot(i);
              if (item != null &&
                item.getItem() instanceof net.minecraft.item.ItemSword) {
                swordInv = inv;
                swordSlot = i;
                sword = item;
                break;
              }
            }
            if (sword != null)
              break;
          }
        }
        if (sword == null)
          return;
        try {
          t = (EntityLiving)var7.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
        } catch (Exception var31) {
          var31.printStackTrace();
          return;
        }
        List list1 = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((x - 2), y, (z - 2), (x + 3), (y + 4), (z + 2)), IEntitySelector.selectAnything);
        t.setLocationAndAngles(x + 0.5D, y + 1.25D, z + 0.5D, par5Random.nextFloat() * 360.0F, 0.0F);
        world.spawnEntityInWorld((Entity)t);
        EnumDifficulty prev = world.difficultySetting;
        world.difficultySetting = EnumDifficulty.HARD;
        t.onSpawnWithEgg(null);
        world.difficultySetting = prev;
        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)world);
        fakePlayer.setCurrentItemOrArmor(0, sword.copy());
        float h1 = t.getHealth();
        fakePlayer.attackTargetEntityWithCurrentItem((Entity)t);
        float h2 = t.getHealth();
        if (t.isDead) {
          if (fakePlayer.getCurrentEquippedItem() == null || (fakePlayer.getCurrentEquippedItem()).stackSize == 0) {
            swordInv.setInventorySlotContents(swordSlot, null);
          } else {
            swordInv.setInventorySlotContents(swordSlot, fakePlayer.getCurrentEquippedItem());
          }
        } else {
          if (fakePlayer.getCurrentEquippedItem() == null || (fakePlayer.getCurrentEquippedItem()).stackSize == 0) {
            swordInv.setInventorySlotContents(swordSlot, null);
          } else {
            if (h1 > h2) {
              float h;
              for (h = h2; h > 0.0F; h -= h1 - h2)
                sword.hitEntity((EntityLivingBase)t, (EntityPlayer)fakePlayer);
            }
            if (sword.stackSize == 0)
              swordInv.setInventorySlotContents(swordSlot, null);
          }
          t.onDeath(DamageSource.causePlayerDamage((EntityPlayer)fakePlayer));
          t.motionX = 0.0D;
          t.motionY = 0.0D;
          t.motionZ = 0.0D;
        }
        fakePlayer.setCurrentItemOrArmor(0, null);
        t.setDead();
        List list2 = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((x - 2), y, (z - 2), (x + 3), (y + 4), (z + 2)));
        for (Object aList2 : list2) {
          if (!list1.contains(aList2)) {
            int[] seq = XUHelper.rndSeq(6, world.rand);
            for (int i = 0; i < 6; i++) {
              IInventory inv = TileEntityHopper.func_145893_b(world, (x + Facing.offsetsXForSide[seq[i]]), (y + Facing.offsetsYForSide[seq[i]]), (z + Facing.offsetsZForSide[seq[i]]));
              if (inv != null && !((EntityItem)aList2).isDead) {
                ItemStack itemstack = ((EntityItem)aList2).getEntityItem().copy();
                ItemStack itemstack1 = XUHelper.invInsert(inv, itemstack, Facing.oppositeSide[i]);
                if (itemstack1 != null && itemstack1.stackSize != 0) {
                  ((EntityItem)aList2).setEntityItemStack(itemstack1);
                } else {
                  ((EntityItem)aList2).setDead();
                }
              }
            }
            ((EntityItem)aList2).setDead();
          }
        }
      }
    }
  }

  public void onBlockAdded(World world, int x, int y, int z) {
    if (!world.isRemote)
      world.scheduleBlockUpdate(x, y, z, this, 5 + world.rand.nextInt(100));
  }

  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity par5Entity) {
    if (par5Entity instanceof net.minecraft.entity.item.EntityXPOrb)
      par5Entity.setDead();
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int x) {
    int i = Math.min(par1, 2);
    return this.icons[i];
  }

  public void prepareForRender(String label) {}

  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    BoxModel boxes = new BoxModel();
    float h = 0.0625F;
    boxes.add(new Box(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F));
    boxes.add(new Box(0.0625F, 0.0F, 0.0625F, 0.3125F, 0.75F, 0.3125F));
    boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.3125F, 0.75F, 0.3125F)).rotateY(1));
    boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.3125F, 0.75F, 0.3125F)).rotateY(2));
    boxes.add((new Box(0.0625F, 0.0F, 0.0625F, 0.3125F, 0.75F, 0.3125F)).rotateY(3));
    return boxes;
  }

  public BoxModel getInventoryModel(int metadata) {
    return getWorldModel((IBlockAccess)null, 0, 0, 0);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockPeacefulTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
