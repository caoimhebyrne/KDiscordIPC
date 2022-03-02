package dev.cbyrne.kdiscordipc.data.activity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InboundActivity(
    @SerialName("application_id")
    val applicationId: String,
    val assets: Activity.Assets,
    @SerialName("created_at")
    val createdAt: String,
    val flags: Int,
    val id: String,
    val name: String,
    val party: Party,
    @SerialName("session_id")
    val sessionId: String,
    val state: String,
    val timestamps: Timestamps,
    val type: Int
) {
    @Serializable
    data class Party(
        val id: String
    )

    @Serializable
    data class Timestamps(
        val start: Long? = 0,
        val end: Long? = 0
    )
}