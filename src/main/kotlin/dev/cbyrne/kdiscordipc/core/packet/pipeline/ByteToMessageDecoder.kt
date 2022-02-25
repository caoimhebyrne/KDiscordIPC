package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.DecodeError.NotSupported
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.util.headerLength
import dev.cbyrne.kdiscordipc.core.util.reverse
import java.nio.ByteBuffer

object ByteToMessageDecoder {
    fun decode(ipc: KDiscordIPC, bytes: ByteArray): Packet {
        val opcode = readOpcode(bytes.take(headerLength))
        val data = bytes
            .takeLast(bytes.size - headerLength)
            .toByteArray()
            .decodeToString()

        ipc.logger.debug("Decoding: $data")

        val handler = ipc.packetHandlers[opcode] ?: throw NotSupported(opcode, data)
        return handler.decode(data)
    }

    private fun readOpcode(bytes: List<Byte>): Int {
        return ByteBuffer.wrap(bytes.toByteArray()).int.reverse()
    }
}