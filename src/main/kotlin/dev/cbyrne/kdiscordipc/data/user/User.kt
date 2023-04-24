package dev.cbyrne.kdiscordipc.data.user

import dev.cbyrne.kdiscordipc.core.event.data.EventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val discriminator: String,
    val avatar: String? = null,
    val bot: Boolean? = null,
    val flags: Int? = null,
    @SerialName("premium_type") val premiumType: PremiumType? = null
) : EventData()