package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.reverse
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.DataInputStream
import java.io.File
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SocketChannel

class UnixSocket : Socket {
    private val socket = AFUNIXSocket.newInstance()

    override val connected: Boolean
        get() = socket.isConnected

    override fun connect(file: File) {
        socket.connect(AFUNIXSocketAddress.of(file))
    }

    override fun read(): RawPacket {
        val opcode = readLittleEndianInt()
        val length = readLittleEndianInt()

        val stream = DataInputStream(socket.inputStream)
        val data = ByteArray(length)
        stream.readFully(data)

        return RawPacket(opcode, length, data)
    }

    private fun readLittleEndianInt() =
        ByteBuffer.wrap(readBytes(4)).int.reverse()

    private fun readBytes(length: Int): ByteArray {
        val array = ByteArray(length)
        socket.inputStream.read(array, 0, length)

        return array
    }

    override fun write(bytes: ByteArray) {
        socket.outputStream.write(bytes)
    }

    override fun close() {
        socket.close()
    }
}
