package dev.cbyrne.kdiscordipc.packet.impl

import dev.cbyrne.kdiscordipc.packet.Packet

data class HandshakePacket(
    val version: Int,
    val clientId: String,
    override val opcode: Int = 0x00
) : Packet