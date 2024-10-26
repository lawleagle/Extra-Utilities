package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ActivationRitual;
import com.rwtema.extrautils.EventHandlerSiege;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.network.packets.PacketTempChatMultiline;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class ItemDivisionSigil extends Item {
  public static int maxdamage = 256;

  public ItemDivisionSigil() {
    setMaxStackSize(1);
    setUnlocalizedName("extrautils:divisionSigil");
    setTextureName("extrautils:divisionSigil");
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
  }

  public static ItemStack newActiveSigil() {
    ItemStack item = new ItemStack(ExtraUtils.divisionSigil);
    NBTTagCompound t = new NBTTagCompound();
    t.setInteger("damage", maxdamage);
    item.setTagCompound(t);
    return item;
  }

  public static ItemStack newStableSigil() {
    ItemStack item = new ItemStack(ExtraUtils.divisionSigil);
    NBTTagCompound t = new NBTTagCompound();
    t.setBoolean("stable", true);
    item.setTagCompound(t);
    return item;
  }

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    if (par1ItemStack.hasTagCompound() && (par1ItemStack.getTagCompound().hasKey("damage") || par1ItemStack.getTagCompound().hasKey("stable")))
      return true;
    if ((Minecraft.getMinecraft()).theWorld != null)
      return (ActivationRitual.checkTime((Minecraft.getMinecraft()).theWorld.getWorldTime()) == 0);
    return false;
  }

  public String getUnlocalizedName(ItemStack item) {
    String name = getUnlocalizedName();
    if (item.hasTagCompound()) {
      if (item.getTagCompound().hasKey("stable"))
        return name + ".stable";
      if (item.getTagCompound().hasKey("damage"))
        return name + ".active";
    }
    return name;
  }

  public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
    return false;
  }

  public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
    return par1ItemStack;
  }

  public ItemStack getContainerItem(ItemStack itemStack) {
    if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("damage")) {
      int damage = itemStack.getTagCompound().getInteger("damage");
      damage--;
      if (damage <= 0) {
        itemStack.getTagCompound().removeTag("damage");
        if (itemStack.getTagCompound().hasNoTags())
          itemStack.setTagCompound(null);
      } else {
        itemStack.getTagCompound().setInteger("damage", damage);
      }
      return itemStack;
    }
    return itemStack;
  }

  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    par3List.add(new ItemStack(par1, 1, 0));
    par3List.add(newActiveSigil());
    par3List.add(newStableSigil());
  }

  public boolean hasContainerItem(ItemStack itemStack) {
    if (itemStack.hasTagCompound()) {
      if (itemStack.getTagCompound().hasKey("damage"))
        return true;
      if (itemStack.getTagCompound().hasKey("stable"))
        return true;
    }
    return false;
  }

  public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
    if (world.isRemote) {
      if ((!item.hasTagCompound() || !item.getTagCompound().hasKey("damage")) && world.getBlock(x, y, z) == Blocks.enchanting_table) {
        boolean flag = true;
        PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Activation Ritual"));
        if (ActivationRitual.redstoneCirclePresent(world, x, y, z)) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Altar has a redstone circle"));
          if (ActivationRitual.altarOnEarth(world, x, y, z)) {
            PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Altar and Circle placed on dirt"));
          } else {
            flag = false;
            PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Altar and Circle not placed on dirt"));
          }
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Altar does not have a redstone circle"));
          flag = false;
        }
        if (ActivationRitual.altarCanSeeMoon(world, x, y, z)) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Altar can see the moon"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Altar cannot see the moon"));
          flag = false;
        }
        if (ActivationRitual.naturalEarth(world, x, y, z)) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Altar has sufficient natural earth"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Area lacks sufficient natural earth"));
          flag = false;
        }
        if (ActivationRitual.altarInDarkness_Client(world, x, y, z)) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Altar is in darkness"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Altar must not be lit by outside sources"));
        }
        int i = ActivationRitual.checkTime(world.getWorldTime());
        if (i == -1) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Too early, sacrifice must be made at midnight"));
        } else if (i == 1) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Too late, sacrifice must be made at midnight"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Time is right"));
          if (flag)
            PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Perform the sacrifice"));
        }
      }
    } else if (item.hasTagCompound() && item.getTagCompound().hasKey("damage") && world.getBlock(x, y, z) == Blocks.beacon) {
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Stabilization Ritual"));
      PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(""));
      if (world.provider.dimensionId != 1) {
        if (world.provider.dimensionId == -1) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Too hot"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- Too much natural earth"));
        }
      } else {
        int f = 0, e = 0, a = 0, w = 0;
        if (TileEntityHopper.func_145893_b(world, x, y, (z - 5)) != null) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- To the north, Children of Fire: " + (f = EventHandlerSiege.checkChestFire(TileEntityHopper.func_145893_b(world, x, y, (z - 5)), false)) + " / 12"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Northern chest not present"));
        }
        if (TileEntityHopper.func_145893_b(world, x, y, (z + 5)) != null) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- To the south, Gifts of Earth: " + (e = EventHandlerSiege.checkChestEarth(TileEntityHopper.func_145893_b(world, x, y, (z + 5)), false)) + " / 12"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Southern chest not present"));
        }
        if (TileEntityHopper.func_145893_b(world, (x + 5), y, z) != null) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- To the east, Descendants of Water: " + (w = EventHandlerSiege.checkChestWater(TileEntityHopper.func_145893_b(world, (x + 5), y, z), false)) + " / 12"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Eastern chest not present"));
        }
        if (TileEntityHopper.func_145893_b(world, (x - 5), y, z) != null) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("- To the west, Spices of Air: " + (a = EventHandlerSiege.checkChestAir(TileEntityHopper.func_145893_b(world, (x - 5), y, z), false)) + " / 12"));
        } else {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("! Western chest not present"));
        }
        PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(""));
        int[] k = EventHandlerSiege.getStrength(world, x, y, z);
        boolean hasString = (world.getBlock(x - 1, y, z) == Blocks.tripwire || world.getBlock(x + 1, y, z) == Blocks.tripwire || world.getBlock(x, y, z - 1) == Blocks.tripwire || world.getBlock(x, y, z + 1) == Blocks.tripwire);
        boolean hasRedstone = (world.getBlock(x - 1, y, z) == Blocks.redstone_wire || world.getBlock(x + 1, y, z) == Blocks.redstone_wire || world.getBlock(x, y, z - 1) == Blocks.redstone_wire || world.getBlock(x, y, z + 1) == Blocks.redstone_wire);
        if (k[1] == 0) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Ritual Markings: No markings present"));
        } else if (k[0] == 0) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Ritual Markings: Only 1 type of marking present"));
        } else {
          String t = k[0] + "";
          for (int i = 1; i < k.length; i++)
            t = t + " / " + k[i] + " / 64";
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Ritual Markings: Strength - " + t));
        }
        if (f >= 12 && e >= 12 && w >= 12 && a >= 12 && k[0] >= 64) {
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(""));
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Everything is prepared."));
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText(""));
          PacketTempChatMultiline.addChatComponentMessage((IChatComponent)new ChatComponentText("Sacrifice one who would sacrifice himself."));
        }
      }
    }
    PacketTempChatMultiline.sendCached(player);
    return true;
  }

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    if (par1ItemStack.hasTagCompound()) {
      if (par1ItemStack.getTagCompound().hasKey("stable")) {
        par3List.add("STABLE");
      } else if (par1ItemStack.getTagCompound().hasKey("damage")) {
        par3List.add("ACTIVE: Number of uses remaining - " + par1ItemStack.getTagCompound().getInteger("damage"));
      }
    } else {
      par3List.add("INACTIVE: You must perform Activation Ritual.");
      par3List.add("Sneak right-click on an enchanting table");
      par3List.add("for more details");
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemDivisionSigil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
