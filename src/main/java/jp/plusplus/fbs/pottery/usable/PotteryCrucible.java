package jp.plusplus.fbs.pottery.usable;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class PotteryCrucible extends PotteryUsableLimitted {
    public static ArrayList<String> mobNames=new ArrayList<String>();

    public PotteryCrucible(){
        if(mobNames.isEmpty()){
            mobNames.add("Zombie");
            mobNames.add("Skeleton");
            mobNames.add("Spider");
            mobNames.add("Creeper");
            mobNames.add("Enderman");
            mobNames.add("Blaze");
            mobNames.add("PigZombie");
            mobNames.add("Slime");
            mobNames.add("Ghast");
            mobNames.add("CaveSpider");
            mobNames.add("Silverfish");
            mobNames.add("LavaSlime");
            mobNames.add("Bat");
            mobNames.add("Witch");
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.monster";
    }

    @Override
    public void effect(EntityPlayer player, ItemStack pottery) {
        World w=player.worldObj;

        if(!w.isRemote){
            int mobC = 1;
            int count = 0;
            int max = mobNames.size();
            for (int i = 0; i < 100 && count < mobC; i++) {
                int n = rand.nextInt(max);
                if (spawnEntityLiving(mobNames.get(n), w, player.posX, player.posY, player.posZ)) {
                    count++;
                }
            }
        }
    }

    @Override
    public void onCrash(EntityPlayer player, ItemStack pottery){
        NBTTagCompound nbt=getPotteryNBT(pottery);
        if(!player.worldObj.isRemote){
            int mobC = nbt.getInteger(USE_COUNT);
            int count = 0;
            int max = mobNames.size();
            for (int i = 0; i < 100 && count < mobC; i++) {
                int n = rand.nextInt(max);
                if (spawnEntityLiving(mobNames.get(n), player.worldObj, player.posX, player.posY, player.posZ)) {
                    count++;
                }
            }
        }
    }

    private boolean spawnEntityLiving(String name, World world, double x, double y, double z){
        EntityLiving entity = (EntityLiving) EntityList.createEntityByName(name, world);
        if (entity == null) return false;
        entity.onSpawnWithEgg(null);


        boolean flag=false;
        for(int i=0;i<30;i++) {
            double x1 = x + 0.5 + (rand.nextDouble() - rand.nextDouble()) * 3;
            double y1 = y+rand.nextInt(3);
            double z1 = z + 0.5 + (rand.nextDouble() - rand.nextDouble()) * 3;
            float a = rand.nextFloat() * 360.0F;
            entity.setLocationAndAngles(x1, y1, z1, a, 0);

            world.spawnEntityInWorld(entity);
            entity.spawnExplosionParticle();
            entity.playLivingSound();
            flag=true;
            break;
        }

        if(flag){
            if(entity instanceof EntityCreeper && rand.nextFloat()<0.25f) {
                entity.getDataWatcher().updateObject(17, new Byte((byte)1));
            }
            return true;
        }
        return false;
    }
}
