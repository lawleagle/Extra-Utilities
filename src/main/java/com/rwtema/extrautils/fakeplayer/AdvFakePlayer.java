package com.rwtema.extrautils.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class AdvFakePlayer extends FakePlayer {
  private static AdvFakePlayer playerDefault = null;
  
  String id = "41C82C87-7AfB-4024-BA57-13D2C99CAE77";
  
  public AdvFakePlayer(WorldServer world, GameProfile name) {
    super(world, name);
  }
  
  public String getPlayerIP() {
    return "fake.player.user.name=" + getGameProfile().getName();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\fakeplayer\AdvFakePlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */