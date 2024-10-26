package com.rwtema.extrautils;

import com.rwtema.extrautils.block.BlockColor;
import com.rwtema.extrautils.block.render.FakeRenderBlocks;
import com.rwtema.extrautils.block.render.RenderBlockColor;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTextures;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTexturesEthereal;
import com.rwtema.extrautils.block.render.RenderBlockDrum;
import com.rwtema.extrautils.block.render.RenderBlockFullBright;
import com.rwtema.extrautils.block.render.RenderBlockMultiBlock;
import com.rwtema.extrautils.block.render.RenderBlockSpike;
import com.rwtema.extrautils.command.CommandDumpNEIInfo;
import com.rwtema.extrautils.command.CommandDumpNEIInfo2;
import com.rwtema.extrautils.command.CommandDumpTextureSheet;
import com.rwtema.extrautils.command.CommandHologram;
import com.rwtema.extrautils.command.CommandUUID;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.ItemBlockSpike;
import com.rwtema.extrautils.item.RenderItemBlockColor;
import com.rwtema.extrautils.item.RenderItemBlockDrum;
import com.rwtema.extrautils.item.RenderItemGlove;
import com.rwtema.extrautils.item.RenderItemLawSword;
import com.rwtema.extrautils.item.RenderItemMultiTransparency;
import com.rwtema.extrautils.item.RenderItemSpikeSword;
import com.rwtema.extrautils.item.RenderItemUnstable;
import com.rwtema.extrautils.multipart.FakeRenderBlocksMultiPart;
import com.rwtema.extrautils.multipart.microblock.RenderItemMicroblock;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.PacketHandler;
import com.rwtema.extrautils.network.PacketHandlerClient;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketUseItemAlt;
import com.rwtema.extrautils.particle.ParticleHelperClient;
import com.rwtema.extrautils.texture.LiquidColorRegistry;
import com.rwtema.extrautils.tileentity.RenderTileEntitySpike;
import com.rwtema.extrautils.tileentity.TileEntityEnchantedSpike;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@SideOnly(Side.CLIENT)
public class ExtraUtilsClient extends ExtraUtilsProxy {
  public static final RenderItemBlockColor renderItemBlockColor = new RenderItemBlockColor();
  
  public ExtraUtilsClient() {
    INSTANCE = this;
  }
  
  public static final RenderItemUnstable renderItemUnstable = new RenderItemUnstable();
  
  public static final RenderItemSpikeSword renderItemSpikeSword = new RenderItemSpikeSword();
  
  public static final RenderItemMultiTransparency renderItemMultiTransparency = new RenderItemMultiTransparency();
  
  public static final RenderItemBlockDrum renderItemDrum = new RenderItemBlockDrum();
  
  public static final RenderBlockSpike renderBlockSpike = new RenderBlockSpike();
  
  public void registerClientCommands() {
    if (XUHelper.deObf || XUHelper.isTema(Minecraft.getMinecraft().getSession().func_148256_e())) {
      ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpNEIInfo());
      ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpNEIInfo2());
      ClientCommandHandler.instance.registerCommand((ICommand)new CommandDumpTextureSheet());
    } 
    ClientCommandHandler.instance.registerCommand((ICommand)CommandTPSTimer.INSTANCE);
  }
  
  public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
    EntityPlayer player = super.getPlayerFromNetHandler(handler);
    if (player == null)
      return getClientPlayer(); 
    return player;
  }
  
  public void postInit() {}
  
  public void registerEventHandler() {
    MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    FMLCommonHandler.instance().bus().register(new EventHandlerClient());
    ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener)new LiquidColorRegistry());
    ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener((IResourceManagerReloadListener)new ParticleHelperClient());
    ExtraUtils.handlesClientMethods = true;
    if (Loader.isModLoaded("ForgeMultipart"))
      RenderBlockConnectedTextures.fakeRender = (FakeRenderBlocks)new FakeRenderBlocksMultiPart(); 
    ClientCommandHandler.instance.registerCommand((ICommand)new CommandHologram());
    ClientCommandHandler.instance.registerCommand((ICommand)new CommandUUID());
    KeyHandler.INSTANCE.register();
    super.registerEventHandler();
  }
  
  public void throwLoadingError(String cause, String... message) {
    throw new CustomErrorWGui(cause, message);
  }
  
  public boolean isClientSideAvailable() {
    return true;
  }
  
  public void newServerStart() {
    super.newServerStart();
  }
  
  public void registerRenderInformation() {
    colorBlockID = RenderingRegistry.getNextAvailableRenderId();
    fullBrightBlockID = RenderingRegistry.getNextAvailableRenderId();
    multiBlockID = RenderingRegistry.getNextAvailableRenderId();
    spikeBlockID = RenderingRegistry.getNextAvailableRenderId();
    drumRendererID = RenderingRegistry.getNextAvailableRenderId();
    connectedTextureID = RenderingRegistry.getNextAvailableRenderId();
    connectedTextureEtheralID = RenderingRegistry.getNextAvailableRenderId();
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockColor());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockFullBright());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockMultiBlock());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)renderBlockSpike);
    for (Item item : ItemBlockSpike.itemHashSet)
      MinecraftForgeClient.registerItemRenderer(item, (IItemRenderer)renderItemSpikeSword); 
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockDrum());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockConnectedTextures());
    RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new RenderBlockConnectedTexturesEthereal());
    if (ExtraUtils.spikeGoldEnabled)
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnchantedSpike.class, (TileEntitySpecialRenderer)new RenderTileEntitySpike()); 
    if (ExtraUtils.colorBlockDataEnabled)
      for (BlockColor b : ExtraUtils.colorblocks)
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock((Block)b), (IItemRenderer)renderItemBlockColor);  
    if (ExtraUtils.unstableIngot != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.unstableIngot, (IItemRenderer)renderItemUnstable); 
    if (ExtraUtils.erosionShovel != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.erosionShovel, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.destructionPickaxe != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.destructionPickaxe, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.buildersWand != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.buildersWand, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.ethericSword != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.ethericSword, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.healingAxe != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.healingAxe, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.creativeBuildersWand != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.creativeBuildersWand, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.precisionShears != null)
      MinecraftForgeClient.registerItemRenderer((Item)ExtraUtils.precisionShears, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.temporalHoe != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.temporalHoe, (IItemRenderer)renderItemMultiTransparency); 
    if (ExtraUtils.drum != null)
      MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ExtraUtils.drum), (IItemRenderer)renderItemDrum); 
    if (ExtraUtils.microBlocks != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.microBlocks, (IItemRenderer)new RenderItemMicroblock()); 
    if (ExtraUtils.lawSwordEnabled)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.lawSword, (IItemRenderer)new RenderItemLawSword()); 
    if (ExtraUtils.glove != null)
      MinecraftForgeClient.registerItemRenderer(ExtraUtils.glove, (IItemRenderer)RenderItemGlove.INSTANCE); 
  }
  
  public EntityPlayer getClientPlayer() {
    return (EntityPlayer)(Minecraft.getMinecraft()).thePlayer;
  }
  
  public World getClientWorld() {
    return (World)(Minecraft.getMinecraft()).theWorld;
  }
  
  public PacketHandler getNewPacketHandler() {
    return (PacketHandler)new PacketHandlerClient();
  }
  
  public void exectuteClientCode(IClientCode clientCode) {
    clientCode.exectuteClientCode();
  }
  
  public void sendUsePacket(PlayerInteractEvent event) {
    if (event.world.isRemote) {
      Vec3 hitVec = (Minecraft.getMinecraft()).objectMouseOver.hitVec;
      float f = (float)hitVec.xCoord - event.x;
      float f1 = (float)hitVec.yCoord - event.y;
      float f2 = (float)hitVec.zCoord - event.z;
      Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(event.x, event.y, event.z, event.face, event.entityPlayer.inventory.getCurrentItem(), f, f1, f2));
    } 
  }
  
  public void sendUsePacket(int x, int y, int z, int face, ItemStack item, float hitX, float hitY, float hitZ) {
    if (isAltSneaking()) {
      sendAltUsePacket(x, y, z, face, item, hitX, hitY, hitZ);
    } else {
      Minecraft.getMinecraft().getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(x, y, z, face, item, hitX, hitY, hitZ));
    } 
  }
  
  public void sendAltUsePacket(int x, int y, int z, int face, ItemStack item, float hitX, float hitY, float hitZ) {
    NetworkHandler.sendPacketToServer((XUPacketBase)new PacketUseItemAlt(x, y, z, face, item, hitX, hitY, hitZ));
  }
  
  public void sendAltUsePacket(ItemStack item) {
    sendAltUsePacket(-1, -1, -1, 255, item, 0.0F, 0.0F, 0.0F);
  }
  
  public boolean isAltSneaking() {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
      return KeyHandler.getIsKeyPressed((Minecraft.getMinecraft()).gameSettings.keyBindSprint); 
    return super.isAltSneaking();
  }
  
  public <F, T> T apply(ISidedFunction<F, T> func, F input) {
    return func.applyClient(input);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ExtraUtilsClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */