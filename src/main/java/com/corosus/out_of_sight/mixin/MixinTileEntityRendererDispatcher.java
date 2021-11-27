package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class MixinTileEntityRendererDispatcher {

    @Redirect(method = "renderLevel",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    public <E extends BlockEntity> void renderTileEntity(BlockEntityRenderDispatcher dispatcher, E tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
        double dist = getDistanceSq(tileEntityIn, dispatcher.camera.getPosition().x, dispatcher.camera.getPosition().y, dispatcher.camera.getPosition().z);
        if (dist > Config.GENERAL.tileEntityRenderRangeMax.get() * Config.GENERAL.tileEntityRenderRangeMax.get()) {
            if (!Config.GENERAL.tileEntityRenderLimitModdedOnly.get() || !OutOfSight.getCanonicalNameCached(tileEntityIn.getClass()).startsWith("net.minecraft")) {
                return;
            }
        }
        dispatcher.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn);
    }

    public double getDistanceSq(BlockEntity tileEntity, double x, double y, double z) {
        double d0 = (double)tileEntity.getBlockPos().getX() + 0.5D - x;
        double d1 = (double)tileEntity.getBlockPos().getY() + 0.5D - y;
        double d2 = (double)tileEntity.getBlockPos().getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }
}