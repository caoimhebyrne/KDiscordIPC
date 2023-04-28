package dev.caoimhe.kdiscordipc.channel.message.outbound

import dev.caoimhe.kdiscordipc.channel.message.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class HandshakeMessage(clientID: String) : Message.Outbound<HandshakeMessage.Data>(
    opcode = 0,
    data = Data(clientID = clientID),
    serializer = Data.serializer()
) {
    @Serializable
    data class Data(
        @SerialName("v")
        val version: Int = 1,

        @SerialName("client_id")
        val clientID: String
    )
}
