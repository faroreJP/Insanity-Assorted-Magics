package jp.plusplus.fbs.world.crack;

import cpw.mods.fml.common.eventhandler.Event;
import jp.plusplus.fbs.world.crack.structure.MapGenSchool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class ChunkProviderCrack implements IChunkProvider {
    private Random endRNG;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    private World world;
    private double[] densities;
    /**
     * The biomes that are used to generate the chunk
     */
    private BiomeGenBase[] biomesForGeneration;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    int[][] field_73203_h = new int[32][32];

    private MapGenSchool mapGenSchool=new MapGenSchool();

    public ChunkProviderCrack(World p_i2007_1_, long p_i2007_2_) {
        this.world = p_i2007_1_;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);

        NoiseGenerator[] noiseGens = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5};
        noiseGens = TerrainGen.getModdedNoiseGenerators(p_i2007_1_, this.endRNG, noiseGens);
        this.noiseGen1 = (NoiseGeneratorOctaves) noiseGens[0];
        this.noiseGen2 = (NoiseGeneratorOctaves) noiseGens[1];
        this.noiseGen3 = (NoiseGeneratorOctaves) noiseGens[2];
        this.noiseGen4 = (NoiseGeneratorOctaves) noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctaves) noiseGens[4];
    }

    public void generateChunk(int chunkX, int chunkZ, Block[] blocks, BiomeGenBase[] biomes) {
        byte base = 2;
        int noiseWidth = base + 1;
        byte noiseHeight = 33;
        int noiseDepth = base + 1;
        this.densities = this.initializeNoiseField(this.densities, chunkX * base, 0, chunkZ * base, noiseWidth, noiseHeight, noiseDepth);

        for (int i1 = 0; i1 < base; ++i1) {
            for (int j1 = 0; j1 < base; ++j1) {
                for (int k1 = 0; k1 < 32; ++k1) {
                    double d0 = 0.25D;
                    double d1 = this.densities[((i1 + 0) * noiseDepth + j1 + 0) * noiseHeight + k1 + 0];
                    double d2 = this.densities[((i1 + 0) * noiseDepth + j1 + 1) * noiseHeight + k1 + 0];
                    double d3 = this.densities[((i1 + 1) * noiseDepth + j1 + 0) * noiseHeight + k1 + 0];
                    double d4 = this.densities[((i1 + 1) * noiseDepth + j1 + 1) * noiseHeight + k1 + 0];
                    double d5 = (this.densities[((i1 + 0) * noiseDepth + j1 + 0) * noiseHeight + k1 + 1] - d1) * d0;
                    double d6 = (this.densities[((i1 + 0) * noiseDepth + j1 + 1) * noiseHeight + k1 + 1] - d2) * d0;
                    double d7 = (this.densities[((i1 + 1) * noiseDepth + j1 + 0) * noiseHeight + k1 + 1] - d3) * d0;
                    double d8 = (this.densities[((i1 + 1) * noiseDepth + j1 + 1) * noiseHeight + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 4; ++l1) {
                        double scale = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * scale;
                        double d13 = (d4 - d2) * scale;

                        for (int i2 = 0; i2 < 8; ++i2) {
                            int index = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
                            short short1 = 128;
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int k2 = 0; k2 < 8; ++k2) {
                                Block block = Blocks.air;

                                if (d15 > 0.0D) {
                                    block = Blocks.dirt;
                                }

                                blocks[index] = block;
                                index += short1;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    @Deprecated // Supply metadata to the below function.
    public void func_147421_b(int p_147421_1_, int p_147421_2_, Block[] p_147421_3_, BiomeGenBase[] p_147421_4_) {
        replaceBiomeBlocks(p_147421_1_, p_147421_2_, p_147421_3_, p_147421_4_, new byte[p_147421_3_.length]);
    }

    public void replaceBiomeBlocks(int chunkX, int chunkZ, Block[] blocks, BiomeGenBase[] biomes, byte[] meta) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, blocks, meta, biomes, this.world);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) return;

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int dirtCount=0;
                int dirtToStone=5+endRNG.nextInt(3);
                for (int j1 = 127; j1 >= 0; --j1) {
                    int index = (z * 16 + x) * 128 + j1;
                    Block block2 = blocks[index];

                    if(block2==Blocks.dirt){
                        //地表を草ブロックにする
                        if(dirtCount==0) blocks[index]=Blocks.grass;

                        //地中は石にする
                        if(dirtCount>dirtToStone) blocks[index]=Blocks.stone;

                        dirtCount++;
                    }
                }
            }
        }

    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     * チャンクを生成する
     */
    public Chunk provideChunk(int chunkX, int chunkZ) {
        this.endRNG.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        Block[] ablock = new Block[32768];
        byte[] meta = new byte[ablock.length];
        this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);

        //チャンク内のブロックをまるごと決定する
        this.generateChunk(chunkX, chunkZ, ablock, this.biomesForGeneration);

        this.replaceBiomeBlocks(chunkX, chunkZ, ablock, this.biomesForGeneration, meta);
        Chunk chunk = new Chunk(this.world, ablock, meta, chunkX, chunkZ);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) this.biomesForGeneration[k].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double[] noise, int x, int y, int z, int width, int height, int depth) {
        ChunkProviderEvent.InitNoiseField event = new ChunkProviderEvent.InitNoiseField(this, noise, x, y, z, width, height, depth);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) return event.noisefield;

        if (noise == null) {
            noise = new double[width * height * depth];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, x, z, width, depth, 1.121D, 1.121D, 0.5D);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, x, z, width, depth, 200.0D, 200.0D, 0.5D);
        d0 *= 2.0D;
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, x, y, z, width, height, depth, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, x, y, z, width, height, depth, d0, d1, d0);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, x, y, z, width, height, depth, d0, d1, d0);
        int k1 = 0;
        int l1 = 0;

        for (int i2 = 0; i2 < width; ++i2) {
            for (int j2 = 0; j2 < depth; ++j2) {
                double d2 = (this.noiseData4[l1] + 256.0D) / 512.0D;

                if (d2 > 1.0D) {
                    d2 = 1.0D;
                }

                double d3 = this.noiseData5[l1] / 8000.0D;

                if (d3 < 0.0D) {
                    d3 = -d3 * 0.3D;
                }

                d3 = d3 * 3.0D - 2.0D;
                float f = (float) (i2 + x - 0) / 1.0F;
                float f1 = (float) (j2 + z - 0) / 1.0F;
                float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;

                if (f2 > 80.0F) {
                    f2 = 80.0F;
                }

                if (f2 < -100.0F) {
                    f2 = -100.0F;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d3 /= 8.0D;
                d3 = 0.0D;

                if (d2 < 0.0D) {
                    d2 = 0.0D;
                }

                d2 += 0.5D;
                d3 = d3 * (double) height / 16.0D;
                ++l1;
                double d4 = (double) height / 2.0D;

                for (int k2 = 0; k2 < height; ++k2) {
                    double d5 = 0.0D;
                    double d6 = ((double) k2 - d4) * 8.0D / d2;

                    if (d6 < 0.0D) {
                        d6 *= -1.0D;
                    }

                    double d7 = this.noiseData2[k1] / 512.0D;
                    double d8 = this.noiseData3[k1] / 512.0D;
                    double d9 = (this.noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D) {
                        d5 = d7;
                    } else if (d9 > 1.0D) {
                        d5 = d8;
                    } else {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= 8.0D;
                    d5 += (double) f2;
                    byte b0 = 2;
                    double d10;

                    if (k2 > height / 2 - b0) {
                        d10 = (double) ((float) (k2 - (height / 2 - b0)) / 64.0F);

                        if (d10 < 0.0D) {
                            d10 = 0.0D;
                        }

                        if (d10 > 1.0D) {
                            d10 = 1.0D;
                        }

                        d5 = d5 * (1.0D - d10) + -3000.0D * d10;
                    }

                    b0 = 8;

                    if (k2 < b0) {
                        d10 = (double) ((float) (b0 - k2) / ((float) b0 - 1.0F));
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    noise[k1] = d5;
                    ++k1;
                }
            }
        }

        return noise;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(p_73153_1_, world, world.rand, p_73153_2_, p_73153_3_, false));

        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(k + 16, l + 16);
        biomegenbase.decorate(this.world, this.world.rand, k, l);

        mapGenSchool.func_151539_a(this, world, p_73153_2_, p_73153_3_, null);
        mapGenSchool.generateStructuresInChunk(this.world, this.endRNG, p_73153_2_, p_73153_3_);

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, world, world.rand, p_73153_2_, p_73153_3_, false));

        BlockFalling.fallInstantly = false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks() {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave() {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString() {
        return "RandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }

    public static class Generator extends ChunkProviderGenerate{
        public Generator(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
            super(p_i2006_1_, p_i2006_2_, p_i2006_4_);
        }
    }
}
