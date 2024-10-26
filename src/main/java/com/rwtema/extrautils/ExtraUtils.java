package com.rwtema.extrautils;

import codechicken.multipart.MultipartGenerator;
import cofh.api.energy.IEnergyHandler;
import com.google.common.collect.Lists;
import com.rwtema.extrautils.block.BlockAngelBlock;
import com.rwtema.extrautils.block.BlockBUD;
import com.rwtema.extrautils.block.BlockBedrockium;
import com.rwtema.extrautils.block.BlockChandelier;
import com.rwtema.extrautils.block.BlockCobblestoneCompressed;
import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.block.BlockConveyor;
import com.rwtema.extrautils.block.BlockCursedEarth;
import com.rwtema.extrautils.block.BlockCurtain;
import com.rwtema.extrautils.block.BlockDecoration;
import com.rwtema.extrautils.block.BlockDrum;
import com.rwtema.extrautils.block.BlockEnderLily;
import com.rwtema.extrautils.block.BlockEnderthermicPump;
import com.rwtema.extrautils.block.BlockEtherealStone;
import com.rwtema.extrautils.block.BlockFilingCabinet;
import com.rwtema.extrautils.block.BlockGreenScreen;
import com.rwtema.extrautils.block.BlockMagnumTorch;
import com.rwtema.extrautils.block.BlockPeacefulTable;
import com.rwtema.extrautils.block.BlockPortal;
import com.rwtema.extrautils.block.BlockPureLove;
import com.rwtema.extrautils.block.BlockSoundMuffler;
import com.rwtema.extrautils.block.BlockSpike;
import com.rwtema.extrautils.block.BlockSpikeDiamond;
import com.rwtema.extrautils.block.BlockSpikeGold;
import com.rwtema.extrautils.block.BlockSpikeWood;
import com.rwtema.extrautils.block.BlockTimer;
import com.rwtema.extrautils.block.BlockTradingPost;
import com.rwtema.extrautils.block.BlockTrashCan;
import com.rwtema.extrautils.command.CommandKillEntities;
import com.rwtema.extrautils.crafting.RecipeBagDye;
import com.rwtema.extrautils.crafting.RecipeCustomOres;
import com.rwtema.extrautils.crafting.RecipeDifficultySpecific;
import com.rwtema.extrautils.crafting.RecipeFilterInvert;
import com.rwtema.extrautils.crafting.RecipeGBEnchanting;
import com.rwtema.extrautils.crafting.RecipeGlove;
import com.rwtema.extrautils.crafting.RecipeHorseTransmutation;
import com.rwtema.extrautils.crafting.RecipeMagicalWood;
import com.rwtema.extrautils.crafting.RecipeSoul;
import com.rwtema.extrautils.crafting.RecipeUnEnchanting;
import com.rwtema.extrautils.crafting.RecipeUnsigil;
import com.rwtema.extrautils.crafting.RecipeUnstableCrafting;
import com.rwtema.extrautils.crafting.RecipeUnstableIngotCrafting;
import com.rwtema.extrautils.crafting.RecipeUnstableNuggetCrafting;
import com.rwtema.extrautils.crafting.ShapedOreRecipeAlwaysLast;
import com.rwtema.extrautils.crafting.ShapelessOreRecipeAlwaysLast;
import com.rwtema.extrautils.dispenser.DispenserStuff;
import com.rwtema.extrautils.helper.ThaumcraftHelper;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XURandom;
import com.rwtema.extrautils.item.ItemAngelBlock;
import com.rwtema.extrautils.item.ItemAngelRing;
import com.rwtema.extrautils.item.ItemBedrockiumIngot;
import com.rwtema.extrautils.item.ItemBlockColor;
import com.rwtema.extrautils.item.ItemBlockDrum;
import com.rwtema.extrautils.item.ItemBlockEnderLily;
import com.rwtema.extrautils.item.ItemBlockGenerator;
import com.rwtema.extrautils.item.ItemBlockMetadata;
import com.rwtema.extrautils.item.ItemBlockQED;
import com.rwtema.extrautils.item.ItemBlockSpike;
import com.rwtema.extrautils.item.ItemBuildersWand;
import com.rwtema.extrautils.item.ItemDestructionPickaxe;
import com.rwtema.extrautils.item.ItemDivisionSigil;
import com.rwtema.extrautils.item.ItemErosionShovel;
import com.rwtema.extrautils.item.ItemEthericSword;
import com.rwtema.extrautils.item.ItemFilingCabinet;
import com.rwtema.extrautils.item.ItemGlove;
import com.rwtema.extrautils.item.ItemGoldenBag;
import com.rwtema.extrautils.item.ItemGoldenLasso;
import com.rwtema.extrautils.item.ItemHealingAxe;
import com.rwtema.extrautils.item.ItemHeatingCoil;
import com.rwtema.extrautils.item.ItemLawSword;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.item.ItemPaintbrush;
import com.rwtema.extrautils.item.ItemPrecisionShears;
import com.rwtema.extrautils.item.ItemSonarGoggles;
import com.rwtema.extrautils.item.ItemSoul;
import com.rwtema.extrautils.item.ItemTemporalHoe;
import com.rwtema.extrautils.item.ItemUnstableIngot;
import com.rwtema.extrautils.item.ItemWateringCan;
import com.rwtema.extrautils.item.scanner.ItemScanner;
import com.rwtema.extrautils.modintegration.EE3Integration;
import com.rwtema.extrautils.modintegration.MFRIntegration;
import com.rwtema.extrautils.modintegration.TE4IMC;
import com.rwtema.extrautils.multipart.ItemBlockMultiPartMagnumTorch;
import com.rwtema.extrautils.multipart.MagnumTorchPart;
import com.rwtema.extrautils.multipart.RegisterBlockPart;
import com.rwtema.extrautils.multipart.RegisterMicroMaterials;
import com.rwtema.extrautils.multipart.microblock.CreativeTabMicroBlocks;
import com.rwtema.extrautils.multipart.microblock.ItemMicroBlock;
import com.rwtema.extrautils.multipart.microblock.RecipeMicroBlocks;
import com.rwtema.extrautils.multipart.microblock.RegisterMicroBlocks;
import com.rwtema.extrautils.network.GuiHandler;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.PacketCodec;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import com.rwtema.extrautils.tileentity.TileEntityBUD;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import com.rwtema.extrautils.tileentity.TileEntityDrum;
import com.rwtema.extrautils.tileentity.TileEntityEnchantedSpike;
import com.rwtema.extrautils.tileentity.TileEntityEnderThermicLavaPump;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import com.rwtema.extrautils.tileentity.TileEntityPortal;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import com.rwtema.extrautils.tileentity.TileEntitySoundMuffler;
import com.rwtema.extrautils.tileentity.TileEntityTradingPost;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanEnergy;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanFluids;
import com.rwtema.extrautils.tileentity.chests.BlockFullChest;
import com.rwtema.extrautils.tileentity.chests.BlockMiniChest;
import com.rwtema.extrautils.tileentity.chests.TileFullChest;
import com.rwtema.extrautils.tileentity.chests.TileMiniChest;
import com.rwtema.extrautils.tileentity.endercollector.BlockEnderCollector;
import com.rwtema.extrautils.tileentity.endercollector.TileEnderCollector;
import com.rwtema.extrautils.tileentity.enderconstructor.BlockEnderConstructor;
import com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler;
import com.rwtema.extrautils.tileentity.enderconstructor.TileEnderConstructor;
import com.rwtema.extrautils.tileentity.enderconstructor.TileEnderPillar;
import com.rwtema.extrautils.tileentity.enderquarry.BlockBreakingRegistry;
import com.rwtema.extrautils.tileentity.enderquarry.BlockEnderMarkers;
import com.rwtema.extrautils.tileentity.enderquarry.BlockEnderQuarry;
import com.rwtema.extrautils.tileentity.enderquarry.BlockQuarryUpgrades;
import com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderMarker;
import com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry;
import com.rwtema.extrautils.tileentity.generators.BlockGenerator;
import com.rwtema.extrautils.tileentity.generators.TileEntityGeneratorPotion;
import com.rwtema.extrautils.tileentity.transfernodes.BlockRetrievalNode;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityFilterPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.ItemBlockTransferNodeMultiPart;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.ItemBlockTransferPipeMultiPart;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.RegisterPipeParts;
import com.rwtema.extrautils.tileentity.transfernodes.multiparts.RegisterTransferNodeParts;
import com.rwtema.extrautils.worldgen.Underdark.DarknessTickHandler;
import com.rwtema.extrautils.worldgen.Underdark.EventHandlerUnderdark;
import com.rwtema.extrautils.worldgen.Underdark.WorldProviderUnderdark;
import com.rwtema.extrautils.worldgen.WorldGenEnderLillies;
import com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommand;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ExtraUtils {
    public static final int dec1EdgedStoneBricks = 0;

    public static final int dec1EnderObsidian = 1;

    public static final int dec1BurntQuartz = 2;

    public static final int dec1FrostStone = 3;

    public static final int dec1BorderStone = 4;

    public static final int dec1UnstableBlock = 5;

    public static final int dec1GravelBricks = 6;

    public static final int dec1BorderStone2 = 7;

    public static final int dec1MagicalWood = 8;

    public static final int dec1SandyGlass = 9;

    public static final int dec1GravelRoad = 10;

    public static final int dec1EnderCore = 11;

    public static final int dec1DiamondMatrix = 12;

    public static final int dec1EnderSandAlloy = 13;

    public static final int dec1EminenceStone = 14;

    public static final int dec2ThickGlass = 0;

    public static final int dec2ThickGlassEdged = 1;

    public static final int dec2ThickGlassBricks = 2;

    public static final int dec2ThickGlassCarved = 3;

    public static final int dec2ThickGlassGolden = 4;

    public static final int dec2ThickGlassObsidian = 5;

    public static final int dec2ThickGlassSwirling = 6;

    public static final int dec2ThickGlassGlowstone = 7;

    public static final int dec2ThickGlassHeart = 8;

    public static final int dec2ThickGlassSquare = 9;

    public static final int dec2DarkGlass = 10;

    public static final int dec2DarkGlassObsidian = 11;

    public static final ArrayList<Item> spikeSwords = new ArrayList<Item>();

    public static boolean allNonVanillaDimensionsValidForEnderPump = false;

    public static Block angelBlock;

    public static Item buildersWand;

    public static boolean buildersWandEnabled;

    public static Block chandelier;

    public static boolean chandelierEnabled;

    public static BlockCobblestoneCompressed cobblestoneCompr;

    public static boolean cobblestoneComprEnabled;

    public static Block colorBlockData;

    public static List<BlockColor> colorblocks = new ArrayList<BlockColor>();

    public static boolean colorBlockDataEnabled;

    public static Block conveyor;

    public static boolean conveyorEnabled;

    public static Item creativeBuildersWand;

    public static boolean creativeBuildersWandEnabled;

    public static CreativeTabExtraUtils creativeTabExtraUtils;

    public static Item creativeTabIcon = null;

    public static Block cursedEarth;

    public static boolean cursedEarthEnabled;

    public static Block curtain;

    public static boolean curtainEnabled;

    public static BlockDecoration decorative1;

    public static boolean decorative1Enabled;

    public static BlockDecoration decorative2;

    public static boolean decorative2Enabled;

    public static BlockBedrockium bedrockiumBlock;

    public static boolean bedrockiumBlockEnabled;

    public static BlockPureLove pureLove;

    public static boolean pureLoveBlockEnabled;

    public static Item destructionPickaxe;

    public static boolean destructionPickaxeEnabled;

    public static int underdarkDimID;

    public static int endoftimeDimID;

    public static boolean disableAdvFilingCabinet;

    public static boolean disableBuildersWandRecipe;

    public static boolean disableChandelierRecipe;

    public static boolean disableColoredBlocksRecipes;

    public static boolean disableCompressedCobblestoneRecipe;

    public static boolean disableConveyorRecipe;

    public static boolean disableCrossoverPipeRecipe = false;

    public static boolean disableCurtainRecipe;

    public static boolean disableDestructionPickaxeRecipe;

    public static boolean disableDivisionSigilInChests;

    public static boolean disableDrumRecipe;

    public static boolean disableEnderLiliesInDungeons;

    public static boolean disableEnderThermicPumpRecipe;

    public static boolean disableEnergyPipeRecipe = false;

    public static boolean disableErosionShovelRecipe;

    public static boolean disableEtherealBlockRecipe;

    public static boolean disableEthericSwordRecipe;

    public static boolean disableFilingCabinet;

    public static boolean disableFilterPipeRecipe;

    public static boolean disableFilterRecipe;

    public static boolean disableGeneratorRecipe = false;

    public static boolean disableGoldenBagRecipe;

    public static boolean disableGoldenLassoRecipe;

    public static boolean disableHealingAxeRecipe;

    public static boolean disableMagnumTorchRecipe;

    public static boolean disableModSortingPipesRecipe = false;

    public static boolean disableEnergyExtractionPipeRecipe;

    public static boolean disableNodeUpgradeSpeedRecipe;

    public static boolean disableObsidianRecipe;

    public static boolean disablePaintbrushRecipe;

    public static boolean disablePeacefulTableRecipe;

    public static boolean disablePortalTexture = false;

    public static boolean disablePrecisionShears;

    public static boolean disableRainMufflerRecipe;

    public static boolean disableRationingPipeRecipe;

    public static boolean disableSonarGogglesRecipe;

    public static boolean disableSortingPipeRecipe;

    public static boolean disableSoundMufflerRecipe;

    public static boolean disableSpikeRecipe;

    public static boolean disableTemporalHoeRecipe;

    public static boolean disableTimerBlockRecipe;

    public static boolean disableTradingPostRecipe;

    public static boolean disableTransferNodeEnergyRecipe;

    public static boolean disableTransferNodeLiquidRecipe;

    public static boolean disableTransferNodeLiquidRemoteRecipe;

    public static boolean disableTransferNodeRecipe;

    public static boolean disableTransferNodeRemoteRecipe;

    public static boolean disableTransferPipeRecipe;

    public static boolean disableTrashCanRecipe;

    public static boolean disableUnstableIngotRecipe;

    public static boolean disableWateringCanRecipe;

    public static boolean disableWitherRecipe;

    public static boolean disableNodeParticles;

    public static boolean disableEnderQuarryUpgradesRecipes;

    public static boolean disableQEDIngotSmeltRecipes;

    public static boolean showMultiblocksTab;

    public static Item divisionSigil;

    public static boolean divisionSigilEnabled;

    public static Block drum;

    public static boolean drumEnabled;

    public static BlockEnderLily enderLily;

    public static boolean enderLilyEnabled;

    public static int enderLilyRetrogenId;

    public static Block enderQuarry;

    public static boolean enderQuarryEnabled;

    public static boolean enderQuarryRecipeEnabled;

    public static Block enderThermicPump;

    public static boolean enderThermicPumpEnabled;

    public static Item erosionShovel;

    public static boolean erosionShovelEnabled;

    public static Block etheralBlock;

    public static boolean etherealBlockEnabled;

    public static Item ethericSword;

    public static boolean ethericSwordEnabled;

    public static BlockFilingCabinet filingCabinet;

    public static boolean filingCabinetEnabled;

    public static Block generator;

    public static Block generator2;

    public static Block generator3;

    public static boolean generatorEnabled;

    public static boolean generator2Enabled;

    public static boolean generator3Enabled;

    public static Item goldenBag;

    public static boolean goldenBagEnabled;

    public static Item goldenLasso;

    public static boolean goldenLassoEnabled;

    public static BlockGreenScreen greenScreen;

    public static boolean greenScreenEnabled;

    public static boolean handlesClientMethods = false;

    public static Item healingAxe;

    public static boolean healingAxeEnabled;

    public static Block magnumTorch;

    public static boolean magnumTorchEnabled;

    public static Item microBlocks = null;

    public static boolean microBlocksEnabled;

    public static ItemNodeUpgrade nodeUpgrade;

    public static boolean nodeUpgradeEnabled;

    public static Item paintBrush;

    public static boolean paintBrushEnabled;

    public static Block peacefultable;

    public static boolean peacefultableEnabled;

    public static Block portal;

    public static boolean portalEnabled;

    public static ItemPrecisionShears precisionShears;

    public static boolean precisionShearsEnabled;

    public static Item scanner;

    public static boolean scannerEnabled;

    public static Item sonarGoggles;

    public static boolean sonarGogglesEnabled;

    public static Item lawSword;

    public static boolean lawSwordEnabled;

    public static Block soundMuffler;

    public static boolean soundMufflerEnabled;

    public static BlockSpike spike;

    public static boolean spikeEnabled;

    public static boolean spikeItemSword;

    public static ItemBedrockiumIngot bedrockium;

    public static boolean bedrockiumEnabled;

    public static Item temporalHoe;

    public static boolean temporalHoeEnabled;

    public static Block timerBlock;

    public static boolean timerBlockEnabled;

    public static Block tradingPost;

    public static boolean tradingPostEnabled;

    public static Block transferNode;

    public static boolean transferNodeEnabled;

    public static Block transferNodeRemote;

    public static boolean transferNodeRemoteEnabled;

    public static BlockTransferPipe transferPipe;

    public static BlockTransferPipe transferPipe2;

    public static boolean transferPipeEnabled;

    public static Block trashCan;

    public static boolean trashCanEnabled;

    public static ItemSoul soul;

    public static boolean soulEnabled;

    public static Item unstableIngot;

    public static boolean unstableIngotExplosion = true;

    public static boolean unstableIngotEnabled;

    public static int[] validDimensionsForEnderPump;

    public static ItemWateringCan wateringCan;

    public static boolean wateringCanEnabled;

    public static boolean qedEnabled;

    public static BlockEnderConstructor qed;

    public static Set<String> qedList = new HashSet<String>();

    public static ItemAngelRing angelRing;

    public static boolean angelRingEnabled;

    public static BlockEnderMarkers enderMarker;

    public static boolean enderMarkerEnabled;

    public static boolean permaSoulDrainOff;

    public static boolean gloveEnabled;

    public static Item glove;

    public static boolean disableChestRecipe;

    public static boolean disableWitherNoiseUnlessNearby;

    public static boolean heatingCoilEnabled;

    public static ItemHeatingCoil heatingCoil;

    public static BlockColor colorBlockBrick;

    public static BlockColor coloredWood;

    public static BlockQuarryUpgrades enderQuarryUpgrade;

    public static boolean enderQuarryUpgradeEnabled;

    public static boolean spikeGoldEnabled;

    public static boolean spikeDiamondEnabled;

    public static boolean spikeWoodEnabled;

    public static BlockSpike spikeGold;

    public static BlockSpike spikeDiamond;

    public static BlockColor colorQuartz;

    public static BlockSpikeWood spikeWood;

    public static BlockColor colorBlockRedstone;

    public static boolean peacefulTableInAllDifficulties;

    public static boolean disableInfiniteWater;

    public static boolean disableCobblegen;

    public static int versionHash;

    public static Set<Class<? extends IRecipe>> registeredRecipes = new HashSet<Class<? extends IRecipe>>();

    private static boolean angelBlockEnabled;

    private static boolean BUDBlockEnabled;

    private static BlockBUD BUDBlock;

    private static boolean disableSuperWateringCanRecipe;

    boolean hasSpecialInit = false;

    public static int tcon_unstable_material_id;

    public static int tcon_magical_wood_id;

    public static int tcon_bedrock_material_id;

    private static boolean disableAngelBlockRecipe;

    private static boolean disableBUDBlockRecipe;

    private static boolean disableAdvBUDBlockRecipe;

    private static Block enderCollector;

    private static boolean enderCollectorEnabled;

    public static Block fullChest;

    public static Block miniChest;

    public static boolean fullChestEnabled;

    public static boolean miniChestEnabled;

    public static void addDungeonItem(ItemStack item, int min, int max, String category, double probability) {
        int n = getWeightTotal(ChestGenHooks.getItems(category, (Random)XURandom.getInstance()));
        int a = (int)Math.ceil(probability * n);
        ChestGenHooks.addItem(category, new WeightedRandomChestContent(item, min, max, a));
    }

    public static void registerTile(Class<? extends TileEntity> clazz, String name) {
        GameRegistry.registerTileEntity(clazz, name);
    }

    public static void registerTile(Class<? extends TileEntity> clazz) {
        GameRegistry.registerTileEntity(clazz, clazz.getSimpleName());
    }

    public static int getWeightTotal(WeightedRandomChestContent[] items) {
        if (items == null || items.length == 0)
            return 1;
        int weight = 0;
        for (WeightedRandomChestContent item : items)
            weight += item.itemWeight;
        return weight;
    }

    public static Block registerBlock(Block block) {
        return registerBlock(block, ItemBlock.class);
    }

    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemblock) {
        String s = block.getUnlocalizedName().substring("tile.".length());
        s = s.replace("extrautils:", "");
        return GameRegistry.registerBlock(block, itemblock, s);
    }

    public static Item registerItem(Item item) {
        String s = getDefaultRegisterName(item);
        registerItem(item, s);
        return item;
    }

    public static String getDefaultRegisterName(Item item) {
        String s = item.getUnlocalizedName().substring("item.".length());
        s = s.replace("extrautils:", "");
        return s;
    }

    public static void registerItem(Item item, String s) {
        if (creativeTabIcon == null)
            creativeTabIcon = item;
        GameRegistry.registerItem(item, s);
    }

    public static void addRecipe(IRecipe recipe) {
        registerRecipe((Class)recipe.getClass());
        GameRegistry.addRecipe(recipe);
    }

    public static void addRecipe(ItemStack out, Object... ingreds) {
        GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(out, ingreds));
    }

    public static void registerRecipe(Class<? extends IRecipe> recipe) {
        if (registeredRecipes.contains(recipe))
            return;
        if (!recipe.getName().startsWith("com.rwtema."))
            return;
        registeredRecipes.add(recipe);
        LogHelper.fine("Registering " + recipe.getSimpleName() + " to RecipeSorter", new Object[0]);
        if (ShapedOreRecipe.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, RecipeSorter.Category.SHAPED, "after:forge:shapedore");
        } else if (ShapelessOreRecipe.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        } else if (ShapedRecipes.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        } else if (ShapelessRecipes.class.isAssignableFrom(recipe)) {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless before:minecraft:bookcloning");
        } else {
            RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        }
    }

    public void addSigil(String category, double probability) {
        addDungeonItem(new ItemStack(divisionSigil, 1), 1, 1, category, probability);
    }

    List<ILoading> loaders = new ArrayList<ILoading>();

    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Hello World", new Object[0]);
        loadTcon();
        versionHash = (event.getModMetadata()).version.hashCode();
        try {
            IEnergyHandler.class.getMethod("canConnectEnergy", new Class[] { ForgeDirection.class });
            for (ModCandidate t : event.getAsmData().getCandidatesFor("cofh.api.energy")) {
                boolean hasProperApi = false;
                if (t.getClassList().contains("cofh/api/energy/IEnergyHandler") &&
                    !t.getClassList().contains("cofh/api/energy/IEnergyConnection"))
                    for (ModContainer mod : t.getContainedMods())
                        LogHelper.info("" + mod.getModId() + " (" + mod.getName() + ") appears to be missing the updated COFH api", new Object[0]);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            List<ModContainer> suspects = new ArrayList<ModContainer>();
            for (ModCandidate t : event.getAsmData().getCandidatesFor("cofh.api.energy")) {
                boolean hasProperApi = false;
                if (t.getClassList().contains("cofh/api/energy/IEnergyHandler") &&
                    !t.getClassList().contains("cofh/api/energy/IEnergyConnection")) {
                    for (ModContainer mod : t.getContainedMods())
                        LogHelper.info("" + mod.getModId() + " (" + mod.getName() + ") appears to be missing the updated COFH api", new Object[0]);
                    suspects.addAll(t.getContainedMods());
                }
            }
            String[] data = new String[2 + suspects.size()];
            data[0] = "Some mod is including a older or incorrect copy of the COFH energy api. This will lead to instability and Extra Utilities will not run properly. Possible candidates that do not include the proper api are...";
            data[1] = "";
            for (int i = 0; i < suspects.size(); i++)
                data[i + 1] = ((ModContainer)suspects.get(i)).getModId();
            ExtraUtilsMod.proxy.throwLoadingError("COFH API Error", data);
        }
        String networkPath = "com.rwtema.extrautils.network.packets.";
        for (ModCandidate t : event.getAsmData().getCandidatesFor("com.rwtema.extrautils")) {
            for (String s : t.getClassList()) {
                s = s.replace('/', '.');
                if (s.startsWith(networkPath))
                    try {
                        Class<? extends XUPacketBase> clazz = (Class<? extends XUPacketBase>)Class.forName(s);
                        if (XUPacketBase.class.isAssignableFrom(clazz)) {
                            PacketCodec.addClass(clazz);
                            continue;
                        }
                        clazz = clazz;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Presented class missing, FML Bug?", e);
                    } catch (NoClassDefFoundError e) {
                        LogHelper.error(s + " can't be created", new Object[0]);
                        throw new RuntimeException(e);
                    }
            }
        }
        NetworkHandler.register();
        try {
            World.class.getMethod("getBlock", new Class[] { int.class, int.class, int.class });
            XUHelper.deObf = true;
            LogHelper.info("Dev Enviroment detected. Releasing hounds...", new Object[0]);
        } catch (NoSuchMethodException e) {
            XUHelper.deObf = false;
        } catch (SecurityException e) {
            XUHelper.deObf = false;
        }
        setupConfigs(event.getSuggestedConfigurationFile());
        creativeTabExtraUtils = new CreativeTabExtraUtils("extraUtil");
        registerStuff();
        registerRecipes();
        ExtraUtilsMod.proxy.registerClientCommands();
        for (ILoading loader : this.loaders)
            loader.preInit();
    }

    public void loadTcon() {
        if (Loader.isModLoaded("TConstruct")) {
            ILoading r = null;
            try {
                Class<?> clazz = Class.forName("com.rwtema.extrautils.modintegration.TConIntegration");
                r = (ILoading)clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (r != null)
                this.loaders.add(r);
        }
    }

    public void setupConfigs(File file) {
        Configuration config = new Configuration(file);
        config.load();
        unstableIngotExplosion = config.get("options", "unstableIngotsExplode", true).getBoolean(true);
        disableNodeParticles = config.get("options", "disableTransferNodeParticles", false).getBoolean(false);
        disablePortalTexture = config.get("client_options", "disablePortalAnimation", false).getBoolean(false);
        disableWitherRecipe = config.get("recipes", "disablePeacefulWitherRecipe", false).getBoolean(false);
        peacefulTableInAllDifficulties = config.get("options", "peacefulTableInAllDifficulties", false).getBoolean(false);
        disableInfiniteWater = config.get("options", "disableTransferNodeWatergen", false).getBoolean(false);
        disableCobblegen = config.get("options", "disableTransferNodeCobblegen", false).getBoolean(false);
        permaSoulDrainOff = config.get("options", "soulDrainResetsOnDeath", false).getBoolean(false);
        disableWitherNoiseUnlessNearby = config.get("options", "disableWitherNoisesIfNotNearby", true).getBoolean(true);
        if (config.hasKey("options", "mimicDeobf"))
            XUHelper.deObf = true;
        tcon_unstable_material_id = config.get("tinkersIntegration", "tcon_unstable_material_id", 314).getInt(314);
        tcon_bedrock_material_id = config.get("tinkersIntegration", "tcon_bedrock_material_id", 315).getInt(315);
        tcon_magical_wood_id = config.get("tinkersIntegration", "tcon_magical_wood_id", 316).getInt(316);
        fullChestEnabled = getBlock(config, "slightlyLargerChestEnabled");
        miniChestEnabled = getBlock(config, "miniChestEnabled");
        enderCollectorEnabled = getBlock(config, "enderCollectorEnabled");
        gloveEnabled = getItem(config, "gloveEnabled");
        pureLoveBlockEnabled = getBlock(config, "pureLoveBlockEnabled");
        bedrockiumBlockEnabled = getBlock(config, "bedrockiumBlockEnabled");
        enderMarkerEnabled = getBlock(config, "enderMarkerEnabled");
        angelBlockEnabled = getBlock(config, "angelBlockEnabled");
        BUDBlockEnabled = getBlock(config, "BUDBlockEnabled");
        chandelierEnabled = getBlock(config, "chandelierEnabled");
        disableChandelierRecipe = config.get("recipes", "disableChandelierRecipe", false).getBoolean(false);
        disableChestRecipe = config.get("recipes", "disableAltChestRecipe", false).getBoolean(false);
        disableColoredBlocksRecipes = config.get("recipes", "disableColoredBlocksRecipes", false).getBoolean(false);
        colorBlockDataEnabled = getBlock(config, "colorBlockDataEnabled");
        disableCompressedCobblestoneRecipe = config.get("recipes", "disableCompressedCobblestoneRecipe", false).getBoolean(false);
        cobblestoneComprEnabled = getBlock(config, "cobblestoneComprEnabled");
        disableConveyorRecipe = config.get("recipes", "disableConveyorRecipe", false).getBoolean(false);
        conveyorEnabled = getBlock(config, "conveyorEnabled");
        greenScreenEnabled = getBlock(config, "greenScreen");
        disablePeacefulTableRecipe = config.get("recipes", "disablePeacefulTableRecipe", false).getBoolean(false);
        peacefultableEnabled = getBlock(config, "peacefultableEnabled");
        disableSoundMufflerRecipe = config.get("recipes", "disableSoundMufflerRecipe", false).getBoolean(false);
        disableRainMufflerRecipe = config.get("recipes", "disableRainMufflerRecipe", false).getBoolean(false);
        soundMufflerEnabled = getBlock(config, "soundMufflerEnabled");
        disableTradingPostRecipe = config.get("recipes", "disableTradingPostRecipe", false).getBoolean(false);
        tradingPostEnabled = getBlock(config, "tradingPost");
        disableFilterPipeRecipe = config.get("recipes", "disableFilterPipeRecipe", false).getBoolean(false);
        disableSortingPipeRecipe = config.get("recipes", "disableSortingPipeRecipe", false).getBoolean(false);
        disableModSortingPipesRecipe = config.get("recipes", "disableModSortingPipeRecipe", false).getBoolean(false);
        disableEnergyExtractionPipeRecipe = config.get("recipes", "disableEnergyExtractionPipeRecipe", false).getBoolean(false);
        disableRationingPipeRecipe = config.get("recipes", "disableRationingPipeRecipe", false).getBoolean(false);
        disableTransferPipeRecipe = config.get("recipes", "disableTransferPipeRecipe", false).getBoolean(false);
        transferPipeEnabled = getBlock(config, "transferPipeEnabled");
        disableTransferNodeRecipe = config.get("recipes", "disableTransferNodeRecipe", false).getBoolean(false);
        disableTransferNodeLiquidRecipe = config.get("recipes", "disableTransferNodeLiquidRecipe", false).getBoolean(false);
        disableTransferNodeEnergyRecipe = config.get("recipes", "disableTransferNodeEnergyRecipe", false).getBoolean(false);
        transferNodeEnabled = getBlock(config, "transferNodeEnabled");
        disableCurtainRecipe = config.get("recipes", "disableCurtainRecipe", false).getBoolean(false);
        curtainEnabled = getBlock(config, "curtainEnabled");
        cursedEarthEnabled = getBlock(config, "cursedEarth");
        disableTrashCanRecipe = config.get("recipes", "disableTrashCanRecipe", false).getBoolean(false);
        trashCanEnabled = getBlock(config, "trashCan");
        disableSpikeRecipe = config.get("recipes", "disableSpikeRecipe", false).getBoolean(false);
        spikeEnabled = getBlock(config, "spikeEnabled");
        spikeGoldEnabled = getBlock(config, "spikeGoldEnabled");
        spikeDiamondEnabled = getBlock(config, "spikeDiamondEnabled");
        spikeWoodEnabled = getBlock(config, "spikeWoodEnabled");
        disableEtherealBlockRecipe = config.get("recipes", "disableEtherealGlassRecipe", false).getBoolean(false);
        etherealBlockEnabled = getBlock(config, "etherealBlockEnabled");
        disableEnderThermicPumpRecipe = config.get("recipes", "disableEnderThermicPumpRecipe", false).getBoolean(false);
        disableEnderQuarryUpgradesRecipes = config.get("recipes", "disableEnderQuarryUpgradesRecipes", false).getBoolean(false);
        allNonVanillaDimensionsValidForEnderPump = !config.get("options", "disableEnderPumpInAllDimensions", false).getBoolean(false);
        validDimensionsForEnderPump = config.get("options", "EnderPumpDimensionExceptions", new int[0]).getIntList();
        enderThermicPumpEnabled = getBlock(config, "enderThermicPumpEnabled");
        disableTimerBlockRecipe = config.get("recipes", "disableRedstoneClockRecipe", false).getBoolean(false);
        timerBlockEnabled = getBlock(config, "timerBlockEnabled");
        disableMagnumTorchRecipe = config.get("recipes", "disableMagnumTorchRecipe", false).getBoolean(false);
        magnumTorchEnabled = getBlock(config, "magnumTorchEnabled");
        disableDrumRecipe = config.get("recipes", "disableDrumRecipe", false).getBoolean(false);
        drumEnabled = getBlock(config, "drumEnabled");
        decorative1Enabled = getBlock(config, "decorative_1Enabled");
        filingCabinetEnabled = getBlock(config, "filingCabinetEnabled");
        disableFilingCabinet = config.get("recipes", "disableFilingCabinet", false).getBoolean(false);
        disableAdvFilingCabinet = config.get("recipes", "disableAdvFilingCabinet", false).getBoolean(false);
        disableTransferPipeRecipe = config.get("recipes", "disableTransferPipeRecipe", false).getBoolean(false);
        enderLilyEnabled = getBlock(config, "enderLilyEnabled");
        disableEnderLiliesInDungeons = config.get("recipes", "disableEnderLiliesInDungeons", false).getBoolean(false);
        disableEnergyPipeRecipe = config.get("recipes", "disableEnergyPipeRecipe", false).getBoolean(false);
        disableCrossoverPipeRecipe = config.get("recipes", "disableEnergyPipeRecipe", false).getBoolean(false);
        portalEnabled = getBlock(config, "portalEnabled");
        underdarkDimID = config.get("options", "deepDarkDimensionID", -100).getInt(-100);
        endoftimeDimID = config.get("options", "lastMilleniumDimensionID", -112).getInt(-112);
        decorative2Enabled = getBlock(config, "decorative_2Enabled");
        generatorEnabled = getBlock(config, "generatorEnabled");
        generator2Enabled = getBlock(config, "generator8Enabled");
        generator3Enabled = getBlock(config, "generator64Enabled");
        enderQuarryEnabled = getBlock(config, "enderQuarryEnabled");
        enderQuarryUpgradeEnabled = getBlock(config, "enderQuarryUpgradeEnabled");
        disableGeneratorRecipe = config.get("recipes", "disableGeneratorRecipe", false).getBoolean(false);
        transferNodeRemoteEnabled = getBlock(config, "transferNodeRemoteEnabled");
        disableTransferNodeRemoteRecipe = config.get("recipes", "disableRetrievalNodeRecipe", false).getBoolean(false);
        disableTransferNodeLiquidRemoteRecipe = config.get("recipes", "disableRetrievalNodeLiquidRecipe", false).getBoolean(false);
        disableGoldenLassoRecipe = config.get("recipes", "disableGoldenLassoRecipe", false).getBoolean(false);
        goldenLassoEnabled = getItem(config, "goldenLassoEnabled");
        disablePaintbrushRecipe = config.get("recipes", "disablePaintbrushRecipe", false).getBoolean(false);
        paintBrushEnabled = getItem(config, "paintBrush");
        disableUnstableIngotRecipe = config.get("recipes", "disableUnstableIngotRecipe", false).getBoolean(false);
        unstableIngotEnabled = getItem(config, "unstableIngotEnabled");
        disableBuildersWandRecipe = config.get("recipes", "disableBuildersWandRecipe", false).getBoolean(false);
        buildersWandEnabled = getItem(config, "buildersWandEnabled");
        creativeBuildersWandEnabled = getItem(config, "creativeBuildersWandEnabled");
        disableEthericSwordRecipe = config.get("recipes", "disableEthericSwordRecipe", false).getBoolean(false);
        ethericSwordEnabled = getItem(config, "ethericSword");
        disableErosionShovelRecipe = config.get("recipes", "disableErosionShovelRecipe", false).getBoolean(false);
        erosionShovelEnabled = getItem(config, "erosionShovel");
        disableDestructionPickaxeRecipe = config.get("recipes", "disableDestructionPickaxeRecipe", false).getBoolean(false);
        destructionPickaxeEnabled = getItem(config, "destructionPickaxe");
        disableHealingAxeRecipe = config.get("recipes", "disableHealingAxeRecipe", false).getBoolean(false);
        healingAxeEnabled = getItem(config, "healingAxe");
        disableTemporalHoeRecipe = config.get("recipes", "disableReversingHoeRecipe", false).getBoolean(false);
        temporalHoeEnabled = getItem(config, "temporalHoe");
        disableDivisionSigilInChests = config.get("recipes", "disableDivisionSiginInChests", false).getBoolean(false);
        divisionSigilEnabled = getItem(config, "divisionSigilEnabled");
        disableSonarGogglesRecipe = config.get("recipes", "disableSonarGogglesRecipe", false).getBoolean(false);
        sonarGogglesEnabled = getItem(config, "sonarGogglesEnabled");
        disableWateringCanRecipe = config.get("recipes", "disableWateringCanRecipe", false).getBoolean(false);
        disableSuperWateringCanRecipe = config.get("recipes", "disableSuperWateringCanRecipe", false).getBoolean(false);
        wateringCanEnabled = getItem(config, "wateringCanEnabled");
        disableFilterRecipe = config.get("recipes", "disableFilterRecipe", false).getBoolean(false);
        disableNodeUpgradeSpeedRecipe = config.get("recipes", "disableNodeUpgradeSpeedRecipe", false).getBoolean(false);
        nodeUpgradeEnabled = getItem(config, "upgradeNodeEnabled");
        disableGoldenBagRecipe = config.get("recipes", "disableGoldenBagRecipe", false).getBoolean(false);
        goldenBagEnabled = getItem(config, "goldenBagEnabled");
        scannerEnabled = getItem(config, "scannerEnabled");
        bedrockiumEnabled = getItem(config, "bedrockiumIngotEnabled");
        angelRingEnabled = getItem(config, "angelRingEnabled");
        soulEnabled = getItem(config, "soulEnabled");
        lawSwordEnabled = getItem(config, "lawSwordEnabled");
        disableQEDIngotSmeltRecipes = config.get("recipes", "disableQEDIngotSmeltRecipes", false).getBoolean(false);
        heatingCoilEnabled = getItem(config, "heatingCoilEnabled");
        qedEnabled = getBlock(config, "QEDEnabled");
        Property prop_version = config.get("QEDCrafting", "QEDVersion", 0, "Internal version number to add/remove items in future updates. Set to -1 to disable auto-updates.");
        int prev_version = prop_version.getInt();
        Property prop = config.get("QEDCrafting", "QEDItems", new String[] { "tile.extrautils:extractor_base_remote.0", "tile.extrautils:extractor_base_remote.6", "item.extrautils:nodeUpgrade.5", "item.extrautils:nodeUpgrade.6", "tile.extrautils:enderQuarry", "tile.extrautils:magnumTorch" }, "ItemStack names to enforce crafting in the QED");
        String[][] toAdd = { { "tile.extrautils:endMarker" }, { "tile.extrautils:extractor_base.12" }, { "tile.extrautils:enderQuarryUpgrade.0", "tile.extrautils:extractor_base.13" } };
        String[][] toRemove = { { "tile.extrautils:enderQuarry" }, {}, {} };
        assert toAdd.length == toRemove.length;
        List<String> strings = new ArrayList<String>();
        Collections.addAll(strings, prop.getStringList());
        if (prev_version >= 0 && prev_version < toAdd.length) {
            for (int i = prev_version; i < toAdd.length; i++) {
                if (toAdd[i] != null)
                    for (String s : toAdd[i]) {
                        if (strings.add(s))
                            LogHelper.info("QEDCrafting Updater: Added Recipe - " + s, new Object[0]);
                    }
                if (toRemove[i] != null)
                    for (String s : toRemove[i]) {
                        if (strings.remove(s))
                            LogHelper.info("QEDCrafting Updater: Removed Recipe - " + s, new Object[0]);
                    }
            }
            prop_version.set(toAdd.length);
        }
        prop.set(strings.<String>toArray(new String[strings.size()]));
        if (strings.size() > 0) {
            for (String s : strings) {
                qedList.add(s);
                LogHelper.fine("Recipes constructing " + s + " will now be used in the Ender Constructor", new Object[0]);
            }
        } else {
            qedEnabled = false;
            LogHelper.fine("No Recipes for QED found. QED will be disabled.", new Object[0]);
        }
        if (enderQuarryEnabled) {
            TileEntityEnderQuarry.baseDrain = config.get("Ender Quarry Power", "baseDrain", 1800).getInt(1800);
            TileEntityEnderQuarry.hardnessDrain = config.get("Ender Quarry Power", "hardnessDrain", 200).getInt(200);
        }
        if (Loader.instance().getIndexedModList().containsKey("ForgeMultipart")) {
            microBlocksEnabled = getItem(config, "microBlocksEnabled");
            showMultiblocksTab = (microBlocksEnabled && !config.get("options", "disableMultiBlocksCreativeTab", false).getBoolean(false));
            if (showMultiblocksTab)
                CreativeTabMicroBlocks.instance = new CreativeTabMicroBlocks();
        }
        precisionShearsEnabled = getItem(config, "precisionShearsId");
        enderLilyRetrogenId = config.get("worldgen", "retrogen_enderlillies", 1).getInt(1);
        config.save();
    }

    public void registerStuff() {
        if (miniChestEnabled) {
            registerBlock(miniChest = (Block)new BlockMiniChest());
            registerTile((Class)TileMiniChest.class);
        }
        if (fullChestEnabled) {
            registerBlock(fullChest = (Block)new BlockFullChest());
            registerTile((Class)TileFullChest.class);
        }
        if (enderCollectorEnabled) {
            registerBlock(enderCollector = (Block)new BlockEnderCollector());
            registerTile((Class)TileEnderCollector.class);
        }
        if (gloveEnabled) {
            glove = (Item)new ItemGlove();
            registerItem(glove);
        }
        if (heatingCoilEnabled) {
            heatingCoil = new ItemHeatingCoil();
            registerItem((Item)heatingCoil);
        }
        if (soulEnabled) {
            soul = new ItemSoul();
            registerItem((Item)soul);
        }
        if (angelRingEnabled) {
            angelRing = new ItemAngelRing();
            registerItem((Item)angelRing);
        }
        if (qedEnabled) {
            qed = new BlockEnderConstructor();
            registerBlock((Block)qed, (Class)ItemBlockQED.class);
            registerTile((Class)TileEnderConstructor.class);
            registerTile((Class)TileEnderPillar.class);
        }
        if (enderMarkerEnabled) {
            enderMarker = new BlockEnderMarkers();
            registerBlock((Block)enderMarker);
            registerTile((Class)TileEntityEnderMarker.class);
        }
        if (curtainEnabled)
            registerBlock(curtain = (Block)new BlockCurtain());
        if (angelBlockEnabled)
            registerBlock(angelBlock = (Block)new BlockAngelBlock(), (Class)ItemAngelBlock.class);
        if (BUDBlockEnabled) {
            registerBlock((Block)(BUDBlock = new BlockBUD()), (Class)ItemBlockMetadata.class);
            registerTile((Class)TileEntityBUD.class);
        }
        if (chandelierEnabled)
            registerBlock(chandelier = (Block)new BlockChandelier());
        if (decorative1Enabled && decorative2Enabled) {
            decorative1 = new BlockDecoration(true);
            decorative1.setBlockName("extrautils:decorativeBlock1");
            registerBlock((Block)decorative1, (Class)ItemBlockMetadata.class);
            decorative1.addBlock(0, "Edged Stone Bricks", "extrautils:ConnectedTextures/test", true, true);
            decorative1.addBlock(1, "Ender-infused Obsidian", "extrautils:ConnectedTextures/endsidian", true, true);
            decorative1.hardness[1] = Blocks.obsidian.getBlockHardness(null, 0, 0, 0);
            decorative1.resistance[1] = Blocks.obsidian.getExplosionResistance(null) * 5.0F;
            decorative1.addBlock(2, "Burnt Quartz", "extrautils:ConnectedTextures/dark", true, true);
            decorative1.addBlock(3, "Frosted Stone", "extrautils:ConnectedTextures/icystone", true, false);
            decorative1.addBlock(4, "Border Stone", "extrautils:ConnectedTextures/carved", true, true);
            decorative1.addBlock(5, "Unstable Ingot Block", "extrautils:ConnectedTextures/uingot", true, true);
            decorative1.addBlock(6, "Gravel Bricks", "extrautils:ConnectedTextures/gravel_brick");
            decorative1.addBlock(7, "Border Stone (Alternate)", "extrautils:ConnectedTextures/singlestonebrick", true, true);
            decorative1.addBlock(8, "Magical Wood", "extrautils:ConnectedTextures/magic_wood_corners");
            decorative1.enchantBonus[8] = 2.5F;
            decorative1.addBlock(9, "Sandy Glass", "extrautils:sandedGlass");
            decorative1.addBlock(10, "Gravel Road", "extrautils:ConnectedTextures/gravel_road", true, true);
            decorative1.flipTopBottom[10] = true;
            decorative1.addBlock(11, "Ender Core", "extrautils:endCore");
            decorative1.enchantBonus[11] = 10.0F;
            decorative1.isSuperEnder[11] = true;
            decorative1.light[11] = 5;
            decorative1.addBlock(12, "Diamond-Etched Computational Matrix", "extrautils:diamondCore");
            decorative1.light[12] = 14;
            decorative1.addBlock(13, "Ender-Sand Alloy", "extrautils:ConnectedTextures/endslab", true, true);
            decorative1.isEnder[13] = true;
            decorative1.addBlock(14, "Eminence Stone", "extrautils:ConnectedTextures/purplestone", true, true);
            OreDictionary.registerOre("bricksGravel", new ItemStack((Block)decorative1, 1, 6));
            OreDictionary.registerOre("blockEnderObsidian", new ItemStack((Block)decorative1, 1, 1));
            OreDictionary.registerOre("burntQuartz", new ItemStack((Block)decorative1, 1, 2));
            OreDictionary.registerOre("blockIcestone", new ItemStack((Block)decorative1, 1, 3));
            OreDictionary.registerOre("blockUnstable", new ItemStack((Block)decorative1, 1, 5));
            OreDictionary.registerOre("blockMagicWood", new ItemStack((Block)decorative1, 1, 7));
            OreDictionary.registerOre("blockEnderCore", new ItemStack((Block)decorative1, 1, 11));
            OreDictionary.registerOre("blockGlassSandy", new ItemStack((Block)decorative1, 1, 9));
            decorative2 = new BlockDecoration(false);
            decorative2.setBlockName("extrautils:decorativeBlock2");
            registerBlock((Block)decorative2, (Class)ItemBlockMetadata.class);
            decorative2.addBlock(0, "Thickened Glass", "extrautils:ConnectedTextures/glass1", true, false);
            decorative2.addBlock(1, "Edged Glass", "extrautils:ConnectedTextures/glass2", true, false);
            decorative2.addBlock(2, "Glass Bricks", "extrautils:ConnectedTextures/glass3", true, false);
            decorative2.addBlock(3, "Carved Glass", "extrautils:ConnectedTextures/glass4", true, false);
            decorative2.addBlock(4, "Golden Edged Glass", "extrautils:ConnectedTextures/glass5", true, false);
            decorative2.addBlock(5, "Obsidian Glass", "extrautils:ConnectedTextures/glass6", true, false);
            decorative2.hardness[5] = 4.0F;
            decorative2.resistance[5] = 2000.0F;
            decorative2.addBlock(6, "Swirling Glass", "extrautils:ConnectedTextures/glass7", true, false);
            decorative2.addBlock(7, "Glowstone Glass", "extrautils:ConnectedTextures/glass8", true, false);
            decorative2.light[7] = 15;
            decorative2.addBlock(8, "Heart Glass", "extrautils:ConnectedTextures/glass9", true, false);
            decorative2.addBlock(9, "Square Glass", "extrautils:glassQuadrants", false, false);
            decorative2.addBlock(10, "Dark Glass", "extrautils:ConnectedTextures/darkglass", true, false);
            decorative2.opacity[10] = 255;
            decorative2.addBlock(11, "Dark Obsidian Glass", "extrautils:ConnectedTextures/obsidiandarkglass", true, false);
            decorative2.opacity[11] = 255;
            decorative2.hardness[11] = 4.0F;
            decorative2.resistance[11] = 2000.0F;
            for (int i = 0; i < 12; i++) {
                if (i != 4 && i != 5 && i != 7 && i != 8 && i != 10 && i != 11)
                    OreDictionary.registerOre("blockGlass", new ItemStack((Block)decorative2, 1, i));
            }
        }
        if (pureLoveBlockEnabled)
            registerBlock((Block)(pureLove = new BlockPureLove()));
        if (bedrockiumBlockEnabled)
            registerBlock((Block)(bedrockiumBlock = new BlockBedrockium()), (Class)BlockBedrockium.ItemBedrockium.class);
        if (colorBlockDataEnabled) {
            registerBlock(colorBlockData = (Block)new BlockColorData());
            if (paintBrushEnabled) {
                paintBrush = (Item)new ItemPaintbrush();
                registerItem(paintBrush);
            }
            registerBlock((Block)(colorBlockBrick = (BlockColor)(new BlockColor(Blocks.stonebrick, "bricksStone", "stonebrick")).setBlockName("extrautils:colorStoneBrick")), (Class)ItemBlockColor.class);
            registerBlock((Block)(coloredWood = (BlockColor)(new BlockColor(Blocks.planks, "plankWood", "planks_oak")).setBlockName("extrautils:colorWoodPlanks")), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.glowstone, "glowstone"), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.stone, "stone"), (Class)ItemBlockColor.class);
            registerBlock((Block)(colorQuartz = new BlockColor(Blocks.quartz_block, null, "quartz_block_top")), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.soul_sand, "soulsand"), (Class)ItemBlockColor.class);
            registerBlock((Block)(new BlockColor(Blocks.lit_redstone_lamp, null, "redstone_lamp_on")).setCustomRecipe(6, new Object[] {
                "SRS", "SDS", "SPS", Character.valueOf('S'), Blocks.redstone_lamp, Character.valueOf('R'), Blocks.redstone_torch, Character.valueOf('D'), "dye", Character.valueOf('P'),
                paintBrush }), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.brick_block, "bricksClay"), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.cobblestone, "cobblestone"), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.lapis_block, "blockLapis"), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.obsidian, "obsidian"), (Class)ItemBlockColor.class);
            registerBlock((Block)(colorBlockRedstone = new BlockColor(Blocks.redstone_block, "blockRedstone")), (Class)ItemBlockColor.class);
            registerBlock((Block)new BlockColor(Blocks.coal_block, "blockCoal"), (Class)ItemBlockColor.class);
            registerTile((Class)TileEntityBlockColorData.class);
        }
        if (cobblestoneComprEnabled) {
            registerBlock((Block)(cobblestoneCompr = new BlockCobblestoneCompressed(Material.rock)), (Class)ItemBlockMetadata.class);
            for (int i = 0; i < 16; i++) {
                String s = "compressed" + BlockCobblestoneCompressed.getOreDictName(i) + (1 + BlockCobblestoneCompressed.getCompr(i)) + "x";
                OreDictionary.registerOre(s, new ItemStack((Block)cobblestoneCompr, 1, i));
            }
        }
        if (conveyorEnabled)
            registerBlock(conveyor = (Block)new BlockConveyor());
        if (filingCabinetEnabled) {
            registerBlock((Block)(filingCabinet = new BlockFilingCabinet()), (Class)ItemFilingCabinet.class);
            registerTile((Class)TileEntityFilingCabinet.class);
        }
        if (greenScreenEnabled)
            registerBlock((Block)(greenScreen = new BlockGreenScreen()), (Class)ItemBlockMetadata.class);
        if (peacefultableEnabled)
            registerBlock(peacefultable = (Block)new BlockPeacefulTable());
        if (tradingPostEnabled) {
            registerBlock(tradingPost = (Block)new BlockTradingPost());
            registerTile((Class)TileEntityTradingPost.class);
        }
        if (soundMufflerEnabled) {
            registerBlock(soundMuffler = (Block)new BlockSoundMuffler(), (Class)ItemBlockMetadata.class);
            registerTile((Class)TileEntitySoundMuffler.class);
            registerTile((Class)TileEntityRainMuffler.class);
        }
        if (transferNodeEnabled && transferPipeEnabled) {
            if (Loader.isModLoaded("ForgeMultipart")) {
                registerBlock(transferNode = (Block)new BlockTransferNode(), (Class)ItemBlockTransferNodeMultiPart.class);
                registerBlock(transferNodeRemote = (Block)new BlockRetrievalNode(), (Class)ItemBlockTransferNodeMultiPart.class);
                registerBlock((Block)(transferPipe = new BlockTransferPipe(0)), (Class)ItemBlockTransferPipeMultiPart.class);
                registerBlock((Block)(transferPipe2 = new BlockTransferPipe(1)), (Class)ItemBlockTransferPipeMultiPart.class);
            } else {
                registerBlock(transferNode = (Block)new BlockTransferNode(), (Class)ItemBlockMetadata.class);
                registerBlock(transferNodeRemote = (Block)new BlockRetrievalNode(), (Class)ItemBlockMetadata.class);
                registerBlock((Block)(transferPipe = new BlockTransferPipe(0)), (Class)ItemBlockMetadata.class);
                registerBlock((Block)(transferPipe2 = new BlockTransferPipe(1)), (Class)ItemBlockMetadata.class);
            }
            if (Loader.isModLoaded("ForgeMultipart")) {
                (new RegisterPipeParts()).init();
                (new RegisterTransferNodeParts()).init();
            }
            registerTile((Class)TileEntityTransferNodeInventory.class);
            registerTile((Class)TileEntityTransferNodeLiquid.class);
            registerTile((Class)TileEntityTransferNodeEnergy.class);
            registerTile((Class)TileEntityTransferNodeHyperEnergy.class);
            registerTile((Class)TileEntityRetrievalNodeInventory.class);
            registerTile((Class)TileEntityRetrievalNodeLiquid.class);
            registerTile((Class)TileEntityFilterPipe.class);
        }
        if (nodeUpgradeEnabled) {
            nodeUpgrade = new ItemNodeUpgrade();
            registerItem((Item)nodeUpgrade);
        }
        if (cursedEarthEnabled)
            registerBlock(cursedEarth = (Block)new BlockCursedEarth(), (Class)ItemBlockMetadata.class);
        if (trashCanEnabled) {
            registerBlock(trashCan = (Block)new BlockTrashCan(), (Class)ItemBlockMetadata.class);
            registerTile((Class)TileEntityTrashCan.class);
            registerTile((Class)TileEntityTrashCanFluids.class);
            registerTile((Class)TileEntityTrashCanEnergy.class);
        }
        if (spikeEnabled)
            registerBlock((Block)(spike = new BlockSpike()), (Class)ItemBlockSpike.class);
        if (spikeDiamondEnabled)
            registerBlock((Block)(spikeDiamond = (BlockSpike)new BlockSpikeDiamond()), (Class)ItemBlockSpike.class);
        if (spikeGoldEnabled)
            registerBlock((Block)(spikeGold = (BlockSpike)new BlockSpikeGold()), (Class)ItemBlockSpike.class);
        if (spikeWoodEnabled)
            registerBlock((Block)(spikeWood = new BlockSpikeWood()), (Class)ItemBlockSpike.class);
        if (spikeEnabled || spikeGoldEnabled || spikeDiamondEnabled || spikeWoodEnabled)
            registerTile((Class)TileEntityEnchantedSpike.class);
        if (enderThermicPumpEnabled) {
            registerBlock(enderThermicPump = (Block)new BlockEnderthermicPump());
            registerTile((Class)TileEntityEnderThermicLavaPump.class, "enderPump");
        }
        if (enderQuarryEnabled) {
            registerBlock(enderQuarry = (Block)new BlockEnderQuarry());
            registerBlock((Block)(enderQuarryUpgrade = new BlockQuarryUpgrades()), (Class)ItemBlockMetadata.class);
            registerTile((Class)TileEntityEnderQuarry.class, "enderQuarry");
        }
        if (enderLilyEnabled) {
            registerBlock((Block)(enderLily = new BlockEnderLily()), (Class)ItemBlockEnderLily.class);
            GameRegistry.registerWorldGenerator((IWorldGenerator)new WorldGenEnderLillies(), 9);
        }
        if (timerBlockEnabled)
            registerBlock(timerBlock = (Block)new BlockTimer());
        if (magnumTorchEnabled) {
            if (Loader.isModLoaded("ForgeMultipart")) {
                registerBlock(magnumTorch = (Block)new BlockMagnumTorch(), (Class)ItemBlockMultiPartMagnumTorch.class);
            } else {
                registerBlock(magnumTorch = (Block)new BlockMagnumTorch());
            }
            registerTile((Class)TileEntityAntiMobTorch.class);
            if (Loader.isModLoaded("ForgeMultipart"))
                (new RegisterBlockPart(magnumTorch, MagnumTorchPart.class, "XU|MagnumTorch")).init();
        }
        if (drumEnabled) {
            registerBlock(drum = (Block)new BlockDrum(), (Class)ItemBlockDrum.class);
            registerTile((Class)TileEntityDrum.class, "drum");
        }
        if (generatorEnabled) {
            registerBlock(generator = (Block)new BlockGenerator(), (Class)ItemBlockGenerator.class);
            if (generator2Enabled)
                registerBlock(generator2 = (Block)new BlockGenerator(8), (Class)ItemBlockGenerator.class);
            if (generator3Enabled)
                registerBlock(generator3 = (Block)new BlockGenerator(64), (Class)ItemBlockGenerator.class);
            BlockGenerator.mapTiles();
        }
        if (lawSwordEnabled) {
            lawSword = (Item)new ItemLawSword();
            registerItem(lawSword);
        }
        if (goldenLassoEnabled) {
            goldenLasso = (Item)new ItemGoldenLasso();
            registerItem(goldenLasso);
        }
        if (divisionSigilEnabled) {
            divisionSigil = (Item)new ItemDivisionSigil();
            registerItem(divisionSigil);
        }
        if (unstableIngotEnabled) {
            unstableIngot = (Item)new ItemUnstableIngot();
            registerItem(unstableIngot);
            OreDictionary.registerOre("ingotUnstable", new ItemStack(unstableIngot, 1, 0));
            OreDictionary.registerOre("ingotUnstable", new ItemStack(unstableIngot, 1, 2));
            OreDictionary.registerOre("nuggetUnstable", new ItemStack(unstableIngot, 1, 1));
        }
        if (portalEnabled && (endoftimeDimID != 0 || underdarkDimID != 0)) {
            registerBlock(portal = (Block)new BlockPortal(), (Class)ItemBlockMetadata.class);
            registerTile((Class)TileEntityPortal.class);
            if (endoftimeDimID != 0) {
                if (DimensionManager.isDimensionRegistered(endoftimeDimID))
                    ExtraUtilsMod.proxy.throwLoadingError("Invalid id for 'End of Time' dimension. Change endoftimeDimID in config.", new String[0]);
                DimensionManager.registerProviderType(endoftimeDimID, WorldProviderEndOfTime.class, true);
                DimensionManager.registerDimension(endoftimeDimID, endoftimeDimID);
            }
            if (underdarkDimID != 0) {
                DimensionManager.registerProviderType(underdarkDimID, WorldProviderUnderdark.class, false);
                DimensionManager.registerDimension(underdarkDimID, underdarkDimID);
                MinecraftForge.EVENT_BUS.register(new EventHandlerUnderdark());
                MinecraftForge.ORE_GEN_BUS.register(new EventHandlerUnderdark());
                MinecraftForge.TERRAIN_GEN_BUS.register(new EventHandlerUnderdark());
            }
        }
        if (buildersWandEnabled)
            registerItem(buildersWand = (Item)new ItemBuildersWand(9));
        if (precisionShearsEnabled) {
            precisionShears = new ItemPrecisionShears();
            registerItem((Item)precisionShears);
        }
        if (creativeBuildersWandEnabled) {
            creativeBuildersWand = (new ItemBuildersWand(49)).setUnlocalizedName("extrautils:creativebuilderswand");
            registerItem(creativeBuildersWand);
        }
        if (ethericSwordEnabled) {
            ethericSword = (Item)new ItemEthericSword();
            registerItem(ethericSword);
        }
        if (erosionShovelEnabled) {
            erosionShovel = (Item)new ItemErosionShovel();
            registerItem(erosionShovel);
        }
        if (destructionPickaxeEnabled) {
            destructionPickaxe = (Item)new ItemDestructionPickaxe();
            registerItem(destructionPickaxe);
        }
        if (healingAxeEnabled) {
            healingAxe = (Item)new ItemHealingAxe();
            registerItem(healingAxe);
        }
        if (temporalHoeEnabled) {
            temporalHoe = (Item)new ItemTemporalHoe();
            registerItem(temporalHoe);
        }
        if (sonarGogglesEnabled) {
            sonarGoggles = (Item)new ItemSonarGoggles();
            registerItem(sonarGoggles);
        }
        if (etherealBlockEnabled)
            registerBlock(etheralBlock = (Block)new BlockEtherealStone(), (Class)ItemBlockMetadata.class);
        if (wateringCanEnabled) {
            wateringCan = new ItemWateringCan();
            registerItem((Item)wateringCan);
        }
        if (scannerEnabled) {
            scanner = (Item)new ItemScanner();
            registerItem(scanner);
        }
        if (goldenBagEnabled) {
            goldenBag = (Item)new ItemGoldenBag();
            registerItem(goldenBag);
        }
        if (bedrockiumEnabled) {
            bedrockium = new ItemBedrockiumIngot();
            registerItem((Item)bedrockium);
        }
        if (microBlocksEnabled) {
            microBlocks = (Item)new ItemMicroBlock();
            registerItem(microBlocks);
            RegisterMicroBlocks.register();
        }
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(ExtraUtilsMod.instance, (IGuiHandler)new GuiHandler());
        if (Loader.isModLoaded("ForgeMultipart"))
            FMPRegisterPassThroughInterfaces();
        ExtraUtilsMod.proxy.registerEventHandler();
        ExtraUtilsMod.proxy.registerRenderInformation();
        if (Loader.isModLoaded("ThermalExpansion"))
            TE4IMC.addIntegration();
        if (Loader.isModLoaded("MineFactoryReloaded"))
            MFRIntegration.registerMFRIntegration();
        for (ILoading loader : this.loaders)
            loader.init();
        EE3Integration.finalRegister();
    }

    private void FMPRegisterPassThroughInterfaces() {
        if (Loader.isModLoaded("ForgeMultipart")) {
            RegisterMicroMaterials.registerBlock((Block)cobblestoneCompr, 0, 16);
            RegisterMicroMaterials.registerBlock(enderThermicPump);
            RegisterMicroMaterials.registerBlock(tradingPost);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 0);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 1);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 2);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 3);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 4);
            RegisterMicroMaterials.registerConnectedTexture(etheralBlock, 5);
            RegisterMicroMaterials.registerFullBright(greenScreen);
            for (BlockColor col : colorblocks)
                RegisterMicroMaterials.registerColorBlock(col);
            for (int i = 0; i < 16; i++) {
                if (decorative1 != null && decorative1.name[i] != null)
                    RegisterMicroMaterials.registerConnectedTexture((Block)decorative1, i);
                if (decorative2 != null && decorative2.name[i] != null)
                    RegisterMicroMaterials.registerConnectedTexture((Block)decorative2, i);
            }
        }
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.IAntiMobTorch", false, true);
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid");
        MultipartGenerator.registerPassThroughInterface("com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy");
        MultipartGenerator.registerPassThroughInterface("cofh.api.energy.IEnergyHandler");
    }

    public void postInit(FMLPostInitializationEvent evt) {
        ForgeChunkManager.setForcedChunkLoadingCallback(ExtraUtilsMod.instance, (ForgeChunkManager.LoadingCallback)new ChunkloadCallback());
        CommandTPSTimer.init();
        if (underdarkDimID != 0 && portalEnabled)
            FMLCommonHandler.instance().bus().register(new DarknessTickHandler());
        if (divisionSigilEnabled && !disableDivisionSigilInChests) {
            addSigil("dungeonChest", 0.01D);
            addSigil("mineshaftCorridor", 0.001D);
            addSigil("pyramidDesertyChest", 0.02D);
            addSigil("pyramidJungleChest", 0.05D);
            addSigil("strongholdCrossing", 0.01D);
            addSigil("strongholdCorridor", 0.01D);
        }
        if (!disableEnderLiliesInDungeons && enderLily != null)
            addDungeonItem(new ItemStack((Block)enderLily), 1, 5, "dungeonChest", 0.03D);
        ExtraUtilsMod.proxy.postInit();
        if (enderQuarryEnabled)
            BlockBreakingRegistry.instance.setupBreaking();
        DispenserStuff.registerItems();
        if (generatorEnabled)
            TileEntityGeneratorPotion.genPotionLevels();
        if (transferPipeEnabled)
            TNHelper.initBlocks();
        if (Loader.isModLoaded("Thaumcraft"))
            ThaumcraftHelper.registerItems();
        for (ILoading loader : this.loaders)
            loader.postInit();
    }

    private String standardizeName(String name) {
        if (name.endsWith("enabled"))
            name = name.replaceAll("enabled", "Enabled");
        if (!name.endsWith("Enabled"))
            name = name + "Enabled";
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    private boolean getItem(Configuration config, String string) {
        return config.get("Items", standardizeName(string), true).getBoolean(true);
    }

    private boolean getBlock(Configuration config, String string) {
        return config.get("Blocks", standardizeName(string), true).getBoolean(true);
    }

    public void specialInit() {
        if (qed != null && (!qedList.isEmpty() || !disableQEDIngotSmeltRecipes))
            EnderConstructorRecipesHandler.postInit();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand)new CommandKillEntities("items", EntityItem.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("mobs", EntityMob.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("living", EntityLiving.class, true));
        event.registerServerCommand((ICommand)new CommandKillEntities("xp", EntityXPOrb.class, true));
    }

    public void registerRecipes() {
        registerRecipe((Class)ShapedOreRecipeAlwaysLast.class, RecipeSorter.Category.SHAPED, "after:forge:shapelessore");
        registerRecipe((Class)ShapelessOreRecipeAlwaysLast.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        String unstableIngot = "ingotUnstable";
        if (fullChestEnabled)
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(fullChest), new Object[] { "sss", "sCs", "sss", Character.valueOf('s'), "stickWood", Character.valueOf('C'), Blocks.chest }));
        if (miniChestEnabled) {
            addRecipe((IRecipe)new ShapelessOreRecipeAlwaysLast(new ItemStack(miniChest, 9), new Object[] { Blocks.chest }));
            if (fullChestEnabled)
                addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(miniChest, 9), new Object[] { fullChest }));
            addShapedRecipe(new ItemStack((Block)Blocks.chest), new Object[] { "ccc", "ccc", "ccc", Character.valueOf('c'), miniChest });
        }
        if (enderCollectorEnabled)
            addRecipe(new ItemStack(enderCollector), new Object[] { "eEe", " E ", "OOO", Character.valueOf('e'), Items.ender_pearl, Character.valueOf('E'), (decorative1 == null) ? Blocks.obsidian : "blockEnderObsidian", Character.valueOf('O'), Blocks.obsidian });
        if (gloveEnabled)
            addRecipe((IRecipe)new RecipeGlove(glove));
        if (heatingCoilEnabled)
            addRecipe(new ItemStack((Item)heatingCoil), new Object[] { "III", "I I", "IRI", Character.valueOf('I'), Blocks.heavy_weighted_pressure_plate, Character.valueOf('R'), Items.redstone });
        if (microBlocksEnabled) {
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), new ItemStack(Blocks.wool, 1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1) }, new ItemStack(microBlocks, 8)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { Integer.valueOf(770), Integer.valueOf(772), Integer.valueOf(770), Integer.valueOf(770), Integer.valueOf(772), Integer.valueOf(770), null, Integer.valueOf(772), null }, new ItemStack(microBlocks, 2, 1)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { null, Integer.valueOf(0), null, Integer.valueOf(4), Integer.valueOf(0), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(0), Integer.valueOf(4) }, new ItemStack(microBlocks, 5, 2)));
            addRecipe((IRecipe)new RecipeMicroBlocks(3, 3, new Object[] { Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), new ItemStack((Block)decorative1, 1, 5), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(1) }, new ItemStack(microBlocks, 1, 3)));
        }
        if (bedrockiumBlockEnabled &&
            bedrockiumEnabled) {
            addRecipe(new ItemStack((Block)bedrockiumBlock), new Object[] { "KKK", "KKK", "KKK", Character.valueOf('K'), bedrockium });
            addRecipe(new ItemStack((Item)bedrockium, 9), new Object[] { "K", Character.valueOf('K'), bedrockiumBlock });
            if (cobblestoneComprEnabled)
                addSmelting(new ItemStack((Block)cobblestoneCompr, 1, 7), new ItemStack((Block)bedrockiumBlock), 0);
        }
        if (soulEnabled && ethericSwordEnabled)
            addRecipe((IRecipe)new RecipeSoul());
        if (angelBlockEnabled &&
            !disableAngelBlockRecipe)
            addRecipe(new ItemStack(angelBlock, 1), new Object[] { " H ", "WOW", Character.valueOf('H'), Items.gold_ingot, Character.valueOf('W'), Items.feather, Character.valueOf('O'), Blocks.obsidian });
        if (BUDBlockEnabled) {
            if (!disableBUDBlockRecipe)
                addRecipe(new ItemStack((Block)BUDBlock, 1, 0), new Object[] {
                    "SRS", "SPS", "STS", Character.valueOf('R'), Items.redstone, Character.valueOf('S'), Blocks.stone, Character.valueOf('P'), Blocks.sticky_piston, Character.valueOf('T'),
                    Blocks.redstone_torch });
            if (!disableAdvBUDBlockRecipe)
                addRecipe(new ItemStack((Block)BUDBlock, 1, 3), new Object[] { "SRS", "RBR", "SRS", Character.valueOf('R'), Blocks.redstone_block, Character.valueOf('S'), Blocks.stone, Character.valueOf('B'), new ItemStack((Block)BUDBlock, 1, 0) });
        }
        if (chandelierEnabled &&
            !disableChandelierRecipe)
            addRecipe(new ItemStack(chandelier, 1), new Object[] { "GDG", "TTT", " T ", Character.valueOf('G'), Items.gold_ingot, Character.valueOf('D'), Items.diamond, Character.valueOf('T'), Blocks.torch });
        if (decorative1Enabled && decorative2Enabled) {
            addRecipe(new ItemStack((Block)decorative1, 9, 0), new Object[] { "SBS", "BBB", "SBS", Character.valueOf('S'), Blocks.stone, Character.valueOf('B'), new ItemStack(Blocks.stonebrick, 1, 0) });
            addRecipe(new ItemStack((Block)decorative1, 4, 1), new Object[] { " O ", "OEO", " O ", Character.valueOf('O'), Blocks.obsidian, Character.valueOf('E'), Items.ender_pearl });
            addSmelting(new ItemStack(Blocks.quartz_block), new ItemStack((Block)decorative1, 1, 2), 0);
            addRecipe(new ItemStack((Block)decorative1, 5, 3), new Object[] { " I ", "ISI", " I ", Character.valueOf('I'), Blocks.ice, Character.valueOf('S'), Blocks.stone });
            addRecipe(new ItemStack((Block)decorative1, 4, 4), new Object[] { "BB", "BB", Character.valueOf('B'), new ItemStack((Block)decorative1, 1, 0) });
            addRecipe(new ItemStack((Block)decorative1, 4, 7), new Object[] { "BB", "BB", Character.valueOf('B'), new ItemStack((Block)decorative1, 1, 4) });
            addRecipe(new ItemStack((Block)decorative1, 1, 6), new Object[] { "GG", "GG", Character.valueOf('G'), Blocks.gravel });
            addRecipe((IRecipe)new RecipeMagicalWood());
            addRecipe(new ItemStack((Block)decorative1, 8, 10), new Object[] { "SGS", "GGG", "SGS", Character.valueOf('S'), new ItemStack((Block)Blocks.stone_slab, 1, 5), Character.valueOf('G'), new ItemStack((Block)decorative1, 1, 6) });
            addRecipe(new ItemStack((Block)decorative1, 1, 11), new Object[] { "GBG", "BDB", "GBG", Character.valueOf('G'), new ItemStack((Block)decorative1, 1, 8), Character.valueOf('D'), Items.ender_eye, Character.valueOf('B'), new ItemStack((Block)decorative1, 1, 1) });
            addRecipe(new ItemStack((Block)decorative1, 1, 12), new Object[] { "dEd", "EDE", "dEd", Character.valueOf('D'), new ItemStack((Block)decorative1, 1, 1), Character.valueOf('E'), Items.diamond, Character.valueOf('d'), new ItemStack((Block)decorative1, 1, 2) });
            addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)decorative1, 4, 13), new Object[] { Blocks.end_stone, "sandstone", "sandstone", Blocks.end_stone }));
            addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)decorative1, 4, 14), new Object[] { "dyeMagenta", "dyePurple", Blocks.stone, Blocks.stone, Blocks.stone, Blocks.stone, Items.ender_pearl }));
            addShapedRecipe(new ItemStack((Block)decorative1, 4, 9), new Object[] { "SG", "GS", Character.valueOf('G'), Blocks.glass, Character.valueOf('S'), Blocks.sand });
            addSmelting(new ItemStack((Block)decorative1, 1, 9), new ItemStack((Block)decorative2, 1, 0), 0);
            ItemStack glass = new ItemStack((Block)decorative2, 1, 0);
            addRecipe(new ItemStack((Block)decorative2, 8, 1), new Object[] { "GGG", "G G", "GGG", Character.valueOf('G'), glass });
            addRecipe(new ItemStack((Block)decorative2, 4, 2), new Object[] { "GG", "GG", Character.valueOf('G'), glass });
            addRecipe(new ItemStack((Block)decorative2, 1, 3), new Object[] { "GP", Character.valueOf('G'), glass, Character.valueOf('P'), Items.gunpowder });
            addRecipe(new ItemStack((Block)decorative2, 1, 4), new Object[] { "ggg", "gGg", "ggg", Character.valueOf('G'), glass, Character.valueOf('g'), Items.gold_nugget });
            addRecipe(new ItemStack((Block)decorative2, 4, 5), new Object[] { "GOG", "O O", "GOG", Character.valueOf('G'), glass, Character.valueOf('O'), Blocks.obsidian });
            addRecipe(new ItemStack((Block)decorative2, 5, 6), new Object[] { " G ", "GGG", " G ", Character.valueOf('G'), glass });
            addRecipe(new ItemStack((Block)decorative2, 1, 7), new Object[] { " g ", "gGg", " g ", Character.valueOf('G'), glass, Character.valueOf('g'), Items.glowstone_dust });
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)decorative2, 6, 8), new Object[] { "GpG", "GGG", " G ", Character.valueOf('G'), glass, Character.valueOf('p'), "dyePink" }));
            addRecipe(new ItemStack((Block)decorative2, 1, 9), new Object[] { "G", Character.valueOf('G'), glass });
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)decorative2, 5, 10), new Object[] { "GCG", "CGC", "GCG", Character.valueOf('G'), glass, Character.valueOf('C'), (curtain == null) ? Items.coal : curtain, Character.valueOf('I'), "dyeBlack" }));
            addRecipe(new ItemStack((Block)decorative2, 4, 11), new Object[] { "GOG", "O O", "GOG", Character.valueOf('G'), new ItemStack((Block)decorative2, 1, 10), Character.valueOf('O'), Blocks.obsidian });
        }
        if (curtainEnabled &&
            !disableCurtainRecipe)
            addRecipe(new ItemStack(curtain, 12), new Object[] { "WW", "WW", "WW", Character.valueOf('W'), Blocks.wool });
        if (colorBlockDataEnabled && paintBrushEnabled) {
            if (!disablePaintbrushRecipe)
                addRecipe(new ItemStack(paintBrush, 1), new Object[] { "S  ", " W ", "  W", Character.valueOf('S'), Items.string, Character.valueOf('W'), Items.stick });
            for (BlockColor b : colorblocks) {
                for (int i = 0; i < 16; i++) {
                    if (b.oreName != null) {
                        OreDictionary.registerOre(b.oreName, new ItemStack((Block)b, 1, i));
                        OreDictionary.registerOre(b.oreName + XUHelper.dyes[i].substring(3), new ItemStack((Block)b, 1, i));
                    }
                    if (!disableColoredBlocksRecipes)
                        if (b.customRecipe == null) {
                            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)b, 7, BlockColored.func_150032_b(i)), new Object[] { "SSS", "SDS", "SPS", Character.valueOf('S'), b.baseBlock, Character.valueOf('D'), XUHelper.dyes[i], Character.valueOf('P'), paintBrush }));
                        } else {
                            Object[] tempRecipe = new Object[b.customRecipe.length];
                            for (int j = 0; j < b.customRecipe.length; j++) {
                                if ("dye".equals(b.customRecipe[j])) {
                                    tempRecipe[j] = XUHelper.dyes[i];
                                } else {
                                    tempRecipe[j] = b.customRecipe[j];
                                }
                            }
                            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)b, b.customRecipeNo, BlockColored.func_150032_b(i)), tempRecipe));
                        }
                }
            }
        }
        if (cobblestoneComprEnabled &&
            !disableCompressedCobblestoneRecipe)
            for (int i = 0; i < 16; i++) {
                if (BlockCobblestoneCompressed.getCompr(i) == 0) {
                    String s = BlockCobblestoneCompressed.getOreDictName(i).toLowerCase();
                    OreDictionary.registerOre(s, BlockCobblestoneCompressed.getBlock(i));
                    addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)cobblestoneCompr, 1, i), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), s }));
                    addRecipe(new ItemStack(BlockCobblestoneCompressed.getBlock(i), 9), new Object[] { "X", Character.valueOf('X'), new ItemStack((Block)cobblestoneCompr, 1, i) });
                } else {
                    addRecipe(new ItemStack((Block)cobblestoneCompr, 1, i), new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), new ItemStack((Block)cobblestoneCompr, 1, i - 1) });
                    addRecipe(new ItemStack((Block)cobblestoneCompr, 9, i - 1), new Object[] { "X", Character.valueOf('X'), new ItemStack((Block)cobblestoneCompr, 1, i) });
                }
            }
        if (conveyorEnabled &&
            !disableConveyorRecipe)
            addRecipe(new ItemStack(conveyor, 8), new Object[] { "TTT", "IRI", "TTT", Character.valueOf('T'), Blocks.rail, Character.valueOf('I'), Items.iron_ingot, Character.valueOf('R'), Items.redstone });
        if (filingCabinetEnabled) {
            if (!disableFilingCabinet)
                addRecipe(new ItemStack((Block)filingCabinet, 1, 0), new Object[] { "ICI", "ICI", "ICI", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('C'), Blocks.chest });
            if (!disableAdvFilingCabinet && decorative1Enabled)
                addRecipe(new ItemStack((Block)filingCabinet, 1, 1), new Object[] { "ICI", "ICI", "ICI", Character.valueOf('I'), new ItemStack((Block)decorative1, 1, 8), Character.valueOf('C'), new ItemStack((Block)filingCabinet, 1, 0) });
        }
        if (peacefultableEnabled &&
            !disablePeacefulTableRecipe)
            addRecipe(new ItemStack(peacefultable, 1), new Object[] { "EWE", "WDW", "EWE", Character.valueOf('E'), Items.emerald, Character.valueOf('D'), Items.ender_pearl, Character.valueOf('W'), Blocks.planks });
        if (tradingPostEnabled &&
            !disableTradingPostRecipe)
            addRecipe(new ItemStack(tradingPost, 1), new Object[] { "WEW", "WJW", "WWW", Character.valueOf('W'), Blocks.planks, Character.valueOf('E'), Blocks.emerald_block, Character.valueOf('J'), Blocks.jukebox });
        if (soundMufflerEnabled) {
            if (!disableSoundMufflerRecipe)
                addRecipe(new ItemStack(soundMuffler, 1, 0), new Object[] { "WWW", "WNW", "WWW", Character.valueOf('W'), Blocks.wool, Character.valueOf('N'), Blocks.noteblock });
            if (!disableRainMufflerRecipe)
                addRecipe(new ItemStack(soundMuffler, 1, 1), new Object[] { "WWW", "WBW", "WWW", Character.valueOf('W'), Blocks.wool, Character.valueOf('B'), Items.water_bucket });
        }
        if (transferNodeEnabled && transferPipeEnabled && transferNodeRemoteEnabled) {
            if (!disableTransferPipeRecipe)
                addRecipe(new ItemStack((Block)transferPipe, 8), new Object[] { "SSS", "GRG", "SSS", Character.valueOf('S'), new ItemStack((Block)Blocks.stone_slab, 6, 0), Character.valueOf('G'), Blocks.glass, Character.valueOf('R'), Items.redstone });
            if (!disableSortingPipeRecipe)
                addRecipe(new ItemStack((Block)transferPipe, 2, 8), new Object[] { "SSS", "G#G", "SSS", Character.valueOf('S'), new ItemStack((Block)Blocks.stone_slab, 6, 0), Character.valueOf('G'), Blocks.glass, Character.valueOf('#'), Items.gold_ingot });
            if (!disableFilterPipeRecipe) {
                ArrayList<ItemStack> dyes = new ArrayList<ItemStack>();
                for (String s : new String[] {
                    "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink",
                    "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" })
                    dyes.addAll(OreDictionary.getOres(s));
                addRecipe((IRecipe)new RecipeCustomOres(new ItemStack((Block)transferPipe, 5, 9), new ItemStack(Items.stick), dyes, new Object[] { "sPs", "PPP", "sPs", Character.valueOf('s'), new ItemStack(Items.stick), Character.valueOf('P'), new ItemStack((Block)transferPipe, 1, 0) }));
            }
            if (!disableRationingPipeRecipe) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe, 2, 10), new Object[] { "SSS", "GBG", "SSS", Character.valueOf('B'), new ItemStack(Items.dye, 1, 4), Character.valueOf('S'), new ItemStack((Block)Blocks.stone_slab, 1, 0), Character.valueOf('G'), Blocks.glass }));
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe2, 2, 0), new Object[] { "SSS", "GBG", "SSS", Character.valueOf('B'), new ItemStack(Items.dye, 1, 4), Character.valueOf('S'), new ItemStack(Blocks.stone_button, 1, 0), Character.valueOf('G'), Blocks.glass }));
            }
            if (!disableEnergyPipeRecipe)
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe, 8, 11), new Object[] { "SSS", "RRR", "SSS", Character.valueOf('R'), Items.redstone, Character.valueOf('S'), new ItemStack((Block)Blocks.stone_slab, 6, 0) }));
            if (!disableCrossoverPipeRecipe)
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe, 1, 12), new Object[] { " P ", "PBP", " P ", Character.valueOf('P'), new ItemStack((Block)transferPipe, 1, 0), Character.valueOf('B'), Items.redstone }));
            if (!disableModSortingPipesRecipe)
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe, 1, 13), new Object[] { "BPB", Character.valueOf('P'), new ItemStack((Block)transferPipe, 1, 8), Character.valueOf('B'), Items.redstone }));
            if (!disableEnergyExtractionPipeRecipe)
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack((Block)transferPipe, 1, 14), new Object[] { "B B", "BPB", " P ", Character.valueOf('P'), new ItemStack((Block)transferPipe, 1, 11), Character.valueOf('B'), Items.gold_ingot }));
            if (!disableTransferNodeRecipe) {
                addRecipe(new ItemStack(transferNode, 1, 0), new Object[] {
                    " P ", "RER", "SHS", Character.valueOf('S'), Blocks.stone, Character.valueOf('E'), Blocks.redstone_block, Character.valueOf('H'), Blocks.chest, Character.valueOf('R'),
                    Items.redstone, Character.valueOf('P'), transferPipe });
                addRecipe(new ItemStack(transferNode, 4, 0), new Object[] {
                    " P ", "RER", "SHS", Character.valueOf('S'), Blocks.stone, Character.valueOf('E'), Items.ender_pearl, Character.valueOf('H'), Blocks.chest, Character.valueOf('R'),
                    Items.redstone, Character.valueOf('P'), transferPipe });
            }
            if (!disableTransferNodeLiquidRecipe) {
                addRecipe(new ItemStack(transferNode, 1, 6), new Object[] {
                    " P ", "LEL", "IHI", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('E'), Blocks.redstone_block, Character.valueOf('H'), Items.bucket, Character.valueOf('L'),
                    new ItemStack(Items.dye, 1, 4), Character.valueOf('P'), transferPipe });
                addRecipe(new ItemStack(transferNode, 4, 6), new Object[] {
                    " P ", "LEL", "IHI", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('E'), Items.ender_pearl, Character.valueOf('H'), Items.bucket, Character.valueOf('L'),
                    new ItemStack(Items.dye, 1, 4), Character.valueOf('P'), transferPipe });
            }
            if (!disableTransferNodeRemoteRecipe)
                addRecipe(new ItemStack(transferNodeRemote, 1, 0), new Object[] { " E ", "TeT", " E ", Character.valueOf('e'), Items.emerald, Character.valueOf('E'), Items.ender_pearl, Character.valueOf('T'), new ItemStack(transferNode, 2, 0) });
            if (!disableTransferNodeLiquidRemoteRecipe)
                addRecipe(new ItemStack(transferNodeRemote, 1, 6), new Object[] { " E ", "TeT", " E ", Character.valueOf('e'), Items.diamond, Character.valueOf('E'), Items.ender_pearl, Character.valueOf('T'), new ItemStack(transferNode, 2, 6) });
            if (!disableTransferNodeEnergyRecipe) {
                addRecipe(new ItemStack(transferNode, 1, 12), new Object[] { "GNG", "NRN", "GNG", Character.valueOf('N'), new ItemStack(transferNode, 1, 0), Character.valueOf('G'), Items.gold_ingot, Character.valueOf('R'), (nodeUpgrade == null) ? Blocks.redstone_block : new ItemStack((Item)nodeUpgrade, 1, 8) });
                if (bedrockiumEnabled)
                    addRecipe(new ItemStack(transferNode, 1, 13), new Object[] { " N ", "NBN", " N ", Character.valueOf('N'), new ItemStack(transferNode, 1, 12), Character.valueOf('B'), bedrockium });
            }
        }
        if (nodeUpgradeEnabled) {
            if (!disableFilterRecipe) {
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 1), new Object[] { "RSR", "STS", "RSR", Character.valueOf('R'), Items.redstone, Character.valueOf('S'), Items.stick, Character.valueOf('T'), Items.string });
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.redstone_torch), "Inverted", new ItemStack((Item)nodeUpgrade, 1, 1)));
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.wool), "FuzzyNBT", new ItemStack((Item)nodeUpgrade, 1, 1)));
                addRecipe((IRecipe)new RecipeFilterInvert(Items.stick, "FuzzyMeta", new ItemStack((Item)nodeUpgrade, 1, 1)));
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 10), new Object[] { "L L", " T ", "L L", Character.valueOf('L'), "gemLapis", Character.valueOf('T'), new ItemStack((Item)nodeUpgrade, 1, 1) });
                addRecipe((IRecipe)new RecipeFilterInvert(Item.getItemFromBlock(Blocks.redstone_torch), "Inverted", new ItemStack((Item)nodeUpgrade, 1, 10)));
            }
            if (!disableNodeUpgradeSpeedRecipe) {
                addRecipe(new ItemStack((Item)nodeUpgrade, 4, 0), new Object[] { "RgR", "gGg", "RGR", Character.valueOf('R'), Blocks.redstone_block, Character.valueOf('G'), Items.gold_ingot, Character.valueOf('g'), Items.gold_nugget });
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 2), new Object[] { "LiL", "iPi", "LiL", Character.valueOf('L'), new ItemStack(Items.dye, 1, 4), Character.valueOf('i'), Items.iron_ingot, Character.valueOf('P'), Items.iron_pickaxe });
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 3), new Object[] {
                    "GgG", "DuD", "GDG", Character.valueOf('u'), new ItemStack((Item)nodeUpgrade, 1, 0), Character.valueOf('G'), Items.gold_ingot, Character.valueOf('D'), Items.diamond, Character.valueOf('g'),
                    Items.gold_nugget });
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 5), new Object[] { "ere", "qeq", "ere", Character.valueOf('e'), Items.ender_pearl, Character.valueOf('r'), Blocks.redstone_torch, Character.valueOf('q'), Items.quartz });
                addRecipe(new ItemStack((Item)nodeUpgrade, 1, 6), new Object[] { "ere", "qeq", "eqe", Character.valueOf('e'), Items.ender_pearl, Character.valueOf('r'), Items.redstone, Character.valueOf('q'), Items.quartz });
                addRecipe(new ItemStack((Item)nodeUpgrade, 3, 7), new Object[] {
                    "sRR", "sGs", "GRR", Character.valueOf('R'), Blocks.redstone_block, Character.valueOf('G'), Items.gold_ingot, Character.valueOf('g'), Items.gold_nugget, Character.valueOf('s'),
                    new ItemStack((Item)nodeUpgrade, 4, 0) });
                addRecipe(new ItemStack((Item)nodeUpgrade, 3, 8), new Object[] {
                    "RRR", "sts", "RRR", Character.valueOf('R'), Blocks.redstone_block, Character.valueOf('G'), Items.gold_ingot, Character.valueOf('g'), Items.gold_nugget, Character.valueOf('s'),
                    new ItemStack((Item)nodeUpgrade, 4, 0), Character.valueOf('t'), new ItemStack((Item)nodeUpgrade, 4, 7) });
                addRecipe(new ItemStack((Item)nodeUpgrade, 3, 9), new Object[] {
                    "RgR", "RGg", "RgR", Character.valueOf('R'), Blocks.redstone_block, Character.valueOf('G'), Items.gold_ingot, Character.valueOf('g'), Items.gold_nugget, Character.valueOf('s'),
                    new ItemStack((Item)nodeUpgrade, 4, 0) });
            }
        }
        if (trashCanEnabled &&
            !disableTrashCanRecipe) {
            addRecipe(new ItemStack(trashCan, 1, 0), new Object[] { "SSS", "CHC", "CCC", Character.valueOf('S'), Blocks.stone, Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('H'), Blocks.chest });
            addShapelessRecipe(new ItemStack(trashCan, 1, 1), new Object[] { trashCan, Items.bucket });
            addShapelessRecipe(new ItemStack(trashCan, 1, 2), new Object[] { trashCan, Items.redstone, Items.gold_ingot, Items.redstone, Items.gold_ingot });
        }
        if (!disableSpikeRecipe) {
            if (spikeEnabled)
                addRecipe(new ItemStack(spike.itemSpike, 4), new Object[] { " S ", "SIS", "ICI", Character.valueOf('S'), Items.iron_sword, Character.valueOf('C'), Blocks.iron_block, Character.valueOf('I'), Items.iron_ingot });
            if (spikeGoldEnabled)
                addRecipe(new ItemStack(spikeGold.itemSpike, 4), new Object[] { " S ", "SIS", "ICI", Character.valueOf('S'), Items.golden_sword, Character.valueOf('C'), Blocks.gold_block, Character.valueOf('I'), decorative1Enabled ? new ItemStack((Block)decorative1, 1, 8) : Items.gold_ingot });
            if (spikeDiamondEnabled) {
                ItemStack dSword = new ItemStack(Items.diamond_sword, 1);
                addRecipe(new ItemStack(spikeDiamond.itemSpike, 4), new Object[] { " S ", "SIS", "ICI", Character.valueOf('S'), dSword, Character.valueOf('C'), Blocks.diamond_block, Character.valueOf('I'), spikeGoldEnabled ? spikeGold : Items.diamond });
            }
            if (spikeWoodEnabled)
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(spikeWood.itemSpike, 4), new Object[] { " S ", "SIS", "ICI", Character.valueOf('S'), Items.wooden_sword, Character.valueOf('C'), "logWood", Character.valueOf('I'), "plankWood" }));
        }
        if (enderThermicPumpEnabled &&
            !disableEnderThermicPumpRecipe)
            if (!decorative1Enabled) {
                addRecipe(new ItemStack(enderThermicPump, 1), new Object[] {
                    "ODO", "LEW", "OPO", Character.valueOf('O'), Blocks.obsidian, Character.valueOf('D'), Items.diamond, Character.valueOf('E'), Items.ender_eye, Character.valueOf('P'),
                    Items.iron_pickaxe, Character.valueOf('L'), Items.lava_bucket, Character.valueOf('W'), Items.water_bucket });
                addRecipe(new ItemStack(enderThermicPump, 1), new Object[] {
                    "ODO", "WEL", "OPO", Character.valueOf('O'), Blocks.obsidian, Character.valueOf('D'), Items.diamond, Character.valueOf('E'), Items.ender_eye, Character.valueOf('P'),
                    Items.iron_pickaxe, Character.valueOf('L'), Items.lava_bucket, Character.valueOf('W'), Items.water_bucket });
            } else {
                addRecipe(new ItemStack(enderThermicPump, 1), new Object[] {
                    "ODO", "LEW", "OPO", Character.valueOf('O'), new ItemStack((Block)decorative1, 1, 1), Character.valueOf('D'), Items.diamond, Character.valueOf('E'), Items.ender_eye, Character.valueOf('P'),
                    Items.iron_pickaxe, Character.valueOf('L'), Items.lava_bucket, Character.valueOf('W'), Items.water_bucket });
                addRecipe(new ItemStack(enderThermicPump, 1), new Object[] {
                    "ODO", "WEL", "OPO", Character.valueOf('O'), new ItemStack((Block)decorative1, 1, 1), Character.valueOf('D'), Items.diamond, Character.valueOf('E'), Items.ender_eye, Character.valueOf('P'),
                    Items.iron_pickaxe, Character.valueOf('L'), Items.lava_bucket, Character.valueOf('W'), Items.water_bucket });
            }
        if (enderQuarryEnabled &&
            decorative1Enabled) {
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(enderQuarry), new Object[] {
                "EsE", "CDC", "pPp", Character.valueOf('E'), new ItemStack((Block)decorative1, 1, 1), Character.valueOf('s'), "treeSapling", Character.valueOf('M'), new ItemStack((Block)decorative1, 1, 8), Character.valueOf('C'),
                new ItemStack((Block)decorative1, 1, 11), Character.valueOf('D'), new ItemStack((Block)decorative1, 1, 12), Character.valueOf('P'), Items.diamond_pickaxe, Character.valueOf('p'), (enderThermicPump == null) ? new ItemStack((Block)decorative1, 1, 12) : enderThermicPump }));
            if (enderQuarryUpgradeEnabled && !disableEnderQuarryUpgradesRecipes)
                TileEntityEnderQuarry.addUpgradeRecipes();
        }
        if (enderMarkerEnabled &&
            decorative1Enabled)
            addRecipe(new ItemStack((Block)enderMarker), new Object[] { "E", "D", "D", Character.valueOf('E'), Items.ender_pearl, Character.valueOf('D'), new ItemStack((Block)decorative1, 1, 1) });
        if (timerBlockEnabled &&
            !disableTimerBlockRecipe)
            addRecipe(new ItemStack(timerBlock, 1), new Object[] { "RSR", "STS", "RSR", Character.valueOf('S'), Blocks.stone, Character.valueOf('T'), Blocks.redstone_torch, Character.valueOf('R'), Items.redstone });
        if (magnumTorchEnabled &&
            !disableMagnumTorchRecipe)
            if (chandelier != null) {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(magnumTorch), new Object[] {
                    "RCH", "CWC", "CWC", Character.valueOf('C'), chandelier, Character.valueOf('W'), "logWood", Character.valueOf('R'), new ItemStack((Item)Items.potionitem, 1, 8225), Character.valueOf('H'),
                    new ItemStack((Item)Items.potionitem, 1, 8229) }));
            } else {
                addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(magnumTorch), new Object[] {
                    "RCH", "CWC", "CWC", Character.valueOf('C'), Items.diamond, Character.valueOf('W'), "logWood", Character.valueOf('R'), new ItemStack((Item)Items.potionitem, 1, 8225), Character.valueOf('H'),
                    new ItemStack((Item)Items.potionitem, 1, 8229) }));
            }
        if (drumEnabled &&
            !disableDrumRecipe) {
            addRecipe(new ItemStack(drum, 1, 0), new Object[] { "ISI", "ICI", "ISI", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('S'), Blocks.heavy_weighted_pressure_plate, Character.valueOf('C'), Items.cauldron });
            if (bedrockiumEnabled)
                addRecipe(new ItemStack(drum, 1, 1), new Object[] { "ISI", "ICI", "ISI", Character.valueOf('I'), bedrockium, Character.valueOf('S'), Blocks.light_weighted_pressure_plate, Character.valueOf('C'), Items.cauldron });
        }
        if (generatorEnabled &&
            !disableGeneratorRecipe) {
            BlockGenerator.addRecipes();
            if (generator2Enabled) {
                BlockGenerator.addUpgradeRecipes(generator, generator2);
                if (generator3Enabled)
                    BlockGenerator.addSuperUpgradeRecipes(generator2, generator3);
            }
        }
        if (goldenLassoEnabled &&
            !disableGoldenLassoRecipe) {
            addRecipe(new ItemStack(goldenLasso, 1), new Object[] { "GSG", "SES", "GSG", Character.valueOf('G'), Items.gold_nugget, Character.valueOf('S'), Items.string, Character.valueOf('E'), Items.ender_eye });
            addRecipe((IRecipe)new RecipeHorseTransmutation());
        }
        if (divisionSigilEnabled)
            addRecipe((IRecipe)new RecipeUnsigil());
        if (unstableIngotEnabled) {
            if (!disableUnstableIngotRecipe && divisionSigilEnabled) {
                addRecipe((IRecipe)new RecipeUnEnchanting());
                addRecipe(RecipeUnstableIngotCrafting.addRecipe(new ItemStack(ExtraUtils.unstableIngot), new Object[] { "I", "S", "D", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('S'), ItemDivisionSigil.newActiveSigil(), Character.valueOf('D'), Items.diamond }));
                addRecipe(RecipeUnstableNuggetCrafting.addRecipe(new ItemStack(ExtraUtils.unstableIngot, 1, 1), new Object[] { "g", "S", "D", Character.valueOf('g'), Items.gold_nugget, Character.valueOf('S'), ItemDivisionSigil.newActiveSigil(), Character.valueOf('D'), Items.diamond }));
                ItemStack item = new ItemStack(ExtraUtils.unstableIngot);
                NBTTagCompound tags = new NBTTagCompound();
                tags.setBoolean("stable", true);
                item.setTagCompound(tags);
                addRecipe(item, new Object[] { "uuu", "uuu", "uuu", Character.valueOf('u'), new ItemStack(ExtraUtils.unstableIngot, 1, 1) });
            }
            if (decorative1Enabled)
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)decorative1, 1, 5), new Object[] { "III", "III", "III", Character.valueOf('I'), unstableIngot }));
        }
        if (portalEnabled && underdarkDimID != 0) {
            ItemStack u, b, b2;
            if (ExtraUtils.unstableIngot != null) {
                u = new ItemStack(ExtraUtils.unstableIngot);
            } else {
                u = new ItemStack(Items.nether_star);
            }
            if (cobblestoneCompr != null) {
                b = new ItemStack((Block)cobblestoneCompr, 1, 4);
                b2 = new ItemStack((Block)cobblestoneCompr, 1, 3);
            } else {
                b = new ItemStack(Blocks.diamond_block);
                b2 = new ItemStack(Blocks.diamond_block);
            }
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(portal, 1), new Object[] { "cNc", "NCN", "cNc", Character.valueOf('C'), b, Character.valueOf('c'), b2, Character.valueOf('N'), u }));
        }
        if (portalEnabled && endoftimeDimID != 0) {
            Object end_stone = (decorative1 == null) ? Blocks.end_stone : new ItemStack((Block)decorative1, 1, 14);
            Object obsidian = (decorative1 == null) ? Blocks.obsidian : "burntQuartz";
            addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(portal, 1, 2), new Object[] { "PEP", "ECE", "PEP", Character.valueOf('C'), Items.clock, Character.valueOf('E'), end_stone, Character.valueOf('P'), obsidian }));
        }
        if (buildersWandEnabled &&
            !disableBuildersWandRecipe && unstableIngotEnabled) {
            RecipeUnstableCrafting recipe = RecipeUnstableCrafting.addRecipe(new ItemStack(buildersWand, 1), new Object[] { " I", "S ", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian });
            if (creativeBuildersWandEnabled)
                recipe.setStable(new ItemStack(creativeBuildersWand));
            addRecipe((IRecipe)recipe);
        }
        if (ethericSwordEnabled &&
            !disableEthericSwordRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(ethericSword, 1), new Object[] { "I", "I", "S", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian }).setStableItem(lawSword));
        if (erosionShovelEnabled &&
            !disableErosionShovelRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(erosionShovel, 1), new Object[] { "I", "S", "S", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian }).addStableEnchant(Enchantment.efficiency, 10));
        if (destructionPickaxeEnabled &&
            !disableDestructionPickaxeRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(destructionPickaxe, 1), new Object[] { "III", " S ", " S ", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian }).addStableEnchant(Enchantment.efficiency, 10));
        if (healingAxeEnabled &&
            !disableHealingAxeRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(healingAxe, 1), new Object[] { "II", "IS", " S", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian }).addStableEnchant(Enchantment.efficiency, 10));
        if (temporalHoeEnabled &&
            !disableTemporalHoeRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(temporalHoe, 1), new Object[] { "II", " S", " S", Character.valueOf('I'), unstableIngot, Character.valueOf('S'), Blocks.obsidian }));
        if (sonarGogglesEnabled &&
            !disableSonarGogglesRecipe && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(sonarGoggles, 1), new Object[] { "III", "EIE", Character.valueOf('I'), unstableIngot, Character.valueOf('E'), Items.ender_eye }));
        if (greenScreen != null) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)greenScreen, 4, 8), new Object[] { "GGG", "GIG", "GGG", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), Blocks.stonebrick }));
            if (colorBlockBrick != null)
                for (int j = 0; j < 16; j++) {
                    addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Block)greenScreen, 4, j), new Object[] { "GGG", "GIG", "GGG", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), new ItemStack((Block)colorBlockBrick, 1, j) }));
                }
            String[] dyes = {
                "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink",
                "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
            for (int i = 0; i < 16; i++) {
                addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack((Block)greenScreen, 1, i), new Object[] { new ItemStack((Block)greenScreen, 1, 32767), "dye" + dyes[15 - i] }));
            }
        }
        if (etherealBlockEnabled &&
            !disableEtherealBlockRecipe && unstableIngotEnabled) {
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(etheralBlock, 4, 0), new Object[] { "GGG", "GIG", "GGG", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), Blocks.glass }));
            if (decorative2Enabled) {
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(etheralBlock, 4, 1), new Object[] { "GGG", "GIG", "GGG", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), new ItemStack((Block)decorative2, 1, 0) }));
                addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack(etheralBlock, 4, 2), new Object[] { "GGG", "GIG", "GGG", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), new ItemStack((Block)decorative2, 1, 10) }));
            }
            for (int i = 0; i < 3; i++) {
                addShapelessRecipe(new ItemStack(etheralBlock, 1, i + 3), new Object[] { new ItemStack(etheralBlock, 1, i), Blocks.redstone_torch });
            }
        }
        if (wateringCanEnabled) {
            if (!disableWateringCanRecipe) {
                addRecipe(new ItemStack((Item)wateringCan, 1, 1), new Object[] { "SB ", "SWS", " S ", Character.valueOf('S'), Items.iron_ingot, Character.valueOf('B'), new ItemStack(Items.dye, 1, 15), Character.valueOf('W'), Items.bowl });
                addRecipe((IRecipe)RecipeDifficultySpecific.addRecipe(new boolean[] { true, false, false, false }, new ItemStack((Item)wateringCan, 1, 1), new String[] { "Peaceful Mode only" }, new Object[] { "S  ", "SWS", " S ", Character.valueOf('S'), Blocks.stone, Character.valueOf('W'), Items.bowl }));
            }
            if (!disableSuperWateringCanRecipe &&
                soulEnabled && bedrockiumEnabled)
                addRecipe(new ItemStack((Item)wateringCan, 1, 3), new Object[] { "SB ", "SWS", " S ", Character.valueOf('S'), bedrockium, Character.valueOf('B'), soul, Character.valueOf('W'), Items.bowl });
        }
        if (scannerEnabled)
            addRecipe(new ItemStack(scanner), new Object[] { "III", "ERI", "III", Character.valueOf('E'), Items.ender_eye, Character.valueOf('R'), Items.redstone, Character.valueOf('I'), Items.iron_ingot });
        if (goldenBagEnabled &&
            !disableGoldenBagRecipe) {
            addRecipe(new ItemStack(goldenBag, 1), new Object[] {
                "WdW", "gCg", "WGW", Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 32767), Character.valueOf('d'), Items.diamond, Character.valueOf('g'), Items.gold_ingot, Character.valueOf('B'),
                Items.blaze_powder, Character.valueOf('G'), Blocks.gold_block, Character.valueOf('C'), Blocks.chest });
            addRecipe((IRecipe)new RecipeBagDye());
            if (decorative1Enabled)
                addRecipe(RecipeGBEnchanting.addRecipe(new ItemStack(goldenBag, 1), new Object[] { "WGW", Character.valueOf('d'), Items.diamond, Character.valueOf('G'), new ItemStack(goldenBag, 1), Character.valueOf('W'), new ItemStack((Block)decorative1, 1, 8) }));
        }
        if (bedrockiumEnabled)
            addRecipe(new ItemStack((Item)bedrockium, 1), new Object[] { "IcI", "cdc", "IcI", Character.valueOf('I'), new ItemStack((Block)cobblestoneCompr, 1, 2), Character.valueOf('d'), Blocks.diamond_block, Character.valueOf('c'), new ItemStack((Block)cobblestoneCompr, 1, 3) });
        if (qedEnabled) {
            addRecipe(new ItemStack((Block)qed, 1, 0), new Object[] {
                "RcR", "EdE", "EEE", Character.valueOf('R'), Items.ender_eye, Character.valueOf('c'), Blocks.crafting_table, Character.valueOf('d'), decorative1Enabled ? new ItemStack((Block)decorative1, 1, 12) : Blocks.diamond_block, Character.valueOf('E'),
                decorative1Enabled ? new ItemStack((Block)decorative1, 1, 1) : Blocks.obsidian });
            addRecipe(new ItemStack((Block)qed, 1, 2), new Object[] { " e ", " E ", "EEE", Character.valueOf('e'), Items.ender_eye, Character.valueOf('d'), Items.diamond, Character.valueOf('E'), decorative1Enabled ? new ItemStack((Block)decorative1, 1, 1) : Blocks.obsidian });
        }
        if (!disableWitherRecipe) {
            ItemStack t2 = new ItemStack(Items.nether_star, 1);
            addRecipe((IRecipe)RecipeDifficultySpecific.addRecipe(new boolean[] { true, false, false, false }, t2, new String[] { "Peaceful Mode only" }, new Object[] {
                "WWW", "SSS", "DSB", Character.valueOf('W'), new ItemStack(Items.skull, 1, 1), Character.valueOf('S'), Blocks.soul_sand, Character.valueOf('D'), Items.diamond_sword, Character.valueOf('B'),
                Items.bow }));
        }
        if (precisionShearsEnabled &&
            !disablePrecisionShears && unstableIngotEnabled)
            addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Item)precisionShears), new Object[] { "AI", "IA", Character.valueOf('I'), unstableIngot, Character.valueOf('B'), new ItemStack((Block)decorative1, 1, 5), Character.valueOf('A'), (angelBlock != null) ? angelBlock : Blocks.obsidian }).addStableEnchant(Enchantment.unbreaking, 10));
        if (angelRingEnabled && unstableIngotEnabled) {
            Object[] leftWing = { Blocks.glass, Items.feather, new ItemStack(Items.dye, 1, 5), Items.leather, Items.gold_nugget };
            Object[] rightWing = { Blocks.glass, Items.feather, new ItemStack(Items.dye, 1, 9), Items.leather, Items.gold_nugget };
            for (int i = 0; i < leftWing.length; i++) {
                if (decorative1Enabled)
                    addRecipe((IRecipe)RecipeUnstableCrafting.addRecipe(new ItemStack((Item)angelRing, 1, i), new Object[] {
                        "LGR", "GNG", "IGI", Character.valueOf('I'), unstableIngot, Character.valueOf('G'), Items.gold_ingot, Character.valueOf('N'), Items.nether_star, Character.valueOf('L'),
                        leftWing[i], Character.valueOf('R'), rightWing[i] }));
                addShapelessRecipe(new ItemStack((Item)angelRing, 1, i), new Object[] { leftWing[i], new ItemStack((Item)angelRing, 1, 32767), rightWing[i] });
            }
        }
        if (!disableChestRecipe)
            GameRegistry.addRecipe((IRecipe)new ShapedOreRecipeAlwaysLast(new ItemStack((Block)Blocks.chest, 4), new Object[] { "WWW", "W W", "WWW", Character.valueOf('W'), "logWood" }));
        EE3Integration.addEMCRecipes();
    }

    public void addSmelting(ItemStack ingredient, ItemStack result, int experience) {
        GameRegistry.addSmelting(ingredient, result, experience);
    }

    public void addShapedRecipe(ItemStack out, Object... ingreds) {
        GameRegistry.addShapedRecipe(out, ingreds);
    }

    public void addShapelessRecipe(ItemStack out, Object... ingreds) {
        GameRegistry.addShapelessRecipe(out, ingreds);
    }

    public void registerRecipe(Class<? extends IRecipe> recipe, RecipeSorter.Category cat, String s) {
        if (registeredRecipes.contains(recipe))
            return;
        registeredRecipes.add(recipe);
        RecipeSorter.register("extrautils:" + recipe.getSimpleName(), recipe, cat, s);
    }

    public void serverStart(FMLServerStartingEvent event) {
        ExtraUtilsMod.proxy.newServerStart();
    }

    public void remap(FMLMissingMappingsEvent event) {}

    public void loadComplete(FMLLoadCompleteEvent event) {
        if (!this.hasSpecialInit) {
            this.hasSpecialInit = true;
            specialInit();
        }
    }

    public class ChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {
        public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
            for (ForgeChunkManager.Ticket ticket : tickets) {
                String ticket_id = ticket.getModData().getString("id");
                if (ticket_id.equals("pump")) {
                    int pumpX = ticket.getModData().getInteger("pumpX");
                    int pumpY = ticket.getModData().getInteger("pumpY");
                    int pumpZ = ticket.getModData().getInteger("pumpZ");
                    TileEntityEnderThermicLavaPump tq = (TileEntityEnderThermicLavaPump)world.getTileEntity(pumpX, pumpY, pumpZ);
                    tq.forceChunkLoading(ticket);
                }
                if (ticket_id.equals("quarry")) {
                    int x = ticket.getModData().getInteger("x");
                    int y = ticket.getModData().getInteger("y");
                    int z = ticket.getModData().getInteger("z");
                    TileEntityEnderQuarry tq = (TileEntityEnderQuarry)world.getTileEntity(x, y, z);
                    tq.forceChunkLoading(ticket);
                }
            }
        }

        public List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, int maxTicketCount) {
            List<ForgeChunkManager.Ticket> validTickets = Lists.newArrayList();
            for (ForgeChunkManager.Ticket ticket : tickets) {
                String ticket_id = ticket.getModData().getString("id");
                if (ticket_id.equals("pump") && ExtraUtils.enderThermicPump != null) {
                    int pumpX = ticket.getModData().getInteger("pumpX");
                    int pumpY = ticket.getModData().getInteger("pumpY");
                    int pumpZ = ticket.getModData().getInteger("pumpZ");
                    Block blId = world.getBlock(pumpX, pumpY, pumpZ);
                    if (blId == ExtraUtils.enderThermicPump)
                        validTickets.add(ticket);
                }
                if (ticket_id.equals("quarry") && ExtraUtils.enderQuarry != null && !TileEntityEnderQuarry.disableSelfChunkLoading) {
                    int x = ticket.getModData().getInteger("x");
                    int y = ticket.getModData().getInteger("y");
                    int z = ticket.getModData().getInteger("z");
                    Block blId = world.getBlock(x, y, z);
                    if (blId == ExtraUtils.enderQuarry)
                        validTickets.add(ticket);
                }
            }
            return validTickets;
        }
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ExtraUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
