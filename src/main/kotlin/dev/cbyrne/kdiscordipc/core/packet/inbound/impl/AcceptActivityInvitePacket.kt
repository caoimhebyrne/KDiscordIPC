package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.event.data.ErrorEventData
import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import kotlinx.serialization.Serializable

@Serializable
data class AcceptActivityInvitePacket(
    override val data: ErrorEventData? = null,
    override val cmd: String = "ACCEPT_ACTIVITY_INVITE",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket()