package com.rwtema.extrautils.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class LiquidColorRegistry implements IResourceManagerReloadListener {
    public static final int emptyColor = 16777215;

    public static final int defaultColor = 16777215;

    public static Map<IIcon, Integer> m = new HashMap<IIcon, Integer>();

    private static IResourceManager resourcemanager;

    public static void reset() {
        m.clear();
    }

    public static int getIconColor(IIcon icon, int defaultColor) {
        if (icon == null)
            return defaultColor;
        if (m.containsKey(icon)) {
            Integer col = m.get(icon);
            if (col == null)
                return defaultColor;
            return col.intValue();
        }
        String t = icon.getIconName();
        Integer integer;
        try {
            integer = readIconCol(t);
        } catch (IOException e) {
            integer = null;
        }
        if (integer == null) {
            m.put(icon, null);
            return defaultColor;
        }
        float r = (integer.intValue() >> 16 & 0xFF) / 255.0F * (defaultColor >> 16 & 0xFF) / 255.0F;
        float g = (integer.intValue() >> 8 & 0xFF) / 255.0F * (defaultColor >> 8 & 0xFF) / 255.0F;
        float b = (integer.intValue() & 0xFF) / 255.0F * (defaultColor & 0xFF) / 255.0F;
        integer = Integer.valueOf((int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F));
        m.put(icon, integer);
        return integer.intValue();
    }

    public static Integer readIconCol(String t) throws IOException {
        String s1 = "minecraft";
        String s2 = t;
        int i = t.indexOf(':');
        if (i >= 0) {
            s2 = t.substring(i + 1, t.length());
            if (i > 1)
                s1 = t.substring(0, i);
        }
        s1 = s1.toLowerCase();
        s2 = "textures/blocks/" + s2 + ".png";
        IResource resource = resourcemanager.getResource(new ResourceLocation(s1, s2));
        InputStream inputstream = resource.getInputStream();
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        int height = bufferedimage.getHeight();
        int width = bufferedimage.getWidth();
        int[] aint = new int[height * width];
        bufferedimage.getRGB(0, 0, width, height, aint, 0, width);
        if (aint.length == 0)
            return null;
        float[] lab = new float[3];
        CIELabColorSpace colorSpace = CIELabColorSpace.getInstance();
        for (int l : aint) {
            float[] f = colorSpace.fromRGB(l);
            for (int k = 0; k < 3; k++)
                lab[k] = lab[k] + f[k];
        }
        for (int j = 0; j < 3; j++)
            lab[j] = lab[j] / aint.length;
        float[] col = colorSpace.toRGB(lab);
        return Integer.valueOf(0xFF000000 | (int)(col[0] * 255.0F) << 16 | (int)(col[1] * 255.0F) << 8 | (int)(col[2] * 255.0F));
    }

    public static int getFluidColor(FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null)
            return 16777215;
        if (fluid.getFluid().getIcon(fluid) == null)
            return 16777215;
        return getIconColor(fluid.getFluid().getIcon(fluid), fluid.getFluid().getColor(fluid));
    }

    public void onResourceManagerReload(IResourceManager resourcemanager) {
        reset();
        LiquidColorRegistry.resourcemanager = resourcemanager;
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\LiquidColorRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
