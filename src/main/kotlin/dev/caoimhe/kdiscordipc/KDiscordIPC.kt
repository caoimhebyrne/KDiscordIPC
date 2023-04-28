package dev.caoimhe.kdiscordipc

import dev.caoimhe.kdiscordipc.channel.MessageChannel
import dev.caoimhe.kdiscordipc.channel.message.inbound.DispatchMessageData
import dev.caoimhe.kdiscordipc.channel.message.outbound.HandshakeMessage
import dev.caoimhe.kdiscordipc.channel.message.outbound.SubscribeMessage
import dev.caoimhe.kdiscordipc.event.data.Event
import dev.caoimhe.kdiscordipc.event.data.EventData
import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData
import dev.caoimhe.kdiscordipc.exception.SocketException
import dev.caoimhe.kdiscordipc.socket.provider.SocketImplementationProvider
import dev.caoimhe.kdiscordipc.socket.provider.impl.SystemSocketProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    private val channel = MessageChannel(scope, socketImplementationProvider.provide())

    /**
     * Used for dispatching events to listeners
     */
    private val _events = MutableSharedFlow<EventData>()

    /**
     * The events that we are currently / about to subscribe to
     */
    private val subscribedEvents = mutableSetOf<Event<*>>()

    init {
        // Subscribe to any events when we are ready :)
        this.onReady {
            subscribedEvents.forEach {
                channel.send(SubscribeMessage(it))
            }
        }
    }

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

        // 4. Attempt to dispatch any events/packets received
        channel.messages.collect {
            when (it.data) {
                // When we receive the DISPATCH message, the client has sent us a new event!
                is DispatchMessageData<*> -> {
                    _events.emit(it.data.data)
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
    fun onReady(callback: suspend (ReadyEventData) -> Unit) =
        on(Event.Ready, callback)

    /**
     * Fired when we receive an event from the Discord client
     */
    inline fun <reified T> on(event: Event<T>, noinline callback: suspend (T) -> Unit) =
        on(event, T::class.java, callback)

    @Suppress("UNCHECKED_CAST")
    fun <T> on(event: Event<T>, clazz: Class<T>, callback: suspend (T) -> Unit) {
        if (event.subscribed) {
            logger.info("Will subscribe to ${event.id}!")
            subscribedEvents.add(event)
        }

        _events
            .filter { clazz.isInstance(it) }
            .onEach { data ->
                scope.launch { callback(data as T) }
            }.launchIn(scope)
    }

    companion object {
        /**
         * The logger used internally by KDiscordIPC for debug/error purposes
         */
        internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    }
}
