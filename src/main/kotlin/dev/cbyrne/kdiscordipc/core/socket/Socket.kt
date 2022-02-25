package dev.cbyrne.kdiscordipc.core.socket

import dev.cbyrne.kdiscordipc.core.socket.impl.UnixSocket
import dev.cbyrne.kdiscordipc.core.socket.impl.WindowsSocket
import dev.cbyrne.kdiscordipc.core.util.Platform
import dev.cbyrne.kdiscordipc.core.util.platform
import java.io.File
import java.io.InputStream
import java.io.OutputStream

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

    val inputStream: InputStream
    val outputStream: OutputStream

    fun connect(file: File)
    fun close()
}
