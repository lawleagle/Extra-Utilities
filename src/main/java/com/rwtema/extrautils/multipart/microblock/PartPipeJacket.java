package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroblockRender;
import codechicken.multipart.IconHitEffects;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.JPartialOcclusion;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class PartPipeJacket extends PartMicroBlock implements JIconHitEffects, IMicroMaterialRender, IMicroBlock, JPartialOcclusion {
  public static final String type = "extrautils:pipeJacket";
  
  public static Cuboid6[] axisCubes = null;
  
  public MicroMaterialRegistry.IMicroMaterial mat = null;
  
  public int connectionMask = 0;
  
  public double pipeSize = 0.3D;
  
  public TMultiPart centerPart = null;
  
  int material;
  
  private MicroMaterialRegistry.IMicroMaterial wool;
  
  public PartPipeJacket() {}
  
  public PartPipeJacket(int material) {
    this.material = material;
  }
  
  public void harvest(MovingObjectPosition hit, EntityPlayer player) {
    super.harvest(hit, player);
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return !npart.getClass().equals(getClass());
  }
  
  public void writeDesc(MCDataOutput packet) {
    packet.writeInt(this.material);
  }
  
  public void readDesc(MCDataInput packet) {
    this.material = packet.readInt();
  }
  
  public void save(NBTTagCompound tag) {
    super.save(tag);
    tag.setString("mat", MicroMaterialRegistry.materialName(this.material));
  }
  
  public void load(NBTTagCompound tag) {
    super.load(tag);
    this.material = MicroMaterialRegistry.materialID(tag.getString("mat"));
  }
  
  public String getType() {
    return "extrautils:pipeJacket";
  }
  
  public Cuboid6 getBounds() {
    return new Cuboid6(0.5D - this.pipeSize, 0.5D - this.pipeSize, 0.5D - this.pipeSize, 0.5D + this.pipeSize, 0.5D + this.pipeSize, 0.5D + this.pipeSize);
  }
  
  public Iterable<Cuboid6> getCollisionBoxes() {
    ArrayList<Cuboid6> boxes = new ArrayList<Cuboid6>();
    if (isEthereal())
      return boxes; 
    boxes.add(getRenderBounds());
    for (int i = 0; i < 6; i++) {
      if ((this.connectionMask & 1 << i) != 0)
        boxes.add((new Cuboid6(0.5D - this.pipeSize, 0.5D + this.pipeSize, 0.5D - this.pipeSize, 0.5D + this.pipeSize, 1.0D, 0.5D + this.pipeSize)).apply(Rotation.sideRotations[Facing.oppositeSide[i]].at(new Vector3(0.5D, 0.5D, 0.5D)))); 
    } 
    return boxes;
  }
  
  public ItemStack getItemDrop() {
    ItemStack item = new ItemStack(ItemMicroBlock.instance, 1);
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("mat", MicroMaterialRegistry.materialName(this.material));
    item.setTagCompound(tag);
    return item;
  }
  
  public Iterable<ItemStack> getDrops() {
    return Arrays.asList(new ItemStack[] { getItemDrop() });
  }
  
  public ItemStack pickItem(MovingObjectPosition hit) {
    return getItemDrop();
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getBreakingIcon(Object subPart, int side) {
    return getBrokenIcon(side);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getBrokenIcon(int side) {
    if (this.mat != null)
      return this.mat.getBreakingIcon(side); 
    return Blocks.stone.getIcon(0, 0);
  }
  
  @SideOnly(Side.CLIENT)
  public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
    IconHitEffects.addHitEffects(this, hit, effectRenderer);
  }
  
  @SideOnly(Side.CLIENT)
  public void addDestroyEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
    IconHitEffects.addDestroyEffects(this, effectRenderer, false);
  }
  
  public Cuboid6 getRenderBounds() {
    return getBounds();
  }
  
  public int getLightValue() {
    return MicroMaterialRegistry.getMaterial(this.material).getLightValue();
  }
  
  public void onNeighborChanged() {
    if (!(world()).isRemote) {
      dropIfCantStay();
    } else {
      reloadShape();
    } 
  }
  
  public boolean canStay() {
    return (tile().partMap(6) != null);
  }
  
  public boolean dropIfCantStay() {
    if (!canStay()) {
      drop();
      return true;
    } 
    reloadShape();
    return false;
  }
  
  public void drop() {
    TileMultipart.dropItem(getItemDrop(), world(), Vector3.fromTileEntityCenter((TileEntity)tile()));
    tile().remPart((TMultiPart)this);
  }
  
  public void onPartChanged(TMultiPart part) {
    if (!(world()).isRemote)
      dropIfCantStay(); 
  }
  
  public Iterable<IndexedCuboid6> getSubParts() {
    ArrayList<IndexedCuboid6> boxes = new ArrayList<IndexedCuboid6>();
    this.overEthereal = true;
    for (Cuboid6 cuboid6 : getCollisionBoxes())
      boxes.add(new IndexedCuboid6(Integer.valueOf(0), cuboid6)); 
    this.overEthereal = false;
    return boxes;
  }
  
  public void onWorldJoin() {
    reloadShape();
    super.onWorldJoin();
  }
  
  public void reloadShape() {
    int prevMask = this.connectionMask;
    this.centerPart = tile().partMap(6);
    double prevSize = this.pipeSize;
    this.pipeSize = 0.26D;
    this.connectionMask = 0;
    if (this.centerPart != null) {
      if (this.centerPart instanceof ISidedHollowConnect)
        for (int side = 0; side < 6; side++)
          this.pipeSize = Math.max(this.pipeSize, ((((ISidedHollowConnect)this.centerPart).getHollowSize(side) + 1) / 32.0F));  
      for (int i = 0; i < 6; i++) {
        for (Cuboid6 cuboid6 : this.centerPart.getCollisionBoxes()) {
          if (cuboid6.intersects((new Cuboid6(this.pipeSize, 0.0D, this.pipeSize, 1.0D - this.pipeSize, 0.01D, 1.0D - this.pipeSize)).apply(Rotation.sideRotations[i].at(new Vector3(0.5D, 0.5D, 0.5D)))))
            this.connectionMask |= 1 << i; 
        } 
      } 
    } 
    if (prevMask != this.connectionMask || prevSize != this.pipeSize) {
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
      MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, getRenderBounds(), this.connectionMask);
      for (int i = 0; i < 6; i++) {
        if ((this.connectionMask & 1 << i) != 0)
          MicroblockRender.renderCuboid(new Vector3(x(), y(), z()), this.mat, pass, (new Cuboid6(0.5D - this.pipeSize, 0.5D + this.pipeSize, 0.5D - this.pipeSize, 0.5D + this.pipeSize, 1.0D, 0.5D + this.pipeSize)).apply(Rotation.sideRotations[Facing.oppositeSide[i]].at(new Vector3(0.5D, 0.5D, 0.5D))), 1 << Facing.oppositeSide[i]); 
      } 
      return true;
    } 
    return false;
  }
  
  public void render(Vector3 pos, int pass) {}
  
  public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
    return getMaterial().getStrength(player) * 4.0F;
  }
  
  public int getMetadata() {
    return 0;
  }
  
  public TMultiPart newPart(boolean client) {
    return (TMultiPart)new PartPipeJacket();
  }
  
  public TMultiPart placePart(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5, int materialID) {
    TileMultipart tile = TileMultipart.getOrConvertTile(world, new BlockCoord(pos.x, pos.y, pos.z));
    if (tile == null)
      return null; 
    TMultiPart part = tile.partMap(6);
    if (part != null)
      return (TMultiPart)new PartPipeJacket(materialID); 
    return null;
  }
  
  public void registerPassThroughs() {}
  
  public void renderItem(ItemStack item, MicroMaterialRegistry.IMicroMaterial material) {
    if (this.wool == null)
      this.wool = MicroMaterialRegistry.getMaterial(Blocks.wool.getUnlocalizedName()); 
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), this.wool, -1, new Cuboid6(0.1995D, 0.4D, 0.4D, 0.8005D, 0.6D, 0.6D), 15);
    MicroblockRender.renderCuboid(new Vector3(0.0D, 0.0D, 0.0D), material, -1, new Cuboid6(0.2D, 0.25D, 0.25D, 0.8D, 0.75D, 0.75D), 0);
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    return Arrays.asList(new Object[0]);
  }
  
  public Iterable<Cuboid6> getPartialOcclusionBoxes() {
    return Arrays.asList(new Object[0]);
  }
  
  public boolean allowCompleteOcclusion() {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartPipeJacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */