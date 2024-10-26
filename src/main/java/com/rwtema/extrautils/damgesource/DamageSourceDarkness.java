package com.rwtema.extrautils.damgesource;

import net.minecraft.util.DamageSource;

public class DamageSourceDarkness extends DamageSource {
  public static DamageSourceDarkness darkness = new DamageSourceDarkness();
  
  protected DamageSourceDarkness() {
    super("darkness");
    setDamageBypassesArmor();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\damgesource\DamageSourceDarkness.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */