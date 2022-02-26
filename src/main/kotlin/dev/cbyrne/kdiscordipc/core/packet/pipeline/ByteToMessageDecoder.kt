package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.DecodeError.NotSupported
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.socket.RawPacket

object ByteToMessageDecoder {
    fun decode(ipc: KDiscordIPC, packet: RawPacket): Packet {
        val json = packet.data.decodeToString()
        ipc.logger.debug("Decoding: $json")

        val handler = ipc.packetHandlers[packet.opcode] ?: throw NotSupported(packet.opcode, json)
        return handler.decode(json)
    }
}