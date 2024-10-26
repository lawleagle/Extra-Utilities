package com.rwtema.extrautils.helper;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public class WeakList<E> implements Iterable<E> {
  LinkedList<WeakReference<E>> list = new LinkedList<WeakReference<E>>();
  
  public boolean add(E a) {
    return (a != null && this.list.add(new WeakReference<E>(a)));
  }
  
  public void clear() {
    this.list.clear();
  }
  
  public Iterator<E> iterator() {
    return new WeakIterator();
  }
  
  public class WeakIterator implements Iterator<E> {
    E next;
    
    Iterator<WeakReference<E>> iter;
    
    public WeakIterator() {
      this.next = null;
      loadNext();
    }
    
    private void loadNext() {
      this.next = null;
      while (this.iter.hasNext()) {
        this.next = ((WeakReference<E>)this.iter.next()).get();
        if (this.next != null)
          return; 
        this.iter.remove();
      } 
    }
    
    public boolean hasNext() {
      return (this.next != null);
    }
    
    public E next() {
      E e = this.next;
      loadNext();
      return e;
    }
    
    public void remove() {
      this.iter.remove();
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\WeakList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */