package jp.plusplus.fbs.alchemy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class ItemEatableAlchemyMaterial extends ItemFood implements IAlchemyMaterial {
    public static String[] NAMES={
            "mushroomQli", "mushroomMatsu", "mushroomWarai", "mushroomNigaQli",

            //4
            "fruitsStamina", "fruitsItete", "fruitsSassa"
    };

    protected int[] heal={
            2, 6, 2, 2, 2, 2, 2
    };
    protected float[] sat={
            0.15f, 0.5f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f
    };

    protected IIcon[] icons;

    public ItemEatableAlchemyMaterial() {
        super(0, 0, false);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(FBS.tabAlchemy);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int meta=p_77667_1_.getItemDamage();
        if(meta<0 ||meta>=NAMES.length) meta=0;

        return "item.fbs.material." + NAMES[meta];
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
        if(p_77617_1_<0 || p_77617_1_>=icons.length) p_77617_1_=0;
        return icons[p_77617_1_];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        AlchemyRegistry.AddCharacteristicsInfo(itemStack, list, p_77624_4_);
        AlchemyRegistry.AddMaterialInfo(itemStack, list, p_77624_4_);
    }


    @Override
    public int func_150905_g(ItemStack p_150905_1_) {
        return heal[p_150905_1_.getItemDamage()];
    }

    @Override
    public float func_150906_h(ItemStack p_150906_1_) {
        return sat[p_150906_1_.getItemDamage()];
    }

    @Override
    protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_) {
        //特性の効果発動
        if (!p_77849_2_.isRemote) {
            ArrayList<CharacteristicBase> list = AlchemyRegistry.ReadCharacteristicFromNBT(p_77849_1_.getTagCompound());
            for (CharacteristicBase cb : list) cb.affectEntity(p_77849_2_, p_77849_3_);
        }
    }

    @Override
    public ArrayList<CharacteristicBase> addCharacteristics(ItemStack itemStack, Random rand) {
        int meta=itemStack.getItemDamage();
        ArrayList<CharacteristicBase> list=new ArrayList<CharacteristicBase>();
        CharacteristicBase cb;

        //--------------------------なんにでもつくやつ--------------------------------
        float r;
        //品質
        r = rand.nextFloat();
        if (r < 0.08f) {
            cb = new CharacteristicQuality();
            cb.setValue(0);
            list.add(cb);
        } else if (r < 0.38f) {
            cb = new CharacteristicQuality();
            cb.setValue(1);
            list.add(cb);
        } else if (r < 0.58f) {
            cb = new CharacteristicQuality();
            cb.setValue(2);
            list.add(cb);
        }

        if(FBS.cooperatesSS2){
            if(meta==1 || meta==4){
                cb = new CharacteristicStamina.Gain();
                cb.setValue(0);
                list.add(cb);
            }
        }

        if(meta==3){
            cb = new CharacteristicPoison.Gain();
            cb.setValue(0);
            list.add(cb);
        }

        if(meta==2){
            cb = new CharacteristicConfusion.Gain();
            cb.setValue(0);
            list.add(cb);
        }

        if(meta==5){
            cb = new CharacteristicHealth.Lose();
            cb.setValue(0);
            list.add(cb);
        }

        if(meta==6){
            cb = new CharacteristicSpeed.Gain();
            cb.setValue(0);
            list.add(cb);
        }

        return list;
    }
}
