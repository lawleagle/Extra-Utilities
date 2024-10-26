package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class Box {
  public float minX;
  
  public float minY;
  
  public float minZ;
  
  public float maxX;
  
  public float maxY;
  
  public float maxZ;
  
  public int offsetx = 0;
  
  public int offsety = 0;
  
  public int offsetz = 0;
  
  public String label = "";
  
  public IIcon texture = null;
  
  public IIcon[] textureSide = new IIcon[6];
  
  public boolean invisible = false;
  
  public boolean renderAsNormalBlock = false;
  
  public boolean[] invisibleSide = new boolean[6];
  
  public int uvRotateEast = 0;
  
  public int uvRotateWest = 0;
  
  public int uvRotateSouth = 0;
  
  public int uvRotateNorth = 0;
  
  public int uvRotateTop = 0;
  
  public int uvRotateBottom = 0;
  
  public int color = 16777215;
  
  public int[] rotAdd = new int[] { 0, 1, 2, 3 };
  
  public Box(String l, float par1, float par3, float par5, float par7, float par9, float par11) {
    this.label = l;
    this.minX = Math.min(par1, par7);
    this.minY = Math.min(par3, par9);
    this.minZ = Math.min(par5, par11);
    this.maxX = Math.max(par1, par7);
    this.maxY = Math.max(par3, par9);
    this.maxZ = Math.max(par5, par11);
  }
  
  public Box(float par1, float par3, float par5, float par7, float par9, float par11) {
    this("", par1, par3, par5, par7, par9, par11);
  }
  
  public Box setTextures(IIcon[] icons) {
    for (int i = 0; i < 6 && i < icons.length; i++)
      this.textureSide[i] = icons[i]; 
    return this;
  }
  
  public Box setTextureSides(Object... tex) {
    this.textureSide = new IIcon[6];
    int s = 0;
    for (Object aTex : tex) {
      if (aTex instanceof Integer) {
        s = ((Integer)aTex).intValue();
      } else if (aTex instanceof IIcon && 
        s < 6 && s >= 0) {
        this.textureSide[s] = (IIcon)aTex;
        s++;
      } 
    } 
    return this;
  }
  
  public Box setColor(int col) {
    this.color = col;
    return this;
  }
  
  public Box setAllSideInvisible() {
    for (int i = 0; i < 6; i++)
      this.invisibleSide[i] = true; 
    return this;
  }
  
  public Box setSideInvisible(Object... tex) {
    int s = 0;
    for (Object aTex : tex) {
      if (aTex instanceof Integer) {
        s = ((Integer)aTex).intValue();
        this.invisibleSide[s] = true;
      } else if (aTex instanceof Boolean) {
        this.invisibleSide[s] = ((Boolean)aTex).booleanValue();
        s++;
      } 
    } 
    return this;
  }
  
  public Box setTexture(IIcon l) {
    this.texture = l;
    return this;
  }
  
  public Box setLabel(String l) {
    this.label = l;
    return this;
  }
  
  public Box copy() {
    return new Box(this.label, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
  }
  
  public void setBounds(float par1, float par3, float par5, float par7, float par9, float par11) {
    this.minX = Math.min(par1, par7);
    this.minY = Math.min(par3, par9);
    this.minZ = Math.min(par5, par11);
    this.maxX = Math.max(par1, par7);
    this.maxY = Math.max(par3, par9);
    this.maxZ = Math.max(par5, par11);
  }
  
  public Box offset(float x, float y, float z) {
    this.minX += x;
    this.minY += y;
    this.minZ += z;
    this.maxX += x;
    this.maxY += y;
    this.maxZ += z;
    return this;
  }
  
  public Box rotateY(int numRotations) {
    if (numRotations == 0)
      return this; 
    if (numRotations < 0)
      numRotations += 4; 
    numRotations &= 0x3;
    for (int i = 0; i < numRotations; i++) {
      Box prev = copy();
      this.minZ = prev.minX;
      this.maxZ = prev.maxX;
      this.minX = 1.0F - prev.maxZ;
      this.maxX = 1.0F - prev.minZ;
      IIcon temp = this.textureSide[2];
      this.textureSide[2] = this.textureSide[4];
      this.textureSide[4] = this.textureSide[3];
      this.textureSide[3] = this.textureSide[5];
      this.textureSide[5] = temp;
    } 
    this.uvRotateTop = (this.uvRotateTop + this.rotAdd[numRotations]) % 2;
    this.uvRotateBottom = (this.uvRotateBottom + this.rotAdd[numRotations]) % 2;
    return this;
  }
  
  public Box fillIcons(final Block b, final int meta) {
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          public void exectuteClientCode() {
            for (int side = 0; side < 6; side++)
              Box.this.textureSide[side] = b.getIcon(side, meta); 
          }
        });
    return this;
  }
  
  public Box swapIcons(int a, int b) {
    IIcon temp = this.textureSide[a];
    this.textureSide[a] = this.textureSide[b];
    this.textureSide[b] = temp;
    return this;
  }
  
  public Box rotateToSideTex(ForgeDirection dir) {
    Box prev = copy();
    rotateToSide(dir);
    switch (dir) {
      case UP:
        swapIcons(0, 1);
        this.uvRotateEast = 3;
        this.uvRotateWest = 3;
        this.uvRotateSouth = 3;
        this.uvRotateNorth = 3;
        break;
      case NORTH:
        swapIcons(1, 3);
        swapIcons(0, 2);
        this.uvRotateSouth = 2;
        this.uvRotateNorth = 1;
        this.uvRotateTop = 3;
        this.uvRotateBottom = 3;
        break;
      case SOUTH:
        swapIcons(1, 2);
        swapIcons(0, 3);
        this.uvRotateSouth = 1;
        this.uvRotateNorth = 2;
        break;
      case WEST:
        swapIcons(1, 5);
        swapIcons(0, 4);
        this.uvRotateEast = 2;
        this.uvRotateWest = 1;
        this.uvRotateTop = 1;
        this.uvRotateBottom = 2;
        break;
      case EAST:
        swapIcons(1, 4);
        swapIcons(0, 5);
        this.uvRotateEast = 1;
        this.uvRotateWest = 2;
        this.uvRotateTop = 2;
        this.uvRotateBottom = 1;
        break;
    } 
    return this;
  }
  
  public Box rotateToSide(ForgeDirection dir) {
    Box prev = copy();
    switch (dir) {
      case UP:
        this.minY = 1.0F - prev.maxY;
        this.maxY = 1.0F - prev.minY;
        break;
      case NORTH:
        this.minZ = prev.minY;
        this.maxZ = prev.maxY;
        this.minY = prev.minX;
        this.maxY = prev.maxX;
        this.minX = prev.minZ;
        this.maxX = prev.maxZ;
        break;
      case SOUTH:
        this.minZ = 1.0F - prev.maxY;
        this.maxZ = 1.0F - prev.minY;
        this.minY = prev.minX;
        this.maxY = prev.maxX;
        this.minX = 1.0F - prev.maxZ;
        this.maxX = 1.0F - prev.minZ;
        break;
      case WEST:
        this.minX = prev.minY;
        this.maxX = prev.maxY;
        this.minY = prev.minX;
        this.maxY = prev.maxX;
        this.minZ = 1.0F - prev.maxZ;
        this.maxZ = 1.0F - prev.minZ;
        break;
      case EAST:
        this.minX = 1.0F - prev.maxY;
        this.maxX = 1.0F - prev.minY;
        this.minY = prev.minX;
        this.maxY = prev.maxX;
        break;
    } 
    return this;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\Box.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */