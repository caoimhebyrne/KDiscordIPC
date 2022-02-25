package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.user.User
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

class UserManager(override val ipc: KDiscordIPC) : Manager() {
    private val _events = MutableSharedFlow<Packet>()

    override suspend fun init() {
        ipc.on<CommandPacket.GetUser> { _events.emit(this) }
    }

    suspend fun getUser(id: String): User {
        ipc.firePacketSend(CommandPacket.GetUser(CommandPacket.GetUser.Arguments(id)))

        return _events
            .filterIsInstance<CommandPacket.GetUser>()
            .mapNotNull { it.data }
            .first { it.id == id }
    }
}