package dev.cxntered.freelook.command

import dev.cxntered.freelook.Freelook
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.universal.ChatColor
import gg.essential.universal.UChat
import gg.essential.universal.UMinecraft

object FreelookCommand : Command("freelook") {
    override val commandAliases: Set<Alias> = setOf(
        Alias("flook", true),
        Alias("perspectivemod"),
        Alias("pmod", true),
        Alias("pm", true)
    )

    private val config = Freelook.config

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(config.gui())
    }

    @SubCommand(value = "help")
    fun help() {
        UChat.chat("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}-------------------------${ChatColor.RESET}") // reset color to bypass compact chat :3
        UChat.chat("${ChatColor.GOLD}${ChatColor.BOLD}Freelook Settings")
        UChat.chat("")
        UChat.chat("${ChatColor.AQUA}/freelook toggle ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle freelook. ${status(config.enabled)}")
        UChat.chat("${ChatColor.AQUA}/freelook hold ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle freelook hold. ${status(config.mode == 0)}")
        UChat.chat("${ChatColor.AQUA}/freelook yaw ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle moving the yaw. ${status(config.yaw)}")
        UChat.chat("${ChatColor.AQUA}/freelook pitch ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle moving the pitch. ${status(config.pitch)}")
        UChat.chat("${ChatColor.AQUA}/freelook invert ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle inverted pitch. ${status(config.invertPitch)}")
        UChat.chat("${ChatColor.AQUA}/freelook lock ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle locking the pitch. ${status(config.lockPitch)}")
        UChat.chat("")
        UChat.chat("${ChatColor.AQUA}/freelook customfov ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Toggle custom FOV. ${status(config.customFov)}")
        UChat.chat("${ChatColor.AQUA}/freelook fov [FOV] ${ChatColor.WHITE}➜ ${ChatColor.YELLOW}Set your custom FOV. ${ChatColor.GRAY}[${ChatColor.GREEN}${config.fov}${ChatColor.GRAY}]")
        UChat.chat("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}-------------------------")
    }

    @SubCommand(value = "toggle", aliases = ["t"])
    fun toggle() {
        config.enabled = !config.enabled
        UChat.chat("${ChatColor.YELLOW}Freelook has been ${availability(config.enabled)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "hold", aliases = ["h"])
    fun hold() {
        config.mode = if (config.mode == 0) 1 else 0
        UChat.chat("${ChatColor.YELLOW}Freelook hold has been ${availability(config.mode == 0)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "yaw", aliases = ["y"])
    fun yaw() {
        config.yaw = !config.yaw
        UChat.chat("${ChatColor.YELLOW}Freelook yaw has been ${availability(config.yaw)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "pitch", aliases = ["p"])
    fun pitch() {
        config.pitch = !config.pitch
        UChat.chat("${ChatColor.YELLOW}Freelook pitch has been ${availability(config.pitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "invert", aliases = ["invertpitch", "i"])
    fun invert() {
        config.invertPitch = !config.invertPitch
        UChat.chat("${ChatColor.YELLOW}Freelook inverted pitch has been ${availability(config.invertPitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "lock", aliases = ["lockpitch", "l"])
    fun lock() {
        config.lockPitch = !config.lockPitch
        UChat.chat("${ChatColor.YELLOW}Freelook pitch lock has been ${availability(config.lockPitch)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "customfov", aliases = ["cf", "c"])
    fun customfov() {
        config.customFov = !config.customFov
        if (Freelook.freelookToggled && !config.customFov) {
            UMinecraft.getMinecraft().gameSettings.fovSetting = Freelook.lastFov.toFloat()
        }
        UChat.chat("${ChatColor.YELLOW}Freelook custom FOV has been ${availability(config.customFov)}${ChatColor.YELLOW}.")
    }

    @SubCommand(value = "fov", aliases = ["f"])
    fun fov(fov: Int) {
        if (fov < 10 || fov > 150) {
            UChat.chat("${ChatColor.RED}Invalid FOV. FOV must be between 10 and 150")
            return
        }

        config.fov = fov
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