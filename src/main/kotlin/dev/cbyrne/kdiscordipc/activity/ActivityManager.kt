@file:Suppress("unused")

package dev.cbyrne.kdiscordipc.activity

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.packet.impl.CommandPacket
import dev.cbyrne.kdiscordipc.util.currentPid

class ActivityManager(private val ipc: KDiscordIPC) {
    init {
        ipc.on<ReadyEvent> {
            activity?.let { sendActivity(activity) }
        }
    }

    var activity: DiscordActivity? = null
        set(value) {
            field = value

            if (ipc.connected)
                sendActivity(value)
        }

    fun clearActivity() {
        activity = null
    }

    private fun sendActivity(activity: DiscordActivity?) {
        val arguments = CommandPacket.SetActivity.Arguments(currentPid, activity)
        ipc.firePacketSend(CommandPacket.SetActivity(arguments))
    }
}