package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.IBlockLocalization;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import com.rwtema.extrautils.particle.ParticlePortal;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import com.rwtema.extrautils.worldgen.Underdark.TeleporterUnderdark;
import com.rwtema.extrautils.worldgen.endoftime.TeleporterEndOfTime;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;

public class BlockPortal extends Block implements IBlockLocalization {
  public static IIcon particle;
  
  private IIcon lightPortal;
  
  public static ItemStack darkPortalItemStack;
  
  public static ItemStack lightPortalItemStack;
  
  public BlockPortal() {
    super(Material.rock);
    setBlockTextureName("extrautils:dark_portal");
    setBlockName("extrautils:dark_portal");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(2.0F);
    darkPortalItemStack = new ItemStack(this, 1, 0);
    lightPortalItemStack = new ItemStack(this, 1, 2);
  }
  
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_) {
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
    if (p_149691_2_ >> 1 == 1)
      return this.lightPortal; 
    return super.getIcon(p_149691_1_, p_149691_2_);
  }
  
  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    return (world.getBlockMetadata(x, y, z) >> 1 == 0) ? 15 : 0;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister par1IIconRegister) {
    super.registerBlockIcons(par1IIconRegister);
    this.lightPortal = par1IIconRegister.registerIcon("extrautils:light_portal");
    particle = par1IIconRegister.registerIcon("extrautils:particle_blue");
  }
  
  public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
    par1World.setBlock(par2, par3 + 1, par4, Blocks.torch);
  }
  
  public float getBlockHardness(World world, int x, int y, int z) {
    return ((world.getBlockMetadata(x, y, z) & 0x1) == 1) ? -1.0F : 2.0F;
  }
  
  public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
    return ((world.getBlockMetadata(x, y, z) & 0x1) != 1);
  }
  
  public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
    return ((world.getBlockMetadata(x, y, z) & 0x1) == 1) ? 1.0E20F : super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
  }
  
  public boolean canDropFromExplosion(Explosion par1Explosion) {
    return (par1Explosion == null || !(par1Explosion.exploder instanceof net.minecraft.entity.projectile.EntityWitherSkull));
  }
  
  public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    if (!world.isRemote && canDropFromExplosion(explosion) && (world.getBlockMetadata(x, y, z) & 0x1) == 0)
      super.onBlockExploded(world, x, y, z, explosion); 
  }
  
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entity, int par6, float par7, float par8, float par9) {
    return (world.isRemote || transferPlayer(world, x, y, z, (Entity)entity));
  }
  
  public boolean transferPlayer(World world, int x, int y, int z, Entity entity) {
    if (entity.ridingEntity == null && entity.riddenByEntity == null && entity instanceof EntityPlayerMP) {
      EntityPlayerMP thePlayer = (EntityPlayerMP)entity;
      if (XUHelper.isPlayerFake(thePlayer))
        return false; 
      int type = world.getBlockMetadata(x, y, z) >> 1;
      if (type == 0) {
        if (ExtraUtils.underdarkDimID == 0)
          return false; 
        if (thePlayer.dimension != ExtraUtils.underdarkDimID) {
          thePlayer.setLocationAndAngles(x + 0.5D, thePlayer.posY, z + 0.5D, thePlayer.rotationYaw, thePlayer.rotationPitch);
          thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, ExtraUtils.underdarkDimID, (Teleporter)new TeleporterUnderdark(thePlayer.mcServer.worldServerForDimension(ExtraUtils.underdarkDimID)));
        } else {
          thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, (Teleporter)new TeleporterUnderdark(thePlayer.mcServer.worldServerForDimension(0)));
        } 
        return true;
      } 
      if (type == 1) {
        if (ExtraUtils.endoftimeDimID == 0)
          return false; 
        if (thePlayer.dimension != ExtraUtils.endoftimeDimID) {
          thePlayer.setLocationAndAngles(x + 0.5D, thePlayer.posY, z + 0.5D, thePlayer.rotationYaw, thePlayer.rotationPitch);
          thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, ExtraUtils.endoftimeDimID, (Teleporter)new TeleporterEndOfTime(thePlayer.mcServer.worldServerForDimension(ExtraUtils.endoftimeDimID)));
        } else {
          thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, (Teleporter)new TeleporterEndOfTime(thePlayer.mcServer.worldServerForDimension(0)));
        } 
        return true;
      } 
    } 
    return false;
  }
  
  public boolean hasTileEntity(int metadata) {
    return true;
  }
  
  public TileEntity createTileEntity(World world, int metadata) {
    return (TileEntity)new TileEntityPortal();
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    int type = par1ItemStack.getItemDamage() >> 1;
    return getUnlocalizedName() + ((type == 0) ? "" : ("." + type));
  }
  
  public int damageDropped(int meta) {
    return meta & 0xE;
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random r) {
    if (world.getBlockMetadata(x, y, z) >> 1 == 0)
      return; 
    double dx = MathHelper.clamp_double(0.5D + 0.2D * r.nextGaussian(), 0.0D, 1.0D);
    double dz = MathHelper.clamp_double(0.5D + 0.2D * r.nextGaussian(), 0.0D, 1.0D);
    ParticleHelperClient.addParticle((EntityFX)new ParticlePortal(world, x + dx, (y + 1), z + dz, 1.0F, 1.0F, 1.0F));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockPortal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */