package com.rwtema.extrautils.command;

import java.util.HashMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandKillMostCommonEntities extends CommandKillEntities {
  public CommandKillMostCommonEntities() {
    super("common", null, false);
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    int mx = -1;
    this.entityclass = null;
    HashMap<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
    for (WorldServer world : (MinecraftServer.getServer()).worldServers) {
      for (int i = 0; i < world.loadedEntityList.size(); i++) {
        Class<?> clazz = ((Entity)world.loadedEntityList.get(i)).getClass();
        Integer j = map.get(clazz);
        if (j == null)
          j = Integer.valueOf(0); 
        Integer integer1 = j, integer2 = j = Integer.valueOf(j.intValue() + 1);
        if (j.intValue() > mx) {
          mx = j.intValue();
          this.entityclass = (Class)clazz;
        } 
        map.put(clazz, j);
      } 
    } 
    if (this.entityclass == null)
      return; 
    super.processCommand(icommandsender, astring);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandKillMostCommonEntities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */