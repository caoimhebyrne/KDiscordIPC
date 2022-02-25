package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.User
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class UserManager(override val ipc: KDiscordIPC) : Manager() {
    private val _events = MutableSharedFlow<User>()

    override suspend fun init() {
        ipc.on<CommandPacket.GetUser> {
            data?.let { _events.emit(data) }
        }
    }

    suspend fun getUser(id: String): User {
        ipc.firePacketSend(CommandPacket.GetUser(CommandPacket.GetUser.Arguments(id)))
        return _events.first { it.id == id }
    }
}