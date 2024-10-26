package com.rwtema.extrautils.nei.ping;

import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.api.API;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketNEIPing;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class NEIPing implements IContainerInputHandler {
  public static final String KEY_IDENTIFIER = "gui.xu_ping";
  
  public boolean keyTyped(GuiContainer guiContainer, char c, int i) {
    int keyBinding = NEIClientConfig.getKeyBinding("gui.xu_ping");
    if (i != keyBinding)
      return false; 
    LayoutManager layout = LayoutManager.instance();
    if (layout == null || LayoutManager.itemPanel == null || NEIClientConfig.isHidden())
      return false; 
    ItemStack stackMouseOver = GuiContainerManager.getStackMouseOver(guiContainer);
    if (stackMouseOver == null || stackMouseOver.getItem() == null)
      return false; 
    NetworkHandler.sendPacketToServer((XUPacketBase)new PacketNEIPing(stackMouseOver));
    return true;
  }
  
  public static void init() {
    API.addKeyBind("gui.xu_ping", 20);
    GuiContainerManager.addInputHandler(new NEIPing());
  }
  
  public void onKeyTyped(GuiContainer guiContainer, char c, int i) {}
  
  public boolean lastKeyTyped(GuiContainer guiContainer, char c, int i) {
    return false;
  }
  
  public boolean mouseClicked(GuiContainer guiContainer, int i, int i1, int i2) {
    return false;
  }
  
  public void onMouseClicked(GuiContainer guiContainer, int i, int i1, int i2) {}
  
  public void onMouseUp(GuiContainer guiContainer, int i, int i1, int i2) {}
  
  public boolean mouseScrolled(GuiContainer guiContainer, int i, int i1, int i2) {
    return false;
  }
  
  public void onMouseScrolled(GuiContainer guiContainer, int i, int i1, int i2) {}
  
  public void onMouseDragged(GuiContainer guiContainer, int i, int i1, int i2, long l) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\ping\NEIPing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */