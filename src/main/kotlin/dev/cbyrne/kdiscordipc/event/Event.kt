package dev.cbyrne.kdiscordipc.event

import dev.cbyrne.kdiscordipc.event.data.EventData

interface Event {
    val data: EventData
}