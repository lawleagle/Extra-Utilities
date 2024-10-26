package com.rwtema.extrautils.core;

import java.util.Objects;

public final class Tuple<U, V> {
  private final U a;
  
  private final V b;
  
  public Tuple(U a, V b) {
    this.a = a;
    this.b = b;
  }
  
  public U getA() {
    return this.a;
  }
  
  public V getB() {
    return this.b;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Tuple tuple = (Tuple)o;
    return (Objects.equals(this.a, tuple.a) && Objects.equals(this.b, tuple.b));
  }
  
  public int hashCode() {
    int result = (this.a != null) ? this.a.hashCode() : 0;
    result = 31 * result + ((this.b != null) ? this.b.hashCode() : 0);
    return result;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\Tuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */