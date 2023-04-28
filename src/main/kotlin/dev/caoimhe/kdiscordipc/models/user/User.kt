package dev.caoimhe.kdiscordipc.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/resources/user#user-object
@Serializable
data class User(
    /**
     * The user's unique ID, as a snowflake
     */
    val id: String,

    /**
     * The user's username, not unique across the platform
     */
    val username: String,

    /**
     * The user's 4-digit discord-tag
     */
    val discriminator: String,

    /**
     * The user's avatar hash
     */
    val avatar: String? = null,

    /**
     * Whether the user belongs to an OAuth2 application
     */
    val bot: Boolean? = null,

    /**
     * Whether the user is an Official Discord System user (part of the urgent message system)
     */
    val system: Boolean? = null,

    /**
     * Whether the user has two factor enabled on their account
     */
    @SerialName("mfa_enabled")
    val mfaEnabled: Boolean? = null,

    /**
     * The user's banner hash
     */
    val banner: String? = null,

    /**
     * The user's banner color encoded as an integer representation of hexadecimal color code
     */
    @SerialName("accent_color")
    val accentColor: String? = null,

    /**
     * The user's chosen language option
     */
    val locale: String? = null,

    /**
     * Whether the email on this account has been verified
     */
    val verified: Boolean? = null,

    /**
     * The user's email
     */
    val email: Boolean? = null,

    /**
     * The flags on a user's account
     */
    val flags: Flags? = null,

    /**
     * The type of Nitro subscription on a user's account
     */
    @SerialName("premium_type")
    val premiumType: PremiumType? = null,

    /**
     * The public flags on a user's account
     */
    @SerialName("public_flags")
    val publicFlags: Flags? = null
)
