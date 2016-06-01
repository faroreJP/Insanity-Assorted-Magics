package jp.plusplus.fbs.world.crack.biome;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class BiomeGenCrack extends BiomeGenBase {
    public BiomeGenCrack(int p_i1990_1_) {
        super(p_i1990_1_, true);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 10, 4, 4));
        this.topBlock = Blocks.dirt;
        this.fillerBlock = Blocks.dirt;
        this.theBiomeDecorator = new BiomeCrackDecorator();
        this.theBiomeDecorator.treesPerChunk = 2;
        this.theBiomeDecorator.grassPerChunk = 3;
        this.theBiomeDecorator.flowersPerChunk = 2;
        this.setTemperatureRainfall(0.7F, 0.F);
        setBiomeName("Dimension Crack");
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float p_76731_1_) {
        //return 0xfa000000;
        return 0xffffffff;
    }

    @Override
    public WorldGenAbstractTree func_150567_a(Random p_150567_1_) {
        return (WorldGenAbstractTree) this.worldGeneratorTrees;
    }

    public static class BiomeCrackDecorator extends BiomeDecorator {
        public BiomeCrackDecorator() {
        }

        protected void genDecorations(BiomeGenBase p_150513_1_) {
            super.genDecorations(p_150513_1_);
        }
    }
}