package com.rwtema.extrautils.texture;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TextureMultiIcon extends TextureAtlasSprite {
  public int grid_x;

  public int grid_y;

  public int grid_w;

  public int grid_h;

  public String iconName;

  boolean successfullyLoad;

  public TextureMultiIcon(String par1Str, int grid_x, int grid_y, int grid_w, int grid_h) {
    super(par1Str + "_" + grid_x + "_" + grid_y);
    this.successfullyLoad = false;
    this.iconName = par1Str;
    if (this.iconName.contains(":"))
      this.iconName = this.iconName.substring(1 + this.iconName.indexOf(":"), this.iconName.length());
    this.grid_x = grid_x;
    this.grid_y = grid_y;
    this.grid_w = grid_w;
    this.grid_h = grid_h;
  }

  public static IIcon registerGridIcon(IIconRegister register, String texture, int grid_x, int grid_y, int w, int h) {
    String entry = texture + "_" + grid_x + "_" + grid_y;
    TextureAtlasSprite result = ((TextureMap)register).getTextureExtry(entry);
    if (result == null) {
      result = new TextureMultiIcon(texture, grid_x, grid_y, w, h);
      ((TextureMap)register).setTextureEntry(result.getIconName(), result);
    }
    return (IIcon)result;
  }

  public boolean hasAnimationMetadata() {
    return false;
  }

  public int[][] getFrameTextureData(int par1) {
    return (int[][])this.framesTextureData.get(par1);
  }

  public void loadSprite(BufferedImage[] p_147964_1_, AnimationMetadataSection p_147964_2_, boolean p_147964_3_) {
    throw new RuntimeException("Likely Optifine error. TextureAtlasSprite.loadSprite(BufferedImage[] p_147964_1_, AnimationMetadataSection p_147964_2_, boolean p_147964_3_) called after TextureAtlasSprite.load(IResourceManager manager, ResourceLocation location) returned false (which is the correct return value for successful loading).\nThis is not supposed to happen and will result in a missing texture.\nThis is likely an Optifine error.");
  }

  public void copyFrom(TextureAtlasSprite par1TextureAtlasSprite) {
    if (this.successfullyLoad) {
      if ("missingno".equals(par1TextureAtlasSprite.getIconName()))
        throw new RuntimeException("Likely Optifine error. Please only report if it occurs without Optifine installed:\nA Sprite with custom loading was not placed in the texture sheet despite TextureMultiIcon.load(IResourceManager manager, ResourceLocation location) completing successfully and returning false (which is the correct return value for successful loading).\nThis should not happen.");
    } else {
      super.copyFrom(par1TextureAtlasSprite);
    }
  }

  public boolean load(IResourceManager manager, ResourceLocation location) {
    String altPath = "textures/blocks/" + this.iconName + ".png";
    if (!(this.iconName + "_" + this.grid_x + "_" + this.grid_y).equals(location.getResourcePath()))
      throw new RuntimeException("Likely Optifine error. Please only report if it occurs without Optifine installed:\nTextureAtlasSprite.load(IResourceManager manager, ResourceLocation location) called with bad parameters.\nExpected resource path " + this.iconName + "_" + this.grid_x + "_" + this.grid_y + ", recieved " + location.getResourcePath() + ".\n");
    ResourceLocation altLocation = new ResourceLocation(location.getResourceDomain(), altPath);
    try {
      altLoadSprite(manager.getResource(altLocation));
    } catch (IOException e) {
      e.printStackTrace();
      this.successfullyLoad = false;
      return true;
    }
    this.successfullyLoad = true;
    return false;
  }

  public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
    return true;
  }

  public void altLoadSprite(IResource par1Resource) throws IOException {
    setFramesTextureData(Lists.newArrayList());
    this.frameCounter = 0;
    this.tickCounter = 0;
    InputStream inputstream = par1Resource.getInputStream();
    BufferedImage bufferedimage = ImageIO.read(inputstream);
    this.height = bufferedimage.getHeight() / this.grid_h;
    this.width = bufferedimage.getWidth() / this.grid_w;
    if (this.height < this.grid_h || this.width < this.grid_w)
      throw new RuntimeException("Texture too small, must be at least " + this.grid_w + " pixels wide and " + this.grid_h + " pixels tall");
    if (this.grid_x < 0 || this.grid_x >= this.grid_w)
      throw new RuntimeException("GridTextureIcon called with an invalid grid_x");
    if (this.grid_y < 0 || this.grid_y >= this.grid_h)
      throw new RuntimeException("GridTextureIcon called with an invalid grid_y");
    int[] aint = new int[this.height * this.width];
    bufferedimage.getRGB(this.grid_x * this.width, this.grid_y * this.height, this.width, this.height, aint, 0, this.width);
    if (this.height != this.width)
      throw new RuntimeException("broken aspect ratio, must be in ratio: " + this.grid_w + ":" + this.grid_h);
    this.framesTextureData.add(prepareAnisotropicFiltering(aint, this.width, this.height, (Minecraft.getMinecraft()).gameSettings.mipmapLevels, ((Minecraft.getMinecraft()).gameSettings.anisotropicFiltering > 1.0F)));
  }

  private int[][] prepareAnisotropicFiltering(int[] p_147960_1_, int p_147960_2_, int p_147960_3_, int mipMap, boolean useAnisotropicFiltering) {
    int[][] aint1 = new int[mipMap + 1][];
    aint1[0] = p_147960_1_;
    return aint1;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\TextureMultiIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
