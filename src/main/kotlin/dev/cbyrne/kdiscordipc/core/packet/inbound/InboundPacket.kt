package dev.cbyrne.kdiscordipc.core.packet.inbound

import dev.cbyrne.kdiscordipc.core.packet.serialization.InboundPacketSerializer
import kotlinx.serialization.Serializable

@Serializable(with = InboundPacketSerializer::class)
abstract class InboundPacket {
    abstract val opcode: Int

    abstract val cmd: String?

    abstract val data: Data?

    abstract val nonce: String?

    @Serializable
    open class Data
}
