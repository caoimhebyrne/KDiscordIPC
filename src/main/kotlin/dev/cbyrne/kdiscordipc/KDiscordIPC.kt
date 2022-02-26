@file:Suppress("unused")

package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.manager.impl.ActivityManager
import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.core.event.data.ErrorEventData
import dev.cbyrne.kdiscordipc.core.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.core.packet.handler.impl.CommandPacketHandler
import dev.cbyrne.kdiscordipc.core.packet.handler.impl.ErrorPacketHandler
import dev.cbyrne.kdiscordipc.core.packet.handler.impl.HandshakePacketHandler
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.core.packet.impl.ErrorPacket
import dev.cbyrne.kdiscordipc.core.packet.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.core.packet.pipeline.MessageToByteEncoder
import dev.cbyrne.kdiscordipc.core.socket.handler.SocketHandler
import dev.cbyrne.kdiscordipc.manager.impl.ApplicationManager
import dev.cbyrne.kdiscordipc.manager.impl.RelationshipManager
import dev.cbyrne.kdiscordipc.manager.impl.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class KDiscordIPC(private val clientID: String) {
    internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    internal val packetHandlers = mutableMapOf<Int, PacketHandler<*>>()

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

    private val _packets = MutableSharedFlow<Packet>()
    val packets = _packets.asSharedFlow()

    init {
        addPacketHandler(0x00, HandshakePacketHandler())
        addPacketHandler(0x01, CommandPacketHandler())
        addPacketHandler(0x02, ErrorPacketHandler())
    }

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
                is CommandPacket.DispatchEvent.Ready -> _events.emit(ReadyEvent(it.data))
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
    suspend inline fun <reified T : Packet> on(noinline consumer: suspend T.() -> Unit) =
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
     * Adds a packet handler for a specific opcode
     *
     * @see PacketHandler
     */
    private fun addPacketHandler(opcode: Int, handler: PacketHandler<*>) {
        check(packetHandlers[opcode] == null) {
            "A packet handler has already been registered for the opcode $opcode!"
        }

        packetHandlers[opcode] = handler
    }

    /**
     * Fired when we want to send a packet
     *
     * @see SocketHandler.write
     */
    internal fun firePacketSend(packet: Packet) {
        val bytes = MessageToByteEncoder.encode(this, packet)
        socketHandler.write(bytes)
    }
}