package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.socket.Socket
import java.io.File
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SocketChannel

class UnixSocket : Socket {
    private val intBuffer = ByteBuffer.allocate(4)
        .apply { order(ByteOrder.LITTLE_ENDIAN) }

    private lateinit var socket: SocketChannel

    override val connected: Boolean
        get() = if (::socket.isInitialized) socket.isConnected else false

    override fun connect(file: File) {
        socket = SocketChannel.open(UnixDomainSocketAddress.of(file.toPath()))
    }

    override fun read(): RawPacket {
        val opcode = readLittleEndianInt()
        val length = readLittleEndianInt()

        val data = ByteBuffer.allocate(length)
        socket.read(data)

        return RawPacket(opcode, length, data.array())
    }

    private fun readLittleEndianInt(): Int {
        socket.read(intBuffer)
        intBuffer.flip()

        return intBuffer.getInt()
    }

    override fun write(bytes: ByteArray) {
        socket.write(ByteBuffer.wrap(bytes))
    }

    override fun close() {
        socket.close()
    }
}
