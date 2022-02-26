package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.IrregularOutboundPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HandshakePacket(
    @SerialName("v")
    val version: Int,

    @SerialName("client_id")
    val clientId: String,

    override val opcode: Int = 0x00,
) : IrregularOutboundPacket()