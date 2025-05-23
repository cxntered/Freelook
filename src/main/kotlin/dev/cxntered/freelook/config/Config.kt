package dev.cxntered.freelook.config

import dev.cxntered.freelook.Freelook
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(File("./config/${Freelook.MOD_ID}.toml")) {
    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "Whether or not Freelook is enabled.",
        category = "General"
    )
    var enabled = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Snaplook",
        description = "Whether or not Snaplook is enabled. When enabled, the camera will behave like normal F5. Snaplook is forcefully enabled when on Hypixel.",
        category = "General"
    )
    var snaplook = false

    @Property(
        type = PropertyType.SELECTOR,
        name = "Mode",
        description = "The mode of enabling Freelook.",
        options = ["Hold", "Toggle"],
        category = "General"
    )
    var mode = 0

    @Property(
        type = PropertyType.SWITCH,
        name = "Yaw",
        description = "Whether or not you can move the yaw.",
        category = "General"
    )
    var yaw = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Pitch",
        description = "Whether or not you can move the pitch.",
        category = "General"
    )
    var pitch = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Invert Pitch",
        description = "Whether or not pitch is inverted. Enabled mimics the behavior of clients such as Lunar or Badlion.",
        category = "General"
    )
    var invertPitch = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Lock Pitch",
        description = "Whether or not pitch is locked. When disabled, the camera can go upside down. This setting is forcefully enabled if Snaplook is enabled.",
        category = "General"
    )
    var lockPitch = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Custom FOV",
        description = "Whether or not a custom FOV is used when in Freelook.",
        category = "General"
    )
    var customFov = false

    @Property(
        type = PropertyType.SLIDER,
        name = "FOV",
        description = "The custom FOV value.",
        category = "General",
        min = 10,
        max = 150,
    )
    var fov = 70

    init {
        initialize()

        listOf(
            "snaplook", "mode", "yaw", "pitch",
            "invertPitch", "lockPitch", "customFov", "fov"
        ).forEach { addDependency(it, "enabled") }
    }
}