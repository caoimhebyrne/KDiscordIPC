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

package dev.cbyrne.kdiscordipc.packet.pipeline

import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.packet.RawPacket
import dev.cbyrne.kdiscordipc.packet.impl.DispatchPacket

/**
 * A class which converts a [RawPacket] to a [Packet]
 * @see [decode]
 */
class RawPacketToPacketDecoder {
    /**
     * Converts a [RawPacket] to a [Packet]
     * @returns a [Packet] instance if there is a wrapper available for this [RawPacket], otherwise null
     */
    fun decode(packet: RawPacket): Packet? {
        return when (packet.opcode) {
            1 -> DispatchPacket(packet.data)
            else -> null
        }
    }
}