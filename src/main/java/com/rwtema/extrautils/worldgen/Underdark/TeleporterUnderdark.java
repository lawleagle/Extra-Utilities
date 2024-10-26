package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class TeleporterUnderdark extends Teleporter {
  private final WorldServer worldServerInstance;
  
  public TeleporterUnderdark(WorldServer par1WorldServer) {
    super(par1WorldServer);
    this.worldServerInstance = par1WorldServer;
  }
  
  public void placeInPortal(Entity entity, double x, double y, double z, float r) {
    if (!placeInExistingPortal(entity, x, y, z, r))
      if (this.worldServerInstance.provider.dimensionId != ExtraUtils.underdarkDimID) {
        y = this.worldServerInstance.getTopSolidOrLiquidBlock((int)x, (int)z);
        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
      } else {
        makePortal(entity);
      }  
  }
  
  public TileEntity findPortalInChunk(double x, double z) {
    Chunk chunk = this.worldServerInstance.getChunkFromBlockCoords((int)x, (int)z);
    for (Object tile : chunk.chunkTileEntityMap.values()) {
      if (tile instanceof com.rwtema.extrautils.tileentity.TileEntityPortal)
        return (TileEntity)tile; 
    } 
    return null;
  }
  
  public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float r) {
    TileEntity destPortal = null;
    for (int s = 0; s <= 5 && destPortal == null; s++) {
      for (int dx = -s; dx <= s; dx++) {
        for (int dz = -s; dz <= s; dz++) {
          if (destPortal == null)
            destPortal = findPortalInChunk(x + (dx * 16), z + (dz * 16)); 
        } 
      } 
    } 
    if (destPortal != null) {
      entity.setLocationAndAngles(destPortal.xCoord + 0.5D, (destPortal.yCoord + 1), destPortal.zCoord + 0.5D, entity.rotationYaw, entity.rotationPitch);
      entity.motionX = entity.motionY = entity.motionZ = 0.0D;
      return true;
    } 
    return false;
  }
  
  public boolean makePortal(Entity entity) {
    int ex = MathHelper.floor_double(entity.posX);
    int ey = MathHelper.floor_double(entity.posY) - 1;
    int ez = MathHelper.floor_double(entity.posZ);
    ey /= 5;
    ey += 139;
    if (ey > 247)
      ey = 247; 
    for (int x = -3; x <= 3; x++) {
      for (int z = -3; z <= 3; z++) {
        for (int y = -2; y <= 4; y++) {
          if (x == 0 && y == -1 && z == 0) {
            this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, ExtraUtils.portal, 1, 2);
            this.worldServerInstance.scheduleBlockUpdate(ex + x, ey + y, ez + z, ExtraUtils.portal, 1);
          } else if (x == -3 || x == 3 || y <= -1 || y == 4 || z == -3 || z == 3) {
            this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.cobblestone);
          } else if (y == 0 && (x == 2 || x == -2 || z == 2 || z == -2)) {
            this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.torch, 5, 3);
          } else {
            this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, Blocks.air);
          } 
        } 
      } 
    } 
    entity.setLocationAndAngles(ex + 0.5D, ey, ez + 0.5D, entity.rotationYaw, 0.0F);
    entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    return true;
  }
  
  public void removeStalePortalLocations(long par1) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\TeleporterUnderdark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */