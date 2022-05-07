package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatePacket(
    override val args: Arguments,
    override val cmd: String = "AUTHENTICATE",
    override val opcode: Int = 0x01,
    override var nonce: String = "0"
) : CommandPacket() {
    constructor(token: String?) : this(args = Arguments(token))

    @Serializable
    data class Arguments(
        @SerialName("access_token") val token: String?
    ) : OutboundPacket.Arguments()
}
