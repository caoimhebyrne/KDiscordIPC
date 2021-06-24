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

import dev.cbyrne.kdiscordipc.gson.toJson
import dev.cbyrne.kdiscordipc.packet.Packet
import java.nio.ByteBuffer
import java.util.*

/**
 * A class which encodes a [Packet] to a [ByteArray]
 * @see [encode]
 */
class PacketToByteArrayEncoder {
    /**
     * Encodes a [Packet] to a [ByteArray]
     * Alongside the [Packet.data] provided, a "nonce" key will be set with a random [UUID]
     *
     * @param packet The packet to encode
     */
    fun encode(packet: Packet): ByteArray {
        val packetData = packet.data.toMutableMap()
        packetData["nonce"] = UUID.randomUUID().toString()

        val data = (packetData.toJson() ?: "null").toByteArray()
        val buffer = ByteBuffer.allocate(data.size + 2 * Integer.BYTES)

        buffer.putInt(Integer.reverseBytes(packet.opcode))
        buffer.putInt(Integer.reverseBytes(data.size))
        buffer.put(data)

        return buffer.array()
    }
}
