package dev.cbyrne.kdiscordipc.core.packet.serialization

import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.*

object CommandPacketSerializer : JsonContentPolymorphicSerializer<CommandPacket>(CommandPacket::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out CommandPacket> {
        val command = element.contentOrNull("cmd")
        return when (command) {
            "DISPATCH" -> when (val event = element.contentOrNull("evt")) {
                "READY" -> CommandPacket.DispatchEvent.Ready.serializer()
                else -> error("Unknown event: $event")
            }
            "SET_ACTIVITY" -> CommandPacket.SetActivity.serializer()
            "GET_USER" -> CommandPacket.GetUser.serializer()
            "AUTHENTICATE" -> CommandPacket.Authenticate.serializer()
            else -> error("Unknown command: $command")
        }
    }

    private fun JsonElement.contentOrNull(key: String) =
        jsonObject[key]?.jsonPrimitive?.contentOrNull
}