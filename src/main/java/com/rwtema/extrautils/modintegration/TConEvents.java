package com.rwtema.extrautils.modintegration;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.texture.TextureBedrockLava;
import com.rwtema.extrautils.texture.TextureUnstableLava;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.event.SmelteryCastedEvent;
import tconstruct.library.event.ToolBuildEvent;
import tconstruct.library.event.ToolCraftEvent;
import tconstruct.library.event.ToolCraftedEvent;
import tconstruct.library.util.IToolPart;
import tconstruct.tools.TinkerTools;

public class TConEvents {
  public final double SPEED_REDUCTION = -0.1D;
  
  public static final String TAG_DEADLINE = "XUDeadline";
  
  public final String TAG_LOCALDEADLINE = "XULocalDeadline";
  
  public final String TAG_LOCALDIM = "XULocalDim";
  
  public static final int TICKSTILDESTRUCTION = 200;
  
  public static final UUID uuid = UUID.fromString("52ca0342-0a6b-11e5-a6c0-1697f925ec7b");
  
  public final String TAG_PREFIX = "[TCon]";
  
  public static final String iconName = "extrautils:unstableFluid";
  
  public static final String iconName2 = "extrautils:bedrockFluid";
  
  int curDim;
  
  @SubscribeEvent
  public void getCurrentWorldTicking(TickEvent.WorldTickEvent event) {
    if (event.side == Side.SERVER && event.phase == TickEvent.Phase.START)
      this.curDim = event.world.provider.dimensionId; 
  }
  
  @SubscribeEvent
  public void addUnstableTimer(SmelteryCastedEvent.CastingTable event) {
    if (ExtraUtils.tcon_unstable_material_id <= 0)
      return; 
    ItemStack output = event.output;
    if (output == null || !(output.getItem() instanceof IToolPart))
      return; 
    IToolPart part = (IToolPart)output.getItem();
    if (part.getMaterialID(output) != ExtraUtils.tcon_unstable_material_id)
      return; 
    NBTTagCompound tag = getOrInitTag(output);
    WorldServer world = DimensionManager.getWorld(0);
    if (world == null)
      return; 
    tag.setLong("XUDeadline", world.getTotalWorldTime());
    WorldServer localWorld = DimensionManager.getWorld(this.curDim);
    if (localWorld != null) {
      tag.setLong("XULocalDeadline", localWorld.getTotalWorldTime());
      tag.setInteger("XULocalDim", this.curDim);
    } 
  }
  
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void denyCraft(ToolBuildEvent event) {
    if (ExtraUtils.tcon_unstable_material_id <= 0)
      return; 
    WorldServer world = DimensionManager.getWorld(0);
    if (world == null)
      return; 
    if (isToolExpired(event.headStack, world) || isToolExpired(event.handleStack, world) || isToolExpired(event.accessoryStack, world) || isToolExpired(event.extraStack, world)) {
      event.headStack = null;
      event.handleStack = null;
      event.accessoryStack = null;
      event.extraStack = null;
    } 
  }
  
  public ItemStack handleToolPart(ItemStack stack, WorldServer world) {
    return !isToolExpired(stack, world) ? stack : null;
  }
  
  public static boolean isToolExpired(ItemStack stack) {
    WorldServer world = DimensionManager.getWorld(0);
    return (world != null && isToolExpired(stack, world));
  }
  
  public static boolean isToolExpired(ItemStack stack, WorldServer world) {
    if (stack == null)
      return false; 
    if (ToolBuilder.instance.getMaterialID(stack) == ExtraUtils.tcon_unstable_material_id && stack.hasTagCompound()) {
      NBTTagCompound tag = stack.getTagCompound();
      if (tag.hasKey("XUDeadline", 4)) {
        long deadline = tag.getLong("XUDeadline");
        if (world.getTotalWorldTime() - deadline > 200L)
          return true; 
      } 
    } 
    return false;
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void unstableTooltip(ItemTooltipEvent event) {
    if (event.itemStack == null || event.entityPlayer == null || event.entityPlayer.worldObj == null)
      return; 
    if (ExtraUtils.tcon_unstable_material_id <= 0)
      return; 
    if (ToolBuilder.instance.getMaterialID(event.itemStack) != ExtraUtils.tcon_unstable_material_id)
      return; 
    NBTTagCompound tag = event.itemStack.getTagCompound();
    if (tag == null || !tag.hasKey("XULocalDeadline") || tag.getInteger("XULocalDim") != event.entityPlayer.worldObj.provider.dimensionId) {
      event.toolTip.add(EnumChatFormatting.RED + "Unstable parts will denature after " + '\n' + " seconds" + EnumChatFormatting.RESET);
      return;
    } 
    long finalTime = tag.getLong("XULocalDeadline") + 200L;
    long curTime = event.entityPlayer.worldObj.getTotalWorldTime();
    if (curTime <= finalTime) {
      EnumChatFormatting col = EnumChatFormatting.RED;
      if (curTime >= finalTime - 100L && 
        Minecraft.getSystemTime() % 200L < 100L)
        col = EnumChatFormatting.YELLOW; 
      event.toolTip.add(col + "Part will denature in " + String.format(Locale.ENGLISH, "%.1f", new Object[] { Float.valueOf((float)(finalTime - curTime) / 20.0F) }) + " seconds" + EnumChatFormatting.RESET);
      event.toolTip.add(col + "After that it will become useless" + EnumChatFormatting.RESET);
    } else {
      event.toolTip.add(EnumChatFormatting.RED + "Denatured" + EnumChatFormatting.RESET);
    } 
  }
  
  @SubscribeEvent
  public void addBedrockiumPartSlowness(SmelteryCastedEvent.CastingTable event) {
    if (ExtraUtils.tcon_bedrock_material_id <= 0)
      return; 
    ItemStack output = event.output;
    if (output == null || !(output.getItem() instanceof IToolPart))
      return; 
    IToolPart part = (IToolPart)output.getItem();
    if (part.getMaterialID(output) != ExtraUtils.tcon_bedrock_material_id)
      return; 
    NBTTagCompound tag = getOrInitTag(output);
    assignAttribute(tag, SharedMonsterAttributes.movementSpeed, new AttributeModifier(uuid, "[TCon]Bedrockium Weight", -0.1D, 2));
  }
  
  public static NBTTagCompound getOrInitTag(ItemStack output) {
    NBTTagCompound tag = output.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      output.setTagCompound(tag);
    } 
    return tag;
  }
  
  @SubscribeEvent
  public void handleBedrockMod(ToolCraftedEvent event) {
    if (!(event.tool.getItem() instanceof tconstruct.library.tools.ToolCore))
      return; 
    NBTTagCompound tag = event.tool.getTagCompound();
    if (tag == null)
      return; 
    assignProperSlowness(tag);
  }
  
  @SubscribeEvent
  public void handleBedrockModification(ToolCraftEvent.NormalTool event) {
    assignProperSlowness(event.toolTag);
  }
  
  public void assignProperSlowness(NBTTagCompound tag) {
    removeTags(tag);
    if (ExtraUtils.tcon_bedrock_material_id <= 0)
      return; 
    int i = getNumMaterials(tag.getCompoundTag("InfiTool"), ExtraUtils.tcon_bedrock_material_id);
    if (i == 0)
      return; 
    assignAttribute(tag, SharedMonsterAttributes.movementSpeed, new AttributeModifier(uuid, "[TCon]Bedrockium Weight", -0.1D * i, 2));
    assignAttribute(tag, SharedMonsterAttributes.knockbackResistance, new AttributeModifier(uuid, "[TCon]Bedrockium Weight", 0.5D * i, 2));
  }
  
  public void removeTags(NBTTagCompound tag) {
    NBTTagList nbttaglist = tag.getTagList("AttributeModifiers", 10);
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      NBTTagCompound tagAt = nbttaglist.getCompoundTagAt(i);
      if (tagAt.getString("Name").startsWith("[TCon]"))
        nbttaglist.removeTag(i--); 
    } 
  }
  
  public void assignAttribute(NBTTagCompound tag, IAttribute attribute, AttributeModifier modifier) {
    NBTTagList nbttaglist = tag.getTagList("AttributeModifiers", 10);
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    nbttagcompound.setString("AttributeName", attribute.getAttributeUnlocalizedName());
    nbttagcompound.setString("Name", modifier.getName());
    nbttagcompound.setDouble("Amount", modifier.getAmount());
    nbttagcompound.setInteger("Operation", modifier.getOperation());
    nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
    nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
    nbttaglist.appendTag((NBTBase)nbttagcompound);
    tag.setTag("AttributeModifiers", (NBTBase)nbttaglist);
  }
  
  @SubscribeEvent
  public void handleUnstableCrafting(ToolCraftEvent.NormalTool event) {
    if (ExtraUtils.tcon_unstable_material_id <= 0)
      return; 
    NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
    if (!isUniformTool(toolTag, ExtraUtils.tcon_unstable_material_id))
      return; 
    toolTag.setInteger("Unbreaking", 10);
  }
  
  @SubscribeEvent
  public void handleMagicWood(ToolCraftEvent.NormalTool event) {
    if (ExtraUtils.tcon_magical_wood_id <= 0)
      return; 
    NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
    int modifiers = toolTag.getInteger("Modifiers");
    if (!isUniformTool(toolTag, ExtraUtils.tcon_magical_wood_id)) {
      int bonusModifiers = getNumMaterials(toolTag, ExtraUtils.tcon_magical_wood_id);
      modifiers += bonusModifiers;
      toolTag.setInteger("Modifiers", modifiers);
    } else {
      if (event.tool == TinkerTools.battlesign)
        modifiers += 3; 
      toolTag.setInteger("Modifiers", modifiers + 8);
    } 
  }
  
  public int getNumMaterials(NBTTagCompound toolTag, int materialID) {
    int bonusModifiers = 0;
    if (toolTag.getInteger("Head") == materialID)
      bonusModifiers++; 
    if (toolTag.getInteger("Handle") == materialID)
      bonusModifiers++; 
    if (toolTag.getInteger("Accessory") == materialID)
      bonusModifiers++; 
    if (toolTag.getInteger("Extra") == materialID)
      bonusModifiers++; 
    return bonusModifiers;
  }
  
  public boolean isUniformTool(NBTTagCompound toolTag, int materialId) {
    return (toolTag.getInteger("Head") == materialId && toolTag.getInteger("Handle") == materialId && valid(toolTag.getInteger("Accessory"), materialId) && valid(toolTag.getInteger("Extra"), materialId));
  }
  
  public boolean valid(int i, int materialId) {
    return (i == materialId || i == -1 || i == 0);
  }
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void handleStich(TextureStitchEvent.Pre event) {
    if (event.map.getTextureType() != 0)
      return; 
    TConIntegration.bedrock.setIcons(event.map.registerIcon("TConIntegration.bedrock"));
    TextureBedrockLava textureBedrockLava = new TextureBedrockLava("extrautils:bedrockFluid", "lava_still");
    event.map.setTextureEntry("extrautils:bedrockFluid", (TextureAtlasSprite)textureBedrockLava);
    if (TConIntegration.bedrock != null)
      TConIntegration.bedrock.setIcons((IIcon)textureBedrockLava); 
    textureBedrockLava = new TextureBedrockLava("extrautils:bedrockFluid_flowing", "lava_flow");
    if (event.map.setTextureEntry("extrautils:bedrockFluid_flowing", (TextureAtlasSprite)textureBedrockLava) && 
      TConIntegration.bedrock != null)
      TConIntegration.bedrock.setFlowingIcon((IIcon)textureBedrockLava); 
    TextureUnstableLava textureUnstableLava = new TextureUnstableLava("extrautils:unstableFluid", "water_still");
    event.map.setTextureEntry("extrautils:unstableFluid", (TextureAtlasSprite)textureUnstableLava);
    if (TConIntegration.unstable != null)
      TConIntegration.unstable.setIcons((IIcon)textureUnstableLava); 
    textureUnstableLava = new TextureUnstableLava("extrautils:unstableFluid_flowing", "water_flow");
    if (event.map.setTextureEntry("extrautils:unstableFluid_flowing", (TextureAtlasSprite)textureUnstableLava) && 
      TConIntegration.unstable != null)
      TConIntegration.unstable.setFlowingIcon((IIcon)textureUnstableLava); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConEvents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */