@file:Suppress("UNCHECKED_CAST")

package dev.cbyrne.kdiscordipc.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.error.EncodeError
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.util.headerLength
import dev.cbyrne.kdiscordipc.util.reverse
import java.nio.ByteBuffer

object MessageToByteEncoder {
    fun <T : Packet> encode(ipc: KDiscordIPC, packet: T): ByteArray {
        val handler = ipc.packetHandlers[packet.opcode] as? PacketHandler<T>
            ?: throw EncodeError.NotSupported(packet.opcode)

        val data = handler.encode(packet)
        val buffer = ByteBuffer.allocate(headerLength + data.size)
        buffer.putInt(packet.opcode.reverse())
        buffer.putInt(data.size.reverse())
        buffer.put(data)

        return buffer.array()
    }
}