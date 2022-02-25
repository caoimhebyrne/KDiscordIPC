package dev.cbyrne.kdiscordipc.core.event

import dev.cbyrne.kdiscordipc.core.event.data.EventData

interface Event {
    val data: EventData
}