package dev.caoimhe.kdiscordipc.socket.impl

import dev.caoimhe.kdiscordipc.socket.Socket
import dev.caoimhe.kdiscordipc.exception.SocketException
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.IOException
import java.nio.file.Path
import java.net.SocketException as JavaNetSocketException

/**
 * Uses [AFUNIXSocket] to communicate with a specified file.
 */
class JUnixSocket : Socket {
    private val socket = AFUNIXSocket.newInstance()

    override val isConnected: Boolean
        get() = socket.isConnected

    @Throws(SocketException::class)
    override fun connect(file: Path) {
        try {
            socket.connect(AFUNIXSocketAddress.of(file))
        } catch (e: JavaNetSocketException) {
            // java.net.SocketException is thrown by `connect` when the socket is closed
            throw SocketException.Closed
        }
    }

    @Throws(SocketException::class)
    override fun disconnect() {
        try {
            socket.close()
        } catch (e: IOException) {
            // We should wrap any IOException errors that occur
            throw SocketException.Generic(e)
        }
    }
}
