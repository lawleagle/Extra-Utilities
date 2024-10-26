package com.rwtema.extrautils.tileentity.enderquarry;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.block.BlockMultiBlockSelection;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.block.IBlockTooltip;
import com.rwtema.extrautils.helper.Translate;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockQuarryUpgrades extends BlockMultiBlockSelection implements IBlockTooltip {
  int[] powerDrain = new int[16];

  IIcon[] icons = new IIcon[16];

  IIcon[] iconsFlipped = new IIcon[16];

  IIcon arms = null;

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister p_149651_1_) {
    for (int i = 0; i < 10; i++) {
      this.icons[i] = p_149651_1_.registerIcon("extrautils:quarry_upgrades/quarryUpgrade" + i);
      this.iconsFlipped[i] = (IIcon)new IconFlipped(this.icons[i], true, false);
    }
    this.blockIcon = this.arms = p_149651_1_.registerIcon("extrautils:quarry_upgrades/quarryUpgradeArm");
  }

  public BlockQuarryUpgrades() {
    super(Material.rock);
    setBlockName("extrautils:enderQuarryUpgrade");
    setBlockTextureName("extrautils:enderQuarryUpgrade");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    setHardness(1.0F);
    setStepSound(soundTypeStone);
  }

  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
    for (int i = 0; i < 10; i++)
      p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
  }

  public void prepareForRender(String label) {}

  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    int metadata = world.getBlockMetadata(x, y, z);
    BoxModel model = getInventoryModel(metadata);
    ((Box)model.get(0)).textureSide[2] = this.iconsFlipped[metadata];
    ((Box)model.get(0)).textureSide[3] = null;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == ExtraUtils.enderQuarry) {
        Box b = new Box(0.125F, 0.0F, 0.125F, 0.875F, 0.0625F, 0.875F);
        b.rotateToSide(dir);
        b.texture = this.arms;
        model.add(b);
      }
    }
    return model;
  }

  public BoxModel getInventoryModel(int metadata) {
    BoxModel b = new BoxModel();
    b.addBoxI(1, 1, 1, 15, 15, 15);
    ((Box)b.get(0)).texture = this.icons[metadata];
    ((Box)b.get(0)).textureSide[0] = this.iconsFlipped[metadata];
    ((Box)b.get(0)).textureSide[3] = this.iconsFlipped[metadata];
    ((Box)b.get(0)).textureSide[5] = this.iconsFlipped[metadata];
    return b;
  }

  public int damageDropped(int p_149692_1_) {
    return p_149692_1_;
  }

  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    int meta = par1ItemStack.getItemDamage() & 0xF;
    double v = TileEntityEnderQuarry.powerMultipliers[meta];
    String format = XUHelper.niceFormat(v);
    par3List.add(Translate.get("power.drain", new Object[] { format }));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderquarry\BlockQuarryUpgrades.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
