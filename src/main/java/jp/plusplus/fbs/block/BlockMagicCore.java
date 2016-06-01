package jp.plusplus.fbs.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.item.ItemCharm;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.particle.EntityGlowFX;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by pluslus_F on 2015/06/17.
 */
public class BlockMagicCore extends BlockBase implements ITileEntityProvider{

    public BlockMagicCore() {
        super(Material.circuits);
        setBlockName("magicCore");
        setBlockTextureName("magicCore");
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        //BlockRedstoneWire
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return FBS.renderCharmId;
    }

    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
        return p_149742_1_.isBlockNormalCubeDefault(p_149742_2_, p_149742_3_ - 1, p_149742_4_, true);
    }

    @Override
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        if (!p_149695_1_.isRemote) {
            boolean flag = this.canPlaceBlockAt(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);

            if (!flag){
                this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }

            super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        if (hasTileEntity(par6)){
            world.removeTileEntity(x, y, z);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMagicCore();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        ItemStack current=player.getCurrentEquippedItem();
        if(current==null || current.getItem()!=Items.flint_and_steel) return false;

        //
        TileEntity e=world.getTileEntity(x, y, z);
        if(!(e instanceof TileEntityMagicCore)) return true;
        if(!((TileEntityMagicCore) e).getCircleName().equals("null")) return true;

        //check size
        int r=0;
        for(int i=1;i<5;i++){
            Block b=world.getBlock(x+i,y,z);
            if(b==BlockCore.charm) r=i;
        }
        if(r==0){
            return true;
        }

        //check magic circle
        String mc= Registry.FindMatchingMagicCircle(world, x, y, z, r);
        if(mc==null){
            return true;
        }

        //damage flint
        if(!player.capabilities.isCreativeMode) current.damageItem(1, player);
        player.inventory.markDirty();
        if(!world.isRemote){
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
            player.triggerAchievement(AchievementRegistry.circle);
        }

        ((TileEntityMagicCore) e).setMagicCircle(mc, r);
        ((TileEntityMagicCore) e).clearCharms();
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntity e=world.getTileEntity(x,y,z);
        if(!(e instanceof TileEntityMagicCore)) return;

        if (!((TileEntityMagicCore) e).getCircleName().equals("null")) {
            double cr=((TileEntityMagicCore) e).getCircleRadius()+0.5;
            double dx=x+0.5+cr*(rand.nextFloat()- rand.nextFloat());
            double dz=z+0.5+cr*(rand.nextFloat()- rand.nextFloat());

            EffectRenderer er= FMLClientHandler.instance().getClient().effectRenderer;
            er.addEffect(new EntityGlowFX(world, dx, y+0.0625f, dz));
        }
    }
}
