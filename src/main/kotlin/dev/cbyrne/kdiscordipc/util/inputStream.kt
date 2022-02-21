package dev.cbyrne.kdiscordipc.util

import java.io.InputStream

@Suppress("ControlFlowWithEmptyBody")
fun InputStream.onBytes(callback: (ByteArray) -> Unit) {
    while (available() == 0) {
    }

    callback(readAvailableBytes())
}

fun InputStream.readAvailableBytes(): ByteArray {
    val available = available()
    val bytes = ByteArray(available)
    read(bytes, 0, available)

    return bytes
}