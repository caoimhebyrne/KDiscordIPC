package dev.cbyrne.kdiscordipc.core.event.data

import kotlinx.serialization.Serializable

@Serializable
data class ActivityJoinEventData(
    val secret: String
) : EventData()