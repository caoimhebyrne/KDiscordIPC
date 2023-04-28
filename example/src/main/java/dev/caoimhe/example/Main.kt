package dev.caoimhe.example

import dev.caoimhe.kdiscordipc.KDiscordIPC
import dev.caoimhe.kdiscordipc.event.data.Event
import org.apache.logging.log4j.LogManager

suspend fun main() {
    val logger = LogManager.getLogger("Example")
    val ipc = KDiscordIPC("1100847786448146505")

    ipc.onReady { data ->
        logger.info("Connected as {}#{}", data.user.username, data.user.discriminator)
    }

    ipc.on(Event.CurrentUserUpdate) {
        println(it)
    }

    ipc.connect()
}
