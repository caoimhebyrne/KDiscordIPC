/*
 *     KDiscordIPC is a library for interacting with the discord client via IPC
 *     Copyright (C) 2021  Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.event.DiscordEvent
import dev.cbyrne.kdiscordipc.listener.IPCListener
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.impl.SetActivityPacket
import dev.cbyrne.kdiscordipc.presence.presence

object IPCTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val ipc = DiscordIPC("857191688454537226")

        // When setting the presence before you connect, the presence will be set as soon as you connect!
        ipc.presence = presence {
            state = "Testing..."
            details = "Hello world"
            startTimestamp = System.currentTimeMillis()
            endTimestamp = System.currentTimeMillis() + 30000
        }

        ipc.listener = object : IPCListener {
            /**
             * Fired when [DiscordEvent.Ready] is received by the client
             * @param event A class containing all relevant info: user info, config, environment, etc.
             */
            override fun onReadyEvent(event: DiscordEvent.Ready) {
                println("[IPCTest] Ready! User: ${event.user}")
                // You could also set the presence in here if you wish
            }

            /**
             * Fired when a packet is received from the client
             */
            override fun onPacket(packet: Packet) {
                if (packet is SetActivityPacket) {
                    println("[IPCTest] Received echoed SetActivity packet: ${packet.presence}")
                }
            }
        }

        ipc.connect()
    }
}
