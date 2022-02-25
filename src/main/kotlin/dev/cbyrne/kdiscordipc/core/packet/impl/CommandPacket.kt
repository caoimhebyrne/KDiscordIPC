package dev.cbyrne.kdiscordipc.core.packet.impl

import dev.cbyrne.kdiscordipc.core.event.data.EventData
import dev.cbyrne.kdiscordipc.core.event.data.ReadyEventData
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.packet.serialization.CommandPacketSerializer
import dev.cbyrne.kdiscordipc.data.activity.Activity
import dev.cbyrne.kdiscordipc.data.authentication.AuthenticationResponse
import dev.cbyrne.kdiscordipc.data.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = CommandPacketSerializer::class)
sealed class CommandPacket : Packet {
    override val opcode = 0x01
    abstract val command: String
    abstract val nonce: String?

    @Serializable
    data class Authenticate(
        val data: AuthenticationResponse? = null,
        @SerialName("cmd")
        override val command: String = "AUTHENTICATE",
        override val nonce: String = "0",
    ) : CommandPacket()

    @Serializable
    data class GetUser(
        @SerialName("args")
        val arguments: Arguments? = null,
        val data: User? = null,
        @SerialName("cmd")
        override val command: String = "GET_USER",
        override val nonce: String = "0",
    ) : CommandPacket() {
        @Serializable
        data class Arguments(
            val id: String
        )
    }

    @Serializable
    data class SetActivity(
        @SerialName("args")
        val arguments: Arguments? = null,
        @SerialName("cmd")
        override val command: String = "SET_ACTIVITY",
        override val nonce: String = "0",
    ) : CommandPacket() {
        @Serializable
        data class Arguments(
            val pid: Long,
            val activity: Activity?
        )
    }

    @Serializable
    sealed class DispatchEvent : CommandPacket() {
        @SerialName("cmd")
        override val command = "DISPATCH"

        abstract val data: EventData
        abstract val event: String?

        @Serializable
        data class Ready(
            @SerialName("evt")
            override val event: String,
            override val data: ReadyEventData,
            override val nonce: String?
        ) : DispatchEvent()
    }
}

