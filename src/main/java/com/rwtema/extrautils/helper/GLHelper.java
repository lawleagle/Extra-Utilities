package com.rwtema.extrautils.helper;

import gnu.trove.map.hash.TIntByteHashMap;
import gnu.trove.procedure.TIntByteProcedure;
import org.lwjgl.opengl.GL11;

public class GLHelper {
  public static int state_level = -1;

  public static final int max_state_level = 256;

  public static final TIntByteHashMap[] maps = new TIntByteHashMap[256];

  public static void pushGLState() {
    state_level++;
    if (maps[state_level] == null) {
      maps[state_level] = new TIntByteHashMap();
    } else {
      maps[state_level].clear();
    }
  }

  public static boolean enableGLState(int state) {
    boolean b = GL11.glIsEnabled(state);
    maps[state_level].putIfAbsent(state, b ? (byte)1 : (byte)0);
    GL11.glEnable(state);
    return b;
  }

  public static boolean disableGLState(int state) {
    boolean b = GL11.glIsEnabled(state);
    maps[state_level].putIfAbsent(state, b ? (byte)1 : (byte)0);
    GL11.glDisable(state);
    return b;
  }

  public static void popGLState() {
    maps[state_level].forEachEntry(new TIntByteProcedure() {
          public boolean execute(int a, byte b) {
            if (b == 1) {
              GL11.glEnable(a);
            } else {
              GL11.glDisable(a);
            }
            return true;
          }
        });
    maps[state_level].clear();
    state_level--;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\GLHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
