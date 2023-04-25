package dev.caoimhe.kdiscordipc.channel

import dev.caoimhe.kdiscordipc.socket.Socket
import java.nio.file.Path

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
    fun connect(file: Path) {
        socket.connect(file)
    }
}
