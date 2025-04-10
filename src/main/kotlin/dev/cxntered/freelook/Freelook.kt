package dev.cxntered.freelook

import dev.cxntered.freelook.command.FreelookCommand
import dev.cxntered.freelook.config.Config
import gg.essential.api.EssentialAPI
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import kotlin.math.pow

@Mod(modid = Freelook.MOD_ID, useMetadata = true, modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter")
object Freelook {
    const val MOD_ID = "freelook"
    const val ALT_MOD_ID = "snaplook"
    const val VERSION = "1.1.1"

    private lateinit var mc: Minecraft
    private val keybind = KeyBinding("Freelook", Keyboard.KEY_LMENU, "Freelook")

    var freelookToggled = false
    private var previousState = false

    var cameraYaw = 0f
    var cameraPitch = 0f
    lateinit var lastFov: Number

    private var previousPerspective = 0

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        mc = Minecraft.getMinecraft()
        Config.preload()
        lastFov = Config.fov

        MinecraftForge.EVENT_BUS.register(this)
        EssentialAPI.getCommandRegistry().registerCommand(FreelookCommand)
        ClientRegistry.registerKeyBinding(this.keybind)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.RenderTickEvent) {
        val down = keybind.isKeyDown
        if (down != previousState && mc.currentScreen == null && mc.theWorld != null && mc.thePlayer != null) {
            previousState = down
            onPressed(down)
        }
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (event.gui != null && freelookToggled && Config.mode == 0) {
            resetPerspective()
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        if (freelookToggled) {
            resetPerspective()
        }
    }

    fun isHypixel(): Boolean {
        if (!::mc.isInitialized || mc.thePlayer == null || mc.theWorld == null || mc.isSingleplayer) return false
        val serverBrand = mc.thePlayer.clientBrand ?: return false
        return serverBrand.lowercase().contains("hypixel")
    }

    private fun onPressed(state: Boolean) {
        if (Config.enabled) {
            if (state) {
                cameraYaw = mc.thePlayer.rotationYaw
                cameraPitch = mc.thePlayer.rotationPitch
                if (freelookToggled) {
                    resetPerspective()
                } else {
                    enterFreelook()
                }
                mc.renderGlobal.setDisplayListEntitiesDirty()
            } else if (Config.mode == 0) {
                resetPerspective()
            }
        } else if (freelookToggled) {
            resetPerspective()
        }
    }

    private fun enterFreelook() {
        freelookToggled = true
        previousPerspective = mc.gameSettings.thirdPersonView
        mc.gameSettings.thirdPersonView = 1
        lastFov = mc.gameSettings.fovSetting.toInt()
        if (Config.customFov) mc.gameSettings.fovSetting = Config.fov.toFloat()
    }

    private fun resetPerspective() {
        freelookToggled = false
        mc.gameSettings.thirdPersonView = previousPerspective
        mc.renderGlobal.setDisplayListEntitiesDirty()
        if (Config.mode == 0 || Config.customFov) {
            mc.gameSettings.fovSetting = lastFov.toFloat()
        }
    }

    fun overrideMouse(): Boolean {
        if (::mc.isInitialized && mc.inGameHasFocus && Display.isActive()) {
            if (!freelookToggled) return true

            mc.mouseHelper.mouseXYChange()

            val sensitivity = (mc.gameSettings.mouseSensitivity * 0.6f + 0.2f).pow(3) * 8.0f * 0.15f
            val yaw = mc.mouseHelper.deltaX.toFloat() * sensitivity
            var pitch = mc.mouseHelper.deltaY.toFloat() * sensitivity
            val snaplook = isHypixel() || Config.snaplook

            if (Config.yaw) cameraYaw += yaw

            if (Config.pitch) {
                if (Config.invertPitch) pitch = -pitch
                cameraPitch += pitch
                if (Config.lockPitch || snaplook) cameraPitch = cameraPitch.coerceIn(-90f, 90f)
            }

            if (snaplook) {
                mc.thePlayer.rotationYaw = cameraYaw
                mc.thePlayer.rotationPitch = cameraPitch
            }

            mc.renderGlobal.setDisplayListEntitiesDirty()
        }

        return false
    }
}
