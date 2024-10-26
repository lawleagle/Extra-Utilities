package com.rwtema.extrautils.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class CommandKillNonPersistant extends CommandKillEntities {
  public CommandKillNonPersistant(String type, Class<? extends Entity> entityclass, boolean except) {
    super("despawns", null, true);
  }
  
  public void killEntities(World world) {
    for (int i = 0; i < world.loadedEntityList.size(); i++) {
      if (!((EntityLiving)world.loadedEntityList.get(i)).isNoDespawnRequired()) {
        this.numKills++;
        ((Entity)world.loadedEntityList.get(i)).setDead();
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandKillNonPersistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */