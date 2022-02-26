package dev.cbyrne.kdiscordipc.core.util

import kotlinx.serialization.json.Json
import java.lang.management.ManagementFactory

internal const val headerLength = 8

val json = Json {
    prettyPrint = false
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}

internal val currentPid = ManagementFactory.getRuntimeMXBean().pid