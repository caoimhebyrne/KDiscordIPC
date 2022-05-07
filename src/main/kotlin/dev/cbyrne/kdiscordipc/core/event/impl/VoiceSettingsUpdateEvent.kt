package dev.cbyrne.kdiscordipc.core.event.impl

import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.data.voiceSettings.VoiceSettings

data class VoiceSettingsUpdateEvent(val data: VoiceSettings): Event