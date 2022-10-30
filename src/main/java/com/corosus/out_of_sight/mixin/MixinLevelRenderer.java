package com.corosus.out_of_sight.mixin;

import com.corosus.out_of_sight.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer
{
    @Shadow
    @Final
    private Minecraft                         minecraft;
    private ChunkRenderDispatcher.RenderChunk current = null;

    @Redirect(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk;getCompiledChunk()Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;"))
    public ChunkRenderDispatcher.CompiledChunk on(final ChunkRenderDispatcher.RenderChunk instance)
    {
        current = instance;
        return instance.getCompiledChunk();
    }

    @Redirect(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;isEmpty(Lnet/minecraft/client/renderer/RenderType;)Z"))
    public boolean isEmpty(final ChunkRenderDispatcher.CompiledChunk instance, final RenderType type)
    {
        if(Config.GENERAL.chunkRenderLimit.get() && minecraft.cameraEntity != null){
            double hor = (double)minecraft.options.renderDistance().get() * 16;
            double ver = Config.GENERAL.chunkRenderVer.get();
            var entity = minecraft.cameraEntity.position();
            var x = entity.x;
            var y = entity.y;
            var z = entity.z;
            var qx = current.getOrigin().getX() + 8 - x;
            var qy = current.getOrigin().getY() + 8 - y;
            var qz = current.getOrigin().getZ() + 8 - z;
            double gap = qx * qx / hor / hor + qy * qy / ver / ver + qz * qz / hor / hor;
            if(gap>1) return true;
        }
        return instance.isEmpty(type);
    }
}
