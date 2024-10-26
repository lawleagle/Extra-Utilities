package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketVillagerReturn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import org.lwjgl.opengl.GL11;

public class GuiTradingPost extends GuiScreen {
  protected static RenderItem itemRenderer = new RenderItem();
  
  private static Comparator c = new VillagerSorter();
  
  protected int guiLeft = 0;
  
  protected int guiTop = 0;
  
  protected String screenTitle = "Trading Post";
  
  List<MerchantRecipe> merchant_recipes = new ArrayList<MerchantRecipe>();
  
  List<Integer> merchant_id = new ArrayList<Integer>();
  
  int item_size = 18;
  
  int space_between_items = 10;
  
  int button_width = this.item_size * 3 + this.space_between_items * 4;
  
  int button_height = 20;
  
  int w = this.button_width + this.space_between_items, h = this.button_height + 5;
  
  int grid_w = 4, grid_h = 5;
  
  int grid_no = this.grid_w * this.grid_h;
  
  int x0 = 0, y0 = 20;
  
  int curPage = 0;
  
  int maxPages;
  
  TileEntity tradingPost;
  
  EntityPlayer player;
  
  private GuiButton[] buttons;
  
  private GuiButton leftButton;
  
  private GuiButton rightButton;
  
  public GuiTradingPost(EntityPlayer _player, int[] p1ids, MerchantRecipeList[] p2recipes, TileEntity _post) {
    this.tradingPost = _post;
    this.player = _player;
    List<Object[]> t = new ArrayList();
    for (int i = 0; i < p2recipes.length; i++) {
      Entity e = _player.worldObj.getEntityByID(p1ids[i]);
      if (e != null)
        for (int m = 0; m < p2recipes[i].size(); m++) {
          t.add(new Object[] { e, p2recipes[i].get(m) });
        }  
    } 
    Collections.sort(t, c);
    int hash = -1;
    int j = 0;
    for (int k = 0; k < p2recipes.length; k++) {
      MerchantRecipe m = (MerchantRecipe)((Object[])t.get(k))[1];
      int h = m.writeToTags().hashCode();
      if (h != hash || k == 0) {
        hash = h;
        this.merchant_recipes.add(j, m);
        this.merchant_id.add(j, Integer.valueOf(((Entity)((Object[])t.get(k))[0]).getEntityId()));
        j++;
      } 
    } 
  }
  
  public static int getProf(Object a) {
    if (a instanceof EntityVillager)
      return ((EntityVillager)a).getProfession(); 
    return -1;
  }
  
  public void setPage(int page) {
    if (page < 0)
      page = 0; 
    if (page >= this.maxPages)
      page = this.maxPages - 1; 
    this.curPage = page;
    for (int i = 0; i < this.buttons.length; i++)
      (this.buttons[i]).enabled = (getRecipeForButton(i) < this.merchant_id.size() && !((MerchantRecipe)this.merchant_recipes.get(i)).isRecipeDisabled()); 
  }
  
  public int getRecipeForButton(int i) {
    return this.curPage * this.grid_no + i;
  }
  
  public void initGui() {
    this.buttonList.clear();
    int h2 = (int)Math.ceil(this.width / 1.5D);
    this.grid_w = Math.min(this.width / this.w, 4);
    this.grid_h = Math.min((h2 - 40) / this.h, 5);
    if (this.grid_h < 0)
      this.grid_h = 1; 
    this.grid_no = this.grid_w * this.grid_h;
    this.buttons = new GuiButton[this.grid_no];
    this.maxPages = (int)Math.ceil((this.merchant_recipes.size() / this.grid_no));
    this.x0 = (this.width - this.grid_w * this.w) / 2;
    this.y0 = 80;
    this.buttonList.add(this.leftButton = new GuiButton(0, this.width / 2 - 100, 15, 20, 20, "<"));
    this.buttonList.add(this.rightButton = new GuiButton(1, this.width / 2 + 80, 15, 20, 20, ">"));
    this.leftButton.enabled = (this.maxPages > 1);
    this.rightButton.enabled = (this.maxPages > 1);
    String text = "";
    for (int i = 0; i < this.grid_no; i++) {
      this.buttons[i] = new GuiButton(2 + i, this.x0 + i / this.grid_h * this.w, this.y0 + i % this.grid_h * this.h, this.button_width, this.button_height, text);
      if (i >= this.merchant_id.size())
        (this.buttons[i]).enabled = false; 
      this.buttonList.add(this.buttons[i]);
    } 
    setPage(0);
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
  
  public void updateScreen() {
    boolean flag = false;
    if (this.tradingPost == null) {
      flag = true;
    } else if (this.tradingPost.getWorldObj() == null) {
      flag = true;
    } else if (this.tradingPost.getWorldObj().getTileEntity(this.tradingPost.xCoord, this.tradingPost.yCoord, this.tradingPost.zCoord) != this.tradingPost) {
      flag = true;
    } 
    if (flag)
      this.mc.displayGuiScreen(null); 
  }
  
  protected void actionPerformed(GuiButton par1GuiButton) {
    if (par1GuiButton.enabled)
      if (par1GuiButton.id == 0) {
        setPage(this.curPage - 1);
      } else if (par1GuiButton.id == 1) {
        setPage(this.curPage + 1);
      } else {
        int i = getRecipeForButton(par1GuiButton.id - 2);
        NetworkHandler.sendPacketToServer((XUPacketBase)new PacketVillagerReturn(((Integer)this.merchant_id.get(i)).intValue(), (this.tradingPost.getWorldObj()).provider.dimensionId, this.tradingPost.xCoord, this.tradingPost.yCoord, this.tradingPost.zCoord));
        this.mc.displayGuiScreen(null);
      }  
  }
  
  public void drawScreen(int par1, int par2, float par3) {
    drawDefaultBackground();
    drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 15, 16777215);
    super.drawScreen(par1, par2, par3);
    GL11.glPushMatrix();
    RenderHelper.enableGUIStandardItemLighting();
    GL11.glDisable(2896);
    GL11.glEnable(32826);
    GL11.glEnable(2903);
    GL11.glEnable(2896);
    itemRenderer.zLevel = 100.0F;
    int i;
    for (i = 0; i < this.buttons.length; i++) {
      int j = getRecipeForButton(i);
      if (j < this.merchant_recipes.size()) {
        int x = (this.buttons[i]).xPosition, y = (this.buttons[i]).yPosition;
        if (((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy() != null) {
          itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), x + this.space_between_items, y + 2);
          itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), x + this.space_between_items, y + 2);
          itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy(), x + this.space_between_items * 2 + this.item_size, y + 2);
          itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy(), x + this.space_between_items * 2 + this.item_size, y + 2);
          itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), x + 3 * this.space_between_items + 2 * this.item_size, y + 2);
          itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), x + 3 * this.space_between_items + 2 * this.item_size, y + 2);
        } else {
          itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y + 2);
          itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y + 2);
          itemRenderer.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y + 2);
          itemRenderer.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y + 2);
        } 
      } 
    } 
    itemRenderer.zLevel = 0.0F;
    GL11.glDisable(2896);
    for (i = 0; i < this.buttons.length; i++) {
      int j = getRecipeForButton(i);
      if (j < this.merchant_recipes.size()) {
        int x = (this.buttons[i]).xPosition, y = (this.buttons[i]).yPosition;
        if (((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy() != null) {
          drawCenteredString(this.fontRendererObj, "+", x + this.space_between_items * 3 / 2 + this.item_size, y + 7, 16777215);
          drawCenteredString(this.fontRendererObj, "=", x + this.space_between_items * 5 / 2 + 2 * this.item_size, y + 7, 16777215);
        } else {
          drawCenteredString(this.fontRendererObj, "=", x + this.button_width / 2, y + 7, 16777215);
        } 
      } 
    } 
    for (i = 0; i < this.buttons.length; i++) {
      int j = getRecipeForButton(i);
      if (j < this.merchant_recipes.size()) {
        int x = (this.buttons[i]).xPosition, y = (this.buttons[i]).yPosition;
        if (((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy() != null) {
          if (func_146978_c(x + this.space_between_items, y, this.item_size, this.item_size, par1, par2))
            drawItemStackTooltip(((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), par1, par2); 
          if (func_146978_c(x + this.space_between_items * 2 + this.item_size, y, this.item_size, this.item_size, par1, par2))
            drawItemStackTooltip(((MerchantRecipe)this.merchant_recipes.get(j)).getSecondItemToBuy(), par1, par2); 
          if (func_146978_c(x + 3 * this.space_between_items + 2 * this.item_size, y, this.item_size, this.item_size, par1, par2))
            drawItemStackTooltip(((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), par1, par2); 
        } else {
          if (func_146978_c(x + (this.item_size + this.space_between_items) / 2 + this.space_between_items, y, this.item_size, this.item_size, par1, par2))
            drawItemStackTooltip(((MerchantRecipe)this.merchant_recipes.get(j)).getItemToBuy(), par1, par2); 
          if (func_146978_c(x + (this.item_size + this.space_between_items) / 2 + 2 * this.space_between_items + this.item_size, y, this.item_size, this.item_size, par1, par2))
            drawItemStackTooltip(((MerchantRecipe)this.merchant_recipes.get(j)).getItemToSell(), par1, par2); 
        } 
      } 
    } 
    GL11.glPopMatrix();
    GL11.glEnable(2896);
    GL11.glEnable(2929);
    RenderHelper.enableStandardItemLighting();
  }
  
  protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3) {
    GL11.glDisable(32826);
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(2896);
    GL11.glDisable(2929);
    List<String> list = par1ItemStack.getTooltip((EntityPlayer)this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
    if (!list.isEmpty()) {
      int k = 0;
      int l;
      for (l = 0; l < list.size(); l++) {
        int i = this.fontRendererObj.getStringWidth(list.get(l));
        if (i > k)
          k = i; 
      } 
      l = par2 + 12;
      int i1 = par3 - 12;
      int j1 = 8;
      if (list.size() > 1)
        j1 += 2 + (list.size() - 1) * 10; 
      this.zLevel = 300.0F;
      itemRenderer.zLevel = 300.0F;
      int k1 = -267386864;
      drawGradientRect(l - 3, i1 - 4, l + k + 3, i1 - 3, k1, k1);
      drawGradientRect(l - 3, i1 + j1 + 3, l + k + 3, i1 + j1 + 4, k1, k1);
      drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 + j1 + 3, k1, k1);
      drawGradientRect(l - 4, i1 - 3, l - 3, i1 + j1 + 3, k1, k1);
      drawGradientRect(l + k + 3, i1 - 3, l + k + 4, i1 + j1 + 3, k1, k1);
      int l1 = 1347420415;
      int i2 = (l1 & 0xFEFEFE) >> 1 | l1 & 0xFF000000;
      drawGradientRect(l - 3, i1 - 3 + 1, l - 3 + 1, i1 + j1 + 3 - 1, l1, i2);
      drawGradientRect(l + k + 2, i1 - 3 + 1, l + k + 3, i1 + j1 + 3 - 1, l1, i2);
      drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 - 3 + 1, l1, l1);
      drawGradientRect(l - 3, i1 + j1 + 2, l + k + 3, i1 + j1 + 3, i2, i2);
      for (int j2 = 0; j2 < list.size(); j2++) {
        String s = list.get(j2);
        if (j2 == 0) {
          s = (par1ItemStack.getRarity()).rarityColor + s;
        } else {
          s = EnumChatFormatting.GRAY + s;
        } 
        this.fontRendererObj.drawStringWithShadow(s, l, i1, -1);
        if (j2 == 0)
          i1 += 2; 
        i1 += 10;
      } 
      this.zLevel = 0.0F;
      itemRenderer.zLevel = 0.0F;
    } 
  }
  
  private void drawLine(double x1, double x2, double y1, double y2, int col) {
    GL11.glDisable(3553);
    GL11.glEnable(3042);
    GL11.glDisable(3008);
    GL11.glBlendFunc(770, 771);
    GL11.glShadeModel(7425);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(1);
    tessellator.setColorOpaque_I(col);
    tessellator.addVertex(x1, y1, this.zLevel);
    tessellator.addVertex(x2, y2, this.zLevel);
    tessellator.draw();
    GL11.glShadeModel(7424);
    GL11.glDisable(3042);
    GL11.glEnable(3008);
    GL11.glEnable(3553);
  }
  
  protected boolean func_146978_c(int par1, int par2, int par3, int par4, int par5, int par6) {
    int k1 = this.guiTop;
    int l1 = this.guiLeft;
    par5 -= k1;
    par6 -= l1;
    return (par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1);
  }
  
  public static class VillagerSorter implements Comparator {
    public int intCompare(int a, int b) {
      if (a == b)
        return 0; 
      if (a > b)
        return 1; 
      return -1;
    }
    
    public int itemCompare(Item a, Item b) {
      return a.getUnlocalizedName().compareTo(b.getUnlocalizedName());
    }
    
    public int compare(Object arg0, Object arg1) {
      int i = intCompare(GuiTradingPost.getProf(((Object[])arg0)[0]), GuiTradingPost.getProf(((Object[])arg0)[0]));
      if (i == 0) {
        MerchantRecipe m1 = (MerchantRecipe)((Object[])arg0)[1];
        MerchantRecipe m2 = (MerchantRecipe)((Object[])arg1)[1];
        int i2 = itemCompare(m1.getItemToBuy().getItem(), m2.getItemToBuy().getItem());
        if (i2 == 0) {
          int i3 = intCompare((m1.getItemToBuy()).stackSize, (m2.getItemToBuy()).stackSize);
          if (i3 == 0) {
            int i4 = itemCompare(m1.getItemToSell().getItem(), m2.getItemToSell().getItem());
            if (i4 == 0) {
              int i5 = intCompare((m1.getItemToSell()).stackSize, (m2.getItemToSell()).stackSize);
              if (i5 == 0)
                return intCompare(m1.writeToTags().hashCode(), m2.writeToTags().hashCode()); 
              return i5;
            } 
            return i4;
          } 
          return i3;
        } 
        return i2;
      } 
      return i;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiTradingPost.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */