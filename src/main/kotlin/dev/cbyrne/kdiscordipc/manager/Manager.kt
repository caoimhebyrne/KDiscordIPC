package dev.cbyrne.kdiscordipc.manager

import dev.cbyrne.kdiscordipc.KDiscordIPC

abstract class Manager() {
    abstract val ipc: KDiscordIPC
    internal abstract suspend fun init()
}