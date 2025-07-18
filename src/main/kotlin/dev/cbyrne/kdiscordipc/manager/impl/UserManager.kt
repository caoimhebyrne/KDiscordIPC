package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.GetUserPacket
import dev.cbyrne.kdiscordipc.data.user.User
import dev.cbyrne.kdiscordipc.manager.Manager
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.GetUserPacket as InboundGetUserPacket

/**
 * This manager helps you to retrieve basic user information for any user on Discord.
 */
@Suppress("MemberVisibilityCanBePrivate")
class UserManager(override val ipc: KDiscordIPC) : Manager() {
    /**
     * **Before accessing this variable, you need to wait for the [CurrentUserUpdateEvent] event to be fired after running [subscribeToUserUpdates].**
     *
     * Returns information about the connected user account.
     */
    var currentUser: User? = null
        private set

    override suspend fun init() {
        ipc.on<CurrentUserUpdateEvent> {
            currentUser = this.data
        }
    }

    /**
     * Get user information for a specific user id
     *
     * @param id the id of the user to fetch
     */
    suspend fun getUser(id: String): User {
        val response: InboundGetUserPacket = ipc.sendPacket(GetUserPacket(id))
        return response.data
    }

    suspend fun subscribeToUserUpdates() = ipc.subscribe(DiscordEvent.CurrentUserUpdate)
}
