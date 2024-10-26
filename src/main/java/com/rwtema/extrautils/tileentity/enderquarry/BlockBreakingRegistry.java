package com.rwtema.extrautils.tileentity.enderquarry;

import com.rwtema.extrautils.LogHelper;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class BlockBreakingRegistry {
  public static BlockBreakingRegistry instance = new BlockBreakingRegistry();
  
  public static HashMap<Block, entry> entries = new HashMap<Block, entry>();
  
  public static Set<String> methodNames = null;
  
  public static Map<String, Boolean> names = new HashMap<String, Boolean>();
  
  public static LaunchClassLoader cl = (LaunchClassLoader)BlockBreakingRegistry.class.getClassLoader();
  
  public static boolean blackList(Block id) {
    return ((entry)entries.get(id)).blackList;
  }
  
  public static boolean isSpecial(Block id) {
    return ((entry)entries.get(id)).isSpecial;
  }
  
  public static boolean isFence(Block id) {
    return ((entry)entries.get(id)).isFence;
  }
  
  public static boolean isFluid(Block id) {
    return ((entry)entries.get(id)).isFluid;
  }
  
  public void setupBreaking() {
    if (methodNames == null) {
      methodNames = new HashSet<String>();
      for (Method m : BlockDummy.class.getDeclaredMethods())
        methodNames.add(m.getName()); 
    } else {
      return;
    } 
    for (Object aBlockRegistry : Block.blockRegistry)
      entries.put((Block)aBlockRegistry, new entry()); 
    ((entry)entries.get(Blocks.torch)).blackList = true;
    for (Object aBlockRegistry : Block.blockRegistry) {
      Block block = (Block)aBlockRegistry;
      entry e = entries.get(block);
      String name = block.getClass().getName();
      if (block.getUnlocalizedName() != null)
        name = block.getUnlocalizedName(); 
      try {
        name = Block.blockRegistry.getNameForObject(block);
      } catch (Exception err) {
        LogHelper.error("Error getting name for block " + name, new Object[0]);
        err.printStackTrace();
      } 
      e.isFence = false;
      try {
        e.isFence = (block.getRenderType() == 11);
      } catch (Exception err) {
        LogHelper.error("Error checking block class code: Exception calling getRenderType() on block " + name, new Object[0]);
        err.printStackTrace();
      } catch (NoClassDefFoundError err) {
        throw new RuntimeException("Serious error calling getRenderType() on block " + name + " : Likely cause is client-side code is being called server-side", err);
      } catch (Throwable err) {
        throw new RuntimeException("Serious error calling getRenderType() on block " + name, err);
      } 
      e.isFence = (e.isFence || block instanceof net.minecraft.block.BlockFence);
      if (block instanceof net.minecraft.block.BlockLiquid || block instanceof net.minecraftforge.fluids.IFluidBlock) {
        e.blackList = true;
        e.isFluid = true;
      } 
      e.isSpecial = hasSpecialBreaking(block.getClass());
    } 
  }
  
  public boolean hasSpecialBreaking(Class clazz) {
    if (clazz == null || clazz.equals(Block.class))
      return false; 
    if (names.containsKey(clazz.getName()))
      return ((Boolean)names.get(clazz.getName())).booleanValue(); 
    LogHelper.fine("Checking class for special block breaking code: " + clazz.getName(), new Object[0]);
    try {
      byte[] bytes;
      if (clazz.getClassLoader() instanceof LaunchClassLoader) {
        bytes = ((LaunchClassLoader)clazz.getClassLoader()).getClassBytes(clazz.getName());
      } else {
        bytes = cl.getClassBytes(clazz.getName());
      } 
      ClassNode classNode = new ClassNode();
      ClassReader classReader = new ClassReader(bytes);
      classReader.accept((ClassVisitor)classNode, 0);
      for (MethodNode method : classNode.methods) {
        if (methodNames.contains(method.name)) {
          LogHelper.fine("Detected special block breaking code in class: " + clazz.getName(), new Object[0]);
          names.put(clazz.getName(), Boolean.valueOf(true));
          return true;
        } 
      } 
    } catch (Throwable e) {
      try {
        for (Method m : clazz.getDeclaredMethods()) {
          if (methodNames.contains(m.getName())) {
            LogHelper.fine("Detected special block breaking code in class: " + clazz.getName(), new Object[0]);
            names.put(clazz.getName(), Boolean.valueOf(true));
            return true;
          } 
        } 
      } catch (Throwable e2) {
        LogHelper.error("Error checking block class code: " + clazz.getName(), new Object[0]);
        e.printStackTrace();
        e2.printStackTrace();
        names.put(clazz.getName(), Boolean.valueOf(true));
        return true;
      } 
    } 
    boolean result = hasSpecialBreaking(clazz.getSuperclass());
    names.put(clazz.getName(), Boolean.valueOf(result));
    return result;
  }
  
  public static class entry {
    public boolean isSpecial = false;
    
    public boolean blackList = false;
    
    public boolean isFence = false;
    
    public boolean isFluid = false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\BlockBreakingRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */