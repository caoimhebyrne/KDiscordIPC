package dev.cbyrne.kdiscordipc.core.packet.pipeline

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import dev.cbyrne.kdiscordipc.core.util.headerLength
import dev.cbyrne.kdiscordipc.core.util.json
import dev.cbyrne.kdiscordipc.core.util.reverse
import kotlinx.serialization.encodeToString
import java.nio.ByteBuffer

object MessageToByteEncoder {
    internal inline fun <reified T : OutboundPacket> encode(packet: T, nonce: String?): ByteArray {
        nonce?.let { packet.nonce = it }

        val data = json.encodeToString(packet)
        KDiscordIPC.logger.debug("Encoding: $data")

        val bytes = data.encodeToByteArray()
        val buffer = ByteBuffer.allocate(headerLength + bytes.size)
        buffer.putInt(packet.opcode.reverse())
        buffer.putInt(bytes.size.reverse())
        buffer.put(bytes)

        return buffer.array()
    }
}