package dev.cbyrne.kdiscordipc.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.error.DecodeError
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.util.headerLength
import dev.cbyrne.kdiscordipc.util.reverse
import java.nio.ByteBuffer

object ByteToMessageDecoder {
    fun decode(ipc: KDiscordIPC, bytes: ByteArray): Packet {
        val opcode = readOpcode(bytes.take(headerLength))
        val data = bytes
            .takeLast(bytes.size - headerLength)
            .toByteArray()
            .decodeToString()

        val handler = ipc.packetHandlers[opcode] ?: throw DecodeError.NotSupported(opcode, data)
        return handler.decode(data)
    }

    private fun readOpcode(bytes: List<Byte>): Int {
        return ByteBuffer.wrap(bytes.toByteArray()).int.reverse()
    }
}