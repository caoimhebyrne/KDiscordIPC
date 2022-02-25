package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.packet.Packet
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.data.authentication.AuthenticationResponse
import dev.cbyrne.kdiscordipc.manager.Manager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

class ApplicationManager(override val ipc: KDiscordIPC) : Manager() {
    private val _events = MutableSharedFlow<Packet>()

    override suspend fun init() {
        ipc.on<CommandPacket.Authenticate> { _events.emit(this) }
    }

    suspend fun getOAuthToken(): AuthenticationResponse {
        ipc.firePacketSend(CommandPacket.Authenticate())

        return _events
            .filterIsInstance<CommandPacket.Authenticate>()
            .mapNotNull { it.data }
            .first()
    }
}