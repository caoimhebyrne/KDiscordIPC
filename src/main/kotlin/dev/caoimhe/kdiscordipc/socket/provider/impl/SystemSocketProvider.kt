package dev.caoimhe.kdiscordipc.socket.provider.impl

import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.socket.impl.JUnixSocket
import dev.caoimhe.kdiscordipc.socket.impl.WindowsSocket
import dev.caoimhe.kdiscordipc.socket.provider.SocketImplementationProvider
import dev.caoimhe.kdiscordipc.utils.OperatingSystem
import java.io.File

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

    /**
     * Attempts to find a file to connect with the Discord client's IPC server.
     *
     * This is a recursive function, if no [index] is supplied, it will be defaulted to 0.
     * If ``$TEMP/discord-ipc-0`` doesn't exist, it will try ipc-1...ipc-9 until it finds one.
     *
     * @throws SocketException.NotFound If the socket file could not be found.
     */
    @Throws(SocketException.NotFound::class)
    override fun determineLocation(index: Int): File {
        // Discord doesn't allow more than 9 sockets to be open
        if (index > 9)
            throw SocketException.NotFound()

        val base = if (OperatingSystem.current() == OperatingSystem.Windows)
            "\\\\?\\pipe\\"
        else
            OperatingSystem.unixTemporaryDirectory()

        val file = File(base, "discord-ipc-${index}")
        return file.takeIf { it.exists() } ?: determineLocation(index + 1)
    }
}
