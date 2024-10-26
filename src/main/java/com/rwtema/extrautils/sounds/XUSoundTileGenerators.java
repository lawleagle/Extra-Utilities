package com.rwtema.extrautils.sounds;

import com.rwtema.extrautils.tileentity.generators.TileEntityGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class XUSoundTileGenerators implements ITickableSound {
  private static WeakReference<XUSoundTileGenerators> instance = null;
  
  private WeakHashMap<TileEntityGenerator, Void> tiles = new WeakHashMap<TileEntityGenerator, Void>();
  
  private boolean donePlaying = false;
  
  private float zPosF = 0.0F;
  
  private float xPosF = 0.0F;
  
  private float yPosF = 0.0F;
  
  private float volume = 1.0E-6F;
  
  private ResourceLocation location;
  
  public XUSoundTileGenerators() {
    this.location = TileEntityGenerator.hum;
  }
  
  private static XUSoundTileGenerators getInstance() {
    XUSoundTileGenerators sound = (instance != null) ? instance.get() : null;
    if (sound == null) {
      sound = new XUSoundTileGenerators();
      instance = new WeakReference<XUSoundTileGenerators>(sound);
    } 
    return sound;
  }
  
  private static void clearInstance() {
    instance = null;
  }
  
  public boolean isDonePlaying() {
    return this.donePlaying;
  }
  
  public ResourceLocation getPositionedSoundLocation() {
    return this.location;
  }
  
  public boolean canRepeat() {
    return true;
  }
  
  public int getRepeatDelay() {
    return 0;
  }
  
  public float getVolume() {
    return this.volume;
  }
  
  public float getPitch() {
    return 1.0F;
  }
  
  public float getXPosF() {
    return this.xPosF;
  }
  
  public float getYPosF() {
    return this.yPosF;
  }
  
  public float getZPosF() {
    return this.zPosF;
  }
  
  public ISound.AttenuationType getAttenuationType() {
    return ISound.AttenuationType.LINEAR;
  }
  
  @SideOnly(Side.CLIENT)
  public static void addGenerator(TileEntityGenerator generator) {
    XUSoundTileGenerators instance1 = getInstance();
    removeOldValues();
    if (!instance1.tiles.containsKey(generator)) {
      instance1.tiles.put(generator, null);
      if (instance1.tiles.size() == 1)
        refresh(); 
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public static void refresh() {
    if (instance == null || instance.get() == null)
      return; 
    XUSoundTileGenerators sound = getInstance();
    if (!sound.tiles.isEmpty()) {
      removeOldValues();
      if (!sound.tiles.isEmpty() && Sounds.canAddSound((ISound)sound)) {
        sound.setDonePlaying(false);
        sound.volume = 1.0E-7F;
        Sounds.tryAddSound((ISound)sound);
      } 
    } 
  }
  
  private static void removeOldValues() {
    WeakHashMap<TileEntityGenerator, Void> weakHashMap = (getInstance()).tiles;
    Iterator<TileEntityGenerator> iter = weakHashMap.keySet().iterator();
    while (iter.hasNext()) {
      TileEntityGenerator gen = iter.next();
      if (gen == null || gen.isInvalid() || gen.getWorldObj() != (Minecraft.getMinecraft()).theWorld)
        iter.remove(); 
    } 
  }
  
  public synchronized void update() {
    EntityClientPlayerMP player = (Minecraft.getMinecraft()).thePlayer;
    if (player == null) {
      setDonePlaying(true);
      return;
    } 
    removeOldValues();
    if (this.tiles.size() == 0) {
      if (this.volume > 5.0E-4D) {
        this.volume *= 0.9F;
      } else {
        setDonePlaying(true);
      } 
    } else {
      boolean shouldPlay = false;
      if (this.tiles.size() == 1) {
        for (TileEntityGenerator gen : this.tiles.keySet()) {
          if (gen.shouldSoundPlay()) {
            shouldPlay = true;
            moveTorwards(gen.x() + 0.5F, gen.y() + 0.5F, gen.z() + 0.5F, 0.05F);
          } 
        } 
      } else {
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        float total_weight = 0.0F;
        for (TileEntityGenerator gen : this.tiles.keySet()) {
          if (gen != null && gen.shouldSoundPlay()) {
            shouldPlay = true;
            float w = weight(gen.getDistanceFrom(player.posX, player.posY, player.posZ));
            x += w * gen.x();
            y += w * gen.y();
            z += w * gen.z();
            total_weight += w;
          } 
        } 
        if (shouldPlay && total_weight > 0.0F) {
          moveTorwards(x / total_weight + 0.5F, y / total_weight + 0.5F, z / total_weight + 0.5F, 0.05F);
        } else {
          this.volume *= 0.9F;
        } 
      } 
      if (shouldPlay) {
        if (this.volume < 0.9995D) {
          this.volume = 1.0F - (1.0F - this.volume) * 0.9F;
        } else {
          this.volume = 1.0F;
        } 
      } else if (this.volume > 5.0E-4D) {
        this.volume *= 0.9F;
      } else {
        this.volume = 0.0F;
      } 
    } 
  }
  
  private void moveTorwards(float x, float y, float z, float speed) {
    if (this.volume == 0.0F)
      speed = 1.0F; 
    this.xPosF = this.xPosF * (1.0F - speed) + x * speed;
    this.yPosF = this.yPosF * (1.0F - speed) + y * speed;
    this.zPosF = this.zPosF * (1.0F - speed) + z * speed;
  }
  
  private static float weight(double d) {
    if (d < 1.0D)
      return 1.0F; 
    return (float)(1.0D / d);
  }
  
  public void setDonePlaying(boolean donePlaying) {
    this.donePlaying = donePlaying;
    if (donePlaying)
      clearInstance(); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sounds\XUSoundTileGenerators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */