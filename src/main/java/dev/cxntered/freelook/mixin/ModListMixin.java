package dev.cxntered.freelook.mixin;

import dev.cxntered.freelook.Freelook;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = FMLHandshakeMessage.ModList.class)
public abstract class ModListMixin {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void spoofModTag(List<ModContainer> modList, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) return;
        if (Freelook.INSTANCE.isHypixel() || Freelook.INSTANCE.getConfig().getSnaplook()) {
            modTags.remove(Freelook.MOD_ID);
            modTags.put(Freelook.ALT_MOD_ID, Freelook.VERSION);
        }
    }
}
