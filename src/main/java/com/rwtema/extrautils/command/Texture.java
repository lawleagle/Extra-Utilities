package com.rwtema.extrautils.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class Texture {
  private int id;
  
  public final int w;
  
  public final int h;
  
  private final IntBuffer pixelBuf;
  
  public Texture(int w, int h, int fillColour, int minFilter, int maxFilter, int textureWrap) {
    this.id = GL11.glGenTextures();
    this.w = w;
    this.h = h;
    this.pixelBuf = allocateDirectIntBuffer(w * h);
    fillRect(0, 0, w, h, fillColour);
    this.pixelBuf.position(0);
    bind();
    GL11.glTexImage2D(3553, 0, 32856, w, h, 0, 32993, 5121, this.pixelBuf);
    setTexParameters(minFilter, maxFilter, textureWrap);
  }
  
  public static IntBuffer allocateDirectIntBuffer(int size) {
    return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
  }
  
  public Texture(int w, int h, int fillColour) {
    this(w, h, fillColour, 9729, 9728, 33071);
  }
  
  public static int getTextureWidth() {
    return GL11.glGetTexLevelParameteri(3553, 0, 4096);
  }
  
  public static int getTextureHeight() {
    return GL11.glGetTexLevelParameteri(3553, 0, 4097);
  }
  
  public Texture(int id) {
    this.id = id;
    bind();
    this.w = getTextureWidth();
    this.h = getTextureHeight();
    this.pixelBuf = allocateDirectIntBuffer(this.w * this.h);
    getPixelsFromExistingTexture();
  }
  
  public synchronized void close() {
    if (this.id != 0) {
      try {
        GL11.glDeleteTextures(this.id);
      } catch (NullPointerException e) {
        log("MwTexture.close: null pointer exception (texture %d)", this.id);
      } 
      this.id = 0;
    } 
  }
  
  public synchronized void fillRect(int x, int y, int w, int h, int colour) {
    int offset = y * this.w + x;
    for (int j = 0; j < h; j++) {
      this.pixelBuf.position(offset + j * this.w);
      for (int i = 0; i < w; i++)
        this.pixelBuf.put(colour); 
    } 
  }
  
  public synchronized void getRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize) {
    int bufOffset = y * this.w + x;
    for (int i = 0; i < h; i++) {
      this.pixelBuf.position(bufOffset + i * this.w);
      this.pixelBuf.get(pixels, offset + i * scanSize, w);
    } 
  }
  
  public void bind() {
    GL11.glBindTexture(3553, this.id);
  }
  
  public void setTexParameters(int minFilter, int maxFilter, int textureWrap) {
    bind();
    GL11.glTexParameteri(3553, 10242, textureWrap);
    GL11.glTexParameteri(3553, 10243, textureWrap);
    GL11.glTexParameteri(3553, 10241, minFilter);
    GL11.glTexParameteri(3553, 10240, maxFilter);
  }
  
  private synchronized void getPixelsFromExistingTexture() {
    try {
      bind();
      this.pixelBuf.clear();
      GL11.glGetTexImage(3553, 0, 32993, 5121, this.pixelBuf);
      this.pixelBuf.limit(this.w * this.h);
    } catch (NullPointerException e) {
      log("MwTexture.getPixels: null pointer exception (texture %d)", this.id);
    } 
  }
  
  private void log(String s, int id) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\command\Texture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */