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

package dev.cbyrne.kdiscordipc.packet.impl.serverbound

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.packet.Packet
import dev.cbyrne.kdiscordipc.presence.DiscordPresence
import java.lang.management.ManagementFactory

/**
 * The packet which will instruct the client to update the user's activity / presence
 *
 * @see DiscordPresence
 * @see DiscordIPC.presence
 */
class SetActivityPacket(val presence: DiscordPresence?) : Packet {
    override val opcode = 0x01

    override fun getData() =
        mapOf(
            "cmd" to "SET_ACTIVITY",
            "args" to mapOf("pid" to getProcessId(), "activity" to presence?.toNativeJson())
        )

    private fun getProcessId() = ManagementFactory.getRuntimeMXBean().name.split("@")[0].toInt()
}
