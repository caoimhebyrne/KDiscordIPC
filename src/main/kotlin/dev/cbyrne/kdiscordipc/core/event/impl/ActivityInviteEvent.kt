package dev.cbyrne.kdiscordipc.core.event.impl

import dev.cbyrne.kdiscordipc.core.event.Event
import dev.cbyrne.kdiscordipc.core.event.data.ActivityInviteEventData

data class ActivityInviteEvent(val data: ActivityInviteEventData) : Event