package dev.caoimhe.kdiscordipc.channel

import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.channel.message.serializer.MessageToByteEncoder
import dev.caoimhe.kdiscordipc.socket.Socket
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
