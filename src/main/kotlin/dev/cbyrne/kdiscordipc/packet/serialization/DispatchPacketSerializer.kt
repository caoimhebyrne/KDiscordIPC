package dev.cbyrne.kdiscordipc.packet.serialization

import dev.cbyrne.kdiscordipc.packet.impl.dispatch.DispatchPacket
import kotlinx.serialization.json.*

object DispatchPacketSerializer : JsonContentPolymorphicSerializer<DispatchPacket>(DispatchPacket::class) {
    override fun selectDeserializer(element: JsonElement) =
        // methinks this code is quite :thumbsup:
        when (element.jsonObject["evt"]?.jsonPrimitive?.contentOrNull) {
            "READY" -> DispatchPacket.ReadyEvent.serializer()
            else -> throw NotImplementedError()
        }
}