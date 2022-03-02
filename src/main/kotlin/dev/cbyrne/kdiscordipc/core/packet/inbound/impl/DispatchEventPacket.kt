package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.event.data.ActivityInviteEventData
import dev.cbyrne.kdiscordipc.core.event.data.ActivityJoinEventData
import dev.cbyrne.kdiscordipc.core.event.data.EventData
import dev.cbyrne.kdiscordipc.core.event.data.ReadyEventData
import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.data.user.User
import kotlinx.serialization.Serializable

@Serializable
abstract class DispatchEventPacket(
    override val opcode: Int = 0x01,
    override val cmd: String = "DISPATCH",
    override val nonce: String? = null
) : CommandPacket() {
    abstract override val data: EventData

    @Serializable
    data class Ready(
        override val data: ReadyEventData
    ) : DispatchEventPacket()

    @Serializable
    data class UserUpdate(
        override val data: User
    ) : DispatchEventPacket()

    @Serializable
    data class ActivityJoin(
        override val data: ActivityJoinEventData
    ) : DispatchEventPacket()

    @Serializable
    data class ActivityInvite(
        override val data: ActivityInviteEventData
    ) : DispatchEventPacket()
}
