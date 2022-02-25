package dev.cbyrne.kdiscordipc.core.packet.impl

import dev.cbyrne.kdiscordipc.core.packet.Packet

data class ErrorPacket(
    val code: Int,
    val message: String,
    override val opcode: Int = 0x02,
) : Packet