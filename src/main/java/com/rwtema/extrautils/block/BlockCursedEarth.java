package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.IBlockLocalization;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Method;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCursedEarth extends Block implements IBlockLocalization {
  public static int powered = 0;

  private IIcon cursedSide;

  private IIcon cursedTop;

  private IIcon blessedSide;

  private IIcon blessedTop;

  private IIcon dirt;

  private Method cache;

  public BlockCursedEarth() {
    super(Material.grass);
    this.cache = null;
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabBlock);
    setStepSound(Block.soundTypeGravel);
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setBlockName("extrautils:cursedearthside");
    setBlockTextureName("extrautils:cursedearthside");
    setHardness(0.5F);
    this.blockResistance = 200.0F;
  }

  public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
    return true;
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return Item.getItemFromBlock(Blocks.dirt);
  }

  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int par1, int par2) {
    if (par1 == 0)
      return this.dirt;
    if (par2 >> 1 == 0) {
      if (par1 == 1)
        return this.cursedTop;
      return this.cursedSide;
    }
    if (par2 >> 1 == 1) {
      if (par1 == 1)
        return this.blessedTop;
      return this.blessedSide;
    }
    return this.dirt;
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    this.dirt = par1IIconRegister.registerIcon("dirt");
    this.cursedSide = par1IIconRegister.registerIcon("extrautils:cursedearthside");
    this.cursedTop = par1IIconRegister.registerIcon("extrautils:cursedearthtop");
    this.blessedSide = par1IIconRegister.registerIcon("extrautils:blessedEarthSide");
    this.blessedTop = par1IIconRegister.registerIcon("extrautils:blessedEarthTop");
  }

  public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
    if (world.isRemote)
      return;
    if ((world.getBlockMetadata(x, y, z) & 0xE) == 2)
      return;
    if (entity.isEntityAlive())
      if (world.getBlockLightValue(x, y + 1, z) > 9) {
        if (!(entity instanceof EntityPlayer))
          entity.attackEntityFrom(DamageSource.cactus, 1.0F);
        if (world.rand.nextInt(24) == 0 && world.canBlockSeeTheSky(x, y + 1, z) && world.isAirBlock(x, y + 1, z)) {
          world.setBlock(x, y + 1, z, (Block)Blocks.fire);
          world.scheduleBlockUpdate(x, y, z, (Block)Blocks.fire, 1 + world.rand.nextInt(200));
        }
        for (int i = 0; i < 20; i++) {
          int dx = x + world.rand.nextInt(9) - 4;
          int dy = y + world.rand.nextInt(5) - 3;
          int dz = z + world.rand.nextInt(9) - 4;
          if ((((world.getBlock(dx, dy, dz) == this) ? 1 : 0) & (((world.getBlockLightOpacity(dx, dy + 1, dz) > 2) ? 1 : 0) | ((world.getBlockLightValue(dx, dy + 1, dz) > 9) ? 1 : 0))) != 0)
            world.scheduleBlockUpdate(dx, dy, dz, this, 10 + world.rand.nextInt(600));
        }
      } else if (entity instanceof EntityMob) {
        ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 7200, 1));
        ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.damageBoost.id, 7200, 1));
        ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 0));
        ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 0));
      } else if (entity instanceof EntityPlayer) {
        for (int i = 0; i < 3; i++) {
          int var7 = x + world.rand.nextInt(9) - 4;
          int var8 = y + world.rand.nextInt(5) - 3;
          int var9 = z + world.rand.nextInt(9) - 4;
          if (world.getBlock(var7, var8, var9) == this && world.getBlockLightValue(var7, var8 + 1, var9) < 9)
            world.scheduleBlockUpdate(var7, var8, var9, this, world.rand.nextInt(100));
        }
      }
  }

  public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return powered;
  }

  public boolean renderAsNormalBlock() {
    return (powered == 0);
  }

  public void resetTimer(MobSpawnerBaseLogic logic) {
    if (this.cache == null)
      this.cache = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, logic, new String[] { "resetTimer", "func_98273_j" }, new Class[0]);
    try {
      this.cache.invoke(logic, new Object[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int onBlockPlaced(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
    if (world.getTileEntity(x, y + 1, z) instanceof TileEntityMobSpawner)
      world.scheduleBlockUpdate(x, y, z, this, 10);
    return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
  }

  public int getMobilityFlag() {
    return 2;
  }

  public void onNeighborBlockChange(World p_149695_1_, int x, int y, int z, Block p_149695_5_) {
    if (p_149695_5_ == Blocks.mob_spawner)
      p_149695_1_.scheduleBlockUpdate(x, y, z, this, 10);
    super.onNeighborBlockChange(p_149695_1_, x, y, z, p_149695_5_);
  }

  public int damageDropped(int p_149692_1_) {
    return p_149692_1_ & 0xE;
  }

  public void spawnLogic(MobSpawnerBaseLogic logic) {
    if (logic.spawnDelay == -1)
      resetTimer(logic);
    if (logic.spawnDelay > 0) {
      logic.spawnDelay -= 100;
      if (logic.spawnDelay < 0)
        logic.spawnDelay = 0;
      return;
    }
    boolean flag = false;
    NBTTagCompound tags = new NBTTagCompound();
    logic.writeToNBT(tags);
    int spawnCount = tags.getShort("SpawnCount");
    int maxNearbyEntities = tags.getShort("MaxNearbyEntities");
    int spawnRange = tags.getShort("SpawnRange");
    for (int i = 0; i < spawnCount; i++) {
      Entity entity = EntityList.createEntityByName(logic.getEntityNameToSpawn(), logic.getSpawnerWorld());
      if (entity == null)
        return;
      int j = logic.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getBoundingBox(logic.getSpawnerX(), logic.getSpawnerY(), logic.getSpawnerZ(), (logic.getSpawnerX() + 1), (logic.getSpawnerY() + 1), (logic.getSpawnerZ() + 1)).expand((spawnRange * 2), 4.0D, (spawnRange * 2))).size();
      if (j >= maxNearbyEntities) {
        resetTimer(logic);
        return;
      }
      double d2 = logic.getSpawnerX() + ((logic.getSpawnerWorld()).rand.nextDouble() - (logic.getSpawnerWorld()).rand.nextDouble()) * spawnRange;
      double d3 = (logic.getSpawnerY() + (logic.getSpawnerWorld()).rand.nextInt(3) - 1);
      double d4 = logic.getSpawnerZ() + ((logic.getSpawnerWorld()).rand.nextDouble() - (logic.getSpawnerWorld()).rand.nextDouble()) * spawnRange;
      EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
      entity.setLocationAndAngles(d2, d3, d4, (logic.getSpawnerWorld()).rand.nextFloat() * 360.0F, 0.0F);
      if (entityliving != null && SpawnMob(entityliving)) {
        logic.func_98265_a((Entity)entityliving);
        logic.getSpawnerWorld().playAuxSFX(2004, logic.getSpawnerX(), logic.getSpawnerY(), logic.getSpawnerZ(), 0);
        entityliving.spawnExplosionParticle();
        flag = true;
      }
    }
    if (flag)
      resetTimer(logic);
  }

  public void updateTick(World world, int x, int y, int z, Random rand) {
    if (!world.isRemote && world instanceof WorldServer) {
      if (world.getTileEntity(x, y + 1, z) instanceof TileEntityMobSpawner) {
        MobSpawnerBaseLogic logic = ((TileEntityMobSpawner)world.getTileEntity(x, y + 1, z)).func_145881_a();
        spawnLogic(logic);
        world.scheduleBlockUpdate(x, y, z, this, 100);
        return;
      }
      boolean isWorldGen = ((world.getBlockMetadata(x, y, z) & 0x1) == 1);
      boolean blessed = ((world.getBlockMetadata(x, y, z) & 0xE) == 2);
      if (blessed || world.getBlockLightValue(x, y + 1, z) < 9) {
        boolean flag = (blessed || world.difficultySetting.getDifficultyId() > 0);
        if (flag && isWorldGen &&
          world.getClosestPlayer(x, y, z, 10.0D) == null)
          flag = false;
        if (flag) {
          int var12 = world.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1)).expand(7.0D, 2.0D, 7.0D)).size();
          flag = (rand.nextInt(blessed ? 8 : 4) >= var12);
        }
        if (flag) {
          EnumCreatureType type = EnumCreatureType.monster;
          if ((world.getBlockMetadata(x, y, z) & 0xE) == 2)
            type = EnumCreatureType.creature;
          BiomeGenBase.SpawnListEntry var7 = ((WorldServer)world).spawnRandomCreature(type, x, y, z);
          if (var7 != null && !EntityFlying.class.isAssignableFrom(var7.entityClass)) {
            EntityLiving t;
            try {
              t = (EntityLiving)var7.entityClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
            } catch (Exception var31) {
              var31.printStackTrace();
              return;
            }
            t.setLocationAndAngles(x + 0.5D, (y + 1), z + 0.5D, rand.nextFloat() * 360.0F, 0.0F);
            int meta = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, (Block)Blocks.grass, 0, 0);
            if (SpawnMob(t)) {
              world.spawnEntityInWorld((Entity)t);
              t.playLivingSound();
            }
            world.setBlock(x, y, z, this, meta, 0);
          }
        }
      }
      if (blessed)
        return;
      if (world.getBlockLightOpacity(x, y + 1, z) > 2 || world.getBlockLightValue(x, y + 1, z) > 9) {
        boolean nearbyFire = (world.getBlock(x, y + 1, z) == Blocks.fire);
        if (nearbyFire) {
          if (rand.nextInt(3) == 0) {
            world.setBlock(x, y, z, Blocks.dirt);
            return;
          }
        } else if (world.isAirBlock(x, y + 1, z) && world.canBlockSeeTheSky(x, y + 1, z)) {
          world.setBlock(x, y + 1, z, (Block)Blocks.fire);
          nearbyFire = true;
        }
        if (!nearbyFire)
          for (int i = 0; i < 20; i++) {
            int dx = x + rand.nextInt(9) - 4;
            int dy = y + rand.nextInt(5) - 3;
            int dz = z + rand.nextInt(9) - 4;
            if (world.getBlock(dx, dy, dz) == Blocks.fire) {
              nearbyFire = true;
              break;
            }
          }
        if (nearbyFire)
          for (int i = 0; i < 20; i++) {
            int dx = x + rand.nextInt(9) - 4;
            int dy = y + rand.nextInt(5) - 3;
            int dz = z + rand.nextInt(9) - 4;
            if ((((world.getBlock(dx, dy, dz) == this) ? 1 : 0) & (((world.getBlockLightOpacity(dx, dy + 1, dz) > 2) ? 1 : 0) | ((world.getBlockLightValue(dx, dy + 1, dz) > 9) ? 1 : 0))) != 0 &&
              world.getBlock(dx, dy + 1, dz) != Blocks.fire)
              if (rand.nextInt(4) == 0 && world.isAirBlock(dx, dy + 1, dz)) {
                world.setBlock(dx, dy + 1, dz, (Block)Blocks.fire);
              } else {
                world.setBlock(dx, dy, dz, Blocks.dirt);
              }
          }
      } else if (world.getBlockLightValue(x, y + 1, z) < 9) {
        for (int var6 = 0; var6 < 4; var6++) {
          int var7 = x + rand.nextInt(3) - 1;
          int var8 = y + rand.nextInt(5) - 3;
          int var9 = z + rand.nextInt(3) - 1;
          if ((world.getBlock(var7, var8, var9) == Blocks.dirt || world.getBlock(var7, var8, var9) == Blocks.grass) && world.getBlockLightOpacity(var7, var8 + 1, var9) <= 2 && world.getBlockLightValue(var7, var8 + 1, var9) < 9)
            world.setBlock(var7, var8, var9, this, world.getBlockMetadata(x, y, z), 3);
        }
      }
    }
  }

  public boolean SpawnMob(EntityLiving t) {
    if (t.getCanSpawnHere()) {
      t.onSpawnWithEgg(null);
      if (t instanceof EntityMob) {
        t.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 3600, 1));
        t.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 3600, 1));
      } else {
        t.addPotionEffect(new PotionEffect(Potion.regeneration.id, 3600, 1));
      }
      t.getEntityData().setLong("CursedEarth", 3600L);
      t.forceSpawn = true;
      t.func_110163_bv();
      return true;
    }
    return false;
  }

  public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
    return true;
  }

  public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
    return true;
  }

  public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    super.randomDisplayTick(par1World, par2, par3, par4, par5Random);
    if (par5Random.nextInt(2) == 0 && par1World.getBlockMetadata(par2, par3, par4) == 0)
      par1World.spawnParticle("portal", (par2 + par5Random.nextFloat()), (par3 + 1.1F), (par4 + par5Random.nextFloat()), 0.0D, 0.05D, 0.0D);
  }

  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
    if (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player) || XUHelper.deObf) {
      world.setBlock(x, y, z, this, 2, 3);
      return true;
    }
    return false;
  }

  public String getUnlocalizedName(ItemStack par1ItemStack) {
    if (par1ItemStack.getItemDamage() == 0)
      return getUnlocalizedName();
    return getUnlocalizedName() + ".blessed";
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockCursedEarth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
