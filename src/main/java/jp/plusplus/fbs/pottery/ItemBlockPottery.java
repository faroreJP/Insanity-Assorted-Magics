package jp.plusplus.fbs.pottery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.pottery.usable.PotteryBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/08/26.
 * 陶芸品用
 */
public class ItemBlockPottery extends ItemBlock {
    protected IPottery pottery;

    public ItemBlockPottery(Block p_i45328_1_) {
        super(p_i45328_1_);
        setMaxDamage(0);
        setHasSubtypes(true);
        setMaxStackSize(1);

        pottery=(IPottery)p_i45328_1_;
    }

    public static void setId(ItemStack itemStack, int id){
        if(!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt=itemStack.getTagCompound();
        nbt.setInteger(BlockPotteryBase.EFFECT_ID, id);
    }

    public static int getId(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getInteger(BlockPotteryBase.EFFECT_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }

    @Override
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        BlockPotteryBase bpb=(BlockPotteryBase)field_150939_a;
        NBTTagCompound nbt=itemStack.getTagCompound();
        if(nbt==null) return bpb.getLocalizedName();

        if(pottery.hasEffect(nbt) && pottery.getState(nbt)==IPottery.PotteryState.BAKED){
            PotteryBase pb=PotteryRegistry.getPotteryEffect(getId(itemStack));
            return bpb.getLocalizedName(bpb.getNBT(itemStack))+StatCollector.translateToLocal(pb.getUnlocalizedName())+pb.getNameModifier(itemStack);
        }
        return bpb.getLocalizedName(bpb.getNBT(itemStack))+bpb.getLocalizedName();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        NBTTagCompound nbt=item.getTagCompound();

        //魔法の壺の場合
        if(pottery.hasEffect(nbt) && pottery.getState(nbt)==IPottery.PotteryState.BAKED){
            PotteryBase pb=PotteryRegistry.getPotteryEffect(getId(item));
            if (pb.canUse(player, item)) {
                return pb.onUse(player, item);
            }
            return item;
        }

        return super.onItemRightClick(item, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        //魔法の壺は置けない
        NBTTagCompound nbt=itemStack.getTagCompound();
        if(pottery.hasEffect(nbt) && pottery.getState(nbt)==IPottery.PotteryState.BAKED){
            return false;
        }
        return super.onItemUse(itemStack, player, world, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        //魔法の壺は置けない
        NBTTagCompound nbt=stack.getTagCompound();
        if(pottery.hasEffect(nbt) && pottery.getState(nbt)==IPottery.PotteryState.BAKED){
            return false;
        }
        else{
            boolean flag=super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
            if(flag){
                TileEntity entity=world.getTileEntity(x,y,z);
                if(entity instanceof TileEntityPottery){
                    TileEntityPottery te=(TileEntityPottery)entity;
                    te.setData(stack);
                    te.markDirty();
                    //((TileEntityPottery) te).potteryMetadata=metadata;
                }
            }
            return flag;
        }
    }
}
