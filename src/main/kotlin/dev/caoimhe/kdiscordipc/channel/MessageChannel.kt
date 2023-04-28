package dev.caoimhe.kdiscordipc.channel

import dev.caoimhe.kdiscordipc.KDiscordIPC
import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.channel.message.serializer.ByteToMessageDecoder
import dev.caoimhe.kdiscordipc.channel.message.serializer.MessageToByteEncoder
import dev.caoimhe.kdiscordipc.socket.Socket
import kotlinx.coroutines.flow.flow
import java.io.File

/**
 * Used for sending/reading messages through the socket.
 *
 * @see Socket
 */
class MessageChannel(
    private val socket: Socket
) {
    /**
     * A flow of messages being read from the Discord client
     */
    val messages = flow {
        while (socket.isConnected) {
            val message = ByteToMessageDecoder.decode(socket.read())
            KDiscordIPC.logger.debug("Received: {}", message)

            emit(message)
        }
    }

    /**
     * Establishes a connection with the Discord client through the [socket].
     */
    fun connect(file: File) {
        // TODO: Error handling

        socket.connect(file)
    }

    /**
     * Attempts to send a [Message] to the Discord client through the [socket].
     */
    fun <T> send(message: Message.Outbound<T>) {
        // TODO: Error handling

        val encoded = MessageToByteEncoder.encode(message)
        socket.write(encoded)
    }
}
