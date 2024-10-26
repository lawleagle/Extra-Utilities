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
import net.minecraft.world.World;

public class PartWall extends PartConnecting implements IWall {
  public static final Cuboid6[] partCuboids = new Cuboid6[] { new Cuboid6(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new Cuboid6(0.25D, 0.0D, 0.25D, 0.75D, 1.5D, 0.75D), new Cuboid6(0.25D, 0.0D, 0.0D, 0.75D, 1.5D, 0.5D), new Cuboid6(0.25D, 0.0D, 0.5D, 0.75D, 1.5D, 1.0D), new Cuboid6(0.0D, 0.0D, 0.25D, 0.5D, 1.5D, 0.75D), new Cuboid6(0.5D, 0.0D, 0.25D, 1.0D, 1.5D, 0.75D) };
  
  public static final Cuboid6[] renderCuboids1 = new Cuboid6[] { null, null, new Cuboid6(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.25D), new Cuboid6(0.3125D, 0.0D, 0.75D, 0.6875D, 0.8125D, 1.0D), new Cuboid6(0.0D, 0.0D, 0.3125D, 0.25D, 0.8125D, 0.6875D), new Cuboid6(0.75D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D) };
  
  public static final Cuboid6[] renderCuboids2 = new Cuboid6[] { null, null, new Cuboid6(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.5D), new Cuboid6(0.3125D, 0.0D, 0.5D, 0.6875D, 0.8125D, 1.0D), new Cuboid6(0.0D, 0.0D, 0.3125D, 0.5D, 0.8125D, 0.6875D), new Cuboid6(0.5D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D) };
  
  public static final String type = "extrautils:wall";
  
  public String getType() {
    return "extrautils:wall";
  }
  
  public Cuboid6 getBounds() {
    return partCuboids[0];
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    List<Cuboid6> t = new ArrayList<Cuboid6>();
    if (isEthereal())
      return t; 
    if ((this.connectionMask & 0x2) != 0)
      t.add(partCuboids[1].copy()); 
    for (int i = 2; i < 6; i++) {
      if ((this.connectionMask & 1 << i) != 0)
        t.add(partCuboids[i].copy()); 
    } 
    return t;
  }
  
  public boolean shouldConnect(int x, int y, int z, int direction) {
    Block l = world().getBlock(x, y, z);
    if (world().getTileEntity(x, y, z) instanceof IWall && 
      tile().canAddPart((TMultiPart)PartWallDummy.dummyArms[direction]))
      return ((TileMultipart)world().getTileEntity(x, y, z)).canAddPart((TMultiPart)PartWallDummy.dummyArms[Facing.oppositeSide[direction]]); 
    return (l.getMaterial().isOpaque() && l.renderAsNormalBlock() && tile().canAddPart((TMultiPart)PartWallDummy.dummyArms[direction]));
  }
  
  public void registerPassThroughs() {
    MultipartGenerator.registerPassThroughInterface(IWall.class.getName());
  }
  
  public void reloadShape() {
    int prevMask = this.connectionMask;
    this.connectionMask = 0;
    for (int i = 2; i < 6; i++) {
      if (shouldConnect(x() + Facing.offsetsXForSide[i], y() + Facing.offsetsYForSide[i], z() + Facing.offsetsZForSide[i], i))
        this.connectionMask |= 1 << i; 
    } 
    if (!world().isAirBlock(x(), y() + 1, z()) || (this.connectionMask != 12 && this.connectionMask != 48))
      this.connectionMask |= 0x2; 
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
    boolean hasCenter = ((this.connectionMask & 0x2) != 0);
    if (this.mat != null && this.mat.canRenderInPass(pass)) {
      if (hasCenter)
        MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, getRenderBounds(), 0); 
      for (int i = 2; i < 6; i++) {
        if ((this.connectionMask & 1 << i) != 0)
          MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, hasCenter ? renderCuboids1[i] : renderCuboids2[i], 1 << Facing.oppositeSide[i] | 1 << i); 
      } 
      return true;
    } 
    return false;
  }
  
  public int getMetadata() {
    return 2;
  }
  
  public TMultiPart newPart(boolean client) {
    return (TMultiPart)new PartWall();
  }
  
  public PartWall() {}
  
  public PartWall(int material) {
    super(material);
  }
  
  public TMultiPart placePart(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5, int materialID) {
    return (TMultiPart)new PartWall(materialID);
  }
  
  public void renderItem(ItemStack item, MicroMaterialRegistry.IMicroMaterial material) {
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, partCuboids[0], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids1[2], 0);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, renderCuboids1[3], 0);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartWall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */