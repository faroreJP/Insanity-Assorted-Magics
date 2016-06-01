package jp.plusplus.fbs.alchemy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/23.
 */
public class ItemAlchemyPotion extends ItemFood implements IAlchemyProduct {
    public static final String[] NAMES={"potion", "ointment", "potionSpirit"};
    protected IIcon[] icons;

    public ItemAlchemyPotion() {
        super(0, 0, false);
        setCreativeTab(FBS.tabAlchemy);
        setAlwaysEdible();
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        --p_77654_1_.stackSize;

        //特性の効果発動
        ArrayList<CharacteristicBase> list=AlchemyRegistry.ReadCharacteristicFromNBT(p_77654_1_.getTagCompound());
        for(CharacteristicBase cb : list) cb.affectEntity(p_77654_2_, p_77654_3_);

        return p_77654_1_;
    }

    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.drink;
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int meta=p_77667_1_.getItemDamage();
        if(meta<0 ||meta>=NAMES.length) meta=0;

        return "item.fbs.alchemy." + NAMES[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i=0;i<NAMES.length;i++){
            ItemStack it=new ItemStack(item, 1, i);
            it.setTagCompound(new NBTTagCompound());
            ArrayList<CharacteristicBase> cbs=getDefaultCharacteristics(it, AlchemyRegistry.getRandom());
            AlchemyRegistry.WriteCharacteristicToNBT(it.getTagCompound(), cbs);

            list.add(it);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        icons=new IIcon[NAMES.length];
        for(int i=0;i<icons.length;i++) icons[i]=register.registerIcon(FBS.MODID+":"+NAMES[i]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        if(p_77617_1_<0 || p_77617_1_>=icons.length) p_77617_1_=0;
        return icons[p_77617_1_];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        AlchemyRegistry.AddCharacteristicsInfo(itemStack, list, p_77624_4_);
    }

    //----------------------------------------------------------------------------------

    @Override
    public boolean canInherit(ItemStack itemStack, CharacteristicBase cb) {
        int meta=itemStack.getItemDamage();

        if(meta==0 && (cb instanceof CharacteristicWater)){
            return true;
        }

        if(cb instanceof CharacteristicLook) return false;
        if(cb instanceof CharacteristicWeight) return false;

        return true;
    }

    @Override
    public int getMaxInheritAmount(ItemStack itemStack) {
        return itemStack.getItemDamage()==0?3:5;
    }

    @Override
    public ArrayList<CharacteristicBase> getDefaultCharacteristics(ItemStack itemStack, Random rand) {
        ArrayList<CharacteristicBase> ret=new ArrayList<CharacteristicBase>();
        int d=itemStack.getItemDamage();
        CharacteristicBase cb;

        if(FBS.cooperatesSS2 && d==0){
            //水分
            cb=new CharacteristicWater.Gain();
            cb.setValue(0);
            ret.add(cb);
        }
        if(d==2){
            if(FBS.cooperatesSS2){
                //水分
                cb=new CharacteristicWater.Gain();
                cb.setValue(0);
                ret.add(cb);
            }

            //SAN
            cb=new CharacteristicSanity.Lose();
            cb.setValue(1);
            ret.add(cb);
        }

        return ret;
    }
}
