package dev.cbyrne.kdiscordipc.core.socket.impl

import dev.cbyrne.kdiscordipc.core.socket.RawPacket
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.util.reverse
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile

/**
 * A class representing a Windows [Socket]
 * This uses a RandomAccessFile to expose an [InputStream] and [OutputStream]
 */
class WindowsSocket : Socket {
    private lateinit var randomAccessFile: RandomAccessFile
    private var _connected = false

    override val connected: Boolean
        get() = _connected

    override fun connect(file: File) {
        randomAccessFile = RandomAccessFile(file, "rw")
        _connected = true
    }

    override fun close() {
        randomAccessFile.close()
        _connected = false
    }

    @Suppress("ControlFlowWithEmptyBody")
    override fun read(): RawPacket {
        while (_connected && randomAccessFile.length() == 0L) {
            Thread.sleep(50L)
        }

        val opcode = randomAccessFile.readInt().reverse()
        val length = randomAccessFile.readInt().reverse()

        val data = ByteArray(length)
        randomAccessFile.readFully(data)

        return RawPacket(opcode, length, data)
    }

    override fun write(bytes: ByteArray) {
        randomAccessFile.write(bytes)
    }
}