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

package dev.cbyrne.kdiscordipc.event

import dev.cbyrne.kdiscordipc.DiscordIPC
import dev.cbyrne.kdiscordipc.gson.fromJson
import dev.cbyrne.kdiscordipc.gson.toJson
import dev.cbyrne.kdiscordipc.objects.DiscordConfig
import dev.cbyrne.kdiscordipc.objects.DiscordUser
import dev.cbyrne.kdiscordipc.packet.impl.DispatchPacket
import dev.cbyrne.kdiscordipc.packet.impl.serverbound.HandshakePacket

/**
 * A class representing all the events which can be received and dispatched
 *
 * @see DiscordIPC.onPacket
 * @see DispatchPacket
 */
sealed class DiscordEvent {
    companion object {
        fun from(name: String, data: Map<String, Any>): DiscordEvent {
            return when (name) {
                "READY" -> {
                    val gatewayVersion = data["v"] as Double?
                    val user: DiscordUser? = data["user"]?.toJson().fromJson()
                    val config: DiscordConfig? = data["config"]?.toJson().fromJson()

                    if (gatewayVersion == null || user == null || config == null)
                        throw IllegalStateException("Failed to parse ready event with data $data")

                    Ready(gatewayVersion.toInt(), user, config)
                }
                else -> throw IllegalStateException("Unknown event: $name! Data: $data")
            }
        }
    }

    /**
     * A class representing the READY event which is sent by the client after we send [HandshakePacket]
     *
     * @see DiscordIPC.connect
     * @see HandshakePacket
     */
    data class Ready(val gatewayVersion: Int, val user: DiscordUser, val config: DiscordConfig) : DiscordEvent()
}
