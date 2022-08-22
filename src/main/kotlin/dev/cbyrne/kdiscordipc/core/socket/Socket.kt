package dev.cbyrne.kdiscordipc.core.socket

import dev.cbyrne.kdiscordipc.core.socket.impl.UnixSocket
import dev.cbyrne.kdiscordipc.core.socket.impl.WindowsSocket
import dev.cbyrne.kdiscordipc.core.util.Platform
import dev.cbyrne.kdiscordipc.core.util.platform
import java.io.File

/**
 * A [Socket] is the base class for a socket implementation.
 *   - On macOS and Linux, this will be backed by a Unix Domain Socket
 *   - On Windows, this will be backed by a RandomAccessFile
 *
 * @see dev.cbyrne.kdiscordipc.core.socket.impl.UnixSocket
 * @see dev.cbyrne.kdiscordipc.core.socket.impl.WindowsSocket
 */
interface Socket {
    val connected: Boolean

    fun connect(file: File)
    fun read(): RawPacket
    fun write(bytes: ByteArray)
    fun close()
}

object SocketProvider {
    @JvmStatic
    fun systemDefault(): Socket {
        if (platform == Platform.UNKNOWN)
            throw NotImplementedError()

        if (platform == Platform.WINDOWS)
            return WindowsSocket()

        return UnixSocket()
    }
}