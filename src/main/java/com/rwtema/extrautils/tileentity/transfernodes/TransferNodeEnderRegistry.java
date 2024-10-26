package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;

public class TransferNodeEnderRegistry {
  private static final HashMap<Frequency, List<WeakReference<INodeBuffer>>> receptors = new HashMap<Frequency, List<WeakReference<INodeBuffer>>>();
  
  public static synchronized void clearTileRegistrations(INodeBuffer buffer) {
    synchronized (receptors) {
      Set<Frequency> t = null;
      for (Frequency fs : receptors.keySet()) {
        List<WeakReference<INodeBuffer>> list = receptors.get(fs);
        Iterator<WeakReference<INodeBuffer>> iter = list.iterator();
        while (iter.hasNext()) {
          INodeBuffer next = ((WeakReference<INodeBuffer>)iter.next()).get();
          if (next == null || next == buffer)
            iter.remove(); 
        } 
        if (list.isEmpty()) {
          if (t == null)
            t = new HashSet<Frequency>(); 
          t.add(fs);
        } 
      } 
      if (t != null)
        for (Frequency fs : t)
          receptors.remove(fs);  
    } 
  }
  
  public static synchronized void registerTile(Frequency freq, INodeBuffer buffer) {
    synchronized (receptors) {
      TileEntity a = buffer.getNode().getNode();
      for (Frequency fs : receptors.keySet()) {
        Iterator<WeakReference<INodeBuffer>> iter = ((List<WeakReference<INodeBuffer>>)receptors.get(fs)).iterator();
        while (iter.hasNext()) {
          INodeBuffer next = ((WeakReference<INodeBuffer>)iter.next()).get();
          if (next == null) {
            iter.remove();
            continue;
          } 
          TileEntity tileEntity = next.getNode().getNode();
          if (a.xCoord == tileEntity.xCoord && a.zCoord == tileEntity.zCoord && a.yCoord == tileEntity.yCoord && (a.getWorldObj()).provider.dimensionId == (tileEntity.getWorldObj()).provider.dimensionId)
            return; 
        } 
      } 
      List<WeakReference<INodeBuffer>> b = receptors.get(freq);
      if (b == null) {
        b = new ArrayList<WeakReference<INodeBuffer>>();
        receptors.put(freq, b);
      } 
      b.add(new WeakReference<INodeBuffer>(buffer));
    } 
  }
  
  public static synchronized void doTransfer(INodeBuffer sender, Frequency f, int no) {
    List<WeakReference<INodeBuffer>> b = receptors.get(f);
    if (b == null)
      return; 
    Collections.shuffle(b);
    Iterator<WeakReference<INodeBuffer>> iterator = b.iterator();
    while (iterator.hasNext()) {
      WeakReference<INodeBuffer> ref = iterator.next();
      INodeBuffer reciever = ref.get();
      if (reciever == null) {
        iterator.remove();
        continue;
      } 
      if (reciever != sender)
        sender.transferTo(reciever, no); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TransferNodeEnderRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */