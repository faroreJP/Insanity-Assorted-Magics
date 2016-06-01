package jp.plusplus.fbs.alchemy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.*;
import jp.plusplus.fbs.item.ItemBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/09.
 */
public class ItemAlchemyMaterial extends ItemBase implements IAlchemyMaterial {
    public static String[] NAMES={
            //0
            "water","waterUnclean","lava",

            //3
            "leafTree","branchTree","rootsTree","leafUnclean","branchUnclean","rootsUnclean",

            //9
            "grassAntidote","grassVine","rootsRakuyo","grassRakuyo","seedsRyusen","flowerRyusen","seedsPower","seedsDefence","fruitsWalnut",

            //18
            "herbBase","herbBlood","herbMana","herbStamina","herbSoul","herbMandrake","fruitsMandrake",

            //25
            "mushroomBlue","mushroomPoisonous","mushroomTiredness","mushroomNitrogen","mushroomTsukiyo",

            //30
            "flowerFox", "grassOtogiri", "grassEnmei", "flowerTsukimi", "grassGunpowder", "grassNightShade",

            //36
            "herbGold", "herbEnder", "herbExplosive", "herbUnclean", "tearSpirit",

            //41
            "mushroomParalysis", "mushroomSuperParalysis", "mushroomReishi", "mushroomGold", "mushroomChaos"
    };
    protected IIcon[] icons;

    public ItemAlchemyMaterial(){
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

    //------------------------------- IAlchemyMaterial -----------------------------------

    @Override
    public ArrayList<CharacteristicBase> addCharacteristics(ItemStack itemStack, Random rand) {
        int meta=itemStack.getItemDamage();
        ArrayList<CharacteristicBase> list=new ArrayList<CharacteristicBase>();
        CharacteristicBase cb;

        //--------------------------なんにでもつくやつ--------------------------------
        float r;
        //見た目
        if(meta==1 || meta==6 || meta==7 || meta==8 || meta==39){
            //不浄なやつ
            cb = new CharacteristicLook();
            cb.setValue(3);
            list.add(cb);
        }
        else {
            /*
            r = rand.nextFloat();
            if (r < 0.08f) {
                cb = new CharacteristicLook();
                cb.setValue(0);
                list.add(cb);
            } else if (r < 0.38f) {
                cb = new CharacteristicLook();
                cb.setValue(1);
                list.add(cb);
            } else if (r < 0.58f) {
                cb = new CharacteristicLook();
                cb.setValue(2);
                list.add(cb);
            }
            */
        }
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

        //--------------------------個別--------------------------------
        if(meta==40){
            cb = new CharacteristicSanity.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==1 || meta==6 || meta==7 || meta==8 || meta==37 || meta==39){
            cb = new CharacteristicSanity.Lose();
            cb.setValue(rand.nextFloat()<0.3f?1:0);
            list.add(cb);
        }
        if(meta==45){
            cb = new CharacteristicSanity.Lose();
            cb.setValue(2);
            list.add(cb);
        }

        if(meta==19 || meta==32 || meta==23 || meta==24){
            cb=new CharacteristicHealth.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==31 || meta==36 || meta==44){
            cb=new CharacteristicHealth.Gain();
            cb.setValue(1);
            list.add(cb);
        }
        if(meta==29){
            cb=new CharacteristicHealth.Lose();
            cb.setValue(0);
            list.add(cb);
        }

        if(FBS.cooperatesSS2){
            if(meta==0 || meta==1){
                cb=new CharacteristicWater.Gain();
                cb.setValue(0);
                list.add(cb);
            }
            if(meta==11 || meta==12 || meta==13 || meta==21 || meta==23 || meta==24){
                cb=new CharacteristicStamina.Gain();
                cb.setValue(0);
                list.add(cb);
            }
            if(meta==14){
                cb=new CharacteristicStamina.Gain();
                cb.setValue(1);
                list.add(cb);
            }
            if(meta==44){
                cb=new CharacteristicStamina.Gain();
                cb.setValue(2);
                list.add(cb);
            }
            if(meta==27){
                cb=new CharacteristicStamina.Lose();
                cb.setValue(0);
                list.add(cb);
            }
        }

        if(meta==36 || meta==37 || meta==43){
            cb=new CharacteristicExp.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==44){
            cb=new CharacteristicExp.Gain();
            cb.setValue(2);
            list.add(cb);
        }

        if(meta==26 || meta==29){
            cb=new CharacteristicPoison.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==30 || meta==45){
            cb=new CharacteristicPoison.Gain();
            cb.setValue(1);
            list.add(cb);
        }
        if(meta==9 || meta==11 || meta==12){
            cb=new CharacteristicPoison.Lose();
            cb.setValue(0);
            list.add(cb);
        }

        if(meta==28 || meta==38){
            cb=new CharacteristicPower.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==15){
            cb=new CharacteristicPower.Gain();
            cb.setValue(1);
            list.add(cb);
        }
        if(meta==27 || meta==41){
            cb=new CharacteristicPower.Lose();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==42){
            cb=new CharacteristicPower.Lose();
            cb.setValue(1);
            list.add(cb);
        }

        if(meta==13 || meta==14){
            cb=new CharacteristicCleverness.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==43){
            cb=new CharacteristicCleverness.Gain();
            cb.setValue(2);
            list.add(cb);
        }

        if(meta==35){
            cb=new CharacteristicSpeed.Gain();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==27){
            cb=new CharacteristicSpeed.Lose();
            cb.setValue(0);
            list.add(cb);
        }
        if(meta==41){
            cb=new CharacteristicSpeed.Lose();
            cb.setValue(1);
            list.add(cb);
        }
        if(meta==42){
            cb=new CharacteristicSpeed.Lose();
            cb.setValue(2);
            list.add(cb);
        }

        return list;
    }

}
