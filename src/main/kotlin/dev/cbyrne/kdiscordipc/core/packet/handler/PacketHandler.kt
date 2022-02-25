package dev.cbyrne.kdiscordipc.core.packet.handler

import dev.cbyrne.kdiscordipc.core.packet.Packet

interface PacketHandler<T : Packet> {
    fun decode(jsonString: String): T {
        throw NotImplementedError()
    }

    fun encode(packet: T): ByteArray {
        throw NotImplementedError()
    }
}