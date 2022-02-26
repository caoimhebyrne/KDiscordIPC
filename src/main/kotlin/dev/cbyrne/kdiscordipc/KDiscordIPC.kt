@file:Suppress("unused")

package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.core.event.data.ErrorEventData
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.DispatchEventPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.ErrorPacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.SubscribePacket
import dev.cbyrne.kdiscordipc.core.packet.pipeline.MessageToByteEncoder
import dev.cbyrne.kdiscordipc.core.socket.handler.SocketHandler
import dev.cbyrne.kdiscordipc.manager.impl.ActivityManager
import dev.cbyrne.kdiscordipc.manager.impl.ApplicationManager
import dev.cbyrne.kdiscordipc.manager.impl.RelationshipManager
import dev.cbyrne.kdiscordipc.manager.impl.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.SubscribePacket as InboundSubscribePacket

class KDiscordIPC(private val clientID: String) {
    internal val logger = LoggerFactory.getLogger("KDiscordIPC")

    private val socketHandler = SocketHandler(this)

    val connected: Boolean
        get() = socketHandler.connected

    val activityManager = ActivityManager(this)
    val applicationManager = ApplicationManager(this)
    val relationshipManager = RelationshipManager(this)
    val userManager = UserManager(this)

    val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _packets = MutableSharedFlow<InboundPacket>()
    val packets = _packets.asSharedFlow()

    /**
     * Connects to the Discord IPC server
     *
     * @see SocketHandler.connect
     */
    suspend fun connect() {
        activityManager.init()
        applicationManager.init()
        relationshipManager.init()
        userManager.init()

        socketHandler.connect()
        firePacketSend(HandshakePacket(1, clientID))

        socketHandler.events.collect {
            when (it) {
                is DispatchEventPacket.Ready -> _events.emit(ReadyEvent(it.data))
                is DispatchEventPacket.UserUpdate -> _events.emit(CurrentUserUpdateEvent(it.data))
                is ErrorPacket -> _events.emit(ErrorEvent(ErrorEventData(it.code, it.message)))
                else -> _packets.emit(it)
            }
        }
    }

    @JvmName("onEvent")
    suspend inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) =
        events
            .filterIsInstance<T>()
            .onEach { event ->
                scope.launch { consumer(event) }
            }
            .launchIn(scope)

    @JvmName("onPacket")
    suspend inline fun <reified T : InboundPacket> on(noinline consumer: suspend T.() -> Unit) =
        packets
            .filterIsInstance<T>()
            .onEach { event ->
                scope.launch { consumer(event) }
            }
            .launchIn(scope)

    /**
     * Disconnects from the Discord IPC server
     *
     * @see SocketHandler.disconnect
     */
    fun disconnect() {
        socketHandler.disconnect()
    }

    /**
     * Subscribe to an [Event]
     */
    internal suspend fun subscribe(name: String) {
        firePacketSend(SubscribePacket(name))

        packets
            .filterIsInstance<InboundSubscribePacket>()
            .first { it.data.event == name }
    }

    /**
     * Fired when we want to send a packet
     *
     * @see SocketHandler.write
     */
    internal fun firePacketSend(packet: OutboundPacket) {
        val bytes = MessageToByteEncoder.encode(this, packet)
        socketHandler.write(bytes)
    }
}