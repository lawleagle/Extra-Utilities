package com.rwtema.extrautils.asm;

import com.google.common.base.Throwables;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class RemoteCallFactory {
  static LaunchClassLoader cl = (LaunchClassLoader)RemoteCallFactory.class.getClassLoader();

  static Method m_defineClass;

  static {
    try {
      m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
      m_defineClass.setAccessible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static IObjectEvaluate<ItemStack> pulverizer = getEvaluator("cofh.thermalexpansion.util.crafting.PulverizerManager", "recipeExists", ItemStack.class);

  public static <T> IObjectEvaluate<T> getEvaluator(String baseClass, String baseMethod, Class param) {
    try {
      Class<?> clazz = Class.forName(baseClass);
      Method method = clazz.getDeclaredMethod(baseMethod, new Class[] { param });
      assert Modifier.isStatic(method.getModifiers());
    } catch (Exception e) {
      return null;
    }
    String classname = "XU_caller_" + baseClass.replace('.', '_') + "_" + baseMethod + "_" + param.getSimpleName();
    String superName = Type.getInternalName(Object.class);
    ClassWriter cw = new ClassWriter(0);
    cw.visit(50, 33, classname, null, superName, new String[] { Type.getInternalName(IObjectEvaluate.class) });
    cw.visitSource(".dynamic", null);
    MethodVisitor constructor = cw.visitMethod(1, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), null, null);
    constructor.visitCode();
    constructor.visitVarInsn(25, 0);
    constructor.visitMethodInsn(183, superName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
    constructor.visitInsn(177);
    constructor.visitMaxs(1, 1);
    constructor.visitEnd();
    MethodVisitor getData = cw.visitMethod(1, "evaluate", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, new Type[] { Type.getType(Object.class) }), null, null);
    getData.visitCode();
    if (param != null) {
      getData.visitVarInsn(25, 1);
      if (param != Object.class)
        getData.visitTypeInsn(192, Type.getInternalName(param));
      getData.visitMethodInsn(184, baseClass.replace('.', '/'), baseMethod, Type.getMethodDescriptor(Type.BOOLEAN_TYPE, new Type[] { Type.getType(param) }), false);
    } else {
      getData.visitMethodInsn(184, baseClass.replace('.', '/'), baseMethod, Type.getMethodDescriptor(Type.BOOLEAN_TYPE, new Type[0]), false);
    }
    getData.visitInsn(172);
    getData.visitMaxs(1, 2);
    getData.visitEnd();
    cw.visitEnd();
    byte[] b = cw.toByteArray();
    try {
      Class<? extends IObjectEvaluate> clazz = (Class<? extends IObjectEvaluate>)m_defineClass.invoke(cl, new Object[] { classname, b, Integer.valueOf(0), Integer.valueOf(b.length) });
      return clazz.newInstance();
    } catch (Throwable e) {
      throw Throwables.propagate(e);
    }
  }

  public static IObjectEvaluate nullValuate = new IObjectEvaluate() {
      public boolean evaluate(Object object) {
        return false;
      }
    };

  public static interface IObjectEvaluate<T> {
    boolean evaluate(T param1T);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\asm\RemoteCallFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
