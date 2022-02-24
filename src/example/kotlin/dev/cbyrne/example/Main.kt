package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.activity.*
import dev.cbyrne.kdiscordipc.observer.KDiscordIPCObserver
import dev.cbyrne.kdiscordipc.packet.impl.command.data.ReadyEventData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val logger: Logger = LogManager.getLogger("Example")

fun main() {
    val ipc = KDiscordIPC("945428344806183003")
    ipc.activity = activity("Hello", "world") {
        largeImage("https://avatars.githubusercontent.com/u/71222289?v=4", "KDiscordIPC")
        smallImage("https://avatars.githubusercontent.com/u/71222289?v=4", "Testing")

        party("test", listOf(1, 5))
        button("Click me", "https://google.com")
        timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
//        secrets("join", "match", "spectate")
    }

    ipc.observer = object : KDiscordIPCObserver {
        override fun onReady(data: ReadyEventData) {
            logger.info("Ready! (${data.user.username}#${data.user.discriminator})")
        }
    }
    ipc.connect()
}