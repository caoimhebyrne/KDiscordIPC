package dev.caoimhe.kdiscordipc.utils

import java.io.DataInput
import java.nio.ByteBuffer

fun DataInput.readBytes(length: Int): ByteArray {
    val array = ByteArray(length)
    readFully(array, 0, length)

    return array
}

fun DataInput.readLittleEndianInt(): Int {
    val bytes = readBytes(4)
    val buffer = ByteBuffer.wrap(bytes)

    return buffer.int.reverse()
}
