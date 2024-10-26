package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.ChunkPos;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TileEnderPillar extends TileEntity {
  public static final int transmitLimit = 10;
  
  public static int range = 10;
  
  public static Random rand = (Random)XURandom.getInstance();
  
  public LinkedHashSet<ChunkPos> targets = new LinkedHashSet<ChunkPos>();
  
  int coolDown = 0;
  
  boolean init = false;
  
  public boolean shouldRefresh(Block oldID, Block newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
    if (oldID == newID) {
      if (((oldMeta > 0) ? true : false) != ((newMeta > 0) ? true : false));
      return false;
    } 
  }
  
  public void getNearbyTiles() {
    this.init = true;
    this.targets.clear();
    for (int x = this.xCoord - range >> 4 << 4; x < (this.xCoord + range >> 4 << 4) + 16; x += 16) {
      for (int z = this.zCoord - range >> 4 << 4; z < (this.zCoord + range >> 4 << 4) + 16; z += 16) {
        if (this.worldObj.blockExists(x, 100, z)) {
          Chunk chunk = this.worldObj.getChunkFromBlockCoords(x, z);
          for (Object obj : chunk.chunkTileEntityMap.values()) {
            TileEntity tile = (TileEntity)obj;
            if (Math.abs(tile.xCoord - this.xCoord) + Math.abs(tile.yCoord - this.yCoord) + Math.abs(tile.zCoord - this.zCoord) < range && 
              tile instanceof IEnderFluxHandler)
              this.targets.add(new ChunkPos(tile.xCoord, tile.yCoord, tile.zCoord)); 
          } 
        } 
      } 
    } 
  }
  
  public void updateEntity() {
    if (!this.init || this.worldObj.getTotalWorldTime() % 40L == 0L)
      getNearbyTiles(); 
    boolean sent = false;
    if (this.targets.size() > 0 && ((
      this.worldObj.isRemote && getBlockMetadata() == 3) || !this.worldObj.isRemote)) {
      Iterator<ChunkPos> iterator = this.targets.iterator();
      while (iterator.hasNext()) {
        ChunkPos c = iterator.next();
        TileEntity tile;
        if (this.worldObj.blockExists(c.x, c.y, c.z) && tile = this.worldObj.getTileEntity(c.x, c.y, c.z) instanceof IEnderFluxHandler) {
          if (((IEnderFluxHandler)tile).isActive()) {
            if (this.worldObj.isRemote) {
              double f = 0.5D;
              for (int i = 0; i < 1; i++) {
                double dx = c.x + f / 2.0D + (1.0D - f) * rand.nextDouble();
                double dy = c.y + f / 2.0D + (1.0D - f) * rand.nextDouble();
                double dz = c.z + f / 2.0D + (1.0D - f) * rand.nextDouble();
                double dx2 = this.xCoord + f / 2.0D + (1.0D - f) * rand.nextDouble();
                double dy2 = this.yCoord + f / 2.0D + (1.0D - f) * rand.nextDouble() - 0.5D;
                double dz2 = this.zCoord + f / 2.0D + (1.0D - f) * rand.nextDouble();
                this.worldObj.spawnParticle("portal", dx, dy, dz, dx2 - dx, dy2 - dy, dz2 - dz);
              } 
              continue;
            } 
            int a = ((IEnderFluxHandler)tile).recieveEnergy(10, Transfer.PERFORM);
            if (a > 0)
              sent = true; 
          } 
          continue;
        } 
        iterator.remove();
      } 
    } 
    if (!this.worldObj.isRemote) {
      if (sent) {
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 3, 2);
        this.coolDown = 20;
      } 
      if (this.coolDown > 0) {
        this.coolDown--;
        if (this.coolDown == 0)
          this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 2); 
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\TileEnderPillar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */