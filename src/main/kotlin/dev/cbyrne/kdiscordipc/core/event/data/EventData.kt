package dev.cbyrne.kdiscordipc.core.event.data

import dev.cbyrne.kdiscordipc.core.packet.inbound.InboundPacket
import kotlinx.serialization.Serializable

@Serializable
abstract class EventData : InboundPacket.Data()