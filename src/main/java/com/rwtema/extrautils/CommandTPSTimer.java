package com.rwtema.extrautils;

import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.packets.PacketTime;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class CommandTPSTimer extends CommandBase {
  public static final CommandTPSTimer INSTANCE = new CommandTPSTimer();

  public static void init() {
    FMLCommonHandler.instance().bus().register(INSTANCE);
  }

  public static HashSet<String> playerSet = new HashSet<String>();

  private static long[] clientTimer = new long[100];

  private static int clientCounter = 0;

  private static String displayString = "";

  public static void add(String commandSenderName) {
    EntityPlayer playerInstance = null;
    for (Object o : (MinecraftServer.getServer().getConfigurationManager()).playerEntityList) {
      EntityPlayer player = (EntityPlayer)o;
      if (commandSenderName.equals(player.getCommandSenderName()))
        playerInstance = player;
    }
    if (playerInstance == null)
      return;
    if (playerSet.contains(commandSenderName)) {
      playerSet.remove(commandSenderName);
      NetworkHandler.sendPacketToPlayer((XUPacketBase)new PacketTime(0L, 255), playerInstance);
    } else {
      playerSet.add(commandSenderName);
    }
  }

  public static void update(int counter, long time) {
    if (counter >= 100 || counter < 0) {
      INSTANCE.display = false;
      return;
    }
    INSTANCE.display = true;
    while (clientCounter != counter) {
      clientCounter++;
      if (clientCounter >= 100)
        clientCounter = 0;
      clientTimer[clientCounter] = time;
    }
    displayString = getDisplayString();
  }

  @SubscribeEvent
  public void onServerTick(TickEvent.ServerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || playerSet.isEmpty())
      return;
    MinecraftServer server = MinecraftServer.getServer();
    int counter = server.getTickCounter() % 100;
    long[] longs = server.tickTimeArray;
    if (longs == null)
      return;
    for (Object o : (server.getConfigurationManager()).playerEntityList) {
      EntityPlayer player = (EntityPlayer)o;
      if (playerSet.contains(player.getCommandSenderName()))
        NetworkHandler.sendPacketToPlayer((XUPacketBase)new PacketTime(longs[counter], counter), player);
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onDraw(TickEvent.RenderTickEvent event) {
    if (event.phase != TickEvent.Phase.END || !this.display || displayString.length() == 0)
      return;
    Minecraft minecraft = Minecraft.getMinecraft();
    if (minecraft.theWorld == null) {
      displayString = "";
      return;
    }
    FontRenderer fontrenderer = minecraft.fontRenderer;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glEnable(3042);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glBlendFunc(770, 771);
    RenderHelper.disableStandardItemLighting();
    fontrenderer.drawString(displayString, 0, 0, -1, true);
  }

  public static String getDisplayString() {
    long sum = 0L;
    long max = 0L;
    for (long l : clientTimer) {
      sum += l;
      max = Math.max(max, l);
    }
    return "TPS: " + formatTimer(clientTimer[clientCounter]) + ", " + formatTimer((sum / clientTimer.length)) + ", " + formatTimer(max);
  }

  public static String formatTimer(double time) {
    double tps = Math.min(1000.0D / time * 1.0E-6D, 20.0D);
    boolean tpsDown = (tps != 20.0D);
    return String.format(Locale.ENGLISH, "%5.2f", new Object[] { Double.valueOf(time * 1.0E-6D) }) + "(" + (tpsDown ? EnumChatFormatting.RED : "") + String.format(Locale.ENGLISH, "%5.2f", new Object[] { Double.valueOf(tps) }) + (tpsDown ? EnumChatFormatting.RESET : "") + ")";
  }

  public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
    return true;
  }

  public String getCommandName() {
    return "xu_tps";
  }

  public String getCommandUsage(ICommandSender p_71518_1_) {
    return "xu_tps";
  }

  public boolean display = false;

  public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
    displayString = "";
    NetworkHandler.sendPacketToServer((XUPacketBase)new PacketTime());
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\CommandTPSTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
