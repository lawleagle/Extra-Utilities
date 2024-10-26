package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.crafting.RecipeUnstableCrafting;
import com.rwtema.extrautils.damgesource.DamageSourceDivByZero;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemUnstableIngot extends Item implements IItemMultiTransparency {
  public static final int numTickstilDestruction = 200;
  
  private IIcon[] iconIngot = new IIcon[2];
  
  private IIcon[] iconNugget = new IIcon[2];
  
  public ItemUnstableIngot() {
    setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    this.maxStackSize = 1;
    setUnlocalizedName("extrautils:unstableingot");
    setHasSubtypes(true);
  }
  
  public int getItemStackLimit(ItemStack item) {
    if (item != null) {
      if (item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && 
        item.hasTagCompound()) {
        if (!item.getTagCompound().hasKey("stable"))
          return 1; 
        if (item.getTagCompound().hasKey("time"))
          return 1; 
      } 
      return 64;
    } 
    return 1;
  }
  
  public static void explode(EntityPlayer player) {
    stripPlayerOfIngots(player);
    if (ExtraUtils.unstableIngotExplosion) {
      player.worldObj.createExplosion((Entity)player, player.posX, player.posY, player.posZ, 1.0F, false);
      player.attackEntityFrom((DamageSource)DamageSourceDivByZero.divbyzero, 32767.0F);
    } 
  }
  
  public static void stripPlayerOfIngots(EntityPlayer player) {
    if (player != null && player.inventory != null) {
      for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
        ItemStack itemStack = player.inventory.getStackInSlot(i);
        if (itemStack != null && itemStack.getItem() == ExtraUtils.unstableIngot && itemStack.hasTagCompound() && (itemStack.getTagCompound().hasKey("crafting") || itemStack.getTagCompound().hasKey("time")))
          player.inventory.setInventorySlotContents(i, null); 
      } 
      ItemStack item = player.inventory.getItemStack();
      if (item != null && item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && item.hasTagCompound() && (item.getTagCompound().hasKey("crafting") || item.getTagCompound().hasKey("time")))
        player.inventory.setItemStack(null); 
      player.inventory.markDirty();
      updatePlayer((Entity)player);
    } 
  }
  
  public static void updatePlayer(Entity player) {
    if (player instanceof EntityPlayerMP)
      ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player); 
  }
  
  public String getUnlocalizedName(ItemStack par1ItemStack) {
    return getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
  }
  
  public static boolean isStable(ItemStack item) {
    return (item.getItemDamage() == 2 || (item.hasTagCompound() && item.getTagCompound().hasKey("stable")));
  }
  
  public static boolean isSuperStable(ItemStack item) {
    return (item.getItemDamage() == 2 || (item.hasTagCompound() && item.getTagCompound().hasKey("superstable")));
  }
  
  public void onUpdate(ItemStack item, World par2World, Entity par3Entity, int par4, boolean par5) {
    if (item.stackSize == 0)
      return; 
    if (par2World.isRemote)
      return; 
    if (!(par3Entity instanceof EntityPlayer))
      return; 
    EntityPlayer player = (EntityPlayer)par3Entity;
    if (item.getItemDamage() > 0)
      return; 
    if (item.hasTagCompound()) {
      boolean deleteIngot = false;
      boolean explode = false;
      if (item.getTagCompound().hasKey("creative") || isStable(item))
        return; 
      if (item.getTagCompound().hasKey("bug")) {
        item.getTagCompound().removeTag("bug");
        item.getTagCompound().setBoolean("bug_show", true);
        return;
      } 
      if (item.getTagCompound().hasKey("crafting") && 
        player.openContainer != null) {
        if (player.openContainer.getClass() != ContainerPlayer.class) {
          addTimeStamp(item, par2World);
          return;
        } 
        stripPlayerOfIngots(player);
        return;
      } 
      if (player.openContainer != null && 
        player.openContainer.getClass() == ContainerPlayer.class)
        explode(player); 
      if (item.getTagCompound().hasKey("time") && item.getTagCompound().hasKey("dimension")) {
        float t = (float)(200L - par2World.getTotalWorldTime() - item.getTagCompound().getLong("time")) / 20.0F;
        if ((((par3Entity.worldObj.provider.dimensionId != item.getTagCompound().getInteger("dimension")) ? 1 : 0) | ((t < 0.0F) ? 1 : 0)) != 0)
          if (par3Entity.worldObj.provider.dimensionId == item.getTagCompound().getInteger("dimension")) {
            explode(player);
          } else {
            stripPlayerOfIngots(player);
          }  
      } 
    } 
  }
  
  public void addTimeStamp(ItemStack item, World world) {
    NBTTagCompound ts = new NBTTagCompound();
    if (ts.hasKey("crafting"))
      ts.removeTag("crafting"); 
    if (item.getItemDamage() > 0)
      return; 
    ts.setInteger("dimension", world.provider.dimensionId);
    ts.setLong("time", world.getTotalWorldTime());
    item.setTagCompound(ts);
  }
  
  public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
    if (isStable(par1ItemStack))
      return; 
    if (par1ItemStack.getItemDamage() > 0)
      return; 
    addTimeStamp(par1ItemStack, par2World);
    if (!par2World.isRemote)
      updatePlayer((Entity)par3EntityPlayer); 
  }
  
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack par1ItemStack, int pass) {
    return false;
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
    NBTTagCompound tag = par1ItemStack.getTagCompound();
    if (tag != null && (
      tag == RecipeUnstableCrafting.nbt || tag.getBoolean("isNEI")))
      par1ItemStack = new ItemStack(this, 1, 0); 
    if (par1ItemStack.getItemDamage() > 0)
      return; 
    if (isStable(par1ItemStack)) {
      par3List.add("Stable");
      return;
    } 
    if (par1ItemStack.hasTagCompound() && !par1ItemStack.getTagCompound().hasKey("crafting") && !par1ItemStack.getTagCompound().hasKey("creative") && !par1ItemStack.getTagCompound().hasKey("bug")) {
      if (par1ItemStack.getTagCompound().hasKey("dimension") && par2EntityPlayer.worldObj.provider.dimensionId == par1ItemStack.getTagCompound().getInteger("dimension")) {
        float t = (float)(200L - par2EntityPlayer.worldObj.getTotalWorldTime() - par1ItemStack.getTagCompound().getLong("time")) / 20.0F;
        if (t < 0.0F)
          t = 0.0F; 
        par3List.add("Explodes in " + String.format(Locale.ENGLISH, "%.1f", new Object[] { Float.valueOf(t) }) + " seconds");
      } else if (par1ItemStack.getTagCompound().hasKey("bug_show")) {
        par3List.add("This ingot was created incorrectly");
        par3List.add("using getRecipeOutput() instead of getCraftingResult()");
        par3List.add("if this ingot was made legitimately please");
        par3List.add("report this to the mod developer.");
        par3List.add("(don't spam them though - check to see if");
        par3List.add("it hasn't already been reported)");
      } 
    } else {
      par3List.add("ERROR: Divide by diamond");
      par3List.add("This ingot is highly unstable and will explode");
      par3List.add("after 10 seconds.");
      par3List.add("Will also explode if the crafting window is closed");
      par3List.add("or the ingot is thrown on the ground.");
      par3List.add("Additionally these ingots do not stack");
      par3List.add(" - Do not craft unless ready -");
      par3List.add("");
      par3List.add("Must be crafted in a vanilla crafting table.");
      if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("creative")) {
        par3List.add("");
        par3List.add("Creative Spawned - Stable");
      } 
    } 
  }
  
  public boolean hasCustomEntity(ItemStack stack) {
    return true;
  }
  
  public Entity createEntity(World world, Entity location, ItemStack itemstack) {
    if (location instanceof EntityItem && itemstack.hasTagCompound() && (itemstack.getTagCompound().hasKey("crafting") || itemstack.getTagCompound().hasKey("time"))) {
      ((EntityItem)location).age = 1;
      location.setDead();
    } 
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("creative", true);
    ItemStack item = new ItemStack(par1, 1, 0);
    item.setTagCompound(tag);
    par3List.add(item);
    item = new ItemStack(par1, 1, 1);
    par3List.add(item);
    par3List.add(new ItemStack(par1, 1, 2));
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IIconRegister) {
    this.itemIcon = this.iconIngot[0] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5));
    this.iconIngot[1] = par1IIconRegister.registerIcon(getUnlocalizedName().substring(5) + "1");
    this.iconNugget[0] = par1IIconRegister.registerIcon("extrautils:unstablenugget");
    this.iconNugget[1] = par1IIconRegister.registerIcon("extrautils:unstablenugget1");
  }
  
  public int numIcons(ItemStack item) {
    return 2;
  }
  
  public IIcon getIconForTransparentRender(ItemStack item, int pass) {
    if (item.getItemDamage() == 1)
      return this.iconNugget[pass]; 
    return this.iconIngot[pass];
  }
  
  public float getIconTransparency(ItemStack item, int pass) {
    if (pass == 1)
      return 0.5F; 
    return 1.0F;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemUnstableIngot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */