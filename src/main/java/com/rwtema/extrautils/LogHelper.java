package com.rwtema.extrautils;

import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
  public static Logger logger = LogManager.getLogger("extrautils");
  
  public static boolean isDeObf = false;
  
  static {
    try {
      World.class.getMethod("getBlock", new Class[] { int.class, int.class, int.class });
      isDeObf = true;
    } catch (Throwable ex) {
      isDeObf = false;
    } 
  }
  
  public static void debug(Object info, Object... info2) {
    if (isDeObf) {
      String temp = "Debug: " + info;
      for (Object t : info2)
        temp = temp + " " + t; 
      logger.info(info);
    } 
  }
  
  public static void info(Object info, Object... info2) {
    String temp = "" + info;
    for (Object t : info2)
      temp = temp + " " + t; 
    logger.info(info);
  }
  
  public static void fine(Object info, Object... info2) {
    String temp = "" + info;
    for (Object t : info2)
      temp = temp + " " + t; 
    logger.debug(temp);
  }
  
  public static void errorThrowable(String message, Throwable t) {
    logger.error(message, t);
  }
  
  public static void error(Object info, Object... info2) {
    String temp = "" + info;
    for (Object t : info2)
      temp = temp + " " + t; 
    logger.error(info);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\LogHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */