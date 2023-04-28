package dev.caoimhe.kdiscordipc.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/resources/user#user-object-premium-types
@Serializable
enum class PremiumType {
    /**
     * None
     */
    @SerialName("0")
    None,

    /**
     * Nitro Classic
     */
    @SerialName("1")
    NitroClassic,

    /**
     * Nitro
     */
    @SerialName("2")
    Nitro,

    /**
     * Nitro Basic
     */
    @SerialName("3")
    NitroBasic
}