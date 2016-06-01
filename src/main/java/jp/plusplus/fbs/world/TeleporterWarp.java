package jp.plusplus.fbs.world;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by plusplus_F on 2015/11/08.
 */
public class TeleporterWarp extends Teleporter {
    public TeleporterWarp(WorldServer p_i1963_1_) {
        super(p_i1963_1_);
    }

    @Override
    public void placeInPortal(Entity p_77185_1_, double p_77185_2_, double p_77185_4_, double p_77185_6_, float p_77185_8_) {
        int i = MathHelper.floor_double(p_77185_1_.posX);
        int j = MathHelper.floor_double(p_77185_1_.posY) - 1;
        int k = MathHelper.floor_double(p_77185_1_.posZ);
        p_77185_1_.setLocationAndAngles((double) i, (double) j, (double) k, p_77185_1_.rotationYaw, 0.0F);
        p_77185_1_.motionX = p_77185_1_.motionY = p_77185_1_.motionZ = 0.0D;
    }

    @Override
    public boolean placeInExistingPortal(Entity p_77184_1_, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_) {
        p_77184_1_.setLocationAndAngles(p_77184_1_.posX, p_77184_1_.posY, p_77184_1_.posZ, p_77184_1_.rotationYaw, p_77184_1_.rotationPitch);
        p_77184_1_.motionX = p_77184_1_.motionY = p_77184_1_.motionZ = 0.0D;
        return true;
    }

    @Override
    public boolean makePortal(Entity p_85188_1_) {
        return true;
    }

    @Override
    public void removeStalePortalLocations(long p_85189_1_){}

}
