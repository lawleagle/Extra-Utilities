package com.rwtema.extrautils.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class SafeCore implements IClassTransformer {
  private static Side INVALID_SIDE = (FMLLaunchHandler.side() == Side.CLIENT) ? Side.SERVER : Side.CLIENT;
  
  private static String SIDE_SERVER = Side.SERVER.name();
  
  private static String SIDE_CLIENT = Side.CLIENT.name();
  
  LaunchClassLoader cl = (LaunchClassLoader)SafeCore.class.getClassLoader();
  
  private static HashMap<String, Side> classSideHashMap = new HashMap<String, Side>();
  
  static {
    classSideHashMap.put(boolean.class.getName(), null);
    classSideHashMap.put(byte.class.getName(), null);
    classSideHashMap.put(char.class.getName(), null);
    classSideHashMap.put(double.class.getName(), null);
    classSideHashMap.put(float.class.getName(), null);
    classSideHashMap.put(int.class.getName(), null);
    classSideHashMap.put(long.class.getName(), null);
    classSideHashMap.put(short.class.getName(), null);
    classSideHashMap.put(void.class.getName(), null);
  }
  
  public String[] clientPrefixes = new String[] { "net.minecraft.", "net.minecraftforge.", "cpw.mods.fml", "com.rwtema.extrautils." };
  
  public Side getSide(String clazz) {
    if (classSideHashMap.containsKey(clazz))
      return classSideHashMap.get(clazz); 
    Side side = getSide_do(clazz);
    classSideHashMap.put(clazz, side);
    return side;
  }
  
  public Side getSide_do(String clazz) {
    byte[] bytes;
    if (clazz.indexOf('.') == -1)
      return null; 
    try {
      bytes = this.cl.getClassBytes(clazz);
    } catch (IOException e) {
      return INVALID_SIDE;
    } 
    if (bytes == null)
      return INVALID_SIDE; 
    boolean flag = true;
    for (String clientPrefix : this.clientPrefixes) {
      if (clazz.startsWith(clientPrefix)) {
        flag = false;
        break;
      } 
    } 
    if (flag)
      return null; 
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept((ClassVisitor)classNode, 0);
    return getSide(classNode.visibleAnnotations);
  }
  
  public Side getSide(List<AnnotationNode> anns) {
    if (anns == null)
      return null; 
    for (AnnotationNode ann : anns) {
      if (ann.desc.equals(Type.getDescriptor(SideOnly.class)) && 
        ann.values != null)
        for (int x = 0; x < ann.values.size() - 1; x += 2) {
          Object key = ann.values.get(x);
          Object value = ann.values.get(x + 1);
          if (key instanceof String && key.equals("value") && 
            value instanceof String[]) {
            String s = ((String[])value)[1];
            if (s.equals(SIDE_SERVER))
              return Side.SERVER; 
            if (s.equals(SIDE_CLIENT))
              return Side.CLIENT; 
          } 
        }  
    } 
    return null;
  }
  
  public boolean isInvalid(String clazz) {
    return (getSide(clazz) == INVALID_SIDE);
  }
  
  public boolean isInvalid(Type type) {
    while (type.getSort() == 9)
      type = type.getElementType(); 
    return (type.getSort() != 11 && isInvalid(type.getClassName()));
  }
  
  public boolean isInvalid(Type[] types) {
    for (Type type : types) {
      if (isInvalid(type))
        return true; 
    } 
    return false;
  }
  
  public byte[] transform(String s, String s2, byte[] bytes) {
    if (!s.startsWith("com.rwtema.extrautils"))
      return bytes; 
    ClassNode classNode = new ClassNode();
    ClassReader reader = new ClassReader(bytes);
    reader.accept((ClassVisitor)classNode, 0);
    Side side = getSide(classNode.visibleAnnotations);
    classSideHashMap.put(s, side);
    stripInvalidAnnotations(classNode.visibleAnnotations);
    for (Iterator<FieldNode> iterator1 = classNode.fields.iterator(); iterator1.hasNext(); ) {
      FieldNode field = iterator1.next();
      if (isInvalid(Type.getType(field.desc))) {
        iterator1.remove();
        continue;
      } 
      stripInvalidAnnotations(field.visibleAnnotations);
    } 
    for (Iterator<MethodNode> iterator = classNode.methods.iterator(); iterator.hasNext(); ) {
      MethodNode method = iterator.next();
      if (invalidMethod(method)) {
        iterator.remove();
        continue;
      } 
      stripInvalidAnnotations(method.visibleAnnotations);
    } 
    ClassWriter writer = new ClassWriter(1);
    classNode.accept(writer);
    return writer.toByteArray();
  }
  
  public void stripInvalidAnnotations(List<AnnotationNode> visibleAnnotations) {
    if (visibleAnnotations == null)
      return; 
    for (Iterator<AnnotationNode> iterator = visibleAnnotations.iterator(); iterator.hasNext(); ) {
      AnnotationNode visibleAnnotation = iterator.next();
      if (isInvalid(Type.getType(visibleAnnotation.desc)))
        iterator.remove(); 
    } 
  }
  
  public boolean invalidMethod(MethodNode method) {
    if (isInvalid(Type.getReturnType(method.desc)))
      return true; 
    for (Type type : Type.getArgumentTypes(method.desc)) {
      if (isInvalid(type))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\asm\SafeCore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */