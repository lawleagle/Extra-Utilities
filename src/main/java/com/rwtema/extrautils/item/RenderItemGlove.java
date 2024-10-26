package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class RenderItemGlove implements IItemRenderer {
  public static final RenderItemGlove INSTANCE = new RenderItemGlove();
  
  float renderTickTime;
  
  static {
    FMLCommonHandler.instance().bus().register(INSTANCE);
    MinecraftForge.EVENT_BUS.register(INSTANCE);
  }
  
  @SubscribeEvent
  public void getFrame(TickEvent.RenderTickEvent event) {
    this.renderTickTime = event.renderTickTime;
  }
  
  @SubscribeEvent
  public void tickCol(TickEvent.ClientTickEvent event) {
    ItemGlove.genericDmg = 0x50 | ItemGlove.genericDmg + 1 & 0xF;
  }
  
  @SubscribeEvent
  public void renderEquippedGlove(RenderPlayerEvent.Specials.Post event) {
    if (event.entityPlayer == null)
      return; 
    ItemStack heldItem = event.entityPlayer.getHeldItem();
    if (heldItem == null || heldItem.getItem() != ExtraUtils.glove)
      return; 
    int dmg = heldItem.getItemDamage();
    RenderPlayer renderplayer = event.renderer;
    float[] col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(1, dmg)];
    GL11.glColor3f(col[0], col[1], col[2]);
    Minecraft.getMinecraft().getTextureManager().bindTexture(glove1);
    renderplayer.modelBipedMain.bipedRightArm.render(0.0625F);
    col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(0, dmg)];
    GL11.glColor3f(col[0], col[1], col[2]);
    Minecraft.getMinecraft().getTextureManager().bindTexture(glove2);
    renderplayer.modelBipedMain.bipedRightArm.render(0.0625F);
  }
  
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == IItemRenderer.ItemRenderType.EQUIPPED);
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON && helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK);
  }
  
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    if (type == IItemRenderer.ItemRenderType.EQUIPPED)
      return; 
    GL11.glEnable(2896);
    GL11.glEnable(2929);
    EntityClientPlayerMP player = (Minecraft.getMinecraft()).thePlayer;
    GL11.glPushMatrix();
    GL11.glEnable(32826);
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    float p_78440_1_ = this.renderTickTime;
    float f1 = 1.0F;
    float f13 = 0.8F;
    float f8 = 2.5F;
    GL11.glScalef(f8, f8, f8);
    float f5 = player.getSwingProgress(p_78440_1_);
    float f6 = MathHelper.sin(f5 * f5 * 3.1415927F);
    float f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
    GL11.glRotatef(f7 * 80.0F, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(f7 * 20.0F, 0.0F, 0.0F, 1.0F);
    GL11.glRotatef(f6 * 20.0F, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.7F * f13, 0.65F * f13 + (1.0F - f1) * 0.6F, 0.9F * f13);
    f5 = player.getSwingProgress(p_78440_1_);
    f6 = MathHelper.sin(f5 * 3.1415927F);
    f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
    GL11.glTranslatef(f7 * 0.4F, -MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F * 2.0F) * 0.2F, f6 * 0.2F);
    f5 = player.getSwingProgress(p_78440_1_);
    f6 = MathHelper.sin(f5 * 3.1415927F);
    f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
    GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F * 2.0F) * 0.4F, -f6 * 0.4F);
    GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    GL11.glEnable(32826);
    f5 = player.getSwingProgress(p_78440_1_);
    f6 = MathHelper.sin(f5 * f5 * 3.1415927F);
    f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
    GL11.glRotatef(f7 * 70.0F, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
    Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
    GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
    GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
    GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
    GL11.glScalef(1.0F, 1.0F, 1.0F);
    GL11.glTranslatef(5.6F, 0.0F, 0.0F);
    RenderPlayer renderplayer = (RenderPlayer)RenderManager.instance.getEntityRenderObject((Entity)player);
    float f10 = 1.0F;
    GL11.glScalef(f10, f10, f10);
    renderplayer.renderFirstPersonArm((EntityPlayer)player);
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
    renderplayer.modelBipedMain.onGround = 0.0F;
    renderplayer.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, (Entity)player);
    renderplayer.modelBipedMain.bipedRightArm.render(0.0625F);
    int dmg = item.getItemDamage();
    float[] col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(1, dmg)];
    GL11.glColor3f(col[0], col[1], col[2]);
    Minecraft.getMinecraft().getTextureManager().bindTexture(glove1);
    renderplayer.modelBipedMain.bipedRightArm.render(0.0625F);
    col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(0, dmg)];
    GL11.glColor3f(col[0], col[1], col[2]);
    Minecraft.getMinecraft().getTextureManager().bindTexture(glove2);
    renderplayer.modelBipedMain.bipedRightArm.render(0.0625F);
    GL11.glPopMatrix();
  }
  
  private static final ResourceLocation glove1 = new ResourceLocation("extrautils", "textures/glove0.png");
  
  private static final ResourceLocation glove2 = new ResourceLocation("extrautils", "textures/glove1.png");
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\RenderItemGlove.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */