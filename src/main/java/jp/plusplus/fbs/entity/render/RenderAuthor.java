package jp.plusplus.fbs.entity.render;

import cpw.mods.fml.common.registry.VillagerRegistry;
import jp.plusplus.fbs.FBS;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class RenderAuthor extends RenderVillager {
    private static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/entity/author.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityVillager p_110775_1_) {
        return rl;
    }
}
