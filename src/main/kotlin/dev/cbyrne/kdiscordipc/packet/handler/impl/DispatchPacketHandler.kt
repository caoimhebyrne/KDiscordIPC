package dev.cbyrne.kdiscordipc.packet.handler.impl

import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.dispatch.DispatchPacket
import dev.cbyrne.kdiscordipc.util.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class DispatchPacketHandler : PacketHandler<DispatchPacket> {
    override fun decode(jsonString: String): DispatchPacket =
        json.decodeFromString(jsonString)

    override fun encode(packet: DispatchPacket) =
        json.encodeToString(packet).toByteArray()
}