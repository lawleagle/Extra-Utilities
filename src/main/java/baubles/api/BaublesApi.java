package baubles.api;

import cpw.mods.fml.common.FMLLog;
import java.lang.reflect.Method;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BaublesApi {
  static Method getBaubles;
  
  public static IInventory getBaubles(EntityPlayer player) {
    IInventory ot = null;
    try {
      if (getBaubles == null) {
        Class<?> fake = Class.forName("baubles.common.lib.PlayerHandler");
        getBaubles = fake.getMethod("getPlayerBaubles", new Class[] { EntityPlayer.class });
      } 
      ot = (IInventory)getBaubles.invoke(null, new Object[] { player });
    } catch (Exception ex) {
      FMLLog.warning("[Baubles API] Could not invoke baubles.common.lib.PlayerHandler method getPlayerBaubles", new Object[0]);
    } 
    return ot;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\baubles\api\BaublesApi.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */