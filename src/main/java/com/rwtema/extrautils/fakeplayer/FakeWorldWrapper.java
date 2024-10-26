package com.rwtema.extrautils.fakeplayer;

import com.google.common.collect.ImmutableSetMultimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;

public class FakeWorldWrapper extends FakeWorld {
  World world;
  
  public FakeWorldWrapper(World world) {
    this.world = world;
  }
  
  public BiomeGenBase getBiomeGenForCoords(int par1, int par2) {
    return this.world.getBiomeGenForCoords(par1, par2);
  }
  
  public BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2) {
    return this.world.getBiomeGenForCoordsBody(par1, par2);
  }
  
  public WorldChunkManager getWorldChunkManager() {
    return this.world.getWorldChunkManager();
  }
  
  protected IChunkProvider createChunkProvider() {
    return null;
  }
  
  public void initialize(WorldSettings par1WorldSettings) {}
  
  @SideOnly(Side.CLIENT)
  public void setSpawnLocation() {
    this.world.setSpawnLocation();
  }
  
  public Block getTopBlock(int p_147474_1_, int p_147474_2_) {
    return this.world.getTopBlock(p_147474_1_, p_147474_2_);
  }
  
  public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_) {
    return this.world.getBlock(p_147439_1_, p_147439_2_, p_147439_3_);
  }
  
  public boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_) {
    return this.world.isAirBlock(p_147437_1_, p_147437_2_, p_147437_3_);
  }
  
  public boolean blockExists(int par1, int par2, int par3) {
    return this.world.blockExists(par1, par2, par3);
  }
  
  public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4) {
    return this.world.doChunksNearChunkExist(par1, par2, par3, par4);
  }
  
  public boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6) {
    return this.world.checkChunksExist(par1, par2, par3, par4, par5, par6);
  }
  
  public boolean chunkExists(int par1, int par2) {
    return this.world.getChunkProvider().chunkExists(par1, par2);
  }
  
  public Chunk getChunkFromBlockCoords(int par1, int par2) {
    return this.world.getChunkFromBlockCoords(par1, par2);
  }
  
  public Chunk getChunkFromChunkCoords(int par1, int par2) {
    return this.world.getChunkFromChunkCoords(par1, par2);
  }
  
  public boolean setBlock(int p_147465_1_, int p_147465_2_, int p_147465_3_, Block p_147465_4_, int p_147465_5_, int p_147465_6_) {
    return this.world.setBlock(p_147465_1_, p_147465_2_, p_147465_3_, p_147465_4_, p_147465_5_, p_147465_6_);
  }
  
  public int getBlockMetadata(int par1, int par2, int par3) {
    return this.world.getBlockMetadata(par1, par2, par3);
  }
  
  public boolean setBlockMetadataWithNotify(int par1, int par2, int par3, int par4, int par5) {
    return this.world.setBlockMetadataWithNotify(par1, par2, par3, par4, par5);
  }
  
  public boolean setBlockToAir(int p_147468_1_, int p_147468_2_, int p_147468_3_) {
    return this.world.setBlockToAir(p_147468_1_, p_147468_2_, p_147468_3_);
  }
  
  public boolean func_147480_a(int p_147480_1_, int p_147480_2_, int p_147480_3_, boolean p_147480_4_) {
    return this.world.func_147480_a(p_147480_1_, p_147480_2_, p_147480_3_, p_147480_4_);
  }
  
  public boolean setBlock(int p_147449_1_, int p_147449_2_, int p_147449_3_, Block p_147449_4_) {
    return this.world.setBlock(p_147449_1_, p_147449_2_, p_147449_3_, p_147449_4_);
  }
  
  public void markBlockForUpdate(int p_147471_1_, int p_147471_2_, int p_147471_3_) {
    this.world.markBlockForUpdate(p_147471_1_, p_147471_2_, p_147471_3_);
  }
  
  public void notifyBlockChange(int p_147444_1_, int p_147444_2_, int p_147444_3_, Block p_147444_4_) {
    this.world.notifyBlockChange(p_147444_1_, p_147444_2_, p_147444_3_, p_147444_4_);
  }
  
  public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4) {
    this.world.markBlocksDirtyVertical(par1, par2, par3, par4);
  }
  
  public void markBlockRangeForRenderUpdate(int p_147458_1_, int p_147458_2_, int p_147458_3_, int p_147458_4_, int p_147458_5_, int p_147458_6_) {
    this.world.markBlockRangeForRenderUpdate(p_147458_1_, p_147458_2_, p_147458_3_, p_147458_4_, p_147458_5_, p_147458_6_);
  }
  
  public void notifyBlocksOfNeighborChange(int p_147459_1_, int p_147459_2_, int p_147459_3_, Block p_147459_4_) {
    this.world.notifyBlocksOfNeighborChange(p_147459_1_, p_147459_2_, p_147459_3_, p_147459_4_);
  }
  
  public void notifyBlocksOfNeighborChange(int p_147441_1_, int p_147441_2_, int p_147441_3_, Block p_147441_4_, int p_147441_5_) {
    this.world.notifyBlocksOfNeighborChange(p_147441_1_, p_147441_2_, p_147441_3_, p_147441_4_, p_147441_5_);
  }
  
  public void notifyBlockOfNeighborChange(int p_147460_1_, int p_147460_2_, int p_147460_3_, Block p_147460_4_) {
    this.world.notifyBlockOfNeighborChange(p_147460_1_, p_147460_2_, p_147460_3_, p_147460_4_);
  }
  
  public boolean isBlockTickScheduledThisTick(int p_147477_1_, int p_147477_2_, int p_147477_3_, Block p_147477_4_) {
    return this.world.isBlockTickScheduledThisTick(p_147477_1_, p_147477_2_, p_147477_3_, p_147477_4_);
  }
  
  public boolean canBlockSeeTheSky(int par1, int par2, int par3) {
    return this.world.canBlockSeeTheSky(par1, par2, par3);
  }
  
  public int getFullBlockLightValue(int par1, int par2, int par3) {
    return this.world.getFullBlockLightValue(par1, par2, par3);
  }
  
  public int getBlockLightValue(int par1, int par2, int par3) {
    return this.world.getBlockLightValue(par1, par2, par3);
  }
  
  public int getBlockLightValue_do(int par1, int par2, int par3, boolean par4) {
    return this.world.getBlockLightValue_do(par1, par2, par3, par4);
  }
  
  public int getHeightValue(int par1, int par2) {
    return this.world.getHeightValue(par1, par2);
  }
  
  public int getChunkHeightMapMinimum(int par1, int par2) {
    return this.world.getChunkHeightMapMinimum(par1, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
    return this.world.getSkyBlockTypeBrightness(par1EnumSkyBlock, par2, par3, par4);
  }
  
  public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
    return this.world.getSavedLightValue(par1EnumSkyBlock, par2, par3, par4);
  }
  
  public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5) {
    this.world.setLightValue(par1EnumSkyBlock, par2, par3, par4, par5);
  }
  
  public void func_147479_m(int p_147479_1_, int p_147479_2_, int p_147479_3_) {
    this.world.func_147479_m(p_147479_1_, p_147479_2_, p_147479_3_);
  }
  
  @SideOnly(Side.CLIENT)
  public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4) {
    return this.world.getLightBrightnessForSkyBlocks(par1, par2, par3, par4);
  }
  
  public float getLightBrightness(int par1, int par2, int par3) {
    return this.world.getLightBrightness(par1, par2, par3);
  }
  
  public boolean isDaytime() {
    return this.world.isDaytime();
  }
  
  public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3) {
    return this.world.rayTraceBlocks(par1Vec3, par2Vec3);
  }
  
  public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3) {
    return this.world.rayTraceBlocks(par1Vec3, par2Vec3, par3);
  }
  
  public MovingObjectPosition func_147447_a(Vec3 p_147447_1_, Vec3 p_147447_2_, boolean p_147447_3_, boolean p_147447_4_, boolean p_147447_5_) {
    return this.world.func_147447_a(p_147447_1_, p_147447_2_, p_147447_3_, p_147447_4_, p_147447_5_);
  }
  
  public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4) {
    this.world.playSoundAtEntity(par1Entity, par2Str, par3, par4);
  }
  
  public void playSoundToNearExcept(EntityPlayer par1EntityPlayer, String par2Str, float par3, float par4) {
    this.world.playSoundToNearExcept(par1EntityPlayer, par2Str, par3, par4);
  }
  
  public void playSoundEffect(double par1, double par3, double par5, String par7Str, float par8, float par9) {
    this.world.playSoundEffect(par1, par3, par5, par7Str, par8, par9);
  }
  
  public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10) {
    this.world.playSound(par1, par3, par5, par7Str, par8, par9, par10);
  }
  
  public void playRecord(String par1Str, int par2, int par3, int par4) {
    this.world.playRecord(par1Str, par2, par3, par4);
  }
  
  public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
    this.world.spawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
  }
  
  public boolean addWeatherEffect(Entity par1Entity) {
    return this.world.addWeatherEffect(par1Entity);
  }
  
  public boolean spawnEntityInWorld(Entity par1Entity) {
    return this.world.spawnEntityInWorld(par1Entity);
  }
  
  public void onEntityAdded(Entity par1Entity) {
    this.world.onEntityAdded(par1Entity);
  }
  
  public void onEntityRemoved(Entity par1Entity) {
    this.world.onEntityRemoved(par1Entity);
  }
  
  public void removeEntity(Entity par1Entity) {
    this.world.removeEntity(par1Entity);
  }
  
  public void removePlayerEntityDangerously(Entity par1Entity) {
    this.world.removePlayerEntityDangerously(par1Entity);
  }
  
  public void addWorldAccess(IWorldAccess par1IWorldAccess) {
    this.world.addWorldAccess(par1IWorldAccess);
  }
  
  public List getCollidingBoundingBoxes(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB) {
    return this.world.getCollidingBoundingBoxes(par1Entity, par2AxisAlignedBB);
  }
  
  public List func_147461_a(AxisAlignedBB p_147461_1_) {
    return this.world.func_147461_a(p_147461_1_);
  }
  
  public int calculateSkylightSubtracted(float par1) {
    return this.world.calculateSkylightSubtracted(par1);
  }
  
  public void removeWorldAccess(IWorldAccess par1IWorldAccess) {
    this.world.removeWorldAccess(par1IWorldAccess);
  }
  
  @SideOnly(Side.CLIENT)
  public float getSunBrightness(float par1) {
    return this.world.getSunBrightness(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getSkyColor(Entity par1Entity, float par2) {
    return this.world.getSkyColor(par1Entity, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getSkyColorBody(Entity par1Entity, float par2) {
    return this.world.getSkyColorBody(par1Entity, par2);
  }
  
  public float getCelestialAngle(float par1) {
    return this.world.getCelestialAngle(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public int getMoonPhase() {
    return this.world.getMoonPhase();
  }
  
  public float getCurrentMoonPhaseFactor() {
    return this.world.getCurrentMoonPhaseFactor();
  }
  
  public float getCelestialAngleRadians(float par1) {
    return this.world.getCelestialAngleRadians(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getCloudColour(float par1) {
    return this.world.getCloudColour(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 drawCloudsBody(float par1) {
    return this.world.drawCloudsBody(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public Vec3 getFogColor(float par1) {
    return this.world.getFogColor(par1);
  }
  
  public int getPrecipitationHeight(int par1, int par2) {
    return this.world.getPrecipitationHeight(par1, par2);
  }
  
  public int getTopSolidOrLiquidBlock(int par1, int par2) {
    return this.world.getTopSolidOrLiquidBlock(par1, par2);
  }
  
  @SideOnly(Side.CLIENT)
  public float getStarBrightness(float par1) {
    return this.world.getStarBrightness(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public float getStarBrightnessBody(float par1) {
    return this.world.getStarBrightnessBody(par1);
  }
  
  public void scheduleBlockUpdate(int p_147464_1_, int p_147464_2_, int p_147464_3_, Block p_147464_4_, int p_147464_5_) {
    this.world.scheduleBlockUpdate(p_147464_1_, p_147464_2_, p_147464_3_, p_147464_4_, p_147464_5_);
  }
  
  public void scheduleBlockUpdateWithPriority(int p_147454_1_, int p_147454_2_, int p_147454_3_, Block p_147454_4_, int p_147454_5_, int p_147454_6_) {
    this.world.scheduleBlockUpdateWithPriority(p_147454_1_, p_147454_2_, p_147454_3_, p_147454_4_, p_147454_5_, p_147454_6_);
  }
  
  public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_, int p_147446_6_) {
    this.world.func_147446_b(p_147446_1_, p_147446_2_, p_147446_3_, p_147446_4_, p_147446_5_, p_147446_6_);
  }
  
  public void updateEntities() {
    this.world.updateEntities();
  }
  
  public void func_147448_a(Collection p_147448_1_) {
    this.world.func_147448_a(p_147448_1_);
  }
  
  public void updateEntity(Entity par1Entity) {
    this.world.updateEntity(par1Entity);
  }
  
  public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2) {
    this.world.updateEntityWithOptionalForce(par1Entity, par2);
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB) {
    return this.world.checkNoEntityCollision(par1AxisAlignedBB);
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB, Entity par2Entity) {
    return this.world.checkNoEntityCollision(par1AxisAlignedBB, par2Entity);
  }
  
  public boolean checkBlockCollision(AxisAlignedBB par1AxisAlignedBB) {
    return this.world.checkBlockCollision(par1AxisAlignedBB);
  }
  
  public boolean isAnyLiquid(AxisAlignedBB par1AxisAlignedBB) {
    return this.world.isAnyLiquid(par1AxisAlignedBB);
  }
  
  public boolean func_147470_e(AxisAlignedBB p_147470_1_) {
    return this.world.func_147470_e(p_147470_1_);
  }
  
  public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity) {
    return this.world.handleMaterialAcceleration(par1AxisAlignedBB, par2Material, par3Entity);
  }
  
  public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material) {
    return this.world.isMaterialInBB(par1AxisAlignedBB, par2Material);
  }
  
  public boolean isAABBInMaterial(AxisAlignedBB par1AxisAlignedBB, Material par2Material) {
    return this.world.isAABBInMaterial(par1AxisAlignedBB, par2Material);
  }
  
  public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9) {
    return this.world.createExplosion(par1Entity, par2, par4, par6, par8, par9);
  }
  
  public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10) {
    return this.world.newExplosion(par1Entity, par2, par4, par6, par8, par9, par10);
  }
  
  public float getBlockDensity(Vec3 par1Vec3, AxisAlignedBB par2AxisAlignedBB) {
    return this.world.getBlockDensity(par1Vec3, par2AxisAlignedBB);
  }
  
  public boolean extinguishFire(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5) {
    return this.world.extinguishFire(par1EntityPlayer, par2, par3, par4, par5);
  }
  
  @SideOnly(Side.CLIENT)
  public String getDebugLoadedEntities() {
    return this.world.getDebugLoadedEntities();
  }
  
  @SideOnly(Side.CLIENT)
  public String getProviderName() {
    return this.world.getProviderName();
  }
  
  public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
    return this.world.getTileEntity(p_147438_1_, p_147438_2_, p_147438_3_);
  }
  
  public void setTileEntity(int p_147455_1_, int p_147455_2_, int p_147455_3_, TileEntity p_147455_4_) {
    this.world.setTileEntity(p_147455_1_, p_147455_2_, p_147455_3_, p_147455_4_);
  }
  
  public void removeTileEntity(int p_147475_1_, int p_147475_2_, int p_147475_3_) {
    this.world.removeTileEntity(p_147475_1_, p_147475_2_, p_147475_3_);
  }
  
  public void func_147457_a(TileEntity p_147457_1_) {
    this.world.func_147457_a(p_147457_1_);
  }
  
  public boolean func_147469_q(int p_147469_1_, int p_147469_2_, int p_147469_3_) {
    return this.world.func_147469_q(p_147469_1_, p_147469_2_, p_147469_3_);
  }
  
  public static boolean doesBlockHaveSolidTopSurface(IBlockAccess p_147466_0_, int p_147466_1_, int p_147466_2_, int p_147466_3_) {
    return World.doesBlockHaveSolidTopSurface(p_147466_0_, p_147466_1_, p_147466_2_, p_147466_3_);
  }
  
  public boolean isBlockNormalCubeDefault(int p_147445_1_, int p_147445_2_, int p_147445_3_, boolean p_147445_4_) {
    return this.world.isBlockNormalCubeDefault(p_147445_1_, p_147445_2_, p_147445_3_, p_147445_4_);
  }
  
  public void calculateInitialSkylight() {
    this.world.calculateInitialSkylight();
  }
  
  public void setAllowedSpawnTypes(boolean par1, boolean par2) {
    this.world.setAllowedSpawnTypes(par1, par2);
  }
  
  public void tick() {
    this.world.tick();
  }
  
  public void calculateInitialWeatherBody() {
    this.world.calculateInitialWeatherBody();
  }
  
  public void updateWeather() {
    this.world.provider.updateWeather();
  }
  
  public void updateWeatherBody() {
    this.world.updateWeatherBody();
  }
  
  public boolean isBlockFreezable(int par1, int par2, int par3) {
    return this.world.isBlockFreezable(par1, par2, par3);
  }
  
  public boolean isBlockFreezableNaturally(int par1, int par2, int par3) {
    return this.world.isBlockFreezableNaturally(par1, par2, par3);
  }
  
  public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4) {
    return this.world.canBlockFreeze(par1, par2, par3, par4);
  }
  
  public boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4) {
    return this.world.canBlockFreezeBody(par1, par2, par3, par4);
  }
  
  public boolean func_147478_e(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_) {
    return this.world.func_147478_e(p_147478_1_, p_147478_2_, p_147478_3_, p_147478_4_);
  }
  
  public boolean canSnowAtBody(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_) {
    return this.world.canSnowAtBody(p_147478_1_, p_147478_2_, p_147478_3_, p_147478_4_);
  }
  
  public boolean func_147451_t(int p_147451_1_, int p_147451_2_, int p_147451_3_) {
    return this.world.func_147451_t(p_147451_1_, p_147451_2_, p_147451_3_);
  }
  
  public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {
    return this.world.updateLightByType(p_147463_1_, p_147463_2_, p_147463_3_, p_147463_4_);
  }
  
  public boolean tickUpdates(boolean par1) {
    return this.world.tickUpdates(par1);
  }
  
  public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2) {
    return this.world.getPendingBlockUpdates(par1Chunk, par2);
  }
  
  public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB) {
    return this.world.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB);
  }
  
  public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3IEntitySelector) {
    return this.world.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB, par3IEntitySelector);
  }
  
  public List getEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB) {
    return this.world.getEntitiesWithinAABB(par1Class, par2AxisAlignedBB);
  }
  
  public List selectEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3IEntitySelector) {
    return this.world.selectEntitiesWithinAABB(par1Class, par2AxisAlignedBB, par3IEntitySelector);
  }
  
  public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity) {
    return this.world.findNearestEntityWithinAABB(par1Class, par2AxisAlignedBB, par3Entity);
  }
  
  public Entity getEntityByID(int var1) {
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public List getLoadedEntityList() {
    return this.world.getLoadedEntityList();
  }
  
  public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {
    this.world.markTileEntityChunkModified(p_147476_1_, p_147476_2_, p_147476_3_, p_147476_4_);
  }
  
  public int countEntities(Class par1Class) {
    return this.world.countEntities(par1Class);
  }
  
  public void addLoadedEntities(List par1List) {
    this.world.addLoadedEntities(par1List);
  }
  
  public void unloadEntities(List par1List) {
    this.world.unloadEntities(par1List);
  }
  
  public boolean canPlaceEntityOnSide(Block p_147472_1_, int p_147472_2_, int p_147472_3_, int p_147472_4_, boolean p_147472_5_, int p_147472_6_, Entity p_147472_7_, ItemStack p_147472_8_) {
    return this.world.canPlaceEntityOnSide(p_147472_1_, p_147472_2_, p_147472_3_, p_147472_4_, p_147472_5_, p_147472_6_, p_147472_7_, p_147472_8_);
  }
  
  public PathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7) {
    return this.world.getPathEntityToEntity(par1Entity, par2Entity, par3, par4, par5, par6, par7);
  }
  
  public PathEntity getEntityPathToXYZ(Entity par1Entity, int par2, int par3, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9) {
    return this.world.getEntityPathToXYZ(par1Entity, par2, par3, par4, par5, par6, par7, par8, par9);
  }
  
  public int isBlockProvidingPowerTo(int par1, int par2, int par3, int par4) {
    return this.world.isBlockProvidingPowerTo(par1, par2, par3, par4);
  }
  
  public int getBlockPowerInput(int par1, int par2, int par3) {
    return this.world.getBlockPowerInput(par1, par2, par3);
  }
  
  public boolean getIndirectPowerOutput(int par1, int par2, int par3, int par4) {
    return this.world.getIndirectPowerOutput(par1, par2, par3, par4);
  }
  
  public int getIndirectPowerLevelTo(int par1, int par2, int par3, int par4) {
    return this.world.getIndirectPowerLevelTo(par1, par2, par3, par4);
  }
  
  public boolean isBlockIndirectlyGettingPowered(int par1, int par2, int par3) {
    return this.world.isBlockIndirectlyGettingPowered(par1, par2, par3);
  }
  
  public int getStrongestIndirectPower(int par1, int par2, int par3) {
    return this.world.getStrongestIndirectPower(par1, par2, par3);
  }
  
  public EntityPlayer getClosestPlayerToEntity(Entity par1Entity, double par2) {
    return this.world.getClosestPlayerToEntity(par1Entity, par2);
  }
  
  public EntityPlayer getClosestPlayer(double par1, double par3, double par5, double par7) {
    return this.world.getClosestPlayer(par1, par3, par5, par7);
  }
  
  public EntityPlayer getClosestVulnerablePlayerToEntity(Entity par1Entity, double par2) {
    return this.world.getClosestVulnerablePlayerToEntity(par1Entity, par2);
  }
  
  public EntityPlayer getClosestVulnerablePlayer(double par1, double par3, double par5, double par7) {
    return this.world.getClosestVulnerablePlayer(par1, par3, par5, par7);
  }
  
  public EntityPlayer getPlayerEntityByName(String par1Str) {
    return this.world.getPlayerEntityByName(par1Str);
  }
  
  @SideOnly(Side.CLIENT)
  public void sendQuittingDisconnectingPacket() {
    this.world.sendQuittingDisconnectingPacket();
  }
  
  public void checkSessionLock() throws MinecraftException {
    this.world.checkSessionLock();
  }
  
  @SideOnly(Side.CLIENT)
  public void func_82738_a(long par1) {
    this.world.func_82738_a(par1);
  }
  
  public long getSeed() {
    return this.world.getSeed();
  }
  
  public long getTotalWorldTime() {
    return this.world.getTotalWorldTime();
  }
  
  public long getWorldTime() {
    return this.world.getWorldTime();
  }
  
  public void setWorldTime(long par1) {
    this.world.setWorldTime(par1);
  }
  
  public ChunkCoordinates getSpawnPoint() {
    return this.world.getSpawnPoint();
  }
  
  public void setSpawnLocation(int par1, int par2, int par3) {
    this.world.setSpawnLocation(par1, par2, par3);
  }
  
  @SideOnly(Side.CLIENT)
  public void joinEntityInSurroundings(Entity par1Entity) {
    this.world.joinEntityInSurroundings(par1Entity);
  }
  
  public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
    return this.world.canMineBlock(par1EntityPlayer, par2, par3, par4);
  }
  
  public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
    return this.world.canMineBlockBody(par1EntityPlayer, par2, par3, par4);
  }
  
  public void setEntityState(Entity par1Entity, byte par2) {
    this.world.setEntityState(par1Entity, par2);
  }
  
  public IChunkProvider getChunkProvider() {
    return this.world.getChunkProvider();
  }
  
  public void addBlockEvent(int p_147452_1_, int p_147452_2_, int p_147452_3_, Block p_147452_4_, int p_147452_5_, int p_147452_6_) {
    this.world.addBlockEvent(p_147452_1_, p_147452_2_, p_147452_3_, p_147452_4_, p_147452_5_, p_147452_6_);
  }
  
  public ISaveHandler getSaveHandler() {
    return this.world.getSaveHandler();
  }
  
  public WorldInfo getWorldInfo() {
    return this.world.getWorldInfo();
  }
  
  public GameRules getGameRules() {
    return this.world.getGameRules();
  }
  
  public void updateAllPlayersSleepingFlag() {
    this.world.updateAllPlayersSleepingFlag();
  }
  
  public float getWeightedThunderStrength(float par1) {
    return this.world.getWeightedThunderStrength(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public void setThunderStrength(float p_147442_1_) {
    this.world.setThunderStrength(p_147442_1_);
  }
  
  public float getRainStrength(float par1) {
    return this.world.getRainStrength(par1);
  }
  
  @SideOnly(Side.CLIENT)
  public void setRainStrength(float par1) {
    this.world.setRainStrength(par1);
  }
  
  public boolean isThundering() {
    return this.world.isThundering();
  }
  
  public boolean isRaining() {
    return this.world.isRaining();
  }
  
  public boolean canLightningStrikeAt(int par1, int par2, int par3) {
    return this.world.canLightningStrikeAt(par1, par2, par3);
  }
  
  public boolean isBlockHighHumidity(int par1, int par2, int par3) {
    return this.world.isBlockHighHumidity(par1, par2, par3);
  }
  
  public void setItemData(String par1Str, WorldSavedData par2WorldSavedData) {
    this.world.setItemData(par1Str, par2WorldSavedData);
  }
  
  public WorldSavedData loadItemData(Class par1Class, String par2Str) {
    return this.world.loadItemData(par1Class, par2Str);
  }
  
  public int getUniqueDataId(String par1Str) {
    return this.world.getUniqueDataId(par1Str);
  }
  
  public void playBroadcastSound(int par1, int par2, int par3, int par4, int par5) {
    this.world.playBroadcastSound(par1, par2, par3, par4, par5);
  }
  
  public void playAuxSFX(int par1, int par2, int par3, int par4, int par5) {
    this.world.playAuxSFX(par1, par2, par3, par4, par5);
  }
  
  public void playAuxSFXAtEntity(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6) {
    this.world.playAuxSFXAtEntity(par1EntityPlayer, par2, par3, par4, par5, par6);
  }
  
  public int getHeight() {
    return this.world.getHeight();
  }
  
  public int getActualHeight() {
    return this.world.getActualHeight();
  }
  
  public Random setRandomSeed(int par1, int par2, int par3) {
    return this.world.setRandomSeed(par1, par2, par3);
  }
  
  public ChunkPosition findClosestStructure(String p_147440_1_, int p_147440_2_, int p_147440_3_, int p_147440_4_) {
    return this.world.findClosestStructure(p_147440_1_, p_147440_2_, p_147440_3_, p_147440_4_);
  }
  
  @SideOnly(Side.CLIENT)
  public boolean extendedLevelsInChunkCache() {
    return this.world.extendedLevelsInChunkCache();
  }
  
  @SideOnly(Side.CLIENT)
  public double getHorizon() {
    return this.world.getHorizon();
  }
  
  public CrashReportCategory addWorldInfoToCrashReport(CrashReport par1CrashReport) {
    return this.world.addWorldInfoToCrashReport(par1CrashReport);
  }
  
  public void destroyBlockInWorldPartially(int p_147443_1_, int p_147443_2_, int p_147443_3_, int p_147443_4_, int p_147443_5_) {
    this.world.destroyBlockInWorldPartially(p_147443_1_, p_147443_2_, p_147443_3_, p_147443_4_, p_147443_5_);
  }
  
  public Calendar getCurrentDate() {
    return this.world.getCurrentDate();
  }
  
  @SideOnly(Side.CLIENT)
  public void makeFireworks(double par1, double par3, double par5, double par7, double par9, double par11, NBTTagCompound par13NBTTagCompound) {
    this.world.makeFireworks(par1, par3, par5, par7, par9, par11, par13NBTTagCompound);
  }
  
  public Scoreboard getScoreboard() {
    return this.world.getScoreboard();
  }
  
  public void func_147453_f(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_) {
    this.world.func_147453_f(p_147453_1_, p_147453_2_, p_147453_3_, p_147453_4_);
  }
  
  public float func_147462_b(double p_147462_1_, double p_147462_3_, double p_147462_5_) {
    return this.world.func_147462_b(p_147462_1_, p_147462_3_, p_147462_5_);
  }
  
  public float func_147473_B(int p_147473_1_, int p_147473_2_, int p_147473_3_) {
    return this.world.func_147473_B(p_147473_1_, p_147473_2_, p_147473_3_);
  }
  
  public void func_147450_X() {
    this.world.func_147450_X();
  }
  
  public void addTileEntity(TileEntity entity) {
    this.world.addTileEntity(entity);
  }
  
  public boolean isSideSolid(int x, int y, int z, ForgeDirection side) {
    return this.world.isSideSolid(x, y, z, side);
  }
  
  public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
    return this.world.isSideSolid(x, y, z, side, _default);
  }
  
  public ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunks() {
    return this.world.getPersistentChunks();
  }
  
  public int getBlockLightOpacity(int x, int y, int z) {
    return this.world.getBlockLightOpacity(x, y, z);
  }
  
  public int countEntities(EnumCreatureType type, boolean forSpawnCount) {
    return this.world.countEntities(type, forSpawnCount);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\fakeplayer\FakeWorldWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */