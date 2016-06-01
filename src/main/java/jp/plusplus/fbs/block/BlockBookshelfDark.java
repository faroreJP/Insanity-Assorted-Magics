package jp.plusplus.fbs.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.item.ItemMonocle;
import jp.plusplus.fbs.particle.EntityGlowFX;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class BlockBookshelfDark extends BlockBase {
    IIcon iconSide;

    public BlockBookshelfDark() {
        super(Material.wood);
        setBlockName("bookshelf");
        setBlockTextureName("bookshelf");
        setStepSound(Block.soundTypeWood);
        setCreativeTab(FBS.tabBook);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) return false;

        ItemStack monocle=ItemMonocle.findMonocle(player);
        if(monocle==null) return false;

        /*
        ItemStack helm = player.getCurrentArmor(3);
        if (helm == null || !(helm.getItem() instanceof ItemMonocle)) return false;
        */

        if (world.isRemote) return true;

        if(!player.capabilities.isCreativeMode){
            ItemMonocle.damageMonocle(player, monocle);
            /*
            helm.damageItem(1, player);
            if (helm.stackSize <= 0) {
                player.setCurrentItemOrArmor(4, null);
            }
            */
        }

        FBSEntityProperties prop = FBSEntityProperties.get(player);
        if (prop == null) return true;

        ItemStack get = Registry.GetRandomBook(prop.getMagicLevel());
        if (get != null) {
            player.entityDropItem(get, player.getEyeHeight());
            meta--;
            world.setBlockMetadataWithNotify(x, y, z, meta, 3);
            player.inventory.markDirty();
        }
        return true;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int meta) {
        //world.setBlockMetadataWithNotify(x, y, z, 3, 3);
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        blockIcon = p_149651_1_.registerIcon(FBS.MODID + ":bookshelfTop");
        iconSide = p_149651_1_.registerIcon(FBS.MODID + ":bookshelfSide");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return (side == 1 || side == 0) ? blockIcon : iconSide;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (world.getBlockMetadata(x, y, z) == 0) return;

        EntityPlayer ep = FBS.proxy.getEntityPlayerInstance();
        if (ep == null) return;

        if(ItemMonocle.findMonocle(ep)==null) return;

        /*
        ItemStack helm = ep.getCurrentArmor(3);
        if (helm == null || !(helm.getItem() instanceof ItemMonocle)) return;
        */

        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
        float f2 = (float) z + 0.5F;
        float f3 = 0.6F;
        float f4 = rand.nextFloat() * 0.6F - 0.3F;
        float f5 =rand.nextFloat()*0.75f;
        //float f4=0.6f;

        spawnParticle(world, (double) (f - f3), (double) (f1+f5), (double) (f2 + f4));
        spawnParticle(world, (double) (f + f3), (double) (f1+f5), (double) (f2 + f4));
        spawnParticle(world, (double) (f + f4), (double) (f1+f5), (double) (f2 - f3));
        spawnParticle(world, (double) (f + f4), (double) (f1+f5), (double) (f2 + f3));
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_){
        return Items.book;
    }
    @Override
    public int quantityDropped(int meta, int fortune, Random random){
        return 3;
    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticle(World world, double x, double y, double z){
        EntityGlowFX e=new EntityGlowFX(world, x,y,z);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(e);
    }
}
