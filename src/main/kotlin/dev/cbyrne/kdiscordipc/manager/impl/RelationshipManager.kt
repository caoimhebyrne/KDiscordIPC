package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.GetRelationshipsPacket
import dev.cbyrne.kdiscordipc.data.relationship.Relationship
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.GetRelationshipsPacket as InboundGetRelationshipsPacket

class RelationshipManager(override val ipc: KDiscordIPC) : Manager() {
    suspend fun getRelationships(): List<Relationship> {
        ipc.firePacketSend(GetRelationshipsPacket())

        return ipc.packets
            .filterIsInstance<InboundGetRelationshipsPacket>()
            .mapNotNull { it.data }
            .first()
            .relationships
    }
}