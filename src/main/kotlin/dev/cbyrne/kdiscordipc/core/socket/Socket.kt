package dev.cbyrne.kdiscordipc.core.socket

import dev.cbyrne.kdiscordipc.core.socket.impl.UnixSocket
import dev.cbyrne.kdiscordipc.core.socket.impl.WindowsSocket
import dev.cbyrne.kdiscordipc.core.util.Platform
import dev.cbyrne.kdiscordipc.core.util.platform
import dev.cbyrne.kdiscordipc.core.util.reverse
import java.io.File
import java.nio.ByteBuffer

/**
 * A [Socket] is the base class for a socket implementation.
 *   - On macOS and Linux, this will be backed by a Unix Domain Socket
 *   - On Windows, this will be backed by a RandomAccessFile
 *
 * @see dev.cbyrne.kdiscordipc.core.socket.impl.UnixSocket
 * @see dev.cbyrne.kdiscordipc.core.socket.impl.WindowsSocket
 */
interface Socket {
    companion object {
        fun get(): Socket {
            if (platform == Platform.OTHER)
                throw NotImplementedError()

            if (platform == Platform.WINDOWS)
                return WindowsSocket()

            return UnixSocket()
        }
    }

    val connected: Boolean

    fun connect(file: File)
    fun read(): RawPacket
    fun write(bytes: ByteArray)
    fun close()
}
