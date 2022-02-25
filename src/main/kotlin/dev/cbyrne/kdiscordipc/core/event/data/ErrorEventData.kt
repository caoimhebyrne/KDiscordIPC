package dev.cbyrne.kdiscordipc.core.event.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorEventData(
    val code: Int,
    val message: String
) : EventData()
