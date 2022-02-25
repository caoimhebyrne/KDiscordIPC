package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.Socket
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class UnixSocket : Socket {
    private val socket = AFUNIXSocket.newInstance()

    override val inputStream: InputStream
        get() = socket.inputStream

    override val outputStream: OutputStream
        get() = socket.outputStream

    override val connected: Boolean
        get() = socket.isConnected

    override fun connect(file: File) {
        socket.connect(AFUNIXSocketAddress.of(file))
    }

    override fun close() {
        socket.close()
    }
}