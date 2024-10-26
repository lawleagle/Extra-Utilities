package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.IMicroMaterialRender;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.IconHitEffects;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;

public abstract class PartConnecting extends PartMicroBlock implements JIconHitEffects, IMicroMaterialRender, JNormalOcclusion, IMicroBlock {
  public MicroMaterialRegistry.IMicroMaterial mat = null;
  
  public int connectionMask = 0;
  
  int material;
  
  public PartConnecting() {}
  
  public PartConnecting(int material) {
    this.material = material;
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return NormalOcclusionTest.apply(this, npart);
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    return Arrays.asList(new Cuboid6[] { getBounds() });
  }
  
  public void harvest(MovingObjectPosition hit, EntityPlayer player) {
    super.harvest(hit, player);
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
  
  public abstract String getType();
  
  public abstract Cuboid6 getBounds();
  
  public abstract Iterable<Cuboid6> getCollisionBoxes();
  
  public ItemStack getItemDrop() {
    ItemStack item = new ItemStack(ItemMicroBlock.instance, 1, getMetadata());
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
  
  public MicroMaterialRegistry.IMicroMaterial getMaterial() {
    if (this.mat == null)
      this.mat = MicroMaterialRegistry.getMaterial(this.material); 
    return this.mat;
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
    reloadShape();
  }
  
  public void drop() {
    TileMultipart.dropItem(getItemDrop(), world(), Vector3.fromTileEntityCenter((TileEntity)tile()));
    tile().remPart((TMultiPart)this);
  }
  
  public void onPartChanged(TMultiPart part) {
    reloadShape();
  }
  
  public Iterable<IndexedCuboid6> getSubParts() {
    IndexedCuboid6 box = new IndexedCuboid6(Integer.valueOf(0), new Cuboid6(0.5D, 0.5D, 0.5D, 0.5D, 0.5D, 0.5D));
    this.overEthereal = true;
    for (Cuboid6 cuboid6 : getCollisionBoxes())
      box.enclose((Cuboid6)new IndexedCuboid6(Integer.valueOf(0), cuboid6)); 
    this.overEthereal = false;
    box.max.y = 1.0D;
    return Arrays.asList(new IndexedCuboid6[] { box });
  }
  
  public void onWorldJoin() {
    reloadShape();
    super.onWorldJoin();
  }
  
  public abstract boolean shouldConnect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void reloadShape();
  
  @SideOnly(Side.CLIENT)
  public abstract boolean renderStatic(Vector3 paramVector3, int paramInt);
  
  @SideOnly(Side.CLIENT)
  public void render(Vector3 pos, int pass) {
    renderStatic(pos, pass);
  }
  
  public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
    return getMaterial().getStrength(player);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartConnecting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */