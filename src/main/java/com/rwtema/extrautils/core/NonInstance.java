package com.rwtema.extrautils.core;

public final class NonInstance {
  private NonInstance() {
    throw new IllegalStateException("NonInstance must never be initiated");
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\NonInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */