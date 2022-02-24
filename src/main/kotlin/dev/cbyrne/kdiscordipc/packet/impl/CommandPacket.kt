package dev.cbyrne.kdiscordipc.packet.impl

import dev.cbyrne.kdiscordipc.activity.DiscordActivity
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.impl.command.data.DispatchEventData
import dev.cbyrne.kdiscordipc.packet.impl.command.data.ReadyEventData
import dev.cbyrne.kdiscordipc.packet.serialization.CommandPacketSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = CommandPacketSerializer::class)
sealed class CommandPacket : Packet {
    override val opcode = 0x01
    abstract val command: String
    abstract val nonce: String?

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
            val activity: DiscordActivity?
        )
    }

    @Serializable
    sealed class DispatchEvent : CommandPacket() {
        @SerialName("cmd")
        override val command = "DISPATCH"

        abstract val data: DispatchEventData
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

