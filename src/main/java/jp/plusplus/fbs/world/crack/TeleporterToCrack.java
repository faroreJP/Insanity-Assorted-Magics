package jp.plusplus.fbs.world.crack;

import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class TeleporterToCrack extends Teleporter {
    private final WorldServer worldServerInstance;
    private Random random=new Random();

    public TeleporterToCrack(WorldServer p_i1963_1_) {
        super(p_i1963_1_);
        worldServerInstance=p_i1963_1_;
    }

    // 近くにポータルがあったらそこに出現させる処理
    // このサンプルではネザーポータルの処理をそのまま利用する
    @Override
    public boolean placeInExistingPortal(Entity p_77184_1_, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
        return super.placeInExistingPortal(p_77184_1_, p_77184_2_, p_77184_4_, p_77184_6_, p_77184_8_);
    }

    // ポータルを作成する処理
    @Override
    public boolean makePortal(Entity p_85188_1_) {
        byte b0 = 16;
        double distance = -1.0D;
        int i = MathHelper.floor_double(p_85188_1_.posX);
        int j = MathHelper.floor_double(p_85188_1_.posY);
        int k = MathHelper.floor_double(p_85188_1_.posZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.random.nextInt(4);
        int x;
        double posX;
        int z;
        double posZ;
        int y;
        int j3;
        int k3;
        int l3;
        int addX;
        int addZ;
        int addY;
        int blockX;
        int blockY;
        double tmpY;
        double tmpDistance;

        for (x = i - b0; x <= i + b0; ++x) {
            posX = (double) x + 0.5D - p_85188_1_.posX;

            for (z = k - b0; z <= k + b0; ++z) {
                posZ = (double) z + 0.5D - p_85188_1_.posZ;
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

                            for (addX = 0; addX < 5; ++addX) {
                                for (addZ = 0; addZ < 5; ++addZ) {
                                    for (addY = -1; addY < 4; ++addY) {
                                        blockX = x + (addZ - 1) * k3 + addX * l3;
                                        blockY = y + addY;
                                        int blockZ = z + (addZ - 1) * l3 - addX * k3;

                                        if (addY < 0 && !this.worldServerInstance.getBlock(blockX, blockY, blockZ).getMaterial().isSolid() || addY >= 0 && !this.worldServerInstance.isAirBlock(blockX, blockY, blockZ)) {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            tmpY = (double) y + 0.5D - p_85188_1_.posY;
                            tmpDistance = posX * posX + tmpY * tmpY + posZ * posZ;

                            if (distance < 0.0D || tmpDistance < distance) {
                                distance = tmpDistance;
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

        if (distance < 0.0D) {
            for (x = i - b0; x <= i + b0; ++x) {
                posX = (double) x + 0.5D - p_85188_1_.posX;

                for (z = k - b0; z <= k + b0; ++z) {
                    posZ = (double) z + 0.5D - p_85188_1_.posZ;
                    label222:

                    for (y = this.worldServerInstance.getActualHeight() - 1; y >= 0; --y) {
                        if (this.worldServerInstance.isAirBlock(x, y, z)) {
                            while (y > 0 && this.worldServerInstance.isAirBlock(x, y - 1, z)) {
                                --y;
                            }

                            for (j3 = l1; j3 < l1 + 2; ++j3) {
                                k3 = j3 % 2;
                                l3 = 1 - k3;

                                for (addX = 0; addX < 5; ++addX) {
                                    for (addZ = -1; addZ < 5; ++addZ) {
                                        addY = x + (addX - 1) * k3;
                                        blockX = y + addZ;
                                        blockY = z + (addX - 1) * l3;

                                        if (addZ < 0 && !this.worldServerInstance.getBlock(addY, blockX, blockY).getMaterial().isSolid() || addZ >= 0 && !this.worldServerInstance.isAirBlock(addY, blockX, blockY)) {
                                            continue label222;
                                        }
                                    }
                                }

                                tmpY = (double) y + 0.5D - p_85188_1_.posY;
                                tmpDistance = posX * posX + tmpY * tmpY + posZ * posZ;

                                if (distance < 0.0D || tmpDistance < distance) {
                                    distance = tmpDistance;
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

        //ポータルの生成処理
        if (distance < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > this.worldServerInstance.getActualHeight() - 10) {
                i1 = this.worldServerInstance.getActualHeight() - 10;
            }

            j2 = i1;

            for (y = -2; y <= 2; ++y) {
                for (j3 = -2; j3 < 2; ++j3) {
                    for (k3 = -2; k3 < 3; ++k3) {
                        l3 = k5 + y;
                        addX = j2 + k3;
                        addZ = z + j3;
                        flag = k3 < 0;
                        this.worldServerInstance.setBlock(l3, addX, addZ, flag ? Blocks.obsidian : Blocks.air);
                    }
                }
            }
        }

        this.worldServerInstance.setBlock(k5, j2, z, BlockCore.magicCore);
        TileEntity te=this.worldServerInstance.getTileEntity(k5, j2, z);
        if(te instanceof TileEntityMagicCore){
            ((TileEntityMagicCore) te).setMagicCircle("fbs.warp", 2);
        }

        /*
        for (y = 0; y < 4; ++y) {
            for (j3 = 0; j3 < 4; ++j3) {
                for (k3 = -1; k3 < 4; ++k3) {
                    l3 = k5 + (j3 - 1) * l5;
                    addX = j2 + k3;
                    addZ = z + (j3 - 1) * l2;
                    flag = j3 == 0 || j3 == 3 || k3 == -1 || k3 == 3;
                    this.worldServerInstance.setBlock(l3, addX, addZ, (Block) (flag ? Blocks.obsidian : Blocks.portal), 0, 2);
                }
            }

            for (j3 = 0; j3 < 4; ++j3) {
                for (k3 = -1; k3 < 4; ++k3) {
                    l3 = k5 + (j3 - 1) * l5;
                    addX = j2 + k3;
                    addZ = z + (j3 - 1) * l2;
                    this.worldServerInstance.notifyBlocksOfNeighborChange(l3, addX, addZ, this.worldServerInstance.getBlock(l3, addX, addZ));
                }
            }
        }
        */

        return true;
    }

    // プレイヤーをポータルに出現させる処理
    // このサンプルではネザーポータルの処理をそのまま利用する
    @Override
    public void placeInPortal(Entity p_77185_1_, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {
        super.placeInPortal(p_77185_1_, p_77185_2_, p_77185_4_, p_77185_6_, p_77185_8_);
    }
}