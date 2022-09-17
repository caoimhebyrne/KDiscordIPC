package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.reverse
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.File
import java.nio.ByteBuffer

class UnixSocket : Socket {
    private val socket = AFUNIXSocket.newInstance()

    override val connected: Boolean
        get() = socket.isConnected

    override fun connect(file: File) {
        socket.connect(AFUNIXSocketAddress.of(file))
    }

    override fun read(): RawPacket {
        val stream = socket.inputStream
        val opcode = readLittleEndianInt()
        val length = readLittleEndianInt()
        val pieces = mutableListOf(readBytes(stream.available()))

        while (length > pieces.sumOf { it.size }) {
            pieces.add(readBytes(stream.available()))
        }

        val data = pieces.flatten()
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

private fun List<ByteArray>.flatten(): ByteArray {
    var byteArray = ByteArray(0)

    forEach {
        byteArray += it
    }

    return byteArray
}
