package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.OutOfSight;
import com.corosus.out_of_sight.config.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinTileEntityRendererDispatcher {

    @Shadow
    public ActiveRenderInfo renderInfo;

    @Overwrite
    public <E extends TileEntity> void renderTileEntity(E tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        double dist = getDistanceSq(tileEntityIn, this.renderInfo.getProjectedView().x, this.renderInfo.getProjectedView().y, this.renderInfo.getProjectedView().z);
        if (dist > Config.GENERAL.tileEntityRenderRangeMax.get() * Config.GENERAL.tileEntityRenderRangeMax.get()) {
            if (!Config.GENERAL.tileEntityRenderLimitModdedOnly.get() || !OutOfSight.getCanonicalNameCached(tileEntityIn.getClass()).startsWith("net.minecraft")) {
                return;
            }
        }
        TileEntityRenderer<E> tileentityrenderer = net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.instance.getRenderer(tileEntityIn);
        if (tileentityrenderer != null) {
            if (tileEntityIn.hasWorld() && tileEntityIn.getType().isValidBlock(tileEntityIn.getBlockState().getBlock())) {
                TileEntityRendererDispatcher.runCrashReportable(tileEntityIn, () -> {
                    TileEntityRendererDispatcher.render(tileentityrenderer, tileEntityIn, partialTicks, matrixStackIn, bufferIn);
                });
            }
        }
    }

    public double getDistanceSq(TileEntity tileEntity, double x, double y, double z) {
        double d0 = (double)tileEntity.getPos().getX() + 0.5D - x;
        double d1 = (double)tileEntity.getPos().getY() + 0.5D - y;
        double d2 = (double)tileEntity.getPos().getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }
}