package dev.caoimhe.kdiscordipc.event.data

import dev.caoimhe.kdiscordipc.event.data.impl.ReadyEventData
import dev.caoimhe.kdiscordipc.models.user.User

sealed class Event<T>(
    val id: String,
    val subscribed: Boolean
) {
    /**
     * Fired when we connect to the Discord client for the first time
     */
    object Ready : Event<ReadyEventData>(
        id = "READY",
        subscribed = false
    )

    object CurrentUserUpdate : Event<User>(
        id = "CURRENT_USER_UPDATE",
        subscribed = true
    )
}

//enum class Event(val id: String, val subscribed: Boolean, val data: KClass<out EventData>) {
//    Ready("READY", false, ReadyEventData::class),
//    CurrentUserUpdate("CURRENT_USER_UPDATE", true, CurrentUserUpdateEventData::class)
//}

//sealed class Event<T : EventData> {
//    /**
//     * The data associated with this event
//     */
//    abstract val data: T
//
//    /**
//     * The READY event.
//     *
//     * See [ReadyEventData]
//     */
//    data class Ready(
//        override val data: ReadyEventData
//    ) : Event<ReadyEventData>()
//}
