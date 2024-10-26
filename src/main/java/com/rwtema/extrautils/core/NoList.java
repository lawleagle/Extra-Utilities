package com.rwtema.extrautils.core;

import java.util.AbstractList;
import java.util.List;

public class NoList<E> extends AbstractList<E> implements List<E> {
  public int size() {
    return 0;
  }
  
  public boolean isEmpty() {
    return true;
  }
  
  public boolean contains(Object o) {
    return false;
  }
  
  public boolean add(E e) {
    return false;
  }
  
  public boolean remove(Object o) {
    return false;
  }
  
  public void clear() {}
  
  public E get(int index) {
    return null;
  }
  
  public E set(int index, E element) {
    return null;
  }
  
  public void add(int index, E element) {}
  
  public E remove(int index) {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\NoList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */