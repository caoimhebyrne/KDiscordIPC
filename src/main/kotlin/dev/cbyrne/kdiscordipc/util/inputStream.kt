package dev.cbyrne.kdiscordipc.util

import java.io.InputStream

@Suppress("ControlFlowWithEmptyBody")
fun InputStream.onBytes(callback: (ByteArray) -> Unit) {
    while (available() == 0) {
    }

    callback(readBytes())
}