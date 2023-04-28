package dev.caoimhe.kdiscordipc

import dev.caoimhe.kdiscordipc.channel.MessageChannel
import dev.caoimhe.kdiscordipc.channel.message.outbound.HandshakeMessage
import dev.caoimhe.kdiscordipc.exception.SocketException
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
    private val socketImplementationProvider: SocketImplementationProvider = SystemSocketProvider,
) {
    /**
     * Used for sending/reading messages through the socket
     */
    private val channel = MessageChannel(socketImplementationProvider.provide())

    /**
     * Attempts to connect to the Discord client.
     *
     * @throws SocketException
     */
    @Throws(SocketException::class)
    suspend fun connect() {
        // 1. TODO: Validate client ID

        // 2. Connect to the socket via the determined location
        val location = socketImplementationProvider.determineLocation()
        channel.connect(location)

        // 3. Send the handshake
        channel.send(HandshakeMessage(clientID))

        // 4. TODO: Dispatch events/any received information
        channel.messages.collect {
            logger.debug("{}", it)
        }
    }

    companion object {
        /**
         * The logger used internally by KDiscordIPC for debug/error purposes
         */
        internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    }
}

suspend fun main() {
    val ipc = KDiscordIPC("1100847786448146505")
    ipc.connect()
}
