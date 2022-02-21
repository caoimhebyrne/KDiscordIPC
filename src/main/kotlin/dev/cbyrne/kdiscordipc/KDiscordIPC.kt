@file:Suppress("unused")

package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.socket.handler.SocketHandler

class KDiscordIPC {
    private val socketHandler = SocketHandler()

    fun connect() {
        socketHandler.connect()
    }

    fun disconnect() {
        socketHandler.disconnect()
    }
}
