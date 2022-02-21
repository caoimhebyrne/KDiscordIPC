package dev.cbyrne.kdiscordipc.util

import kotlinx.serialization.json.Json

internal const val headerLength = 8

internal val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
}