package dev.caoimhe.kdiscordipc.socket

import dev.caoimhe.kdiscordipc.channel.message.Message
import dev.caoimhe.kdiscordipc.exception.SocketException
import java.io.File

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
    fun connect(file: File)

    /**
     * Attempts to disconnect from the socket
     *
     * @throws SocketException
     */
    @Throws(SocketException::class)
    fun disconnect()

    /**
     * Attempts to read a raw message from the socket
     */
    @Throws(SocketException::class)
    fun read(): Message.Raw

    /**
     * Attempts to write data to the socket
     *
     * @throws SocketException
     */
    @Throws(SocketException::class)
    fun write(data: ByteArray)
}
