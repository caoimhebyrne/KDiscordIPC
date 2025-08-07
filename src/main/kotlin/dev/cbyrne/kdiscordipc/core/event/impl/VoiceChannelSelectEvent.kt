package dev.cbyrne.kdiscordipc.core.event.impl

import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.core.event.data.VoiceChannelSelectEventData

data class VoiceChannelSelectEvent(val data: VoiceChannelSelectEventData): Event