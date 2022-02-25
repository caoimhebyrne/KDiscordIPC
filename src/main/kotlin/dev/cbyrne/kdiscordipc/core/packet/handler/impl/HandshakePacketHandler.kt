package dev.cbyrne.kdiscordipc.core.packet.handler.impl

import dev.cbyrne.kdiscordipc.core.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.core.packet.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.core.util.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class HandshakePacketHandler : PacketHandler<HandshakePacket> {
    @Serializable
    data class Data(
        @SerialName("v")
        val version: Int,

        @SerialName("client_id")
        val clientId: String
    )

    override fun encode(packet: HandshakePacket) =
        json.encodeToString(Data(packet.version, packet.clientId)).toByteArray()
}