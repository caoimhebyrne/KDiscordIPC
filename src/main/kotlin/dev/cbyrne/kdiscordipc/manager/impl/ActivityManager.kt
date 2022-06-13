@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.data.ActivityInviteEventData
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.AcceptActivityInvitePacket
import dev.cbyrne.kdiscordipc.core.packet.outbound.impl.SetActivityPacket
import dev.cbyrne.kdiscordipc.core.util.currentPid
import dev.cbyrne.kdiscordipc.data.activity.Activity
import dev.cbyrne.kdiscordipc.data.activity.activity
import dev.cbyrne.kdiscordipc.manager.Manager
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.AcceptActivityInvitePacket as InboundAcceptActivityInvitePacket
import dev.cbyrne.kdiscordipc.core.packet.inbound.impl.SetActivityPacket as InboundSetActivityPacket

/**
 * This manager allows you to set the current user's activity (a.k.a. rich presence)
 */
class ActivityManager(override val ipc: KDiscordIPC) : Manager() {
    var activity: Activity? = null
        private set

    /**
     * Sets a user's presence in Discord to a new activity. This has a rate limit of 5 updates per 20 seconds.
     */
    suspend fun setActivity(activity: Activity?) {
        // TODO: Verify that the response had no errors
        ipc.sendPacket<InboundSetActivityPacket>(SetActivityPacket(currentPid, activity))
        this.activity = activity
    }

    /**
     * Sets a user's presence in Discord to a new activity. This has a rate limit of 5 updates per 20 seconds.
     */
    suspend fun setActivity(details: String, state: String, init: Activity.() -> Unit) =
        setActivity(activity(details, state, init))

    suspend fun acceptInvite(data: ActivityInviteEventData): Boolean {
        val result = ipc.sendPacket<InboundAcceptActivityInvitePacket>(
            AcceptActivityInvitePacket(
                data.channelId,
                data.messageId,
                data.activity.sessionId,
                data.user.id,
                data.type
            )
        )

        return result.data != null
    }

    /**
     * Clear's a user's presence in Discord to make it show nothing.
     */
    suspend fun clearActivity() {
        setActivity(null)
    }
}