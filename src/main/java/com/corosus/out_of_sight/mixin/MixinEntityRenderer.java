package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {

    @Overwrite
    public boolean shouldRender(T livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        if (!isInRangeToRender3d(livingEntityIn, camX, camY, camZ)) {
            return false;
        } else if (livingEntityIn.ignoreFrustumCheck) {
            return true;
        } else {
            AxisAlignedBB axisalignedbb = livingEntityIn.getRenderBoundingBox().grow(0.5D);
            if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0D) {
                axisalignedbb = new AxisAlignedBB(livingEntityIn.getPosX() - 2.0D, livingEntityIn.getPosY() - 2.0D, livingEntityIn.getPosZ() - 2.0D, livingEntityIn.getPosX() + 2.0D, livingEntityIn.getPosY() + 2.0D, livingEntityIn.getPosZ() + 2.0D);
            }

            return camera.isBoundingBoxInFrustum(axisalignedbb);
        }
    }

    public boolean isInRangeToRender3d(T livingEntityIn, double x, double y, double z) {
        double d0 = livingEntityIn.getPosX() - x;
        double d1 = livingEntityIn.getPosY() - y;
        double d2 = livingEntityIn.getPosZ() - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (d3 > Config.GENERAL.entityRenderRangeMax.get() * Config.GENERAL.entityRenderRangeMax.get()) {
            if (!Config.GENERAL.entityRenderLimitModdedOnly.get() || !OutOfSight.getCanonicalNameCached(livingEntityIn.getClass()).startsWith("net.minecraft")) {
                return false;
            }
        }
        return livingEntityIn.isInRangeToRenderDist(d3);
    }
}