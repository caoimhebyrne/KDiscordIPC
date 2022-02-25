@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.core.util.currentPid
import dev.cbyrne.kdiscordipc.data.activity.Activity
import dev.cbyrne.kdiscordipc.manager.Manager

class ActivityManager(override val ipc: KDiscordIPC) : Manager() {
    override suspend fun init() {
        ipc.on<ReadyEvent> {
            activity?.let { sendActivity(activity) }
        }
    }

    var activity: Activity? = null
        set(value) {
            field = value
            if (ipc.connected) sendActivity(value)
        }

    fun clearActivity() {
        activity = null
    }

    private fun sendActivity(activity: Activity?) {
        val arguments = CommandPacket.SetActivity.Arguments(currentPid, activity)
        ipc.firePacketSend(CommandPacket.SetActivity(arguments))
    }
}