package com.rwtema.extrautils.sounds;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.tileentity.generators.TileEntityGenerator;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;

@SideOnly(Side.CLIENT)
public class Sounds {
  public static void registerSoundTile(ISoundTile soundTile) {
    if (ExtraUtilsMod.proxy.isClientSideAvailable())
      registerSoundTileDo(soundTile); 
  }
  
  public static void addGenerator(TileEntityGenerator gen) {
    if (ExtraUtilsMod.proxy.isClientSideAvailable())
      XUSoundTileGenerators.addGenerator(gen); 
  }
  
  public static void refresh() {
    if (ExtraUtilsMod.proxy.isClientSideAvailable())
      XUSoundTileGenerators.refresh(); 
  }
  
  private static void registerSoundTileDo(ISoundTile soundTile) {
    tryAddSound((ISound)new XUSoundTile(soundTile));
  }
  
  public static void tryAddSound(ISound sound) {
    if (canAddSound(sound))
      Minecraft.getMinecraft().getSoundHandler().playSound(sound); 
  }
  
  private static Field playingSounds = null;
  
  private static Field soundMgr;
  
  public static boolean canAddSound(ISound sound) {
    if (playingSounds == null) {
      playingSounds = ReflectionHelper.findField(SoundManager.class, new String[] { "playingSounds", "field_148629_h" });
      soundMgr = ReflectionHelper.findField(SoundHandler.class, new String[] { "sndManager", "field_147694_f" });
    } 
    try {
      SoundManager manager = (SoundManager)soundMgr.get(Minecraft.getMinecraft().getSoundHandler());
      Map map = (Map)playingSounds.get(manager);
      return !map.containsValue(sound);
    } catch (IllegalAccessException e) {
      return false;
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sounds\Sounds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */