package com.rwtema.extrautils.command;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.nei.InfoData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

public class CommandDumpLocalization extends CommandBase {
  public String getCommandName() {
    return "dumplocalization";
  }
  
  public String getCommandUsage(ICommandSender icommandsender) {
    return "/dumplocalization";
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    File f = new File((Minecraft.getMinecraft()).mcDataDir, "extrautilities_localization.txt");
    Map<String, Properties> k = (Map<String, Properties>)ReflectionHelper.getPrivateValue(LanguageRegistry.class, LanguageRegistry.instance(), new String[] { "modLanguageData" });
    String lang = FMLCommonHandler.instance().getCurrentLanguage();
    Properties p = k.get(lang);
    String t = "";
    for (Map.Entry<Object, Object> entry : p.entrySet())
      t = t + entry.getKey() + "=" + entry.getValue() + "\n"; 
    try {
      Files.write(t, f, Charsets.UTF_8);
      LogHelper.info("Dumped Language data to " + f.getAbsolutePath(), new Object[0]);
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
      if (arg0.isBlock && !arg1.isBlock)
        return -1; 
      if (arg1.isBlock && !arg0.isBlock)
        return 1; 
      return arg0.name.compareTo(arg1.name);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandDumpLocalization.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */