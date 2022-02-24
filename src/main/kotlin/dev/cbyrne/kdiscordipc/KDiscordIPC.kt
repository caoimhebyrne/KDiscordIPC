@file:Suppress("unused")

package dev.cbyrne.kdiscordipc

import dev.cbyrne.kdiscordipc.activity.DiscordActivity
import dev.cbyrne.kdiscordipc.event.Event
import dev.cbyrne.kdiscordipc.event.data.ErrorEventData
import dev.cbyrne.kdiscordipc.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.handler.PacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.CommandPacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.ErrorPacketHandler
import dev.cbyrne.kdiscordipc.packet.handler.impl.HandshakePacketHandler
import dev.cbyrne.kdiscordipc.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.packet.impl.ErrorPacket
import dev.cbyrne.kdiscordipc.packet.impl.HandshakePacket
import dev.cbyrne.kdiscordipc.packet.pipeline.MessageToByteEncoder
import dev.cbyrne.kdiscordipc.socket.handler.SocketHandler
import dev.cbyrne.kdiscordipc.util.currentPid
import org.slf4j.LoggerFactory

class KDiscordIPC(val clientID: String) {
    val subscribers: MutableMap<Class<out Event>, MutableSet<(Event) -> Unit>> = mutableMapOf()

    var activity: DiscordActivity? = null
        set(value) {
            field = value

            if (socketHandler.connected)
                sendActivity(value)
        }

    private val socketHandler = SocketHandler(this)

    internal val logger = LoggerFactory.getLogger("KDiscordIPC")
    internal val packetHandlers = mutableMapOf<Int, PacketHandler<*>>()

    init {
        addPacketHandler(0x00, HandshakePacketHandler())
        addPacketHandler(0x01, CommandPacketHandler())
        addPacketHandler(0x02, ErrorPacketHandler())

        on<ReadyEvent> { activity?.let { sendActivity(activity) } }
    }

    /**
     * Connects to the Discord IPC server
     *
     * @see SocketHandler.connect
     */
    fun connect() {
        socketHandler.connect()
        firePacketSend(HandshakePacket(1, clientID))
    }

    /**
     * Disconnects from the Discord IPC server
     *
     * @see SocketHandler.disconnect
     */
    fun disconnect() {
        socketHandler.disconnect()
    }

    fun sendActivity(activity: DiscordActivity?) {
        val arguments = CommandPacket.SetActivity.Arguments(currentPid, activity)
        firePacketSend(CommandPacket.SetActivity(arguments))
    }

    /**
     * Subscribe to an [Event]
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Event> on(noinline action: (T) -> Unit) {
        subscribers
            .computeIfAbsent(T::class.java) { mutableSetOf() }
            .add(action as (Event) -> Unit)
    }

    /**
     * Send an [Event] to all listeners
     */
    internal fun <T : Event> post(event: T) =
        subscribers[event::class.java]?.forEach { it(event) }

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
    private fun firePacketSend(packet: Packet) {
        val bytes = MessageToByteEncoder.encode(this, packet)
        socketHandler.write(bytes)
    }

    /**
     * Fired when a packet is received
     *
     * @see SocketHandler.read
     */
    internal fun firePacketRead(packet: Packet) {
        when (packet) {
            is CommandPacket.DispatchEvent.Ready -> post(ReadyEvent(packet.data))
            is ErrorPacket -> post(ErrorEvent(ErrorEventData(packet.code, packet.message)))
        }
    }
}