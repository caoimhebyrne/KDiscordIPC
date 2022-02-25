package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.activity.*
import dev.cbyrne.kdiscordipc.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.event.impl.ReadyEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val logger: Logger = LogManager.getLogger("Example")

fun main() {
    val ipc = KDiscordIPC("945428344806183003")
    logger.info("Starting example!")

    ipc.activityManager.activity = activity("Hello", "world") {
        largeImage("https://avatars.githubusercontent.com/u/71222289?v=4", "KDiscordIPC")
        smallImage("https://avatars.githubusercontent.com/u/71222289?v=4", "Testing")

        party("test", listOf())
        button("Click me", "https://google.com")
        timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
    }

    ipc.on<ReadyEvent> {
        logger.info("Ready! (${it.data.user.username}#${it.data.user.discriminator})")
    }

    ipc.on<ErrorEvent> {
        logger.error("Error (${it.data.code}): ${it.data.message}")
    }

    ipc.connect()
}