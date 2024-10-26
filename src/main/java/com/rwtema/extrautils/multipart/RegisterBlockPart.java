package com.rwtema.extrautils.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class RegisterBlockPart implements MultiPartRegistry.IPartConverter, MultiPartRegistry.IPartFactory {
  public Set<Block> t = null;
  
  Block block = null;
  
  Class<? extends TMultiPart> part = null;
  
  String name = "";
  
  public RegisterBlockPart(Block block, Class<? extends TMultiPart> part) {
    try {
      this.name = ((TMultiPart)part.getConstructor(new Class[0]).newInstance(new Object[0])).getType();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public RegisterBlockPart(Block block, Class<? extends TMultiPart> part, String name) {
    this.block = block;
    this.part = part;
    this.name = name;
  }
  
  public TMultiPart createPart(String name, boolean client) {
    if (name.equals(name))
      try {
        return this.part.getConstructor(new Class[0]).newInstance(new Object[0]);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }  
    return null;
  }
  
  public void init() {
    if (this.name.equals("") || this.block == null || this.part == null)
      return; 
    MultiPartRegistry.registerConverter(this);
    MultiPartRegistry.registerParts(this, new String[] { this.name });
  }
  
  public Iterable<Block> blockTypes() {
    if (this.t == null) {
      this.t = new HashSet<Block>();
      this.t.add(this.block);
    } 
    return this.t;
  }
  
  public TMultiPart convert(World world, BlockCoord pos) {
    Block id = world.getBlock(pos.x, pos.y, pos.z);
    if (id == this.block)
      try {
        return this.part.getConstructor(new Class[0]).newInstance(new Object[0]);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }  
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\RegisterBlockPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */