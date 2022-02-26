package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.authentication.AuthenticationResponse
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

/**
 * This manager gives you access  to a bearer token for the currently connected Discord user, which you can then use against the Discord REST API.
 */
class ApplicationManager(override val ipc: KDiscordIPC) : Manager() {
    /**
     * **Make sure that you have `http://127.0.0.1` set as a valid redirect URI for your application in the Developer Portal.**
     *
     * Retrieve an oauth2 bearer token for the current user.
     *   - If your application was launched from Discord and you call this function, you will automatically receive the token.
     *   - If the application was not launched from Discord and this method is called, Discord will focus itself and prompt the user for authorization.
     *
     * These bearer tokens are active for seven days, after which they will expire.
     */
    suspend fun getOAuthToken(): AuthenticationResponse {
        ipc.firePacketSend(CommandPacket.Authenticate())

        return ipc.packets
            .filterIsInstance<CommandPacket.Authenticate>()
            .mapNotNull { it.data }
            .first()
    }
}