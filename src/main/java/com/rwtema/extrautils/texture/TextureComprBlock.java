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
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureComprBlock extends TextureAtlasSprite {
  private int n;
  
  private ResourceLocation textureLocation;
  
  public TextureComprBlock(String par1Str, String base, int n) {
    super(par1Str);
    this.n = n;
    this.textureLocation = new ResourceLocation("minecraft", "textures/blocks/" + base + ".png");
  }
  
  public boolean load(IResourceManager manager, ResourceLocation location) {
    int mp = (Minecraft.getMinecraft()).gameSettings.mipmapLevels;
    try {
      IResource iresource = manager.getResource(location);
      BufferedImage[] abufferedimage = new BufferedImage[1 + (Minecraft.getMinecraft()).gameSettings.mipmapLevels];
      abufferedimage[0] = ImageIO.read(iresource.getInputStream());
      TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
      AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
      loadSprite(abufferedimage, animationmetadatasection, ((Minecraft.getMinecraft()).gameSettings.anisotropicFiltering > 1.0F));
    } catch (IOException e) {
      try {
        IResource iresource = manager.getResource(this.textureLocation);
        BufferedImage[] abufferedimage = new BufferedImage[1 + (Minecraft.getMinecraft()).gameSettings.mipmapLevels];
        abufferedimage[0] = ImageIO.read(iresource.getInputStream());
        TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
        AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
        loadSprite(abufferedimage, animationmetadatasection, ((Minecraft.getMinecraft()).gameSettings.anisotropicFiltering > 1.0F));
      } catch (IOException e1) {
        e.printStackTrace();
        return true;
      } 
      float nh = this.n / 8.5F;
      float br = 1.0F - nh;
      for (int j = 0; j < this.framesTextureData.size(); j++) {
        int[] image = new int[(((int[][])this.framesTextureData.get(j))[0]).length];
        for (int i = 0; i < image.length; i++) {
          int x = i % this.width;
          int y = i / this.height;
          int l = ((int[][])this.framesTextureData.get(j))[0][i];
          float r = (-l >> 16 & 0xFF) / 255.0F;
          float g = (-l >> 8 & 0xFF) / 255.0F;
          float b = (-l & 0xFF) / 255.0F;
          float dx = (2 * x) / (this.width - 1) - 1.0F;
          float dy = (2 * y) / (this.height - 1) - 1.0F;
          float db = Math.max(Math.abs(dx), Math.abs(dy));
          db = Math.max(db, (float)Math.sqrt((dx * dx + dy * dy)) / 1.4F);
          float d = 1.0F - db + 1.0F - nh;
          float rb = 1.0F - (2 + this.n) / 32.0F;
          float k = 1.0F;
          if (db > rb)
            k = 0.7F + 0.1F * (db - rb) / (1.0F - rb); 
          d *= k * k;
          if (d > 1.0F) {
            d = 1.0F;
          } else if (d < 0.0F) {
            d = 0.0F;
          } 
          r = 1.0F - (1.0F - r) * br * d;
          g = 1.0F - (1.0F - g) * br * d;
          b = 1.0F - (1.0F - b) * br * d;
          image[i] = -((int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F));
        } 
        int[][] aint = new int[1 + mp][];
        aint[0] = image;
        this.framesTextureData.set(j, aint);
      } 
    } 
    return false;
  }
  
  public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\TextureComprBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */