package jp.plusplus.fbs.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemMonocle;
import jp.plusplus.fbs.particle.EntityGlowFX;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import jp.plusplus.fbs.tileentity.TileEntityFillingTable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class BlockFillingTable extends BlockBase implements ITileEntityProvider {
    private IIcon iconFTop;
    private IIcon iconFBottom;

    public BlockFillingTable() {
        super(Material.rock);
        setBlockName("fillingTable");
        setBlockTextureName("fillingTable");
        setHardness(3.5F);
        setStepSound(soundTypePiston);
        setCreativeTab(FBS.tabBook);
        infoName="fillingTable";
        infoRow=3;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityFillingTable();
    }
    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        TileEntity e=par1World.getTileEntity(par2, par3, par4);
        if(!par1World.isRemote && e instanceof TileEntityFillingTable){
            par5EntityPlayer.openGui(FBS.instance, -1, par1World, par2, par3, par4);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntity te=world.getTileEntity(x,y,z);
        if (!(te instanceof TileEntityFillingTable) || ((TileEntityFillingTable)te).progress==0) return;

        EntityPlayer ep = FBS.proxy.getEntityPlayerInstance();
        if (ep == null) return;
        ItemStack helm = ep.getCurrentArmor(3);
        if (helm == null || !(helm.getItem() instanceof ItemMonocle)) return;

        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
        float f2 = (float) z + 0.5F;
        float f3 = 0.6F;
        float f4 = rand.nextFloat() * 0.6F - 0.3F;
        float f5 =rand.nextFloat()*0.75f;
        //float f4=0.6f;

        EffectRenderer er=FMLClientHandler.instance().getClient().effectRenderer;
        er.addEffect(new EntityGlowFX(world, (double) (f - f3), (double) (f1+f5), (double) (f2 + f4)));
        er.addEffect(new EntityGlowFX(world, (double) (f + f3), (double) (f1+f5), (double) (f2 + f4)));
        er.addEffect(new EntityGlowFX(world, (double) (f + f4), (double) (f1+f5), (double) (f2 - f3)));
        er.addEffect(new EntityGlowFX(world, (double) (f + f4), (double) (f1+f5), (double) (f2 + f3)));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        blockIcon = p_149651_1_.registerIcon(FBS.MODID+":fillingTableSide");
        iconFTop = p_149651_1_.registerIcon(FBS.MODID+":fillingTableTop");
        iconFBottom = p_149651_1_.registerIcon("furnace_top");
    }
    @Override
    public IIcon getIcon(int side, int meta){
        if(side==0) return iconFBottom;
        if(side==1) return iconFTop;
        return blockIcon;
    }

    @Override
    public int getRenderType(){
        return FBS.renderDirectionalId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
}
