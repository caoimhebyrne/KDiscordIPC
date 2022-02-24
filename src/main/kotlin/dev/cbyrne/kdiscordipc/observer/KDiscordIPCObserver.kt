package dev.cbyrne.kdiscordipc.observer

import dev.cbyrne.kdiscordipc.packet.impl.command.data.ReadyEventData

// Minecraft reference?
interface KDiscordIPCObserver {
    fun onReady(data: ReadyEventData)
}