package com.rwtema.extrautils.asm;

import com.google.common.base.Throwables;
import java.lang.reflect.Method;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fluids.FluidStack;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class FluidIDGetter {
  public static final Class<? extends IFluidLegacy> clazz;
  
  public static final IFluidLegacy fluidLegacy;
  
  static {
    Method m_defineClass;
    LaunchClassLoader cl = (LaunchClassLoader)FluidIDGetter.class.getClassLoader();
    try {
      m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
      m_defineClass.setAccessible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
    String classname = "XU_fluidstack_compat_code";
    String superName = Type.getInternalName(Object.class);
    ClassWriter cw = new ClassWriter(0);
    cw.visit(50, 33, classname, null, superName, new String[] { Type.getInternalName(IFluidLegacy.class) });
    cw.visitSource(".dynamic", null);
    MethodVisitor constructor = cw.visitMethod(1, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), null, null);
    constructor.visitCode();
    constructor.visitVarInsn(25, 0);
    constructor.visitMethodInsn(183, superName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
    constructor.visitInsn(177);
    constructor.visitMaxs(1, 1);
    constructor.visitEnd();
    MethodVisitor getData = cw.visitMethod(1, "getID", Type.getMethodDescriptor(Type.INT_TYPE, new Type[] { Type.getType(FluidStack.class) }), null, null);
    getData.visitCode();
    getData.visitVarInsn(25, 1);
    try {
      FluidStack.class.getDeclaredMethod("getFluidID", new Class[0]);
      getData.visitMethodInsn(182, "net/minecraftforge/fluids/FluidStack", "getFluidID", "()I", false);
    } catch (NoSuchMethodException e) {
      getData.visitFieldInsn(180, "net/minecraftforge/fluids/FluidStack", "fluidID", "I");
    } 
    getData.visitInsn(172);
    getData.visitMaxs(1, 2);
    getData.visitEnd();
    cw.visitEnd();
    byte[] b = cw.toByteArray();
    try {
      clazz = (Class<? extends IFluidLegacy>)m_defineClass.invoke(cl, new Object[] { classname, b, Integer.valueOf(0), Integer.valueOf(b.length) });
      fluidLegacy = clazz.newInstance();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    } 
  }
  
  public static interface IFluidLegacy {
    int getID(FluidStack param1FluidStack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\asm\FluidIDGetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */