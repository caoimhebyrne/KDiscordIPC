package dev.cbyrne.kdiscordipc.core.packet.inbound.impl

import dev.cbyrne.kdiscordipc.core.packet.inbound.CommandPacket
import dev.cbyrne.kdiscordipc.data.voiceSettings.VoiceSettings
import kotlinx.serialization.Serializable

@Serializable
data class GetVoiceSettingsPacket(
    override val data: VoiceSettings,
    override val cmd: String = "GET_VOICE_SETTINGS",
    override val opcode: Int = 0x01,
    override val nonce: String = "0"
) : CommandPacket()

@Serializable
data class SetVoiceSettingsPacket(
    override val data: VoiceSettings,
    override val cmd: String? = "SET_VOICE_SETTINGS",
    override val opcode: Int = 0x01,
    override val nonce: String? = "0"
): CommandPacket()