package dev.caoimhe.kdiscordipc.channel.message.outbound

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class OutboundMessageData {
    /**
     * The command associated with this message
     */
    @SerialName("cmd")
    abstract val command: String

    /**
     * The nonce being used in this string of commands, used to associate sent messages with their received counterparts.
     */
    abstract val nonce: String?
}