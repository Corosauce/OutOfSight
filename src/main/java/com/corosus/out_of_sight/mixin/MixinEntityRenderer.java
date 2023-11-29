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
        double d0 = livingEntityIn.getX() - x;
        double d1 = livingEntityIn.getY() - y;
        double d2 = livingEntityIn.getZ() - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (d3 > Config.GENERAL.entityRenderRangeMax.get() * Config.GENERAL.entityRenderRangeMax.get()) {
            String clazzName = OutOfSight.getCanonicalNameCached(livingEntityIn.getClass());
            if (!Config.GENERAL.entityRenderLimitModdedOnly.get()) {
                return false;
            }
            if (clazzName != null && !clazzName.startsWith("net.minecraft")) {
                return false;
            }
        }
        return true;
    }
}