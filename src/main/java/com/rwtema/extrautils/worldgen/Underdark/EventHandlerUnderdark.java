package com.rwtema.extrautils.worldgen.Underdark;

import com.rwtema.extrautils.EventHandlerServer;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;

public class EventHandlerUnderdark {
  public static Random rand = (Random)XURandom.getInstance();
  
  @SubscribeEvent
  public void noMobs(LivingSpawnEvent.CheckSpawn event) {
    if (event.getResult() == Event.Result.DEFAULT && 
      event.world.provider.dimensionId == ExtraUtils.underdarkDimID && 
      event.entity instanceof EntityMob)
      if (rand.nextDouble() < Math.min(0.95D, event.entity.posY / 80.0D)) {
        event.setResult(Event.Result.DENY);
      } else {
        IAttributeInstance t = ((EntityMob)event.entity).getEntityAttribute(SharedMonsterAttributes.maxHealth);
        t.setBaseValue(t.getBaseValue() * 2.0D);
        ((EntityMob)event.entity).heal((float)t.getAttributeValue());
        t = ((EntityMob)event.entity).getEntityAttribute(SharedMonsterAttributes.attackDamage);
        t.setBaseValue(t.getBaseValue() * 2.0D);
        if (!EventHandlerServer.isInRangeOfTorch(event.entity) && 
          event.entityLiving.worldObj.checkNoEntityCollision(event.entityLiving.boundingBox) && event.entityLiving.worldObj.getCollidingBoundingBoxes((Entity)event.entityLiving, event.entityLiving.boundingBox).isEmpty() && !event.entityLiving.worldObj.isAnyLiquid(event.entityLiving.boundingBox))
          event.setResult(Event.Result.ALLOW); 
      }  
  }
  
  @SubscribeEvent
  public void preventDoubleDecor(DecorateBiomeEvent.Decorate decor) {}
  
  @SubscribeEvent
  public void noDirt(OreGenEvent.GenerateMinable event) {
    if (event.world.provider.dimensionId == ExtraUtils.underdarkDimID)
      switch (event.type) {
        case DIRT:
        case GRAVEL:
          event.setResult(Event.Result.DENY);
          break;
      }  
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\Underdark\EventHandlerUnderdark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */