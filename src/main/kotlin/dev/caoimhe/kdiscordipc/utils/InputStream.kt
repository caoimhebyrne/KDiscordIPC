package dev.caoimhe.kdiscordipc.utils

import java.io.InputStream
import java.nio.ByteBuffer

fun InputStream.readBytes(length: Int): ByteArray {
    val array = ByteArray(length)
    read(array, 0, length)

    return array
}

fun InputStream.readLittleEndianInt(): Int {
    val bytes = readBytes(4)
    val buffer = ByteBuffer.wrap(bytes)

    return buffer.int.reverse()
}
