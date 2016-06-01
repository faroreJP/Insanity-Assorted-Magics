package jp.plusplus.fbs.pottery;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.pottery.model.ModelPotLarge;
import jp.plusplus.fbs.pottery.model.ModelPotMedium;
import jp.plusplus.fbs.pottery.model.ModelPotSmall;
import jp.plusplus.fbs.pottery.usable.PotteryBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/26.
 */
public class BlockPot extends BlockPotteryBase {
    public static final ResourceLocation rlSmall =new ResourceLocation(FBS.MODID+":textures/models/PotSmall.png");
    public static final ResourceLocation rlMedium =new ResourceLocation(FBS.MODID+":textures/models/PotMedium.png");
    public static final ResourceLocation rlMedium1 =new ResourceLocation(FBS.MODID+":textures/models/PotMedium1.png");
    public static final ResourceLocation rlLarge[]=new ResourceLocation[]{
            new ResourceLocation(FBS.MODID+":textures/models/PotLarge0.png"),
            new ResourceLocation(FBS.MODID+":textures/models/PotLarge1.png"),
            new ResourceLocation(FBS.MODID+":textures/models/PotLarge2.png"),
            new ResourceLocation(FBS.MODID+":textures/models/PotLarge3.png")
    };
    public static final ModelPotSmall[] mpSmall=new ModelPotSmall[]{
            new ModelPotSmall(0), new ModelPotSmall(6), new ModelPotSmall(12), new ModelPotSmall(18)
    };
    public static final ModelPotMedium[] mpMedium=new ModelPotMedium[]{
            new ModelPotMedium(0), new ModelPotMedium(9), new ModelPotMedium(18)
    };
    public static final ModelPotLarge mpLarge=new ModelPotLarge();

    public static final String[] EFFECTS={
            "pottery.fbs.pot.keep",
            "pottery.fbs.pot.senaka",
            "pottery.fbs.pot.void",
            "pottery.fbs.pot.change",
            "pottery.fbs.pot.monster",
            "pottery.fbs.pot.lottery",
            "pottery.fbs.pot.taboo",
            "pottery.fbs.pot.unbreakable",
            "pottery.fbs.pot.enchant",
            "pottery.fbs.pot.taboo",
            "pottery.fbs.pot.furnace",
            "pottery.fbs.pot.appraisal"
    };

    public BlockPot(int value) {
        super("pot", value);
        textureName="flower_pot";
    }

    @Override
    public ResourceLocation getResourceLocation(int metadata) {
        int pat=metadata&0xf;
        PotterySize size=PotterySize.Get((metadata>>4)&3);

        if(pat<0) pat=0;

        if(size==PotterySize.SMALL) return rlSmall;
        if(size==PotterySize.MEDIUM) return pat>=3?rlMedium1:rlMedium;
        return rlLarge[pat%rlLarge.length];
    }

    @Override
    public ModelBase getModel(int metadata) {
        int pat=metadata&0xf;
        PotterySize size=PotterySize.Get((metadata >> 4) & 3);

        if(pat<0) pat=0;

        if(size==PotterySize.SMALL) return mpSmall[pat%mpSmall.length];
        if(size==PotterySize.MEDIUM) return mpMedium[pat%mpMedium.length];
        return mpLarge;
    }

    @Override
    public ItemStack getEnchantedItemStack(ItemStack itemStack, Random rand){
        ItemBlockPottery ipub=(ItemBlockPottery) Item.getItemFromBlock(this);

        ItemStack ret=new ItemStack(ipub, itemStack.stackSize, itemStack.getItemDamage());
        ret.setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());

        setEffect(ret, true);
        int id=PotteryRegistry.getPotteryEffectId(EFFECTS[rand.nextInt(EFFECTS.length)]);
        ret.getTagCompound().setInteger(EFFECT_ID, id);
        PotteryBase pb=PotteryRegistry.getPotteryEffect(id);
        pb.onBaked(ret);

        return ret;
    }

    @Override
    public ArrayList<ItemStack> getAllPattern() {
        ArrayList<ItemStack> list = super.getAllPattern();

        for (int i = 0; i < EFFECTS.length; i++) {
            for (int k = 0; k < 3; k++) {
                int id=PotteryRegistry.getPotteryEffectId(EFFECTS[i]);

                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("state", IPottery.PotteryState.BAKED.getValue());
                nbt.setByte("grade", (byte) 1);
                nbt.setByte("size", (byte) k);
                nbt.setByte("pattern", (byte) 0);
                nbt.setInteger(EFFECT_ID, id);

                ItemStack tmp = new ItemStack(this, 1, getMetadata(nbt));
                tmp.setTagCompound(nbt);
                setEffect(tmp, true);

                PotteryRegistry.getPotteryEffect(id).onBaked(tmp);

                list.add(tmp);
            }
        }

        return list;
    }

}
