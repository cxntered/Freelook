package dev.cxntered.freelook

import dev.cxntered.freelook.command.FreelookCommand
import dev.cxntered.freelook.config.Config
import gg.essential.api.EssentialAPI
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import kotlin.math.pow

@Mod(modid = "freelook", useMetadata = true, modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter")
object Freelook {
    private val mc: Minecraft by lazy {
        Minecraft.getMinecraft()
    }
    val config: Config = Config
    private val keybind = KeyBinding("Freelook", Keyboard.KEY_LMENU, "Freelook")

    var freelookToggled = false
    private var previousState = false

    var cameraYaw = 0f
    var cameraPitch = 0f
    lateinit var lastFov: Number

    private var previousPerspective = 0

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config.preload()
        lastFov = config.fov

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
        if (event.gui != null && freelookToggled && config.mode == 0) {
            resetPerspective()
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        if (freelookToggled) {
            resetPerspective()
        }
    }

    private fun onPressed(state: Boolean) {
        if (config.enabled) {
            if (state) {
                cameraYaw = mc.thePlayer.rotationYaw
                cameraPitch = mc.thePlayer.rotationPitch
                if (freelookToggled) {
                    resetPerspective()
                } else {
                    enterFreelook()
                }
                mc.renderGlobal.setDisplayListEntitiesDirty()
            } else if (config.mode == 0) {
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
    }

    private fun resetPerspective() {
        freelookToggled = false
        mc.gameSettings.thirdPersonView = previousPerspective
        mc.renderGlobal.setDisplayListEntitiesDirty()
        if (config.mode == 0 || config.customFov) {
            mc.gameSettings.fovSetting = lastFov.toFloat()
        }
    }

    fun overrideMouse(): Boolean {
        if (mc.inGameHasFocus && Display.isActive()) {
            if (!freelookToggled) {
                return true
            }

            mc.mouseHelper.mouseXYChange()

            val f1 = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f
            val f2 = f1.pow(3) * 8.0f
            val f3 = mc.mouseHelper.deltaX.toFloat() * f2
            var f4 = mc.mouseHelper.deltaY.toFloat() * f2

            if (config.yaw) {
                cameraYaw += f3 * 0.15f
            }

            if (config.pitch) {
                if (config.invertPitch) {
                    f4 = -f4
                }

                cameraPitch += f4 * 0.15f
                if (config.lockPitch) {
                    if (cameraPitch > 90f) cameraPitch = 90f
                    if (cameraPitch < -90f) cameraPitch = -90f
                }
            }

            if (config.customFov) {
                mc.gameSettings.fovSetting = config.fov.toFloat()
            }

            mc.renderGlobal.setDisplayListEntitiesDirty()
        }

        return false
    }
}
