package com.rwtema.extrautils.helper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class XUHelperClient {
  public static EntityPlayer clientPlayer() {
    return (EntityPlayer)(Minecraft.getMinecraft()).thePlayer;
  }
  
  public static World clientWorld() {
    return (World)(Minecraft.getMinecraft()).theWorld;
  }
  
  public static IIcon registerCustomIcon(String texture, IIconRegister register, TextureAtlasSprite sprite) {
    TextureAtlasSprite textureAtlasSprite = ((TextureMap)register).getTextureExtry(texture);
    if (textureAtlasSprite == null) {
      TextureAtlasSprite t2 = sprite;
      textureAtlasSprite = t2;
      ((TextureMap)register).setTextureEntry(texture, t2);
    } 
    return (IIcon)textureAtlasSprite;
  }
  
  public static String commaDelimited(int n) {
    return String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf(n) });
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\XUHelperClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */