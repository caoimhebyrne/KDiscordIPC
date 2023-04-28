package dev.caoimhe.kdiscordipc.channel.message.inbound

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This is how we receive event data from the Discord client.
 * It has one extra property compared to normal messages, the [event] property.
 */
@Serializable
data class DispatchMessageData(
    override val command: String = "DISPATCH",
    override val nonce: String? = null,

    /**
     * The event to be dispatched
     */
    @SerialName("evt")
    val event: String
) : InboundMessageData()
