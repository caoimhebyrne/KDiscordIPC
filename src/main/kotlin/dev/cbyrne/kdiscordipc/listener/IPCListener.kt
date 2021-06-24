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

package dev.cbyrne.kdiscordipc.listener

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.event.DiscordEvent
import dev.cbyrne.kdiscordipc.packet.Packet

/**
 * A listener used to listen for events dispatched by [DiscordIPC]
 * @see [DiscordIPC.onPacket]
 */
interface IPCListener {
    /**
     * Fired when [DiscordEvent.Ready] is received from the client
     * @param event A class containing all relevant info: user info, config, environment, etc.
     */
    fun onReadyEvent(event: DiscordEvent.Ready) {}

    /**
     * Fired when we are disconnected from the client
     */
    fun onDisconnect(reason: String?) {}

    /**
     * Fired when a packet is received from the client
     */
    fun onPacket(packet: Packet) {}
}