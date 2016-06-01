package jp.plusplus.fbs.block;

import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class BlockFBSWood extends BlockBase {
    public BlockFBSWood() {
        super(Material.wood);
        setBlockName("plank");
        setBlockTextureName("bookshelfTop");
        setStepSound(Block.soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
    }
}
