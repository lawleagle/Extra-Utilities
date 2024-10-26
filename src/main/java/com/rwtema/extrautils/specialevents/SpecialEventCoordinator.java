package com.rwtema.extrautils.specialevents;

import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.particle.Particles;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;

public class SpecialEventCoordinator {
  public final String maxHealthName = SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName();
  
  public final int CAP = 2250;
  
  public final String soulDranTag = "XU_SoulDrain";
  
  public static final XURandom random = XURandom.getInstance();
  
  public void init() {
    FMLCommonHandler.instance().bus().register(this);
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  public TObjectIntHashMap<ChunkLocation> chunkmap = new TObjectIntHashMap(10, 0.5F, 0);
  
  @SubscribeEvent
  public void getChunkData(ChunkDataEvent.Save event) {
    Chunk chunk = event.getChunk();
    int i = this.chunkmap.get(new ChunkLocation(chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition));
    if (i != 0)
      event.getData().setInteger("XU_SoulDrain", i); 
  }
  
  @SubscribeEvent
  public void getChunkData(ChunkDataEvent.Load event) {
    int i = event.getData().getInteger("XU_SoulDrain");
    if (i == 0)
      return; 
    Chunk chunk = event.getChunk();
    this.chunkmap.put(new ChunkLocation(chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition), i);
  }
  
  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase == TickEvent.Phase.START)
      return; 
    EntityPlayer player = event.player;
    if (player.worldObj.isRemote)
      return; 
    if (LogHelper.isDeObf);
    if ((player.worldObj.getTotalWorldTime() & 0x1FL) > 0L)
      return; 
    int i = this.chunkmap.adjustOrPutValue(new ChunkLocation(event.player), 1, 1);
    if (i > 2250);
  }
  
  private final class ChunkLocation {
    final int dim;
    
    final int x;
    
    final int z;
    
    public ChunkLocation(EntityPlayer player) {
      this(player.worldObj.provider.dimensionId, (int)player.posX >> 4, (int)player.posZ >> 4);
    }
    
    public String toString() {
      return "ChunkLocation{x=" + this.x + ", z=" + this.z + '}';
    }
    
    private ChunkLocation(int dim, int x, int z) {
      this.dim = dim;
      this.x = x;
      this.z = z;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true; 
      if (o == null || getClass() != o.getClass())
        return false; 
      ChunkLocation that = (ChunkLocation)o;
      return (this.dim == that.dim && this.x == that.x && this.z == that.z);
    }
    
    public int hashCode() {
      int result = this.dim;
      result = 31 * result + this.x;
      result = 31 * result + this.z;
      return result;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\specialevents\SpecialEventCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */