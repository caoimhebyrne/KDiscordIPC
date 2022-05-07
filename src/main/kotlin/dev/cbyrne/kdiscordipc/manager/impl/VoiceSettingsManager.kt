package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.DiscordEvent
import dev.cbyrne.kdiscordipc.core.event.impl.VoiceSettingsUpdateEvent
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.GetVoiceSettingsPacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.SetVoiceSettingsPacket
import dev.cbyrne.kdiscordipc.data.voiceSettings.VoiceSettings
import dev.cbyrne.kdiscordipc.manager.Manager
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.GetVoiceSettingsPacket as InboundGetVoiceSettingsPacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.SetVoiceSettingsPacket as InboundSetVoiceSettingsPacket

class VoiceSettingsManager(override val ipc: KDiscordIPC) : Manager() {
    var currentVoiceSettings: VoiceSettings? = null
        private set

    override suspend fun init() {
        ipc.on<VoiceSettingsUpdateEvent> {
            currentVoiceSettings = this.data
        }
    }

    suspend fun getVoiceSettings(): VoiceSettings {
        val response: InboundGetVoiceSettingsPacket = ipc.sendPacket(GetVoiceSettingsPacket())
        return response.data
    }

    suspend fun setVoiceSettings(settings: SetVoiceSettingsPacket.VoiceSettingArguments) {
        ipc.sendPacket<InboundSetVoiceSettingsPacket>(SetVoiceSettingsPacket(args = settings))
    }

    suspend fun subscribeToVoiceSettingsUpdate() = ipc.subscribe(DiscordEvent.VoiceSettingsUpdate)
}