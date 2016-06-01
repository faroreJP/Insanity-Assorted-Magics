package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class MagicHarvest extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(!isSpelled || usingStaff) return false;
        float r=0.15f+0.03f*Math.max(getLvDiff(), 0);
        return rand.nextFloat()<r;
    }

    @Override
    public void success() {
        int ld=Math.max(getLvDiff(), 0);
        int h=getHeight();

        //抽選
        ArrayList<Registry.ChestContent> list=Registry.GetChestContents(1);
        int valueSum=0;
        for(Registry.ChestContent cc : list) valueSum+=cc.weight;

        //アイテムスタックのスタックサイズと数
        int min=1+ld/2;
        int max=3+ld;
        int amount=6+ld/2+rand.nextInt(ld+5);

        for(int i=0;i<amount;i++){
            int r=rand.nextInt(valueSum);
            int sum=0;
            for(Registry.ChestContent cc : list){
                if(r<cc.weight+sum){
                    double x=player.posX+2*(rand.nextDouble()-rand.nextDouble());
                    double z=player.posZ+2*(rand.nextDouble()-rand.nextDouble());

                    ItemStack item=cc.get();
                    item.stackSize=min+rand.nextInt(max-min+1);
                    if(item.stackSize>item.getMaxStackSize()) item.stackSize=item.getMaxStackSize();

                    if(item.isItemEnchantable() && rand.nextInt(3)==0){
                        EnchantmentHelper.addRandomEnchantment(rand, item, 3*ld+rand.nextInt(10));
                    }

                    EntityItem e=new EntityItem(world, x,h-1.5*rand.nextDouble(),z, item);
                    world.spawnEntityInWorld(e);

                    break;
                }
                sum+=cc.weight;
            }
        }
    }

    @Override
    public void failure() {

    }

    @Override
    public String getMagicCircleName(){
        return "fbs.harvest";
    }

    public int getHeight(){
        int x= MathHelper.floor_double(player.posX);
        int y= MathHelper.floor_double(player.posY+player.getEyeHeight());
        int z= MathHelper.floor_double(player.posZ);

        if(world.getHeightValue(x,z)<y){
            return y+32;
        }
        else{
            int sy=y;
            while(y-sy<34 && world.isAirBlock(x,y,z)){
                y++;
            }
            y--;
            return y;
        }
    }
}
