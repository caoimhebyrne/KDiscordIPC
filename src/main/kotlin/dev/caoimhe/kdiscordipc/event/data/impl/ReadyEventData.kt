package dev.caoimhe.kdiscordipc.event.data.impl

import dev.caoimhe.kdiscordipc.event.data.EventData
import dev.caoimhe.kdiscordipc.models.configuration.ServerConfiguration
import dev.caoimhe.kdiscordipc.models.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/topics/rpc#ready
@Serializable
data class ReadyEventData(
    /**
     * The version of the protocol used, should always be 1
     */
    @SerialName("v")
    val version: Int,

    /**
     * The client's configuration
     */
    @SerialName("config")
    val configuration: ServerConfiguration,

    /**
     * The user to whom we are connected to
     */
    val user: User
) : EventData
