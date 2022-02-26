package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.data.relationship.Relationship
import kotlinx.serialization.Serializable

@Serializable
data class GetRelationshipsPacket(
    override val data: Data,
    override val cmd: String = "GET_RELATIONSHIPS",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket() {
    @Serializable
    data class Data(
        val relationships: List<Relationship>
    ) : InboundPacket.Data()
}