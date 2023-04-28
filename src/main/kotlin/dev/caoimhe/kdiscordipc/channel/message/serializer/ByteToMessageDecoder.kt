package dev.caoimhe.kdiscordipc.channel.message.serializer

import dev.caoimhe.kdiscordipc.KDiscordIPC
import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.channel.message.inbound.InboundMessageData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Converts a [Message.Raw] to [Message.Inbound]
 */
object ByteToMessageDecoder {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Converts a [Message.Raw] to [Message.Inbound].
     * See [InboundMessageData.Companion] for how data is de-serialized.
     */
    fun decode(message: Message.Raw): Message.Inbound {
        val string = message.data.decodeToString()
        KDiscordIPC.logger.debug("Received: {}", string)

        val data = json.decodeFromString<InboundMessageData>(string)
        return Message.Inbound(message.opcode, data)
    }
}
