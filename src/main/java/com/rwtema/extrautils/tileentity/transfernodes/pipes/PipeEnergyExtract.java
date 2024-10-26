package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeEnergyExtract extends PipeEnergy {
  public static final String name = "Energy_Extract";
  
  public PipeEnergyExtract() {
    super("Energy_Extract");
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_energy_extract;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return BlockTransferPipe.pipes_energy_extract;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_energy_extract;
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_nozzle_energy_extract;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeEnergyExtract.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */