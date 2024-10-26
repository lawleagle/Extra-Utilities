package com.rwtema.extrautils.command;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.nei.InfoData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class CommandDumpNEIInfo extends CommandBase {
  public String getCommandName() {
    return "dumpneidocs";
  }
  
  public String getCommandUsage(ICommandSender icommandsender) {
    return "/dumpneidocs";
  }
  
  public void processCommand(ICommandSender icommandsender, String[] astring) {
    TextureMap tex = Minecraft.getMinecraft().getTextureMapBlocks();
    Map map = (Map)ReflectionHelper.getPrivateValue(TextureMap.class, tex, new String[] { "mapRegisteredSprites" });
    for (Object o : map.values()) {
      TextureAtlasSprite sprite = (TextureAtlasSprite)o;
      if (sprite.getIconWidth() < 16 || sprite.getIconHeight() < 16)
        LogHelper.debug(sprite.getIconName(), new Object[0]); 
    } 
    File f = new File((Minecraft.getMinecraft()).mcDataDir, "extrautilitiesnei.txt");
    String t = "";
    Collections.sort(InfoData.data, cmpData.instance);
    boolean blocks = true;
    t = "[spoiler='New Blocks']\n";
    for (InfoData data : InfoData.data) {
      if (blocks && !data.isBlock) {
        blocks = false;
        t = t + "[/spoiler]\n\n";
        t = t + "[spoiler='New Items']\n";
      } 
      t = t + "[spoiler='" + data.name + "']\n";
      if (data.url != null)
        t = t + "[center][img]" + data.url + "[/img][/center]\n"; 
      boolean extraSpoilerTag = false;
      for (String s : data.info) {
        if (s.startsWith("Spoilers:")) {
          extraSpoilerTag = true;
          t = t + "[spoiler='Spoilers!']\n";
        } else {
          t = t + s + "\n";
        } 
      } 
      if (extraSpoilerTag)
        t = t + "[/spoiler]\n"; 
      t = t + "[/spoiler]\n\n";
    } 
    t = t + "[/spoiler]\n";
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
      if (arg0.isBlock && !arg1.isBlock)
        return -1; 
      if (arg1.isBlock && !arg0.isBlock)
        return 1; 
      return arg0.name.compareTo(arg1.name);
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandDumpNEIInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */