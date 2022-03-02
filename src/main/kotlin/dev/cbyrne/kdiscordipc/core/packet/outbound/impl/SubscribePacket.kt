package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class SubscribePacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "SUBSCRIBE",
    override val args: Arguments = Arguments(),
    override var nonce: String = "0",
    val evt: String
) : OutboundPacket() {
    constructor(name: DiscordEvent) : this(evt = name.eventName)

    @Serializable
    class Arguments : OutboundPacket.Arguments()
}