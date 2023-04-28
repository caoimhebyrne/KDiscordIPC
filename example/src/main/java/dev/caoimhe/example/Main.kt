package dev.caoimhe.example

import dev.caoimhe.kdiscordipc.KDiscordIPC

suspend fun main() {
    val ipc = KDiscordIPC("1100847786448146505")
    ipc.connect()
}
