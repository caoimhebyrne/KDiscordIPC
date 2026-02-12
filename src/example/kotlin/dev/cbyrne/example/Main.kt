package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.*
import dev.cbyrne.kdiscordipc.core.event.impl.DisconnectedEvent
import dev.cbyrne.kdiscordipc.data.activity.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

val logger: Logger = LogManager.getLogger("Example")

suspend fun main() {
    val ipc = KDiscordIPC("945428344806183003")
    logger.info("Starting example!")

    ipc.on<ReadyEvent> {
        logger.info("Ready! (${data.user.username}#${data.user.discriminator})")

        // Set the user's activity (a.k.a. rich presence)
        ipc.activityManager.setActivity("Hello") {
            largeImage("https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/62fddf0fde45a8baedcc7ee5_847541504914fd33810e70a0ea73177e%20(2)-1.png", "KDiscordIPC")
            smallImage("https://cdn.prod.website-files.com/6257adef93867e50d84d30e2/62fddf0fde45a8baedcc7ee5_847541504914fd33810e70a0ea73177e%20(2)-1.png", "Testing")

            party(UUID.randomUUID().toString(), 1, 2)
            secrets(UUID.randomUUID().toString())
//            button("Click me", "https://google.com")
            timestamps(System.currentTimeMillis(), System.currentTimeMillis() + 50000)
        }

        // Subscribe to some events
        ipc.subscribe(DiscordEvent.CurrentUserUpdate)
        ipc.subscribe(DiscordEvent.ActivityJoinRequest)
        ipc.subscribe(DiscordEvent.ActivityJoin)
        ipc.subscribe(DiscordEvent.ActivityInvite)
        ipc.subscribe(DiscordEvent.ActivitySpectate)

        // Get a specific user by ID
        val user = ipc.userManager.getUser("843135686173392946")
        logger.info("User by ID: $user")

        // Get the user's friend list
        val relationships = ipc.relationshipManager.getRelationships()
        logger.info("Relationships: ${relationships.size}")
    }

    ipc.on<DisconnectedEvent> {
        logger.error("Disconnected!")
    }

    ipc.on<ErrorEvent> {
        logger.error("IPC communication error (${data.code}): ${data.message}")
    }

    ipc.on<CurrentUserUpdateEvent> {
        logger.info("Current user updated!")
    }

    ipc.on<ActivityJoinEvent> {
        logger.info("The user has joined someone else's party! ${data.secret}")
    }

    ipc.on<ActivityInviteEvent> {
        logger.info("We have been invited to join ${data.user.username}'s party! (${data.activity.party.id})")

        ipc.activityManager.acceptInvite(data)
    }

    ipc.on<VoiceSettingsUpdateEvent> {
        logger.info("Voice settings updated! User is now ${if (this.data.mute) "" else "not "}muted")
    }

    ipc.connect()
}
