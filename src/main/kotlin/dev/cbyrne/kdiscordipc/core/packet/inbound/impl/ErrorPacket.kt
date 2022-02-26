package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket

@kotlinx.serialization.Serializable
data class ErrorPacket(
    val code: Int,
    val message: String,
    override val opcode: Int = 0x02,
) : InboundPacket() {
    override val cmd: String? = null
    override val data: Data? = null
    override val nonce: String? = null
}