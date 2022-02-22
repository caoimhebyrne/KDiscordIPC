package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.observer.KDiscordIPCObserver
import dev.cbyrne.kdiscordipc.packet.impl.dispatch.data.ReadyEventData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val logger: Logger = LogManager.getLogger("Example")

fun main() {
    val ipc = KDiscordIPC("945428344806183003")
    ipc.observer = object : KDiscordIPCObserver {
        override fun onReady(data: ReadyEventData) {
            logger.info("Ready! (${data.user.username}#${data.user.discriminator})")
        }
    }
    ipc.connect()
}