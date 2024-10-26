package com.rwtema.extrautils.damgesource;

import net.minecraft.util.DamageSource;

public class DamageSourceDivByZero extends DamageSource {
  public static DamageSourceDivByZero divbyzero = new DamageSourceDivByZero();
  
  protected DamageSourceDivByZero() {
    super("divbyzero");
    setDamageBypassesArmor();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\damgesource\DamageSourceDivByZero.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */