package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.damgesource.DamageSourceDarkness;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class DarknessTickHandler {
  public static float maxDarkTime = 100.0F;
  
  public static Random random = (Random)XURandom.getInstance();
  
  public static int maxLevel = 20 + random.nextInt(120);
  
  @SubscribeEvent
  public void tickStart(TickEvent.PlayerTickEvent event) {
    if (event.phase == TickEvent.Phase.START && !event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP)event.player;
      if (!player.worldObj.isRemote && player.worldObj.provider.dimensionId == ExtraUtils.underdarkDimID && player.worldObj.getTotalWorldTime() % 10L == 0L) {
        int time = 0;
        if (player.getEntityData().hasKey("XU|DarkTimer"))
          time = player.getEntityData().getInteger("XU|DarkTimer"); 
        if (player.getBrightness(1.0F) < 0.03D) {
          if (time > 100) {
            player.attackEntityFrom((DamageSource)DamageSourceDarkness.darkness, 1.0F);
          } else {
            time++;
          } 
        } else if (time > 0) {
          time--;
        } 
        player.getEntityData().setInteger("XU|DarkTimer", time);
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\DarknessTickHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */