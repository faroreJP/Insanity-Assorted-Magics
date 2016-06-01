package jp.plusplus.fbs.spirit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/11/02.
 */
public class ItemSwordSpirit extends ItemBase implements ISpiritTool {
    public ItemSwordSpirit() {
        setCreativeTab(null);
        setUnlocalizedName("spiritSword");
        setTextureName("spiritSword");
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
        if(player instanceof EntityPlayer){
            SpiritStatus ss=SpiritStatus.readFromNBT(itemStack.getTagCompound());

            int dur=calcDurable(ss);
            ss.addItemDamage(1);
            if(ss.getItemDamage()>dur){
                ss.setItemDamage(dur);
            }
            if(ss.getItemDamage()==dur){
                ((EntityPlayer) player).triggerAchievement(AchievementRegistry.evil);
            }
            if(ss.getItemDamage()<dur && ss.isOwner((EntityPlayer)player) && ss.addExp(1)){
                SpiritManager.talk((EntityPlayer)player, ss.getCharacter(), "lvup", itemStack);
                SanityManager.setSpiritLevel((EntityPlayer)player, ss.getLv());

                if(!((EntityPlayer) player).worldObj.isRemote && ss.getLv()==50) ((EntityPlayer) player).triggerAchievement(AchievementRegistry.best);
            }
            SpiritManager.updateNBT(itemStack, ss);
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase p_150894_7_) {
        if ((double) block.getBlockHardness(world, x, y, z) != 0.0D) {
            SpiritStatus ss=SpiritStatus.readFromNBT(itemStack.getTagCompound());

            ss.addItemDamage(2);
            SpiritManager.updateNBT(itemStack, ss);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()){
            SpiritStatus status=SpiritStatus.readFromNBT(itemStack.getTagCompound());
            if(status.getOwnerName().equals(player.getDisplayName())){
                SpiritManager.openGui(player);
            }
        }
        else{
            player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        }

        return itemStack;
    }

    @Override
    public boolean func_150897_b(Block p_150897_1_) {
        return p_150897_1_ == Blocks.web;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        SpiritManager.addInformation(itemStack, player, list, flag);
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return SpiritManager.getSpiritDisplayName(p_77653_1_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        SpiritStatus ss=SpiritStatus.readFromNBT(stack.getTagCompound());
        return (double) ss.getItemDamage() / calcDurable(ss);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        if(!stack.hasTagCompound()) return false;
        return SpiritStatus.readFromNBT(stack.getTagCompound()).getItemDamage()>0;
        //return true;
    }
    @Override
    public boolean isItemTool(ItemStack p_77616_1_) {
        return true;
    }
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return isDamaged(stack);
    }

    //--------------------------------------------------------------------------------
    @Override
    public float calcDamage(SpiritStatus ss) {
        if(ss.getItemDamage()>=calcDurable(ss)) return 0.5f;
        return 3.5f+ss.getMaxDamage()*ss.calcRatio(ss.getStrength());
    }

    @Override
    public int calcDigLv(SpiritStatus ss) {
        return 0;
    }

    @Override
    public int calcDurable(SpiritStatus ss) {
        return 1000+(int)(ss.getMaxDurability()*ss.calcRatio(ss.getToughness()));
    }

    @Override
    public void calcInitialValue(SpiritStatus ss, ItemStack material){
        ItemSword sword=(ItemSword)material.getItem();
        ToolMaterial tm=ToolMaterial.valueOf(sword.getToolMaterialName());

        //最大攻撃力は武器の攻撃力の10倍
        float maxDamage=(tm.getDamageVsEntity())*10;

        //最大耐久力は武器の耐久*10+10000
        float maxDurabity=tm.getMaxUses()*10+10000;

        ss.setMaxStatus(maxDamage, 0, maxDurabity);
        ss.addStrength(MathHelper.ceiling_float_int(tm.getDamageVsEntity() / (ss.getMaxDamage() / 255.f)));
        ss.addToughness(MathHelper.ceiling_float_int(tm.getMaxUses()/(ss.getMaxDurability()/255.f)));
    }
}
