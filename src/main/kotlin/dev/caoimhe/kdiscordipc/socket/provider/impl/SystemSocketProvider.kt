package dev.caoimhe.kdiscordipc.socket.provider.impl

import dev.caoimhe.kdiscordipc.socket.impl.JUnixSocket
import dev.caoimhe.kdiscordipc.socket.impl.WindowsSocket
import dev.caoimhe.kdiscordipc.socket.provider.SocketImplementationProvider
import dev.caoimhe.kdiscordipc.utils.OperatingSystem

/**
 * Provides a socket to be used for communicating with the Discord client.
 *
 * On Windows, a "socket" implementation using [java.io.RandomAccessFile] will be provided.
 * On any other OS, a socket implementation using JUnixSocket will be provided.
 *
 * @see SocketImplementationProvider
 */
object SystemSocketProvider : SocketImplementationProvider {
    /**
     * On Windows, a "socket" implementation using [java.io.RandomAccessFile] will be provided.
     * On any other OS, a socket implementation using JUnixSocket will be provided.
     */
    override fun provide() =
        when (OperatingSystem.current()) {
            OperatingSystem.Windows -> WindowsSocket()
            else -> JUnixSocket()
        }
}
