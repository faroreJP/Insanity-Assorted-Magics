package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.tileentity.TileEntityPortalWarp;
import jp.plusplus.fbs.world.TeleporterWarp;
import jp.plusplus.fbs.world.crack.TeleporterToCrack;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/23.
 * meta: 8...向き, 4...狭間生成フラグ, 1...上下
 */
public class BlockPortalWarp extends BlockBase implements ITileEntityProvider {
    protected IIcon[] icons;

    public BlockPortalWarp() {
        super(Material.portal);
        this.setTickRandomly(true);
        setCreativeTab(null);
        setBlockName("portalWarp");
        setHardness(0.5f);
        setResistance(1.f);
        setStepSound(Block.soundTypeGlass);
    }

    @Override
    public int tickRate(World w) {
        return 30;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        icons = new IIcon[2];
        icons[0] = p_149651_1_.registerIcon(FBS.MODID + ":portalWarpUp");
        icons[1] = p_149651_1_.registerIcon(FBS.MODID + ":portalWarpDown");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return (p_149691_2_ & 1) == 0 ? icons[1] : icons[0];
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rand) {
        if (!w.isRemote) {
            int meta = w.getBlockMetadata(x, y, z);
            if ((meta & 1) != 0 || rand.nextInt(10) != 0) return;
            w.func_147480_a(x, y, z, false);
        }
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemById(0);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
        //return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
        int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 8;
        if(l==0) setBlockBounds(0,0,0.5f,1,1,0.5f);
        else setBlockBounds(0.5f,0,0,0.5f,1,1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_) {
        return super.getCollisionBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
    }

    @Override
    public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity e) {
        //FBS.logger.info("collided:"+(e.getClass().toString()));
        if (e instanceof EntityPlayerMP && !w.isRemote) {
            int meta=w.getBlockMetadata(x,y,z);
            if((meta&4)!=0){
                //狭間を生成して移動
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP) e;
                ServerConfigurationManager serverConfigurationManager = entityPlayerMP.mcServer.getConfigurationManager();
                WorldServer worldServer = entityPlayerMP.mcServer.worldServerForDimension(FBS.dimensionCrackId);

                //基準座標を得る
                ChunkCoordinates cc=WorldProvider.getProviderForDimension(FBS.dimensionCrackId).getEntrancePortalLocation();

                //基準座標からテキトーに回ると多分地面があるはず
                boolean found=false;
                for(int r=1;r<30 && !found;r++){
                    for(int aa=0;aa<8;aa++){
                        float angle=(float)Math.PI/8.f*aa;
                        int bx=MathHelper.floor_float(cc.posX+5*r*MathHelper.cos(angle));
                        int bz=MathHelper.floor_float(cc.posZ+5*r*MathHelper.sin(angle));

                        int cx=bx%16;
                        int cz=bz%16;
                        if(cx<0) cx+=16;
                        if(cz<0) cz+=16;

                        Chunk c=worldServer.getChunkProvider().provideChunk(bx%16, bz%16);
                        int h=c.getHeightValue(cx, cz);
                        if(h>0){
                            cc.posX=bx;
                            cc.posY=h+10;
                            cc.posZ=bz;
                            found=true;
                            break;
                        }
                    }
                }


                entityPlayerMP.setPositionAndUpdate(cc.posX + 0.5, cc.posY, cc.posZ + 0.5);
                serverConfigurationManager.transferPlayerToDimension(entityPlayerMP, FBS.dimensionCrackId, new TeleporterWarp(worldServer));
            }
            else{
                //通常のワープ処理
                EntityPlayerMP playerMP = (EntityPlayerMP) e;
                TileEntity te = w.getTileEntity(x, y, z);
                if (!(te instanceof TileEntityPortalWarp)) return;
                FBSEntityProperties.WarpPosition dest = ((TileEntityPortalWarp) te).destination;
                if (dest == null) return;

                playerMP.setPositionAndUpdate(dest.x + 0.5, dest.y+3, dest.z + 0.5);
                if (playerMP.dimension != dest.dimId) {
                    //playerMP.travelToDimension(dest.dimId);
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) e;
                    ServerConfigurationManager serverConfigurationManager = entityPlayerMP.mcServer.getConfigurationManager();
                    WorldServer worldServer = entityPlayerMP.mcServer.worldServerForDimension(dest.dimId);

                    serverConfigurationManager.transferPlayerToDimension(entityPlayerMP, dest.dimId, new TeleporterWarp(worldServer));
                }
            }
        }
    }

    @Override
    public int getRenderType() {
        return FBS.renderPortalWarpId;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World w, int x, int y, int z, Block block) {
        int meta = w.getBlockMetadata(x, y, z);
        Block b;
        if ((meta & 1) == 0) b = w.getBlock(x, y + 1, z);
        else b = w.getBlock(x, y - 1, z);

        if (b != this) {
            w.func_147480_a(x, y, z, false);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityPortalWarp();
    }
}
