package dev.cbyrne.kdiscordipc.core.packet.impl

import dev.cbyrne.kdiscordipc.core.packet.Packet

data class HandshakePacket(
    val version: Int,
    val clientId: String,
    override val opcode: Int = 0x00
) : Packet