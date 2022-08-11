package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(BlockEntityRenderer.class)
public interface MixinBlockEntityViewDistance<T extends BlockEntity> {

    /**
     * @author
     * @reason
     */
    @Overwrite
    default int getViewDistance() {
        if (!OutOfSight.getCanonicalNameCached(this.getClass()).startsWith("net.minecraft")) {
            return Config.GENERAL.tileEntityRenderRangeMax.get().intValue();
        } else {
            return 64;
        }
    }
}