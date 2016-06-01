package jp.plusplus.fbs.pottery;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.pottery.model.ModelJarLarge;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by plusplus_F on 2015/08/26.
 */
public abstract class BlockJar extends BlockPotteryBase {
    public static final ResourceLocation rlSmall =new ResourceLocation(FBS.MODID+":textures/models/pot00.png");
    public static final ResourceLocation rlMedium =new ResourceLocation(FBS.MODID+":textures/models/pot00.png");
    public static final ResourceLocation rlLarge=new ResourceLocation(FBS.MODID+":textures/models/pot00.png");
    public static final ModelJarLarge mjLarge=new ModelJarLarge();

    public BlockJar(int value) {
        super("jar", value);
    }


    @Override
    public ResourceLocation getResourceLocation(int metadata) {
        metadata=((metadata>>12)&0xf);
        if(metadata==0) return rlSmall;
        else if(metadata==1) return rlMedium;
        else return rlLarge;
    }

    @Override
    public ModelBase getModel(int metadata) {
        metadata=((metadata>>8)&0xf);
        if(metadata==0) return mjLarge;
        else if(metadata==1) return mjLarge;
        else return mjLarge;
    }
}
