package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.ItemPaintbrush;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import com.rwtema.extrautils.texture.TextureColorBlockBase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockColor extends Block {
    public static float[][] initColor = new float[16][3];

    static {
        float saturation = 0.85F;
        for (int i = 0; i < 16; i++) {
            float r = EntitySheep.fleeceColorTable[i][0];
            float g = EntitySheep.fleeceColorTable[i][1];
            float b = EntitySheep.fleeceColorTable[i][2];
            float m = (r + g + b) / 3.0F * (1.0F - saturation);
            initColor[i][0] = r * saturation + m;
            initColor[i][1] = g * saturation + m;
            initColor[i][2] = b * saturation + m;
        }
    }

    public int curMetadata = 0;

    public boolean specialTexture;

    public String oreName;

    public Block baseBlock;

    public BlockColor(Block b, String orename) {
        this(b, orename, (String)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "textureName", "field_149768_d" }));
    }

    public Object[] customRecipe = null;

    public int customRecipeNo = 0;

    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return super.getBlockColor();
    }

    public BlockColor(Block b, String orename, String texture) {
        super(b.getMaterial());
        setHardness(((Float)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "blockHardness", "field_149782_v" })).floatValue());
        setResistance(((Float)ReflectionHelper.getPrivateValue(Block.class, b, new String[] { "blockResistance", "field_149781_w" })).floatValue());
        setStepSound(b.stepSound);
        setBlockTextureName(texture);
        setBlockName("extrautils:color_" + b.getUnlocalizedName().substring(5));
        setLightLevel(b.getLightValue() / 15.0F);
        setLightOpacity(b.getLightOpacity());
        this.oreName = orename;
        this.baseBlock = b;
        ExtraUtils.colorblocks.add(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        if (!(par1IIconRegister instanceof TextureMap))
            return;
        String t = getTextureName();
        this.blockIcon = (IIcon)((TextureMap)par1IIconRegister).getTextureExtry("extrautils:bw_(" + t + ")");
        if (this.blockIcon == null) {
            TextureColorBlockBase textureColorBlockBase = new TextureColorBlockBase(t);
            this.blockIcon = (IIcon)textureColorBlockBase;
            ((TextureMap)par1IIconRegister).setTextureEntry("extrautils:bw_(" + t + ")", (TextureAtlasSprite)textureColorBlockBase);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        this.curMetadata = par2;
        return super.getIcon(par1, par2);
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        if (par1IBlockAccess.getBlock(par2, par3, par4) == this)
            this.curMetadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        float[] col = BlockColorData.getColorData(par1IBlockAccess, par2, par3, par4, this.curMetadata);
        return (int)(col[0] * 255.0F) << 16 | (int)(col[1] * 255.0F) << 8 | (int)(col[2] * 255.0F);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        float[] col = initColor[p_149741_1_];
        return (int)(col[0] * 255.0F) << 16 | (int)(col[1] * 255.0F) << 8 | (int)(col[2] * 255.0F);
    }

    public int getRenderType() {
        return ExtraUtilsProxy.colorBlockID;
    }

    public int damageDropped(int par1) {
        return par1;
    }

    @SideOnly(Side.CLIENT) @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int j = 0; j < 16; j++)
            par3List.add(new ItemStack(par1, 1, j));
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par5EntityPlayer != null && par5EntityPlayer.getCurrentEquippedItem() != null) {
            ItemStack itemstack = par5EntityPlayer.getCurrentEquippedItem();
            int metadata = par1World.getBlockMetadata(par2, par3, par4);
            if (itemstack.getItem() instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                if (itemarmor.hasColor(itemstack)) {
                    int l = itemarmor.getColor(itemstack);
                    float r = (l >> 16 & 0xFF) / 255.0F;
                    float g = (l >> 8 & 0xFF) / 255.0F;
                    float b = (l & 0xFF) / 255.0F;
                    if (BlockColorData.changeColorData(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), r, g, b))
                        return true;
                    PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
                }
            } else if (XUHelper.getDyeFromItemStack(itemstack) >= 0) {
                float p = 0.9F;
                float[] col1 = BlockColorData.getColorData((IBlockAccess)par1World, par2, par3, par4);
                float[] col2 = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(XUHelper.getDyeFromItemStack(itemstack))];
                float r = (col1[0] + col2[0]) / 2.0F, g = (col1[1] + col2[1]) / 2.0F, b = (col1[2] + col2[2]) / 2.0F;
                float f = (Math.max(Math.max(col1[0], col1[1]), col1[2]) + Math.max(Math.max(col2[0], col2[1]), col2[2])) / 2.0F;
                float f1 = Math.max(r, Math.max(g, b));
                r = r * f / f1;
                g = g * f / f1;
                b = b * f / f1;
                r = col1[0] * p + col2[0] * (1.0F - p);
                g = col1[1] * p + col2[1] * (1.0F - p);
                b = col1[2] * p + col2[2] * (1.0F - p);
                if (BlockColorData.changeColorData(par1World, par2, par3, par4, metadata, r, g, b))
                    return true;
                PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
            } else if ((((itemstack.getItem() == Items.potionitem) ? 1 : 0) & ((Items.potionitem.getEffects(itemstack) == null || Items.potionitem.getEffects(itemstack).isEmpty()) ? 1 : 0)) != 0) {
                float r = initColor[metadata][0];
                float g = initColor[metadata][1];
                float b = initColor[metadata][2];
                if (BlockColorData.changeColorData(par1World, par2, par3, par4, metadata, r, g, b))
                    return true;
                PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
            } else if (itemstack.getItem() == ExtraUtils.paintBrush) {
                if (!par5EntityPlayer.isSneaking()) {
                    float r = 1.0F, g = 1.0F, b = 1.0F;
                    NBTTagCompound tag = itemstack.getTagCompound();
                    if (tag != null) {
                        if (tag.hasKey("r"))
                            r = tag.getInteger("r") / 255.0F;
                        if (tag.hasKey("g"))
                            g = tag.getInteger("g") / 255.0F;
                        if (tag.hasKey("b"))
                            b = tag.getInteger("b") / 255.0F;
                    }
                    if (BlockColorData.changeColorData(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), r, g, b))
                        return true;
                    PacketTempChat.sendChat(par5EntityPlayer, "Unable to change color at this location");
                } else {
                    float[] col = BlockColorData.getColorData((IBlockAccess)par1World, par2, par3, par4);
                    ItemPaintbrush.setColor(itemstack, (int)(col[0] * 255.0F), (int)(col[1] * 255.0F), (int)(col[2] * 255.0F), metadata);
                }
            }
        }
        return false;
    }

    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
        int metadata = world.getBlockMetadata(x, y, z);
        float p = 0.9F;
        float[] col1 = BlockColorData.getColorData((IBlockAccess)world, x, y, z);
        float[] col2 = initColor[colour];
        float r = col1[0] * p + col2[0] * (1.0F - p);
        float g = col1[1] * p + col2[1] * (1.0F - p);
        float b = col1[2] * p + col2[2] * (1.0F - p);
        return BlockColorData.changeColorData(world, x, y, z, metadata, r, g, b);
    }

    public BlockColor setCustomRecipe(int customRecipeNo, Object... customRecipe) {
        this.customRecipe = customRecipe;
        this.customRecipeNo = customRecipeNo;
        return this;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.baseBlock.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
        this.baseBlock.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);
    }

    public boolean canProvidePower() {
        return this.baseBlock.canProvidePower();
    }

    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
        return this.baseBlock.isProvidingWeakPower(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, p_149709_5_);
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
