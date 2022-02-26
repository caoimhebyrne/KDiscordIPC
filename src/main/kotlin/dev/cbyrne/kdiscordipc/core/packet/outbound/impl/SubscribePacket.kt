package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class SubscribePacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "SUBSCRIBE",
    override val args: Arguments = Arguments(),
    override val nonce: String = "0",
    val evt: String
) : OutboundPacket() {
    constructor(name: String) : this(evt = name)

    @Serializable
    class Arguments : OutboundPacket.Arguments()
}