package com.rwtema.extrautils;

import com.rwtema.extrautils.core.CastIterator;
import com.rwtema.extrautils.item.ItemBuildersWand;
import com.rwtema.extrautils.tileentity.TileEntityRainMuffler;
import com.rwtema.extrautils.tileentity.generators.DynamicContainerGenerator;
import com.rwtema.extrautils.tileentity.generators.TileEntityGeneratorFurnace;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EventHandlerClient {
  private static final ResourceLocation[] wing_textures = new ResourceLocation[] { null, new ResourceLocation("extrautils", "textures/wing_feather.png"), new ResourceLocation("extrautils", "textures/wing_butterfly.png"), new ResourceLocation("extrautils", "textures/wing_demon.png"), new ResourceLocation("extrautils", "textures/wing_golden.png"), new ResourceLocation("extrautils", "textures/wing_bat.png") };
  
  public static Map<String, Integer> flyingPlayers = new HashMap<String, Integer>();
  
  public static List<String> holograms = new ArrayList<String>();
  
  public boolean resetRender = false;
  
  boolean avoidRecursion = false;
  
  int maxSonarRange = 4;
  
  List<ChunkPos> sonar_edges = new ArrayList<ChunkPos>();
  
  ChunkPos curTarget = new ChunkPos(-1, -1, -1);
  
  private static float renderTickTime;
  
  private static long time = 0L;
  
  public static IIcon particle;
  
  public float getRenderTickTime() {
    return renderTickTime;
  }
  
  public float getRenderTime() {
    return (float)time + renderTickTime;
  }
  
  @SubscribeEvent
  public void getTimer(TickEvent.ClientTickEvent event) {
    time++;
  }
  
  @SubscribeEvent
  public void registerParticle(TextureStitchEvent.Pre event) {
    if (event.map.getTextureType() != 0)
      return; 
    particle = event.map.registerIcon("extrautils:particle");
  }
  
  @SubscribeEvent
  public void getTimer(TickEvent.RenderTickEvent event) {
    renderTickTime = event.renderTickTime;
  }
  
  @SubscribeEvent
  public void generatorTooltip(ItemTooltipEvent event) {
    if (!ExtraUtils.generatorEnabled)
      return; 
    if (event.entityPlayer == null || event.entityPlayer.openContainer == null)
      return; 
    if (event.entityPlayer.openContainer.getClass() == DynamicContainerGenerator.class) {
      TileEntityGeneratorFurnace gen = ((DynamicContainerGenerator)event.entityPlayer.openContainer).genFurnace;
      if (gen != null) {
        double burn = gen.getFuelBurn(event.itemStack);
        if (burn > 0.0D) {
          double level = gen.getGenLevelForStack(event.itemStack);
          double amount = level * burn;
          String s = "Generates ";
          if (amount == (int)amount) {
            s = s + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf((int)amount) });
          } else {
            s = s + String.format(Locale.ENGLISH, "%,.1f", new Object[] { Double.valueOf(amount) });
          } 
          s = s + " RF at ";
          if (level == (int)level) {
            s = s + String.format(Locale.ENGLISH, "%,d", new Object[] { Integer.valueOf((int)level) });
          } else {
            s = s + String.format(Locale.ENGLISH, "%,.1f", new Object[] { Double.valueOf(level) });
          } 
          s = s + " RF/T";
          event.toolTip.add(s);
        } 
      } 
    } 
  }
  
  @SubscribeEvent
  public void renderWings(RenderPlayerEvent.Specials.Post event) {
    if (flyingPlayers.containsKey(event.entityPlayer.getGameProfile().getName())) {
      int tex = ((Integer)flyingPlayers.get(event.entityPlayer.getGameProfile().getName())).intValue();
      if (tex <= 0 || tex >= wing_textures.length)
        return; 
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      event.renderer.modelBipedMain.bipedBody.postRender(0.0625F);
      (Minecraft.getMinecraft()).renderEngine.bindTexture(wing_textures[tex]);
      GL11.glTranslatef(0.0F, -0.3125F, 0.25F);
      Tessellator t = Tessellator.instance;
      double d = 0.0D;
      float a = (1.0F + (float)Math.cos((getRenderTime() / 4.0F))) * 2.0F;
      if (event.entityPlayer.capabilities.isFlying)
        a = (1.0F + (float)Math.cos((getRenderTime() / 4.0F))) * 20.0F; 
      a += 5.0F;
      GL11.glPushMatrix();
      GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-a, 0.0F, 1.0F, 0.0F);
      t.startDrawingQuads();
      t.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
      t.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
      t.addVertexWithUV(1.0D, 1.0D, d, 1.0D, 1.0D);
      t.addVertexWithUV(1.0D, 0.0D, d, 1.0D, 0.0D);
      t.draw();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glRotatef(a, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(20.0F, 0.0F, 1.0F, 0.0F);
      t.startDrawingQuads();
      t.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
      t.addVertexWithUV(0.0D, 1.0D, 0.0D, 0.0D, 1.0D);
      t.addVertexWithUV(-1.0D, 1.0D, d, 1.0D, 1.0D);
      t.addVertexWithUV(-1.0D, 0.0D, d, 1.0D, 0.0D);
      t.draw();
      GL11.glPopMatrix();
      GL11.glPopMatrix();
    } 
  }
  
  public float min0(float x) {
    if (x < 0.0F)
      return 0.0F; 
    return x;
  }
  
  public static final ResourceLocation temaSword = new ResourceLocation("extrautils", "textures/rwtemaSword.png");
  
  @SubscribeEvent
  public void renderSword(RenderPlayerEvent.Specials.Post event) {
    if (!"RWTema".equals(event.entityPlayer.getGameProfile().getName()))
      return; 
    if (event.entityPlayer.getHideCape())
      return; 
    boolean holdingSword = false;
    if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof com.rwtema.extrautils.item.ItemLawSword)
      holdingSword = true; 
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    event.renderer.modelBipedMain.bipedBody.postRender(0.0625F);
    (Minecraft.getMinecraft()).renderEngine.bindTexture(temaSword);
    GL11.glTranslatef(0.0F, 0.23750001F, 0.25F);
    Tessellator t = Tessellator.instance;
    GL11.glRotatef(-55.0F, 0.0F, 1.0F, 1.0F);
    float h = 87.0F;
    float h2 = holdingSword ? 20.0F : 0.0F;
    float w = 18.0F;
    float w2 = 5.0F;
    float w3 = 13.0F;
    double u = (w2 / w);
    float h3 = h2 / h;
    GL11.glScalef(1.7F / h, 1.7F / h, 1.7F / h);
    GL11.glTranslatef(-w2 / 2.0F, -h / 2.0F, 0.0F);
    GL11.glPushMatrix();
    t.startDrawingQuads();
    t.setNormal(0.0F, 0.0F, -1.0F);
    t.addVertexWithUV(0.0D, h2, 0.0D, 0.0D, h3);
    t.addVertexWithUV(0.0D, h, 0.0D, 0.0D, 1.0D);
    t.addVertexWithUV(w2, h, 0.0D, u, 1.0D);
    t.addVertexWithUV(w2, h2, 0.0D, u, h3);
    t.setNormal(0.0F, 0.0F, 1.0F);
    t.addVertexWithUV(w2, h2, w2, u, h3);
    t.addVertexWithUV(w2, h, w2, u, 1.0D);
    t.addVertexWithUV(0.0D, h, w2, 0.0D, 1.0D);
    t.addVertexWithUV(0.0D, h2, w2, 0.0D, h3);
    t.setNormal(1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(w2, h2, w2, u, h3);
    t.addVertexWithUV(w2, h, w2, u, 1.0D);
    t.addVertexWithUV(w2, h, 0.0D, 0.0D, 1.0D);
    t.addVertexWithUV(w2, h2, 0.0D, 0.0D, h3);
    t.setNormal(-1.0F, 0.0F, 0.0F);
    t.addVertexWithUV(0.0D, h2, 0.0D, u, h3);
    t.addVertexWithUV(0.0D, h, 0.0D, u, 1.0D);
    t.addVertexWithUV(0.0D, h, w2, 0.0D, 1.0D);
    t.addVertexWithUV(0.0D, h2, w2, 0.0D, h3);
    if (!holdingSword) {
      t.setNormal(0.0F, -1.0F, 0.0F);
      t.addVertexWithUV(0.0D, 0.0D, 0.0D, (9.0F / w), (4.0F / h));
      t.addVertexWithUV(w2, 0.0D, 0.0D, (13.0F / w), (8.0F / h));
      t.addVertexWithUV(w2, 0.0D, w2, (13.0F / w), (8.0F / h));
      t.addVertexWithUV(0.0D, 0.0D, w2, (9.0F / w), (4.0F / h));
    } 
    t.setNormal(0.0F, 1.0F, 0.0F);
    t.addVertexWithUV(0.0D, h, 0.0D, (9.0F / w), (4.0F / h));
    t.addVertexWithUV(w2, h, 0.0D, (13.0F / w), (8.0F / h));
    t.addVertexWithUV(w2, h, w2, (13.0F / w), (8.0F / h));
    t.addVertexWithUV(0.0D, h, w2, (9.0F / w), (4.0F / h));
    if (!holdingSword) {
      t.setNormal(0.0F, -1.0F, 0.0F);
      t.addVertexWithUV(-3.0D, 16.0D, -3.0D, (6.0F / w), (18.0F / h));
      t.addVertexWithUV(8.0D, 16.0D, -3.0D, (17.0F / w), (18.0F / h));
      t.addVertexWithUV(8.0D, 16.0D, 8.0D, (17.0F / w), (29.0F / h));
      t.addVertexWithUV(-3.0D, 16.0D, 8.0D, (6.0F / w), (29.0F / h));
      t.setNormal(0.0F, 1.0F, 0.0F);
      t.addVertexWithUV(-3.0D, 20.0D, -3.0D, (6.0F / w), (1.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, -3.0D, (17.0F / w), (1.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, 8.0D, (17.0F / w), (12.0F / h));
      t.addVertexWithUV(-3.0D, 20.0D, 8.0D, (6.0F / w), (12.0F / h));
      t.setNormal(0.0F, 0.0F, -1.0F);
      t.addVertexWithUV(-3.0D, 16.0D, -3.0D, u, (12.0F / h));
      t.addVertexWithUV(-3.0D, 20.0D, -3.0D, u, (17.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
      t.addVertexWithUV(8.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
      t.setNormal(0.0F, 0.0F, 1.0F);
      t.addVertexWithUV(-3.0D, 16.0D, 8.0D, u, (12.0F / h));
      t.addVertexWithUV(-3.0D, 20.0D, 8.0D, u, (17.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, 8.0D, 1.0D, (17.0F / h));
      t.addVertexWithUV(8.0D, 16.0D, 8.0D, 1.0D, (12.0F / h));
      t.setNormal(1.0F, 0.0F, 0.0F);
      t.addVertexWithUV(8.0D, 16.0D, 8.0D, u, (12.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, 8.0D, u, (17.0F / h));
      t.addVertexWithUV(8.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
      t.addVertexWithUV(8.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
      t.setNormal(-1.0F, 0.0F, 0.0F);
      t.addVertexWithUV(-3.0D, 16.0D, 8.0D, u, (12.0F / h));
      t.addVertexWithUV(-3.0D, 20.0D, 8.0D, u, (17.0F / h));
      t.addVertexWithUV(-3.0D, 20.0D, -3.0D, 1.0D, (17.0F / h));
      t.addVertexWithUV(-3.0D, 16.0D, -3.0D, 1.0D, (12.0F / h));
    } 
    t.draw();
    GL11.glPopMatrix();
    GL11.glPopMatrix();
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void entityColorRender(RenderLivingEvent.Pre event) {
    String s = EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getCommandSenderName());
    if (s.startsWith("Aureylian") && !(event.entity instanceof EntityPlayer)) {
      GL11.glColor4f(0.9686F, 0.7059F, 0.8392F, 1.0F);
      this.resetRender = true;
    } 
    if (holograms.contains(s) && (!(event.entity instanceof EntityPlayer) || !((EntityPlayer)event.entity).getHideCape())) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.45F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.resetRender = true;
    } 
    if (s.equals("RWTema") && !(event.entity instanceof EntityPlayer)) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.resetRender = true;
    } 
    if (s.equals("jadedcat") && (!(event.entity instanceof EntityPlayer) || !((EntityPlayer)event.entity).getHideCape())) {
      GL11.glColor4f(0.69F, 0.392F, 0.847F, 1.0F);
      this.resetRender = true;
    } 
  }
  
  @SubscribeEvent
  public void entityColorRender(RenderLivingEvent.Post event) {
    if (!this.avoidRecursion && this.resetRender) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
    } 
  }
  
  @SubscribeEvent
  public void SonarRender(DrawBlockHighlightEvent event) {
    if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView != 0)
      return; 
    if (ExtraUtils.sonarGoggles == null || event.player.getCurrentArmor(3) == null || event.player.getCurrentArmor(3).getItem() != ExtraUtils.sonarGoggles)
      return; 
    Block id = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
    if (id == Blocks.air)
      return; 
    if (!id.isOpaqueCube())
      return; 
    int x = MathHelper.floor_double(event.player.chunkCoordX);
    int y = MathHelper.floor_double(event.player.chunkCoordY);
    int z = MathHelper.floor_double(event.player.chunkCoordZ);
    double transparency = 1.0D - (event.player.worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y, z) + event.player.worldObj.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x, y, z) - event.player.worldObj.calculateSkylightSubtracted(1.0F)) / 12.0D;
    if (transparency <= 0.0D)
      return; 
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.35F);
    GL11.glLineWidth(3.0F);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glShadeModel(7425);
    double px = event.player.lastTickPosX + (event.player.chunkCoordX - event.player.lastTickPosX) * event.partialTicks;
    double py = event.player.lastTickPosY + (event.player.chunkCoordY - event.player.lastTickPosY) * event.partialTicks;
    double pz = event.player.lastTickPosZ + (event.player.chunkCoordZ - event.player.lastTickPosZ) * event.partialTicks;
    GL11.glTranslated(-px, -py, -pz);
    if (this.curTarget.x != event.target.blockX || this.curTarget.y != event.target.blockY || this.curTarget.z != event.target.blockZ) {
      this.curTarget = new ChunkPos(event.target.blockX, event.target.blockY, event.target.blockZ);
      this.sonar_edges.clear();
      List<ChunkPos> blocks = new ArrayList<ChunkPos>();
      blocks.add(this.curTarget);
      for (int i = 0; i < blocks.size(); i++) {
        int j = 0;
        boolean[] s = new boolean[27];
        for (int dy = -1; dy <= 1; dy++) {
          for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
              s[j] = false;
              ChunkPos b = new ChunkPos(((ChunkPos)blocks.get(i)).x + dx, ((ChunkPos)blocks.get(i)).y + dy, ((ChunkPos)blocks.get(i)).z + dz);
              if (!blocks.contains(b)) {
                if (dist((b.x - event.target.blockX), (b.y - event.target.blockY), (b.z - event.target.blockZ)) < this.maxSonarRange && 
                  event.player.worldObj.getBlock(b.x, b.y, b.z) == id) {
                  if ((dx == 0 && dy == 0) || (dz == 0 && dy == 0) || (dx == 0 && dz == 0))
                    blocks.add(b); 
                  s[j] = true;
                } 
              } else {
                s[j] = true;
              } 
              j++;
            } 
          } 
        } 
        int k = ((ChunkPos)blocks.get(i)).x, m = ((ChunkPos)blocks.get(i)).y, n = ((ChunkPos)blocks.get(i)).z;
        if (d(s[10], s[12], s[9])) {
          this.sonar_edges.add(new ChunkPos(k, m, n));
          this.sonar_edges.add(new ChunkPos(k, m + 1, n));
        } 
        if (d(s[10], s[14], s[11])) {
          this.sonar_edges.add(new ChunkPos(k, m, n + 1));
          this.sonar_edges.add(new ChunkPos(k, m + 1, n + 1));
        } 
        if (d(s[16], s[12], s[15])) {
          this.sonar_edges.add(new ChunkPos(k + 1, m, n));
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n));
        } 
        if (d(s[16], s[14], s[17])) {
          this.sonar_edges.add(new ChunkPos(k + 1, m, n + 1));
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n + 1));
        } 
        if (d(s[22], s[10], s[19])) {
          this.sonar_edges.add(new ChunkPos(k, m + 1, n));
          this.sonar_edges.add(new ChunkPos(k, m + 1, n + 1));
        } 
        if (d(s[22], s[16], s[25])) {
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n));
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n + 1));
        } 
        if (d(s[22], s[12], s[21])) {
          this.sonar_edges.add(new ChunkPos(k, m + 1, n));
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n));
        } 
        if (d(s[22], s[14], s[23])) {
          this.sonar_edges.add(new ChunkPos(k, m + 1, n + 1));
          this.sonar_edges.add(new ChunkPos(k + 1, m + 1, n + 1));
        } 
        if (d(s[4], s[10], s[1])) {
          this.sonar_edges.add(new ChunkPos(k, m, n));
          this.sonar_edges.add(new ChunkPos(k, m, n + 1));
        } 
        if (d(s[4], s[16], s[7])) {
          this.sonar_edges.add(new ChunkPos(k + 1, m, n));
          this.sonar_edges.add(new ChunkPos(k + 1, m, n + 1));
        } 
        if (d(s[4], s[12], s[3])) {
          this.sonar_edges.add(new ChunkPos(k, m, n));
          this.sonar_edges.add(new ChunkPos(k + 1, m, n));
        } 
        if (d(s[4], s[14], s[5])) {
          this.sonar_edges.add(new ChunkPos(k, m, n + 1));
          this.sonar_edges.add(new ChunkPos(k + 1, m, n + 1));
        } 
      } 
    } 
    Tessellator t = Tessellator.instance;
    t.startDrawing(1);
    t.setColorRGBA(255, 255, 255, 255);
    for (ChunkPos sonar_edge : this.sonar_edges) {
      t.setColorRGBA(255, 255, 255, (int)(transparency * (155.0D - 100.0D * dist(sonar_edge.x - px, sonar_edge.y - py, sonar_edge.z - pz) / (this.maxSonarRange + 1))));
      t.addVertex(sonar_edge.x, sonar_edge.y, sonar_edge.z);
    } 
    t.draw();
    GL11.glShadeModel(7424);
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDisable(3042);
    GL11.glTranslated(px, py, pz);
  }
  
  public boolean d(boolean side1, boolean side2, boolean corner) {
    return ((!side1 && !side2) || (!corner && side1 && side2));
  }
  
  public double dist(double x, double y, double z) {
    return (int)Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
  }
  
  @SubscribeEvent
  public void witherSoundKilling(PlaySoundEvent17 event) {
    if (!ExtraUtils.disableWitherNoiseUnlessNearby)
      return; 
    if (!"mob.wither.spawn".equals(event.name))
      return; 
    for (Entity e : new CastIterator((Minecraft.getMinecraft()).theWorld.getLoadedEntityList())) {
      if (e.getClass() == EntityWither.class)
        return; 
    } 
    event.result = null;
  }
  
  @SubscribeEvent
  public void rainKiller(PlaySoundEvent17 event) {
    if ((Minecraft.getMinecraft()).theWorld != null && (Minecraft.getMinecraft()).thePlayer != null && ExtraUtils.soundMuffler != null) {
      if (event == null || !"ambient.weather.rain".equals(event.name))
        return; 
      WorldClient worldClient = (Minecraft.getMinecraft()).theWorld;
      NBTTagCompound tags = new NBTTagCompound();
      if ((Minecraft.getMinecraft()).thePlayer.getEntityData().hasKey("PlayerPersisted")) {
        tags = (Minecraft.getMinecraft()).thePlayer.getEntityData().getCompoundTag("PlayerPersisted");
      } else {
        (Minecraft.getMinecraft()).thePlayer.getEntityData().setTag("PlayerPersisted", (NBTBase)tags);
      } 
      boolean force = (tags.hasKey("ExtraUtilities|Rain") && tags.getBoolean("ExtraUtilities|Rain"));
      if (!force && TileEntityRainMuffler.playerNeedsMuffler) {
        TileEntityRainMuffler.playerNeedsMufflerInstantCheck = false;
      } else {
        event.result = null;
        if (force)
          return; 
        boolean flag = false;
        if (((World)worldClient).provider.dimensionId != TileEntityRainMuffler.curDimension) {
          flag = true;
        } else if (!(worldClient.getTileEntity(TileEntityRainMuffler.curX, TileEntityRainMuffler.curY, TileEntityRainMuffler.curZ) instanceof TileEntityRainMuffler)) {
          flag = true;
        } else if (worldClient.getTileEntity(TileEntityRainMuffler.curX, TileEntityRainMuffler.curY, TileEntityRainMuffler.curZ).getDistanceFrom((Minecraft.getMinecraft()).thePlayer.chunkCoordX, (Minecraft.getMinecraft()).thePlayer.chunkCoordX, (Minecraft.getMinecraft()).thePlayer.chunkCoordZ) > 4096.0D) {
          flag = true;
        } 
        if (flag) {
          TileEntityRainMuffler.playerNeedsMuffler = true;
          TileEntityRainMuffler.playerNeedsMufflerInstantCheck = true;
          TileEntityRainMuffler.curDimension = -100;
        } 
      } 
    } 
  }
  
  @SubscribeEvent
  public void soundMufflerPlay(PlaySoundEvent17 event) {
    if ((Minecraft.getMinecraft()).theWorld != null && ExtraUtils.soundMuffler != null && event.result != null) {
      WorldClient worldClient = (Minecraft.getMinecraft()).theWorld;
      if (event.result instanceof net.minecraft.client.audio.ITickableSound)
        return; 
      float x = event.result.getXPosF();
      float y = event.result.getYPosF();
      float z = event.result.getZPosF();
      for (int dx = (int)Math.floor((x - 8.0F)) >> 4; dx <= (int)Math.floor((x + 8.0F)) >> 4; dx++) {
        for (int dz = (int)Math.floor((z - 8.0F)) >> 4; dz <= (int)Math.floor((z + 8.0F)) >> 4; dz++) {
          Iterator<TileEntity> var18 = (worldClient.getChunkFromChunkCoords(dx, dz)).chunkTileEntityMap.values().iterator();
          while (var18.hasNext()) {
            TileEntity var22 = var18.next();
            if (!(var22 instanceof com.rwtema.extrautils.tileentity.TileEntitySoundMuffler) || 
              var22.getBlockMetadata() == 1)
              continue; 
            double d = (var22.xCoord + 0.5D - x) * (var22.xCoord + 0.5D - x) + (var22.yCoord + 0.5D - y) * (var22.yCoord + 0.5D - y) + (var22.zCoord + 0.5D - z) * (var22.zCoord + 0.5D - z);
            if (d > 64.0D || d <= 0.0D)
              continue; 
            event.result = new SoundMuffled(event.result);
            d = Math.sqrt(d);
            if (d != 0.0D)
              d = 1.0D / d / 8.0D; 
            worldClient.spawnParticle("smoke", x, y, z, (var22.xCoord + 0.5D - x) * d, (var22.yCoord + 0.5D - y) * d, (var22.zCoord + 0.5D - z) * d);
          } 
        } 
      } 
    } 
  }
  
  public class SoundMuffled implements ISound {
    protected final ISound sound;
    
    public SoundMuffled(ISound sound) {
      this.sound = sound;
    }
    
    public ResourceLocation getPositionedSoundLocation() {
      return this.sound.getPositionedSoundLocation();
    }
    
    public boolean canRepeat() {
      return this.sound.canRepeat();
    }
    
    public int getRepeatDelay() {
      return this.sound.getRepeatDelay();
    }
    
    public float getVolume() {
      return this.sound.getVolume() / 10.0F;
    }
    
    public float getPitch() {
      return this.sound.getPitch();
    }
    
    public float getXPosF() {
      return this.sound.getXPosF();
    }
    
    public float getYPosF() {
      return this.sound.getYPosF();
    }
    
    public float getZPosF() {
      return this.sound.getZPosF();
    }
    
    public ISound.AttenuationType getAttenuationType() {
      return this.sound.getAttenuationType();
    }
  }
  
  @SubscribeEvent
  public void BuildersWandRender(DrawBlockHighlightEvent event) {
    if (event.currentItem != null && 
      event.currentItem.getItem() instanceof ItemBuildersWand) {
      List<ChunkPos> blocks = ((ItemBuildersWand)event.currentItem.getItem()).getPotentialBlocks(event.player, event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ, event.target.sideHit);
      Block blockId = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
      if ((((blockId != Blocks.air) ? 1 : 0) & ((blocks.size() > 0) ? 1 : 0)) != 0) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.35F);
        GL11.glLineWidth(3.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        double px = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks;
        double py = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks;
        double pz = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks;
        GL11.glTranslated(-px, -py, -pz);
        for (ChunkPos temp : blocks)
          drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox(temp.x, temp.y, temp.z, (temp.x + 1), (temp.y + 1), (temp.z + 1))); 
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glTranslated(px, py, pz);
        event.setCanceled(true);
      } 
    } 
  }
  
  private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
    Tessellator var2 = Tessellator.instance;
    var2.startDrawing(3);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
    var2.draw();
    var2.startDrawing(3);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
    var2.draw();
    var2.startDrawing(1);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
    var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
    var2.draw();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\EventHandlerClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */