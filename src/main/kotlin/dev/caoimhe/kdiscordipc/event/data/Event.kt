package dev.caoimhe.kdiscordipc.event.data

import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData

sealed class Event<T : EventData> {
    /**
     * The data associated with this event
     */
    abstract val data: T

    /**
     * The READY event.
     *
     * See [ReadyEventData]
     */
    data class Ready(
        override val data: ReadyEventData
    ) : Event<ReadyEventData>()
}
