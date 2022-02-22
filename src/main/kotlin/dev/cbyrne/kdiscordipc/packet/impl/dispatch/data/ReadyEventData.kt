package dev.cbyrne.kdiscordipc.packet.impl.dispatch.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadyEventData(
    @SerialName("v")
    val version: Int,
    val config: Configuration,
    val user: User,
) : DispatchPacketData() {
    @Serializable
    data class Configuration(
        @SerialName("cdn_host") val cdnHost: String,
        @SerialName("api_endpoint") val apiEndpoint: String,
        val environment: String
    )

    @Serializable
    data class User(
        val id: String,
        val username: String,
        val discriminator: String,
        val avatar: String,
        val bot: Boolean,
        val flags: Int,
        @SerialName("premium_type") val premiumType: Int
    )
}