package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityFilterPipe;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.World;

public class RegisterPipeParts implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter {
  public TMultiPart createPart(String name, boolean client) {
    if (name.equals("extrautils:transfer_pipe_filter"))
      return (TMultiPart)new FilterPipePart(); 
    if (name.equals("extrautils:transfer_pipe"))
      return (TMultiPart)new PipePart(); 
    return null;
  }
  
  public void init() {
    MultiPartRegistry.registerConverter(this);
    MultiPartRegistry.registerParts(this, new String[] { "extrautils:transfer_pipe", "extrautils:transfer_pipe_filter" });
  }
  
  public Iterable<Block> blockTypes() {
    Set<Block> set = new HashSet<Block>();
    set.add(ExtraUtils.transferPipe);
    set.add(ExtraUtils.transferPipe2);
    return set;
  }
  
  public TMultiPart convert(World world, BlockCoord pos) {
    Block id = world.getBlock(pos.x, pos.y, pos.z);
    int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
    if (id == ExtraUtils.transferPipe || id == ExtraUtils.transferPipe2) {
      if (id == ExtraUtils.transferPipe2)
        meta += 16; 
      if (meta == 9) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityFilterPipe) {
          InventoryBasic t = ((TileEntityFilterPipe)world.getTileEntity(pos.x, pos.y, pos.z)).items;
          return (TMultiPart)new FilterPipePart(t);
        } 
        return (TMultiPart)new FilterPipePart();
      } 
      return (TMultiPart)new PipePart(meta);
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\RegisterPipeParts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */