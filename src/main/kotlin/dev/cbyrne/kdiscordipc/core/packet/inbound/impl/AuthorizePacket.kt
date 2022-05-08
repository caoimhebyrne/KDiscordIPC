package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizePacket(
    override val data: Data,
    override val cmd: String = "AUTHORIZE",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
): CommandPacket() {
    @Serializable
    data class Data(val code: String): InboundPacket.Data()
}