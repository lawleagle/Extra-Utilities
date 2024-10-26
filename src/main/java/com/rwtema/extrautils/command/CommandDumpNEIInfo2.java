package com.rwtema.extrautils.command;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.nei.InfoData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class CommandDumpNEIInfo2 extends CommandBase {
  public String getCommandName() {
    return "dumpneidocs2";
  }
  
  public String getCommandUsage(ICommandSender icommandsender) {
    return "/dumpneidocs2";
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    File f = new File((Minecraft.getMinecraft()).mcDataDir, "en_US_doc.lang");
    String t = "";
    Collections.sort(InfoData.data, cmpData.instance);
    for (InfoData data : InfoData.data) {
      String id;
      if (data.precise) {
        id = data.item.getUnlocalizedName();
      } else {
        id = data.item.getItem().getUnlocalizedName();
      } 
      t = t + "doc." + id + ".name=" + data.name + "\n";
      if (data.info.length == 1) {
        t = t + "doc." + id + ".info=" + data.info[0].replace('\n', ' ') + "\n";
        continue;
      } 
      for (int i = 0; i < data.info.length; i++)
        t = t + "doc." + id + ".info." + i + "=" + data.info[i].replace('\n', ' ') + "\n"; 
    } 
    try {
      Files.write(t, f, Charsets.UTF_8);
      LogHelper.info("Dumped Extra Utilities NEI info data to " + f.getAbsolutePath(), new Object[0]);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public static class cmpData implements Comparator<InfoData> {
    public static cmpData instance = new cmpData();
    
    public static String getItem(ItemStack i) {
      ItemStack t = new ItemStack(i.getItem(), 1, 0);
      return t.getDisplayName();
    }
    
    public int compare(InfoData arg0, InfoData arg1) {
      return arg0.item.getUnlocalizedName().compareTo(arg1.item.getUnlocalizedName());
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandDumpNEIInfo2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */