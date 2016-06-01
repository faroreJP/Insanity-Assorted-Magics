package jp.plusplus.fbs.event;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.RecipeBladeSpice;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.IAlchemyMaterial;
import jp.plusplus.fbs.alchemy.IAlchemyProduct;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.api.event.PlayerSanityEvent;
import jp.plusplus.fbs.api.event.PlayerSanityRollEvent;
import jp.plusplus.fbs.api.event.PlayerUseMagicEvent;
import jp.plusplus.fbs.block.BlockBonfire;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.container.inventory.InventoryBasket;
import jp.plusplus.fbs.entity.EntityButterfly;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.*;
import jp.plusplus.fbs.item.enchant.EnchantmentSanityProtect;
import jp.plusplus.fbs.item.enchant.EnchantmentWealth;
import jp.plusplus.fbs.packet.MessagePlayerJoinInAnnouncement;
import jp.plusplus.fbs.packet.MessagePlayerProperties;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.particle.EntitySpellCircleFX;
import jp.plusplus.fbs.particle.EntitySpellFX;
import jp.plusplus.fbs.pottery.BlockPotteryBase;
import jp.plusplus.fbs.pottery.ItemBlockPottery;
import jp.plusplus.fbs.pottery.PotteryRegistry;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import jp.plusplus.fbs.world.TeleporterWarp;
import jp.plusplus.fbs.world.WorldGenGrass;
import jp.plusplus.fbs.world.WorldGenHerbs;
import jp.plusplus.fbs.world.WorldGenMushroom;
import jp.plusplus.fbs.world.biome.BiomeAutumn;
import jp.plusplus.fbs.world.structure.MapGenSealdLib;
import jp.plusplus.fbs.tileentity.render.RenderMagicCircle;
import jp.plusplus.fbs.world.structure.MapGenStudy;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.event.world.BlockEvent;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.api.event.PriceEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class FBSEventHandler implements IFuelHandler{
    public static IIcon SpellTexture;

    private Random rand=new Random();
    private long lastTime=-1;

    public FBSEventHandler(){}

    @SubscribeEvent
    public void onInitNoiseGensEvent(InitNoiseGensEvent event) {

    }

    /*IExtendedEntityPropertiesを登録する処理を呼び出す*/
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            FBSEntityProperties.register((EntityPlayer)event.entity);
        }
    }

    @SubscribeEvent
    /*ワールドに入った時に呼ばれるイベント。ここでIExtendedEntityPropertiesを読み込む処理を呼び出す*/
    public void onEntityJoinWorld(EntityJoinWorldEvent event)  {
        if (event.world.isRemote && event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entity;
            PacketHandler.INSTANCE.sendToServer(new MessagePlayerJoinInAnnouncement(player));
        }
        else{
            Entity e=event.entity;

            //火矢
            if(e.ticksExisted==0 && e instanceof EntityArrow){
                EntityArrow arrow=(EntityArrow)e;

                //まだ火がついていないもののみ
                if(!arrow.isBurning()){
                    int x=MathHelper.floor_double(arrow.posX);
                    int y=MathHelper.floor_double(arrow.posY);
                    int z=MathHelper.floor_double(arrow.posZ);

                    int r=2;

                    for(int i=x-r;i<=x+r;i++){
                        for(int j=y-r;j<=y+r;j++){
                            for(int k=z-r;k<=z+r;k++){
                                Block b=event.world.getBlock(i,j,k);
                                if(b instanceof BlockBonfire){
                                    if(event.world.getBlockMetadata(i,j,k)>0){
                                        arrow.setFire(100);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    //Dimension移動時や、リスポーン時に呼ばれるイベント。古いインスタンスと新しいインスタンスの両方を参照できる。
    public void onClonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        //死亡時に呼ばれてるかどうか
        if (event.wasDeath) {
            FBSEntityProperties oldEntityProperties = (FBSEntityProperties)event.original.getExtendedProperties(FBSEntityProperties.EXT_PROP_NAME);
            FBSEntityProperties newEntityProperties = (FBSEntityProperties)event.entityPlayer.getExtendedProperties(FBSEntityProperties.EXT_PROP_NAME);

            NBTTagCompound playerData = new NBTTagCompound();

            //SAN値の回復
            //もしも回復基準値より小さければその分まで回復する
            int t=(int)((float)FBS.sanityRecoveryRatio/100.f*oldEntityProperties.getMaxSanity());
            if(t>oldEntityProperties.getSanity()) oldEntityProperties.setSanity(t);
            if(oldEntityProperties.getSanity()<5) oldEntityProperties.setSanity(5);
            oldEntityProperties.saveNBTData(playerData);

            //データの書き込み
            newEntityProperties.loadNBTData(playerData);

            //ついでにバインド
            oldEntityProperties.copyFromBindInventory(event.entityPlayer);
        }
    }

    @SubscribeEvent
    /*リスポーン時に呼ばれるイベント。Serverとの同期を取る*/
    public void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.worldObj.isRemote) {
            PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(event.player), (EntityPlayerMP)event.player);
        }
    }

    @SubscribeEvent
    public void onDecorateChunckEvent(DecorateBiomeEvent.Pre event) {
        WorldGenHerbs wgh;
        WorldGenMushroom wgm;
        WorldGenGrass wgg;

        //地表と地下で分ける

        //-----------------------------------------地表---------------------------------------------
        wgg=new WorldGenGrass();
        for(int i=0;i<2;i++){
            int x = event.chunkX + event.rand.nextInt(16) + 8;
            int z = event.chunkZ + event.rand.nextInt(16) + 8;
            int h = event.world.getHeightValue(x, z);
            int y = h + event.rand.nextInt(16) - event.rand.nextInt(16);
            wgg.generate(event.world, event.rand, x, y, z);
        }

        if (event.rand.nextFloat() < 0.75f || event.world.provider.dimensionId == FBS.dimensionCrackId) {
            wgh = new WorldGenHerbs(true);
            for (int i = 0; i < 1; i++) {
                int x = event.chunkX + event.rand.nextInt(16) + 8;
                int z = event.chunkZ + event.rand.nextInt(16) + 8;
                int h = event.world.getHeightValue(x, z);
                int y = h + event.rand.nextInt(16) - event.rand.nextInt(16);
                wgh.generate(event.world, event.rand, x, y, z);
            }
        }

        wgm = new WorldGenMushroom(true);
        for (int i = 0; i < 1; i++) {
            int x = event.chunkX + event.rand.nextInt(16) + 8;
            int z = event.chunkZ + event.rand.nextInt(16) + 8;
            int h = event.world.getHeightValue(x, z);
            int y = h + event.rand.nextInt(16) - event.rand.nextInt(16);
            wgm.generate(event.world, event.rand, x, y, z);
        }

        //----------------------------------地下-----------------------------------------
        wgh = new WorldGenHerbs(false);
        for (int i = 0; i < 3; i++) {
            int x = event.chunkX + event.rand.nextInt(16) + 8;
            int z = event.chunkZ + event.rand.nextInt(16) + 8;
            int y = 1 + event.rand.nextInt(Math.max(event.world.getHeightValue(x, z) - 16, 2));
            if (event.world.provider.dimensionId == -1) y = 5 + event.rand.nextInt(100);
            if (event.world.provider.dimensionId == 1) y = 35 + event.rand.nextInt(40);
            wgh.generate(event.world, event.rand, x, y, z);
        }

        wgm = new WorldGenMushroom(false);
        for (int i = 0; i < 3; i++) {
            int x = event.chunkX + event.rand.nextInt(16) + 8;
            int z = event.chunkZ + event.rand.nextInt(16) + 8;
            int y = 1 + event.rand.nextInt(Math.max(event.world.getHeightValue(x, z) - 16, 2));
            if (event.world.provider.dimensionId == -1) y = 5 + event.rand.nextInt(100);
            if (event.world.provider.dimensionId == 1) y = 35 + event.rand.nextInt(40);
            wgm.generate(event.world, event.rand, x, y, z);
        }
    }

    @SubscribeEvent
    public void onPopulateChunkEvent(PopulateChunkEvent.Pre event) {
    }

    // generateStructuresInChunk相当
    // 完全にチャンクの要素が決定された後のタイミングならこちら
    // ここで生成するなら要塞等を潰さないように注意
    @SubscribeEvent
    public void onPopulateChunkEvent(PopulateChunkEvent.Post event) {
        int dId=event.world.provider.dimensionId;
        if(dId==0 || dId==FBS.dimensionAutumnId){
            MapGenSealdLib genSealedLib=new MapGenSealdLib();

            genSealedLib.func_151539_a(event.chunkProvider, event.world, event.chunkX, event.chunkZ, null);
            genSealedLib.generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);

            MapGenStudy genStudy=new MapGenStudy();
            genStudy.func_151539_a(event.chunkProvider, event.world, event.chunkX, event.chunkZ, null);
            genStudy.generateStructuresInChunk(event.world, event.rand, event.chunkX, event.chunkZ);

            //event.world.getChunkFromBlockCoords(event.chunkX, event.chunkZ).func_150802_k();
        }

        //Endのクソッタレだけ特別にここでハーブの生成処理
        if(dId==1){
            WorldGenHerbs wgh=new WorldGenHerbs(false);
            for(int i=0;i<1;i++){
                int x = event.chunkX*16 + event.rand.nextInt(16) + 8;
                int z = event.chunkZ*16 + event.rand.nextInt(16) + 8;
                int y=1+event.rand.nextInt(Math.max(event.world.getHeightValue(x, z)-16, 2));
                if(event.world.provider.dimensionId==1) y=40+event.rand.nextInt(40);
                wgh.generate(event.world, event.rand, x, y, z);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public int onPlayerUseItemTickEvent(PlayerUseItemEvent.Tick event){
        if(event.item.getItemUseAction()== FBS.actionSpell && event.duration%3==0){
            if(FBS.proxy.getClientWorld()!=null && event.entityPlayer.worldObj.isRemote){
                EntityPlayer ep=event.entityPlayer;
                //魔法陣がある場合、いいかんじにエフェクト
                boolean hasCircle=false;
                int px=MathHelper.floor_double(ep.posX);
                int py=MathHelper.floor_double(ep.posY)-1;
                int pz=MathHelper.floor_double(ep.posZ);

                TileEntity te=ep.worldObj.getTileEntity(px,py,pz);
                if(te instanceof TileEntityMagicCore){
                    TileEntityMagicCore mc=(TileEntityMagicCore)te;
                    Registry.MagicData md=Registry.GetMagicDataFromItemStack(event.item);
                    if(md!=null && mc.getCircleName().equals(md.title)){
                        hasCircle=true;

                        EntitySpellCircleFX fx=new EntitySpellCircleFX(ep.getEntityWorld(), px+0.5f, py+0.25f*rand.nextFloat(), pz+0.5f, 1+Math.min(rand.nextFloat(), 0.85f)*mc.getCircleRadius(), (float)(2*rand.nextFloat()*Math.PI-Math.PI));
                        fx.setParticleIcon(SpellTexture);
                        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
                    }
                }

                if(!hasCircle){
                    //通常の詠唱エフェクト
                    double motionX = rand.nextDouble()*(double) (-MathHelper.sin(ep.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(ep.rotationPitch / 180.0F * (float) Math.PI));
                    double motionZ = rand.nextDouble()*(double) (MathHelper.cos(ep.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(ep.rotationPitch / 180.0F * (float) Math.PI));
                    double motionY = rand.nextDouble()*(double) (-MathHelper.sin(ep.rotationPitch / 180.0F * (float) Math.PI));

                    double x=1.5*(rand.nextDouble()-rand.nextDouble());
                    double y=1.5*(rand.nextDouble()-rand.nextDouble());
                    double z=1.5*(rand.nextDouble()-rand.nextDouble());

                    EntitySpellFX fx=new EntitySpellFX(ep.getEntityWorld(), ep.posX+motionX, ep.posY+ep.getEyeHeight()+motionY, ep.posZ+motionZ, motionX+x,motionY+y,motionZ+z);
                    fx.setParticleIcon(SpellTexture);
                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
                }
            }
        }
        return 0;
    }

    public static boolean foodLocked=false;
    @SubscribeEvent
    public void onPlayerEatenItemEvent(PlayerUseItemEvent.Finish event){
        if(event.entityPlayer.worldObj.isRemote) return;

        Registry.ItemSanity s=Registry.GetItemSanity(event.item);
        if(s!=null){
            s.sanity(event.entityPlayer);
        }

        if(!foodLocked && event.item!=null && event.item.getItem() instanceof ItemFood){
            int x=MathHelper.floor_double(event.entityPlayer.posX);
            int y=MathHelper.floor_double(event.entityPlayer.posY);
            int z=MathHelper.floor_double(event.entityPlayer.posZ);

            int r=2;

            for(int i=x-r;i<=x+r;i++){
                for(int j=y-r;j<=y+r;j++){
                    for(int k=z-r;k<=z+r;k++){
                        Block b=event.entityPlayer.worldObj.getBlock(i, j, k);
                        if(b instanceof BlockBonfire){
                            if(event.entityPlayer.worldObj.getBlockMetadata(i,j,k)>0){
                                foodLocked=true;

                                //食べ物を焚き火の近くにいるプレイヤー全てに与える
                                 AxisAlignedBB aabb= AxisAlignedBB.getBoundingBox(i-r, j-r, k-r, i+r+1, j+r+1, k+r+1);
                                List players=event.entityPlayer.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                                Iterator it=players.iterator();
                                while(it.hasNext()){
                                    EntityPlayer ep=(EntityPlayer)it.next();
                                    if(ep.equals(event.entityPlayer)) continue;

                                    //食べ物渡す
                                    ItemStack cpy=event.item.copy();
                                    cpy.onFoodEaten(event.entityPlayer.worldObj, ep);

                                    //メッセージ残す
                                    String str=String.format(StatCollector.translateToLocal("info.fbs.food.shearing"), event.entityPlayer.getCommandSenderName(), cpy.getDisplayName());
                                    ep.addChatComponentMessage(new ChatComponentText(str));
                                }

                                foodLocked=false;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event) {
        if (event.isCanceled()) return;

        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) event.entityLiving;
            Entity ee = event.source.getEntity();


            //------------------------インフィニティ！------------------------
            boolean isInfinity = true;
            for (int i = 0; i < 4; i++) {
                ItemStack armor = ep.inventory.armorItemInSlot(i);
                if (armor == null || !(armor.getItem() instanceof ItemArmorInfinity)) {
                    isInfinity=false;
                    break;
                }
            }
            if(isInfinity){
                for(int i=0;i<4;i++){
                    ep.inventory.armorItemInSlot(i).damageItem(1, ep);
                }
                event.setCanceled(true);
                return;
            }


            //------------------------契約によるダメージ無効------------------------
            if (ep.isPotionActive(Registry.potionContract) && event.ammount >= ep.getHealth()) {
                //契約
                PotionEffect pe = ep.getActivePotionEffect(Registry.potionContract);
                ep.heal(ep.getMaxHealth());
                ep.removePotionEffect(Registry.potionContract.getId());
                event.setCanceled(true);
                return;
            }

            //------------------------攻撃によるSAN値減少------------------------
            if (ee != null) {
                Registry.MobSanity ms = Registry.GetMobSanity(ee);
                if (ms != null) {
                    ms.sanity(ep);
                }
            }

            //---------------------------壺の破壊------------------------------------
            String type=event.source.getDamageType();
            if(type.equals("fall") || type.equals("mob") || type.equals("explosion.player") || type.equals("explosion") || type.equals("player")){
                int iSize=ep.inventory.getSizeInventory();
                for(int i=0;i<iSize;i++){
                    ItemStack itemStack=ep.inventory.getStackInSlot(i);
                    if(itemStack!=null && itemStack.getItem() instanceof ItemBlockPottery){
                        IPottery ip=(IPottery)((ItemBlockPottery) itemStack.getItem()).field_150939_a;

                        float prob=ip.getCrashProbability(itemStack);
                        if(prob>0 && (event.source.getDamageType().equals("fall") || rand.nextFloat()<prob)){
                            String t=StatCollector.translateToLocal("info.pottery.crash");
                            ep.addChatComponentMessage(new ChatComponentText(String.format(t, itemStack.getDisplayName())));

                            //魔法の壺の効果
                            if(ip.hasEffect(itemStack.getTagCompound()) && ip.getState(itemStack.getTagCompound())== IPottery.PotteryState.BAKED){
                                int id= ItemBlockPottery.getId(itemStack);
                                PotteryRegistry.getPotteryEffect(id).onCrash(ep, itemStack);
                            }
                            ep.worldObj.playSoundAtEntity(ep, "dig.glass", 0.8F, 0.8F + ep.worldObj.rand.nextFloat() * 0.4F);
                            ep.inventory.setInventorySlotContents(i, null);
                        }
                    }
                }
            }


        }
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event){
        EntityLivingBase e=event.entityLiving;

        //魔力の秋で死亡した場合、蝶になる
        if(e!=null && !e.worldObj.isRemote){
            int x=MathHelper.floor_double(e.posX);
            int z=MathHelper.floor_double(e.posZ);

            BiomeGenBase bgb=e.worldObj.getBiomeGenForCoords(x, z);
            if(bgb instanceof BiomeAutumn){
                EntityButterfly eb=new EntityButterfly(e.worldObj, (float)e.posX, (float)e.posY+1, (float)e.posZ);
                e.worldObj.spawnEntityInWorld(eb);
            }
        }

        //ラッキーフィニッシュ
        if(!e.worldObj.isRemote && MCEconomyAPI.ShopManager.hasEntityPurchase(e)){
            EntityPlayer player=null;
            if(event.source.getSourceOfDamage() instanceof EntityPlayer){
                player=(EntityPlayer)event.source.getSourceOfDamage();
            }
            else if(event.source.getEntity() instanceof EntityPlayer){
                player=(EntityPlayer)event.source.getEntity();
            }

            if(player!=null){
                int luckyType=0;
                int mp=MCEconomyAPI.ShopManager.getEntityPurchase(e);
                int r, randMax=10000;

                //ここでラッキーフィニッシュの効果
                ItemStack spirit=SpiritManager.findSpiritTool(player);
                if(spirit!=null){
                    int lucky=SpiritStatus.readFromNBT(spirit.getTagCompound()).getSkillLevel("fbs.lucky")+1;
                    if(lucky>0){
                        randMax-=200*lucky;
                    }
                }

                //エンチャント効果
                int eSum= EnchantmentWealth.getSum(player);
                if(eSum>0) randMax-=200*eSum;

                //ラッキーフィニッシュ判定
                r=rand.nextInt(randMax);
                if(r<5) luckyType=3;
                else if(r<80+5) luckyType=2;
                else if(r<800+80+5) luckyType=1;

                switch (luckyType){
                    case 1: mp*=5; player.addChatComponentMessage(new ChatComponentText("Lucky Finish!")); break;
                    case 2: mp*=100; player.addChatComponentMessage(new ChatComponentText("Big Lucky Finish!")); break;
                    case 3: mp*=2000; player.addChatComponentMessage(new ChatComponentText("Huge Lucky Finish!")); player.triggerAchievement(AchievementRegistry.lucky); break;
                    default: break;
                }
                if(luckyType>0) MCEconomyAPI.addPlayerMP(player, mp, false);
            }
        }

        //ソウルバインド
        if(e instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) e;
            int index=SpiritManager.findSpiritToolIndex(ep);

            if(index!=-1){
                FBSEntityProperties prop = FBSEntityProperties.get(ep);
                SpiritStatus status=SpiritStatus.readFromNBT(ep.inventory.getStackInSlot(index).getTagCompound());

                //ここでソウルバインドの効果発動
                if(status.hasSkill("fbs.soulBind")){
                    prop.bindPlayerInventory(ep, false);
                    for(int i=0;i<ep.inventory.getSizeInventory();i++){
                        ep.inventory.setInventorySlotContents(i, null);
                    }
                }
                else{
                    prop.bindPlayerInventory(ep, true);
                    ep.inventory.setInventorySlotContents(index, null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        if(event.phase==TickEvent.Phase.START && !event.player.worldObj.isRemote){
            EntityPlayer ep=event.player;

            //------------------------------------
            boolean fullInfinity=false;
            for(int i=0;i<4;i++){
                ItemStack armor=ep.inventory.armorItemInSlot(i);
                if(armor==null || armor.getItem() instanceof ItemArmorInfinity){
                    fullInfinity=false;
                    break;
                }
            }
            if(fullInfinity){
                ep.triggerAchievement(AchievementRegistry.infinity);
            }


            if(!event.player.capabilities.isCreativeMode){
                FBSEntityProperties prop=FBSEntityProperties.get(ep);
                int san=prop.getSanity();

                //----------------------------------不定の狂気--------------------------------------------------
                float rate=(float)san/prop.getMaxSanity();
                boolean flag=false;

                if(san<40){
                    float rate1=(float)san/99;
                    if(rand.nextFloat()<0.0004f-0.00032f*rate1){
                        flag=true;
                    }
                }
                else if(rate<0.5f){
                    if(rand.nextFloat()<0.0004f-0.00032f*rate){
                        flag=true;
                    }
                }

                if(flag){
                    ep.addChatComponentMessage(new ChatComponentTranslation("info.fbs.san.3"));

                    ep.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 15, 2));
                    ep.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 20*15, 1));
                }


                //---------------------------------バイオームによるSAN値喪失------------------------------------------------
                BiomeGenBase bgb=ep.worldObj.getBiomeGenForCoords(MathHelper.floor_double(ep.posX), MathHelper.floor_double(ep.posZ));
                //ヴィンテールクロークを装備しているか
                boolean flag1=false;
                ItemStack armor = ep.getCurrentArmor(2);
                if (armor == null || armor.getItem() instanceof ItemCloak) flag1=true;

                if(bgb==Registry.biomeAutumn) {
                    //ついでに実績
                    ep.triggerAchievement(AchievementRegistry.autumn);

                    if (ep.worldObj.rand.nextInt(flag1?250:50)==1) {
                        SanityManager.loseSanity(ep, 1, 2, true);
                        ep.inventory.markDirty();
                    }
                }
                else if(bgb==Registry.biomeCrack) {
                    if (ep.worldObj.rand.nextInt(flag1?250:38)==1) {
                        SanityManager.loseSanity(ep, 1, 2, true);
                        ep.inventory.markDirty();
                    }
                }

                //-------------------------------------吸血-------------------------------------------------------
                if(ep.worldObj.getWorldTime()%100==0){
                    ItemStack current=ep.getCurrentEquippedItem();
                    if(current!=null && current.getItem() instanceof ISpiritTool){
                        SpiritStatus ss=SpiritStatus.readFromNBT(current.getTagCompound());
                        if(ss!=null && ss.getSkillLevel("fbs.blood")>=0 && rand.nextInt(4096)==0){
                            ep.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("spirit.demerit.fbs.blood")));
                            ep.addPotionEffect(new PotionEffect(Potion.wither.getId(), 20*5, 0));
                        }
                    }
                }
            }

            //---------------------------------狭間からの脱出------------------------------------------------
            if(ep.dimension==FBS.dimensionCrackId && ep.posY<0){
                World w= DimensionManager.getWorld(0);
                ChunkCoordinates cc=w.getSpawnPoint();

                cc.posX+=(rand.nextBoolean()?1:-1)*rand.nextInt(800);
                cc.posZ+=(rand.nextBoolean()?1:-1)*rand.nextInt(800);
                Chunk c=w.getChunkProvider().provideChunk(cc.posX/16, cc.posZ/16);
                if(c==null) FBS.logger.info("chunk is null");
                else{
                    int cx=cc.posX%16;
                    int cz=cc.posZ%16;
                    if(cx<0) cx+=16;
                    if(cz<0) cz+=16;

                    cc.posY=c.getHeightValue(cx, cz)+3;
                    //cc.posY=w.getHeightValue(cc.posX, cc.posZ);
                }

                EntityPlayerMP entityPlayerMP = (EntityPlayerMP) ep;
                ServerConfigurationManager serverConfigurationManager = entityPlayerMP.mcServer.getConfigurationManager();
                WorldServer worldServer = entityPlayerMP.mcServer.worldServerForDimension(0);

                serverConfigurationManager.transferPlayerToDimension(entityPlayerMP, 0, new TeleporterWarp(worldServer));
                ep.setPositionAndUpdate(cc.posX + 0.5, cc.posY + 1, cc.posZ+0.5);
                ep.fallDistance=0;
            }


        }
    }

    @SubscribeEvent
    public int onPlayerDestroyItemEvent(PlayerDestroyItemEvent event){
        if(event.original.getItem() instanceof ItemStaff){
            ItemStack[] items=ItemStaff.loadInventory(event.original);
            for(ItemStack i : items){
                if(!event.entityPlayer.inventory.addItemStackToInventory(i)){
                    event.entityPlayer.entityDropItem(i, event.entityPlayer.getEyeHeight());
                }
            }
        }
        return 0;
    }

    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        float f = FBS.proxy.getRenderPartialTicks();
        FBS.proxy.updateTimer();
        FBS.proxy.setRenderPartialTicks(f);

        //---------------------------------時報ボイスと空腹------------------------------------------------
        long time = event.world.getWorldTime()%24000;
        if (time!=lastTime && (time == 0 || time == 6000 || time == 15000)) {
            Iterator it=event.world.playerEntities.iterator();
            while(it.hasNext()){
                EntityPlayer ep=(EntityPlayer)it.next();
                if (ep != null) {
                    ItemStack spiritStack = SpiritManager.findSpiritTool(ep);
                    if (spiritStack != null) {
                        SpiritStatus status = SpiritStatus.readFromNBT(spiritStack.getTagCompound());

                        //空腹にする
                        boolean hunger=false;
                        /*
                        int food=status.getFoodLevel();
                        status.setFoodLevel(food-25);
                        if(food/25!=status.getFoodLevel()/25){
                            food=status.getFoodLevel();
                            if(food<50){
                                hunger=true;
                                SpiritManager.talk(ep, status.getCharacter(), "hunger", spiritStack);
                            }
                        }
                        */

                        //空腹でないなら時報ボイス
                        if(!hunger){
                            if (time == 0) SpiritManager.talk(ep, status.getCharacter(), "morning", spiritStack);
                            if (time == 6000) SpiritManager.talk(ep, status.getCharacter(), "noon", spiritStack);
                            if (time == 15000) SpiritManager.talk(ep, status.getCharacter(), "night", spiritStack);
                        }
                    }
                }
            }
        }
        //if(lastTime!=time) FBS.logger.info("time:"+time);
        lastTime=time;
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onStitchTexture(TextureStitchEvent.Pre e){
        //FMLLog.severe("called registering");
        if(e.map.getTextureType()==0) {
            BlockCore.mana.setIcons(e.map.registerIcon(FBS.MODID + ":fluidMana_still"));

            //-----------------------魔法陣のテクスチャ----------------------------------------------------------
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.copy", e.map, FBS.MODID + ":circleCopy");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.summonVillager", e.map, FBS.MODID + ":circleVillager");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.harvest", e.map, FBS.MODID + ":circleHarvest");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.barrier", e.map, FBS.MODID + ":circleBarrier");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.warp", e.map, FBS.MODID + ":circleWarp");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.contract", e.map, FBS.MODID + ":circleContract");
            RenderMagicCircle.RegisterMagicCircleIcon("fbs.timeTrace", e.map, FBS.MODID + ":circleTimeTrace");
        }
        if(e.map.getTextureType()==1){
            SpellTexture=e.map.registerIcon(FBS.MODID+":spelling");
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent e){
        EntityPlayer player=e.entityPlayer;
        ItemStack itemStack=player.getCurrentEquippedItem();

        //刃薬の処理
        if(e.target instanceof EntityLivingBase && itemStack!=null){
            if(itemStack.getItem() instanceof ItemSword){
                ArrayList<CharacteristicBase> cbs= RecipeBladeSpice.getCharacteristics(itemStack);
                for(CharacteristicBase cb : cbs){
                    cb.affectEntity(player.worldObj, (EntityLivingBase)e.target);
                }

                RecipeBladeSpice.consumeBladeSpiceAmount(itemStack);
            }
        }
    }

    @SubscribeEvent
    public void onSpiritAttackEntity(AttackEntityEvent e){
        EntityPlayer player=e.entityPlayer;
        ItemStack itemStack=player.getCurrentEquippedItem();
        if(itemStack!=null && itemStack.getItem() instanceof ISpiritTool){
            /*
            精霊武器用に攻撃時の処理を作る。
            */
            ISpiritTool sp=(ISpiritTool)itemStack.getItem();
            SpiritStatus status=SpiritStatus.readFromNBT(itemStack.getTagCompound());
            Entity entity=e.target;

            if (entity.canAttackWithItem()) {
                if (!entity.hitByEntity(player)) {
                    float damage = sp.calcDamage(status);
                    int knockback = 0;
                    float additionalDamage = 0.0F;

                    if (entity instanceof EntityLivingBase) {
                        //ここでノックバックスキルの効果適用
                        knockback+=status.getSkillLevel("fbs.knockback")+1;

                        //ここで特効系
                        int lv=0;
                        switch (((EntityLivingBase) entity).getCreatureAttribute()){
                            case UNDEAD: lv=status.getSkillLevel("fbs,smite")+1; break;
                            case ARTHROPOD: lv=status.getSkillLevel("fbs.arthropods")+1; break;
                        }
                        if(lv>0){
                            additionalDamage+=1.5f*lv;
                        }
                    }

                    if (player.isSprinting()) {
                        ++knockback;
                    }

                    if (damage > 0.0F || additionalDamage > 0.0F) {
                        //クリティカル判定
                        boolean isCritical = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && entity instanceof EntityLivingBase;
                        if(!isCritical){
                            //ここでクリティカルの効果適用
                            int criLv=status.getSkillLevel("fbs.critical")+1;
                            if(criLv>0){
                                isCritical=rand.nextFloat()<0.1f+0.05f*criLv;
                            }
                        }
                        if (isCritical && damage > 0.0F) {
                            damage *= 1.5F;
                        }

                        damage += additionalDamage;

                        //ここで炎の刃の効果適用
                        boolean flag1 = false;
                        int fire = status.getSkillLevel("fbs.fire")+1;
                        if (entity instanceof EntityLivingBase && fire > 0 && !entity.isBurning()) {
                            flag1 = true;
                            entity.setFire(1); //死亡時の処理のためにここでいったん燃やす
                        }

                        //ここで毒の刃の効果適応
                        if(entity instanceof EntityLivingBase){
                            int poison=status.getSkillLevel("fbs.poison")+1;
                            if(poison>0){
                                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.getId(), 20*3*poison));
                            }
                        }

                        //ここで英雄効果
                        int hero=status.getSkillLevel("fbs.hero")+1;
                        if(hero>0){
                            List mobs=player.worldObj.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, player.posX, player.posY, player.posZ).expand(5,5,5));
                            float ext=1.0f+hero*mobs.size()/20.f;
                            damage*=Math.min(ext, 2.0f);
                        }

                        //首狩り
                        if(entity instanceof EntityLivingBase){
                            int headhunt=status.getSkillLevel("fbs.headhunt")+1;
                            if(headhunt>0 && rand.nextFloat()<0.05f+0.03f*headhunt){
                                damage=((EntityLivingBase) entity).getMaxHealth()+1;
                            }
                        }

                        boolean damaged = entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
                        if (damaged) {
                            //ここで吸血の刃の効果適応
                            int vampire=status.getSkillLevel("fbs.vampire")+1;
                            if(vampire>0){
                                player.heal(0.5f*(1+MathHelper.floor_float((0.25f+0.05f*vampire)*damage/0.5f)));
                            }

                            if (knockback > 0) {
                                entity.addVelocity((double) (-MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * (float) knockback * 0.5F), 0.1D, (double) (MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));
                                player.motionX *= 0.6D;
                                player.motionZ *= 0.6D;
                                player.setSprinting(false);
                            }

                            if (isCritical) {
                                player.onCriticalHit(entity);
                            }

                            if (additionalDamage > 0.0F) {
                                player.onEnchantmentCritical(entity);
                            }

                            //オーバーキル取得
                            if (damage >= 18.0F) {
                                player.triggerAchievement(AchievementList.overkill);
                            }

                            player.setLastAttacker(entity);

                            if (entity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a((EntityLivingBase) entity, player);
                            }

                            EnchantmentHelper.func_151385_b(player, entity);
                            Object object = entity;

                            if (entity instanceof EntityDragonPart) {
                                IEntityMultiPart ientitymultipart = ((EntityDragonPart) entity).entityDragonObj;

                                if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase) {
                                    object = (EntityLivingBase) ientitymultipart;
                                }
                            }

                            //ここで契約の効果発動
                            int cont=status.getSkillLevel("fbs.contract")+1;
                            if(cont>0 && rand.nextFloat()<0.10f+0.05f*cont){
                                player.addPotionEffect(new PotionEffect(Registry.potionContract.getId(), 20*10, 0));
                            }

                            if (itemStack != null && object instanceof EntityLivingBase) {
                                itemStack.hitEntity((EntityLivingBase) object, player);
                            }

                            if (entity instanceof EntityLivingBase) {
                                //実績関係
                                player.addStat(StatList.damageDealtStat, Math.round(damage * 10.0F));

                                //ここでちゃんと燃やす
                                if (fire > 0) {
                                    entity.setFire(fire * 4);
                                }
                            }

                            player.addExhaustion(0.3F);
                        } else if (flag1) {
                            //鎮火処理？
                            entity.extinguish();
                        }
                    }
                }
            }



            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event){
        if(!event.entityPlayer.worldObj.isRemote){
            ItemStack item=SpiritManager.findSpiritTool(event.entityPlayer);
            if(item!=null){
                SpiritStatus ss=SpiritStatus.readFromNBT(item.getTagCompound());
                SpiritManager.talk(event.entityPlayer, ss.getCharacter(), "sleep", item);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUseMagicPre(PlayerUseMagicEvent.Pre e){
        String title=e.magic.magicData.title;

        if(!e.entityPlayer.worldObj.isRemote && title.equals("fbs.harvest")){
            e.entityPlayer.triggerAchievement(AchievementRegistry.harvest);
        }

        if(title.equals("fbs.copy")){
            int x=MathHelper.floor_double(e.entityPlayer.posX);
            int y=MathHelper.floor_double(e.entityPlayer.posY);
            int z=MathHelper.floor_double(e.entityPlayer.posZ);
            e.entityPlayer.openGui(FBS.instance, FBS.GUI_MAGIC_COPY_ID, e.entityPlayer.worldObj, x,y,z);
            e.setCanceled(true);
        }
        else if(title.equals("fbs.warp")){
            int x=MathHelper.floor_double(e.entityPlayer.posX);
            int y=MathHelper.floor_double(e.entityPlayer.posY);
            int z=MathHelper.floor_double(e.entityPlayer.posZ);

            //現在地を移動先リストへ追加
            SanityManager.addDestination(e.entityPlayer, e.entityPlayer.worldObj.provider.dimensionId, x,y,z);

            //魔術レベル一定以上で狭間追加
            if(e.entityPlayer.capabilities.isCreativeMode || FBSEntityPropertiesAPI.GetMagicLevelRaw(e.entityPlayer)>=25) {
                SanityManager.addDestination(e.entityPlayer, FBS.dimensionCrackId, -1, -1, -1);
            }

            if(!e.entityPlayer.worldObj.isRemote) e.entityPlayer.triggerAchievement(AchievementRegistry.warp);
            e.entityPlayer.openGui(FBS.instance, FBS.GUI_MAGIC_WARP_ID, e.entityPlayer.worldObj, x,y,z);
            e.setCanceled(true);
        }
        else if(title.equals("fbs.contract")){
            int x=MathHelper.floor_double(e.entityPlayer.posX);
            int y=MathHelper.floor_double(e.entityPlayer.posY);
            int z=MathHelper.floor_double(e.entityPlayer.posZ);
            e.entityPlayer.openGui(FBS.instance, FBS.GUI_MAGIC_CONTRACT_ID, e.entityPlayer.worldObj, x,y,z);
            e.setCanceled(true);
        }
        else if(title.equals("fbs.timeTrace") && e.magic.checkMagicCircle("fbs.timeTrace")) {
            int x = MathHelper.floor_double(e.entityPlayer.posX);
            int y = MathHelper.floor_double(e.entityPlayer.posY);
            int z = MathHelper.floor_double(e.entityPlayer.posZ);
            e.entityPlayer.openGui(FBS.instance, FBS.GUI_MAGIC_TIME_TRACE_ID, e.entityPlayer.worldObj, x, y, z);
            e.setCanceled(true);
        }

        //インフィニティ！
        if(!e.isCanceled()){
            EntityPlayer player=e.entityPlayer;
            for(int i=0;i<4;i++){
                ItemStack armor=player.inventory.armorItemInSlot(i);
                if(armor!=null && armor.getItem() instanceof ItemArmorInfinity){
                    int repair=e.magic.bookData.lv;
                    armor.setItemDamage(armor.getItemDamage()-repair);
                }
            }
        }
    }

    @SubscribeEvent
    public void generateOrePre(OreGenEvent.Pre event){
        if(!FBS.generatesOre)   return;

        WorldGenerator genRuby = new WorldGenMinable(BlockCore.ore, 0, 5, Blocks.stone);
        WorldGenerator genSapphire = new WorldGenMinable(BlockCore.ore, 1, 5, Blocks.stone);
        WorldGenerator genAmethyst = new WorldGenMinable(BlockCore.ore, 2, 5, Blocks.stone);

        if(TerrainGen.generateOre(event.world, event.rand, genRuby, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<2;i++){
                genRuby.generate(event.world, event.rand, event.worldX + event.rand.nextInt(16), 1 + event.rand.nextInt(16), event.worldZ + event.rand.nextInt(16));
            }
        }
        if(TerrainGen.generateOre(event.world, event.rand, genSapphire, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<2;i++){
                genSapphire.generate(event.world, event.rand, event.worldX + event.rand.nextInt(16), 1 + event.rand.nextInt(16), event.worldZ + event.rand.nextInt(16));
            }
        }
        if(TerrainGen.generateOre(event.world, event.rand, genAmethyst, event.worldX, event.worldZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            for(int i=0;i<2;i++){
                genAmethyst.generate(event.world, event.rand, event.worldX + event.rand.nextInt(16), 1 + event.rand.nextInt(16), event.worldZ + event.rand.nextInt(16));
            }
        }
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if(fuel.getItem() instanceof ItemBlock){
            Block b=((ItemBlock) fuel.getItem()).field_150939_a;
            if(b==BlockCore.plank) return 20*10;
        }
        if(fuel.getItem()==ItemCore.stick) return 20*5;

        return 0;
    }

    @SubscribeEvent
    public void onPriceEvent(PriceEvent event){
        ItemStack item=event.getPriceItem();

        //壺
        if(item.getItem() instanceof ItemBlockPottery){
            IPottery ip=(IPottery)((ItemBlockPottery) item.getItem()).field_150939_a;
            int p=ip.getMP(item);

            //魔法の壺補正
            if(ip.hasEffect(item.getTagCompound()) && ip.getState(item.getTagCompound())== IPottery.PotteryState.BAKED){
                p=MathHelper.floor_float(p*PotteryRegistry.getPotteryEffect(item.getTagCompound().getInteger(BlockPotteryBase.EFFECT_ID)).getPriceScale(item));
            }
            event.setNewPrice(p);
        }

        //錬金術関係
        if(item.getItem() instanceof IAlchemyProduct){
            float op=event.getOldPrice();
            ArrayList<CharacteristicBase> list= AlchemyRegistry.ReadCharacteristicFromNBT(item.getTagCompound());
            for(CharacteristicBase cb : list){
                op*=cb.getMPScale();
            }
            event.setNewPrice((int)op);
        }
    }

    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event){

    }

    @SubscribeEvent
    public void onItemPickUp(EntityItemPickupEvent event){
        EntityPlayer player=event.entityPlayer;
        EntityItem eItem=event.item;
        ItemStack picked=eItem.getEntityItem().copy();
        Item item=picked.getItem();

        if(!player.worldObj.isRemote){
            if(item==ItemCore.bookNoDecoded) player.triggerAchievement(AchievementRegistry.book);
            else if(item==ItemCore.butterfly) player.triggerAchievement(AchievementRegistry.butterfly);
            else if(item==ItemCore.alchemyMaterial && picked.getItemDamage()==40) player.triggerAchievement(AchievementRegistry.tear);
            else if(item==ItemCore.alchemyMaterial && picked.getItemDamage()==36) player.triggerAchievement(AchievementRegistry.herbGold);
        }


        if(picked.getItem() instanceof IAlchemyMaterial || picked.getItem() instanceof IAlchemyProduct || AlchemyRegistry.isBasketItem(picked)){
            //カゴを探す
            boolean flag=false;
            int size=player.inventory.getSizeInventory();
            int stacksize=picked.stackSize;
            for(int i=0;i<size && !flag;i++){
                ItemStack is=player.inventory.getStackInSlot(i);
                if(is==null) continue;
                if(is.getItem()!=ItemCore.basket) continue;

                //インベントリがあいてるか調べる
                InventoryBasket ib=new InventoryBasket(player.inventory, i);
                ib.openInventory();
                int size2=ib.getSizeInventory();
                for(int k=0;k<size2;k++){
                    ItemStack is2=ib.getStackInSlot(k);
                    if(is2==null){
                        ib.setInventorySlotContents(k, picked.copy());
                        flag=true;
                    }
                    else if(picked.isItemEqual(is2) && ItemStack.areItemStackTagsEqual(picked, is2) && is2.stackSize<is2.getMaxStackSize()){
                        int t=is2.getMaxStackSize()-is2.stackSize;
                        is2.stackSize+=Math.min(t, picked.stackSize);
                        picked.stackSize-=t;
                        if(picked.stackSize<=0){
                            flag=true;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
                ib.closeInventory();
            }

            if(flag){
                //全部拾えたらEntityItemを消す
                player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                eItem.setDead();
                event.setCanceled(true);
                return;
            }
            else if(picked.stackSize>0 && stacksize!=picked.stackSize){
                player.onItemPickup(eItem, stacksize-picked.stackSize);
            }
        }

        //MPCoinを拾った場合、即座にMPに変換
        if(item!=null && item instanceof ItemMPCoin){
            int v=picked.getItemDamage();
            if(v>0 && !player.worldObj.isRemote){
                MCEconomyAPI.addPlayerMP(player, v, false);
                MCEconomyAPI.playCoinSoundEffect(player.worldObj, MathHelper.floor_double(eItem.posX), MathHelper.floor_double(eItem.posY), MathHelper.floor_double(eItem.posZ));
            }
            picked.stackSize=0;
            eItem.setDead();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack stack=event.crafting;
        Item item=stack.getItem();

        if(item==Item.getItemFromBlock(BlockCore.schoolTable)) player.triggerAchievement(AchievementRegistry.ga);
        //else if(item==ItemCore.monocle) player.triggerAchievement(AchievementRegistry.monocle);
        else if(item==Item.getItemFromBlock(BlockCore.workbench)) player.triggerAchievement(AchievementRegistry.workbench);
        else if(item==ItemCore.membership) player.triggerAchievement(AchievementRegistry.witch);
        else if(item==Item.getItemFromBlock(BlockCore.tableAlchemist)) player.triggerAchievement(AchievementRegistry.beginner);
        else if(item==Item.getItemFromBlock(BlockCore.alchemyCauldron)) player.triggerAchievement(AchievementRegistry.atelier);
        else if(item==Item.getItemFromBlock(BlockCore.pottersWheel)) player.triggerAchievement(AchievementRegistry.potter);
    }

    @SubscribeEvent
    public void onLivingDropEvent(LivingDropsEvent event){
        if(!event.entity.worldObj.isRemote){
            if(event.entity instanceof EntityWitch && rand.nextInt(5)==1){
                EntityItem entityItem=new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ);
                entityItem.setEntityItemStack(new ItemStack(ItemCore.membership));
                event.drops.add(entityItem);
            }

            if(event.entity instanceof IMob && rand.nextInt(2)==1){
                EntityItem entityItem=new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ);
                ItemStack drop;
                switch (rand.nextInt(5)){
                    case 0: drop=new ItemStack(ItemCore.grassUnknown); break;
                    case 1: drop=new ItemStack(ItemCore.flowerUnknown); break;
                    case 2: drop=new ItemStack(ItemCore.fruitsUnknown); break;
                    case 3: drop=new ItemStack(ItemCore.herbUnknown); break;
                    case 4: drop=new ItemStack(ItemCore.mushroomUnknown); break;
                    default: drop=new ItemStack(ItemCore.seedsUnknown); break;
                }
                drop.stackSize=1+rand.nextInt(3);
                entityItem.setEntityItemStack(drop);
                event.drops.add(entityItem);
            }
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event){
        if(event.entityPlayer==null || event.entityPlayer.worldObj.isRemote) return;

        ItemStack itemStack=event.item.getEntityItem();
        if(itemStack==null) return;

        if(itemStack.getItem()==ItemCore.membership) event.entityPlayer.triggerAchievement(AchievementRegistry.witch);
    }

    @SubscribeEvent
    public void onPlayerSanityRoll(PlayerSanityRollEvent event){
        EntityPlayer ep=event.entityPlayer;
        if(!ep.worldObj.isRemote){
            //----------------------金のモノクルによる正気度保護---------------------------
            ItemStack helm=ep.getCurrentArmor(3);
            if(helm!=null && helm.getItem()==ItemCore.monocleGold && event.getMax()<0){
                event.newMax=(event.getMax()-1)/2;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerSanity(PlayerSanityEvent event){
        EntityPlayer ep=event.entityPlayer;
        if(!ep.worldObj.isRemote && event.newChangeSanity<0){
            //--------------------エンチャントによる正気度保護-----------------------------
            int lvSum= EnchantmentSanityProtect.getSum(ep); //最大20
            int thMax=lvSum/2; //閾値。これを下回る場合は0になる
            float prot=0.02f*lvSum;//軽減率

            if(-event.newChangeSanity<thMax) event.newChangeSanity=0;
            else event.newChangeSanity=MathHelper.floor_float(event.newChangeSanity * (1.f - prot));
        }
    }
}
