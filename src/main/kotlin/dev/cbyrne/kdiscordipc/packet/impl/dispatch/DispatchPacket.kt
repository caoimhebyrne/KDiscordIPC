@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.packet.impl.dispatch

import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.impl.dispatch.data.DispatchPacketData
import dev.cbyrne.kdiscordipc.packet.impl.dispatch.data.ReadyEventData
import dev.cbyrne.kdiscordipc.packet.serialization.DispatchPacketSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = DispatchPacketSerializer::class)
sealed class DispatchPacket : Packet {
    override val opcode: Int = 0x01
    val command: String = "DISPATCH"

    abstract val data: DispatchPacketData
    abstract val event: String?
    abstract val nonce: String?

    @Serializable
    data class ReadyEvent(
        @SerialName("evt")
        override val event: String,
        override val data: ReadyEventData,
        override val nonce: String?
    ) : DispatchPacket()
}
