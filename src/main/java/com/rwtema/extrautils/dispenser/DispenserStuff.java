package com.rwtema.extrautils.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.World;

public class DispenserStuff {
  public static void registerItems() {
    if (!((RegistrySimple)BlockDispenser.dispenseBehaviorRegistry).containsKey(Items.glass_bottle))
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.glass_bottle, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
              ItemPotion itemPotion;
              EnumFacing enumfacing = BlockDispenser.func_149937_b(par1IBlockSource.getBlockMetadata());
              World world = par1IBlockSource.getWorld();
              int i = par1IBlockSource.getXInt() + enumfacing.getFrontOffsetX();
              int j = par1IBlockSource.getYInt() + enumfacing.getFrontOffsetY();
              int k = par1IBlockSource.getZInt() + enumfacing.getFrontOffsetZ();
              Material material = world.getBlock(i, j, k).getMaterial();
              int l = world.getBlockMetadata(i, j, k);
              if (Material.water.equals(material) && l == 0) {
                itemPotion = Items.potionitem;
              } else {
                return super.dispenseStack(par1IBlockSource, par2ItemStack);
              } 
              if (--par2ItemStack.stackSize == 0) {
                par2ItemStack.func_150996_a((Item)itemPotion);
                par2ItemStack.stackSize = 1;
                par2ItemStack.setTagCompound(null);
              } else if (((TileEntityDispenser)par1IBlockSource.getBlockTileEntity()).func_146019_a(new ItemStack((Item)itemPotion)) < 0) {
                this.field_150840_b.dispense(par1IBlockSource, new ItemStack((Item)itemPotion));
              } 
              return par2ItemStack;
            }
          }); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dispenser\DispenserStuff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */