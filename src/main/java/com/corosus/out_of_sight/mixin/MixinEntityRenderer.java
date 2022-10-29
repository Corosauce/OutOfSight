package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderer {

    @Redirect(method = "shouldRender",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"))
    public <T extends Entity> boolean shouldRender(EntityRenderer<? super T> entityrenderer, T livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (!isInRangeToRender3d(livingEntityIn, camX, camY, camZ)) {
            return false;
        }
        return entityrenderer.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    public <T extends Entity> boolean isInRangeToRender3d(T livingEntityIn, double x, double y, double z) {
        if(Config.GENERAL.entityRenderLimit.get()){
            double hor = Config.GENERAL.entityRenderHor.get();
            double ver = Config.GENERAL.entityRenderVer.get();
            var entity = livingEntityIn;
            var qx = entity.getX() - x;
            var qy = entity.getY() - y;
            var qz = entity.getZ() - z;
            double gap = qx * qx / hor / hor + qy * qy / ver / ver + qz * qz / hor / hor;
            if(gap>1 || !OutOfSight.getCanonicalNameCached(livingEntityIn.getClass()).startsWith("net.minecraft")) return false;
        }
        return true;
    }
}