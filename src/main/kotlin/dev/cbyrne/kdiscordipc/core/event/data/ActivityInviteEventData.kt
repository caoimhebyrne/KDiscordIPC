package dev.cbyrne.kdiscordipc.core.event.data

import dev.cbyrne.kdiscordipc.data.activity.InboundActivity
import dev.cbyrne.kdiscordipc.data.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityInviteEventData(
    val user: User,
    val activity: InboundActivity,
    val type: Int,
    @SerialName("channel_id")
    val channelId: String,
    @SerialName("message_id")
    val messageId: String
) : EventData()