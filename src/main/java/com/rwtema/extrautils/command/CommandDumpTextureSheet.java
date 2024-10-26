package com.rwtema.extrautils.command;

import com.rwtema.extrautils.LogHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CommandDumpTextureSheet extends CommandBase {
  public String getCommandName() {
    return "dumpTextureAtlas";
  }
  
  public String getCommandUsage(ICommandSender p_71518_1_) {
    return "dumpTextureAtlas";
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
    return true;
  }
  
  public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
    outputTexture(TextureMap.locationBlocksTexture, "blocks");
    outputTexture(TextureMap.locationItemsTexture, "items");
  }
  
  public void outputTexture(ResourceLocation locationTexture, String s) {
    int terrainTextureId = (Minecraft.getMinecraft()).renderEngine.getTexture(locationTexture).getGlTextureId();
    if (terrainTextureId == 0)
      return; 
    int w = GL11.glGetTexLevelParameteri(3553, 0, 4096);
    int h = GL11.glGetTexLevelParameteri(3553, 0, 4097);
    int[] pixels = new int[w * h];
    IntBuffer pixelBuf = ByteBuffer.allocateDirect(w * h * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
    GL11.glGetTexImage(3553, 0, 32993, 5121, pixelBuf);
    pixelBuf.limit(w * h);
    pixelBuf.get(pixels);
    BufferedImage image = ImageTypeSpecifier.createFromBufferedImageType(2).createBufferedImage(w, h);
    image.setRGB(0, 0, w, h, pixels, 0, w);
    File f = new File(new File((Minecraft.getMinecraft()).mcDataDir, "xutexture"), s + ".png");
    try {
      if (!f.getParentFile().exists() && !f.getParentFile().mkdirs())
        return; 
      if (!f.exists() && !f.createNewFile())
        return; 
      ImageIO.write(image, "png", f);
    } catch (IOException e) {
      LogHelper.info("Unable to output " + s, new Object[0]);
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\CommandDumpTextureSheet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */