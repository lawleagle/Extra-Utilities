package com.rwtema.extrautils.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureColorBlockBase extends TextureAtlasSprite {
  private String name;
  
  private final String directory;
  
  public float scale;
  
  public TextureColorBlockBase(String par1Str) {
    this(par1Str, "blocks");
  }
  
  public TextureColorBlockBase(String par1Str, String directory) {
    super("extrautils:bw_(" + par1Str + ")");
    this.name = par1Str;
    this.directory = directory;
    this.scale = 1.6666666F;
  }
  
  public boolean load(IResourceManager manager, ResourceLocation location) {
    String s1 = "minecraft";
    String s2 = this.name;
    int ind = this.name.indexOf(':');
    if (ind >= 0) {
      s2 = this.name.substring(ind + 1, this.name.length());
      if (ind > 1)
        s1 = this.name.substring(0, ind); 
    } 
    int mp = (Minecraft.getMinecraft()).gameSettings.mipmapLevels;
    s1 = s1.toLowerCase();
    s2 = "textures/" + this.directory + "/" + s2 + ".png";
    try {
      IResource iresource = manager.getResource(new ResourceLocation(s1, s2));
      BufferedImage[] abufferedimage = new BufferedImage[1 + mp];
      abufferedimage[0] = ImageIO.read(iresource.getInputStream());
      AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
      loadSprite(abufferedimage, animationmetadatasection, ((Minecraft.getMinecraft()).gameSettings.anisotropicFiltering > 1.0F));
    } catch (IOException e) {
      return true;
    } 
    for (int j = 0; j < this.framesTextureData.size(); j++) {
      int[] image = new int[(((int[][])this.framesTextureData.get(j))[0]).length];
      float min_m = 1.0F, max_m = 0.0F;
      for (int i = 0; i < image.length; i++) {
        int l = ((int[][])this.framesTextureData.get(j))[0][i];
        if (l < 0) {
          float r = (-l >> 16 & 0xFF) / 255.0F;
          float g = (-l >> 8 & 0xFF) / 255.0F;
          float b = (-l & 0xFF) / 255.0F;
          r = 1.0F - r;
          g = 1.0F - g;
          b = 1.0F - b;
          float m = r * 0.2126F + g * 0.7152F + b * 0.0722F;
          if (m > max_m)
            max_m = m; 
          if (m < min_m)
            min_m = m; 
        } 
      } 
      if (min_m == 1.0F && max_m == 0.0F)
        return false; 
      if (max_m == min_m)
        max_m = min_m + 0.001F; 
      float new_max_m = Math.min(max_m * this.scale, 1.0F);
      float new_min_m = min_m / max_m * new_max_m;
      for (int k = 0; k < image.length; k++) {
        int l = ((int[][])this.framesTextureData.get(j))[0][k];
        if (l < 0) {
          float r = (-l >> 16 & 0xFF) / 255.0F;
          float g = (-l >> 8 & 0xFF) / 255.0F;
          float b = (-l & 0xFF) / 255.0F;
          r = 1.0F - r;
          g = 1.0F - g;
          b = 1.0F - b;
          float m = r * 0.2126F + g * 0.7152F + b * 0.0722F;
          float dm = (m - min_m) / (max_m - min_m);
          m = new_min_m + dm * (new_max_m - new_min_m);
          r = g = b = Math.max(Math.min(0.975F, m), 0.025F);
          r = 1.0F - r;
          g = 1.0F - g;
          b = 1.0F - b;
          image[k] = -((int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F));
        } 
      } 
      int[][] aint = new int[1 + mp][];
      aint[0] = image;
      this.framesTextureData.set(j, aint);
    } 
    return false;
  }
  
  public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\TextureColorBlockBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */