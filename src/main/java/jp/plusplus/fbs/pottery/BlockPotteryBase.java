package jp.plusplus.fbs.pottery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.block.BlockBase;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.pottery.usable.PotteryBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/26.
 *
 * INT型8byteのうち、
 * 0xVWXYZZ
 * V:hasEffect
 * W:state
 * X:grade
 * Y:size
 * Z:pattern
 *
 */
public abstract class BlockPotteryBase extends BlockBase implements IPottery,ITileEntityProvider{

    public static final String EFFECT_ID ="PotteryID";
    public String typeName;
    public int baseValue;

    public BlockPotteryBase(String t, int value) {
        super(Material.glass);
        typeName="pottery.fbs."+t;
        baseValue=value;

        setBlockName(t);
        setBlockTextureName("flower_pot");
        setCreativeTab(FBS.tabPottery);
    }

    @Override
    public int getRenderType(){
        return FBS.renderJarId;
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

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        ArrayList<ItemStack> meta=getAllPattern();
        for(ItemStack m : meta){
            list.add(m);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityPottery();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        TileEntity entity=world.getTileEntity(x,y,z);
        if(entity instanceof TileEntityPottery){
            TileEntityPottery te=(TileEntityPottery)entity;
            ItemStack item=new ItemStack(Item.getItemFromBlock(this), 1, te.getMetadata());
            NBTTagCompound nbt=new NBTTagCompound();
            te.writePotteryData(nbt);
            item.setTagCompound(nbt);
            ret.add(item);
        }

        return ret;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {

        TileEntity entity=world.getTileEntity(x,y,z);
        if(entity instanceof TileEntityPottery){
            TileEntityPottery te=(TileEntityPottery)entity;
            dropBlockAsItemWithChance(world, x, y, z, te.getMetadata(), 1.0f, 0);
            //ret.add(new ItemStack(Item.getItemFromBlock(this), 1, te.potteryMetadata));
        }

        //p_149749_1_.func_147480_a()
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        TileEntity entity=world.getTileEntity(x,y,z);
        if(entity instanceof TileEntityPottery){
            TileEntityPottery te=(TileEntityPottery)entity;
            ItemStack ret=new ItemStack(Item.getItemFromBlock(this), 1, te.getMetadata());
            te.writePotteryData(getNBT(ret));
            return ret;
        }
        return new ItemStack(Item.getItemFromBlock(this));
    }

    /**
     * Gradeによる価格補正倍率を得る
     * @param grade
     * @return
     */
    public float getRateWithGrade(PotteryGrade grade){
        float ext;
        switch (grade){
            case BAD: ext=0.75f; break;
            case GOOD: ext=1.5f; break;
            case GREAT: ext=4.0f; break;
            case SOULFUL: ext=12.0f; break;
            default: ext=1.f; break;
        }
        return ext;
    }

    /**
     * Sizeによる価格補正倍率を得る
     * @param size
     * @return
     */
    public float getRateWithSize(PotterySize size){
        float ext;
        switch (size){
            case SMALL: ext=0.5f; break;
            case LARGE: ext=2.0f; break;
            default: ext=1.f; break;
        }
        return ext;
    }

    public String getPrefixWithPattern(int pattern){
        String str;
        switch (pattern){
            case 1: str=StatCollector.translateToLocal("pottery.fbs.linear"); break;
            case 2: str=StatCollector.translateToLocal("pottery.fbs.mesh"); break;
            case 3: str=StatCollector.translateToLocal("pottery.fbs.flower"); break;
            default: str=StatCollector.translateToLocal("pottery.fbs.plain"); break;
        }
        return str;
    }

    //継承用
    public String getPotteryName(NBTTagCompound nbt){
        return StatCollector.translateToLocal(typeName);
    }

    public NBTTagCompound getNBT(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound();
    }

    //------------------------------- IPottery ----------------------------------------
    @Override
    public ItemStack getItemStack(PotteryState state, PotteryGrade grade, PotterySize size, byte pattern, boolean hasEffect){
        ItemStack dummy=new ItemStack(this);
        setState(dummy, state);
        setGrade(dummy, grade);
        setSize(dummy, size);
        setPattern(dummy, pattern);
        setEffect(dummy, hasEffect);
        dummy.setItemDamage(getMetadata(dummy.getTagCompound()));
        return dummy;
    }

    @Override
    public void setSize(ItemStack itemStack, PotterySize size){
        NBTTagCompound nbt=getNBT(itemStack);
        if(nbt.hasKey("size")) nbt.removeTag("size");
        nbt.setByte("size", size.getValue());
    }
    @Override
    public PotterySize getSize(NBTTagCompound nbt) {
        if(!nbt.hasKey("size")) return PotterySize.MEDIUM;
        return PotterySize.Get(nbt.getByte("size"));
    }

    @Override
    public void setGrade(ItemStack itemStack, PotteryGrade grade){
        NBTTagCompound nbt=getNBT(itemStack);
        if(nbt.hasKey("grade")) nbt.removeTag("grade");
        nbt.setByte("grade", grade.getValue());
    }
    @Override
    public void setGrade(ItemStack itemStack, Random rand){
        float r=rand.nextFloat();
        PotteryGrade g;

        if(r<=0.05f) g=PotteryGrade.SOULFUL;
        else if(r<=0.05f+0.10f) g=PotteryGrade.GREAT;
        else if(r<=0.15f+0.25f) g=PotteryGrade.GOOD;
        else if(r<=0.40f+0.15f) g=PotteryGrade.BAD;
        else g=PotteryGrade.NORMAL;

        setGrade(itemStack, g);
    }
    @Override
    public PotteryGrade getGrade(NBTTagCompound nbt){
        if(!nbt.hasKey("grade")) return PotteryGrade.NORMAL;
        return PotteryGrade.Get(nbt.getByte("grade"));
    }

    @Override
    public void setPattern(ItemStack itemStack, byte pattern){
        NBTTagCompound nbt=getNBT(itemStack);
        nbt.setByte("pattern", pattern);
    }
    @Override
    public byte getPattern(NBTTagCompound nbt){
        if(!nbt.hasKey("pattern")) return 0;
        return nbt.getByte("pattern");
    }

    @Override
    public void setState(ItemStack itemStack, PotteryState state){
        NBTTagCompound nbt=getNBT(itemStack);
        if(nbt.hasKey("state")) nbt.removeTag("state");
        nbt.setByte("state", state.getValue());
    }
    @Override
    public PotteryState getState(NBTTagCompound nbt){
        if(!nbt.hasKey("state")) return PotteryState.BAKED;
        return PotteryState.Get(nbt.getByte("state"));
    }

    @Override
    public void setEffect(ItemStack itemStack, boolean eff){
        NBTTagCompound nbt=getNBT(itemStack);
        if(nbt.hasKey("hasEffect")) nbt.removeTag("hasEffect");
        nbt.setBoolean("hasEffect", eff);
    }
    @Override
    public boolean hasEffect(NBTTagCompound nbt){
        if(!nbt.hasKey("hasEffect")) return false;
        return nbt.getBoolean("hasEffect");
    }

    @Override
    public abstract ResourceLocation getResourceLocation(int metadata);

    @Override
    public abstract ModelBase getModel(int metadata);

    @Override
    public Block getBlockType() {
        return this;
    }

    @Override
    public int getDrySec(NBTTagCompound nbt){
        PotterySize size=getSize(nbt);
        if(size==PotterySize.SMALL) return 5*60;
        if(size==PotterySize.LARGE) return 20*60;
        return 12*60;
    }

    @Override
    public int getMP(ItemStack itemStack) {
        NBTTagCompound nbt=getNBT(itemStack);
        if(getState(nbt)!=PotteryState.BAKED) return 0;
        return (int)(getRateWithGrade(getGrade(nbt))*getRateWithSize(getSize(nbt))*baseValue);
    }

    @Override
    public int getMetadata(NBTTagCompound nbt){
        int meta=(nbt.getByte("pattern")%0xf);
        meta=(meta|(nbt.getByte("state")<<6));
        meta=(meta|(nbt.getByte("size")<<4));
        meta=(meta|(nbt.getByte("grade")<<8));
        return meta;
    }

    @Override
    public String getLocalizedName(NBTTagCompound nbt) {
        PotteryState state=getState(nbt);
        String stString=PotteryState.GetLocalizedPrefix(state);
        String grade=PotteryGrade.GetLocalizedPrefix(getGrade(nbt));
        String size=PotterySize.GetLocalizedPrefix(getSize(nbt));
        String pattern=getPrefixWithPattern(getPattern(nbt));

        if(StatCollector.canTranslate("locale.fbs.jp")){
            if(state==PotteryState.BAKED) return stString+grade+pattern+size;
            return stString+pattern+size;
        }
        else{
            if(state==PotteryState.BAKED) return (grade.isEmpty()?"":grade+" ")+pattern+" "+(size.isEmpty()?"":size+" ");
            return stString+" "+pattern+" "+(size.isEmpty()?"":size+" ");
        }
    }

    @Override
    public ArrayList<ItemStack> getAllPattern() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        for (int g = 1; g < 2; g++) {
            for (int p = 0; p < 4; p++) {
                for (int s = 0; s < 3; s++) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setByte("state", PotteryState.BAKED.getValue());
                    nbt.setByte("grade", (byte) g);
                    nbt.setByte("size", (byte) s);
                    nbt.setByte("pattern", (byte) p);

                    ItemStack tmp = new ItemStack(this, 1, getMetadata(nbt));
                    tmp.setTagCompound(nbt);
                    ret.add(tmp);
                    //ret.add(((g<<12)|(s<<8)|p));
                }
            }
        }
        return ret;
    }

    @Override
    public float getCrashProbability(ItemStack itemStack){
        NBTTagCompound nbt=BlockCore.pot.getNBT(itemStack);

        //割れない壺は割れない
        if(hasEffect(nbt) && getState(nbt)==PotteryState.BAKED){
            PotteryBase pb=PotteryRegistry.getPotteryEffect(nbt.getInteger(EFFECT_ID));
            if(pb.getUnlocalizedName().equals("pottery.fbs.pot.unbreakable")) return 0;
        }

        //壺の大きさで割れやすさが変わる
        PotterySize size = getSize(nbt);
        if (size == PotterySize.SMALL) return 0.05f;
        if (size == PotterySize.LARGE) return 0.35f;
        return 0.15f;
    }
}
