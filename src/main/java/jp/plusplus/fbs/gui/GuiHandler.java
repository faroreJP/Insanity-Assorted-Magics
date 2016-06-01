package jp.plusplus.fbs.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.container.*;
import jp.plusplus.fbs.container.inventory.InventoryMagicCopy;
import jp.plusplus.fbs.container.inventory.InventoryMagicTimeTrace;
import jp.plusplus.fbs.container.spirit.ContainerSpiritLearn;
import jp.plusplus.fbs.container.spirit.ContainerSpiritMain;
import jp.plusplus.fbs.entity.EntityMagicAuthor;
import jp.plusplus.fbs.event.wish.ContainerWish;
import jp.plusplus.fbs.event.wish.GuiWish;
import jp.plusplus.fbs.gui.spirit.GuiSpiritConfig;
import jp.plusplus.fbs.gui.spirit.GuiSpiritLearn;
import jp.plusplus.fbs.gui.spirit.GuiSpiritMain;
import jp.plusplus.fbs.gui.spirit.GuiSpiritSkill;
import jp.plusplus.fbs.pottery.GuiKiln;
import jp.plusplus.fbs.pottery.GuiPottersWheel;
import jp.plusplus.fbs.pottery.TileEntityKiln;
import jp.plusplus.fbs.pottery.TileEntityPottersWheel;
import jp.plusplus.fbs.pottery.usable.container.ContainerPotteryUsableBase;
import jp.plusplus.fbs.pottery.usable.container.GuiPotteryUsableBase;
import jp.plusplus.fbs.storage.ContainerMealTerminal;
import jp.plusplus.fbs.storage.GuiMealTerminal;
import jp.plusplus.fbs.storage.TileEntityMealTerminal;
import jp.plusplus.fbs.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.mceconomy2.gui.GuiShop;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if(ID==FBS.GUI_STAFF_ID){
            return new ContainerStaff(player.inventory);
        }
        if(ID==FBS.GUI_MAGIC_COPY_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new ContainerMagic(player.inventory, new InventoryMagicCopy((TileEntityMagicCore)te, player));
            }
        }
        if(ID==FBS.GUI_MAGIC_TIME_TRACE_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new ContainerMagic(player.inventory, new InventoryMagicTimeTrace((TileEntityMagicCore)te, player));
            }
        }
        if(ID==FBS.GUI_ENCHANTMENT_ID){
            return new ContainerTFKEnchantment(player);
        }
        if(ID==FBS.GUI_MAGIC_WARP_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new ContainerWarp(player);
            }
        }
        if(ID==FBS.GUI_MAGIC_CONTRACT_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new ContainerContract(player);
            }
        }
        if(ID==FBS.GUI_SPIRIT_MAIN_ID){
            return new ContainerSpiritMain(player, 0);
        }
        if(ID==FBS.GUI_BASKET_ID){
            return new ContainerBasket(player.inventory);
        }
        if(ID==FBS.GUI_SPIRIT_CONFIG_ID){
            return new ContainerSpiritMain(player, 1);
        }
        if(ID==FBS.GUI_SPIRIT_LEARN_ID){
            return new ContainerSpiritLearn(player);
        }
        if(ID==FBS.GUI_SPIRIT_SKILL_ID){
            return new ContainerSpiritLearn(player);
        }
        if(ID==FBS.GUI_SHOP_AUTHOR_ID){
            return new ContainerShopAuthor(player, MCEconomyAPI.getShop(Registry.shopAuthorId), world, null);
        }
        if(ID==FBS.GUI_MAGIC_POT_ID){
            return new ContainerPotteryUsableBase(player);
        }
        if(ID==FBS.GUI_WISH_ID){
            return new ContainerWish();
        }

        //-------------------------------------------------------
        if(!world.blockExists(x,y,z))    return null;

        Block b=world.getBlock(x,y,z);
        if(b== BlockCore.bonfire){
            return new ContainerBonfire(x,y,z, player);
        }

        TileEntity e=world.getTileEntity(x,y,z);
        if(e instanceof TileEntityExtractingFurnace){
            return new ContainerExtractingFurnace(player, (TileEntityExtractingFurnace)e);
        }
        if(e instanceof TileEntityFillingTable){
            return new ContainerFillingTable(player, (TileEntityFillingTable)e);
        }
        if(e instanceof TileEntityFBSWorkbench){
            return new ContainerFBSWorkbench(player, (TileEntityFBSWorkbench)e);
        }
        if(e instanceof TileEntityPottersWheel){
            return new ContainerPottersWheel(player, (TileEntityPottersWheel)e);
        }
        if(e instanceof TileEntityKiln){
            return new ContainerKiln(player, (TileEntityKiln)e);
        }
        if(e instanceof TileEntityAlchemyCauldron){
            return new ContainerAlchemyCauldron(player, (TileEntityAlchemyCauldron)e);
        }
        if(e instanceof TileEntityMealTerminal){
            return new ContainerMealTerminal(player, (TileEntityMealTerminal)e);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if(ID== FBS.GUI_STAFF_ID){
            return new GuiStaff(player.inventory);
        }
        if(ID==FBS.GUI_MAGIC_COPY_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new GuiMagic(new ContainerMagic(player.inventory, new InventoryMagicCopy((TileEntityMagicCore)te, player)));
            }
        }
        if(ID==FBS.GUI_MAGIC_TIME_TRACE_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new GuiMagic(new ContainerMagic(player.inventory, new InventoryMagicTimeTrace((TileEntityMagicCore)te, player)));
            }
        }
        if(ID==FBS.GUI_ENCHANTMENT_ID){
            return new GuiEnchantment(new ContainerTFKEnchantment(player));
        }
        if(ID==FBS.GUI_MAGIC_WARP_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new GuiWarp(new ContainerWarp(player));
            }
        }
        if(ID==FBS.GUI_MAGIC_CONTRACT_ID){
            TileEntity te=world.getTileEntity(x,y,z);
            if(te instanceof TileEntityMagicCore){
                return new GuiContract(new ContainerContract(player));
            }
        }
        if(ID==FBS.GUI_SPIRIT_MAIN_ID){
            return new GuiSpiritMain(player);
        }
        if(ID==FBS.GUI_BASKET_ID){
            return new GuiBasket(player.inventory);
        }
        if(ID==FBS.GUI_SPIRIT_CONFIG_ID){
            return new GuiSpiritConfig(player);
        }
        if(ID==FBS.GUI_SPIRIT_LEARN_ID){
            return new GuiSpiritLearn(player);
        }
        if(ID==FBS.GUI_SPIRIT_SKILL_ID){
            return new GuiSpiritSkill(player);
        }
        if(ID==FBS.GUI_SHOP_AUTHOR_ID){
            List list=world.getEntitiesWithinAABB(EntityMagicAuthor.class, AxisAlignedBB.getBoundingBox(x,y,z,x+1,y+1,z+1).expand(1,1,1));
            for(int i=0;i<list.size();i++){
                EntityMagicAuthor e=(EntityMagicAuthor)list.get(i);
                if(true || e.getCustomer()==player){
                    GuiShop gui=new GuiShop(player, MCEconomyAPI.getShop(Registry.shopAuthorId), Registry.shopAuthorId, world);
                    gui.inventorySlots=new ContainerShopAuthor(player, MCEconomyAPI.getShop(Registry.shopAuthorId), world, null);
                    return gui;
                }
            }
            return null;
        }
        if(ID==FBS.GUI_MAGIC_POT_ID){
            return new GuiPotteryUsableBase(new ContainerPotteryUsableBase(player));
        }
        if(ID==FBS.GUI_WISH_ID){
            return new GuiWish();
        }

        //-------------------------------------------------------
        if(!world.blockExists(x,y,z))    return null;

        Block b=world.getBlock(x,y,z);
        if(b== BlockCore.bonfire){
            return new GuiBonfire(new ContainerBonfire(x,y,z, player));
        }

        TileEntity e=world.getTileEntity(x,y,z);
        if(e instanceof TileEntityExtractingFurnace){
            return new GuiExtractingFurnace(new ContainerExtractingFurnace(player, (TileEntityExtractingFurnace)e), (TileEntityExtractingFurnace)e);
        }
        if(e instanceof TileEntityFillingTable){
            return new GuiFillingTable(new ContainerFillingTable(player, (TileEntityFillingTable)e), (TileEntityFillingTable)e);
        }
        if(e instanceof TileEntityFBSWorkbench){
            return new GuiWorkbench(new ContainerFBSWorkbench(player, (TileEntityFBSWorkbench)e), (TileEntityFBSWorkbench)e);
        }
        if(e instanceof TileEntityPottersWheel){
            return new GuiPottersWheel(new ContainerPottersWheel(player, (TileEntityPottersWheel)e), (TileEntityPottersWheel)e);
        }
        if(e instanceof TileEntityKiln){
            return new GuiKiln(new ContainerKiln(player, (TileEntityKiln)e), (TileEntityKiln)e);
        }
        if(e instanceof TileEntityAlchemyCauldron){
            return new GuiAlchemyCauldron(new ContainerAlchemyCauldron(player, (TileEntityAlchemyCauldron)e), (TileEntityAlchemyCauldron)e);
        }
        if(e instanceof TileEntityMealTerminal){
            return new GuiMealTerminal(new ContainerMealTerminal(player, (TileEntityMealTerminal)e));
        }

        return null;
    }
}
