package dev.cbyrne.kdiscordipc.core.util

import java.io.InputStream

fun InputStream.readAvailableBytes(): ByteArray {
    val available = available()
    val bytes = ByteArray(available)
    read(bytes, 0, available)

    return bytes
}