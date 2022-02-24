package dev.cbyrne.kdiscordipc.event.impl

import dev.cbyrne.kdiscordipc.event.Event
import dev.cbyrne.kdiscordipc.event.data.ReadyEventData

data class ReadyEvent(override val data: ReadyEventData) : Event