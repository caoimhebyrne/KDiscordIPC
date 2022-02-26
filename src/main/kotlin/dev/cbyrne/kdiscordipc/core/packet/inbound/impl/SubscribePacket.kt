package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribePacket(
    override val data: Data,
    override val cmd: String = "SUBSCRIBE",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket() {
    @Serializable
    data class Data(
        @SerialName("evt")
        val event: String
    ) : InboundPacket.Data()
}
