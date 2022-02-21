package dev.cbyrne.kdiscordipc.packet.handler

import dev.cbyrne.kdiscordipc.packet.Packet

interface PacketHandler<T : Packet> {
    fun decode(jsonString: String): T {
        throw NotImplementedError()
    }

    fun encode(packet: T): ByteArray {
        throw NotImplementedError()
    }
}