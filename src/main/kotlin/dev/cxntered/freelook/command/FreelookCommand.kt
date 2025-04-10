package dev.cxntered.freelook.command

import dev.cxntered.freelook.Freelook
import dev.cxntered.freelook.config.Config
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.UMinecraft

object FreelookCommand : Command(Freelook.MOD_ID) {
    override val commandAliases: Set<Alias> = setOf(
        Alias("flook", true),
        Alias("perspectivemod"),
        Alias("pmod", true),
        Alias("pm", true)
    )

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(Config.gui())
    }

    @SubCommand(value = "help")
    fun help() {
        UChat.chat("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}-------------------------${ChatColor.RESET}") // reset color to bypass compact chat :3
        UChat.chat("${ChatColor.GOLD}${ChatColor.BOLD}Freelook Settings")
        UChat.chat("")
        UChat.chat("${ChatColor.AQUA}/freelook toggle ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle Freelook. ${status(Config.enabled)}")
        UChat.chat("${ChatColor.AQUA}/freelook snaplook ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle Snaplook. ${status(Freelook.isHypixel() || Config.snaplook)}")
        UChat.chat("")
        UChat.chat("${ChatColor.AQUA}/freelook hold ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle hold to enable. ${status(Config.mode == 0)}")
        UChat.chat("${ChatColor.AQUA}/freelook yaw ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle moving the yaw. ${status(Config.yaw)}")
        UChat.chat("${ChatColor.AQUA}/freelook pitch ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle moving the pitch. ${status(Config.pitch)}")
        UChat.chat("${ChatColor.AQUA}/freelook invert ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle inverted pitch. ${status(Config.invertPitch)}")
        UChat.chat("${ChatColor.AQUA}/freelook lock ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle locking the pitch. ${status(Config.lockPitch)}")
        UChat.chat("")
        UChat.chat("${ChatColor.AQUA}/freelook customfov ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle custom FOV. ${status(Config.customFov)}")
        UChat.chat("${ChatColor.AQUA}/freelook fov [FOV] ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Set the custom FOV. ${ChatColor.GRAY}[${ChatColor.GREEN}${Config.fov}${ChatColor.GRAY}]")
        UChat.chat("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}-------------------------")
    }

    @SubCommand(value = "toggle", aliases = ["t"], description = "Toggle Freelook.")
    fun toggle() {
        Config.enabled = !Config.enabled
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook has been ${availability(Config.enabled)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "snaplook", aliases = ["s"], description = "Toggle Snaplook.")
    fun snaplook() {
        if (Freelook.isHypixel()) {
            UChat.chat("${ChatColor.RED}Snaplook is forcefully enabled on Hypixel.")
            return
        }
        Config.snaplook = !Config.snaplook
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Snaplook has been ${availability(Config.snaplook)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "hold", aliases = ["h"], description = "Toggle hold to enable.")
    fun hold() {
        Config.mode = if (Config.mode == 0) 1 else 0
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook hold has been ${availability(Config.mode == 0)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "yaw", aliases = ["y"], description = "Toggle moving the yaw.")
    fun yaw() {
        Config.yaw = !Config.yaw
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook yaw has been ${availability(Config.yaw)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "pitch", aliases = ["p"], description = "Toggle moving the pitch.")
    fun pitch() {
        Config.pitch = !Config.pitch
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook pitch has been ${availability(Config.pitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "invert", aliases = ["invertpitch", "i"], description = "Toggle inverted pitch.")
    fun invert() {
        Config.invertPitch = !Config.invertPitch
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook inverted pitch has been ${availability(Config.invertPitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "lock", aliases = ["lockpitch", "l"], description = "Toggle locking the pitch.")
    fun lock() {
        Config.lockPitch = !Config.lockPitch
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook pitch lock has been ${availability(Config.lockPitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "customfov", aliases = ["cf", "c"], description = "Toggle custom FOV.")
    fun customfov() {
        Config.customFov = !Config.customFov
        Config.markDirty()
        if (Freelook.freelookToggled && !Config.customFov) {
            UMinecraft.getMinecraft().gameSettings.fovSetting = Freelook.lastFov.toFloat()
        }
        UChat.chat("${ChatColor.YELLOW}Freelook custom FOV has been ${availability(Config.customFov)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "fov", aliases = ["f"], description = "Set the custom FOV. Must be between 10 and 150.")
    fun fov(fov: Int) {
        if (fov < 10 || fov > 150) {
            UChat.chat("${ChatColor.RED}Invalid FOV. FOV must be between 10 and 150")
            return
        }

        Config.fov = fov
        Config.markDirty()
        UChat.chat("${ChatColor.YELLOW}Freelook FOV has been set to ${ChatColor.GREEN}${fov}${ChatColor.YELLOW}.")
    }

    private fun availability(option: Boolean): String {
        return if (option) {
            "${ChatColor.GREEN}enabled"
        } else {
            "${ChatColor.RED}disabled"
        }
    }

    private fun status(option: Boolean): String {
        val toggled = if (option) {
            "${ChatColor.GREEN}+"
        } else {
            "${ChatColor.RED}-"
        }
        return "${ChatColor.GRAY}[${toggled}${ChatColor.GRAY}]"
    }
}