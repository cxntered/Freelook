package dev.cxntered.freelook.mixin.render;

import dev.cxntered.freelook.Freelook;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderManager.class)
public abstract class RenderManagerMixin {
    @Redirect(method = "cacheActiveRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewX:F"))
    private void playerViewX(RenderManager renderManager, float value) {
        renderManager.playerViewX = Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraPitch() : value;
    }

    @Redirect(method = "cacheActiveRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewY:F"))
    private void playerViewY(RenderManager renderManager, float value) {
        renderManager.playerViewY = Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraYaw() : value;
    }
}
