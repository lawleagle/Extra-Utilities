package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.block.BlockColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBlockColorData extends TileEntity {
  public float[][] palette = new float[16][3];
  
  private int rerenderTimer = 0;
  
  private int rerenderDelay = 20;
  
  public TileEntityBlockColorData() {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.palette[i][j] == 0.0F)
          this.palette[i][j] = BlockColor.initColor[i][j]; 
      } 
    } 
  }
  
  public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound);
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 3; j++) {
        if (par1NBTTagCompound.hasKey("col" + i + "_" + j)) {
          if (par1NBTTagCompound.getTag("col" + i + "_" + j) instanceof net.minecraft.nbt.NBTTagFloat)
            this.palette[i][j] = par1NBTTagCompound.getFloat("col" + i + "_" + j); 
        } else {
          this.palette[i][j] = BlockColor.initColor[i][j];
        } 
      } 
    } 
  }
  
  public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound);
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 3; j++)
        par1NBTTagCompound.setFloat("col" + i + "_" + j, this.palette[i][j]); 
    } 
  }
  
  public void setColor(int metadata, float r, float g, float b) {
    if (this.worldObj.isRemote)
      return; 
    if (this.palette[metadata][0] == r && this.palette[metadata][1] == g && this.palette[metadata][2] == b)
      return; 
    this.palette[metadata][0] = r;
    this.palette[metadata][1] = g;
    this.palette[metadata][2] = b;
    boolean notDefault = false;
    for (int i = 0; i < 16 && !notDefault; i++) {
      for (int j = 0; j < 3 && !notDefault; j++) {
        if (this.palette[i][j] != BlockColor.initColor[i][j]) {
          notDefault = true;
          break;
        } 
      } 
    } 
    if (notDefault) {
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    } else {
      this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    } 
    markDirty();
  }
  
  public Packet getDescriptionPacket() {
    NBTTagCompound t = new NBTTagCompound();
    writeToNBT(t);
    return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
  }
  
  @SideOnly(Side.CLIENT)
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    readFromNBT(pkt.func_148857_g());
    if (this.worldObj.isRemote && 
      this.rerenderTimer == 0) {
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, 0, this.zCoord, this.xCoord + 16, 255, this.zCoord + 16);
      this.rerenderTimer = this.rerenderDelay;
      this.rerenderDelay = (int)(this.rerenderDelay * 1.1D);
    } 
  }
  
  public void updateEntity() {
    if (this.worldObj.isRemote)
      if (this.rerenderTimer > 0) {
        this.rerenderTimer--;
        if (this.rerenderTimer == 0)
          this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, 0, this.zCoord, this.xCoord + 16, 255, this.zCoord + 16); 
      } else if (this.rerenderDelay > 10) {
        this.rerenderDelay--;
      } else {
        this.rerenderDelay = 10;
      }  
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityBlockColorData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */