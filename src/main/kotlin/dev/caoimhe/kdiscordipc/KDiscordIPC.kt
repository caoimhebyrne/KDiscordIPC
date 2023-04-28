package dev.caoimhe.kdiscordipc

import dev.caoimhe.kdiscordipc.channel.MessageChannel
import dev.caoimhe.kdiscordipc.channel.message.inbound.DispatchMessageData
import dev.caoimhe.kdiscordipc.channel.message.outbound.HandshakeMessage
import dev.caoimhe.kdiscordipc.event.data.Event
import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData
import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.socket.provider.SocketImplementationProvider
import dev.caoimhe.kdiscordipc.socket.provider.impl.SystemSocketProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val socketImplementationProvider: SocketImplementationProvider = SystemSocketProvider,
    val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO),
) {
    /**
     * Used for sending/reading messages through the socket
     */
    private val channel = MessageChannel(socketImplementationProvider.provide())

    /**
     * Used for dispatching events to listeners
     */
    private val _events = MutableSharedFlow<Event<*>>()

    /**
     * Used for dispatching events to listeners
     */
    val events = _events.asSharedFlow()

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

        // Attempt to dispatch any events/packets received
        channel.messages.collect {
            when (it.data) {
                // When we receive the DISPATCH message, the client has sent us a new event!
                is DispatchMessageData<*> -> {
                    when (val data = it.data.data) {
                        is ReadyEventData -> _events.emit(Event.Ready(data))
                        else -> error("Unknown event ${it.data.event}")
                    }
                }
            }
        }
    }

    /**
     * Fired when we connect to the Discord client for the first time.
     * A helper function for [on]:
     * ```kt
     * ipc.on<Event.Ready> {}
     * ```
     */
    inline fun onReady(crossinline callback: (ReadyEventData) -> Unit) =
        on<Event.Ready> { callback(it.data) }

    /**
     * Fired when we receive an event from the Discord client
     */
    inline fun <reified T : Event<*>> on(crossinline callback: (T) -> Unit) =
        events.filterIsInstance<T>()
            .onEach { event ->
                callback(event)
            }.launchIn(scope)

    companion object {
        /**
         * The logger used internally by KDiscordIPC for debug/error purposes
         */
        internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    }
}
