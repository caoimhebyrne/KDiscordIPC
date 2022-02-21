package dev.cbyrne.kdiscordipc.packet.handler.impl

import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.ErrorPacket
import dev.cbyrne.kdiscordipc.util.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

class ErrorPacketHandler : PacketHandler<ErrorPacket> {
    @Serializable
    private data class Data(
        val code: Int,
        val message: String
    )

    override fun decode(jsonString: String): ErrorPacket {
        val data = json.decodeFromString<Data>(jsonString)
        return ErrorPacket(data.code, data.message)
    }
}