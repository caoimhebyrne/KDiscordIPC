package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.user.User
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

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
        ipc.firePacketSend(CommandPacket.GetUser(CommandPacket.GetUser.Arguments(id)))

        return ipc.packets
            .filterIsInstance<CommandPacket.GetUser>()
            .mapNotNull { it.data }
            .first { it.id == id }
    }

    fun subscribeToUserUpdates() =
        ipc.subscribe("CURRENT_USER_UPDATE")
}