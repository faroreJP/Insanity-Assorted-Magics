package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.ItemAlchemyMaterial;
import jp.plusplus.fbs.api.IHarvestable;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenFlowers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/09.
 */
public class BlockHerb extends BlockBase implements ITileEntityProvider,IHarvestable,IMeta {
    public BlockHerb() {
        super(Material.grass);
        setBlockName("herb");
        setBlockTextureName("herb");
        setHardness(0.5f);
        setResistance(0.1f);
        setCreativeTab(FBS.tabAlchemy);
        setStepSound(soundTypeGrass);
    }

    public TileEntityHavestable getTE(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityHavestable ? (TileEntityHavestable) te : null;
    }

    //---------------------------------------------------------------------------------------

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        for (int i = 0; i < 9; i++) p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (canHarvest(world, x, y, z)) {
            ArrayList<ItemStack> get = harvest(world, x, y, z, world.rand, player);
            if(!world.isRemote){
                for (ItemStack is : get) {
                    player.entityDropItem(is, player.getEyeHeight());
                }
            }
            player.inventory.markDirty();
        }
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if (y == 0) return false;
        Block b = world.getBlock(x, y, z);
        if (b != this && !b.isReplaceable(world, x, y, z)) return false;
        b = world.getBlock(x, y - 1, z);

        return b == Blocks.stone || b == Blocks.dirt || b == Blocks.grass || b == BlockCore.fallenLeaves || b == Blocks.mossy_cobblestone || b == Blocks.cobblestone || b==Blocks.end_stone || b==Blocks.netherrack;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
        if (!canPlaceBlockAt(world, x, y, z)) {
            world.func_147480_a(x, y, z, false);
            world.notifyBlockOfNeighborChange(x, y, z, this);
        }
    }

    @Override
    public int getRenderType() {
        return FBS.renderHerbId;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
    }

    //---------------------------------------------------------------------------------------
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityHavestable(60, p_149915_2_);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public boolean canHarvest(World world, int x, int y, int z) {
        return getTE(world, x, y, z).canHarvest();
    }

    @Override
    public void glow(World world, int x, int y, int z, Random rand) {
        getTE(world, x, y, z).glow(rand);
    }

    @Override
    public ArrayList<ItemStack> getHarvestItems(World world, int x, int y, int z) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        int meta = world.getBlockMetadata(x, y, z);

        if (meta < 6) {
            ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 18 + meta));
        } else if (meta < 9) {
            ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 36 + meta - 6));
        }
        if(meta==5) ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 24));
        ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 39));

        return ret;
    }

    @Override
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand, EntityPlayer player) {
        return harvest(world, x, y, z, rand);
    }

    @Override
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ArrayList<ItemStack> list = getHarvestItems(world, x, y, z);
        int num = 1 + rand.nextInt(2);

        for (int i = 0; i < num; i++) {
            boolean flag = false;

            //不浄なハーブとかの判定
            if (list.size()>1 && (rand.nextInt(32) == 0 || (world.getBiomeGenForCoords(x, z) == Registry.biomeAutumn))) {
                if (rand.nextInt(32) == 0) {
                    flag = true;
                } else {
                    BiomeGenBase bgb = world.getBiomeGenForCoords(x, z);
                    if (bgb == Registry.biomeAutumn || bgb == Registry.biomeCrack) {
                        flag = rand.nextInt(8) == 0;
                    }
                }
            }
            if (flag) ret.add(list.get(list.size()-1).copy());
            else ret.add(list.get(rand.nextInt(list.size()-1)).copy());
        }

        getTE(world, x, y, z).onHarvest();
        return ret;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        String name = "tile.fbs.";
        if (meta < 6) {
            name += ItemAlchemyMaterial.NAMES[18 + meta];
        } else if (meta < 9) {
            name += ItemAlchemyMaterial.NAMES[36 + meta - 6];
        }
        return name;
    }
}
