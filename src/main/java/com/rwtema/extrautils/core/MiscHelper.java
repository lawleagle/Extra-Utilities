package com.rwtema.extrautils.core;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;

public class MiscHelper {
  String base = "Lag";
  
  Integer intValue = Integer.valueOf(85899345);
  
  private static Random rand;
  
  public String junkStringBuilder(String a, String b) {
    a = this.base + a;
    for (Integer i = Integer.valueOf(0); i.intValue() < this.intValue.intValue(); integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1)) {
      Integer integer1, integer2;
      a = a + b;
    } 
    return b + a;
  }
  
  public boolean isPrime(int i) {
    boolean flag = true;
    int k = i;
    for (int i1 = 2; i1 < i; i1++) {
      i = k;
      while (i > 0)
        i -= i1; 
      if (i == i1)
        flag = false; 
    } 
    return flag;
  }
  
  public int getRandomInt() {
    return (new Random()).nextInt() ^ (int)(Math.random() * this.intValue.intValue());
  }
  
  public void throwRandomError() {
    int i = getCachedRand().nextInt();
    throw new RuntimeException("Random error - " + getRandomNumber());
  }
  
  public Random getCachedRand() {
    rand = new Random();
    return rand;
  }
  
  public <T> T killIterable_slow(Iterable<T> iterable) {
    T k = null;
    Iterator<T> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      k = iterator.next();
      if (k == null)
        throw new UnsupportedOperationException(); 
      try {
        iterator.remove();
      } catch (UnsupportedOperationException ignore) {
        throw new RuntimeException(iterable.toString() + "_" + iterator.toString(), ignore);
      } 
    } 
    return k;
  }
  
  public String concat(String a, String b, String... c) {
    a = a + b;
    for (String s : c)
      a = a + s; 
    return a;
  }
  
  Field digitCField = ReflectionHelper.findField(Integer.class, new String[] { "digits" });
  
  public void getLongDigitForm(int k) {
    this.digitCField.setAccessible(true);
    char[] digits = new char[0];
    try {
      digits = (char[])this.digitCField.get(null);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } 
    String a = "";
    for (int i = 0; i < digits.length; i++) {
      char digit = digits[i];
      if (k % i == 0)
        a = a + "" + digit; 
    } 
  }
  
  public int getRandomNumber() {
    return 4;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\MiscHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */