package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.DecodeError
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.util.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString

object ByteToMessageDecoder {
    fun decode(packet: RawPacket): InboundPacket? {
        try {
            val data = packet.data.decodeToString()
            KDiscordIPC.logger.debug("Decoding: $data")

            return json.decodeFromString<InboundPacket>(data)
        } catch (e: SerializationException) {
            // We didn't receive the full data, probably because the socket was closed.
            throw DecodeError.InvalidData
        } catch (e: Exception) {
            KDiscordIPC.logger.debug(
                "Caught error when decoding packet (op: ${packet.opcode}, length: ${packet.length})",
                e
            )
        }

        return null
    }
}