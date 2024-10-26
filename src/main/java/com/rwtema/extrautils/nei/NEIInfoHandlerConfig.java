package com.rwtema.extrautils.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.LogHelper;
import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockDecoration;
import com.rwtema.extrautils.item.filters.AdvancedNodeUpgrades;
import com.rwtema.extrautils.item.filters.Matcher;
import com.rwtema.extrautils.nei.ping.NEIPing;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class NEIInfoHandlerConfig implements IConfigureNEI {
  public void loadConfig() {
    NEIPing.init();
    if (ExtraUtils.drum != null)
      API.addSubset("Extra Common.Drums", new SubsetItemsNBT(Item.getItemFromBlock(ExtraUtils.drum))); 
    if (ExtraUtils.microBlocks != null)
      API.addSubset("Extra Common.Extra Microblocks", new SubsetItemsNBT(ExtraUtils.microBlocks)); 
    if (ExtraUtils.colorBlockDataEnabled)
      API.addSubset("Extra Common.Colored Blocks", new SubsetBlockClass((Class)BlockColor.class)); 
    if (ExtraUtils.greenScreen != null)
      API.addSubset("Extra Common.Lapis Caelestis", new SubsetItems(new Item[] { Item.getItemFromBlock((Block)ExtraUtils.greenScreen) })); 
    if (ExtraUtils.cobblestoneCompr != null)
      API.addSubset("Extra Common.Compressed Blocks", new SubsetItems(new Item[] { Item.getItemFromBlock((Block)ExtraUtils.cobblestoneCompr) })); 
    if (ExtraUtils.decorative1Enabled || ExtraUtils.decorative2Enabled)
      API.addSubset("Extra Common.Decorative Blocks", new SubsetBlockClass((Class)BlockDecoration.class)); 
    if (ExtraUtils.generator2 != null) {
      SubsetItems s = new SubsetItems(new Item[] { Item.getItemFromBlock(ExtraUtils.generator2) });
      if (ExtraUtils.generator3 != null)
        s.addItem(Item.getItemFromBlock(ExtraUtils.generator3)); 
      API.addSubset("Extra Common.Higher Tier Generators", s);
    } 
    for (Matcher matcher : AdvancedNodeUpgrades.entryList) {
      if (matcher != AdvancedNodeUpgrades.nullMatcher && matcher.type == Matcher.Type.ITEM && matcher.shouldAddToNEI() && matcher.isSelectable()) {
        String localizedName = matcher.getLocalizedName();
        localizedName = localizedName.replace(".exe", "");
        localizedName = localizedName.replaceAll("\\.", "");
        API.addSubset("Extra Filtering." + localizedName, new ItemFilterWrapper(matcher));
      } 
    } 
    API.registerRecipeHandler((ICraftingHandler)new EnderConstructorHandler());
    API.registerUsageHandler((IUsageHandler)new EnderConstructorHandler());
    API.registerRecipeHandler(new InfoHandler());
    API.registerUsageHandler(new InfoHandler());
    API.registerRecipeHandler((ICraftingHandler)new SoulHandler());
    API.registerUsageHandler((IUsageHandler)new SoulHandler());
    if (Loader.isModLoaded("ForgeMultipart")) {
      API.registerRecipeHandler((ICraftingHandler)new FMPMicroBlocksHandler());
      API.registerUsageHandler((IUsageHandler)new FMPMicroBlocksHandler());
      API.registerRecipeHandler((ICraftingHandler)new MicroBlocksHandler());
      API.registerUsageHandler((IUsageHandler)new MicroBlocksHandler());
      API.registerGuiOverlayHandler(GuiCrafting.class, new FMPMicroBlocksOverlayHandler(), "microblocks");
      API.registerGuiOverlayHandler(GuiInventory.class, new FMPMicroBlocksOverlayHandler(63, 20), "microblocks2x2");
    } 
    if (ExtraUtils.colorBlockData != null)
      API.hideItem(new ItemStack(ExtraUtils.colorBlockData)); 
    LogHelper.info("Added NEI integration", new Object[0]);
  }
  
  public String getName() {
    return "Extra Utilities: Nei Integration";
  }
  
  public String getVersion() {
    return "1";
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\NEIInfoHandlerConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */