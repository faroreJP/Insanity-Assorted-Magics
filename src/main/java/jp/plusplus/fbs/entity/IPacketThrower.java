package jp.plusplus.fbs.entity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by plusplus_F on 2016/04/03.
 */
public interface IPacketThrower {
    public void writeToPacketNBT(NBTTagCompound nbt);
    public void readFromPacketNBT(NBTTagCompound nbt);
}
