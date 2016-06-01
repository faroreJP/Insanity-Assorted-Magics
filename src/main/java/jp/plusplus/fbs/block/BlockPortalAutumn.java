package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.world.autumn.TeleporterAutumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/07.
 */
public class BlockPortalAutumn extends BlockBase {
    private IIcon iconSide;

    protected BlockPortalAutumn() {
        super(Material.wood);
        setBlockName("butterfly");
        setBlockTextureName("butterfly");
        setHardness(1.5f);
        setResistance(5.0f);
        setHarvestLevel("axe", 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        blockIcon = p_149651_1_.registerIcon(this.getTextureName()+"Top");
        iconSide = p_149651_1_.registerIcon(this.getTextureName()+"Side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return p_149691_1_==1?this.blockIcon:iconSide;
    }


    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entity, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null && entity.timeUntilPortal==0) {
            // 現在地が独自ディメンション以外
            if (world.provider.dimensionId != FBS.dimensionAutumnId) {
                if (entity instanceof EntityPlayerMP) {
                    // １行で書くと長過ぎるので一旦ローカル変数に格納
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entity;
                    ServerConfigurationManager serverConfigurationManager = entityPlayerMP.mcServer.getConfigurationManager();
                    WorldServer worldServer = entityPlayerMP.mcServer.worldServerForDimension(FBS.dimensionAutumnId);

                    // 移動後にネザーポータルが作成されるので即座に再送還されないように
                    entityPlayerMP.timeUntilPortal = 20;
                    entityPlayerMP.setInPortal();

                    // 独自ディメンションに移動する
                    serverConfigurationManager.transferPlayerToDimension(entityPlayerMP, FBS.dimensionAutumnId, new TeleporterAutumn(worldServer));
                    entityPlayerMP.triggerAchievement(AchievementRegistry.eternalAutumn);
                    return true;
                }
            }
            // 現在地が独自ディメンション
            else {
                if (entity instanceof EntityPlayerMP) {
                    // １行で書くと長過ぎるので一旦ローカル変数に格納
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entity;
                    ServerConfigurationManager serverConfigurationManager = entityPlayerMP.mcServer.getConfigurationManager();
                    WorldServer worldServer = entityPlayerMP.mcServer.worldServerForDimension(0);

                    entityPlayerMP.timeUntilPortal = 20;
                    entityPlayerMP.setInPortal();

                    // 独自ディメンションからはオーバーワールドに移動
                    serverConfigurationManager.transferPlayerToDimension(entityPlayerMP, 0, new TeleporterAutumn(worldServer));
                    return true;
                }
            }
        }
        return false;
    }
}
