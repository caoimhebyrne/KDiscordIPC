package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class GetUserPacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "GET_USER",
    override val args: Arguments,
    override var nonce: String = "0"
) : OutboundPacket() {
    constructor(id: String) : this(args = Arguments(id))

    @Serializable
    data class Arguments(
        val id: String
    ) : OutboundPacket.Arguments()
}