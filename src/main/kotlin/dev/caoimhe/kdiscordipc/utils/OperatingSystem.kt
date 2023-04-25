package dev.caoimhe.kdiscordipc.utils

/**
 * An enum representing the 3 main operating systems (Windows, Mac, and Linux)
 */
enum class OperatingSystem {
    Windows,
    Mac,
    Linux,
    Other;

    companion object {
        /**
         * Returns the current operating system, determined by the `os.name` property
         */
        fun current(): OperatingSystem {
            val name = System.getProperty("os.name").lowercase()
            return when {
                name.contains("win") -> {
                    Windows
                }

                name.contains("mac") || name.contains("osx") -> {
                    Mac
                }

                name.contains("nix") || name.contains("nux") || name.contains("aix") -> {
                    Linux
                }

                else -> Other
            }
        }
    }
}
