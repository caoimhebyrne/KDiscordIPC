package dev.cbyrne.kdiscordipc.user

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.data.User
import dev.cbyrne.kdiscordipc.packet.impl.CommandPacket

class UserManager(private val ipc: KDiscordIPC) {
    private val getUserCallbacks: MutableMap<String, MutableSet<(User) -> Unit>> = mutableMapOf()

    init {
        ipc.on<CommandPacket.GetUser> {
            val data = it.data ?: return@on
            val callbacks = getUserCallbacks[data.id] ?: return@on run {
                ipc.logger.warn("Received a GetUser packet without a corresponding callback: $it")
            }

            callbacks.forEach { it(data) }
            getUserCallbacks.remove(data.id)
        }
    }

    fun getUser(id: String, callback: (User) -> Unit) {
        getUserCallbacks
            .computeIfAbsent(id) { mutableSetOf() }
            .add(callback)

        ipc.firePacketSend(CommandPacket.GetUser(CommandPacket.GetUser.Arguments(id)))
    }
}