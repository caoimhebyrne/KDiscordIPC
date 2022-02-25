package dev.cbyrne.kdiscordipc.user

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.data.User
import dev.cbyrne.kdiscordipc.packet.impl.CommandPacket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class UserManager(private val ipc: KDiscordIPC) {
    private val _events = MutableSharedFlow<User>()

    internal suspend fun init() {
        ipc.on<CommandPacket.GetUser> {
            data?.let { _events.emit(data) }
        }
    }

    suspend fun getUser(id: String): User {
        ipc.firePacketSend(CommandPacket.GetUser(CommandPacket.GetUser.Arguments(id)))
        return _events.first { it.id == id }
    }
}