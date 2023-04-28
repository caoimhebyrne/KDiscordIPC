package dev.caoimhe.kdiscordipc.event.data.impl

import dev.caoimhe.kdiscordipc.event.data.EventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadyEventData(
    @SerialName("v")
    val version: Int
) : EventData