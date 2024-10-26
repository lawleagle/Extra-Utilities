package com.rwtema.extrautils.sounds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.ref.WeakReference;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class XUSoundTile implements ITickableSound {
  WeakReference<ISoundTile> sound;
  
  private boolean donePlaying = false;
  
  private float zPosF = 0.0F;
  
  private float xPosF = 0.0F;
  
  private float yPosF = 0.0F;
  
  private float volume = 1.0E-6F;
  
  private ResourceLocation location;
  
  public XUSoundTile(ISoundTile sound) {
    this.sound = new WeakReference<ISoundTile>(sound);
    this.location = sound.getSound();
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
  
  public void update() {
    ISoundTile sound = (this.sound != null) ? this.sound.get() : null;
    if (sound == null || sound.getTile().isInvalid()) {
      this.sound = null;
      if (this.volume > 5.0E-4D) {
        this.volume *= 0.9F;
      } else {
        this.donePlaying = true;
      } 
    } else {
      this.xPosF = (sound.getTile()).xCoord + 0.5F;
      this.yPosF = (sound.getTile()).yCoord + 0.5F;
      this.zPosF = (sound.getTile()).zCoord + 0.5F;
      if (sound.shouldSoundPlay()) {
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
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sounds\XUSoundTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */