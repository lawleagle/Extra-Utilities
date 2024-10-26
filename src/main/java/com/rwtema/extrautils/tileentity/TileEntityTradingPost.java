package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketVillager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.village.MerchantRecipeList;

public class TileEntityTradingPost extends TileEntity {
  public static int maxRange = 32;
  
  public int[] ids = null;
  
  public MerchantRecipeList[] data = null;
  
  public boolean canUpdate() {
    return false;
  }
  
  public AxisAlignedBB getAABB() {
    return AxisAlignedBB.getBoundingBox(this.xCoord + 0.5D - maxRange, 0.0D, this.zCoord + 0.5D - maxRange, this.xCoord + 0.5D + maxRange, 255.0D, this.zCoord + 0.5D + maxRange);
  }
  
  public List<IMerchant> getVillagers() {
    List t = this.worldObj.getEntitiesWithinAABB(IMerchant.class, getAABB());
    List<IMerchant> traders = new ArrayList<IMerchant>();
    for (Object aT : t) {
      if (isValidVillager((IMerchant)aT, true))
        traders.add((IMerchant)aT); 
    } 
    return traders;
  }
  
  public boolean isValidVillager(IMerchant villager, boolean locationAlreadyChecked) {
    return (villager instanceof EntityLiving && !((EntityLiving)villager).isChild() && (locationAlreadyChecked || getAABB().isVecInside(Vec3.createVectorHelper(((EntityLiving)villager).posX, ((EntityLiving)villager).posY, ((EntityLiving)villager).posZ))));
  }
  
  public XUPacketBase getTradePacket(EntityPlayer player) {
    List<IMerchant> traders = getVillagers();
    if (traders == null || traders.size() == 0)
      return null; 
    NBTTagCompound pkt = new NBTTagCompound();
    int n = 0;
    pkt.setInteger("player_id", player.getEntityId());
    for (int i = 0; i < traders.size(); i++) {
      IMerchant v = traders.get(i);
      pkt.setInteger("i" + i, ((EntityLiving)v).getEntityId());
      pkt.setTag("t" + i, (NBTBase)v.getRecipes(null).getRecipiesAsTags());
      n++;
    } 
    if (n == 0)
      return null; 
    pkt.setInteger("n", n);
    return (XUPacketBase)new PacketVillager(this.xCoord, this.yCoord, this.zCoord, pkt);
  }
  
  public double distSq(double x, double y, double z) {
    x -= this.xCoord + 0.5D;
    y -= this.yCoord + 0.5D;
    z -= this.zCoord + 0.5D;
    return x * x + y * y + z * z;
  }
  
  public int toInt(Object x) {
    if (x instanceof Double)
      return (int)Math.floor(((Double)x).doubleValue()); 
    if (x instanceof Float)
      return (int)Math.floor(((Float)x).floatValue()); 
    if (x instanceof Integer)
      return ((Integer)x).intValue(); 
    if (x instanceof String)
      return Integer.parseInt((String)x); 
    return 0;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityTradingPost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */