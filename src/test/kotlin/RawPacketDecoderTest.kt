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

import dev.cbyrne.kdiscordipc.packet.pipeline.ByteArrayToRawPacketDecoder

object RawPacketDecoderTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val decoder = ByteArrayToRawPacketDecoder()
        val packet = decoder.decode(byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x08))

        if (packet.opcode != 0x01020304)
            throw IllegalStateException("Expected ${0x01020304} but received ${packet.opcode}")
    }
}
