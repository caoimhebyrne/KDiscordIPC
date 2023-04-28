package dev.caoimhe.kdiscordipc.channel.message.inbound

import dev.caoimhe.kdiscordipc.event.data.EventData
import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * This is how we receive event data from the Discord client.
 * It has one extra property compared to normal messages, the [event] property.
 */
@Serializable
data class DispatchMessageData<T : EventData>(
    override val command: String = "DISPATCH",
    override val nonce: String? = null,

    /**
     * The data associated with this event
     */
    val data: T,

    /**
     * The event to be dispatched
     */
    @SerialName("evt")
    val event: String
) : InboundMessageData() {
    // FIXME: For some reason we can't just use a custom de-serializer via JsonContentPolymorphicSerializer
    companion object {
        fun selectDeserializer(element: JsonElement) =
            // The event will tell us what type we expect the data to be
            when (val event = element.jsonObject["evt"]?.jsonPrimitive?.contentOrNull) {
                // The first event we receive from the client is the READY event
                "READY" -> serializer(ReadyEventData.serializer())

                // TODO: Better error handling
                else -> error("Unknown event: $event")
            }
    }
}
