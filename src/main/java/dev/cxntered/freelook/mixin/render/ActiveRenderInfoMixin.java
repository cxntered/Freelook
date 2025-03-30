package dev.cxntered.freelook.mixin.render;

import dev.cxntered.freelook.Freelook;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ActiveRenderInfo.class)
public abstract class ActiveRenderInfoMixin {
    @Redirect(method = "updateRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationPitch:F"))
    private static float modifyPitch(EntityPlayer player) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraPitch() : player.rotationPitch;
    }

    @Redirect(method = "updateRenderInfo", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationYaw:F"))
    private static float modifyYaw(EntityPlayer player) {
        return Freelook.INSTANCE.getFreelookToggled() ? Freelook.INSTANCE.getCameraYaw() : player.rotationYaw;
    }
}
