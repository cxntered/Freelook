package dev.cxntered.freelook.mixin.render;

import dev.cxntered.freelook.Freelook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityRenderer.class, priority = Integer.MAX_VALUE)
public abstract class EntityRendererMixin {
    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F"))
    private float modifyPitch(Entity entity) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraPitch() : entity.rotationPitch;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F"))
    private float modifyYaw(Entity entity) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraYaw() : entity.rotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F"))
    private float modifyPrevPitch(Entity entity) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraPitch() : entity.prevRotationPitch;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F"))
    private float modifyPrevYaw(Entity entity) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraYaw() : entity.prevRotationYaw;
    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;inGameHasFocus:Z"))
    private boolean overrideMouse(Minecraft minecraft) {
        return Freelook.INSTANCE.overrideMouse();
    }
}
