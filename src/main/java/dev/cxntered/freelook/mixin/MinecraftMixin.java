package dev.cxntered.freelook.mixin;

import dev.cxntered.freelook.config.Config;
import dev.cxntered.freelook.Freelook;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Unique
    public final Config freelook$config = Freelook.INSTANCE.getConfig();

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I"))
    private void thirdPerson(CallbackInfo ci) {
        if (Freelook.INSTANCE.getFreelookToggled()) {
            Minecraft mc = Minecraft.getMinecraft();
            boolean freelookHold = freelook$config.getMode() == 0;
            boolean freelookCustomFov = freelook$config.getCustomFov();
            float lastFov = Freelook.INSTANCE.getLastFov().floatValue();

            Freelook.INSTANCE.setFreelookToggled(false);
            mc.gameSettings.thirdPersonView = 2;
            mc.renderGlobal.setDisplayListEntitiesDirty();
            if (freelookHold || freelookCustomFov) {
                mc.gameSettings.fovSetting = lastFov;
            }
        }
    }
}
