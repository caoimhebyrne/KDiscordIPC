package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class GetRelationshipsPacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "GET_RELATIONSHIPS",
    override val args: Arguments = Arguments(),
    override var nonce: String = "0"
) : OutboundPacket() {
    @Serializable
    class Arguments : OutboundPacket.Arguments()
}