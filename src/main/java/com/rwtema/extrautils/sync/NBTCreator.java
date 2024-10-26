package com.rwtema.extrautils.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.nbt.NBTTagCompound;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class NBTCreator {
  public static LaunchClassLoader cl = (LaunchClassLoader)NBTCreator.class.getClassLoader();
  
  public static String type = Type.getDescriptor(Sync.class);
  
  public static AutoNBT createAutoNBT(Class<?> clazz) {
    byte[] bytes;
    String targetName = clazz.getName();
    String targetType = Type.getDescriptor(clazz);
    try {
      bytes = cl.getClassBytes(targetName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
    List<FieldNode> fields = new ArrayList<FieldNode>();
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept((ClassVisitor)classNode, 1);
    for (FieldNode field : classNode.fields) {
      for (AnnotationNode ann : field.visibleAnnotations) {
        if (type.equals(ann.desc))
          fields.add(field); 
      } 
    } 
    ClassWriter cw = new ClassWriter(1);
    String name = "FLM_ItemWrench";
    String superName = Type.getInternalName(AutoNBT.class);
    cw.visit(50, 33, name, null, superName, new String[0]);
    cw.visitSource(".dynamic", null);
    MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitMethodInsn(183, superName, "<init>", "()V", false);
    mv.visitInsn(177);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    mv = cw.visitMethod(1, "writeToNBT", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Object;)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 2);
    mv.visitTypeInsn(192, targetType);
    mv.visitVarInsn(58, 3);
    for (FieldNode field : fields) {
      mv.visitVarInsn(25, 1);
      mv.visitLdcInsn(field.name);
      mv.visitVarInsn(25, 3);
      mv.visitFieldInsn(180, targetType, field.name, field.desc);
      mv.visitMethodInsn(184, getTargetForField(field), "save", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/String;" + field.desc + ")V", false);
    } 
    mv.visitInsn(177);
    mv.visitEnd();
    mv = cw.visitMethod(1, "readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/Object;)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 2);
    mv.visitTypeInsn(192, targetType);
    mv.visitVarInsn(58, 3);
    for (FieldNode field : fields) {
      mv.visitVarInsn(25, 1);
      mv.visitLdcInsn(field.name);
      mv.visitVarInsn(25, 3);
      mv.visitFieldInsn(180, targetType, field.name, field.desc);
      mv.visitMethodInsn(184, getTargetForField(field), "save", "(Lnet/minecraft/nbt/NBTTagCompound;Ljava/lang/String;" + field.desc + ")V", false);
    } 
    mv.visitInsn(177);
    mv.visitEnd();
    cw.visitEnd();
    return null;
  }
  
  private static final HashMap<Type, Class<?>> handlers = new HashMap<Type, Class<?>>();
  
  static {
    handlers.put(Type.BOOLEAN_TYPE, NBTHandlers.NBTHandlerBoolean.class);
    handlers.put(Type.BYTE_TYPE, NBTHandlers.NBTHandlerByte.class);
    handlers.put(Type.SHORT_TYPE, NBTHandlers.NBTHandlerShort.class);
    handlers.put(Type.INT_TYPE, NBTHandlers.NBTHandlerInt.class);
    handlers.put(Type.LONG_TYPE, NBTHandlers.NBTHandlerLong.class);
    handlers.put(Type.FLOAT_TYPE, NBTHandlers.NBTHandlerFloat.class);
    handlers.put(Type.DOUBLE_TYPE, NBTHandlers.NBTHandlerDouble.class);
    handlers.put(Type.getType(byte[].class), NBTHandlers.NBTHandlerByteArray.class);
    handlers.put(Type.getType(String.class), NBTHandlers.NBTHandlerString.class);
    handlers.put(Type.getType(NBTTagCompound.class), NBTHandlers.NBTHandlerNBT.class);
    handlers.put(Type.getType(int[].class), NBTHandlers.NBTHandlerIntArray.class);
    handlers.put(Type.getType(ItemStack.class), NBTHandlers.NBTHandlerItemStack.class);
  }
  
  public static String getTargetForField(FieldNode field) {
    return Type.getDescriptor(handlers.get(Type.getType(field.desc)));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sync\NBTCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */