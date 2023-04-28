package dev.caoimhe.kdiscordipc.models.configuration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/topics/rpc#ready-rpc-server-configuration-object
@Serializable
data class ServerConfiguration(
    /**
     * The client's CDN host, this may change depending on location
     */
    @SerialName("cdn_host")
    val cdnHost: String,

    /**
     * The client's API endpoint, this may change if the user is not on the production environment
     */
    @SerialName("api_endpoint")
    val apiEndpoint: String,

    /**
     * The client's environment
     */
    val environment: String
)
