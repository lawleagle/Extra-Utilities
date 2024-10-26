package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.IRandomDisplayTick;
import codechicken.multipart.minecraft.McMetaPart;
import com.rwtema.extrautils.EventHandlerServer;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.IAntiMobTorch;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class MagnumTorchPart extends McMetaPart implements IRandomDisplayTick, IAntiMobTorch {
  public Cuboid6 getBounds() {
    return new Cuboid6(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
  }
  
  public Block getBlock() {
    return ExtraUtils.magnumTorch;
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    ArrayList<Cuboid6> t = new ArrayList();
    t.add(getBounds());
    return t;
  }
  
  public String getType() {
    return "XU|MagnumTorch";
  }
  
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(Random random) {
    ExtraUtils.magnumTorch.randomDisplayTick(tile().getWorldObj(), x(), y(), z(), random);
  }
  
  public void onWorldJoin() {
    int[] myCoord = TileEntityAntiMobTorch.getCoord((TileEntity)tile());
    for (int i = 0; i < EventHandlerServer.magnumTorchRegistry.size(); i++) {
      int[] coord = EventHandlerServer.magnumTorchRegistry.get(i);
      if (myCoord[0] == coord[0] && myCoord[1] == coord[1] && myCoord[2] == coord[2] && myCoord[3] == coord[3])
        return; 
    } 
    EventHandlerServer.magnumTorchRegistry.add(myCoord);
  }
  
  public void onWorldSeparate() {
    int[] myCoord = { (getWorld()).provider.dimensionId, x(), y(), z() };
    EventHandlerServer.magnumTorchRegistry.remove(myCoord);
  }
  
  public float getHorizontalTorchRangeSquared() {
    return 16384.0F;
  }
  
  public float getVerticalTorchRangeSquared() {
    return 1024.0F;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\MagnumTorchPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */