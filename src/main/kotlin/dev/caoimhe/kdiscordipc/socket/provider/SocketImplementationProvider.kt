package dev.caoimhe.kdiscordipc.socket.provider

import dev.caoimhe.kdiscordipc.socket.Socket

/**
 * Provides a [Socket] to be used for communicating with the Discord client.
 *
 * @see dev.caoimhe.kdiscordipc.socket.provider.impl.SystemSocketProvider
 */
interface SocketImplementationProvider {
    fun provide(): Socket
}
