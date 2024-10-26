package com.rwtema.extrautils.core;

import java.util.Iterator;

public class CastIterator<T> implements Iterable<T>, Iterator<T> {
    Iterator<T> iterator;

    public CastIterator(Iterable iterable) {
        this(iterable.iterator());
    }

    public CastIterator(Iterator iterator) {
        this.iterator = iterator;
    }

    public Iterator<T> iterator() {
        return this;
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public T next() {
        return this.iterator.next();
    }

    public void remove() {
        this.iterator.remove();
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\CastIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
