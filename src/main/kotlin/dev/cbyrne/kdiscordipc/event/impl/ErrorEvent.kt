package dev.cbyrne.kdiscordipc.event.impl

import dev.cbyrne.kdiscordipc.event.Event
import dev.cbyrne.kdiscordipc.event.data.ErrorEventData

data class ErrorEvent(override val data: ErrorEventData) : Event