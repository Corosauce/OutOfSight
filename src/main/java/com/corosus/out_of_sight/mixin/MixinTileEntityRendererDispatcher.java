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
        if(Config.GENERAL.tileEntityRenderLimit.get()){
            double hor = Config.GENERAL.tileEntityRenderHor.get();
            double ver = Config.GENERAL.tileEntityRenderVer.get();
            var entity = tileEntityIn;
            var x = dispatcher.camera.getPosition().x;
            var y = dispatcher.camera.getPosition().y;
            var z = dispatcher.camera.getPosition().z;
            var qx = (double)entity.getBlockPos().getX() + 0.5D - x;
            var qy = (double)entity.getBlockPos().getY() + 0.5D - y;
            var qz = (double)entity.getBlockPos().getZ() + 0.5D - z;
            double gap = qx * qx / hor / hor + qy * qy / ver / ver + qz * qz / hor / hor;
            if(gap>1 || !OutOfSight.getCanonicalNameCached(entity.getClass()).startsWith("net.minecraft")) return;
        }
        dispatcher.render(tileEntityIn, partialTicks, matrixStackIn, bufferIn);
    }
}