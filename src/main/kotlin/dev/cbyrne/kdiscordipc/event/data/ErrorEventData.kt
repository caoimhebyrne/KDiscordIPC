package dev.cbyrne.kdiscordipc.event.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorEventData(
    val code: Int,
    val message: String
) : EventData()
