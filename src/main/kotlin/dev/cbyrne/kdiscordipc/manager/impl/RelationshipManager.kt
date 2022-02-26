package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.relationship.Relationship
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

class RelationshipManager(override val ipc: KDiscordIPC) : Manager() {
    suspend fun getRelationships(): List<Relationship> {
        ipc.firePacketSend(CommandPacket.GetRelationships())

        return ipc.packets
            .filterIsInstance<CommandPacket.GetRelationships>()
            .mapNotNull { it.data }
            .first()
            .relationships
    }
}