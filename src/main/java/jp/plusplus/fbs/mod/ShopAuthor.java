package jp.plusplus.fbs.mod;

import com.ibm.icu.util.Calendar;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shift.mceconomy2.api.shop.IProduct;
import shift.mceconomy2.api.shop.IShop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class ShopAuthor implements IShop {
    int lastDate=-1;
    ArrayList<IProduct> cachedProductList=null;
    ArrayList<String> cachedBooks=null;

    @Override
    public String getShopName(World world, EntityPlayer player) {
        return "Spell Book Author";
    }

    @Override
    public void addProduct(IProduct product) {
    }

    @Override
    public ArrayList<IProduct> getProductList(World world, EntityPlayer player) {
        ArrayList<IProduct> list=new ArrayList<IProduct>();
        ArrayList<String> books= FBSEntityProperties.get(player).getDecodedBooks();
        int date=world.getCurrentDate().get(Calendar.DATE);
        Random rand=new Random(date);

        //日付と解読済み書物が一致する場合、商品のキャッシュを返す
        if(lastDate==date && cachedBooks!=null && cachedBooks.equals(books) && cachedProductList!=null){
            return cachedProductList;
        }

        //ソート用BookDataリスト生成
        Registry.BookData[] bds=new Registry.BookData[books.size()];
        for(int i=0;i<bds.length;i++){
            bds[i]=Registry.GetBook(books.get(i));
        }
        Arrays.sort(bds, new Comparator<Registry.BookData>() {
            @Override
            public int compare(Registry.BookData o1, Registry.BookData o2) {
                return o1.lv-o2.lv;
            }
        });

        //魔法の矢は必ずリストに入る
        list.add(new TFKProductItem(Registry.GetBookItemStack("fbs.arrow"), (int)((1800+150*3)*(0.75f+0.5f*rand.nextFloat()))));
        //解読したこのある書物を全てリストに入れる（値段は日替わり）
        for(Registry.BookData bd : bds){
            if(bd.isMagic && !bd.title.equals("fbs.arrow")){
                list.add(new TFKProductItem(Registry.GetBookItemStack(bd.title), (int)((1800+150*bd.lv)*(0.75f+0.5f*rand.nextFloat()))));
            }
        }

        //Lvが10より高い場合、1/4でリストから除外される（日替わり）
        for(int i=0;i<list.size();){
            IProduct p=list.get(i);
            ItemStack itemStack=p.getItem(null, null, null);

            if(Registry.GetBookDataFromItemStack(itemStack).lv>10 && rand.nextInt(4)==0){
                list.remove(i);
                continue;
            }
            i++;
        }

        //キャッシュを取っておく
        lastDate=date;
        cachedBooks=new ArrayList<String>(books);
        cachedProductList=list;

        return list;
    }
}
