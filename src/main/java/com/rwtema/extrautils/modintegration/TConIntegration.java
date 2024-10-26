package com.rwtema.extrautils.modintegration;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.ILoading;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.crafting.Smeltery;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.modifier.ItemModifier;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.modifiers.tools.ModExtraModifier;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;
import tconstruct.util.config.PHConstruct;
import tconstruct.weaponry.TinkerWeaponry;

public class TConIntegration implements ILoading {
  public static final TConIntegration instance = new TConIntegration();
  
  static {
    MinecraftForge.EVENT_BUS.register(new TConEvents());
  }
  
  public static Fluid unstable = (new Fluid("molten.unstableIngots")).setDensity(3000).setViscosity(6000).setTemperature(1300);
  
  public static Fluid bedrock = (new Fluid("molten.bedrockiumIngots")).setDensity(3000).setViscosity(6000).setTemperature(1300);
  
  public void addBedrockiumMaterial() {
    if (ExtraUtils.bedrockiumBlock == null || ExtraUtils.bedrockium == null) {
      ExtraUtils.tcon_bedrock_material_id = -1;
      return;
    } 
    int id = ExtraUtils.tcon_bedrock_material_id;
    if (id <= 0)
      return; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("Id", id);
    String name = "Bedrockium";
    tag.setString("Name", "Bedrockium");
    tag.setString("localizationString", "material.extrautils.bedrockium");
    tag.setInteger("Durability", 7500);
    tag.setInteger("MiningSpeed", 800);
    tag.setInteger("HarvestLevel", 7);
    tag.setInteger("Attack", 4);
    tag.setFloat("HandleModifier", 1.75F);
    tag.setInteger("Reinforced", 0);
    tag.setFloat("Bow_ProjectileSpeed", 3.0F);
    tag.setInteger("Bow_DrawSpeed", 200);
    tag.setFloat("Projectile_Mass", 40.0F);
    tag.setFloat("Projectile_Fragility", 0.4F);
    tag.setString("Style", EnumChatFormatting.BLACK.toString());
    tag.setInteger("Color", 16777215);
    FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
    FluidRegistry.registerFluid(bedrock);
    FluidType.registerFluidType(bedrock.getName(), (Block)ExtraUtils.bedrockiumBlock, 0, 850, bedrock, true);
    Smeltery.addMelting(new ItemStack((Block)ExtraUtils.bedrockiumBlock, 1), (Block)ExtraUtils.bedrockiumBlock, 0, 850, new FluidStack(bedrock, 1296));
    Smeltery.addMelting(new ItemStack((Item)ExtraUtils.bedrockium, 1, 0), (Block)ExtraUtils.bedrockiumBlock, 0, 850, new FluidStack(bedrock, 144));
    ItemStack ingotcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
    TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack((Block)ExtraUtils.bedrockiumBlock, 1), new FluidStack(bedrock, 1296), null, true, 100);
    TConstructRegistry.getTableCasting().addCastingRecipe(new ItemStack((Item)ExtraUtils.bedrockium, 1), new FluidStack(bedrock, 144), ingotcast, false, 50);
    tag = new NBTTagCompound();
    tag.setString("FluidName", bedrock.getName());
    tag.setInteger("MaterialId", id);
    FMLInterModComms.sendMessage("TConstruct", "addPartCastingMaterial", tag);
    tag = new NBTTagCompound();
    tag.setInteger("MaterialId", id);
    tag.setTag("Item", (NBTBase)(new ItemStack((Item)ExtraUtils.bedrockium, 1, 0)).writeToNBT(new NBTTagCompound()));
    tag.setInteger("Value", 2);
    FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          @SideOnly(Side.CLIENT)
          public void exectuteClientCode() {
            (new TConTextureResourcePackBedrockium("Bedrockium")).register();
          }
        });
  }
  
  public void addMagicWoodMaterial() {
    if (ExtraUtils.decorative1 == null) {
      ExtraUtils.tcon_magical_wood_id = -1;
      return;
    } 
    int id = ExtraUtils.tcon_magical_wood_id;
    if (id <= 0)
      return; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("Id", id);
    String name = "MagicWood";
    tag.setString("Name", "MagicWood");
    tag.setString("localizationString", "material.extrautils.magicwood");
    tag.setInteger("Durability", 97);
    tag.setInteger("MiningSpeed", 150);
    tag.setInteger("HarvestLevel", 1);
    tag.setInteger("Attack", 0);
    tag.setFloat("HandleModifier", 1.0F);
    tag.setInteger("Reinforced", 0);
    tag.setFloat("Bow_ProjectileSpeed", 3.0F);
    tag.setInteger("Bow_DrawSpeed", 18);
    tag.setFloat("Projectile_Mass", 0.69F);
    tag.setFloat("Projectile_Fragility", 0.5F);
    tag.setString("Style", EnumChatFormatting.YELLOW.toString());
    tag.setInteger("Color", 7690273);
    FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);
    ItemStack itemstack = new ItemStack((Block)ExtraUtils.decorative1, 1, 8);
    tag = new NBTTagCompound();
    tag.setInteger("MaterialId", id);
    NBTTagCompound item = new NBTTagCompound();
    itemstack.writeToNBT(item);
    tag.setTag("Item", (NBTBase)item);
    tag.setInteger("Value", 2);
    FMLInterModComms.sendMessage("TConstruct", "addPartBuilderMaterial", tag);
    tag = new NBTTagCompound();
    tag.setInteger("MaterialId", id);
    tag.setInteger("Value", 2);
    item = new NBTTagCompound();
    itemstack.writeToNBT(item);
    tag.setTag("Item", (NBTBase)item);
    FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          @SideOnly(Side.CLIENT)
          public void exectuteClientCode() {
            (new TConTextureResourcePackMagicWood("MagicWood")).register();
          }
        });
  }
  
  public void addUnstableMaterial() {
    if (ExtraUtils.unstableIngot == null || ExtraUtils.decorative1 == null) {
      ExtraUtils.tcon_unstable_material_id = -1;
      return;
    } 
    final int id = ExtraUtils.tcon_unstable_material_id;
    if (id <= 0)
      return; 
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("Id", id);
    String name = "unstableIngot";
    final ToolMaterial mat = new ToolMaterial("unstableIngot", "material.extrautils.unstableIngot", 4, 100, 700, 2, 0.6F, 4, 0.0F, EnumChatFormatting.WHITE.toString(), 16777215);
    TConstructRegistry.addtoolMaterial(id, mat);
    TConstructRegistry.addDefaultToolPartMaterial(id);
    TConstructRegistry.addBowMaterial(id, 109, 1.0F);
    TConstructRegistry.addArrowMaterial(id, 2.4F, 0.0F);
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          @SideOnly(Side.CLIENT)
          public void exectuteClientCode() {
            if (FMLCommonHandler.instance().getSide().isClient())
              TConstructClientRegistry.addMaterialRenderMapping(id, "tinker", mat.name().toLowerCase(), true); 
          }
        });
    FluidRegistry.registerFluid(unstable);
    FluidType.registerFluidType(unstable.getName(), (Block)ExtraUtils.decorative1, 5, 850, unstable, true);
    Smeltery.addMelting(new ItemStack((Block)ExtraUtils.decorative1, 1, 5), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(unstable, 648));
    Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 0), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(unstable, 72));
    Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 1), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(unstable, 8));
    Smeltery.addMelting(new ItemStack(ExtraUtils.unstableIngot, 1, 2), (Block)ExtraUtils.decorative1, 5, 850, new FluidStack(unstable, 144));
    TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack((Block)ExtraUtils.decorative1, 1, 5), new FluidStack(unstable, 1296), null, true, 100);
    List<CastingRecipe> newRecipies = new LinkedList<CastingRecipe>();
    for (CastingRecipe recipe : TConstructRegistry.getTableCasting().getCastingRecipes()) {
      if (recipe.castingMetal.getFluid() != TinkerSmeltery.moltenIronFluid || recipe.cast == null || !(recipe.cast.getItem() instanceof tconstruct.library.util.IPattern) || !(recipe.getResult().getItem() instanceof tconstruct.library.tools.DynamicToolPart))
        continue; 
      newRecipies.add(recipe);
    } 
    FluidType ft = FluidType.getFluidType(unstable);
    for (CastingRecipe recipe : newRecipies) {
      ItemStack output = recipe.getResult().copy();
      output.setItemDamage(id);
      FluidStack liquid2 = new FluidStack(unstable, recipe.castingMetal.amount);
      TConstructRegistry.getTableCasting().addCastingRecipe(output, liquid2, recipe.cast, recipe.consumeCast, recipe.coolTime);
      Smeltery.addMelting(ft, output, 0, liquid2.amount / 2);
    } 
    tag = new NBTTagCompound();
    tag.setInteger("MaterialId", id);
    tag.setTag("Item", (NBTBase)(new ItemStack(ExtraUtils.unstableIngot, 1, 0)).writeToNBT(new NBTTagCompound()));
    tag.setInteger("Value", 2);
    FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
    tag = new NBTTagCompound();
    tag.setInteger("MaterialId", id);
    tag.setTag("Item", (NBTBase)(new ItemStack(ExtraUtils.unstableIngot, 1, 2)).writeToNBT(new NBTTagCompound()));
    tag.setInteger("Value", 2);
    FMLInterModComms.sendMessage("TConstruct", "addMaterialItem", tag);
    ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
          @SideOnly(Side.CLIENT)
          public void exectuteClientCode() {
            (new TConTextureResourcePackUnstableIngot("unstableIngot")).register();
          }
        });
  }
  
  public void init() {
    addBedrockiumMaterial();
    addUnstableMaterial();
    addMagicWoodMaterial();
    addModifiers();
  }
  
  public void addModifiers() {
    ModifyBuilder.registerModifier((ItemModifier)new ModExtraModifier(new ItemStack[] { new ItemStack((Item)ExtraUtils.soul, 1, 0) }"XUSoul"));
  }
  
  public void preInit() {}
  
  public void postInit() {
    if (PHConstruct.alternativeBoltRecipe)
      return; 
    LiquidCasting tb = TConstructRegistry.getTableCasting();
    for (ListIterator<CastingRecipe> iterator = tb.getCastingRecipes().listIterator(); iterator.hasNext(); ) {
      CastingRecipe castingRecipe = iterator.next();
      if (castingRecipe == null || castingRecipe.getClass() != CastingRecipe.class)
        continue; 
      if (castingRecipe.output == null || castingRecipe.output.getItem() != TinkerWeaponry.partBolt)
        continue; 
      if (castingRecipe.cast == null || castingRecipe.cast.getItem() != TinkerTools.toolRod)
        continue; 
      int materialID = ToolBuilder.instance.getMaterialID(castingRecipe.cast);
      if (materialID <= 0)
        continue; 
      if (materialID == ExtraUtils.tcon_unstable_material_id)
        iterator.set(new TConCastingRecipeUnsensitive(castingRecipe)); 
      if (materialID == ExtraUtils.tcon_bedrock_material_id)
        iterator.set(new TConCastingRecipeUnsensitive(castingRecipe)); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConIntegration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */