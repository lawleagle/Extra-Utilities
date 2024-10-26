package com.rwtema.extrautils.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class CommandKillEntities extends CommandBase {
  private final String type;
  
  private final boolean except;
  
  protected Class<? extends Entity> entityclass;
  
  protected int numKills = 0;
  
  public CommandKillEntities(String type, Class<? extends Entity> entityclass, boolean except) {
    this.type = type;
    this.entityclass = entityclass;
    this.except = except;
  }
  
  public String getCommandName() {
    return "xu_kill" + this.type;
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender icommandsender) {
    return "/xu_kill" + this.type;
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    this.numKills = 0;
    for (int j = 0; j < (MinecraftServer.getServer()).worldServers.length; j++)
      killEntities((World)(MinecraftServer.getServer()).worldServers[j]); 
    icommandsender.addChatMessage((IChatComponent)new ChatComponentText("Killed " + this.numKills + " of type " + this.entityclass.getName()));
  }
  
  public void killEntities(World world) {
    for (int i = 0; i < world.loadedEntityList.size(); i++) {
      if (this.entityclass.isInstance(world.loadedEntityList.get(i)) == this.except) {
        this.numKills++;
        ((Entity)world.loadedEntityList.get(i)).setDead();
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandKillEntities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */