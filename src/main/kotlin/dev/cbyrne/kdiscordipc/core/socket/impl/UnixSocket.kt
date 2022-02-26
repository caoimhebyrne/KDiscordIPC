package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.reverse
import java.io.File
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class UnixSocket : Socket {
    private val socket = SocketChannel.open(StandardProtocolFamily.UNIX)

    override val connected: Boolean
        get() = socket.isConnected

    override fun connect(file: File) {
        socket.connect(UnixDomainSocketAddress.of(file.absolutePath))
    }

    override fun read(): RawPacket {
        val opcode = socket.readLittleEndianInt()
        val length = socket.readLittleEndianInt()
        val data = socket.readBytes(length)

        return RawPacket(opcode, length, data.array())
    }

    private fun SocketChannel.readLittleEndianInt() =
        readBytes(4).int.reverse()

    private fun SocketChannel.readBytes(length: Int): ByteBuffer {
        val data = ByteBuffer.allocate(length)
        while (data.position() < data.limit()) {
            read(data)
        }
        data.flip()

        return data
    }

    override fun write(bytes: ByteArray) {
        socket.write(ByteBuffer.wrap(bytes))
    }

    override fun close() {
        socket.close()
    }
}