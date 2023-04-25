package dev.caoimhe.kdiscordipc.socket.provider

import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.socket.Socket
import java.nio.file.Path

/**
 * Provides a [Socket] to be used for communicating with the Discord client.
 *
 * @see dev.caoimhe.kdiscordipc.socket.provider.impl.SystemSocketProvider
 */
interface SocketImplementationProvider {
    fun provide(): Socket

    /**
     * Attempts to find where the Discord socket file is located.
     *
     * @throws SocketException.NotFound If the socket file could not be found
     */
    @Throws(SocketException.NotFound::class)
    fun determineLocation(index: Int = 0): Path
}
