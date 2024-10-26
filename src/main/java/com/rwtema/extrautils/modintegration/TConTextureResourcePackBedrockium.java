package com.rwtema.extrautils.modintegration;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class TConTextureResourcePackBedrockium extends TConTextureResourcePackBase {
  public TConTextureResourcePackBedrockium(String name) {
    super(name);
  }
  
  static BufferedImage bedrockImage = null;
  
  public BufferedImage modifyImage(BufferedImage image) {
    BufferedImage bedrockImage = getBedrockImage();
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int c = image.getRGB(x, y);
        if (c == 0 || rgb.getAlpha(c) < 16) {
          image.setRGB(x, y, 0);
        } else {
          float b = brightness(c) / 255.0F;
          int dx = x * bedrockImage.getWidth() / image.getWidth();
          int dy = y * bedrockImage.getHeight() / image.getHeight();
          int col = bedrockImage.getRGB(dx, dy);
          image.setRGB(x, y, rgb.getAlpha(c) << 24 | (int)(rgb.getRed(col) * b) << 16 | (int)(rgb.getGreen(col) * b) << 8 | (int)(rgb.getBlue(col) * b));
        } 
      } 
    } 
    return image;
  }
  
  public static BufferedImage getBedrockImage() {
    if (bedrockImage == null) {
      ResourceLocation bedrockLocation = new ResourceLocation("minecraft", "textures/blocks/bedrock.png");
      try {
        DefaultResourcePack mcDefaultResourcePack = (DefaultResourcePack)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), new String[] { "field_110450_ap", "mcDefaultResourcePack" });
        InputStream inputStream = mcDefaultResourcePack.getInputStream(bedrockLocation);
        List<ResourcePackRepository.Entry> t = Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries();
        for (ResourcePackRepository.Entry entry : t) {
          IResourcePack resourcePack = entry.getResourcePack();
          if (resourcePack.resourceExists(bedrockLocation))
            inputStream = resourcePack.getInputStream(bedrockLocation); 
        } 
        bedrockImage = ImageIO.read(inputStream);
      } catch (IOException e) {
        throw Throwables.propagate(e);
      } 
    } 
    return bedrockImage;
  }
  
  public void onResourceManagerReload(IResourceManager p_110549_1_) {
    super.onResourceManagerReload(p_110549_1_);
    bedrockImage = null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConTextureResourcePackBedrockium.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */