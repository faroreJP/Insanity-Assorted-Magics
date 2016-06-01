package jp.plusplus.fbs.world.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.entity.EntityButterfly;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/20.
 * 「魔力の秋」バイオーム
 */
public class BiomeAutumn extends BiomeGenBase {
    private int biomeType;
    protected final WorldGenDirtyOak genOak = new WorldGenDirtyOak(false, false);
    protected final WorldGenDirtyOak genOakBig = new WorldGenDirtyOak(false, true);
    protected final WorldGenDirtyOak genBirch = new WorldGenDirtyBirch(false);

    public BiomeAutumn(int id) {
        super(id);
        this.biomeType = 0;
        this.theBiomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 4;
        setBiomeName("Magical Autumn");

        //表面のブロックは落ち葉
        this.topBlock= BlockCore.fallenLeaves;
        this.field_150604_aj=0;

        //地中は土
        this.fillerBlock=Blocks.dirt;
        this.field_76754_C=0;

        //知らん
        this.func_76733_a(5159473);
        this.setTemperatureRainfall(0.7F, 0.8F);

        //狼と蝶がスポーン
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        //this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityButterfly.class, 8, 4, 4));
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
        int r=p_150567_1_.nextInt(100);

        if(r<10) return genOakBig;
        else if(r<10+15) return genBirch;
        return genOak;
    }

    @Override
    public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_) {
        int k;
        int l;
        int i1;
        int j1;
        int k1;

        k = p_76728_2_.nextInt(5) - 3;

        l = 0;

        while (l < k) {
            i1 = p_76728_2_.nextInt(3);

            if (i1 == 0) {
                genTallFlowers.func_150548_a(1);
            } else if (i1 == 1) {
                genTallFlowers.func_150548_a(4);
            } else if (i1 == 2) {
                genTallFlowers.func_150548_a(5);
            }

            j1 = 0;

            while (true) {
                if (j1 < 5) {
                    k1 = p_76728_3_ + p_76728_2_.nextInt(16) + 8;
                    int i2 = p_76728_4_ + p_76728_2_.nextInt(16) + 8;
                    int l1 = p_76728_2_.nextInt(p_76728_1_.getHeightValue(k1, i2) + 32);

                    if (!genTallFlowers.generate(p_76728_1_, p_76728_2_, k1, l1, i2)) {
                        ++j1;
                        continue;
                    }
                }

                ++l;
                break;
            }
        }

        super.decorate(p_76728_1_, p_76728_2_, p_76728_3_, p_76728_4_);
    }

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
        int l = super.getBiomeGrassColor(p_150558_1_, p_150558_2_, p_150558_3_);
        return (l & 0x00f000)+0xff0f00;
    }
}
