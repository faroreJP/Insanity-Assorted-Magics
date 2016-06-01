package jp.plusplus.fbs.world.autumn;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/07.
 */
public class TeleporterAutumn extends Teleporter {
    private final LongHashMap destinationCoordinateCache = new LongHashMap();
    private final List destinationCoordinateKeys = new ArrayList();
    private final WorldServer worldServerInstance;
    private final Random random;

    public TeleporterAutumn(WorldServer p_i1963_1_) {
        super(p_i1963_1_);
        worldServerInstance=p_i1963_1_;
        random=new Random();
    }

    // 近くにポータルがあったらそこに出現させる処理
    // このサンプルではネザーポータルの処理をそのまま利用する
    @Override
    public boolean placeInExistingPortal(Entity entity, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
        short short1 = 128;
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor_double(entity.posX);
        int i1 = MathHelper.floor_double(entity.posZ);
        long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
        boolean flag = true;
        double z;
        int l3;

        if (this.destinationCoordinateCache.containsItem(j1)) {
            Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache.getValueByKey(j1);
            d3 = 0.0D;
            i = portalposition.posX;
            j = portalposition.posY;
            k = portalposition.posZ;
            portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
        } else {
            for (l3 = l - short1; l3 <= l + short1; ++l3) {
                double d4 = (double) l3 + 0.5D - entity.posX;

                for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
                    double d5 = (double) l1 + 0.5D - entity.posZ;

                    for (int i2 = this.worldServerInstance.getActualHeight() - 1; i2 >= 0; --i2) {
                        if (this.worldServerInstance.getBlock(l3, i2, l1) == BlockCore.portal2) {
                            while (this.worldServerInstance.getBlock(l3, i2 - 1, l1) == BlockCore.portal2) {
                                --i2;
                            }

                            z = (double) i2 + 0.5D - entity.posY;
                            double d8 = d4 * d4 + z * z + d5 * d5;

                            if (d3 < 0.0D || d8 < d3) {
                                d3 = d8;
                                i = l3;
                                j = i2;
                                k = l1;
                            }
                        }
                    }
                }
            }
        }

        if (d3 >= 0.0D) {
            if (flag) {
                this.destinationCoordinateCache.add(j1, new Teleporter.PortalPosition(i, j, k, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(j1));
            }

            double x = (double) i + 0.5D;
            double y = (double) j + 0.5D;
            z = (double) k + 0.5D;

            entity.motionX = entity.motionY = entity.motionZ = 0.0D;

            entity.setLocationAndAngles(x, y+1, z, entity.rotationYaw, entity.rotationPitch);
            return true;
        } else {
            return false;
        }
    }

    // ポータルを作成する処理
    // このサンプルではネザーポータルの処理をそのまま利用する
    @Override
    public boolean makePortal(Entity p_85188_1_) {
        byte b0 = 16;
        double d0 = -1.0D;
        int i = MathHelper.floor_double(p_85188_1_.posX);
        int j = MathHelper.floor_double(p_85188_1_.posY);
        int k = MathHelper.floor_double(p_85188_1_.posZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.random.nextInt(4);
        int x;
        double d1;
        int z;
        double d2;
        int y;
        int j3;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        int x2;
        int y2;
        double d3;
        double d4;

        for (x = i - b0; x <= i + b0; ++x) {
            d1 = (double) x + 0.5D - p_85188_1_.posX;

            for (z = k - b0; z <= k + b0; ++z) {
                d2 = (double) z + 0.5D - p_85188_1_.posZ;
                label274:

                for (y = this.worldServerInstance.getActualHeight() - 1; y >= 0; --y) {
                    if (this.worldServerInstance.isAirBlock(x, y, z)) {
                        while (y > 0 && this.worldServerInstance.isAirBlock(x, y - 1, z)) {
                            --y;
                        }

                        for (j3 = l1; j3 < l1 + 4; ++j3) {
                            k3 = j3 % 2;
                            l3 = 1 - k3;

                            if (j3 % 4 >= 2) {
                                k3 = -k3;
                                l3 = -l3;
                            }

                            for (i4 = 0; i4 < 3; ++i4) {
                                for (j4 = 0; j4 < 4; ++j4) {
                                    for (k4 = -1; k4 < 4; ++k4) {
                                        x2 = x + (j4 - 1) * k3 + i4 * l3;
                                        y2 = y + k4;
                                        int z2 = z + (j4 - 1) * l3 - i4 * k3;

                                        if (k4 < 0 && !this.worldServerInstance.getBlock(x2, y2, z2).getMaterial().isSolid() || k4 >= 0 && !this.worldServerInstance.isAirBlock(x2, y2, z2)) {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            d3 = (double) y + 0.5D - p_85188_1_.posY;
                            d4 = d1 * d1 + d3 * d3 + d2 * d2;

                            if (d0 < 0.0D || d4 < d0) {
                                d0 = d4;
                                l = x;
                                i1 = y;
                                j1 = z;
                                k1 = j3 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (x = i - b0; x <= i + b0; ++x) {
                d1 = (double) x + 0.5D - p_85188_1_.posX;

                for (z = k - b0; z <= k + b0; ++z) {
                    d2 = (double) z + 0.5D - p_85188_1_.posZ;
                    label222:

                    for (y = this.worldServerInstance.getActualHeight() - 1; y >= 0; --y) {
                        if (this.worldServerInstance.isAirBlock(x, y, z)) {
                            while (y > 0 && this.worldServerInstance.isAirBlock(x, y - 1, z)) {
                                --y;
                            }

                            for (j3 = l1; j3 < l1 + 2; ++j3) {
                                k3 = j3 % 2;
                                l3 = 1 - k3;

                                for (i4 = 0; i4 < 4; ++i4) {
                                    for (j4 = -1; j4 < 4; ++j4) {
                                        k4 = x + (i4 - 1) * k3;
                                        x2 = y + j4;
                                        y2 = z + (i4 - 1) * l3;

                                        if (j4 < 0 && !this.worldServerInstance.getBlock(k4, x2, y2).getMaterial().isSolid() || j4 >= 0 && !this.worldServerInstance.isAirBlock(k4, x2, y2)) {
                                            continue label222;
                                        }
                                    }
                                }

                                d3 = (double) y + 0.5D - p_85188_1_.posY;
                                d4 = d1 * d1 + d3 * d3 + d2 * d2;

                                if (d0 < 0.0D || d4 < d0) {
                                    d0 = d4;
                                    l = x;
                                    i1 = y;
                                    j1 = z;
                                    k1 = j3 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int k5 = l;
        int j2 = i1;
        z = j1;
        int l5 = k1 % 2;
        int l2 = 1 - l5;

        if (k1 % 4 >= 2) {
            l5 = -l5;
            l2 = -l2;
        }

        boolean flag;

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > this.worldServerInstance.getActualHeight() - 10) {
                i1 = this.worldServerInstance.getActualHeight() - 10;
            }

            j2 = i1;

            for (y = -1; y <= 1; ++y) {
                for (j3 = 1; j3 < 3; ++j3) {
                    for (k3 = -1; k3 < 3; ++k3) {
                        l3 = k5 + (j3 - 1) * l5 + y * l2;
                        i4 = j2 + k3;
                        j4 = z + (j3 - 1) * l2 - y * l5;
                        flag = k3 < 0;
                        this.worldServerInstance.setBlock(l3, i4, j4, flag ? BlockCore.plank : Blocks.air);
                    }
                }
            }
        }

        worldServerInstance.setBlock(k5, j2-1, z, BlockCore.portal2, 0, 2);
        /*
        for (y = 0; y < 4; ++y) {
            for (j3 = 0; j3 < 4; ++j3) {
                for (k3 = -1; k3 < 4; ++k3) {
                    l3 = k5 + (j3 - 1) * l5;
                    i4 = j2 + k3;
                    j4 = z + (j3 - 1) * l2;
                    flag = j3 == 0 || j3 == 3 || k3 == -1 || k3 == 3;
                    this.worldServerInstance.setBlock(l3, i4, j4, (Block) (flag ? BlockCore.plank : BlockCore.portal2), 0, 2);
                }
            }

            for (j3 = 0; j3 < 4; ++j3) {
                for (k3 = -1; k3 < 4; ++k3) {
                    l3 = k5 + (j3 - 1) * l5;
                    i4 = j2 + k3;
                    j4 = z + (j3 - 1) * l2;
                    this.worldServerInstance.notifyBlocksOfNeighborChange(l3, i4, j4, this.worldServerInstance.getBlock(l3, i4, j4));
                }
            }
        }
        */

        return true;
    }

    public void removeStalePortalLocations(long p_85189_1_) {
        if (p_85189_1_ % 100L == 0L) {
            Iterator iterator = this.destinationCoordinateKeys.iterator();
            long j = p_85189_1_ - 600L;

            while (iterator.hasNext()) {
                Long olong = (Long) iterator.next();
                Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache.getValueByKey(olong.longValue());

                if (portalposition == null || portalposition.lastUpdateTime < j) {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }

    @Override
    public void placeInPortal(Entity p_77185_1_, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {
        if (!this.placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_, p_77185_6_, p_77185_8_)) {
            this.makePortal(p_77185_1_);
            this.placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_, p_77185_6_, p_77185_8_);
        }

        /*
        if (this.worldServerInstance.provider.dimensionId != FBS.dimensionAutumnId) {
            if (!this.placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_, p_77185_6_, p_77185_8_)) {
                this.makePortal(p_77185_1_);
                this.placeInExistingPortal(p_77185_1_, p_77185_2_, p_77185_4_, p_77185_6_, p_77185_8_);
            }
        } else {
            int i = MathHelper.floor_double(p_77185_1_.posX);
            int j = MathHelper.floor_double(p_77185_1_.posY) - 1;
            int k = MathHelper.floor_double(p_77185_1_.posZ);
            byte b0 = 1;
            byte b1 = 0;

            for (int l = -4; l <= 4; ++l) {
                for (int i1 = -4; i1 <= 4; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                        int blockX = i + i1 * b0 + l * b1;
                        int blockY = j + j1;
                        int blockZ = k + i1 * b1 - l * b0;
                        boolean flag = j1 < 0;
                        this.worldServerInstance.setBlock(blockX, blockY, blockZ, flag ? BlockCore.plank : Blocks.air);
                    }
                }
            }

            p_77185_1_.setLocationAndAngles((double) i+2, (double) j, (double) k+2, p_77185_1_.rotationYaw, 0.0F);
            p_77185_1_.motionX = p_77185_1_.motionY = p_77185_1_.motionZ = 0.0D;
        }
        */
    }
}
