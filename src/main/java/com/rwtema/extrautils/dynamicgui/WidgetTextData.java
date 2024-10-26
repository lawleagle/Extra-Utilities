package com.rwtema.extrautils.dynamicgui;

import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import org.lwjgl.opengl.GL11;

public abstract class WidgetTextData extends WidgetText implements IWidget {
  public Object[] curData = null;
  
  public WidgetTextData(int x, int y, int align, int color) {
    super(x, y, align, color, null);
  }
  
  public WidgetTextData(int x, int y, int w, int h, int align, int color) {
    super(x, y, w, h, align, color, null);
  }
  
  public WidgetTextData(int x, int y, int w) {
    super(x, y, null, w);
  }
  
  public static Object getNBTBaseData(NBTBase nbt) {
    if (nbt == null)
      return null; 
    switch (nbt.getId()) {
      case 1:
        return Byte.valueOf(((NBTTagByte)nbt).func_150290_f());
      case 2:
        return Short.valueOf(((NBTTagShort)nbt).func_150289_e());
      case 3:
        return Integer.valueOf(((NBTTagInt)nbt).func_150287_d());
      case 4:
        return Long.valueOf(((NBTTagLong)nbt).func_150291_c());
      case 5:
        return Float.valueOf(((NBTTagFloat)nbt).func_150288_h());
      case 6:
        return Double.valueOf(((NBTTagDouble)nbt).func_150286_g());
      case 8:
        return ((NBTTagString)nbt).func_150285_a_();
      case 10:
        return nbt;
    } 
    return null;
  }
  
  public static NBTBase getNBTBase(Object o) {
    if (o instanceof Integer)
      return (NBTBase)new NBTTagInt(((Integer)o).intValue()); 
    if (o instanceof Short)
      return (NBTBase)new NBTTagShort(((Short)o).shortValue()); 
    if (o instanceof Long)
      return (NBTBase)new NBTTagLong(((Long)o).longValue()); 
    if (o instanceof String)
      return (NBTBase)new NBTTagString((String)o); 
    if (o instanceof Double)
      return (NBTBase)new NBTTagDouble(((Double)o).doubleValue()); 
    if (o instanceof Float)
      return (NBTBase)new NBTTagFloat(((Float)o).floatValue()); 
    if (o instanceof NBTTagCompound)
      return (NBTBase)o; 
    if (o instanceof Byte)
      return (NBTBase)new NBTTagByte(((Byte)o).byteValue()); 
    LogHelper.debug("Can't find type for " + o, new Object[0]);
    throw null;
  }
  
  public abstract int getNumParams();
  
  public abstract Object[] getData();
  
  public abstract String getConstructedText();
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    Object[] newData = getData();
    if (this.curData == null) {
      this.curData = new Object[getNumParams()];
      changesOnly = false;
    } 
    NBTTagCompound tag = new NBTTagCompound();
    for (int i = 0; i < this.curData.length; i++) {
      if (newData[i] != null && (!changesOnly || this.curData[i] == null || !this.curData[i].toString().equals(newData[i].toString()))) {
        NBTBase nbtBase = getNBTBase(newData[i]);
        if (nbtBase != null)
          tag.setTag(Integer.toString(i), nbtBase); 
      } 
      this.curData[i] = newData[i];
    } 
    if (tag.hasNoTags())
      return null; 
    return tag;
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {
    if (this.curData == null)
      this.curData = new Object[getNumParams()]; 
    int n = getNumParams();
    for (int i = 0; i < n; i++) {
      NBTBase base = packet.getTag(Integer.toString(i));
      if (base != null)
        this.curData[i] = getNBTBaseData(base); 
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    if (this.curData == null)
      return; 
    int x = getX() + (1 - this.align) * (getW() - gui.getFontRenderer().getStringWidth(getMsgClient())) / 2;
    gui.getFontRenderer().drawSplitString(getConstructedText(), guiLeft + x, guiTop + getY(), getW(), 4210752);
    manager.bindTexture(gui.getWidgets());
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetTextData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */