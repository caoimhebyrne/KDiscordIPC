package dev.cbyrne.example

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.*
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
        ipc.activityManager.setActivity("Hello", "world") {
            largeImage("https://avatars.githubusercontent.com/u/71222289?v=4", "KDiscordIPC")
            smallImage("https://avatars.githubusercontent.com/u/71222289?v=4", "Testing")

            party("test", listOf(1, 2))
            secrets("sdfdsfhusdfdsjfiodsjfdsoflmskdngnretorenls;kdp[")
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

        // Get an oauth token for the currently logged-in user
        val oauthToken = ipc.applicationManager.getOAuthToken()
        logger.info("Received oauth token from Discord! Expires on: ${oauthToken.expires}")
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
    }

    ipc.connect()
}