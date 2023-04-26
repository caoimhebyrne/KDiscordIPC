package dev.caoimhe.kdiscordipc.socket.impl

import dev.caoimhe.kdiscordipc.channel.message.RawMessage
import dev.caoimhe.kdiscordipc.socket.Socket
import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.utils.readBytes
import dev.caoimhe.kdiscordipc.utils.readLittleEndianInt
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.DataInputStream
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
        // If the socket is already close, we do not need to close it
        if (socket.isClosed) {
            throw SocketException.Disconnected
        }

        try {
            socket.close()
        } catch (e: IOException) {
            // We should wrap any IOException errors that occur
            throw SocketException.Generic(e)
        }
    }

    override fun read(): RawMessage {
        // The first two integers are the opcode, and the length.
        val opcode = socket.inputStream.readLittleEndianInt()
        val length = socket.inputStream.readLittleEndianInt()

        // Ensures that we read all of our data correctly
        val stream = DataInputStream(socket.inputStream)
        val data = ByteArray(length)
        stream.readFully(data)

        return RawMessage(opcode, length, data)
    }

    @Throws(SocketException::class)
    override fun write(data: ByteArray) {
        // If the socket is already closed, we do not want to write to it
        if (socket.isClosed) {
            throw SocketException.Disconnected
        }

        try {
            socket.outputStream.write(data)
        } catch (e: IOException) {
            // We should wrap any IOException errors that occur
            throw SocketException.Generic(e)
        }
    }
}
