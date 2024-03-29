package dev.cbyrne.kdiscordipc.core.util

internal enum class Platform {
    WINDOWS,
    MACOS,
    LINUX,
    UNKNOWN
}

internal val platform: Platform
    get() {
        val name = System.getProperty("os.name").lowercase()
        return if (name.contains("win")) {
            Platform.WINDOWS
        } else if (name.contains("mac")) {
            Platform.MACOS
        } else if (name.contains("nix") || name.contains("nux") || name.contains("aix")) {
            Platform.LINUX
        } else {
            Platform.UNKNOWN
        }
    }
