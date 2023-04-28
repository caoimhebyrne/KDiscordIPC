package dev.caoimhe.kdiscordipc.channel.message.inbound

import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = InboundMessageData.Companion::class)
sealed class InboundMessageData {
    /**
     * The command associated with this message
     */
    @SerialName("cmd")
    abstract val command: String

    /**
     * The nonce being used in this string of commands, used to associate sent messages with their received counterparts.
     */
    abstract val nonce: String?

    /**
     * De-serializes an incoming message JSON
     */
    companion object : JsonContentPolymorphicSerializer<InboundMessageData>(InboundMessageData::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out InboundMessageData> =
            // The command will tell us what exactly to do with this message
            when (val command = element.jsonObject["cmd"]?.jsonPrimitive?.contentOrNull) {
                // Used for dispatching events to the client
                "DISPATCH" -> DispatchMessageData.selectDeserializer(element)

                // TODO: Better error handling
                else -> error("Unsupported command $command")
            }
    }
}
