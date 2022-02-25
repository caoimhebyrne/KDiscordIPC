@file:Suppress("UNCHECKED_CAST")

package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.EncodeError
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.core.util.headerLength
import dev.cbyrne.kdiscordipc.core.util.reverse
import java.nio.ByteBuffer

object MessageToByteEncoder {
    fun <T : Packet> encode(ipc: KDiscordIPC, packet: T): ByteArray {
        val handler = ipc.packetHandlers[packet.opcode] as? PacketHandler<T>
            ?: throw EncodeError.NotSupported(packet.opcode)

        val data = handler.encode(packet)
        ipc.logger.debug("Encoding: ${data.decodeToString()}")

        val buffer = ByteBuffer.allocate(headerLength + data.size)
        buffer.putInt(packet.opcode.reverse())
        buffer.putInt(data.size.reverse())
        buffer.put(data)

        return buffer.array()
    }
}