package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.AuthenticatePacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.AuthorizePacket
import dev.cbyrne.kdiscordipc.manager.Manager
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.AuthenticatePacket as InboundAuthenticatePacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.AuthorizePacket as InboundAuthorizePacket

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
    suspend fun authenticate(token: String? = null): InboundAuthenticatePacket.Data {
        val response: InboundAuthenticatePacket = ipc.sendPacket(AuthenticatePacket(token))
        return response.data
    }

    suspend fun authorize(scopes: Array<String>? = null, clientId: String? = null, rpcToken: String? = null, username: String? = null): InboundAuthorizePacket.Data {
        val response: InboundAuthorizePacket = ipc.sendPacket(AuthorizePacket(scopes, clientId, rpcToken, username))
        return response.data
    }
}