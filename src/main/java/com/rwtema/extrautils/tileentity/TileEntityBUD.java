package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.block.BlockBUD;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class TileEntityBUD extends TileEntity {
    public static final int maxCountDown = 8;

    public int countDown = 0;

    public boolean powered = false;

    int[] metadata = new int[6];

    int[] hashCode = new int[6];

    boolean init = false;

    private Block[] ids = new Block[6];

    private boolean ready;

    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
        if (oldBlock == newBlock) {
            if (((oldMeta >= 3) ? true : false) != ((newMeta >= 3) ? true : false));
            return false;
        }
        return false;
    }

    public Packet getDescriptionPacket() {
        this.powered = (this.countDown >= 6);
        NBTTagCompound t = new NBTTagCompound();
        t.setBoolean("powered", this.powered);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
    }

    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if (this.worldObj.isRemote &&
            pkt.func_148857_g().hasKey("powered")) {
            this.powered = pkt.func_148857_g().getBoolean("powered");
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void updateEntity() {
        if (this.worldObj.isRemote)
            return;
        this.powered = (this.countDown >= 7);
        if (this.countDown > 0) {
            this.countDown--;
            if (this.countDown == 0 || this.countDown == 7 || this.countDown == 5) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                ((BlockBUD)getBlockType()).updateRedstone(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
            }
        }
        this.ready = (this.countDown == 0);
        if (!this.init) {
            this.init = true;
            this.ready = false;
        }
        for (int i = 0; i < 6; i++)
            checkDir(i);
    }

    public void checkDir(int i) {
        int dx = this.xCoord + Facing.offsetsXForSide[i];
        int dy = this.yCoord + Facing.offsetsYForSide[i];
        int dz = this.zCoord + Facing.offsetsZForSide[i];
        if (this.worldObj.blockExists(dx, dy, dz)) {
            Block id = this.worldObj.getBlock(dx, dy, dz);
            if (id != getBlockType())
                if (this.worldObj.isAirBlock(dx, dy, dz)) {
                    if (this.ids[i] != Blocks.air) {
                        if (this.ready)
                            this.countDown = 8;
                        this.ids[i] = Blocks.air;
                        this.metadata[i] = 0;
                        this.hashCode[i] = 0;
                    }
                } else {
                    boolean idChange = false;
                    if (id != this.ids[i]) {
                        if (this.ready)
                            this.countDown = 8;
                        idChange = true;
                        this.ids[i] = id;
                    }
                    int md = this.worldObj.getBlockMetadata(dx, dy, dz);
                    if (md != this.metadata[i]) {
                        if (this.ready)
                            this.countDown = 8;
                        idChange = true;
                        this.metadata[i] = md;
                    }
                    TileEntity tile = this.worldObj.getTileEntity(dx, dy, dz);
                    if (tile != null) {
                        NBTTagCompound t = new NBTTagCompound();
                        tile.writeToNBT(t);
                        int h = t.hashCode();
                        if (h != this.hashCode[i]) {
                            if (this.ready || !idChange)
                                this.countDown = 8;
                            this.hashCode[i] = h;
                        }
                    }
                }
        }
    }

    public boolean getPowered() {
        return this.worldObj.isRemote ? this.powered : ((this.countDown >= 6));
    }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityBUD.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
