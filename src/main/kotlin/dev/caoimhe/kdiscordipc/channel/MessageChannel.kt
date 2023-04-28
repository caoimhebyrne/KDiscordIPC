package dev.caoimhe.kdiscordipc.channel

import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.channel.message.serializer.ByteToMessageDecoder
import dev.caoimhe.kdiscordipc.channel.message.serializer.MessageToByteEncoder
import dev.caoimhe.kdiscordipc.socket.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Used for sending/reading messages through the socket.
 *
 * @see Socket
 */
class MessageChannel(
    private val scope: CoroutineScope,
    private val socket: Socket
) {
    /**
     * A flow of messages being read from the Discord client
     */
    val messages =
        flow {
            while (socket.isConnected) {
                val message = ByteToMessageDecoder.decode(socket.read())
                emit(message)
            }
        }.flowOn(Dispatchers.IO)

    /**
     * A flow of messages to be sent to the Discord client
     */
    private val outbound = MutableSharedFlow<ByteArray>()

    init {
        outbound.onEach {
            socket.write(it)
        }.launchIn(scope)
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
    suspend fun <T> send(message: Message.Outbound<T>) {
        // TODO: Error handling

        val encoded = MessageToByteEncoder.encode(message)
        outbound.emit(encoded)
    }
}
