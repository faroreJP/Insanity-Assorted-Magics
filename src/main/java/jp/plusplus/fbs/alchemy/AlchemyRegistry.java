package jp.plusplus.fbs.alchemy;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.*;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * Created by plusplus_F on 2015/09/08.
 * たぶん錬金術はわりと規模がでかくなると思うんで分離
 */
public class AlchemyRegistry {
    //特性
    private ArrayList<Class<? extends CharacteristicBase>> characteristics=new ArrayList<Class<? extends CharacteristicBase>>();
    private HashMap<Class<? extends CharacteristicBase>, Integer> characteristicsIdMap=new HashMap<Class<? extends CharacteristicBase>, Integer>();
    //素材
    private HashMap<String, ArrayList<ItemStack>> materials=new HashMap<String, ArrayList<ItemStack>>();
    private HashMap<ItemStack, ArrayList<String>> materialNames=new HashMap<ItemStack, ArrayList<String>>();
    //鑑定
    private HashMap<AppraisalItemStack, ArrayList<WeightedItemStack>> appraisalItems=new HashMap<AppraisalItemStack, ArrayList<WeightedItemStack>>();
    //調合
    private ArrayList<Recipe> recipes=new ArrayList<Recipe>();
    private ArrayList<ProductExpPair> productExpPairs=new ArrayList<ProductExpPair>();
    //カゴ
    private ArrayList<ItemStack> basketItems=new ArrayList<ItemStack>();

    private static AlchemyRegistry instance=new AlchemyRegistry();
    private static Random rand=new Random();

    private AlchemyRegistry(){}

    public static Random getRandom(){ return instance.rand; }


    public static void RegisterAlchemy(){
        //-------------------------------------------------------------------------------------------------------
        // 特性
        //-------------------------------------------------------------------------------------------------------
        AddCharacteristic(CharacteristicSanity.Gain.class);
        AddCharacteristic(CharacteristicSanity.Lose.class);
        AddCharacteristic(CharacteristicHealth.Gain.class);
        AddCharacteristic(CharacteristicHealth.Lose.class);
        if(FBS.cooperatesSS2){
            AddCharacteristic(CharacteristicWater.Gain.class);
            AddCharacteristic(CharacteristicWater.Lose.class);
            AddCharacteristic(CharacteristicStamina.Gain.class);
            AddCharacteristic(CharacteristicStamina.Lose.class);
        }
        AddCharacteristic(CharacteristicExp.Gain.class);
        AddCharacteristic(CharacteristicExp.Lose.class);
        AddCharacteristic(CharacteristicLook.class);
        AddCharacteristic(CharacteristicWeight.class);
        AddCharacteristic(CharacteristicQuality.class);
        AddCharacteristic(CharacteristicCleverness.Gain.class);
        AddCharacteristic(CharacteristicPoison.Gain.class);
        AddCharacteristic(CharacteristicPoison.Lose.class);
        AddCharacteristic(CharacteristicPower.Gain.class);
        AddCharacteristic(CharacteristicPower.Lose.class);
        AddCharacteristic(CharacteristicSpeed.Gain.class);
        AddCharacteristic(CharacteristicSpeed.Lose.class);
        AddCharacteristic(CharacteristicConfusion.Gain.class);
        AddCharacteristic(CharacteristicConfusion.Lose.class);

        //-------------------------------------------------------------------------------------------------------
        // 鑑定
        //-------------------------------------------------------------------------------------------------------
        RegisterAppraisal(new ItemStack(Items.potionitem), new ItemStack(ItemCore.alchemyMaterial, 1, 0), 63);
        RegisterAppraisal(new ItemStack(Items.potionitem), new ItemStack(ItemCore.alchemyMaterial, 1, 1), 1);
        RegisterAppraisal(new ItemStack(Items.lava_bucket), new ItemStack(ItemCore.alchemyMaterial, 1, 2), 1);

        RegisterAppraisal("treeLeaves", new ItemStack(ItemCore.alchemyMaterial, 1, 3), 63);
        RegisterAppraisal("treeLeaves", new ItemStack(ItemCore.alchemyMaterial, 1, 6), 1);
        RegisterAppraisal(new ItemStack(Items.stick), new ItemStack(ItemCore.alchemyMaterial, 1, 4), 63);
        RegisterAppraisal(new ItemStack(Items.stick), new ItemStack(ItemCore.alchemyMaterial, 1, 7), 1);

        for(int i=0;i<ItemAlchemyMaterial.NAMES.length;i++){
            if(ItemAlchemyMaterial.NAMES[i].startsWith("herb") && i!=18 && i!=36 && i!=37){
                RegisterAppraisal(new ItemStack(ItemCore.herbUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
            if(ItemAlchemyMaterial.NAMES[i].startsWith("seeds")){
                RegisterAppraisal(new ItemStack(ItemCore.seedsUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
            if(ItemAlchemyMaterial.NAMES[i].startsWith("grass")){
                RegisterAppraisal(new ItemStack(ItemCore.grassUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
            if(ItemAlchemyMaterial.NAMES[i].startsWith("flower") || i==31 || i==35){
                RegisterAppraisal(new ItemStack(ItemCore.flowerUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
            if(ItemAlchemyMaterial.NAMES[i].startsWith("fruits") && i!=24){
                RegisterAppraisal(new ItemStack(ItemCore.fruitsUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
            if(ItemAlchemyMaterial.NAMES[i].startsWith("mushroom") && i!=25 && (i<42 || i>45)){
                RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, i), 10);
            }
        }
        RegisterAppraisal(new ItemStack(ItemCore.herbUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 18), 60);
        RegisterAppraisal(new ItemStack(ItemCore.herbUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 36), 3);
        RegisterAppraisal(new ItemStack(ItemCore.herbUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 37), 3);

        RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 25), 50);
        RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 42), 3);
        RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 43), 3);
        RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 44), 3);
        RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterial, 1, 45), 3);

        for(int i=0;i<ItemEatableAlchemyMaterial.NAMES.length;i++){
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("herb")){
                RegisterAppraisal(new ItemStack(ItemCore.herbUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("seeds")){
                RegisterAppraisal(new ItemStack(ItemCore.seedsUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("grass")){
                RegisterAppraisal(new ItemStack(ItemCore.grassUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("flower")){
                RegisterAppraisal(new ItemStack(ItemCore.flowerUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("fruits")){
                RegisterAppraisal(new ItemStack(ItemCore.fruitsUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
            if(ItemEatableAlchemyMaterial.NAMES[i].startsWith("mushroom")){
                RegisterAppraisal(new ItemStack(ItemCore.mushroomUnknown), new ItemStack(ItemCore.alchemyMaterialEatable, 1, i), 10);
            }
        }

        //-------------------------------------------------------------------------------------------------------
        // 素材
        //-------------------------------------------------------------------------------------------------------
        for(int i=0;i<ItemAlchemyMaterial.NAMES.length;i++){
            String t=ItemAlchemyMaterial.NAMES[i];

            if(t.startsWith("grass") || t.startsWith("roots") || t.startsWith("grass") || t.startsWith("flower") || t.startsWith("seeds") || t.startsWith("fruits")){
                RegisterMaterial("plant", new ItemStack(ItemCore.alchemyMaterial, 1, i));
            }

            if(t.startsWith("herb")) RegisterMaterial("herb", new ItemStack(ItemCore.alchemyMaterial, 1, i));
            if(t.startsWith("mushroom")) RegisterMaterial("mushroom", new ItemStack(ItemCore.alchemyMaterial, 1, i));

            if(t.startsWith("roots")) RegisterMaterial("roots", new ItemStack(ItemCore.alchemyMaterial, 1, i));
            if(t.startsWith("branch")) RegisterMaterial("branch", new ItemStack(ItemCore.alchemyMaterial, 1, i));
            if(t.startsWith("leaves")) RegisterMaterial("leaves", new ItemStack(ItemCore.alchemyMaterial, 1, i));

            if(t.endsWith("Unclean")) RegisterMaterial("unclean", new ItemStack(ItemCore.alchemyMaterial, 1, i));
        }
        for(int i=0;i<ItemEatableAlchemyMaterial.NAMES.length;i++){
            String t=ItemEatableAlchemyMaterial.NAMES[i];
            if(t.startsWith("grass") || t.startsWith("roots") || t.startsWith("grass") || t.startsWith("flower") || t.startsWith("seeds") || t.startsWith("fruits")){
                RegisterMaterial("plant", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
            }

            if(t.startsWith("herb")) RegisterMaterial("herb", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
            if(t.startsWith("mushroom")) RegisterMaterial("mushroom", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));

            if(t.startsWith("roots")) RegisterMaterial("roots", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
            if(t.startsWith("branch")) RegisterMaterial("branch", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
            if(t.startsWith("leaves")) RegisterMaterial("leaves", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));

            if(t.endsWith("Unclean")) RegisterMaterial("unclean", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
        }
        //RegisterMaterial("herb", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 2));
        RegisterMaterial("herb", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 3));

        RegisterMaterial("potion base", new ItemStack(ItemCore.alchemyMaterial, 1, 18));
        RegisterMaterial("potion base", new ItemStack(ItemCore.alchemyMaterial, 1, 25));
        RegisterMaterial("potion base", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 0));

        //for(int i=9;i<18;i++) if(!ItemAlchemyMaterial.NAMES[i].startsWith("seeds")) RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterial, 1, i));
        for(int i=19;i<25;i++) RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterial, 1, i));
        for(int i=36;i<40;i++) RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterial, 1, i));
        //for(int i=41;i<46;i++) RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterial, 1, i));
        //for(int i=2;i<7;i++) RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterialEatable, 1, i));
        RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyMaterial, 1, 40));
        RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 2));
        RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 3));
        RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 6));
        RegisterMaterial("potion flavor", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 7));

        RegisterMaterial("activator", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 4));

        RegisterMaterial("spirit medium", new ItemStack(ItemCore.alchemyMaterial, 1, 22));
        RegisterMaterial("spirit medium", new ItemStack(ItemCore.alchemyMaterial, 1, 40));

        RegisterMaterial("water", new ItemStack(ItemCore.alchemyMaterial, 1, 0));
        RegisterMaterial("water", new ItemStack(ItemCore.alchemyMaterial, 1, 1));
        RegisterMaterial("lava", new ItemStack(ItemCore.alchemyMaterial, 1, 2));

        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 26));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 27));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 29));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 30));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 41));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 42));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterial, 1, 45));
        RegisterMaterial("poisonous", new ItemStack(ItemCore.alchemyMaterialEatable, 1, 3));

        RegisterMaterial("explosive", new ItemStack(ItemCore.alchemyMaterial, 1, 28));
        RegisterMaterial("explosive", new ItemStack(ItemCore.alchemyMaterial, 1, 34));
        RegisterMaterial("explosive", new ItemStack(ItemCore.alchemyMaterial, 1, 38));
        RegisterMaterial("explosive", new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 1));
        //-------------------------------------------------------------------------------------------------------
        // 調合
        //-------------------------------------------------------------------------------------------------------
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 0), new ItemStack(ItemCore.alchemyPotion, 1, 0), 5, 0.6f, 100, "water", "potion base", "potion flavor");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 1), new ItemStack(ItemCore.alchemyPotion, 1, 1), 10, 0.55f, 150, "water", "potion base", "potion flavor", "potion flavor");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 2), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 0), 1, 0.85f, 20, "water");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 3), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 1), 3, 0.7f, 20, "explosive");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 4), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 2), 7, 0.6f,  80, "herb", "herb", "herb");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 5), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 3), 7, 0.6f, 80, "herb", "herb", "herb");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 6), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 4), 12, 0.45f, 150, "water", "potion base", "explosive");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 7), new ItemStack(ItemCore.alchemyPotion, 1, 2), 15, 0.45f, 200, "water", "potion base", "spirit medium");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 8), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 5), 10, 0.5f, 110, "activator", "potion flavor");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 9), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 6), 1, 0.75f, 20, "plant");
        RegisterRecipe(new ItemStack(ItemCore.alchemyRecipe, 1, 10), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 7), 1, 0.75f, 20, "mushroom");


        //-------------------------------------------------------------------------------------------------------
        // 錬金アイテム以外でカゴに入るアイテム
        //-------------------------------------------------------------------------------------------------------
        RegisterBasketItem(new ItemStack(ItemCore.herbUnknown));
        RegisterBasketItem(new ItemStack(ItemCore.grassUnknown));
        RegisterBasketItem(new ItemStack(ItemCore.fruitsUnknown));
        RegisterBasketItem(new ItemStack(ItemCore.flowerUnknown));
        RegisterBasketItem(new ItemStack(ItemCore.seedsUnknown));
        RegisterBasketItem(new ItemStack(ItemCore.mushroomUnknown));
    }

    /**
     * デフォルト特性を持った状態でItemStackを生成する
     * @param item IAlchemyMaterialまたはIAlchemyProductのいずれかを実装したItem
     * @param stacksize
     * @param meta
     * @return
     */
    public static ItemStack getItemStack(Item item, int stacksize, int meta){
        ItemStack itemStack=new ItemStack(item, stacksize, meta);
        itemStack.setTagCompound(new NBTTagCompound());
        if(item instanceof IAlchemyMaterial){
            AlchemyRegistry.WriteCharacteristicToNBT(itemStack.getTagCompound(), ((IAlchemyMaterial) item).addCharacteristics(itemStack, rand));
        }
        if(item instanceof IAlchemyProduct){
            AlchemyRegistry.WriteCharacteristicToNBT(itemStack.getTagCompound(), ((IAlchemyProduct) item).getDefaultCharacteristics(itemStack, rand));
        }
        return itemStack;
    }

    //--------------------------------------------------------------------------------------
    //  特性
    //--------------------------------------------------------------------------------------

    /**
     * 特性を登録する
     * @param chara
     */
    public static void AddCharacteristic(Class<? extends CharacteristicBase> chara){
        instance.characteristics.add(chara);
        instance.characteristicsIdMap.put(chara, instance.characteristics.size()-1);
    }

    /**
     * 特性からIdを得る
     * @param chara
     * @return
     */
    public static int GetCharacteristicId(Class<? extends CharacteristicBase> chara){
        if(!instance.characteristicsIdMap.containsKey(chara)){
            AddCharacteristic(chara);
        }

        return instance.characteristicsIdMap.get(chara);
    }

    /**
     * Idから特性を得る
     * @param id
     * @return
     */
    public static Class<? extends CharacteristicBase> GetCharacteristicFromId(int id){
        if(id<0 || id>=instance.characteristics.size()) return null;
        return instance.characteristics.get(id);
    }

    /**
     * 特性のリストをnbtに書き込む
     * @param nbt
     * @param list
     */
    public static void WriteCharacteristicToNBT(NBTTagCompound nbt, ArrayList<CharacteristicBase> list){
        NBTTagList tags=new NBTTagList();
        for(CharacteristicBase  cb : list){
            NBTTagCompound tag=new NBTTagCompound();
            tag.setInteger("id", cb.getId());
            cb.writeToNBT(tag);
            tags.appendTag(tag);
        }

        nbt.setTag("characteristics", tags);
        nbt.setBoolean("appraisal", true);
    }

    /**
     * nbtから特性リストを得る
     * @param nbt
     * @return
     */
    public static ArrayList<CharacteristicBase> ReadCharacteristicFromNBT(NBTTagCompound nbt){
        ArrayList<CharacteristicBase> ret=new ArrayList<CharacteristicBase>();

        if(nbt!=null && nbt.hasKey("characteristics")){
            NBTTagList tags=(NBTTagList)nbt.getTag("characteristics");
            for(int i=0;i<tags.tagCount();i++){
                NBTTagCompound tag=tags.getCompoundTagAt(i);
                int id=tag.getInteger("id");
                try{
                    CharacteristicBase cb=GetCharacteristicFromId(id).newInstance();
                    cb.readFromNBT(tag);
                    ret.add(cb);
                }
                catch (Exception e){
                    FBS.logger.error(e.toString());
                }
            }
        }

        return ret;
    }

    /**
     * listに特性一覧表を追加する
     * @param itemStack
     * @param list
     * @param flag
     */
    public static void AddCharacteristicsInfo(ItemStack itemStack, List list, boolean flag){
        list.add(StatCollector.translateToLocal("alchemy.fbs.characteristic"));
        if(itemStack.hasTagCompound()){
            ArrayList<CharacteristicBase> cbs=AlchemyRegistry.ReadCharacteristicFromNBT(itemStack.getTagCompound());

            for(CharacteristicBase cb : cbs){
                list.add(cb.getNameColor()+"-"+cb.getLocalizedName()+":"+cb.getLocalizedEffectValue());
            }
        }
    }

    //--------------------------------------------------------------------------------------
    //  素材
    //--------------------------------------------------------------------------------------

    /**
     * 素材としてアイテムスタックを登録する
     * @param name
     * @param itemStack
     */
    public static void RegisterMaterial(String name, ItemStack itemStack){
        if(!instance.materials.containsKey(name)){
            ArrayList<ItemStack> list=new ArrayList<ItemStack>();
            instance.materials.put(name, list);
        }

        instance.materials.get(name).add(itemStack);
        RegisterMaterialName(itemStack, name);
    }

    /**
     * アイテムスタックがその素材として使えるかを返す
     * @param name
     * @param itemStack
     * @return
     */
    public static boolean IsMatching(String name, ItemStack itemStack){
        if(!instance.materials.containsKey(name)) return false;
        ArrayList<ItemStack> list=instance.materials.get(name);

        for(ItemStack item : list){
            if(item.isItemEqual(itemStack)){
                return true;
            }
        }

        return false;
    }

    private static Map.Entry<ItemStack, ArrayList<String>> cachedMaterial;
    /**
     * アイテムスタックの持つ素材名を得る
     * @param itemStack
     * @return
     */
    public static ArrayList<String> GetMaterialNames(ItemStack itemStack){
        if(cachedMaterial!=null && cachedMaterial.getKey().isItemEqual(itemStack)){
            return cachedMaterial.getValue();
        }

        for(Map.Entry<ItemStack, ArrayList<String>> e : instance.materialNames.entrySet()){
            if(e.getKey().isItemEqual(itemStack)){
                cachedMaterial=e;
                return e.getValue();
            }
        }
        return null;
    }
    private static void RegisterMaterialName(ItemStack itemStack, String name){
        ArrayList<String> list=GetMaterialNames(itemStack);

        if(list!=null){
            //素材名リストがある場合直接追加
            list.add(name);
        }
        else{
            //素材名リストがない場合リスト作ってマップに追加
            list=new ArrayList<String>();
            list.add(name);

            instance.materialNames.put(itemStack, list);
        }
    }

    /**
     * listに素材名一覧を追加する
     * @param itemStack
     * @param list
     * @param flag
     */
    public static void AddMaterialInfo(ItemStack itemStack, List list, boolean flag){
        list.add(StatCollector.translateToLocal("alchemy.fbs.material"));
        ArrayList<String> names= AlchemyRegistry.GetMaterialNames(itemStack);
        if(names!=null && !names.isEmpty()){
            for(String str : names){
                list.add("-"+str);
            }
        }
    }
    //--------------------------------------------------------------------------------------
    //  鑑定
    //--------------------------------------------------------------------------------------

    /**
     * 鑑定アイテムを登録する
     * @param input
     * @param output 鑑定品(ItemはIAlchemyMaterialを継承しておくこと)
     * @param weight 出現重み
     */
    public static void RegisterAppraisal(ItemStack input, ItemStack output, int weight){
        AppraisalItemStack ais=new AppraisalItemStack(input);
        ArrayList<WeightedItemStack> list=GetAppraisals(ais);
        list.add(new WeightedItemStack(weight, output));
    }
    /**
    * 鑑定アイテムを登録する(鉱石辞書編)
    * @param input
    * @param output 鑑定品(ItemはIAlchemyMaterialを継承しておくこと)
    * @param weight 出現重み
    */
    public static void RegisterAppraisal(String input, ItemStack output, int weight){
        AppraisalItemStack ais=new AppraisalItemStack(input);
        ArrayList<WeightedItemStack> list=GetAppraisals(ais);
        list.add(new WeightedItemStack(weight, output));
    }
    private static ArrayList<WeightedItemStack> GetAppraisals(AppraisalItemStack ais){
        ArrayList<WeightedItemStack> list;

        for(AppraisalItemStack aiss : instance.appraisalItems.keySet()){
            if(aiss.equals(ais)){
                return instance.appraisalItems.get(aiss);
            }
        }
        list=new ArrayList<WeightedItemStack>();
        instance.appraisalItems.put(ais, list);
        return list;
    }

    public static ArrayList<WeightedItemStack> GetAppraisalList(ItemStack input){
        for(AppraisalItemStack aiss : instance.appraisalItems.keySet()){
            if(aiss.isItemEqual(input)) return instance.appraisalItems.get(aiss);
        }
        return null;
    }
    public static ArrayList<ItemPair> GetAlllAppraisal(){
        ArrayList<ItemPair> ret=new ArrayList<ItemPair>();
        for(Map.Entry<AppraisalItemStack, ArrayList<WeightedItemStack>> e : instance.appraisalItems.entrySet()){
            for(WeightedItemStack wis :  e.getValue()){
                ret.add(new ItemPair(e.getKey().getItemStack(), wis.getItemStack()));
            }
        }
        return ret;
    }

    /**
     * そのアイテムが鑑定できるか判定する
     * @param input
     * @return
     */
    public static boolean CanAppraisal(ItemStack input){
        NBTTagCompound nbt=input.getTagCompound();
        if(nbt!=null && nbt.getBoolean("appraisal")) return false;

        return GetAppraisalList(input)!=null;
    }

    /**
     * 鑑定したアイテムを得る。この時ランダムで特性が付与される。
     * @param input
     * @return 鑑定できないアイテムであればnull
     */
    public static ItemStack GetRandomAppraisal(ItemStack input){
        ArrayList<WeightedItemStack> list=GetAppraisalList(input);
        if(list.isEmpty()) return null;

        int ws=0;
        for(WeightedItemStack wis : list){
            ws+=wis.getWeight();
        }

        int r=instance.rand.nextInt(ws);
        ws=0;
        for(int i=0;i<list.size();i++){
            WeightedItemStack wis=list.get(i);
            if(r<wis.getWeight()+ws){
                ItemStack ret=wis.getItemStack().copy();

                //アイテムに特性を付与
                ArrayList<CharacteristicBase> cList=((IAlchemyMaterial)ret.getItem()).addCharacteristics(ret, instance.rand);
                if(cList!=null && !cList.isEmpty()){
                    ret.setTagCompound(new NBTTagCompound());
                    WriteCharacteristicToNBT(ret.getTagCompound(), cList);
                   // FBS.logger.info("written");
                }
                //FBS.logger.info(cList.toString());

                return ret;
            }
            ws+=wis.getWeight();
        }

        return null;
    }


    //--------------------------------------------------------------------------------------
    //  調合
    //--------------------------------------------------------------------------------------
    private static Recipe cachedRecipe;
    public static void RegisterRecipe(ItemStack key, ItemStack output, int lv, float prob, double exp, String ... materials){
        instance.recipes.add(new Recipe(lv, prob, key, output, materials));
        RegisterProductExpPair(output, exp);
    }
    public static Recipe GetRecipe(ItemStack key){
        if(cachedRecipe!=null && cachedRecipe.isKey(key)) return cachedRecipe;

        for(Recipe r : instance.recipes){
            if(r.isKey(key)){
                cachedRecipe=r;
                return cachedRecipe;
            }
        }

        return null;
    }
    /**
     * レシピと投入済み素材リストから、inputが未投入の素材か判定する
     * @param key
     * @param input
     * @param inputMaterials
     * @return
     */
    public static boolean IsMaterial(ItemStack key, ItemStack input, ArrayList<ItemStack> inputMaterials){
        //レシピを得る
        Recipe r=GetRecipe(key);
        return IsMaterial(r, input, inputMaterials);
    }
    public static boolean IsMaterial(Recipe recipe, ItemStack input, ArrayList<ItemStack> inputMaterials){
        if(recipe==null) return false;

        //matから未投入素材リストを得る
        LinkedList<String> mats=recipe.getMaterialList();
        LinkedList<ItemStack> tmp=new LinkedList<ItemStack>(inputMaterials);
        for(int i=0;i<mats.size();){
            String name=mats.get(i);

            boolean found=false;
            for(ItemStack is : tmp){
                if(IsMatching(name, is)){
                    tmp.remove(is);
                    found=true;
                    break;
                }
            }
            if(found){
                mats.remove(i);
                continue;
            }
            i++;
        }

        //未投入リストにinputが含まれているか判定
        for(String name : mats){
            if(IsMatching(name, input)) return true;
        }
        return false;
    }

    public static boolean IsSatisfied(ItemStack key, ArrayList<ItemStack> inputMaterials){
        return IsSatisfied(GetRecipe(key), inputMaterials);
    }
    public static boolean IsSatisfied(Recipe recipe, ArrayList<ItemStack> inputMaterials){
        if(recipe==null) return false;

        //matから未投入素材リストを得る
        LinkedList<String> mats=recipe.getMaterialList();

        //投入素材数が違うのに条件を満たしているはずがないだろ！
        if(inputMaterials.size()!=mats.size()){
            return false;
        }

        //投入素材リストを走査し、必要素材リストの中身を抜いていく
        LinkedList<ItemStack> tmp=new LinkedList<ItemStack>(inputMaterials);
        for(int i=0;i<mats.size();){
            String name=mats.get(i);

            boolean found=false;
            for(ItemStack is : tmp){
                if(IsMatching(name, is)){
                    tmp.remove(is);
                    found=true;
                    break;
                }
            }
            if(found){
                mats.remove(i);
                continue;
            }
            i++;
        }

        //条件を満たしていればこの時点で必要素材リストは空のはずである
        return mats.isEmpty();
    }

    /**
     * レシピと投入済み素材リストから、特性が付与された完成品を得る
     * @param key
     * @param inputMaterials
     * @return
     */
    public static ItemStack GetAlchemyProduct(ItemStack key, ArrayList<ItemStack> inputMaterials){
        Recipe r=GetRecipe(key);
        ItemStack ret=r.getProduct();

        IAlchemyProduct prod=((IAlchemyProduct) ret.getItem());
        ArrayList<CharacteristicBase> characteristics;//完成品の持つ特性

        //------------------------初期特性------------------------------
        characteristics=prod.getDefaultCharacteristics(ret, instance.rand);

        //-----------------------引継ぎ特性-----------------------------
        LinkedList<CharacteristicBase> inherits=new LinkedList<CharacteristicBase>();
        for(ItemStack mat : inputMaterials){
            ArrayList<CharacteristicBase> aList=ReadCharacteristicFromNBT(mat.getTagCompound());
            for(CharacteristicBase cb : aList){
                if(prod.canInherit(ret, cb)) inherits.add(cb);
            }
        }

        //-----------------------引継ぎ判定-----------------------------
        int max=prod.getMaxInheritAmount(ret);
        for(int i=0;i<max && !inherits.isEmpty();i++){
            int index=instance.rand.nextInt(inherits.size());
            CharacteristicBase cb=inherits.get(index);

            //その特性を既に持っているか
            boolean had=false;
            for(CharacteristicBase ccbb : characteristics){
                if(ccbb.getClass().equals(cb.getClass())){
                    had=true;
                    break;
                }
            }
            if(had){
                inherits.remove(index);
                continue;
            }

            characteristics.add(cb);
        }

        //------------------------特性を書き込む-------------------------------
        if(!ret.hasTagCompound()) ret.setTagCompound(new NBTTagCompound());
        WriteCharacteristicToNBT(ret.getTagCompound(), characteristics);

        return ret;
    }
    public static ItemStack GetAlchemyProduct(Recipe recipe, ArrayList<ItemStack> inputMaterials){
        ItemStack ret=recipe.getProduct().copy();

        IAlchemyProduct prod=((IAlchemyProduct) ret.getItem());
        ArrayList<CharacteristicBase> characteristics;//完成品の持つ特性

        //------------------------初期特性------------------------------
        characteristics=prod.getDefaultCharacteristics(ret, instance.rand);

        //-----------------------引継ぎ特性-----------------------------
        LinkedList<CharacteristicBase> inherits=new LinkedList<CharacteristicBase>();
        for(ItemStack mat : inputMaterials){
            ArrayList<CharacteristicBase> aList=ReadCharacteristicFromNBT(mat.getTagCompound());
            for(CharacteristicBase cb : aList){
                if(prod.canInherit(ret, cb)) inherits.add(cb);
            }
        }

        //-----------------------引継ぎ判定-----------------------------
        int max=prod.getMaxInheritAmount(ret);
        for(int i=0;i<max && !inherits.isEmpty();i++){
            int index=instance.rand.nextInt(inherits.size());
            CharacteristicBase cb=inherits.get(index);
            inherits.remove(index);

            //その特性を既に持っているか
            boolean had=false;
            int cbIndex=-1;
            for(int k=0;k<characteristics.size();k++){
                if(characteristics.get(k).getClass().equals(cb.getClass())){
                    had=true;
                    cbIndex=k;
                    break;
                }
            }
            if(had){
                //特性が被った場合、より値の大きいものが優先される。
                if(characteristics.get(cbIndex).getValue()<cb.getValue()) characteristics.remove(cbIndex);
                else{
                    i--;
                    continue;
                }
            }

            characteristics.add(cb);
        }

        //------------------------特性を書き込む-------------------------------
        if(!ret.hasTagCompound()) ret.setTagCompound(new NBTTagCompound());
        WriteCharacteristicToNBT(ret.getTagCompound(), characteristics);

        return ret;
    }
    public static ArrayList<Recipe> GetRecieps(){
        return instance.recipes;
    }

    private static ProductExpPair cachedPEP;
    public static void RegisterProductExpPair(ItemStack p, double e){
        instance.productExpPairs.add(new ProductExpPair(p, e));
    }
    public static double GetProductExp(ItemStack p){
        if(cachedPEP!=null && cachedPEP.isItemEqual(p)){
            return cachedPEP.getExp();
        }

        for(ProductExpPair pep : instance.productExpPairs){
            if(pep.isItemEqual(p)){
                cachedPEP=pep;
                return pep.getExp();
            }
        }

        return 0;
    }

    private static ItemStack cachedBasketItem;
    public static void RegisterBasketItem(ItemStack itemStack){
        instance.basketItems.add(itemStack);
    }
    public static boolean isBasketItem(ItemStack itemStack){
        if(cachedBasketItem!=null && cachedBasketItem.isItemEqual(itemStack)) return true;

        for(ItemStack is : instance.basketItems){
            if(is.isItemEqual(itemStack)){
                cachedBasketItem=is;
                return true;
            }
        }
        return false;
    }

    //-------------------------------------------------------------------------------------
    public static class AppraisalItemStack{
        private boolean isOre;
        private int oreId;
        private ItemStack itemStack;

        public AppraisalItemStack(ItemStack item){
            isOre=false;
            itemStack=item;
        }
        public AppraisalItemStack(String oreId){
            isOre=true;
            this.oreId= OreDictionary.getOreID(oreId);
        }
        public boolean isItemEqual(ItemStack m){
            if (isOre){
                int[] ids=OreDictionary.getOreIDs(m);
                for(int i : ids){
                    if(i==oreId) return true;
                }
                return false;
            }
            else{
                return itemStack.isItemEqual(m);
            }
        }

        public ItemStack getItemStack(){
            if(itemStack==null && isOre){
                itemStack=OreDictionary.getOres(OreDictionary.getOreName(oreId)).get(0).copy();
            }
            return itemStack;
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof AppraisalItemStack){
                AppraisalItemStack ais=(AppraisalItemStack)obj;
                if(isOre!=ais.isOre) return false;
                return isOre?oreId==ais.oreId:itemStack.isItemEqual(ais.itemStack);
            }
            return false;
        }
    }
    public static class WeightedItemStack{
        private int weight;
        private ItemStack itemStack;

        public WeightedItemStack(int w, ItemStack stack){
            weight=w;
            itemStack=stack;
        }

        public int getWeight(){ return weight; }
        public ItemStack getItemStack(){ return itemStack; }

    }
    public static class Recipe{
        protected int level;
        protected float prob;
        protected ItemStack key;
        protected ItemStack output;
        protected String[] materials;

        public Recipe(int level, float prob, ItemStack key, ItemStack output, String ... materials){
            this.level=level;
            this.prob=prob;
            this.key=key;
            this.output=output;
            this.materials=materials;
        }

        public ItemStack getKey(){ return key; }
        public boolean isKey(ItemStack itemStack){
            return key.isItemEqual(itemStack);
        }
        public ItemStack getProduct(){
            return output;
        }
        public int getLevel(){ return level; }
        public float getProb(){ return prob; }

        public LinkedList<String> getMaterialList(){
            LinkedList<String> list=new LinkedList<String>();
            for(String n : materials) list.add(n);
            return list;
        }
    }
    public static class ProductExpPair{
        public ItemStack product;
        public double exp;

        public ProductExpPair(ItemStack p, double e){
            product=p;
            exp=e;
        }
        public boolean isItemEqual(ItemStack item){
            return product.isItemEqual(item);
        }
        public double getExp(){
            return exp;
        }
    }
    public static class ItemPair{
        private ItemStack item1;
        private ItemStack item2;
        public ItemPair(ItemStack i1, ItemStack i2){
            item1=i1;
            item2=i2;
        }
        public ItemStack getItem1(){ return item1; }
        public ItemStack getItem2(){ return item2; }
    }
}
