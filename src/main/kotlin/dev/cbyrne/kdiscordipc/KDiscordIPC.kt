package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.socket.Socket
import dev.cbyrne.kdiscordipc.socket.handler.SocketHandler
import java.io.File
import kotlin.concurrent.thread

class KDiscordIPC {
    private val socketHandler = SocketHandler()

    fun connect() {
        socketHandler.connect()
    }

    fun disconnect() {
        socketHandler.disconnect()
    }
}
