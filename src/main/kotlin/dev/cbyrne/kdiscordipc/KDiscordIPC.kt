@file:Suppress("unused")

package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.core.event.data.ErrorEventData
import dev.cbyrne.kdiscordipc.core.event.impl.ActivityInviteEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ActivityJoinEvent
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.core.event.impl.VoiceSettingsUpdateEvent
import dev.cbyrne.kdiscordipc.core.event.impl.DisconnectedEvent
import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.DispatchEventPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.ErrorPacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.SubscribePacket
import dev.cbyrne.kdiscordipc.core.packet.pipeline.MessageToByteEncoder
import dev.cbyrne.kdiscordipc.core.socket.Socket
import dev.cbyrne.kdiscordipc.core.socket.SocketProvider
import dev.cbyrne.kdiscordipc.core.socket.handler.SocketHandler
import dev.cbyrne.kdiscordipc.manager.impl.ActivityManager
import dev.cbyrne.kdiscordipc.manager.impl.ApplicationManager
import dev.cbyrne.kdiscordipc.manager.impl.RelationshipManager
import dev.cbyrne.kdiscordipc.manager.impl.UserManager
import dev.cbyrne.kdiscordipc.manager.impl.VoiceSettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.util.*
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.SubscribePacket as InboundSubscribePacket

class KDiscordIPC(
    private val clientID: String,
    socketSupplier: () -> Socket = SocketProvider::systemDefault,
    val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
) {
    companion object {
        internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    }

    private val socketHandler = SocketHandler(scope, socketSupplier) {
        scope.launch {
            this@KDiscordIPC._events.emit(DisconnectedEvent())
        }
    }

    val connected: Boolean
        get() = socketHandler.connected

    val activityManager = ActivityManager(this)
    val applicationManager = ApplicationManager(this)
    val relationshipManager = RelationshipManager(this)
    val userManager = UserManager(this)
    val voiceSettingsManager = VoiceSettingsManager(this)

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _packets = MutableSharedFlow<InboundPacket>()
    val packets = _packets.asSharedFlow()

    /**
     * Connects to the Discord IPC server
     *
     * @param index The discord IPC socket to connect to. i.e. $TMPDIR/discord-ipc-{index}
     * @see SocketHandler.connect
     */
    suspend fun connect(index: Int = 0) {
        activityManager.init()
        applicationManager.init()
        relationshipManager.init()
        userManager.init()
        voiceSettingsManager.init()

        socketHandler.connect(index)
        writePacket(HandshakePacket(1, clientID))

        socketHandler.events.collect {
            when (it) {
                is DispatchEventPacket.Ready -> _events.emit(ReadyEvent(it.data))
                is DispatchEventPacket.UserUpdate -> _events.emit(CurrentUserUpdateEvent(it.data))
                is DispatchEventPacket.VoiceSettingsUpdate -> _events.emit(VoiceSettingsUpdateEvent(it.data))
                is DispatchEventPacket.ActivityJoin -> _events.emit(ActivityJoinEvent(it.data))
                is DispatchEventPacket.ActivityInvite -> _events.emit(ActivityInviteEvent(it.data))
                is DispatchEventPacket.Error -> _events.emit(ErrorEvent(it.data))
                is ErrorPacket -> _events.emit(ErrorEvent(ErrorEventData(it.code, it.message)))
                else -> _packets.emit(it)
            }
        }
    }

    @JvmName("onEvent")
    suspend inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) =
        events.filterIsInstance<T>().onEach { event ->
            scope.launch { consumer(event) }
        }.launchIn(scope)

    @JvmName("onPacket")
    suspend inline fun <reified T : InboundPacket> on(noinline consumer: suspend T.() -> Unit) =
        packets.filterIsInstance<T>().onEach { event ->
            scope.launch { consumer(event) }
        }.launchIn(scope)

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
    suspend fun subscribe(event: DiscordEvent) {
        sendPacket<InboundSubscribePacket>(SubscribePacket(event))
    }

    private suspend fun writePacket(packet: OutboundPacket, nonce: String? = null) {
        val bytes = MessageToByteEncoder.encode(packet, nonce)
        socketHandler.write(bytes)
    }

    internal suspend inline fun <reified T : InboundPacket> sendPacket(packet: OutboundPacket): T {
        val nonce = UUID.randomUUID().toString()
        writePacket(packet, nonce)

        return packets.filterIsInstance<T>().first { it.nonce == nonce }
    }
}
