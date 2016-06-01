package jp.plusplus.fbs.alchemy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.*;
import jp.plusplus.fbs.item.ItemBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/19.
 * 中間素材だよ！中間素材！
 */
public class ItemAlchemyIntermediateMaterial extends ItemBase implements IAlchemyMaterial, IAlchemyProduct {
    public static String[] NAMES={
            //0
            "neutralizer", "gunpowder", "herbPowder", "herbDense", "potionActivate", "bladeSpice", "plantPowder", "mushroomPowder"
    };
    protected IIcon[] icons;

    public ItemAlchemyIntermediateMaterial(){
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(FBS.tabAlchemy);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i=0;i<NAMES.length;i++){
            ItemStack it=new ItemStack(item, 1, i);
            it.setTagCompound(new NBTTagCompound());
            ArrayList<CharacteristicBase> cbs=addCharacteristics(it, AlchemyRegistry.getRandom());
            AlchemyRegistry.WriteCharacteristicToNBT(it.getTagCompound(), cbs);

            list.add(it);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int meta=p_77667_1_.getItemDamage();
        if(meta<0 ||meta>=NAMES.length) meta=0;

        return "item.fbs.intermediate." + NAMES[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        icons=new IIcon[NAMES.length];

        for(int i=0;i<NAMES.length;i++){
            icons[i]=register.registerIcon(FBS.MODID+":"+NAMES[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return icons[p_77617_1_];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        AlchemyRegistry.AddCharacteristicsInfo(itemStack, list, p_77624_4_);
        if(itemStack.getItemDamage()!=5){
            AlchemyRegistry.AddMaterialInfo(itemStack, list, p_77624_4_);
        }
    }

    //-----------------------------------IAlchemyMaterial---------------------------------------------
    @Override
    public ArrayList<CharacteristicBase> addCharacteristics(ItemStack itemStack, Random rand) {
        ArrayList<CharacteristicBase> list=new ArrayList<CharacteristicBase>();
        return list;
    }

    //-----------------------------------IAlchemyProduct---------------------------------------------
    @Override
    public boolean canInherit(ItemStack itemStack, CharacteristicBase cb) {
        int meta=itemStack.getItemDamage();

        if(cb instanceof CharacteristicQuality){
            return true;
        }

        if(meta!=0 && meta!=4 && cb instanceof CharacteristicWeight){
            return true;
        }

        if((meta==2 || meta==3 || meta==6 || meta==7) &&
                (cb instanceof CharacteristicHealth || cb instanceof CharacteristicSanity || cb instanceof CharacteristicStamina
                || cb instanceof CharacteristicExp)){
            return true;
        }

        if((meta==5 || meta==6 || meta==8) &&
                (cb instanceof CharacteristicPoison || cb instanceof CharacteristicSpeed || cb instanceof CharacteristicHealth
                || cb instanceof CharacteristicConfusion || cb instanceof CharacteristicPower)){
            return true;
        }

        /*
        if(meta==3 && cb instanceof CharacteristicLook){
            return true;
        }
        */

        return false;
    }

    @Override
    public int getMaxInheritAmount(ItemStack itemStack) {
        int meta=itemStack.getItemDamage();
        if(meta==2 || meta==3) return 4;
        if(meta==5) return 3;
        if(meta==6 || meta==7) return 5;
        return 2;
    }

    @Override
    public ArrayList<CharacteristicBase> getDefaultCharacteristics(ItemStack itemStack, Random rand) {
        ArrayList<CharacteristicBase> ret=new ArrayList<CharacteristicBase>();
        return ret;
    }
}
