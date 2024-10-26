package com.rwtema.extrautils.network.packets;

import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.sounds.Sounds;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PacketPlaySound extends XUPacketBase {
  public short sound_id;
  
  public float x;
  
  public float y;
  
  public float z;
  
  public PacketPlaySound() {
    this.sound_id = -1;
  }
  
  public PacketPlaySound(short sound_id, float x, float y, float z) {
    this.sound_id = sound_id;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void writeData(ByteBuf data) throws Exception {
    data.writeByte(this.sound_id);
    data.writeFloat(this.x);
    data.writeFloat(this.y);
    data.writeFloat(this.z);
  }
  
  public void readData(EntityPlayer player, ByteBuf data) {
    this.sound_id = data.readUnsignedByte();
    this.x = data.readFloat();
    this.y = data.readFloat();
    this.z = data.readFloat();
  }
  
  public void doStuffServer(ChannelHandlerContext ctx) {}
  
  ResourceLocation[] sounds = new ResourceLocation[] { new ResourceLocation("extrautils", "hostile.creepy_laugh") };
  
  @SideOnly(Side.CLIENT)
  public void doStuffClient() {
    if (this.sound_id < 0 || this.sound_id >= this.sounds.length)
      return; 
    Sounds.tryAddSound((ISound)PositionedSoundRecord.func_147675_a(this.sounds[this.sound_id], this.x, this.y, this.z));
  }
  
  public boolean isValidSenderSide(Side properSenderSide) {
    return (properSenderSide == Side.SERVER);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\network\packets\PacketPlaySound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */