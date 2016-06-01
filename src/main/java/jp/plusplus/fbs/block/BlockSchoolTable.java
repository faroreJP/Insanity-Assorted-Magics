package jp.plusplus.fbs.block;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.tileentity.TileEntityForRender;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/25.
 */
public class BlockSchoolTable extends BlockBase implements ITileEntityProvider {
    public BlockSchoolTable() {
        super(Material.wood);
        setBlockName("schoolTable");
        setBlockTextureName("bookshelfTop");
        setHardness(1.0f);
        setResistance(15.f);
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
        return FBS.renderDecorationId;
    }


    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityForRender();
    }
}
