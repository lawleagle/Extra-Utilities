package com.rwtema.extrautils.command;

import com.rwtema.extrautils.EventHandlerClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

@SideOnly(Side.CLIENT)
public class CommandHologram extends CommandBase {
  public String getCommandName() {
    return "xu_holo";
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
    return true;
  }
  
  public String getCommandUsage(ICommandSender icommandsender) {
    return "/xu_holo <playername>";
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    if (EventHandlerClient.holograms.contains(astring[0])) {
      EventHandlerClient.holograms.remove(astring[0]);
    } else {
      EventHandlerClient.holograms.add(astring[0]);
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandHologram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */