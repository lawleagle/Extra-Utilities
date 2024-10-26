package com.rwtema.extrautils.core.config;

import java.util.HashSet;
import net.minecraftforge.common.config.Configuration;

public class Config {
  public static HashSet<Config> configs = new HashSet<Config>();
  
  public String name;
  
  public String comment;
  
  public boolean shouldReload;
  
  public boolean reloadData() {
    return false;
  }
  
  public void load(Configuration config) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\config\Config.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */