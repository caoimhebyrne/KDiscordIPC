package dev.cbyrne.kdiscordipc.util

import kotlinx.serialization.json.Json
import java.lang.management.ManagementFactory

internal const val headerLength = 8

internal val json = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
}

internal val currentPid = ManagementFactory.getRuntimeMXBean().pid