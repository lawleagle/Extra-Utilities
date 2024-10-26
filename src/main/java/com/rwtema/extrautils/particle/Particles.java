package com.rwtema.extrautils.particle;

import net.minecraft.world.World;

public enum Particles {
  BUBBLE, SUSPENDED, DEPTHSUSPEND, TOWNAURA, CRIT, MAGICCRIT, SMOKE, MOBSPELL, MOBSPELLAMBIENT, SPELL, INSTANTSPELL, WITCHMAGIC, NOTE, PORTAL, ENCHANTMENTTABLE, EXPLODE, FLAME, LAVA, FOOTSTEP, SPLASH, LARGESMOKE, CLOUD, REDDUST, SNOWBALLPOOF, DRIPWATER, DRIPLAVA, SNOWSHOVEL, SLIME, HEART, ANGRYVILLAGER, HAPPYVILLAGER;
  
  public final String id;
  
  Particles() {
    this.id = name().toLowerCase();
  }
  
  public void spawn(World world, double x, double y, double z) {
    spawn(world, x, y, z, 0, 0, 0);
  }
  
  public void spawn(World world, double x, double y, double z, int r, int g, int b) {
    world.spawnParticle(this.id, x, y, z, r, g, b);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\particle\Particles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */