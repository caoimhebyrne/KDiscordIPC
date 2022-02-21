package dev.cbyrne.kdiscordipc.packet.handler.impl

import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.util.json
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