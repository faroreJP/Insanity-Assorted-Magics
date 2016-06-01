package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.tileentity.TileEntityForRender;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;

/**
 * Created by plusplus_F on 2015/10/19.
 * 焚き火ブロック
 */
public class BlockBonfire extends BlockBase implements ITileEntityProvider {
    public BlockBonfire() {
        super(Material.rock);
        setBlockName("bonfire");
        setTickRandomly(true);
        setHardness(0.8f);
        setResistance(1.5f);
        setBlockTextureName("planks_oak");
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x,y,z)>0?15:0;
    }

    @Override
    public Block setBlockTextureName(String p_149658_1_) {
        this.textureName = p_149658_1_;
        return this;
    }

    @Override
    public int getRenderType(){
        return FBS.renderDecorationId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int tickRate(World w) {
        return 30;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta > 0) {
            //火がついている場合、GUIひらく
            player.openGui(FBS.instance, -1, world, x,y,z);
            return true;
        } else {
            //持ち物が火打石と打ち金かどうか判定
            ItemStack current = player.getCurrentEquippedItem();
            if (current == null || current.getItem() != Items.flint_and_steel) return false;

            //火をつける
            meta = 5 + world.rand.nextInt(4);
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);

            //damage flint
            if (!player.capabilities.isCreativeMode) current.damageItem(1, player);
            player.inventory.markDirty();
            if (!world.isRemote){
                world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                player.triggerAchievement(AchievementRegistry.bonfire);
            }

            return true;
        }
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rand) {
        if (!w.isRemote) {
            int meta = w.getBlockMetadata(x, y, z);
            if (meta == 0) return;

            //雨が降っているとはやく消える
            if(w.isRaining() && w.getBiomeGenForCoords(x,z).getIntRainfall()>0 && w.getHeightValue(x,z)==y){
                meta -= 2*(1+rand.nextInt(3));
            }
            else{
                meta -= 1 + rand.nextInt(3);
            }

            if (meta > 0) w.setBlockMetadataWithNotify(x, y, z, meta, 2);
            else{
                w.setBlockMetadataWithNotify(x, y, z, meta, 2);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
    }

    @Override
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_)) {
            p_149695_1_.func_147480_a(p_149695_2_, p_149695_3_, p_149695_4_, false);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if(!entity.isBurning() && world.getBlockMetadata(x,y,z)>0){
            entity.setFire(20*5);
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
        int meta=p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);
        if(meta==0) return;

        if (p_149734_5_.nextInt(24) == 0) {
            p_149734_1_.playSound((double) ((float) p_149734_2_ + 0.5F), (double) ((float) p_149734_3_ + 0.5F), (double) ((float) p_149734_4_ + 0.5F), "fire.fire", 1.0F + p_149734_5_.nextFloat(), p_149734_5_.nextFloat() * 0.7F + 0.3F, false);
        }

        int l;
        float f;
        float f1;
        float f2;

        if(p_149734_5_.nextInt(4)==0) return;

        if (!World.doesBlockHaveSolidTopSurface(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_) && !Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_, UP)) {
            if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_ - 1, p_149734_3_, p_149734_4_, EAST)) {
                for (l = 0; l < 2; ++l) {
                    f = (float) p_149734_2_ + p_149734_5_.nextFloat() * 0.1F;
                    f1 = (float) p_149734_3_ + p_149734_5_.nextFloat();
                    f2 = (float) p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_ + 1, p_149734_3_, p_149734_4_, WEST)) {
                for (l = 0; l < 2; ++l) {
                    f = (float) (p_149734_2_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    f1 = (float) p_149734_3_ + p_149734_5_.nextFloat();
                    f2 = (float) p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ - 1, SOUTH)) {
                for (l = 0; l < 2; ++l) {
                    f = (float) p_149734_2_ + p_149734_5_.nextFloat();
                    f1 = (float) p_149734_3_ + p_149734_5_.nextFloat();
                    f2 = (float) p_149734_4_ + p_149734_5_.nextFloat() * 0.1F;
                    p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ + 1, NORTH)) {
                for (l = 0; l < 2; ++l) {
                    f = (float) p_149734_2_ + p_149734_5_.nextFloat();
                    f1 = (float) p_149734_3_ + p_149734_5_.nextFloat();
                    f2 = (float) (p_149734_4_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.canCatchFire(p_149734_1_, p_149734_2_, p_149734_3_ + 1, p_149734_4_, DOWN)) {
                for (l = 0; l < 2; ++l) {
                    f = (float) p_149734_2_ + p_149734_5_.nextFloat();
                    f1 = (float) (p_149734_3_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    f2 = (float) p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for (l = 0; l < 3; ++l) {
                f = (float) p_149734_2_ + p_149734_5_.nextFloat();
                f1 = (float) p_149734_3_ + p_149734_5_.nextFloat() * 0.5F + 0.5F;
                f2 = (float) p_149734_4_ + p_149734_5_.nextFloat();
                p_149734_1_.spawnParticle("largesmoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        setBlockBounds(0,0,0,1,0.25f,1);
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x,y,z,x+1,y+0.25,z+1);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x,y,z,x+1,y+0.25,z+1);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityForRender();
    }
}
