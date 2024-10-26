package com.rwtema.extrautils.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
public class CommandUUID extends CommandBase {
  public String getCommandName() {
    return "uuid";
  }
  
  public String getCommandUsage(ICommandSender var1) {
    return "/uuid";
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
    return true;
  }
  
  public void processCommand(ICommandSender var1, String[] var2) {
    var1.addChatMessage((IChatComponent)new ChatComponentText("Username: " + Minecraft.getMinecraft().getSession().func_148256_e().getName() + " UUID: " + Minecraft.getMinecraft().getSession().func_148256_e().getId()));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandUUID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */