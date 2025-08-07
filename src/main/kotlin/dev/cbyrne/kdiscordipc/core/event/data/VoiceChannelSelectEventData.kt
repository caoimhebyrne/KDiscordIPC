package dev.cbyrne.kdiscordipc.core.event.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceChannelSelectEventData(
    @SerialName("channel_id")
    val channelId: String,
    @SerialName("guild_id")
    val guildId: String,
) : EventData()