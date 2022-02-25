@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.manager.impl

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.core.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.core.util.currentPid
import dev.cbyrne.kdiscordipc.data.DiscordActivity

class ActivityManager(private val ipc: KDiscordIPC) {
    internal suspend fun init() {
        ipc.on<ReadyEvent> {
            activity?.let { sendActivity(activity) }
        }
    }

    var activity: DiscordActivity? = null
        set(value) {
            field = value
            if (ipc.connected) sendActivity(value)
        }

    fun clearActivity() {
        activity = null
    }

    private fun sendActivity(activity: DiscordActivity?) {
        val arguments = CommandPacket.SetActivity.Arguments(currentPid, activity)
        ipc.firePacketSend(CommandPacket.SetActivity(arguments))
    }
}