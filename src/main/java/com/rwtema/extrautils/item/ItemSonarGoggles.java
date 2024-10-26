package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ItemSonarGoggles extends ItemArmor {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/goggle_overlay.png");
  
  public ItemSonarGoggles() {
    super(ItemArmor.ArmorMaterial.IRON, 0, 0);
    setMaxDamage(1800);
    setUnlocalizedName("extrautils:sonar_goggles");
    setTextureName("extrautils:sonar_goggles");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setMaxStackSize(1);
  }
  
  public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
    return "extrautils:textures/sonar_lens.png";
  }
  
  @SideOnly(Side.CLIENT)
  public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
    double w = resolution.getScaledWidth_double();
    double h = resolution.getScaledHeight_double();
    GL11.glDisable(2929);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.2F);
    GL11.glDisable(3008);
    GL11.glDisable(3553);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertex(0.0D, h, -90.0D);
    tessellator.addVertex(w, h, -90.0D);
    tessellator.addVertex(w, 0.0D, -90.0D);
    tessellator.addVertex(0.0D, 0.0D, -90.0D);
    tessellator.draw();
    GL11.glEnable(3553);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    (Minecraft.getMinecraft()).renderEngine.bindTexture(texture);
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(0.0D, h, -90.0D, 0.0D, 1.0D);
    tessellator.addVertexWithUV(w, h, -90.0D, 1.0D, 1.0D);
    tessellator.addVertexWithUV(w, 0.0D, -90.0D, 1.0D, 0.0D);
    tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
    tessellator.draw();
    GL11.glEnable(2929);
    GL11.glEnable(3008);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemSonarGoggles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */