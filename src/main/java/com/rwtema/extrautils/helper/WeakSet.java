package com.rwtema.extrautils.helper;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public class WeakSet<E> extends AbstractSet<E> implements Set<E> {
  private transient WeakHashMap<E, Object> map;
  
  private static final Object PRESENT = new Object();
  
  public WeakSet() {
    this.map = new WeakHashMap<E, Object>();
  }
  
  public WeakSet(Collection<? extends E> c) {
    this.map = new WeakHashMap<E, Object>(Math.max((int)(c.size() / 0.75F) + 1, 16));
    addAll(c);
  }
  
  public WeakSet(int initialCapacity, float loadFactor) {
    this.map = new WeakHashMap<E, Object>(initialCapacity, loadFactor);
  }
  
  public WeakSet(int initialCapacity) {
    this.map = new WeakHashMap<E, Object>(initialCapacity);
  }
  
  WeakSet(int initialCapacity, float loadFactor, boolean dummy) {
    this.map = new WeakHashMap<E, Object>(initialCapacity, loadFactor);
  }
  
  public Iterator<E> iterator() {
    return this.map.keySet().iterator();
  }
  
  public int size() {
    return this.map.size();
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public boolean contains(Object o) {
    return this.map.containsKey(o);
  }
  
  public boolean add(E e) {
    return (this.map.put(e, PRESENT) == null);
  }
  
  public boolean remove(Object o) {
    return (this.map.remove(o) == PRESENT);
  }
  
  public void clear() {
    this.map.clear();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\WeakSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */