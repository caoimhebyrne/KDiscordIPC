package dev.caoimhe.kdiscordipc.socket

import java.nio.file.Path
import dev.caoimhe.kdiscordipc.exception.SocketException

/**
 * An abstract socket, used for communicating with the Discord client.
 * Any anticipated exceptions should be caught and a SocketException should be re-thrown.
 */
interface Socket {
    /**
     * If the socket is currently connected or not
     */
    val isConnected: Boolean

    /**
     * Attempts to connect to the specified [file].
     *
     * @throws SocketException
     */
    @Throws(SocketException::class)
    fun connect(file: Path)

    /**
     * Attempts to disconnect from the socket
     *
     * @throws SocketException
     */
    @Throws(SocketException::class)
    fun disconnect()

    // TODO: Writing/reading functions
}
