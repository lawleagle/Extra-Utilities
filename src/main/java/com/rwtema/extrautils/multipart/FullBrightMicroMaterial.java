package com.rwtema.extrautils.multipart;

import codechicken.lib.render.CCRenderPipeline;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.ColourMultiplier;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.BlockMicroMaterial;
import com.rwtema.extrautils.block.BlockGreenScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

public class FullBrightMicroMaterial extends BlockMicroMaterial {
  public FullBrightMicroMaterial(BlockGreenScreen block, int meta) {
    super((Block)block, meta);
  }
  
  @SideOnly(Side.CLIENT)
  public int getColour(int pass) {
    return block().getRenderColor(meta()) << 8 | 0xFF;
  }
  
  public int getLightValue() {
    return BlockGreenScreen.getLightLevel(meta());
  }
  
  @SideOnly(Side.CLIENT)
  public void renderMicroFace(Vector3 pos, int pass, Cuboid6 bounds) {
    CCRenderPipeline.PipelineBuilder builder = CCRenderState.pipeline.builder();
    builder.add((CCRenderState.IVertexOperation)pos.translation()).add((CCRenderState.IVertexOperation)icont());
    builder.add((CCRenderState.IVertexOperation)ColourMultiplier.instance(getColour(pass)));
    builder.add(Lighting.instance);
    builder.render();
  }
  
  public static class Lighting implements CCRenderState.IVertexOperation {
    public static Lighting instance = new Lighting();
    
    public static int id = CCRenderState.registerOperation();
    
    public boolean load() {
      if (!CCRenderState.computeLighting)
        return false; 
      CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
      CCRenderState.pipeline.addDependency(CCRenderState.lightCoordAttrib);
      return true;
    }
    
    public void operate() {
      CCRenderState.setBrightness(16711935);
    }
    
    public int operationID() {
      return id;
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\FullBrightMicroMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */