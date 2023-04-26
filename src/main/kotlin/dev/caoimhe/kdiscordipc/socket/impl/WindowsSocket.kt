package dev.caoimhe.kdiscordipc.socket.impl

import dev.caoimhe.kdiscordipc.channel.message.RawMessage
import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.socket.Socket
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.file.Path

/**
 * Uses [RandomAccessFile] to communicate with a specified file.
 */
class WindowsSocket : Socket {
    private lateinit var randomAccessFile: RandomAccessFile
    private var connected: Boolean = false

    override val isConnected: Boolean
        get() = connected

    override fun connect(file: Path) {
        try {
            randomAccessFile = RandomAccessFile(file.toFile(), "rw")
            connected = true
        } catch (e: FileNotFoundException) {
            // Thrown if we can't find the socket
            throw SocketException.NotFound(file)
        } catch (e: SecurityException) {
            // Thrown if we don't have the correct permissions
            throw SocketException.NotAllowed(file)
        }
    }

    override fun disconnect() {
        connected = false

        try {
            randomAccessFile.close()
        } catch (e: IOException) {
            if (isClosedException(e)) {
                throw SocketException.Closed
            }

            throw SocketException.Generic(e)
        }
    }

    override fun read(): RawMessage {
        TODO("Not yet implemented")
    }

    override fun write(data: ByteArray) {
        try {
            randomAccessFile.write(data)
        } catch (e: IOException) {
            if (isClosedException(e)) {
                throw SocketException.Closed
            }

            throw SocketException.Generic(e)
        }
    }

    /**
     * These are two known messages when the stream is being closed, and we attempt to write to it.
     */
    private fun isClosedException(e: IOException): Boolean {
        val message = e.message?.lowercase() ?: return false
        return message.contains("stream closed") || message.contains("pipe is being closed")
    }
}
