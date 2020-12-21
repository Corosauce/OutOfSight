package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRendererManager.class)
public abstract class MixinEntityRenderer {

    @Redirect(method = "shouldRender",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z"))
    public <T extends Entity> boolean shouldRender(EntityRenderer<Entity> entityrenderer, T livingEntityIn, ICamera camera, double camX, double camY, double camZ) {
        if (!isInRangeToRender3d(livingEntityIn, camX, camY, camZ)) {
            return false;
        }
        return entityrenderer.shouldRender(livingEntityIn, camera, camX, camY, camZ);
    }

    public <T extends Entity> boolean isInRangeToRender3d(T livingEntityIn, double x, double y, double z) {
        double d0 = livingEntityIn.posX - x;
        double d1 = livingEntityIn.posY - y;
        double d2 = livingEntityIn.posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (d3 > Config.GENERAL.entityRenderRangeMax.get() * Config.GENERAL.entityRenderRangeMax.get()) {
            if (!Config.GENERAL.entityRenderLimitModdedOnly.get() || !OutOfSight.getCanonicalNameCached(livingEntityIn.getClass()).startsWith("net.minecraft")) {
                return false;
            }
        }
        return true;
    }
}