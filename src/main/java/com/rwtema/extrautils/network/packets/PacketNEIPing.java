package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PacketNEIPing extends XUPacketBase {
  private ItemStack itemStack;
  
  private EntityPlayer player;
  
  public PacketNEIPing() {}
  
  public PacketNEIPing(ItemStack itemStack) {
    this.itemStack = itemStack;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    writeItemStack(data, this.itemStack);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.player = player;
    this.itemStack = readItemStack(data);
  }
  
  static WeakHashMap<EntityPlayer, Long> timeOutsHandler = new WeakHashMap<EntityPlayer, Long>();
  
  static final long TIMEOUT = 10L;
  
  static final int RANGE = 16;
  
  public void doStuffServer(ChannelHandlerContext ctx) {
    if (this.player == null || this.itemStack == null || this.itemStack.getItem() == null)
      return; 
    World world = this.player.worldObj;
    long time = world.getTotalWorldTime();
    Long aLong = timeOutsHandler.get(this.player);
    if (aLong != null && 
      time - aLong.longValue() < 10L)
      return; 
    timeOutsHandler.put(this.player, Long.valueOf(time));
    final int x = (int)Math.round(this.player.posX);
    final int y = (int)Math.round(this.player.posY);
    final int z = (int)Math.round(this.player.posZ);
    Item trueItem = this.itemStack.getItem();
    int trueItemDamage = this.itemStack.getItemDamage();
    TreeSet<ChunkPosition> positions = new TreeSet<ChunkPosition>(new Comparator<ChunkPosition>() {
          public int compare(ChunkPosition o1, ChunkPosition o2) {
            return Double.compare(PacketNEIPing.this.getRange(x, y, z, o1), PacketNEIPing.this.getRange(x, y, z, o2));
          }
        });
    for (int cx = x - 16; cx <= x + 16; cx += 16) {
      for (int cz = z - 16; cz <= z + 16; cz += 16) {
        if (world.blockExists(cx, y, cz)) {
          Chunk chunk = world.getChunkFromBlockCoords(cx, cz);
          Set<Map.Entry<ChunkPosition, Object>> entrySet = chunk.chunkTileEntityMap.entrySet();
          for (Map.Entry<ChunkPosition, Object> entry : entrySet) {
            ChunkPosition e = entry.getKey();
            ChunkPosition key = new ChunkPosition(chunk.xPosition * 16 + e.chunkPosX, e.chunkPosY, chunk.zPosition * 16 + e.chunkPosZ);
            if (!inRange(x, y, z, key))
              continue; 
            Object value = entry.getValue();
            if (!(value instanceof IInventory))
              continue; 
            IInventory inv = (IInventory)value;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
              ItemStack stackInSlot = inv.getStackInSlot(i);
              if (stackInSlot != null && 
                stackInSlot.getItem() == trueItem)
                if (!trueItem.getHasSubtypes() || stackInSlot.getItemDamage() == trueItemDamage) {
                  positions.add(key);
                  if (positions.size() >= 20)
                    positions.pollLast(); 
                  break;
                }  
            } 
          } 
        } 
      } 
    } 
    if (!positions.isEmpty())
      NetworkHandler.sendPacketToPlayer(new PacketNEIPong(new ArrayList<ChunkPosition>(positions)), this.player); 
  }
  
  public int getRange(int x, int y, int z, ChunkPosition pos) {
    return Math.abs(pos.chunkPosX - x) + Math.abs(pos.chunkPosY - y) + Math.abs(pos.chunkPosZ - z);
  }
  
  public boolean inRange(int x, int y, int z, ChunkPosition pos) {
    return (Math.abs(pos.chunkPosX - x) <= 16 && Math.abs(pos.chunkPosY - y) <= 16 && Math.abs(pos.chunkPosZ - z) <= 16);
  }
  
  public void doStuffClient() {}
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return properSenderSide.isClient();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketNEIPing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */