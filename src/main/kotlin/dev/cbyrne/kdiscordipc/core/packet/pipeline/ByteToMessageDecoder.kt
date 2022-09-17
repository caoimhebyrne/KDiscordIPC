package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.util.json
import kotlinx.serialization.decodeFromString

object ByteToMessageDecoder {
    fun decode(ipc: KDiscordIPC, packet: RawPacket): InboundPacket? {
        try {
            val data = packet.data.decodeToString()
            ipc.logger.debug("Decoding: $data")

            return json.decodeFromString<InboundPacket>(data)
        } catch (e: Exception) {
            ipc.logger.error("Caught error when decoding packet (op: ${packet.opcode}, length: ${packet.length}) ", e)
        }

        return null
    }
}