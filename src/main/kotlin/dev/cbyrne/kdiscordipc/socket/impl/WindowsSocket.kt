package dev.cbyrne.kdiscordipc.socket.impl

import dev.cbyrne.kdiscordipc.socket.Socket
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

    override val inputStream = RandomAccessInputStream(randomAccessFile)
    override val outputStream = RandomAccessOutputStream(randomAccessFile)

    override fun connect(file: File) {
        randomAccessFile = RandomAccessFile(file, "rw")
        _connected = true
    }

    override fun close() {
        randomAccessFile.close()
        _connected = false
    }

    /**
     * An [InputStream] which is backed by a [RandomAccessFile]
     */
    class RandomAccessInputStream(private val file: RandomAccessFile) : InputStream() {
        override fun available() = file.length().toInt()
        override fun close() = file.close()

        override fun read(): Int = file.read()
        override fun read(b: ByteArray) = file.read(b)
        override fun read(b: ByteArray, off: Int, len: Int) = file.read(b, off, len)
    }

    /**
     * An [OutputStream] which is backed by a [RandomAccessFile]
     */
    class RandomAccessOutputStream(private val file: RandomAccessFile) : OutputStream() {
        override fun write(b: Int) = file.write(b)
        override fun write(b: ByteArray) = file.write(b)
        override fun write(b: ByteArray, off: Int, len: Int) = file.write(b, off, len)
        override fun close() = file.close()
    }
}