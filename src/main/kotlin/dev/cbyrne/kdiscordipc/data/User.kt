package dev.cbyrne.kdiscordipc.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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