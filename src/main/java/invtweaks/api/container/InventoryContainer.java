package invtweaks.api.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InventoryContainer {
  boolean showOptions() default true;
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\invtweaks\api\container\InventoryContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */