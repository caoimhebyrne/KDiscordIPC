package dev.cbyrne.kdiscordipc.packet.handler.impl

import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.util.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class CommandPacketHandler : PacketHandler<CommandPacket> {
    override fun decode(jsonString: String): CommandPacket =
        json.decodeFromString(jsonString)

    override fun encode(packet: CommandPacket) =
        json.encodeToString(packet).toByteArray()
}