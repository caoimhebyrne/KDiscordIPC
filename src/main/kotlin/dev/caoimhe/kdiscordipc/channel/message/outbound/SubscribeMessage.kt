package dev.caoimhe.kdiscordipc.channel.message.outbound

import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.event.data.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/topics/rpc#subscribe
class SubscribeMessage(
    event: Event<*>
) : Message.Outbound<SubscribeMessage.Data>(
    opcode = 1,
    data = Data(event = event.id),
    serializer = Data.serializer()
) {
    @Serializable
    data class Data(
        @SerialName("cmd")
        override val command: String = "SUBSCRIBE",
        override val nonce: String? = null,

        @SerialName("evt")
        val event: String
    ) : OutboundMessageData()
}
