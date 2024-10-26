package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroblockRender;
import codechicken.multipart.MultipartGenerator;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PartFence extends PartConnecting implements IFence {
  public static final Cuboid6[] partCuboids = new Cuboid6[] { new Cuboid6(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new Cuboid6(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D), new Cuboid6(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D), new Cuboid6(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D), new Cuboid6(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D), new Cuboid6(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D) };
  
  public static final Cuboid6[] renderCuboids1 = new Cuboid6[] { null, null, new Cuboid6(0.4375D, 0.75D, 0.0D, 0.5625D, 0.9375D, 0.375D), new Cuboid6(0.4375D, 0.75D, 0.625D, 0.5625D, 0.9375D, 1.0D), new Cuboid6(0.0D, 0.75D, 0.4375D, 0.375D, 0.9375D, 0.5625D), new Cuboid6(0.625D, 0.75D, 0.4375D, 1.0D, 0.9375D, 0.5625D) };
  
  public static final Cuboid6[] renderCuboids2 = new Cuboid6[] { null, null, new Cuboid6(0.4375D, 0.375D, 0.0D, 0.5625D, 0.5625D, 0.375D), new Cuboid6(0.4375D, 0.375D, 0.625D, 0.5625D, 0.5625D, 1.0D), new Cuboid6(0.0D, 0.375D, 0.4375D, 0.375D, 0.5625D, 0.5625D), new Cuboid6(0.625D, 0.375D, 0.4375D, 1.0D, 0.5625D, 0.5625D) };
  
  public static final String type = "extrautils:fence";
  
  public PartFence() {}
  
  public PartFence(int material) {
    super(material);
  }
  
  public String getType() {
    return "extrautils:fence";
  }
  
  public Cuboid6 getBounds() {
    return partCuboids[0];
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    List<Cuboid6> t = new ArrayList<Cuboid6>();
    if (isEthereal())
      return t; 
    t.add(partCuboids[1].copy());
    for (int i = 2; i < 6; i++) {
      if ((this.connectionMask & 1 << i) != 0)
        t.add(partCuboids[i].copy()); 
    } 
    return t;
  }
  
  public boolean shouldConnect(int x, int y, int z, int direction) {
    Block l = world().getBlock(x, y, z);
    if (world().getTileEntity(x, y, z) instanceof IFence && 
      tile().canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[direction]))
      return ((TileMultipart)world().getTileEntity(x, y, z)).canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[Facing.oppositeSide[direction]]); 
    return (l.getMaterial().isOpaque() && l.renderAsNormalBlock() && tile().canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[direction]));
  }
  
  public void reloadShape() {
    int prevMask = this.connectionMask;
    this.connectionMask = 0;
    for (int i = 2; i < 6; i++) {
      if (shouldConnect(x() + Facing.offsetsXForSide[i], y() + Facing.offsetsYForSide[i], z() + Facing.offsetsZForSide[i], i))
        this.connectionMask |= 1 << i; 
    } 
    if (prevMask != this.connectionMask) {
      tile().notifyPartChange((TMultiPart)this);
      tile().markRender();
    } 
  }
  
  @SideOnly(Side.CLIENT)
  public boolean renderStatic(Vector3 pos, int pass) {
    reloadShape();
    if (this.mat == null)
      this.mat = MicroMaterialRegistry.getMaterial(this.material); 
    if (this.mat != null && this.mat.canRenderInPass(pass)) {
      MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, getRenderBounds(), 0);
      for (int i = 0; i < 6; i++) {
        if ((this.connectionMask & 1 << i) != 0) {
          MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, renderCuboids1[i], 1 << Facing.oppositeSide[i] | 1 << i);
          MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, renderCuboids2[i], 1 << Facing.oppositeSide[i] | 1 << i);
        } 
      } 
      return true;
    } 
    return false;
  }
  
  public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
    return getMaterial().getStrength(player);
  }
  
  public int getMetadata() {
    return 1;
  }
  
  public TMultiPart newPart(boolean client) {
    return (TMultiPart)new PartFence();
  }
  
  public TMultiPart placePart(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5, int materialID) {
    return (TMultiPart)new PartFence(materialID);
  }
  
  public void registerPassThroughs() {
    MultipartGenerator.registerPassThroughInterface(IFence.class.getName());
  }
  
  public void renderItem(ItemStack item, MicroMaterialRegistry.IMicroMaterial material) {
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, partCuboids[0], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids1[2], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids1[3], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids2[2], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids2[3], 0);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartFence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */