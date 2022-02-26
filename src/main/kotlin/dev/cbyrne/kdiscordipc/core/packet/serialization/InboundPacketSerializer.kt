package dev.cbyrne.kdiscordipc.core.packet.serialization

import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*

object InboundPacketSerializer : JsonContentPolymorphicSerializer<InboundPacket>(InboundPacket::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out InboundPacket> =
        when (val command = element.contentOrNull("cmd")) {
            "DISPATCH" -> when (val event = element.contentOrNull("evt")) {
                "READY" -> DispatchEventPacket.Ready.serializer()
                "CURRENT_USER_UPDATE" -> DispatchEventPacket.UserUpdate.serializer()
                else -> error("Unknown DISPATCH event: $event")
            }
            "SET_ACTIVITY" -> SetActivityPacket.serializer()
            "AUTHENTICATE" -> AuthenticatePacket.serializer()
            "GET_USER" -> GetUserPacket.serializer()
            "GET_RELATIONSHIPS" -> GetRelationshipsPacket.serializer()
            "SUBSCRIBE" -> SubscribePacket.serializer()
            else -> error("Unknown packet command: $command")
        }

    private fun JsonElement.contentOrNull(key: String) =
        jsonObject[key]?.jsonPrimitive?.contentOrNull
}