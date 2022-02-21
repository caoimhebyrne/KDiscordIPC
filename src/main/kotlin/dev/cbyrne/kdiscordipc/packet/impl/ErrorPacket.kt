package dev.cbyrne.kdiscordipc.packet.impl

import dev.cbyrne.kdiscordipc.packet.Packet

data class ErrorPacket(
    val code: Int,
    val message: String,
    override val opcode: Int = 0x02,
) : Packet