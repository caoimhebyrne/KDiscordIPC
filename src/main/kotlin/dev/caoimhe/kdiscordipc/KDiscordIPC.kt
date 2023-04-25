package dev.caoimhe.kdiscordipc

import dev.caoimhe.kdiscordipc.socket.provider.SocketImplementationProvider
import dev.caoimhe.kdiscordipc.socket.provider.impl.SystemSocketProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.slf4j.LoggerFactory

/**
 * The class used to communicate with Discord.
 *
 * The method of communication is decided by the [SocketImplementationProvider], which by default is [SystemSocketProvider].
 *
 * @param clientID                      The client ID of your application provided by Discord
 * @param scope                         The coroutines scope to use for the execution of KDiscordIPC
 * @param socketImplementationProvider  Provides a socket implementation to be used when communcating with the Discord client
 */
class KDiscordIPC(
    private val clientID: String,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO),
    socketImplementationProvider: SocketImplementationProvider = SystemSocketProvider
) {
    /**
     * Attempts to connect to the Discord client.
     */
    suspend fun connect() {
        // 1. Validate client ID
    }

    companion object {
        /**
         * The logger used internally by KDiscordIPC for debug/error purposes
         */
        internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    }
}
