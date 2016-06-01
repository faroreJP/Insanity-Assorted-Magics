package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.tileentity.TileEntityForRender;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class BlockAlchemistTable extends BlockBase implements ITileEntityProvider{
    public static int[] X_SHIFT=new int[]{-1,1,0,0};
    public static int[] Z_SHIFT=new int[]{0,0,1,-1};
    private static boolean breakFlag=false;

    public BlockAlchemistTable() {
        super(Material.wood);
        setBlockTextureName("bookshelfTop");
        setStepSound(Block.soundTypeWood);
        setHardness(1.0f);
        setResistance(17.5f);
        setHarvestLevel("axe", 0);
        setBlockName("translator");
        setCreativeTab(FBS.tabAlchemy);
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

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }


    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null && AlchemyRegistry.CanAppraisal(itemStack)) {
            if (!par1World.isRemote) {
                ItemStack get = AlchemyRegistry.GetRandomAppraisal(itemStack);
                player.entityDropItem(get, player.getEyeHeight());
                player.triggerAchievement(AchievementRegistry.appraisal);

                if(!player.capabilities.isCreativeMode){
                    ItemStack con=itemStack.getItem().getContainerItem(itemStack);
                    if(con!=null) player.entityDropItem(con.copy(), player.getEyeHeight());

                    itemStack.stackSize--;
                    if (itemStack.stackSize <= 0) player.setCurrentItemOrArmor(0, null);
                }
                player.inventory.markDirty();
            }
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        if((p_149915_2_&8)==0) return new TileEntityForRender();
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int pSide=2;

        //プレイヤーの向きの決定
        switch (l){
            case 0: pSide=2; break;
            case 1: pSide=5; break;
            case 2: pSide=3; break;
            case 3: pSide=4; break;
        }

        world.setBlockMetadataWithNotify(x,y,z,pSide, 2);
        world.setBlock(x+X_SHIFT[pSide-2], y, z+Z_SHIFT[pSide-2], this, 8|pSide, 2);
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        int meta=par6;

        if(!breakFlag){
            breakFlag=true;

            if((meta&7)<2) meta=((meta&8)|2);

            if((meta&8)!=0){
                meta=(meta&7);
                if(par1World.getBlock(x-X_SHIFT[meta-2], y, z-Z_SHIFT[meta-2])==this) par1World.func_147480_a(x-X_SHIFT[meta-2], y, z-Z_SHIFT[meta-2], false);
            }
            else{
                if(par1World.getBlock(x+X_SHIFT[meta-2], y, z+Z_SHIFT[meta-2])==this) par1World.func_147480_a(x+X_SHIFT[meta-2], y, z+Z_SHIFT[meta-2], false);
            }

            breakFlag=false;
            return;
        }

        super.breakBlock(par1World, x,y,z, block, par6);
    }
}
