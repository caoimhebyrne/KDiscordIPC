package dev.cbyrne.kdiscordipc.core.packet.outbound.impl

import dev.cbyrne.kdiscordipc.core.packet.outbound.OutboundPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcceptActivityInvitePacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "ACCEPT_ACTIVITY_INVITE",
    override val args: Arguments,
    override var nonce: String = "0"
) : OutboundPacket() {
    constructor(
        channelId: String,
        messageId: String,
        sessionId: String,
        userId: String,
        type: Int
    ) : this(args = Arguments(channelId, messageId, sessionId, userId, type))

    @Serializable
    data class Arguments(
        @SerialName("channel_id")
        val channelId: String,
        @SerialName("message_id")
        val messageId: String,
        @SerialName("session_id")
        val sessionId: String,
        @SerialName("user_id")
        val userId: String,
        val type: Int
    ) : OutboundPacket.Arguments()
}