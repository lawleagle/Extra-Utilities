package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ICreativeTabSorting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import scala.Tuple2;

public class ItemMicroBlock extends Item implements ICreativeTabSorting {
  public static ItemMicroBlock instance;

  public ItemMicroBlock() {
    instance = this;
    if (ExtraUtils.showMultiblocksTab)
      setCreativeTab(CreativeTabMicroBlocks.instance);
    setUnlocalizedName("extrautils:microblocks");
    setHasSubtypes(true);
  }

  public static ItemStack getStack(ItemStack item, String material) {
    item = item.copy();
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("mat", material);
    item.setTagCompound(tag);
    return item;
  }

  public String getItemStackDisplayName(ItemStack stack) {
    if (!stack.hasTagCompound())
      return "ERR";
    String material = stack.getTagCompound().getString("mat");
    if (MicroMaterialRegistry.getMaterial(material) == null)
      return "ERR";
    return MicroMaterialRegistry.getMaterial(material).getLocalizedName() + " " + super.getItemStackDisplayName(stack);
  }

  public String getUnlocalizedName(ItemStack par1ItemStack) {
    return getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (IMicroBlock microBlock : RegisterMicroBlocks.mParts.values()) {
      if (!microBlock.hideCreativeTab()) {
        int meta = microBlock.getMetadata();
        ItemStack item = new ItemStack(par1, 1, meta);
        for (Tuple2<String, MicroMaterialRegistry.IMicroMaterial> t : MicroMaterialRegistry.getIdMap())
          par3List.add(getStack(item, (String)t._1()));
      }
    }
  }

  public double getHitDepth(Vector3 vhit, int side) {
    return vhit.copy().scalarProject(Rotation.axes[side]) + (side % 2 ^ 0x1);
  }

  public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    BlockCoord pos = new BlockCoord(x, y, z);
    Vector3 vhit = new Vector3(hitX, hitY, hitZ);
    double d = getHitDepth(vhit, side);
    if (d < 1.0D && place(item, player, world, pos, side, vhit))
      return true;
    pos.offset(side);
    return place(item, player, world, pos, side, vhit);
  }

  public boolean place(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5) {
    TMultiPart part = newPart(stack, player, world, pos, side, arg5);
    if (part == null || !TileMultipart.canPlacePart(world, pos, part))
      return false;
    if (!world.isRemote)
      TileMultipart.addPart(world, pos, part);
    if (!player.capabilities.isCreativeMode)
      stack.stackSize--;
    return true;
  }

  public TMultiPart newPart(ItemStack stack, EntityPlayer player, World world, BlockCoord pos, int side, Vector3 arg5) {
    if (!stack.hasTagCompound())
      return null;
    String material = stack.getTagCompound().getString("mat");
    if (material.equals("") || MicroMaterialRegistry.getMaterial(material) == null)
      return null;
    IMicroBlock part = RegisterMicroBlocks.mParts.get(Integer.valueOf(stack.getItemDamage()));
    if (part != null)
      return part.placePart(stack, player, world, pos, side, arg5, MicroMaterialRegistry.materialID(material));
    return null;
  }

  public String getSortingName(ItemStack par1ItemStack) {
    return par1ItemStack.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage() + "_" + par1ItemStack.getDisplayName();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\ItemMicroBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
