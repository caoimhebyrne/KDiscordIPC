package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.data.user.User
import kotlinx.serialization.Serializable

@Serializable
data class GetUserPacket(
    override val data: User,
    override val cmd: String = "GET_USER",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket()