package dev.cbyrne.kdiscordipc.activity

import kotlinx.serialization.Serializable

@Serializable
data class DiscordActivity(
    val details: String,
    val state: String,
)
