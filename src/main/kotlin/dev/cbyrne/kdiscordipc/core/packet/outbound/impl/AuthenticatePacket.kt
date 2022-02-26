package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatePacket(
    override val args: Arguments = Arguments(),
    override val cmd: String = "AUTHENTICATE",
    override val opcode: Int = 0x01,
    override var nonce: String = "0"
) : CommandPacket() {
    @Serializable
    class Arguments : OutboundPacket.Arguments()
}
