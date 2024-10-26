package com.rwtema.extrautils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "ExtraUtilities", name = "ExtraUtilities", dependencies = "required-after:Forge@[10.13.2.1291,);after:ForgeMultipart@[1.2.0.336,);after:Baubles;after:ThermalFoundation;after:EE3;before:TConstruct@[1.7.10-1.8.5,)")
public class ExtraUtilsMod {
  public static final String modId = "ExtraUtilities";
  
  @SidedProxy(clientSide = "com.rwtema.extrautils.ExtraUtilsClient", serverSide = "com.rwtema.extrautils.ExtraUtilsProxy")
  public static ExtraUtilsProxy proxy;
  
  @Instance("ExtraUtilities")
  public static ExtraUtilsMod instance;
  
  public static ExtraUtils extraUtils;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    extraUtils = new ExtraUtils();
    extraUtils.preInit(event);
  }
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
    extraUtils.init(event);
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    extraUtils.postInit(event);
  }
  
  @EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    extraUtils.serverStarting(event);
  }
  
  @EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    extraUtils.serverStart(event);
  }
  
  @EventHandler
  public void remap(FMLMissingMappingsEvent event) {
    extraUtils.remap(event);
  }
  
  @EventHandler
  public void loadComplete(FMLLoadCompleteEvent event) {
    extraUtils.loadComplete(event);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ExtraUtilsMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */