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

package dev.cbyrne.kdiscordipc.socket

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.pipeline.ByteArrayToRawPacketDecoder
import dev.cbyrne.kdiscordipc.packet.pipeline.RawPacketToPacketDecoder

/**
 * A listener used to handle packets which have been decoded by [DiscordSocket]
 *
 * @see ByteArrayToRawPacketDecoder
 * @see RawPacketToPacketDecoder
 */
interface SocketListener {
    /**
     * Fired when a packet is received and decoded by [DiscordSocket]
     *
     * @param packet The packet instance
     * @see DiscordIPC
     */
    fun onPacket(packet: Packet)

    /**
     * Fired when the socket is closed
     *
     * @param message The reason for the socket being closed
     */
    fun onSocketClosed(message: String)
}